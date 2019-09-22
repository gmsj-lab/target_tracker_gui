package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class GraphPrefs {

	final static Color RED   = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_RED   ) ;
	final static Color BLACK = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_BLACK ) ;
	final static Color BLUE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_BLUE	 ) ;
	
	private GraphFigure	figure ;
	
	public static void definePreferencesCatalog ( String widgetName ) {
		
		TrackerPreferences.createStringPreferences 	( widgetName , "title"	 				, "<title>" 		) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showToolbar"	 		, true 				) ;
		TrackerPreferences.createStringPreferences 	( widgetName , "xAxisTitle" 			, "<xAxisTitle>" 	) ;
		TrackerPreferences.createStringPreferences 	( widgetName , "yAxisTitle" 			, "<yAxisTitle>" 	) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showMajorXGrid"	 		, true 				) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showMajorYGrid"	 		, true 				) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "xMinValue" 				, 0.0 				) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "yMinValue" 				, 0.0 				) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "xMaxValue" 				, 100.0 			) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "yMaxValue" 				, 100.0 			) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "xAutoScale"	 			, true 				) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "yAutoScale"	 			, true 				) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "xAutoScaleThreshold"	, 0.0 				) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "yAutoScaleThreshold"	, 0.0 				) ;
	}
	public void init ( GraphFigure figure ) {
		this.figure = figure ;
	}
	@Inject @Optional
	public void setTitle 					( @Named ( "title" ) String title ) {
		if ( figure != null ) figure.getXyGraph ().setTitle ( title ) ;
	}
	@Inject @Optional
	public void setShowToolbar 				( @Named ( "showToolbar" ) boolean showToolbar ) {
		if ( figure != null ) figure.getToolbar ().setShowToolbar ( showToolbar ) ;
	}
	@Inject @Optional
	public void setXAxisTitle 				( @Named ( "xAxisTitle" ) String xAxisTitle ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryXAxis ().setTitle ( xAxisTitle ) ;
	}
	@Inject @Optional
	public void setYAxisTitle 				( @Named ( "yAxisTitle" ) String yAxisTitle ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryYAxis ().setTitle ( yAxisTitle ) ;
	}
	@Inject @Optional
	public void setXAxisShowMajorGrid 		( @Named ( "showMajorXGrid" ) boolean showMajorXGrid ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryXAxis ().setShowMajorGrid ( showMajorXGrid ) ;
	}
	@Inject @Optional
	public void setYAxisShowMajorGrid 		( @Named ( "showMajorYGrid" ) boolean showMajorYGrid ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryYAxis ().setShowMajorGrid ( showMajorYGrid ) ;
	}
	@Inject @Optional
	public void setXAxisRange 				( @Named ( "xMinValue" ) double xMinValue , @Named ( "xMaxValue" ) double xMaxValue ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryXAxis ().setRange ( xMinValue , xMaxValue ) ;
	}
	@Inject @Optional
	public void setYAxisRange 				( @Named ( "yMinValue" ) double yMinValue , @Named ( "yMaxValue" ) double yMaxValue ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryYAxis ().setRange ( yMinValue , yMaxValue ) ;
	}
	@Inject @Optional
	public void setXAxisAutoScale 			( @Named ( "xAutoScale" ) boolean xAutoScale ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryXAxis ().setAutoScale ( xAutoScale ) ;
	}
	@Inject @Optional
	public void setYAxisAutoScale 			( @Named ( "yAutoScale" ) boolean yAutoScale ) {
		if ( figure != null ) figure.getXyGraph ().getPrimaryYAxis ().setAutoScale ( yAutoScale ) ;
	}
	@Inject @Optional
	public void setXAxisAutoScaleThreshold 	( @Named ( "xAutoScaleThreshold" ) double xAutoScaleThreshold ) {
		if ( ( xAutoScaleThreshold < 0.0 ) || ( xAutoScaleThreshold > 1.0 ) ) {
			System.out.println ( "xAutoScaleThreshold must be in the range [ 0.0 , 1.0 ]" ) ;
		}
		else if ( figure != null ) figure.getXyGraph ().getPrimaryXAxis ().setAutoScaleThreshold ( xAutoScaleThreshold ) ;
	}
	@Inject @Optional
	public void setYAxisAutoScaleThreshold 	( @Named ( "yAutoScaleThreshold" ) double yAutoScaleThreshold ) {
		if ( ( yAutoScaleThreshold < 0.0 ) || ( yAutoScaleThreshold > 1.0 ) ) {
			System.out.println ( "yAutoScaleThreshold must be in the range [ 0.0 , 1.0 ]" ) ;
		}
		else if ( figure != null ) figure.getXyGraph ().getPrimaryYAxis ().setAutoScaleThreshold ( yAutoScaleThreshold ) ;
	}	
}
