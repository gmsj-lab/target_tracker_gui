 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Tracker;

public class OpenTargetNavigationViewHandler {
	
	@Execute
	 public void execute( EPartService partService ) {
	    
		MPart navPart = partService.findPart ( Tracker.TARGET_NAVIGATION_ID ) ;
		partService.showPart ( navPart , PartState.ACTIVATE ) ;
	}
	@CanExecute
	public boolean canExecute( EPartService partService ) {
		if ( partService != null ) {
			
			MPart navPart = partService.findPart ( Tracker.TARGET_NAVIGATION_ID ) ;
			if ( ! partService.isPartVisible ( navPart ) ) {
				return true ;
				}
			}
		return false;
	}		
}