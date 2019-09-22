
package gmsj.robotics.tracker.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.model.CloseTargetPopup;
import gmsj.robotics.tracker.model.Model;
import gmsj.robotics.tracker.parts.TargetNavigationPart;

public class CloseTargetHandler {
	@Execute
	public void execute ( @Optional @Named ( Tracker.CLOSE_TARGET_PARAM_ID ) String targetName , Model model , Shell shell ) {
		if ( ( targetName == null ) || ( targetName.contains ( "selection" ) ) ) {
			if (TargetNavigationPart.getSelectedTarget () != null ) {
				targetName = TargetNavigationPart.getSelectedTarget ().getName () ;		
			}
		}
		if ( targetName != null ) {
			CloseTargetPopup dialog = new CloseTargetPopup ( shell , model , targetName ) ;    	
			String selectedTargetName = (String) dialog.open () ;	

			if ( selectedTargetName != null ) {
				model.closeTarget ( selectedTargetName ) ;
			}
		}
	}
}

//TODO : tester et finir de coder ?