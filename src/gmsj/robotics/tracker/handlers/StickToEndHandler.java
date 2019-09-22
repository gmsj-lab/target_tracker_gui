 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class StickToEndHandler {
		
	@Execute
	public void execute ( MHandledToolItem toolItem , EPartService partService ) {
		boolean stickToLastEntry = false ;
		
		if ( toolItem.isSelected () ) {
			stickToLastEntry = true ;
		}

		TrackerPreferences.storeValueBoolean  ( "Console" , "stickToLastEntry" , stickToLastEntry ) ;
		TrackerPreferences.restorePreferences ( "Console" , partService.getActivePart().getContext() ) ;
	}	
}