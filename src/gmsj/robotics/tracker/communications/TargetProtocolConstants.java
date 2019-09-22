package gmsj.robotics.tracker.communications;

public class TargetProtocolConstants {

	protected static final String SOM 						= "@";	
	protected static final char  _SOM 						= '@';	
	protected  static final String EOM 						= "#";	
	protected static final char    _EOM 					= '#';	
	protected  static final String EOM_ACQ_REQ 				= "§";	
	protected static final String DISCOVER 					= "D";
	protected static final char  _DISCOVER 					= 'D';
	protected static final String UPDATE 					= "U";
	protected static final char  _UPDATE 					= 'U';
	protected static final String SOFTWARE_REPROG_ACK 		= "Z";
	protected static final char  _SOFTWARE_REPROG_ACK 		= 'Z';	
	protected static final String TELNET 					= "W";
	protected static final char  _TELNET 					= 'W';
	protected static final String CHANGE_RATE 				= "S";
	protected static final char  _CHANGE_RATE 				= 'S';
	protected static final String RESET 					= "Z";
	protected static final char  _RESET 					= 'Z';
	protected static final String VALUE_SEPARATOR 			= "&";
	protected static final char  _VALUE_SEPARATOR 			= '&';
	protected static final String ATTRIBUTES_SEPARATOR 		= "*";
	protected static final char  _ATTRIBUTES_SEPARATOR 		= '*';
	protected static final String TARGET_NAME_SEPARATOR 	= "$";
	protected static final String TIMETAG_SEPARATOR 		= "T";
	protected static final char _TIMETAG_SEPARATOR 			= 'T';
	protected static final char _SEQNUM_SEPARATOR 			= 'Q';
	protected static final String MULTI_ATTRIBUTE_SEPARATOR = ":";
	protected static final String ATT_TYPE_SEPARATOR 		= "%";
	protected static final String BOOLEAN 					= "B";
	protected static final String UINT_8 					= "D";
	protected static final String INT_8 					= "C";
	protected static final String UINT_10				 	= "S";
	protected static final String UINT_16 					= "J";
	protected static final String INT_16 					= "I";
	protected static final String UINT_32 					= "M";
	protected static final String INT_32 					= "L";
	protected static final String FLOAT_32 					= "F";
	protected static final String ARRAY_8 					= "A";
	protected static final String READ_ONLY 				= "R";
	protected static final String READ_WRITE 				= "W";
	protected static final int 	LENGTH_FIELD_SIZE			=  3 ;
	protected static final char  _SEQNUM_START 				= '[';	
	protected static final char  _SEQNUM_END 				= ']';	
	
	protected enum MessageType {
		NO_MESSAGE,
		DISCOVER,
		UPDATE,
		UKNOWN_MESSAGE;
	};
	public String buildUpdateMsg ( String id, String value ){
		return new String ( SOM + UPDATE + id + VALUE_SEPARATOR + value + EOM ) ;
	}
	public String buildResetMsg() {
		return new String ( SOM + RESET + EOM ) ;
	}
	public String buildDiscoverMsg() {
		return new String ( SOM + DISCOVER + EOM ) ;
	}
	public String buildChangeRateMsg( String desiredRate ) {
		return new String ( SOM + CHANGE_RATE + desiredRate + EOM ) ;
	}
	// Reserved for future use
	protected String buildUpdateMsgAcqReq( String id, String value ){
		return new String ( SOM + UPDATE + id + VALUE_SEPARATOR + value + EOM_ACQ_REQ ) ;
	}
	public String buildDownloadCommandMsg ( int size, long checksum ) {
		return new String ( SOM + SOFTWARE_REPROG_ACK + String.valueOf ( size ) + VALUE_SEPARATOR + String.valueOf ( checksum ) + EOM ) ;
	}
}
