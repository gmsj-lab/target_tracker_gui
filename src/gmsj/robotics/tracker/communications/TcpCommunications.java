package gmsj.robotics.tracker.communications;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.widgets.Display;

import gmsj.robotics.tracker.controler.Controler;

public class TcpCommunications implements ITargetCommunication {

	final static int 								datagramMaxLength 	= 2048 ;
	private boolean 								connected 			= false ;
	private CommListener 							listener 			= null ;	
	private Socket 									socket ;
	private InetAddress 							serverAddress ;
	private AtomicInteger   						nbRendezVous ;
	private ArrayBlockingQueue < StreamPacket > 	bufferQueue ;
	private ArrayBlockingQueue < StreamPacket > 	freeQueue ;
	private Display									display ;
	private Thread 									rxThread  ;
    private DataInputStream 						in ;
    private PrintWriter 							out ;
	
    public class StreamPacket {
    	byte[] buffer ;
		private int length ;
		
    	StreamPacket () {
    		buffer = new byte [ datagramMaxLength ] ;
    		length = 0 ;
    	}
    	byte[] getData () {
			return buffer ;	
    	}
    	int getLength () {
			return length ;
    	}
		public void setLength ( int length ) {
			this.length = length ;		
		}
    }
	@Override
	public String getName () {
		return "TCP" ;
	}

	@Override
	public void addComListener ( CommListener listener ) {   
		this.listener = listener ;
	}
	public boolean init ( String serveur, int port ) 
	{
		try 
		{
			display 	 = Controler.getInstance ().display ;
			nbRendezVous = new AtomicInteger ( 0 ) ;
			freeQueue    = new ArrayBlockingQueue < StreamPacket > ( 100 ) ;
			bufferQueue  = new ArrayBlockingQueue < StreamPacket > ( 100 ) ;
			
			while ( freeQueue.offer ( new StreamPacket () ) )  ;

			if ( ! setServeurAddress ( serveur, port ) ) {
				return false ;
			}
			socket 	   	 = new Socket ( serverAddress, port ) ;
			connected 	 = true ;

			socket.setReceiveBufferSize ( socket.getReceiveBufferSize () * 10 ) ;
			socket.setTcpNoDelay ( true ) ;
			socket.setPerformancePreferences ( 0, 10, 0 ) ; // prefer cnx time , prefer low latncy, prefer bandwidth

			in  = new DataInputStream ( socket.getInputStream () ) ;
	        out = new PrintWriter ( socket.getOutputStream (), true ) ;
	        
			rxThread = new Thread ( new TcpReceiveThread () ) ;
			rxThread.setPriority ( Thread.MAX_PRIORITY ) ;
			rxThread.start () ;

			Thread.sleep ( 1000 ) ;
		} 
		catch ( InterruptedException | IOException e ) {
			System.err.println( "Connection unsuccessful") ;
			connected = false ;
			return false ;
		}
		return true ;
	}
	private boolean setServeurAddress ( String serveur, int port ) 
	{
		try 
		{
			serverAddress = InetAddress.getByName ( serveur ) ;
		}
		catch ( UnknownHostException e ) {
			System.err.println ( "Invalid Ethernet address" ) ;
			serverAddress = InetAddress.getLoopbackAddress() ;
			return false ;
		}
		return true ;
	}
	@Override
	public boolean send ( String command ) {
		out.println ( command ) ;
		return true ;
	}
	@Override
	public boolean send ( byte[] command ) {
		try {
			socket.getOutputStream ().write ( command ) ;
		} catch ( IOException e ) {
			e.printStackTrace () ;
		}
		return false ;
	}
	@Override
	public void closePort () {
		connected = false ;
		try {
			socket.close () ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	Runnable rendezVous = new Runnable () {
		@Override
		public void run () {
			nbRendezVous.decrementAndGet () ;
			StreamPacket receivePacket = bufferQueue.poll () ;
			while ( receivePacket != null ) {
				listener.packetReceived ( receivePacket.getData () , receivePacket.getLength () ) ;
				freeQueue.add ( receivePacket ) ;
				receivePacket = bufferQueue.poll () ;
			}
		} 
	};
	public class TcpReceiveThread implements Runnable {  
		@Override
		public void run () {	
			try {
				while ( connected ) {					
					StreamPacket receivePacket = freeQueue.take () ;
					receivePacket.setLength ( in.read ( receivePacket.getData () ) ) ;
					if ( ( receivePacket.getLength () > 0 ) && ( listener != null ) ) {
						if ( bufferQueue.offer ( receivePacket ) == false ) {	
							try {
								while ( bufferQueue.offer ( receivePacket , 100 , TimeUnit.MILLISECONDS ) == false ) ;
							} catch (InterruptedException e) {
								display.asyncExec ( new Runnable () {
									@Override
									public void run () {
										System.err.println ( e ) ;
									}
								} );
							}
						}
						if ( nbRendezVous.compareAndSet ( 0, 1 )  ) {
							display.asyncExec ( rendezVous ) ; 
						}
					}
					else {
						// Not used, put it back
						freeQueue.add ( receivePacket ) ;
					}
				}
			} catch ( Exception e ) {
				//e.printStackTrace () ;
			}
		}
	}
	@Override
	public boolean receive() {
		return false;
	}

}
