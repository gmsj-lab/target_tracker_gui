package gmsj.robotics.tracker.communications;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.TrackerPreferences;

public class OpenUdpConnectionPopup extends Dialog {

	final static Color RED = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_RED   ) ;
	private   Object 		result ;
	private   Shell 		shell ;
	private   Controler		controler 			= Controler.getInstance () ;
	private   String 		port 				= "target port" ;
	private   String 		host 				= "target address" ;
	private   boolean 		rememberMyChoices 	= false ;
	private   boolean 		tcpProtocol		 	= false ;
	
	private Thread cnxThread  ;

	@Inject private 	EPartService 	partService ;
	@Inject private 	IEclipseContext	context ;

	static public void definePreferencesCatalog ()  {
		
		TrackerPreferences.createBooleanPrivatePreferences ( "OpenUdpConnectionPopup" , "tcpProtocol" 		, false 			) ;		
		TrackerPreferences.createBooleanPrivatePreferences ( "OpenUdpConnectionPopup" , "rememberMyChoices" , false 			) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenUdpConnectionPopup" , "udpPort" 	 		 , "target port" 	) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenUdpConnectionPopup" , "udpHost" 	 		 , "target address" ) ;		
	}
	public OpenUdpConnectionPopup () {
		super ( Controler.getInstance ().display.getShells ()[0], SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
	}
	public Object open () {
		TrackerPreferences.restorePreferences ( "OpenUdpConnectionPopup" 	, context ) ;

		createContents () ;
		shell.open () ;
		shell.layout () ;		

		Display display = getParent().getDisplay () ;

		while ( ! shell.isDisposed () ) {
			if ( ! display.readAndDispatch () ) {
				display.sleep () ;
			}
		}
		return result ;
	}

	// Create contents of the dialog.
	private void createContents () {

		shell = new Shell(getParent(), getStyle () ) ;
		shell.setSize ( 280, 210 ) ;
		shell.setText ( "Network connection" ) ;

		//-------------------------------
		final Button btnTcpProtocol = new Button ( shell, SWT.RADIO ) ;
		final Button btnUdpProtocol = new Button ( shell, SWT.RADIO ) ;
		btnUdpProtocol.setBounds ( 165, 10, 80, 30 ) ;
		btnUdpProtocol.setText ( "UDP" ) ;			
		btnUdpProtocol.setSelection ( ! tcpProtocol ) ;

		btnTcpProtocol.setBounds ( 215, 10, 80, 30 ) ;
		btnTcpProtocol.setText ( "TCP" ) ;			
		btnTcpProtocol.setSelection ( tcpProtocol ) ;

		Label lblAddress = new Label ( shell, SWT.NONE ) ;
		lblAddress.setBounds ( 20, 40, 30, 30 ) ;
		lblAddress.setText ( "Host" ) ;

		final Text addressText = new Text ( shell, SWT.NONE ) ;
		addressText.setBounds ( 50, 40, 210, 15 ) ;
		addressText.setText ( host ) ;

		//-------------------------------
		Label lblPort = new Label ( shell, SWT.NONE ) ;
		lblPort.setBounds ( 20, 60, 30, 30 ) ;
		lblPort.setText ( "Port" ) ;

		final Text portText = new Text ( shell, SWT.NONE ) ;
		portText.setBounds ( 50, 60, 210, 15 ) ;
		portText.setText ( port ) ;

		Label label = new Label ( shell, SWT.SEPARATOR | SWT.HORIZONTAL ) ;
		label.setBounds ( 20, 90, 240, 2 ) ;

		//-------------------------------
		final Button btnRemenberMyChoices = new Button ( shell, SWT.CHECK ) ;
		btnRemenberMyChoices.setBounds ( 17, 100, 351, 28 ) ;
		btnRemenberMyChoices.setText ( "Remenber my choices" ) ;			
		btnRemenberMyChoices.setSelection ( rememberMyChoices ) ;

		//-------------------------------
		final Button connectButton = new Button ( shell, SWT.NONE ) ;
		connectButton.setBounds ( 160, 140, 90, 20 ) ;
		connectButton.setText ( "Connect" ) ;

		connectButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {

				try {
					Integer.valueOf ( portText.getText () ) ;
				} catch ( NumberFormatException  exception ) {
					portText.setForeground ( RED ) ;
					return ;
				}

				savePreferences () ;
				
				if ( ( cnxThread != null ) && ( cnxThread.isAlive() ) ) {
					cnxThread.interrupt () ;					
				}
				cnxThread = new Thread ( new CnxThread ( btnUdpProtocol.getSelection () ,addressText.getText() , portText.getText() ) ) ;

				// DEBUG !!!!!!!!!
				if ( btnUdpProtocol.getSelection () ) {
					controler.udpConnectionRequested ( addressText.getText() , portText.getText() ) ;
				}
				else {
					controler.tcpConnectionRequested ( addressText.getText() , portText.getText() ) ;
				}	
				shell.dispose () ;

				// DEBUG !!!!!!!!!
				
			}

			private void savePreferences () {

				rememberMyChoices = btnRemenberMyChoices.getSelection () ;
				TrackerPreferences.storeValueBoolean 	 ( "OpenUdpConnectionPopup" , "rememberMyChoices" , rememberMyChoices 	) ;
				if ( rememberMyChoices ) {
					TrackerPreferences.storeValueBoolean ( "OpenUdpConnectionPopup" , "tcpProtocol" 	  , btnTcpProtocol.getSelection () 	) ;
					TrackerPreferences.storeValueString  ( "OpenUdpConnectionPopup" , "udpPort" 		  , portText.getText () 	) ;
					TrackerPreferences.storeValueString  ( "OpenUdpConnectionPopup" , "udpHost" 		  , addressText.getText () ) ;
				}
				TrackerPreferences.restorePreferences 	 ( "OpenUdpConnectionPopup" , partService.getActivePart().getContext ()  ) ;
			}
		});

		//-------------------------------
		final Button cancelButton = new Button ( shell, SWT.NONE ) ;
		cancelButton.setBounds ( 40, 140, 90, 20 ) ;
		cancelButton.setText ( "Cancel" ) ;

		cancelButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				// forget everything and turn off the light !
				if ( ( cnxThread != null ) && ( cnxThread.isAlive() ) ) {
					cnxThread.interrupt () ;					
				}
				shell.dispose () ;
			}
		});

		// Highlight the default button
		shell.setDefaultButton ( connectButton ) ;
	}
	@Inject @Optional
	private void setPort ( @Named ( "udpPort" ) String port ) {
		if ( port != null ) {
		this.port = port ;
		}
	}
	@Inject @Optional
	private void setHost ( @Named ( "udpHost" ) String host ) {
		if ( host != null ) {
			this.host = host ;
		}
	}
	@Inject @Optional
	private void setRememberMyChoices ( @Named ( "rememberMyChoices" ) boolean rememberMyChoices ) {
		this.rememberMyChoices = rememberMyChoices ;
	}
	@Inject @Optional
	private void setTcpProtocol ( @Named ( "tcpProtocol" ) boolean tcpProtocol ) {
		this.tcpProtocol = tcpProtocol ;
	}

	public class CnxThread implements Runnable {  
		private boolean udp ;
		private String 	address ;
		private	String  port ;
		
		CnxThread ( boolean udp , String address, String port ) {
			this.udp 	 = udp ;
			this.address = address ;
			this.port 	 = port ;
		}
		@Override
		public void run () {	
			System.err.println( "RUN....................") ;
			try {
				// Let's inform the controler to start the connection
				if ( udp ) {
					controler.udpConnectionRequested ( address , port ) ;
				}
				else {
					controler.tcpConnectionRequested ( address , port ) ;
				}
				// and say good bye 
				shell.dispose () ;
			} catch ( Exception e ) {
				e.printStackTrace () ;
			}
		}
	}
}