 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.SWT;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class VerticalDisplayOrientationHandler {
	
	@Execute
	public void execute ( EPartService partService ) {
		
		String displayId   = "Display_" + partService.getActivePart().getLabel () ;
		
		TrackerPreferences.storeValueInt  	  ( displayId , "orientation" , SWT.VERTICAL ) ;
		TrackerPreferences.restorePreferences ( displayId , partService.getActivePart().getContext () ) ;
	}
}