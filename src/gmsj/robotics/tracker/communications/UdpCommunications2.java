package gmsj.robotics.tracker.communications;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.widgets.Display;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.Profiler;

public class UdpCommunications2 implements ITargetCommunication {

	final static int 								datagramMaxLength 	= 1024 ;
	private boolean 								connected 			= false ;
	private byte [] 								txData 				= new byte [ datagramMaxLength ] ;
	private CommListener 							listener 			= null ;	
	private InetAddress 							serverAddress ;
	private int 									serverPort ;
	private DatagramSocket 							socket ;
	private DatagramPacket 							sendPacket ;
	private UdpTransmitThread						udpTransmitThread ;
	private AtomicInteger   						nbRendezVous ;
	private ArrayBlockingQueue < DatagramPacket > 	bufferQueue ;
	private ArrayBlockingQueue < DatagramPacket > 	freeQueue ;
	private BlockingQueue < Block > 				sendQueue ;
	private Display									display ;
	private Thread 									rxThread  ;
	private Thread 									txThread  ;

	public class Block {
		int length ;
		byte [] buffer ;
		public Block ( byte[] bytes ) {
			buffer = bytes ;
			length = bytes.length ;
			}
	}
	@Override
	public String getName () {
		return "UDP" ;
	}
	@Override
	public void addComListener ( CommListener listener ) {   
		this.listener = listener ;
	}
	public boolean init ( String serveur, int port ) 
	{
		try 
		{
			display = Controler.getInstance ().display ;
			nbRendezVous = new AtomicInteger ( 0 ) ;
			freeQueue    = new ArrayBlockingQueue < DatagramPacket > ( 100 ) ;
			bufferQueue  = new ArrayBlockingQueue < DatagramPacket > ( 100 ) ;
			sendQueue  	 = new ArrayBlockingQueue < Block > ( 1000 ) ;
			
			while ( freeQueue.offer ( new DatagramPacket ( new byte [ datagramMaxLength ] , datagramMaxLength, serverAddress , serverPort ) ) )  ;

			setServeurAddress ( serveur, port ) ;

			socket 	   	  	= new DatagramSocket () ;
			sendPacket 	  	= new DatagramPacket ( txData , datagramMaxLength, serverAddress , serverPort ) ;
			connected 	  	= true ;

			socket.setReceiveBufferSize ( socket.getReceiveBufferSize () * 10 ) ;

			rxThread = new Thread ( new UdpReceiveThread () ) ;
			rxThread.setPriority ( Thread.MAX_PRIORITY ) ;
			rxThread.start () ;

			udpTransmitThread = new UdpTransmitThread () ;
			txThread = new Thread ( udpTransmitThread ) ;
			txThread.setPriority ( Thread.MIN_PRIORITY ) ;
			txThread.start () ;		

//			System.out.println ( "UDP: RX Thread:" + rxThread.getName () + "UDP: TX Thread:" + txThread.getName () ) ;
			// send something to make the connection active
			send ( "Connection request to target" ) ;

			Thread.sleep ( 1000 ) ;
		} 
		catch ( SocketException | InterruptedException e ) {
			e.printStackTrace () ;
		}
		return true ;
	}
	private void setServeurAddress ( String serveur, int port ) 
	{
		try 
		{
			serverAddress = InetAddress.getByName ( serveur ) ;
			serverPort 	  = port ;
		}
		catch ( UnknownHostException e ) {
			e.printStackTrace () ;
		}
	}
	@Override
	public boolean send ( String command ) {
		udpTransmitThread.send ( command ) ;
		return true ;
	}

	@Override
	public void closePort () {
		connected = false ;
		socket.close () ;
		udpTransmitThread = null ;// TODO : remove, for debug only
	}
	private String convertBytesToString (  byte buffer[] , int length ) {
		String convertedString = new String ("") ;
		for ( int offset = 0; offset < buffer.length ; offset ++ )
		{
			convertedString += (char) buffer [ offset ] ;
		}
		convertedString += '\0';
		return convertedString ;
	}
	Runnable rendezVous = new Runnable () {
		@Override
		public void run () {
			nbRendezVous.decrementAndGet() ;
			DatagramPacket receivePacket = bufferQueue.poll() ;

			while ( receivePacket != null ) {
				if ( convertBytesToString ( receivePacket.getData() , receivePacket.getLength() ).contains ( "&1023" ) ) {		// TODO remove after tests
					Profiler.add ("serial.rx rdv: <"+ receivePacket.toString() + ">" ); 										// TODO remove after tests
				}																												// TODO remove after tests
				listener.packetReceived ( receivePacket.getData() , receivePacket.getLength() ) ;
				freeQueue.add ( receivePacket ) ;
				receivePacket = bufferQueue.poll () ;
			}
		} 
	};
	public class UdpReceiveThread implements Runnable {  
		@Override
		public void run () {	
			try {
				while ( connected ) {					
					DatagramPacket receivePacket = freeQueue.take () ;
					socket.receive ( receivePacket ) ;
					if ( ( receivePacket.getLength () != 0 ) && ( listener != null ) ) {
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
						if ( nbRendezVous.compareAndSet( 0, 1 )  ) {
							display.asyncExec ( rendezVous ) ; 
						}
					}
				}
			} catch ( Exception e ) {
				//e.printStackTrace () ;
			}
		}
	}

	public class UdpTransmitThread implements Runnable {  

		@Override
		public void run () {	
			try {
				while ( connected ) {
					//Block block = sendQueue.take () ;
					Block block = sendQueue.poll ( 300, TimeUnit.MILLISECONDS ) ; // so we can check if still connected
					if ( block != null ) {
						sendPacket.setData ( block.buffer , 0 , block.length ) ;
						socket.send ( sendPacket ) ;						
					}

				}
			}
			catch ( Exception e ) {
			}
			display.syncExec ( new Runnable () {
				@Override
				public void run () {
					System.err.println ( "UDP: TX Thread about to finish:" + txThread.getName() ) ;
				}
			} );
		}
		public void send ( String command ) {	
			try {
				sendQueue.put ( new Block ( command.getBytes () ) ) ;
			} catch ( Exception e ) {
				e.printStackTrace () ;
			}
		}
	}

	@Override
	public boolean receive() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean send(byte[] command) {
		// TODO Auto-generated method stub
		return false;
	}
}
