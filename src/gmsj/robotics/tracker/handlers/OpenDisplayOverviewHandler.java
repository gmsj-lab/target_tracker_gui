 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Tracker;

public class OpenDisplayOverviewHandler {
	
	@Execute
	 public void execute ( EPartService partService ) {

		MPart displayOverviewPart = partService.findPart ( Tracker.DISPLAY_OVERVIEW_ID ) ;

		partService.showPart ( displayOverviewPart , PartState.ACTIVATE ) ;
	}
	@CanExecute
	public boolean canExecute ( EPartService partService ) {

		if ( partService != null ) {
			
			MPart displayOverviewPart = partService.findPart ( Tracker.DISPLAY_OVERVIEW_ID ) ;

			if ( ! partService.isPartVisible ( displayOverviewPart ) ) {
				
				return true ;
				}
			}
		return false ;
	}		
}