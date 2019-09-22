package gmsj.robotics.tracker.communications;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.TrackerPreferences;

public class OpenSerialConnectionPopup extends Dialog {
	
	private Object 		result ;
	private Shell 		shell ;
	private String 		initRate 	= new String ( "9600" ) ; //$NON-NLS-1$
	private String 		desiredRate = new String ( "9600" ) ; //$NON-NLS-1$
	private Controler	controler   = Controler.getInstance () ;
	private boolean 	rememberMyChoices = false ;
	private String 		serialPort 	= new String ( "<port>" ) ;
	
	@Inject private IEclipseContext		context ;
	@Inject private EPartService 		partService ;
	
	static public void definePreferencesCatalog ()  {
		
		TrackerPreferences.createBooleanPrivatePreferences ( "OpenSerialConnectionPopup" , "rememberMyChoices" , false 				) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenSerialConnectionPopup" , "serialPort" 	 	, "<port>" 			) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenSerialConnectionPopup" , "initRate" 	 		, "9600"			) ;	
		TrackerPreferences.createStringPrivatePreferences  ( "OpenSerialConnectionPopup" , "desiredRate" 	 	, "9600"			) ;	
	}
	
	public OpenSerialConnectionPopup () {
		super ( Controler.getInstance ().display.getShells ()[0], SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
	}

	// Open the popup dialog box 
	public Object open () {
		TrackerPreferences.restorePreferences ( "OpenSerialConnectionPopup" , context ) ;

		createContents () ;
		shell.open () ;
		shell.layout () ;		
		
		Display display = getParent().getDisplay () ;
		
		while (!shell.isDisposed () ) {
			if (!display.readAndDispatch () ) {
				display.sleep () ;
			}
		}
		return result ;
	}

	// Create contents of the dialog.
	private void createContents () {
	
		shell = new Shell ( getParent (), getStyle () ) ;
		shell.setSize ( 260, 490 ) ;
		shell.setText ( "Serial Connection" ) ;

		SelectionAdapter initSpeedButtonAdapter = new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				if( ((Button)e.getSource()).getSelection () ) {
					initRate = ((Button)e.getSource()).getText () ;
				}
			}
		};

		SelectionAdapter speedButtonAdapter = new SelectionAdapter () {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( ((Button)e.getSource()).getSelection() ) {
					desiredRate = ((Button)e.getSource()).getText () ;
				}
			}
		};
		
		//-------------------------------
		Label lblPort = new Label ( shell, SWT.NONE ) ;
		lblPort.setBounds (10, 32, 30, 30 ) ;
		lblPort.setText ( "Port" ) ;
		
		final Combo combo = new Combo ( shell, SWT.NONE ) ;
		combo.setItems ( new String[] {} ) ;
		combo.setBounds ( 40, 30, 210, 28 ) ;
		combo.setText ( serialPort ) ;

		//-------------------------------
		final Group grpInitialRate = new Group ( shell, SWT.NONE ) ;
    	grpInitialRate.setText ( "Initial line rate" ) ;
    	grpInitialRate.setToolTipText ( "Initial baud rate for Serial interface as defined by target" ) ;
    	grpInitialRate.setBounds(20, 70, 100, 270);
    	
    	Button buttonInit9600 = new Button ( grpInitialRate, SWT.RADIO ) ;
    	buttonInit9600.addSelectionListener ( initSpeedButtonAdapter ) ;
    	buttonInit9600.setText ( "9600" ) ; //$NON-NLS-1$
    	buttonInit9600.setBounds ( 10, 10, 144, 18 ) ;
    	
    	final Button buttonInit19200 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit19200.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit19200.setText("19200"); //$NON-NLS-1$
    	buttonInit19200.setBounds(10, 40, 144, 18);
    	
    	final Button buttonInit38400 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit38400.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit38400.setText("38400"); //$NON-NLS-1$
    	buttonInit38400.setBounds(10, 70, 151, 18);
    	
    	final Button buttonInit57600 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit57600.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit57600.setText("57600"); //$NON-NLS-1$
    	buttonInit57600.setBounds(10, 100, 143, 18);
    	
    	final Button buttonInit115200 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit115200.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit115200.setText("115200"); //$NON-NLS-1$
    	buttonInit115200.setBounds(10, 130, 143, 18);
    	
    	final Button buttonInit230400 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit230400.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit230400.setText("256000"); //$NON-NLS-1$
    	buttonInit230400.setBounds(10, 160, 143, 18);
    		
    	final Button buttonInit921600 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit921600.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit921600.setText("921600"); //$NON-NLS-1$
    	buttonInit921600.setBounds(10, 190, 143, 18);
    	
    	final Button buttonInit1843200 = new Button(grpInitialRate, SWT.RADIO);
    	buttonInit1843200.addSelectionListener( initSpeedButtonAdapter );
    	buttonInit1843200.setText("1843200"); //$NON-NLS-1$
    	buttonInit1843200.setBounds(10, 220, 143, 18);
   
    	final Group speedSelectionGroup = new Group(shell, SWT.NONE);
		speedSelectionGroup.setText ( "Desired rate" ) ;
		speedSelectionGroup.setToolTipText ( "Select new baud rate for Serial interface both on host and target" ) ;
		speedSelectionGroup.setBounds(140, 70, 100, 270);
		
		final Button button9600 = new Button(speedSelectionGroup, SWT.RADIO);
		button9600.addSelectionListener( speedButtonAdapter );
		button9600.setBounds(10, 10, 128, 18);
		button9600.setText("9600"); //$NON-NLS-1$

		final Button button19200 = new Button(speedSelectionGroup, SWT.RADIO);
		button19200.addSelectionListener(speedButtonAdapter ); 
		button19200.setBounds(10, 40, 128, 18);
		button19200.setText("19200"); //$NON-NLS-1$
					
		final Button button38400 = new Button(speedSelectionGroup, SWT.RADIO);
		button38400.addSelectionListener( speedButtonAdapter );
		button38400.setBounds(10, 70, 128, 18);
		button38400.setText("38400"); //$NON-NLS-1$
		
		final Button button57600 = new Button(speedSelectionGroup, SWT.RADIO);
		button57600.addSelectionListener( speedButtonAdapter );
		button57600.setBounds(10, 100, 128, 18);
		button57600.setText("57600"); //$NON-NLS-1$
		
		final Button button115200 = new Button(speedSelectionGroup, SWT.RADIO);
		button115200.addSelectionListener( speedButtonAdapter );
		button115200.setBounds(10, 130, 128, 18);
		button115200.setText("115200"); //$NON-NLS-1$

		final Button button230400 = new Button(speedSelectionGroup, SWT.RADIO);
		button230400.addSelectionListener( speedButtonAdapter );
		button230400.setBounds(10, 160, 128, 18);
		button230400.setText("256000"); //$NON-NLS-1$

		final Button button921600 = new Button(speedSelectionGroup, SWT.RADIO);
		button921600.addSelectionListener( speedButtonAdapter );
		button921600.setText("921600"); //$NON-NLS-1$
		button921600.setBounds(10, 190, 143, 18);
		
		final Button button1843200 = new Button(speedSelectionGroup, SWT.RADIO);
		button1843200.addSelectionListener( speedButtonAdapter );
		button1843200.setText("1843200"); //$NON-NLS-1$
		button1843200.setBounds(10, 220, 143, 18);
		
		Label label = new Label ( shell, SWT.SEPARATOR | SWT.HORIZONTAL ) ;
		label.setBounds(20, 360, 220, 2);
		
		//-------------------------------
		final Button btnRemenberMyChoices = new Button ( shell, SWT.CHECK ) ;
		btnRemenberMyChoices.setBounds(17, 360, 351, 28);
		btnRemenberMyChoices.setText ( "Remenber my choices" ) ;			
		//-------------------------------
		final Button connectButton = new Button ( shell, SWT.NONE ) ;
		connectButton.setBounds(160, 415, 90, 20);
		connectButton.setText ( "Connect" ) ;
		connectButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
								
				Integer initRateInt ;
				Integer desiredRateInt ;
				try {
					initRateInt 	= Integer.valueOf ( initRate ) ;
					desiredRateInt  = Integer.valueOf ( desiredRate ) ;
					
//					initRateInt 	= Integer.valueOf ( 4000000 ) ;
//					desiredRateInt  = Integer.valueOf ( 4000000 ) ;
					
					
				} catch ( NumberFormatException e1 ) {
					System.out.println ( "OpenSerialConnection : NumberFormatException on rate" ) ;
					initRateInt 	= 9600 ;
					desiredRateInt  = 9600 ;
				}
				
				savePreferences () ;

				// Let's inform the controler to start the connection
				controler.serialConnectionRequested ( combo.getText (), initRateInt, desiredRateInt ) ;

				// and say good bye 
				shell.dispose();
			}
			private void savePreferences () {

				rememberMyChoices =  btnRemenberMyChoices.getSelection () ;
				TrackerPreferences.storeValueBoolean 	 ( "OpenSerialConnectionPopup" , "rememberMyChoices" , rememberMyChoices   ) ;
				if ( rememberMyChoices ) {
					TrackerPreferences.storeValueString  ( "OpenSerialConnectionPopup" , "serialPort" 		 , combo.getText ()    ) ;
					TrackerPreferences.storeValueString  ( "OpenSerialConnectionPopup" , "initRate" 		 , initRate			   ) ;
					TrackerPreferences.storeValueString  ( "OpenSerialConnectionPopup" , "desiredRate" 		 , desiredRate		   ) ;
				}
				TrackerPreferences.restorePreferences 	 ( "OpenSerialConnectionPopup" , partService.getActivePart().getContext () ) ;
			}
		});

		//-------------------------------
		final Button cancelButton = new Button ( shell, SWT.NONE ) ;
		cancelButton.setBounds ( 40, 415, 90, 20 ) ;
		cancelButton.setText ( "Cancel" ) ;
					
		cancelButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				// forget everything and turn off the light !
				shell.dispose () ;
			}
		});

		String[] portNames = SerialCommunications.getSerialPortList () ;

		// TODO : PREFERENCES btnRemenberMyChoices.setSelection( choice.equals("true") );
		btnRemenberMyChoices.setSelection ( rememberMyChoices ) ;
				
        for ( int i = 0; i < portNames.length; i++ ) {
        	combo.add ( portNames[i] ) ;
        }
		// Set the button corresponig to the init speed, unset others
		buttonInit9600.setSelection    ( initRate.equals ( buttonInit9600.getText    () ) ) ;
		buttonInit19200.setSelection   ( initRate.equals ( buttonInit19200.getText   () ) ) ;
		buttonInit38400.setSelection   ( initRate.equals ( buttonInit38400.getText   () ) ) ;
		buttonInit57600.setSelection   ( initRate.equals ( buttonInit57600.getText   () ) ) ;
		buttonInit115200.setSelection  ( initRate.equals ( buttonInit115200.getText  () ) ) ;
		buttonInit230400.setSelection  ( initRate.equals ( buttonInit230400.getText  () ) ) ;
		buttonInit921600.setSelection  ( initRate.equals ( buttonInit921600.getText  () ) ) ;
		buttonInit1843200.setSelection ( initRate.equals ( buttonInit1843200.getText () ) ) ;
		
		// Set the button corresponig to the speed, unset others
		button9600.setSelection		( desiredRate.equals ( button9600.getText    () ) );
		button19200.setSelection	( desiredRate.equals ( button19200.getText   () ) );
		button38400.setSelection	( desiredRate.equals ( button38400.getText   () ) );
		button57600.setSelection	( desiredRate.equals ( button57600.getText   () ) );
		button115200.setSelection	( desiredRate.equals ( button115200.getText  () ) );
		button230400.setSelection	( desiredRate.equals ( button230400.getText  () ) );
		button921600.setSelection   ( desiredRate.equals ( button921600.getText  () ) ) ;
		button1843200.setSelection  ( desiredRate.equals ( button1843200.getText () ) ) ;

		// Highlight the default button
    	shell.setDefaultButton ( connectButton ) ;
    
		}
	@Inject @Optional
	private void setDesiredRate ( @Named ( "desiredRate" ) String desiredRate ) {
		if ( desiredRate != null ) {
			this.desiredRate = desiredRate ;
		}
	}	
	@Inject @Optional
	private void setInitRate ( @Named ( "initRate" ) String initRate ) {
		if ( initRate != null ) {
			this.initRate = initRate ;
		}
	}
	@Inject @Optional
	private void setPort ( @Named ( "serialPort" ) String serialPort ) {
		if ( serialPort != null ) {
		this.serialPort = serialPort ;
		}
	}
	@Inject @Optional
	private void setRememberMyChoices ( @Named ( "rememberMyChoices" ) boolean rememberMyChoices ) {
		this.rememberMyChoices = rememberMyChoices ;
	}
}
