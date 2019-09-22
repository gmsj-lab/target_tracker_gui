package gmsj.robotics.tracker.model;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class OpenDownloadFilePopup extends Dialog {
 	private Object 		result ;
	private Shell 		shell ;
	private boolean 	rememberMyChoices = false ;
	private String 		binaryFile 	= new String ( "<binary file>" ) ;
	private String 		targetName ;
	private String		invitation ;
	private Model 		model ;
	private Text 		binaryFileText ;
	
	final static Color  RED = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_RED   ) ;

	@Inject private IEclipseContext		context ;
	@Inject private EPartService 		partService ;

    class OpenFile implements SelectionListener {
      public void widgetSelected ( SelectionEvent event ) {
        FileDialog fd = new FileDialog ( shell , SWT.OPEN ) ;
        fd.setText( "Select binary file" );
        fd.setFilterPath("./");
        String[] filterExt = { "*.bin", "*.*" };
        fd.setFilterExtensions ( filterExt ) ;
        binaryFile = fd.open () ;
        binaryFileText.setText ( binaryFile ) ;
		System.out.println ( binaryFile ) ;
      }

      public void widgetDefaultSelected(SelectionEvent event) {
      }
    }
  	static public void definePreferencesCatalog ()  {
		TrackerPreferences.createBooleanPrivatePreferences ( "OpenDownloadFilePopup" , "rememberMyChoices" 	, false 	 ) ;		
		TrackerPreferences.createStringPrivatePreferences  ( "OpenDownloadFilePopup" , "binaryFile" 	 	, "<binary file>" ) ;		
	}
	public OpenDownloadFilePopup () {
		super ( Display.getCurrent().getActiveShell() , SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL ) ;
	}
 	// Open the popup dialog box 
	public Object open ( Model model, String targetName ) {
		this.targetName = targetName ;
		this.model = model ;
		this.invitation = "<enter target name>" ;
		
		TrackerPreferences.restorePreferences ( "OpenDownloadFilePopup" , context ) ;

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
		shell.setSize ( 620, 200 ) ;
		shell.setText ( "Specify binary file to be downloaded" ) ;

		//-------------------------------
		Label lblTarget = new Label ( shell, SWT.NONE ) ;
		lblTarget.setBounds ( 10, 32, 40, 20 ) ;
		lblTarget.setText ( "Target" ) ;
		
		final Combo comboTarget = new Combo ( shell, SWT.NONE ) ;
		comboTarget.setItems 	( new String[] {} ) ;
		comboTarget.setBounds ( 60, 30, 200, 20 ) ;
		comboTarget.setText ( invitation ) ;
		
        for( Target target : model.getTargets () ) {
        	comboTarget.add( target.getName () ) ;
        	
            if ( ( targetName != null ) && ( target.getName().equals ( targetName ) ) ) {
            	comboTarget.select ( comboTarget.getItemCount() - 1 ) ;
            }
        }
		
		//-------------------------------
		Label lblFile = new Label ( shell, SWT.NONE ) ;
		lblFile.setBounds (10, 70, 30, 18 ) ;
		lblFile.setText ( "File:" ) ;
		
		binaryFileText = new Text ( shell, SWT.NONE ) ;
		binaryFileText.setBounds ( 60, 70, 510, 18 ) ;
		binaryFileText.setText ( binaryFile ) ;
		
		Button openFileDialogButton = new Button ( shell, SWT.PUSH ) ;
    	openFileDialogButton.addSelectionListener ( new OpenFile () ) ;
    	openFileDialogButton.setText ( "..." ) ; //$NON-NLS-1$
    	openFileDialogButton.setBounds ( 570, 70, 40, 18 ) ;
		
		Label label = new Label ( shell, SWT.SEPARATOR | SWT.HORIZONTAL ) ;
		label.setBounds( 10, 110, 600, 2);
		
		//-------------------------------
		final Button btnRemenberMyChoices = new Button ( shell, SWT.CHECK ) ;
		btnRemenberMyChoices.setBounds(17, 110, 200, 28);
		btnRemenberMyChoices.setText ( "Remember my choices" ) ;			
		//-------------------------------
		final Button downloadButton = new Button ( shell, SWT.NONE ) ;
		downloadButton.setBounds(500, 140, 90, 20);
		downloadButton.setText ( "Download" ) ;
		downloadButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				if ( comboTarget.getText().equals ( invitation ) ) {
					comboTarget.setForeground ( RED ) ;
					return ;
				}
				else {
					targetName = comboTarget.getText() ;	
					for( Target target : model.getTargets () ) {
						if ( ( targetName != null ) && ( target.getName().equals ( targetName ) ) ) {
							savePreferences () ;

							// Let's start downloading
							target.downloadRequested ( binaryFileText.getText () ) ;

							// and say good bye 
							shell.dispose () ;						
						}
					}
				}
			}
			private void savePreferences () {
				rememberMyChoices =  btnRemenberMyChoices.getSelection () ;
				TrackerPreferences.storeValueBoolean 	 ( "OpenDownloadFilePopup" , "rememberMyChoices" , rememberMyChoices ) ;
				if ( rememberMyChoices ) {
					TrackerPreferences.storeValueString  ( "OpenDownloadFilePopup" , "binaryFile" , binaryFileText.getText () ) ;
				}
				TrackerPreferences.restorePreferences 	 ( "OpenDownloadFilePopup" , partService.getActivePart().getContext () ) ;
			}
		});

		//-------------------------------
		final Button cancelButton = new Button ( shell, SWT.NONE ) ;
		cancelButton.setBounds ( 380, 140, 90, 20 ) ;
		cancelButton.setText ( "Cancel" ) ;
					
		cancelButton.addSelectionListener ( new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				// forget everything and turn off the light !
				shell.dispose () ;
			}
		});

		// TODO : PREFERENCES btnRemenberMyChoices.setSelection( choice.equals("true") );
		btnRemenberMyChoices.setSelection ( rememberMyChoices ) ;

		// Highlight the default button
    	shell.setDefaultButton ( downloadButton ) ;
		}

	@Inject @Optional
	private void setPort ( @Named ( "binaryFile" ) String binaryFile ) {
		if ( binaryFile != null ) {
		this.binaryFile = binaryFile ;
		}
	}
	@Inject @Optional
	private void setRememberMyChoices ( @Named ( "rememberMyChoices" ) boolean rememberMyChoices ) {
		this.rememberMyChoices = rememberMyChoices ;
	}
}

