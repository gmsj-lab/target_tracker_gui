package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Tracker;

public class OpenConsoleHandler {

	@Execute
	public void execute ( MApplication application , EPartService partService , EModelService modelService , MWindow window ) {

		MPart consolePart = partService.findPart ( Tracker.CONSOLE_ID ) ;
		
		partService.showPart( consolePart , PartState.ACTIVATE ) ;
	}
	@CanExecute
	public boolean canExecute ( MApplication application , EPartService partService , EModelService modelService ) {
		
		if ( partService != null ) {	
			
			MPart consolePart = partService.findPart ( Tracker.CONSOLE_ID ) ;
			
			if ( ! partService.isPartVisible( consolePart ) ) {
				
				return true ;
				}
			}
		return false ;
	}
}      
