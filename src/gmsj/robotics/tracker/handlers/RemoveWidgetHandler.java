
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.nebula.GraphAttribute;

public class RemoveWidgetHandler {
	@Execute
	public void execute ( MApplication application , MHandledMenuItem toolItem , ISelectedPreferenceTarget selection ) {

		if ( selection != null ) {
			if ( ( selection instanceof IWidget ) && ( ((IWidget)selection).getDisplay () != null ) ) {
				((IWidget)selection).getDisplay().removeWidget ( (IWidget)selection ) ;
			}
			else if ( selection instanceof GraphAttribute ){
				WidgetAttribute attribute = ((GraphAttribute)selection).getAttribute () ;
				((GraphAttribute)selection).getParentWidget().removeAttribute ( attribute ) ;		
			}
			else if ( selection instanceof WidgetAttribute ) {
				WidgetAttribute widgetAttribute = (WidgetAttribute) selection ;
				
				if ( widgetAttribute.getParent () instanceof IWidget ) {
					((IWidget) widgetAttribute.getParent ()).removeAttribute ( widgetAttribute ) ;
				}
			}
			application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;
		}
	}
	@CanExecute
	public boolean canExecute ( ISelectedPreferenceTarget selection ) {
		boolean doIt = false ;
		if ( selection != null ) {
			if ( ( selection instanceof IWidget ) && ( ((IWidget)selection).getDisplay () != null ) ) {
				doIt = true ;		
			}
			else if ( selection instanceof GraphAttribute ) {
				doIt = true ;		
			}
			else if ( selection instanceof WidgetAttribute ) {
				doIt = true ;		
			}
		}
		return doIt ;		
	}	
}