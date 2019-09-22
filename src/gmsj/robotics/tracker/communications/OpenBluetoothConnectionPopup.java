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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.TrackerPreferences;

public class OpenBluetoothConnectionPopup extends Dialog {
	private Object 		result ;
	private Shell 		shell ;
	private Controler	controler   = Controler.getInstance () ;
	private boolean 	rememberMyChoices = false ;
	private String 		serialPort 	= new String ( "<port>" ) ;
	
	@Inject private IEclipseContext		context ;
	@Inject private EPartService 		partService ;
	
	static public void definePreferencesCatalog ()  {
		
		TrackerPreferences.createBooleanPrivatePreferences ( "OpenBluetoothConnectionPopup" , "rememberMyChoices" , false 	  ) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenBluetoothConnectionPopup" , "Port" 	 		   , "<port>" ) ;		
	}
	
	public OpenBluetoothConnectionPopup () {
		super ( Controler.getInstance ().display.getShells ()[0], SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
	}

	// Open the popup dialog box 
	public Object open () {
		TrackerPreferences.restorePreferences ( "OpenBluetoothConnectionPopup" , context ) ;

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
		shell.setSize ( 260, 170 ) ;
		shell.setText ( "Bluetooth Connection" ) ;

		//-------------------------------
		Label lblPort = new Label ( shell, SWT.NONE ) ;
		lblPort.setBounds (10, 32, 30, 30 ) ;
		lblPort.setText ( "Port" ) ;
		
		final Combo combo = new Combo ( shell, SWT.NONE ) ;
		combo.setItems ( new String[] {} ) ;
		combo.setBounds ( 40, 30, 210, 28 ) ;
		combo.setText ( serialPort ) ;
		
		Label label = new Label ( shell, SWT.SEPARATOR | SWT.HORIZONTAL ) ;
		label.setBounds(20, 70, 220, 2);
		
		//-------------------------------
		final Button btnRemenberMyChoices = new Button ( shell, SWT.CHECK ) ;
		btnRemenberMyChoices.setBounds(17, 70, 351, 28);
		btnRemenberMyChoices.setText ( "Remenber my choices" ) ;			
		//-------------------------------
		final Button connectButton = new Button ( shell, SWT.NONE ) ;
		connectButton.setBounds(160, 100, 90, 20);
		connectButton.setText ( "Connect" ) ;
		connectButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				
				savePreferences () ;

				// Let's inform the controler to start the connection
				controler.serialConnectionRequested ( combo.getText (), 4000000, 4000000 ) ;

				// and say good bye 
				shell.dispose();
			}
			private void savePreferences () {

				rememberMyChoices =  btnRemenberMyChoices.getSelection () ;
				TrackerPreferences.storeValueBoolean 	 ( "OpenBluetoothConnectionPopup" , "rememberMyChoices" , rememberMyChoices   ) ;
				if ( rememberMyChoices ) {
					TrackerPreferences.storeValueString  ( "OpenBluetoothConnectionPopup" , "serialPort" 		 , combo.getText ()    ) ;
				}
				TrackerPreferences.restorePreferences 	 ( "OpenBluetoothConnectionPopup" , partService.getActivePart().getContext () ) ;
			}
		});

		//-------------------------------
		final Button cancelButton = new Button ( shell, SWT.NONE ) ;
		cancelButton.setBounds ( 40, 100, 90, 20 ) ;
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

		// Highlight the default button
    	shell.setDefaultButton ( connectButton ) ;
    
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
