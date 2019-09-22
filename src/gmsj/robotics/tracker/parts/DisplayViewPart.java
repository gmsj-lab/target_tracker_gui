package gmsj.robotics.tracker.parts;
/*
 * This part is responsible for :
 * 	- accepting dropping of widgets: DisplayPart will call widget constructor 
 *  - refuse dropping of target attributes: widgets of the part should have accept the drop of attribute
 *  	Note: 
 *  		- widgets accept dropping of attributes, always ( multi-attributes widgets or mono: should popup confirmation of replacement of attribute to user) 
 *  		- but should also accept widgets and pass them back to the display (display will have register for that)
 *  - register to newly accepted widgets for getting drops of other widgets
 *  - changeViewOrientation: a new composite will be created, old one disposed : probably need a command to widgets to rebuild themselves
 *  	 ( or let them discover that by injection of composite...dangerous ?) 
 * */

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.Drop;
import gmsj.robotics.tracker.controler.IDropTarget;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.WidgetFactory;

public class DisplayViewPart implements IDropTarget , ITreeElement , ISelectedPreferenceTarget {	

	private ArrayList < IWidget > 	widgetList 			= new ArrayList < IWidget > () ;
	private Composite 				displayComposite ;
	private int 					orientation ;
	private static String		 	partType = new String ( "Displays" ) ;	
	private String		 			partName ;	
	private Drop 					drop = new Drop () ;
	final static Color 				WHITE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_WHITE ) ;
	private Color 					backgroundColor = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_WHITE ) ;

	@Inject IEclipseContext 		context ;	
	@Inject MPart 					part ;	
	@Inject EModelService 			modelService ;
	@Inject MApplication 			application ;
	@Inject Composite 				parent ;
	@Inject Shell 					shell ;
//	@Inject MDirtyable 				dirty ;
	@Inject IEventBroker 			broker ;
	@Inject EMenuService 			menuService ;

	@Optional @Inject
	private void dndOfAttributeInProgress ( WidgetAttribute selectedAttribute ) {
		if ( displayComposite != null ) {
			if ( selectedAttribute == null ) {
				drop.setDrop ( this ) ;	
			}
			else {
				drop.unsetDrop () ;					
			}
		}
	}
	@Inject @Optional
	private void setOrientation ( @Named ( "orientation" ) int orientation ) {

		this.orientation = orientation ;
		createControl () ;
	}
	@Inject @Optional 
	private void setBackground ( @Named ( "background" ) Color color ) {
		backgroundColor = color ;

		if ( displayComposite != null ) {
			displayComposite.setBackground ( backgroundColor ) ;
		}
	}
	@PostConstruct
	private void postConstruct () {

		partName = new String ( "Display_" + part.getLabel () ) ; 
		TrackerPreferences.defineCatalog ( partName , partType ) ;
		TrackerPreferences.restorePreferences ( partName , context ) ;	
		createControl () ;
		Controler.getInstance ().addDisplay ( this ) ;
	}
	static public void definePreferencesCatalog ()  {

		TrackerPreferences.createIntPrivatePreferences 	( partType , "orientation"  , SWT.VERTICAL  ) ;		
		TrackerPreferences.createColorPreferences 		( partType , "background"   , WHITE 		) ;
	}
	private void createControl () {

		if ( displayComposite != null ) {
			for ( IWidget widget : widgetList ) {
				widget.removeMouseListener ( mouseListener ( widget ) ) ;
				widget.dispose () ;
			}	
			widgetList.clear () ;
			displayComposite.dispose () ;
			drop.unsetDrop () ;
		}

		displayComposite = new Composite ( parent, SWT.NONE ) ;
		displayComposite.setLayout 	  	 ( new FillLayout ( orientation ) ) ;	
		displayComposite.setBackground 	 ( backgroundColor ) ;
		displayComposite.setData 		 ( this ) ;

		drop.setDrop ( this ) ;

		restoreWidgets () ;
		parent.layout () ;
		displayComposite.layout () ;
		menuService.registerContextMenu ( displayComposite  , Tracker.DELETE_POPUP_MENU ) ;

	}
	private void restoreWidgets () {
		for ( String widgetId : TrackerPreferences.getPartWidgets ( partName ) ) {	
			addWidget ( WidgetFactory.get ( widgetId , context ) ) ;
		}
	}
	private void addWidget ( IWidget widget ) {

		widget.createWidget ( displayComposite ) ;
		widget.setDrag () ;
		widget.addMouseListener ( mouseListener ( widget ) ) ;
		widget.setParent ( this ) ;
		displayComposite.layout () ;
		parent.layout () ;

		widgetList.add ( widget ) ;
		
		TrackerPreferences.storeWidgets ( partName , widgetList ) ;
		application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;
		application.getContext ().modify ( ISelectedPreferenceTarget.class, widget ) ;

	}
	public void removeWidget ( IWidget widget ) {

		widgetList.remove ( widget ) ;
		widget.removeMouseListener ( mouseListener ( widget ) ) ;
		widget.dispose () ;
		displayComposite.layout () ;
		parent.layout () ;

		TrackerPreferences.storeWidgets ( partName , widgetList ) ;
		application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;

	}
	public ArrayList < IWidget > getWidgets () {
		return widgetList ;
	}
	@Override
	public Control getDnDControl () {
		return displayComposite ;
	}
	@Override
	public void receiveDrop ( String widgetId ) {
		addWidget ( WidgetFactory.get ( widgetId , context ) ) ;	
	}
	@Override
	public boolean isDropOk () {
		return true ;
	}
	private MouseListener mouseListener ( final IWidget widget ) {
		return new MouseListener () {
			@Override
			public void mouseDown ( MouseEvent e ) {
				if ( e.button == 1 ) {
					application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;
					application.getContext ().modify ( ISelectedPreferenceTarget.class, widget ) ;
					
				}
				if ( e.button == 3 ) {
					System.out.println (" mouse contextuel: " +  widget.getName () + e.button) ;
					broker.send ( Tracker.TOPIC_LEFTCLICK_WIDGET , widget ) ;
					application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;
					application.getContext ().modify ( ISelectedPreferenceTarget.class, widget ) ;

				}
			}
			@Override
			public void mouseDoubleClick ( MouseEvent e ) {
				System.out.println (" mouseDoubleClick: " +  widget.getName () ) ;
				broker.send ( Tracker.TOPIC_DOUBLECLICK_WIDGET , widget ) ;
			}
			@Override
			public void mouseUp ( MouseEvent e ) {
			}
		};
	}
	@Persist
	public void save () {
//		MessageDialog dialog = new MessageDialog ( shell , "Closing display " + part.getLabel () , null , 
//				"Do you wish to close or delete display "  + part.getLabel () + "?" ,
//				MessageDialog.QUESTION, new String [] { "Close", "Delete" } , 0 ) ;
//
//		if ( dialog.open () == 1 ) {
//			System.out.println ( "Delete" ) ; 
//			part.setLabel ( null ) ;
//		}
//		dirty.setDirty ( false ) ;
	}
	@Focus
	public void onFocus () {
		displayComposite.setFocus () ;
	}
	@Override
	public ITreeElement getParent () {
		return Controler.getInstance () ;
	}
	@Override
	public ITreeElement [] getChildren () {
		ITreeElement [] widgets = new ITreeElement [0] ;

		if ( widgetList.size () > 0 ) {
			widgets = ( ITreeElement [] ) widgetList.toArray ( new  ITreeElement [0] ) ;
		}
		return widgets ;
	}
	@Override
	public boolean hasChildren () {
		return ( ! widgetList.isEmpty () ) ;
	}
	@Override
	public String getName () {
		return part.getLabel () ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {		
	}
	public ArrayList < Property > getPreferences ( boolean showAll ) {
		return TrackerPreferences.getPreferences ( partName , context , showAll ) ;
	}
	@PreDestroy
	private void predestroy () {
		Controler.getInstance ().removeDisplay ( this ) ;
	}
	@Override
	public String getId () {
		return "Display" ;
	}
}
