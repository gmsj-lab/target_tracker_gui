package gmsj.robotics.tracker.widgets.nebula;

import java.util.ArrayList;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.prefs.GraphFigure;
import gmsj.robotics.tracker.widgets.prefs.GraphPrefs;

public class GraphWidget extends TrackerWidget {

	public  static  String 									name 		= "Graph" ;	
	private 				GraphFigure						figure ;
	private 				GraphPrefs						graphPrefs ;
	private 				ArrayList < GraphAttribute >	graphAttributes 	= new ArrayList < GraphAttribute > () ;

	@Inject 				MApplication 					application ;

	static public void definePreferencesCatalog ( ) {
		GraphPrefs.definePreferencesCatalog ( name ) ;
	}
	public GraphWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void createWidget ( Composite parent ) {

		if ( figure != null ) figure.erase () ;
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		figure 	   = new GraphFigure  () ;
		graphPrefs = ContextInjectionFactory.make ( GraphPrefs.class, context ) ; 
		graphPrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;

		restoreAttributes () ;
	}
	@Override
	public boolean addAttribute ( WidgetAttribute targetAttribute ) {

		if ( isAllowed ( targetAttribute ) ) {
			GraphAttribute graphAttribute = ContextInjectionFactory.make ( GraphAttribute.class, context ) ; 
			graphAttribute.setId ( "GRAPH_TRACE_" + String.valueOf ( graphAttributes.size () ) ) ;
			graphAttributes.add ( graphAttribute ) ; 
			graphAttribute.setParent ( this ) ;
			graphAttribute.init ( figure , targetAttribute , parentContext , getId () , this ) ; 
			targetAttribute.registerForAttribute ( graphAttribute , false ) ;
			attributes.put ( targetAttribute.getId () , targetAttribute ) ;
			application.getContext().set ( String.class, "AttributeChanged" ) ;
			application.getContext ().modify ( ISelectedPreferenceTarget.class, this ) ;				

			TrackerPreferences.storeAttributes ( getId () , attributes ) ;

			return true ;
		}
		return false ;
	}
	@Override
	public void removeAttribute ( WidgetAttribute attribute ) {
		GraphAttribute graphAttribute = null ;
		for ( GraphAttribute candidate : graphAttributes ) {

			if ( candidate.getAttribute ().getId ().equals ( attribute.getId () ) ) {
				graphAttribute = candidate ;
			}
		}

		if ( graphAttribute != null ) {
			graphAttribute.removeAttribute () ;
			graphAttributes.remove ( graphAttribute ) ; 
		}
		
		attribute.unRegisterForAttribute () ;
		attributes.remove ( attribute.getId () ) ;

		TrackerPreferences.storeAttributes ( getId () , attributes ) ;
	}
	@Override
	public ITreeElement[] getChildren () {
		ITreeElement [] children = new ITreeElement [0] ;
		children = ( ITreeElement [] ) graphAttributes.toArray ( new  ITreeElement [0] ) ;

		return children ;
	}
	@Override
	public boolean hasChildren () {
		return ! graphAttributes.isEmpty () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		// Do nothing
	}
	@Override
	public void dispose () {	
		if ( graphAttributes != null ) {
			for ( GraphAttribute ga : graphAttributes ) {
				ga.removeAttribute () ;
				}
			}
		super.dispose () ;
	}
}
