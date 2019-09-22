package gmsj.robotics.tracker.communications;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import gmsj.robotics.tracker.controler.Controler;
import jssc.SerialNativeInterface;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

// SerialCommunications Class allows to read from and write to the target
public class SerialCommunications implements SerialPortEventListener , ITargetCommunication {

	private SerialPort 		serialPort ;
	private CommListener 	listener ;
	private Display			display ;
	private AtomicInteger   nbRendezVous ;
	private boolean 		connected = false;
	private ArrayBlockingQueue < byte [] > bufferQueue ;
	private String 			name ;
	
    private static final Pattern PORTNAMES_REGEXP;
    private static final String PORTNAMES_PATH;
    static {
        switch (SerialNativeInterface.getOsType()) {
            case SerialNativeInterface.OS_LINUX: {
                PORTNAMES_REGEXP = Pattern.compile("(ttyS|ttyUSB|ttyACM|ttyAMA|rfcomm|ttyO)[0-9]{1,3}");
                PORTNAMES_PATH = "/dev/";
                break;
            }
            case SerialNativeInterface.OS_SOLARIS: {
                PORTNAMES_REGEXP = Pattern.compile("[0-9]*|[a-z]*");
                PORTNAMES_PATH = "/dev/term/";
                break;
            }
            case SerialNativeInterface.OS_MAC_OS_X: {
                PORTNAMES_REGEXP = Pattern.compile("tty.(serial|usbserial|usbmodem|TUBO).*");
                PORTNAMES_PATH = "/dev/";
                break;
            }
            case SerialNativeInterface.OS_WINDOWS: {
                PORTNAMES_REGEXP = Pattern.compile("");
                PORTNAMES_PATH = "";
                break;
            }
            default: {
                PORTNAMES_REGEXP = null;
                PORTNAMES_PATH = null;
                break;
            }
        }
    }
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public void addComListener ( CommListener listener ) {   
		this.listener = listener ;
	}
	public static String[] getSerialPortList () {
		return SerialPortList.getPortNames ( PORTNAMES_PATH, PORTNAMES_REGEXP ) ;
	}
	public boolean init ( String port, int rate ) {
		serialPort = new SerialPort ( port ) ;
		try {
			name = ( rate < 4000000 ) ? "SERIAL" : "BLUETOOTH" ;
			display = Controler.getInstance ().display ;
			nbRendezVous = new AtomicInteger ( 0 ) ;
			bufferQueue = new ArrayBlockingQueue < byte [] > ( 100 ) ;
			serialPort.openPort () ;
			serialPort.setParams ( rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE ) ;
			serialPort.setEventsMask ( SerialPort.MASK_RXCHAR ) ;
			serialPort.purgePort ( SerialPort.PURGE_RXCLEAR ) ;
			connected = true ;
			serialPort.addEventListener ( this ) ;
		}
		catch (SerialPortException e) {
			MessageBox messageBox = new MessageBox ( display.getActiveShell () , SWT.ICON_WARNING | SWT.OK);		        
			messageBox.setText("Warning") ;
			messageBox.setMessage ( "Impossible d'ouvrir le port : " + port ) ;
			messageBox.open () ;
		}		
		return connected ;
	}
	public boolean send ( String output ) {
		try {			   
			/////////////////////////// TEST: needed for serial sinon marche pas si envoi de plus de 7 char en même temps...!!!!
			if( output.length() > 7 ) {
				serialPort.writeString ( output.substring(0, 6) ) ;
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				serialPort.writeString ( output.substring(6) ) ;
			}
			else {
			/////////////////////////// TEST: needed for serial sinon marche pas si envoi de plus de 7 char en même temps...!!!!

			serialPort.writeString ( output ) ;
			}
		} 
		catch ( SerialPortException e ) {
			System.err.println ( e ) ;
		}
		return true ;
	}
	// receive() ====> for test receiving directly from ui thread at any time
	public boolean receive () {
		boolean success = false;
		nbRendezVous.decrementAndGet() ;
		byte [] buffer = bufferQueue.poll() ;
		while ( buffer != null ) {
			listener.packetReceived ( buffer , buffer.length ) ;
			buffer = bufferQueue.poll() ;
			success = true ;
		}
		return success ;
	}
//	private String convertBytesToString (  byte buffer[]  ) {
//		String convertedString = new String ("") ;
//		for ( int offset = 0; offset < buffer.length ; offset ++ )
//		{
//			convertedString += (char) buffer [ offset ] ;
//		}
//		convertedString += '\0';
//		return convertedString ;
//	}
	Runnable rendezVous = new Runnable () {
		@Override
		public void run () {
			nbRendezVous.decrementAndGet() ;
			byte [] buffer = bufferQueue.poll() ;
			while ( buffer != null ) {
				listener.packetReceived ( buffer , buffer.length ) ;
				buffer = bufferQueue.poll() ;
			}
		} 
	};

	@Override
	public void serialEvent ( SerialPortEvent serialPortEvent ) {
		try {
			byte [] bytesReceived = serialPort.readBytes () ;

			if ( ( bytesReceived != null ) && ( bytesReceived.length > 0 ) ) {
				if ( bufferQueue.offer ( bytesReceived ) == false ) {	
					while ( bufferQueue.offer ( bytesReceived , 100 , TimeUnit.MILLISECONDS ) == false ) ;
				}
				if ( nbRendezVous.compareAndSet( 0, 1 )  ) {
					display.asyncExec ( rendezVous ) ; 
				}
			}
		}
		catch ( SerialPortException | InterruptedException | NullPointerException e ) {
			display.asyncExec ( new Runnable () {
				@Override
				public void run () {
					System.err.println ( e ) ;
				} 
			} ) ; 
		}
	}
	public void serialEvent1 ( SerialPortEvent serialPortEvent ) {
		if ( serialPortEvent.isRXCHAR () ) {
			Display.getDefault ().syncExec ( new Runnable () {
				@Override
				public void run () { 			
					try {
						byte [] buffer = serialPort.readBytes () ;

						if ( ( buffer.length != 0 ) && ( listener != null ) ) {
							listener.packetReceived ( buffer , buffer.length ) ;									
						} 
					}
					catch ( SerialPortException | NullPointerException e ) {
						System.err.println ( e ) ;
					}
				}
			} ) ;
		}
	}
	public void closePort () {
		try {
			serialPort.closePort () ;
		} 
		catch ( SerialPortException e ) {
			System.err.println ( e ) ;
		}		
	}
	@Override
	public boolean send(byte[] command) {
		// TODO Auto-generated method stub
		return false ;
	}
}
