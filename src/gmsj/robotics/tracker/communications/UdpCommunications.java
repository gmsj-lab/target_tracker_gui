package gmsj.robotics.tracker.communications;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.swt.widgets.Display;

import gmsj.robotics.tracker.controler.Controler;

public class UdpCommunications implements ITargetCommunication {

	final static int 								datagramMaxLength 	= 2048 ;
	final static int								IPTOS_LOWDELAY		= 0x10 ;
	private boolean 								connected 			= false ;
	private byte [] 								txData 				= new byte [ datagramMaxLength ] ;
	private CommListener 							listener 			= null ;	
	private InetAddress 							serverAddress ;
	private int 									serverPort ;
	private DatagramSocket 							socket ;
	private DatagramPacket 							sendPacket ;
	private AtomicInteger   						nbRendezVous ;
	private ArrayBlockingQueue < DatagramPacket > 	bufferQueue ;
	private ArrayBlockingQueue < DatagramPacket > 	freeQueue ;
	private Display									display ;
	private Thread 									rxThread  ;

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
			
			while ( freeQueue.offer ( new DatagramPacket ( new byte [ datagramMaxLength ] , datagramMaxLength, serverAddress , serverPort ) ) )  ;

			if ( ! setServeurAddress ( serveur, port ) ) {
				return false ;
			}

			socket 	   	  	= new DatagramSocket () ;
			sendPacket 	  	= new DatagramPacket ( txData , datagramMaxLength, serverAddress , serverPort ) ;
			connected 	  	= true ;

			socket.setReceiveBufferSize ( socket.getReceiveBufferSize () * 10 ) ;
			socket.setTrafficClass (  IPTOS_LOWDELAY ) ;
			rxThread = new Thread ( new UdpReceiveThread () ) ;
			rxThread.setPriority ( Thread.MAX_PRIORITY ) ;
			rxThread.start () ;

			// send something to make the connection active
			send ( "Connection request to target" ) ;

			Thread.sleep ( 1000 ) ;
		} 
		catch ( SocketException | InterruptedException e ) {
			e.printStackTrace () ;
			return false ;
		}
		return true ;
	}
	private boolean setServeurAddress ( String serveur, int port ) 
	{
		try 
		{
			serverAddress = InetAddress.getByName ( serveur ) ;
			serverPort 	  = port ;
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
		
		sendPacket.setData ( command.getBytes() , 0 , command.length() ) ;
		try {
			socket.send ( sendPacket ) ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true ;
	}
	@Override
	public boolean send ( byte[] command ) {
		try {
			// TODO : CODER UN THREAD QUI DECOUPE EN TRANCHE. jouer sur l'offset : setData ( command, i , 1400 ) ; i=i+1400 ...
			sendPacket.setData ( command, 0, command.length ) ;
			socket.send ( sendPacket ) ;
		} catch ( IOException e ) {
			e.printStackTrace () ;
		}
		return false ;
	}
	@Override
	public void closePort () {
		connected = false ;
		socket.close () ;
	}
	Runnable rendezVous = new Runnable () {
		@Override
		public void run () {
			nbRendezVous.decrementAndGet() ;
			DatagramPacket receivePacket = bufferQueue.poll() ;

			while ( receivePacket != null ) {
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
		// TODO Auto-generated method stub
		return false;
	}
}
