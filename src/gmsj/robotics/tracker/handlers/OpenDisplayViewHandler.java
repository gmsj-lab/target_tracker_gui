
package gmsj.robotics.tracker.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;

import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.parts.OpenDisplayPopup;
import gmsj.robotics.tracker.parts.OpenDisplayPopup.OpenDisplayPopupResponse;

public class OpenDisplayViewHandler {

	@Execute
	public void execute ( Shell shell , EPartService partService , MApplication application , EModelService modelService ) {

		MPart displayPart 					= null ;
		ArrayList < MPart >  displayParts 	= new ArrayList < MPart > ()  ;
		Collection < MPart > parts 			= partService.getParts () ;

		for ( MPart part : parts ) {
			if ( part.getElementId().equals ( Tracker.DISPLAYVIEW_ID ) ) {
				if ( part.getLabel () != null ) {
					displayParts.add ( part ) ;
				}
			}
		}

		OpenDisplayPopup dialog 		  = new OpenDisplayPopup ( shell , displayParts ) ;    	
		OpenDisplayPopupResponse response = (OpenDisplayPopupResponse) dialog.open () ;

		if ( response.isValid ) {

			for ( MPart part : displayParts ) {

				if ( part.getLabel().equals ( response.selectedItemsText ) ) {
					displayPart = part ;
					break ;
				}
			}
			if ( displayPart == null ) {

				displayPart = partService.createPart ( Tracker.DISPLAYVIEW_ID ) ;
				displayPart.setLabel ( response.selectedItemsText ) ;

				List < MPartStack > stacks = modelService.findElements ( application, Tracker.DISPLAYVIEW_PARTSTACK_ID , MPartStack.class, null ) ;
				stacks.get( 0 ).getChildren().add ( displayPart ) ;
			}
			partService.showPart ( displayPart , PartState.ACTIVATE ) ;	
		}	
	}
}