package gmsj.robotics.tracker.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class CloseTargetPopup extends Dialog {
	private Object 		result ;
	private Shell 		shell ;
	private String		invitation ;
	private Model 		model ;
	private String 		targetName ;

	public CloseTargetPopup( Shell parent, Model model, String targetName ) {
		super( parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
		this.result = null ;
		this.model = model ;
		this.targetName = targetName ;
		this.invitation = "<enter target name>" ;
	}
	public Object open() {
		
		createContents () ;
		shell.open () ;
		shell.layout () ;		

		Display display = getParent().getDisplay() ;

		while ( !shell.isDisposed () ) {
			if ( !display.readAndDispatch () ) {
				display.sleep () ;
			}
		}
		return result ;
	}
	// Create contents of the dialog.
	private void createContents() {
	
		shell = new Shell(getParent(), getStyle());
		shell.setSize ( 300, 150 ) ;
		shell.setText( "Close target" ) ;

		//-------------------------------
		Label lblPort = new Label ( shell, SWT.NONE ) ;
		lblPort.setBounds ( 10, 32, 40, 30 ) ;
		lblPort.setText ( "Target" ) ;
		
		final Combo combo = new Combo ( shell, SWT.NONE ) ;
		combo.setItems 	( new String[] {} ) ;
		combo.setBounds ( 60, 30, 200, 28 ) ;
		combo.setText ( invitation ) ;
		
        for( Target target : model.getTargets () ) {
        	combo.add( target.getName() ) ;
        	
            if ( ( targetName != null ) && ( target.getName().equals ( targetName ) ) ) {
            	combo.select ( combo.getItemCount() - 1 ) ;
            }
        }

		//-------------------------------
		final Button closeButton = new Button(shell, SWT.NONE);
		closeButton.setBounds ( 160, 80, 90, 20 ) ;
		closeButton.setText ( "Close" ) ;

		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if ( ! combo.getText().equals ( invitation ) ) {
					result = combo.getText() ;	
				}
				shell.dispose();
			}
		});

		//-------------------------------
		final Button cancelButton = new Button(shell, SWT.NONE);
		cancelButton.setBounds ( 40, 80, 90, 20 ) ;
		cancelButton.setText ( "Cancel" ) ;

		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				result = null ;
				shell.dispose();
			}
		});
		shell.setDefaultButton( closeButton ) ;
	}
}
