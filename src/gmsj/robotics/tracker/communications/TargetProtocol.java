package gmsj.robotics.tracker.communications;

import java.util.StringTokenizer;

import gmsj.robotics.tracker.controler.TargetBroker;
import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.model.DataType;
import gmsj.robotics.tracker.model.ITargetAttribute;
import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.model.TargetAttribute;
import gmsj.robotics.tracker.model.TargetAttributeGroup;
import gmsj.robotics.tracker.model.TargetSubAttribute;

public class TargetProtocol extends TargetProtocolConstants {

	private Target 					target ;
	private AttributeEvent 			event ;
	private TargetBroker 			broker ;
	private StringBuilder			currentString ;
	private int 					lastSeqNum ;
	private StringBuilder 			seqNumString = new StringBuilder ( 20 ) ;
	private enum RxState {
		WAITING_FOR_SOM, WAITING_FOR_MSG_TYPE , WAITING_FOR_DISCO_EOM, WAITING_FOR_UPDATE_TT, WAITING_FOR_UPDATE_ID, WAITING_FOR_UPDATE_VALUE, WAITING_FOR_TELNET_EOM , WAITING_FOR_REPROG_ACK_EOM
	} 
	private RxState 				rxState ;

	public TargetProtocol () {

		this.currentString 		= new StringBuilder ( 20 ) ;
		this.rxState 			= RxState.WAITING_FOR_SOM ;
		this.event				= new AttributeEvent () ;
		this.lastSeqNum			= 0 ;
	}
	public void setTarget ( Target target ) {		
		this.target = target ;
		this.broker	= target.getBroker() ;
	}
	public void inputReceived ( byte[] buffer, int length ) {
		int offset = 0 ;

		// Do we have a packet sequence number ?
		if ( buffer [ offset ] == _SEQNUM_START ) {
			seqNumString.setLength( 0 ) ;
			while ( buffer [ ++ offset ] != _SEQNUM_END ) {
				seqNumString.append ( (char) buffer [ offset ] ) ;
			}
			offset ++ ;	
			
			int rxSeqNum = Integer.parseInt ( seqNumString.toString () ) ;
			if ( lastSeqNum != rxSeqNum ) {
				System.err.println ( "TargetProtocol::inputReceived: OUT OF SEQUENCE PACKET :" + rxSeqNum + "expected:" + lastSeqNum ) ;
				rxState = RxState.WAITING_FOR_SOM ;
			}
			lastSeqNum = rxSeqNum + 1 ;
		}
		while ( offset < length ) {

			switch ( rxState )
			{
			case WAITING_FOR_SOM :
				while ( offset < length ) {
					if ( buffer [ offset ++ ] == _SOM ) {
						rxState = RxState.WAITING_FOR_MSG_TYPE ;
						break;
					}	
				}	
				break;

			case WAITING_FOR_MSG_TYPE :
				if ( buffer [ offset ] == _UPDATE ) {
					rxState = RxState.WAITING_FOR_UPDATE_TT ;
				}
				else if ( buffer [ offset ] == _DISCOVER ) {
					rxState = RxState.WAITING_FOR_DISCO_EOM ;
				}
				else if ( buffer [ offset ] == _SOFTWARE_REPROG_ACK ) {
					rxState = RxState.WAITING_FOR_REPROG_ACK_EOM ;
				}
				else if ( buffer [ offset ] == _TELNET ) {
					// TODO : TO BE CODED
					offset = parseTelnetMessage (  buffer , offset + 1 , length ) ;
				}
				else {
					rxState = RxState.WAITING_FOR_SOM ;
					System.err.println ( "TargetProtocol::inputReceived: ERROR WAITING_FOR_MSG_TYPE" ) ;
				}
				currentString.setLength ( 0 ) ;
				offset ++ ;
				break;

			case WAITING_FOR_DISCO_EOM :
				while ( offset < length ) {
					if ( buffer [ offset ] == _EOM ) {
						rxState = RxState.WAITING_FOR_SOM ;
						
						target.discoveryMsgReceived () ;
						parseDiscoverMessage ( currentString.toString() ) ;
						target.discoveryMsgProcessed () ;

						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else if ( buffer [ offset ] == _SOM ) {
						// oops, problem..forget everything received yet and start over
						rxState = RxState.WAITING_FOR_MSG_TYPE ;
						System.err.println ( "TargetProtocol::inputReceived: ERROR WAITING_FOR_DISCO_EOM" ) ;
						break;
					}
					else {
						currentString.append ( (char) buffer [ offset ] ) ;
					}
					offset ++ ;
				}
				break;

			case WAITING_FOR_UPDATE_TT :
				while ( offset < length ) {
					if ( buffer [ offset ] == _TIMETAG_SEPARATOR ) {
						rxState = RxState.WAITING_FOR_UPDATE_ID ;
						try {
							target.updateTargetTime ( Long.parseLong ( currentString.toString () ) ) ;
						} 
						catch ( NumberFormatException e ) {
							System.err.println ( "TargetProtocol::inputReceived: ERROR DECODING TIME_TAG: " + currentString.toString () ) ;
							target.updateTargetTime ( 0 ) ;
						}
						event.setTimeTag ( target.getTargetTime () ) ;
						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else if ( buffer [ offset ] == _SEQNUM_SEPARATOR ) {
						rxState = RxState.WAITING_FOR_UPDATE_ID ;
						try {
							long timeDiff = (long) (Long.parseLong ( currentString.toString () ) - target.getTargetTime ().longValue()) ;
							if ( timeDiff != 1 ) {
								System.err.println("TargetProtocol: out of sequence update message received (" + timeDiff + "): expected:" + target.getTargetTime ().longValue() +"+1") ;
							}
							target.updateTargetTime ( Long.parseLong ( currentString.toString () ) - target.getTargetTime ().longValue() ) ;
						} 
						catch ( NumberFormatException e ) {
							System.err.println ( "TargetProtocol::inputReceived: ERROR DECODING SEQ_NUM:" + currentString.toString () ) ;
							target.updateTargetTime ( 0 ) ;
						}
						event.setTimeTag ( target.getTargetTime () ) ;
						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else {
						currentString.append ( (char) buffer [ offset ] ) ;
					}
					offset ++ ;
				}
				break;

			case WAITING_FOR_UPDATE_ID :
				while ( offset < length ) {
					if ( buffer [ offset ] == _VALUE_SEPARATOR ) {
						rxState = RxState.WAITING_FOR_UPDATE_VALUE ;
						if ( ! event.setAttributeId ( currentString.toString () ) ) {
							rxState = RxState.WAITING_FOR_SOM ;
						}
						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else {
						currentString.append ( (char) buffer [ offset ] ) ;
					}
					if ( currentString.length() > 2 ) {
						System.err.println ( "TargetProtocol::inputReceived: ERROR WAITING_FOR_MSG_ID" ) ;
						currentString.setLength ( 0 ) ;
						rxState = RxState.WAITING_FOR_SOM ;
					}
					offset ++ ;
				}
				break;
				
			case WAITING_FOR_UPDATE_VALUE :
				while ( offset < length ) {
					if ( buffer [ offset ] == _ATTRIBUTES_SEPARATOR ) {
						if (event.setValue ( currentString.toString () ) ) {
							broker.attributeNotification ( event ) ;
							rxState = RxState.WAITING_FOR_UPDATE_ID ;
						}
						else {
							rxState = RxState.WAITING_FOR_SOM ;
						}
						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else if ( buffer [ offset ] == _EOM ) {
						rxState = RxState.WAITING_FOR_SOM ;
						if (event.setValue ( currentString.toString () ) ) {
							broker.attributeNotification ( event ) ;
						}
						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else {
						currentString.append ( (char) buffer [ offset ] ) ;
						offset ++ ;
					}
					if ( currentString.length() > 20 ) {
						System.err.println ( "TargetProtocol::inputReceived: ERROR WAITING_FOR_MSG_VALUE:" + currentString ) ;
						currentString.setLength ( 0 ) ;
						rxState = RxState.WAITING_FOR_SOM ;
					}
				}
				break;
			case WAITING_FOR_REPROG_ACK_EOM :	
				while ( offset < length ) {
					if ( buffer [ offset ] == _EOM ) {
						rxState = RxState.WAITING_FOR_SOM ;
						
						target.downloadAckReceived () ;

						currentString.setLength ( 0 ) ;
						offset ++ ;
						break;
					}
					else if ( buffer [ offset ] == _SOM ) {
						// oops, problem..forget everything received yet and start over
						rxState = RxState.WAITING_FOR_MSG_TYPE ;
						System.err.println ( "TargetProtocol::inputReceived: ERROR WAITING_FOR_REPROG_ACK_EOM" ) ;
						break;
					}
					else {
						currentString.append ( (char) buffer [ offset ] ) ;
					}
					offset ++ ;
				}
				break;
			case WAITING_FOR_TELNET_EOM :	
				//todo
				break;
			}
		}
	}

	private void parseDiscoverMessage ( String message ) {

		try {
			// Get the target name
			StringTokenizer tokenizer = new StringTokenizer ( message, TARGET_NAME_SEPARATOR, false ) ; 
			target.setName ( tokenizer.nextToken () ) ;
			this.broker	= target.getBroker() ;

			// Do we have a time tag ?		
			StringTokenizer tokenizer2 = new StringTokenizer ( tokenizer.nextToken () , TIMETAG_SEPARATOR +_SEQNUM_SEPARATOR, false ) ; 		
			if ( tokenizer2.countTokens () > 1 ) {
				target.setTargetTime ( Long.parseLong ( tokenizer2.nextToken () ) );
			}
			String next = tokenizer2.nextToken () ;
			while ( tokenizer2.hasMoreTokens() ) next += TIMETAG_SEPARATOR + tokenizer2.nextToken () ;
			parseAttributes ( next ) ;
		}
		catch ( Exception e ) {
			System.err.println ("parseDiscoverMessage (" + message + "), Exception:" + e.getMessage () ) ;
		}
	}
	private void parseAttributes ( String message ) {

		StringTokenizer tokenizer = new StringTokenizer ( message, ATTRIBUTES_SEPARATOR, false ) ; 

		while( tokenizer.hasMoreTokens () ) { 
			String attributeMessage = tokenizer.nextToken () ;
			if ( attributeMessage.indexOf ( MULTI_ATTRIBUTE_SEPARATOR ) != -1 ) {
				TargetAttributeGroup attributeGroup  = new TargetAttributeGroup () ;
				parseMultiAttribute ( attributeGroup , attributeMessage ) ;
				if ( attributeGroup.getId () != null ) {
					target.addTargetAttributeGroup ( attributeGroup ) ;
				}
				else {
					System.err.println ( "TargetProtocol::parseAttributes: ERROR parsing attributeGroup" ) ;
				}
			}
			else {
				TargetAttribute attribute = new TargetAttribute () ;
				parseAttribute ( attribute , attributeMessage ) ;
				if ( attribute.getId () != null ) {
					target.addTargetAttribute ( attribute ) ;
				}	
				else {
					System.err.println ( "TargetProtocol::parseAttributes: ERROR parsing attribute" ) ;
				}
			}
		}
	}
	private void parseMultiAttribute ( TargetAttributeGroup targetAttribute , String message ) {

		StringTokenizer tokenizer = new StringTokenizer( message, MULTI_ATTRIBUTE_SEPARATOR, false ) ; 

		if( tokenizer.hasMoreTokens () ) { 
			parseIdAndName ( targetAttribute, tokenizer.nextToken () ) ;		
			while( tokenizer.hasMoreTokens () ) { 

				TargetSubAttribute subAttribute = new TargetSubAttribute () ;
				subAttribute.setGroup ( targetAttribute ) ;
				parseAttribute ( subAttribute , tokenizer.nextToken () ) ;
				if ( subAttribute.getId () != null ) {
					targetAttribute.addSubAttribute ( subAttribute ) ;
				}
				else {
					System.err.println ( "TargetProtocol::parseMultiAttribute: ERROR parsing sub-attributes" ) ;
				}
			}
		}
	}
	private void parseAttribute ( ITargetAttribute attribute , String message ) {

		StringTokenizer tokenizer = new StringTokenizer ( message, ATT_TYPE_SEPARATOR+VALUE_SEPARATOR, true ) ; 

		if ( tokenizer.hasMoreTokens () ) { 
			parseIdAndName ( attribute, tokenizer.nextToken () ) ;
		}
		String token 	 = "INVALID" ;
		String separator = "INVALID" ;

		if( tokenizer.hasMoreTokens () ) { 
			separator = tokenizer.nextToken () ;
			if( tokenizer.hasMoreTokens () ) { 
				token = tokenizer.nextToken () ;
			}
			if ( separator.equals ( ATT_TYPE_SEPARATOR ) ) {
				parseAttributeType ( attribute, token ) ;
				if ( tokenizer.hasMoreTokens () ) {
					separator = tokenizer.nextToken () ;
					if ( tokenizer.hasMoreTokens () ) { 
						token = tokenizer.nextToken () ;
					}
					else {
						token = "INVALID";
					}
				}
			}
			if ( separator.equals ( VALUE_SEPARATOR ) ){
				event.setAttributeId ( attribute.getId () ) ;
				event.setTimeTag	 ( target.getTargetTime () ) ;
				event.setValue 		 ( token ) ;

				broker.attributeNotification ( event ) ;
			}			
		}
	}
	private void parseIdAndName ( ITargetAttribute targetAttribute, String message ) {
		int attributeId ;
		String attributeName ;

		// the id comes first and is only composed of ascii digits 0 to 9
		if ( '0' <= message.charAt ( 0 ) && message.charAt ( 0 ) <= '9' ) {
			if ( '0' <= message.charAt ( 1 ) && message.charAt ( 1 ) <= '9' ) {
				attributeId = Integer.parseInt ( message.substring (0, 2) ) ;
				attributeName = message.substring ( 2 ) ;
			}
			else {
				attributeId = Integer.parseInt ( message.substring (0, 1) ) ;
				attributeName = message.substring ( 1 ) ;
			}
			targetAttribute.setId ( Integer.toString ( attributeId ) ) ;
			targetAttribute.setName ( attributeName ) ;
		}
	}

	private void parseAttributeType ( ITargetAttribute targetAttribute, String message ) {
		String[] types = message.split( "" ) ;

		for ( String type : types ){
			if ( type.equals( "INVALID" ) ) {
				targetAttribute.setType( DataType.INVALID ) ;	
			}
			else if ( type.equals( BOOLEAN ) ) {
				targetAttribute.setType( DataType.BOOLEAN ) ;		
			}
			else if ( type.equals( UINT_8 ) ) {
				targetAttribute.setType( DataType.UINT_8 ) ;		
			}
			else if ( type.equals( INT_8 ) ) {
				targetAttribute.setType( DataType.INT_8 ) ;		
			}
			else if ( type.equals( UINT_10 ) ) {
				targetAttribute.setType( DataType.UINT_10 ) ;		
			}
			else if ( type.equals( UINT_16 ) ) {
				targetAttribute.setType( DataType.UINT_16 ) ;		
			}
			else if ( type.equals( INT_16 ) ) {
				targetAttribute.setType( DataType.INT_16 ) ;		
			}
			else if ( type.equals( UINT_32 ) ) {
				targetAttribute.setType( DataType.UINT_32 ) ;		
			}			
			else if ( type.equals( INT_32 ) ) {
				targetAttribute.setType( DataType.INT_32 ) ;		
			}
			else if ( type.equals( FLOAT_32 ) ) {
				targetAttribute.setType( DataType.FLOAT_32 ) ;		
			}
			else if ( type.equals( ARRAY_8 ) ) {
				targetAttribute.setType( DataType.ARRAY_8 ) ;		
			}
			else if ( type.equals( READ_ONLY ) ) {
				targetAttribute.setWrite( false ) ;		
			}
			else if ( type.equals( READ_WRITE ) ) {
				targetAttribute.setWrite( true ) ;		
			}
		}
	}
	private int parseTelnetMessage ( byte buffer [] , int offset , int length ) 
	{
		// Get the length
		String lengthString = "";
		for( ; offset < LENGTH_FIELD_SIZE ; offset ++ )
		{
			lengthString += (char) buffer [ offset ] ;
		}
		lengthString += '\0';

		int rxLength = Integer.parseInt ( lengthString ) ;

		if ( rxLength > ( length - offset ) )
		{
			System.err.println ("parseTelnetMessage error length received too big :(" + rxLength + ")" ) ;
		}
		// Provide the data
		target.telnetMsgReceived ( buffer [ offset ] , rxLength ) ;

		return ( offset + rxLength ) ;
	}
}


