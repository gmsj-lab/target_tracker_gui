
package gmsj.robotics.tracker.handlers;

import java.util.Collection;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.SideValue;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.Tracker;

public class SwitchPerspectiveHandler {

	@Execute
	public void execute ( MApplication application , EPartService partService , EModelService modelService , MWindow window , IEventBroker broker ) {

		Collection < MPart > parts 		= partService.getParts() ;
		MTrimmedWindow trimmedWindow 	= (MTrimmedWindow) modelService.find ( Tracker.TRIMMED_WINDOW_ID , application ) ;
		MTrimBar trimBar 				= modelService.getTrim ( trimmedWindow ,  SideValue.TOP ) ;

		MPart widgetOverviewPart 		= partService.findPart ( Tracker.WIDGET_PREFERENCES_ID 		) ;
		MPart navPart 					= partService.findPart ( Tracker.TARGET_NAVIGATION_ID 	) ;    
		MPart widgetNavigationPart 		= partService.findPart ( Tracker.WIDGET_NAVIGATION_ID 		) ;
		MPart widgetPreviewPart 		= partService.findPart ( Tracker.WIDGET_PREVIEW_ID 			) ;
		MPart displayOverviewPart 		= partService.findPart ( Tracker.DISPLAY_OVERVIEW_ID 		) ;
		//MPart consolePart 				= partService.findPart ( Tracker.CONSOLE_ID 				) ;

		window.setVisible ( false ) ;

		if ( ! Controler.getInstance ().isDisplayPerspectiveOn ) {

			trimBar.setVisible ( false ) ;

			partService.hidePart ( widgetOverviewPart 	) ;
			partService.hidePart ( navPart 				) ;
			partService.hidePart ( widgetNavigationPart ) ;
			partService.hidePart ( widgetPreviewPart 	) ;
			partService.hidePart ( displayOverviewPart 	) ;
			//partService.hidePart ( consolePart			) ;

			for ( MPart part : parts ) {

				if ( part.getElementId().equals ( Tracker.DISPLAYVIEW_ID ) ) {
					part.setCloseable ( false ) ;

					part.getToolbar().getChildren().get(2).setVisible ( false ) ; 
					part.getToolbar().getChildren().get(3).setVisible ( false ) ;
					part.getToolbar().getChildren().get(4).setVisible ( false ) ;
				}
			}
		}
		else {

			trimBar.setVisible ( true ) ;

			partService.showPart ( navPart 				, PartState.ACTIVATE ) ;
			partService.showPart ( widgetNavigationPart , PartState.ACTIVATE ) ;
			partService.showPart ( widgetPreviewPart 	, PartState.ACTIVATE ) ;
			partService.showPart ( displayOverviewPart 	, PartState.ACTIVATE ) ;	
			//partService.showPart ( consolePart 			, PartState.ACTIVATE ) ;
			partService.showPart ( widgetOverviewPart 	, PartState.ACTIVATE ) ;

			for ( MPart part : parts ) {

				if ( part.getElementId().equals ( Tracker.DISPLAYVIEW_ID ) ) {
					part.setCloseable ( true ) ;

					part.getToolbar().getChildren().get(2).setVisible ( true ) ; 
					part.getToolbar().getChildren().get(3).setVisible ( true ) ; 
					part.getToolbar().getChildren().get(4).setVisible ( true ) ; 
				}
			}
		}
		window.setVisible ( true ) ;

		Controler.getInstance ().isDisplayPerspectiveOn = ! Controler.getInstance ().isDisplayPerspectiveOn ;
		broker.send ( Tracker.TOPIC_DISPLAY_PERSPECTIVE , Controler.getInstance ().isDisplayPerspectiveOn ) ;
	}
}

