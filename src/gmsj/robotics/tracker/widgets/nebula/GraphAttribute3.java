package gmsj.robotics.tracker.widgets.nebula;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider.UpdateMode;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.BaseLine;
import org.eclipse.swt.widgets.Display;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.WidgetAttribute.IAttributeListener;
import gmsj.robotics.tracker.widgets.prefs.GraphAttributePrefs;
import gmsj.robotics.tracker.widgets.prefs.GraphFigure;

public class GraphAttribute3 implements ITreeElement , ISelectedPreferenceTarget , IAttributeListener {

	public  static  String 				name 		= "GRAPH_TRACE" ;	
	private String 						id ;
	private WidgetAttribute 			attribute ;
	private GraphFigure 				figure ;
	private CircularBufferDataProvider 	traceProvider ;
	private Trace 						trace ;
	private ITreeElement 				parent ;
	private IEclipseContext				context ;
	private GraphAttributePrefs 		graphAttributePrefs ;
	private String 						parentId ;
	private GraphWidget 				parentWidget ;
	private List < Double >				currentXDataArray 	= new ArrayList < Double > () ;
	private List < Double >				currentYDataArray 	= new ArrayList < Double > () ;
	private boolean 					running = true ;


	static public void definePreferencesCatalog () {
		GraphAttributePrefs.definePreferencesCatalog ( name ) ;
	}

	GraphAttribute3 () {

		traceProvider = new CircularBufferDataProvider ( true ) ;
		traceProvider.setBufferSize  ( 100000 ) ;
		traceProvider.setUpdateDelay ( 0 ) ;  
		traceProvider.setUpdateMode  ( UpdateMode.X_AND_Y );
	}
	void init ( GraphFigure xyGraphFigure , WidgetAttribute targetAttribute, IEclipseContext parentContext, 
			String parentId, GraphWidget graphWidget ) {

		this.parentWidget 	= graphWidget ;
		this.parentId 		= parentId ;
		this.attribute 		= targetAttribute ;
		this.figure 		= xyGraphFigure ;

		trace 				= new Trace ( attribute.getName () , 
										figure.getXyGraph ().getPrimaryXAxis () ,
										figure.getXyGraph ().getPrimaryYAxis () , 
										traceProvider ) ;

		trace.setDataProvider 		( traceProvider ) ;
		trace.setBaseLine			( BaseLine.NEGATIVE_INFINITY ) ;
		trace.setAreaAlpha 			( 100 ) ;
		trace.setAntiAliasing 		( false ) ; 
		trace.setErrorBarEnabled	( false ) ;

		context = EclipseContextFactory.create () ;
		context.setParent ( parentContext ) ;	

		figure.addTrace	( trace ) ;	

		graphAttributePrefs = ContextInjectionFactory.make ( GraphAttributePrefs.class, context ) ; 
		graphAttributePrefs.init ( trace , traceProvider ) ;

		TrackerPreferences.defineCatalog ( parentId , getId () , "GRAPH_TRACE"  ) ;
		TrackerPreferences.restorePreferences ( parentId , getId () , context ) ;	

		// TODO : OBJECTIF : si le nom et la couleur n'ont pas été changées par le user, proposer le nom de l'attribut 
		// et une couleur differente à chaqye nouveau attribut
		// POUR CELA : regarder dans les pref si la valeur name = GRAPH_TRACE, si oui, do it ! 
		//			=> faire un storeValue sur le "name" et la traceColor
		// POUR l'instant, on fait context.set ( "name" ,  attribute.getName () ) ,
		//	MAIS CELA NE SERA PAS SUFFISANT...(pas ecrit en pref)?
		context.set ( "name" ,  attribute.getName () ) ;
		
		Runnable traceUpdater = new Runnable () {
			@Override
			public void run () {
				if ( running ) {
					updateGraph () ; 
					Display.getCurrent ().timerExec ( 100, this ) ;
				}
			}
		};

		Display.getCurrent ().timerExec ( 100, traceUpdater ) ;
	}
	public void removeAttribute () {
		running = false ;
		figure.removeTrace ( trace ) ;
	}
	public WidgetAttribute getAttribute () {
		return attribute ;
	}
	@Override
	public ITreeElement getParent () {
		return parent ;
	}
	@Override
	public ITreeElement [] getChildren () {
		return null ;
	}
	@Override
	public boolean hasChildren () {
		return false;
	}
	@Override
	public String getName () {
		return attribute.getName () ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {
		this.parent = parent ;
	}
	public void setId ( String id ) {
		this.id = id ; 
	}
	@Override
	public String getId ( ) {
		return id ;
	}
	@Override
	public ArrayList < Property > getPreferences ( boolean showAll ) {
		return TrackerPreferences.getPreferences ( getId () , parentId , context , showAll ) ;
	}
	public GraphWidget getParentWidget () {
		return parentWidget ;		
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
//		final Sample sample ;

		if ( true /* xAxisTargetTime */) {
			currentXDataArray.add ( event.getTimeTag() ) ;
			currentYDataArray.add ( event.getValue() ) ;			
		}
//		else {
//			// TODO XY
//			sample = new Sample ( event.getTimeTag() , event.getValue() ) ;
//			traceProvider.addSample ( sample ) ;
//		}		
	}
	private void updateGraph () {
		if ( currentXDataArray.size () > 0 ) {
			double [] xArray = new double [ currentXDataArray.size () ] ;
			double [] yArray = new double [ currentYDataArray.size () ] ;

			int i = 0 ;
			for ( double x : currentXDataArray ) {
				xArray [ i ++ ] = x ;
			}
			int j = 0 ;
			for ( double y : currentYDataArray ) {
				yArray [ j ++ ] =  y ;
			}
			traceProvider.setCurrentXDataArray ( xArray ) ;
			traceProvider.setCurrentYDataArray ( yArray ) ;
//			if ( count++ == 10 ) {
//				traceProvider.triggerUpdate () ;
//				count = 0 ;
//			}
			
			currentXDataArray.clear () ;
			currentYDataArray.clear () ;
		}
	}
}
