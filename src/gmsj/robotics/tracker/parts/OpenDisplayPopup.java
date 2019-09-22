package gmsj.robotics.tracker.parts;

import java.util.ArrayList;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class OpenDisplayPopup extends Dialog {

	private OpenDisplayPopupResponse 	result ;
	private Shell 						shell ;
	private ArrayList<MPart>			displayParts ;
	private String						invitation ;
	
	public class OpenDisplayPopupResponse {
		public boolean	isValid ;
		public String 	selectedItemsText ;
	}
	public OpenDisplayPopup ( Shell parent , ArrayList<MPart> displayParts ) {
		super( parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
		
		result = new OpenDisplayPopupResponse () ;
		this.displayParts 	= displayParts ;
		this.invitation 	= "<enter new display name>" ;
	}
	public Object open() {
		

		createContents () ;
		shell.open () ;
		shell.layout () ;		

		Display display = getParent().getDisplay();

		while ( !shell.isDisposed () ) {
			if ( !display.readAndDispatch () ) {
				display.sleep () ;
			}
		}
		return result ;
	}
	// Create contents of the dialog.
	private void createContents() {
	
		shell = new Shell ( getParent() , getStyle () ) ;
		shell.setSize ( 300, 150 ) ;
		shell.setText ( "Open display" ) ;

		//-------------------------------
		Label lblPort = new Label ( shell, SWT.NONE ) ;
		lblPort.setBounds ( 10, 32, 40, 30 ) ;
		lblPort.setText   ( "Display" ) ;
		
		final Combo combo = new Combo ( shell, SWT.NONE ) ;
		combo.setItems 	( new String[] {} ) ;
		combo.setBounds ( 60, 30, 200, 28 ) ;
		combo.setText   ( invitation ) ;
		
        for( MPart part : displayParts ) {
        	combo.add ( part.getLabel () ) ;
        }
        
		//-------------------------------
		final Button openButton = new Button ( shell , SWT.NONE ) ;
		openButton.setBounds ( 160, 80, 90, 20 ) ;
		openButton.setText 	 ( "Open" ) ;

		openButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {

				result.isValid = true ;
				
				if ( combo.getText().equals ( invitation ) ) {
					result.selectedItemsText = String.valueOf ( combo.getItemCount() ) ;
				}
				else {
					result.selectedItemsText = combo.getText () ;	
				}
				shell.dispose();
			}
		});

		//-------------------------------
		final Button cancelButton = new Button ( shell , SWT.NONE ) ;
		cancelButton.setBounds ( 40, 80, 90, 20 ) ;
		cancelButton.setText   ( "Cancel" ) ;

		cancelButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				
				result.isValid = false ;
				shell.dispose () ;
			}
		});
		shell.setDefaultButton ( openButton ) ;
	}
}
