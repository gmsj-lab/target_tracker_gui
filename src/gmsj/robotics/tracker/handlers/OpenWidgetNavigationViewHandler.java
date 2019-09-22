 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Tracker;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class OpenWidgetNavigationViewHandler {

	@Execute
	 public void execute( EPartService partService ) {
	    
		MPart widgetNavigationPart 	= partService.findPart ( Tracker.WIDGET_NAVIGATION_ID ) ;
		MPart widgetPreviewPart 	= partService.findPart ( Tracker.WIDGET_PREVIEW_ID ) ;
		
		partService.showPart ( widgetNavigationPart , PartState.ACTIVATE ) ;
		partService.showPart ( widgetPreviewPart , PartState.ACTIVATE ) ;
	}
	@CanExecute
	public boolean canExecute( EPartService partService ) {
		
		if ( partService != null ) {
			
			MPart widgetNavigationPart 	= partService.findPart ( Tracker.WIDGET_NAVIGATION_ID ) ;
			MPart widgetPreviewPart 	= partService.findPart ( Tracker.WIDGET_PREVIEW_ID ) ;
			
			if ( ! partService.isPartVisible ( widgetPreviewPart ) ) {
				return true ;
				}
			if ( ! partService.isPartVisible ( widgetNavigationPart ) ) {
				return true ;
				}
			}
		return false;
	}		
}