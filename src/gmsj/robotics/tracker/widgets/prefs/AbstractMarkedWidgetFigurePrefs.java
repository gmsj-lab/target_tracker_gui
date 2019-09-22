package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.AbstractMarkedWidgetFigure;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class AbstractMarkedWidgetFigurePrefs extends FigurePrefs {

	final static Color RED   = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_RED   ) ;
	final static Color GREEN = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_GREEN ) ;
	final static Color BLACK = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_BLACK ) ;
	final static Color BLUE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_BLUE	 ) ;
	
	private  AbstractMarkedWidgetFigure figure ;
	
	public void init ( AbstractMarkedWidgetFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	public static void definePreferencesCatalog ( String widgetName ) {

		TrackerPreferences.createColorPreferences 	( widgetName , "hiColor" 				, RED	) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "hihiColor" 				, BLACK ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "loColor" 				, GREEN ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "loloColor" 				, BLUE	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "hiLevel" 				, 80.0 	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "hihiLevel" 				, 90.0 	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "loLevel" 				, 20.0 	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "loloLevel" 				, 10.0 	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "min" 					, 0.0 	) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "max" 					, 100.0 ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showHi"    				, true  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showHihi"    			, true  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showLo"    				, true  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showLolo"    			, true  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showMarkers"    		, true  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "logScale"    			, false ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "showMinorTicks"    		, true  ) ;
		TrackerPreferences.createIntPreferences 	( widgetName , "majorTickMarkStepHint"  , 0 	) ;		
		
		FigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	@Inject @Optional
	public void setHiColor		( @Named ( "hiColor" ) Color hiColor ) {
		if ( figure != null ) figure.setHiColor ( hiColor ) ;
	}	
	@Inject @Optional
	public void setHihiColor 	( @Named ( "hihiColor" ) Color hihiColor ) {
		if ( figure != null ) figure.setHihiColor ( hihiColor ) ;
	}
	@Inject @Optional
	public void setLoColor 		( @Named ( "loColor" ) Color loColor ) {
		if ( figure != null ) figure.setLoColor ( loColor ) ;
	}
	@Inject @Optional
	public void setLoloColor 	( @Named ( "loloColor" ) Color loloColor ) {
		if ( figure != null ) figure.setLoloColor ( loloColor ) ;
	}
	@Inject @Optional 
	public void setHiLevel 		( @Named ( "hiLevel" ) double hiLevel ) {
		if ( figure != null ) figure.setHiLevel ( hiLevel ) ;
	}
	@Inject @Optional
	public void setHihiLevel 	( @Named ( "hihiLevel" ) double hihiLevel ) {
		if ( figure != null ) figure.setHihiLevel ( hihiLevel ) ;
	}
	@Inject @Optional
	public void setLoLevel 		( @Named ( "loLevel" ) double loLevel ) {
		if ( figure != null ) figure.setLoLevel ( loLevel ) ;
	}
	@Inject @Optional
	public void setLoloLevel 	( @Named ( "loloLevel" ) double loloLevel ) {
		if ( figure != null ) figure.setLoloLevel ( loloLevel ) ;
	}
	@Inject @Optional
	public void setRange		( @Named ( "min" ) double min , @Named ( "max" ) double max ) {
		if ( figure != null ) figure.setRange ( min , max ) ;
	}
	@Inject @Optional
	public void setShowHi 		( @Named ( "showHi" ) boolean showHi ) {
		if ( figure != null ) figure.setShowHi ( showHi ) ;
	}
	@Inject @Optional
	public void setShowHihi 	( @Named ( "showHihi" ) boolean showHihi ) {
		if ( figure != null ) figure.setShowHihi ( showHihi ) ;
	}
	@Inject @Optional
	public void setShowLo 		( @Named ( "showLo" ) boolean showLo ) {
		if ( figure != null ) figure.setShowLo ( showLo ) ;
	}
	@Inject @Optional
	public void setShowLolo 	( @Named ( "showLolo" ) boolean showLolo ) {
		if ( figure != null ) figure.setShowLolo ( showLolo ) ;
	}
	@Inject @Optional
	public void setShowMarkers 	( @Named ( "showMarkers" ) boolean showMarkers ) {
		if ( figure != null ) figure.setShowMarkers ( showMarkers ) ;
	}	
	@Inject @Optional
	public void setLogScale		( @Named  ( "logScale" ) boolean logScale ) {
		if ( figure != null ) figure.setLogScale ( logScale ) ;
	}
	@Inject @Optional
	public void setShowMinorTicks ( @Named ( "showMinorTicks" ) final boolean showMinorTicks ) {
		if ( figure != null ) figure.setShowMinorTicks ( showMinorTicks ) ;
	}
	@Inject @Optional
	public void setMajorTickMarkStepHint ( @Named  ( "majorTickMarkStepHint" ) int majorTickMarkStepHint ) {
		if ( figure != null ) figure.setMajorTickMarkStepHint ( majorTickMarkStepHint ) ;
	}

}
