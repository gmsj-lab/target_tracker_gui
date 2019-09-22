 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Tracker;

public class OpenPreferencesHandler {
	
	@Execute
	 public void execute ( EPartService partService ) {
	    
		MPart preferencesPart = partService.findPart ( Tracker.WIDGET_PREFERENCES_ID ) ;
		
		partService.showPart ( preferencesPart , PartState.ACTIVATE ) ;
	}
	@CanExecute
	public boolean canExecute( EPartService partService ) {
		
		if ( partService != null ) {
			
			MPart preferencesPart = partService.findPart ( Tracker.WIDGET_PREFERENCES_ID ) ;
			
			if ( ! partService.isPartVisible ( preferencesPart ) ) {
				
				return true ;
				}
			}
		return false ;
	}		
}