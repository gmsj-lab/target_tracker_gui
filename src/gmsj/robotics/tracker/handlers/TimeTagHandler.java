 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class TimeTagHandler {
		
	@Execute
	public void execute ( MHandledToolItem toolItem , EPartService partService  ) {
		boolean showTimeTag = false ;
		
		if ( toolItem.isSelected () ) {
			showTimeTag = true ;
		}	
		TrackerPreferences.storeValueBoolean  ( "Console" , "showTimeTag" , showTimeTag ) ;
		TrackerPreferences.restorePreferences ( "Console" , partService.getActivePart().getContext () ) ;
	}	
}