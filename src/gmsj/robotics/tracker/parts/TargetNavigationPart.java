 
package gmsj.robotics.tracker.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

import gmsj.robotics.tracker.controler.Drag;
import gmsj.robotics.tracker.controler.IDragSource;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.model.ITargetAttribute;
import gmsj.robotics.tracker.model.Model;
import gmsj.robotics.tracker.model.ModelChange;
import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.model.TargetAttributeGroup;
import gmsj.robotics.tracker.navigation.TreeContentProvider;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.navigation.AttributeLabelProvider;

@SuppressWarnings("restriction")
public class TargetNavigationPart implements IDragSource {
	
	private static 	TreeViewer 		viewer ;	
	@Inject private Model 			model ;
	@Inject private MApplication 	application ;
	@Inject private EMenuService 	menuService ;
	static 	private ECommandService commandService ;
	static 	private EHandlerService handlerService ;
	
	@PostConstruct
	public void postConstruct (  ECommandService commandService , EHandlerService handlerService , Composite parent ) {
		TargetNavigationPart.commandService = commandService ;
		TargetNavigationPart.handlerService = handlerService ;
		createControl ( parent ) ;
		menuService.registerContextMenu ( viewer.getControl ()  , Tracker.TARGET_POPUP_MENU ) ;
	}

	@Inject 
	@Optional
	public void setViewerInput ( ModelChange change ) {

		if ( ( viewer != null ) && ( ! viewer.getControl().isDisposed() ) ) {
			
			viewer.setInput	 ( null ) ;
			viewer.setInput	 ( model ) ;
			viewer.expandAll ( ) ;
		}
	}
	@Focus
	public void onFocus() {
	    viewer.getControl().setFocus () ;
	}	
	private void createControl ( Composite parent ) {
		
		final int treeStyle = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER ;
		  
		viewer = new TreeViewer		( parent, treeStyle ) ;
		viewer.setContentProvider	( new TreeContentProvider () ) ;
		viewer.setLabelProvider 	( new DelegatingStyledCellLabelProvider ( new AttributeLabelProvider ( ) ) ) ;
		viewer.setInput				( model ) ; 
		viewer.expandAll 			() ;
		
		new Drag ().setDrag ( this ) ;

		hookListeners () ;
	}

	private void hookListeners () {
		
		viewer.addDoubleClickListener ( new IDoubleClickListener () {
			@Override
			public void doubleClick ( DoubleClickEvent e ) {
				TargetNavigationPart.handleDoubleClick ( ( IStructuredSelection ) e.getSelection () ) ;
			}
		});		
	}
	public static Target getSelectedTarget () {
		return ( (Target)((TreeSelection) viewer.getSelection()).getFirstElement()) ;
	}
	private static void handleDoubleClick ( IStructuredSelection selection ) {
		
		if ( selection.getFirstElement () instanceof Target ) {
			String targetName = ( (Target) selection.getFirstElement()).getName () ;
			Map <String, String> parameters = new HashMap <String, String> () ;	
			
			parameters.put ( Tracker.CLOSE_TARGET_PARAM_ID , targetName ) ;
			Command cmd = commandService.getCommand ( Tracker.CLOSE_TARGET_COMMAND_ID ) ;
			final ParameterizedCommand pcmd = ParameterizedCommand.generateCommand ( cmd, parameters ) ;
			handlerService.executeHandler ( pcmd ) ;
		}
	}
	@Override
	public Control getDnDControl () {
		return viewer.getTree () ;
	}
	@Override
	public boolean isDragOk ( ) {
		Object selection =  viewer.getTree ().getSelection ()[0].getData () ;
		boolean dragOk =  ! ( ( selection instanceof Target ) || ( selection instanceof TargetAttributeGroup ) ) ;
		
		if ( dragOk ) {		
			// tell all widgets to activate drop of attribute
			application.getContext ().set ( WidgetAttribute.class, new WidgetAttribute ( ( ITargetAttribute ) selection ) ) ;
		}
		return dragOk ;
	}
	@Override
	public String getDraggedElement () {
		ITargetAttribute targetAttribute = ( ITargetAttribute ) ( viewer.getTree ().getSelection ()[0].getData () ) ;
		WidgetAttribute widgetAttribute = new WidgetAttribute ( targetAttribute ) ;
		return ( widgetAttribute.toString () ) ;
	}
	@Override
	public void dragFinishedSuccessfully ( ) {	
		// tell all widgets to deactivate drop of attribute
		application.getContext ().set ( WidgetAttribute.class, null ) ;
	}
	@Override
	public void dragAborted () {
		// tell all widgets to deactivate drop of attribute
		application.getContext ().set ( WidgetAttribute.class, null ) ;
	}
}