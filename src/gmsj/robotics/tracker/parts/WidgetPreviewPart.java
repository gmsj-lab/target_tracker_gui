
package gmsj.robotics.tracker.parts;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import gmsj.robotics.tracker.controler.Drag;
import gmsj.robotics.tracker.controler.IDragSource;
import gmsj.robotics.tracker.events.SelectedWidget;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.WidgetFactory;

public class WidgetPreviewPart implements IDragSource {

	private Composite 		displayComposite ;
	private IWidget 		widget 				= null ;
	private Drag			drag ;

	@Inject MApplication 	application ;

	@Inject
	WidgetPreviewPart ( Composite parent ) {
		displayComposite = new Composite ( parent, SWT.NONE ) ;
		displayComposite.setLayout 		 ( new FillLayout ( SWT.VERTICAL ) ) ;	
		displayComposite.setBackground 	 ( parent.getDisplay().getSystemColor ( SWT.COLOR_WHITE ) ) ;
		
		drag = new Drag () ;
	}
	@Optional @Inject
	private void selectedWidget ( SelectedWidget selectedWidget ) {
		removePreview  () ;
		widget = selectedWidget.get () ;
		if ( widget != null ) {
			displayPreview ( widget ) ;
		}
	}
	private void removePreview () {
		
		drag.unsetDrag () ;
		if ( widget != null ) {
			widget.dispose () ;
			widget = null ;
		}
		displayComposite.layout () ;		
	}
	private void displayPreview ( IWidget widget ) {
		widget.createWidget ( displayComposite ) ; 
		displayComposite.layout () ;
		drag.setDrag ( this ) ;
		application.getContext ().modify ( ISelectedPreferenceTarget.class, widget ) ;				
	}
	@Override
	public Control getDnDControl () {
		if ( displayComposite.getChildren ().length > 0 ) {
			return displayComposite.getChildren () [0] ;
		}
		return null ;
	}
	@Override
	public boolean isDragOk () {	
		return ( widget != null ) ;
	}
	@Override
	public String getDraggedElement ( ) {
		String widgetName = widget.getName () ;
		return WidgetFactory.createNewWidgetId ( widgetName ) ;
	}
	@Override
	public void dragFinishedSuccessfully () {		
	}
	@Override
	public void dragAborted () {		
	}
	@Focus
	private void onFocus () {
	}	
}