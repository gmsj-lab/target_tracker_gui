//package gmsj.robotics.tracker.communications;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.MessageBox;
//
//import jssc.SerialPort;
//import jssc.SerialPortEvent;
//import jssc.SerialPortEventListener;
//import jssc.SerialPortException;
//import jssc.SerialPortList;
//
//// SerialCommunications Class allows to read from and write to the target
//public class SerialCommunications2 implements SerialPortEventListener , ITargetCommunication {
//
//	private SerialPort 		serialPort ;
//	private CommListener 	listener ;
//	private boolean 		connected = false;
//
//	@Override
//	public String getName () {
//		return "SERIAL" ;
//	}
//	@Override
//	public void addComListener ( CommListener listener ) {   
//		this.listener = listener ;
//	}
//	public static String[] getSerialPortList () {
//		return SerialPortList.getPortNames () ;
//	}
//	public boolean init ( String port, int rate ) {
//		serialPort = new SerialPort ( port ) ;
//		try {
//			serialPort.openPort () ;
//			serialPort.setParams ( rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE ) ;
//			serialPort.setEventsMask ( SerialPort.MASK_RXCHAR ) ;
//			serialPort.purgePort ( SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR ) ;
//			serialPort.addEventListener ( this ) ;
//			connected = true ;
//		}
//		catch (SerialPortException e) {
//			MessageBox messageBox = new MessageBox ( Display.getDefault ().getActiveShell () , SWT.ICON_WARNING | SWT.OK);		        
//			messageBox.setText("Warning") ;
//			messageBox.setMessage ( "Impossible d'ouvrir le port : " + port ) ;
//			messageBox.open () ;
//		}
//		return connected ;
//	}
//	public boolean send ( String output ) {
//		try {			   
//			serialPort.writeString ( output ) ;
//		} 
//		catch ( SerialPortException e ) {
//			System.err.println ( e ) ;
//		}
//		return true ;
//	}
//	@Override
//	public void serialEvent ( SerialPortEvent serialPortEvent ) {
//		if ( serialPortEvent.isRXCHAR () ) {
//			Display.getDefault ().syncExec ( new Runnable () {
//				@Override
//				public void run () { 			
//					try {
//						byte [] buffer = serialPort.readBytes () ;
//
//						if ( ( buffer.length != 0 ) && ( listener != null ) ) {
//
//							listener.packetReceived ( buffer , buffer.length ) ;									
//						} 
//					}
//					catch ( SerialPortException e ) {
//						System.err.println ( e ) ;
//					}
//				}
//			} ) ;
//		}
//	}
//	public void serialEvent3 ( SerialPortEvent serialPortEvent ) {
//		if ( serialPortEvent.isRXCHAR () ) {
//			try {
//				byte [] buffer = serialPort.readBytes () ;
//
//				if ( ( buffer.length != 0 ) && ( listener != null ) ) {
//					Display.getDefault ().syncExec ( new Runnable () {
//						@Override
//						public void run () { 
//							listener.packetReceived ( buffer , buffer.length ) ;									
//						} 
//					} ) ;
//				}
//			}
//			catch ( SerialPortException e ) {
//				System.err.println ( e ) ;
//			}
//		}
//	}
//	public void serialEvent2 ( SerialPortEvent serialPortEvent ) {
//		if ( serialPortEvent.isRXCHAR () ) {
//			try {
//				byte [] buffer = serialPort.readBytes () ;
//
//				if ( ( buffer.length != 0 ) && ( listener != null ) ) {
//					listener.packetReceived ( buffer , buffer.length ) ;
//				}
//			}
//			catch ( SerialPortException e ) {
//				System.err.println ( e ) ;
//			}
//		}
//	}
//	public void closePort () {
//		try {
//			serialPort.closePort () ;
//		} 
//		catch ( SerialPortException e ) {
//			System.err.println ( e ) ;
//		}		
//	}
//	@Override
//	public boolean receive() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//}
