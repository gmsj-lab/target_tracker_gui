
package gmsj.robotics.tracker.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;

import gmsj.robotics.tracker.controler.Drag;
import gmsj.robotics.tracker.controler.IDragSource;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.events.SelectedWidget;
import gmsj.robotics.tracker.navigation.TreeContentProvider;
import gmsj.robotics.tracker.navigation.WidgetLabelProvider;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.WidgetCatalog;
import gmsj.robotics.tracker.widgets.WidgetFactory;

public class WidgetNavigationPart implements IDragSource {

	private 		TreeViewer 		viewer ;
	@Inject private MApplication 	application ;
	@Inject private IEclipseContext context ;

	@PostConstruct
	public void postConstruct ( Composite parent ) {

		final int treeStyle = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER ;

		WidgetCatalog widgetCatalog = ContextInjectionFactory.make ( WidgetCatalog.class , context ) ;
		viewer = new TreeViewer		( parent, treeStyle ) ;
		viewer.setContentProvider	( new TreeContentProvider () ) ;
		viewer.setLabelProvider 	( new DelegatingStyledCellLabelProvider ( new WidgetLabelProvider () ) ) ;
		viewer.setInput				( widgetCatalog ) ;
		viewer.expandAll 			( ) ;

		new Drag ().setDrag ( this ) ;
		publishSelectedWidget () ;
	}
	@Inject @Optional
	public void toggleDrag ( @UIEventTopic (Tracker.TOPIC_DISPLAY_PERSPECTIVE) boolean displayPerspective ) {
		if ( displayPerspective ) {
			application.getContext ().set ( SelectedWidget.class, new SelectedWidget ( null ) ) ;
		}
		else {
			application.getContext ().set ( SelectedWidget.class, new SelectedWidget ( null ) ) ;
		}
	}
	@Focus
	public void onFocus () {
		viewer.getControl().setFocus () ;
	}
	private void publishSelectedWidget () {
		viewer.addSelectionChangedListener ( new ISelectionChangedListener () {

			public void selectionChanged ( SelectionChangedEvent event ) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection () ;

				if ( ( ! event.getSelection ().isEmpty () ) && ( selection.getFirstElement () instanceof IWidget ) ) {
					application.getContext ().set ( SelectedWidget.class, new SelectedWidget ( (IWidget ) selection.getFirstElement () ) ) ;
				}
				else {
					application.getContext ().set ( SelectedWidget.class, new SelectedWidget ( null ) ) ;
				}
			}
		});
	}
	@Override
	public Control getDnDControl ( ) {
		return viewer.getTree () ;
	}
	@Override
	public boolean isDragOk ( ) {	
		return ( viewer.getTree ().getSelection ()[0].getData () instanceof TrackerWidget ) ;
	}
	@Override
	public String getDraggedElement ( ) {
		String widgetName = (( TrackerWidget ) viewer.getTree ().getSelection ()[0].getData () ).getName () ;
		return WidgetFactory.createNewWidgetId ( widgetName ) ;
	}
	@Override
	public void dragFinishedSuccessfully () {		
	}
	@Override
	public void dragAborted () {
	}
}