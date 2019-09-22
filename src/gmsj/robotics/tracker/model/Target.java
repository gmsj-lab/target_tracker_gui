package gmsj.robotics.tracker.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.widgets.Display;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import gmsj.robotics.tracker.communications.CommListener;
import gmsj.robotics.tracker.communications.ITargetCommunication;
import gmsj.robotics.tracker.communications.TargetProtocol;
import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.STM32CRC;
import gmsj.robotics.tracker.controler.TargetBroker;
import gmsj.robotics.tracker.logging.ILogging;
import gmsj.robotics.tracker.navigation.ITreeElement;

public class Target implements ITreeElement , CommListener {

	private String 								name 				= new String ( "UKNOWN TARGET" ) ;
	private int 						 		numberOfAttributes 	= 0 ;
	private Double 								targetTime 			= 0.0 ;
	private TargetProtocol 				 		targetProtocol ;	
	private Map < String, ITargetAttribute >	attributes 			= new HashMap < String, ITargetAttribute > () ;
	private ITargetCommunication 				comm ;
	private ILogging							logger ;
	private	int									colorFromTarget ;
	private	int									colorFromHost ;
	@Inject MApplication 						application ;
	@Inject Model								model ;
	private TargetBroker 						broker ;
	//private boolean 							discoveryReceived ;
	public 	Display 							display ;
	
	final static int 							MAX_FILE_SIZE = 0x40000 ;
    private byte[] 								binaryFileBuffer ;
    private int 								binaryFileSize ;
    private long 								binaryFileChecksum ;
			
	public void init (  IEclipseContext context, ITargetCommunication comm , int colorFromHost , int colorFromTarget ) {
		// register for receive event
		this.comm 			 = comm ;
		this.colorFromHost 	 = colorFromHost ;
		this.colorFromTarget = colorFromTarget ;
		this.targetProtocol  = ContextInjectionFactory.make ( TargetProtocol.class, context ) ;
		this.targetProtocol.setTarget ( this ) ;
		comm.addComListener ( this ) ;
		sendDiscoveryMsg () ;
	}
	@Inject 
	private void getDisplay ( Display display ) {
		this.display = display ;
	}
	@Inject @Optional
	private void injectLogger ( ILogging logger ) {
		this.logger = logger ;
	}
	@Override
	public ITreeElement getParent () {
		return model ;
	}
	@Override
	public ITreeElement[] getChildren () {
		return attributes.values().toArray ( new ITargetAttribute [ attributes.size () ] ) ;
	}
	@Override
	public boolean hasChildren () {
		return ( ! attributes.isEmpty () ) ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void setName ( String name ) {
		this.name = name ;
		this.broker = Controler.getInstance ().getBroker ( name ) ;
		broker.setTarget( this ) ;
	}
	public ITargetCommunication getComm() {
		return comm;
	}
	public int getNumberOfAttributes () {
		return numberOfAttributes ;
	}
	public void clear () {
		attributes.clear () ;
		numberOfAttributes = 0 ;
	}
	public void setTargetTime ( long time ) {
		targetTime =  (double) time ;
	}
	public void updateTargetTime ( long time ) {
		targetTime += (double) time ;
	}
	public Double getTargetTime () {
		return targetTime ;
	}
	public void addTargetAttribute ( TargetAttribute attribute ) {
		attribute.setTarget ( this ) ; 
		attributes.put ( attribute.getId () , attribute ) ;
		numberOfAttributes ++ ;
	}
	public void addTargetAttributeGroup ( TargetAttributeGroup attributeGroup ) {
		attributeGroup.setTarget ( this ) ; 
		attributes.put ( attributeGroup.getId (), attributeGroup ) ;	

		for ( TargetSubAttribute subAttribute : attributeGroup.getAttributes () ) {
			subAttribute.setTarget ( this ) ; 
			numberOfAttributes ++ ;
		}
	}
	public void attributeChangeNotification ( AttributeEvent event ) {
		sendToTarget ( targetProtocol.buildUpdateMsg ( event.getAttributeIdString (), event.getValueString () ) ) ;
	}
	public void sendResetMsg () {
		sendToTarget ( targetProtocol.buildResetMsg () ) ;
	}
	public void sendBuildChangeRateMsg ( String desiredRate ) {
		sendToTarget ( targetProtocol.buildChangeRateMsg ( desiredRate ) ) ;
	}
	public void sendDiscoveryMsg ( ) {
		sendToTarget ( targetProtocol.buildDiscoverMsg () ) ;
		//startDiscoveryResponseTimer () ;
	}
//	private void startDiscoveryResponseTimer () {
//		discoveryReceived = false ;
//
//		Runnable timer = new Runnable () {
//			private int nbRetransmissions = 3 ;
//
//			@Override
//			public void run () {
//					if ( ( ! discoveryReceived ) && ( nbRetransmissions > 0 ) ) {
//						sendToTarget ( targetProtocol.buildDiscoverMsg () ) ;
//						display.timerExec ( 1000 , this ) ;
//						nbRetransmissions -- ;
//					}
//			}
//		};
//		display.timerExec ( 1, timer ) ;		
//	}
	public void discoveryMsgReceived () {
		// clean up the model from previous attributes if any
		clear () ;
		// show we receive it 
		//discoveryReceived = true ;
	}
	public void discoveryMsgProcessed () {
		// clean up the model from previous attributes if any
		// tell the model has changed
		application.getContext().set ( ModelChange.class, new ModelChange () ) ;	
	}
	public void telnetMsgReceived ( byte message, int length) {
		System.out.println ("TELNET MESSAGE RECEIVED, length:" + length ) ;
	}
	private void sendToTarget ( String command ) {
		comm.send ( command ) ;
		if ( logger != null ) {
			logger.logFromHost ( name , colorFromHost , command + "\n" ) ;
		}
	}
	@Override
	public void packetReceived ( byte buffer[] , int length ) {
		targetProtocol.inputReceived ( buffer , length ) ;  
		if ( logger != null ) {
			logger.logFromTarget ( name, colorFromTarget , buffer , length ) ;
		}
	}
	public void close () {
		comm.closePort () ;
		application.getContext().set ( ModelChange.class, new ModelChange () ) ;	
		//discoveryReceived = true ; // to stop discovery retransmission timer
	}
	@Override
	public void setParent ( ITreeElement parent ) {
	}
	public TargetBroker getBroker() {
		return 	broker ; 
	}
	public void sendDownloadCommand ( int size , long checksum ) {
		sendToTarget ( targetProtocol.buildDownloadCommandMsg ( size , checksum ) ) ;
	}
	public long calculateCRC32Checksum ( byte[] binaryFileBuffer , int size ) {
			 
		Checksum checksum = new CRC32() ;
		
		// update the current checksum with the specified array of bytes
		checksum.update ( binaryFileBuffer, 0, size ) ;
		 
		// get the current checksum value
		return checksum.getValue () ;
	}
	public long calculateCRC32ChecksumSTM32 ( byte[] binaryFileBuffer , int size ) {
		STM32CRC crc = new STM32CRC () ;
		// get the current checksum value
		return crc.get ( binaryFileBuffer , size ) ;
		
//		ByteBuffer buf = ByteBuffer.wrap ( binaryFileBuffer , 0 , size ) ;
//		crc.reset () ;
//		crc.update ( buf ) ;
//		return crc.get ( buf ) ;
	}
	public void downloadRequested ( String binaryFileName ) {
		// Read binary file 
		binaryFileSize = readBinaryFile ( binaryFileName ) ;
		
		// Compute checksum
//		binaryFileChecksum = calculateCRC32Checksum ( binaryFileBuffer , binaryFileSize ) ;
//		System.out.println ( "CRC32 checksum for input string is: " + binaryFileChecksum ) ;	
		binaryFileChecksum = calculateCRC32ChecksumSTM32 ( binaryFileBuffer , binaryFileSize ) ;
		System.out.println ( "Downloading application. Size: " + binaryFileSize + ",CRC32 checksum: " + binaryFileChecksum ) ;	
		
		// send downloadRequest command
		sendDownloadCommand ( binaryFileSize , binaryFileChecksum ) ;
	}
	public int readBinaryFile ( String binaryFileName ) {
        int nRead = 0 ;
        try {
			binaryFileBuffer 			= new byte [ MAX_FILE_SIZE ] ;
            FileInputStream inputStream = new FileInputStream ( binaryFileName ) ;

            if ( ( nRead = inputStream.read ( binaryFileBuffer ) ) == -1 ) {
            	System.out.println ( "readBinaryFile : nothing to read from binary file : '" + binaryFileName + "'" ) ;		
            }   
            inputStream.close () ;        
        }
        catch ( FileNotFoundException ex ) {
            System.out.println ( "Unable to open file '" + binaryFileName + "'" ) ;                
        }
        catch ( IOException ex ) {
            System.out.println ( "Error reading file '" + binaryFileName + "'" ) ;                  
        }
        return nRead ;
	}
	public void downloadAckReceived () {
		// Send Header a second time
		// send downloadRequest command
		sendDownloadCommand ( binaryFileSize , binaryFileChecksum ) ;
		
		try {
			Thread.sleep ( 100 ) ;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Send binary file
		comm.send ( binaryFileBuffer ) ;
	}
}
