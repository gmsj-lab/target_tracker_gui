package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class GraphAttributePrefs {

	final static Color PURPLE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_PURPLE ) ;
	private Trace 						trace ;
	private CircularBufferDataProvider 	traceProvider ;
	
	static public void definePreferencesCatalog ( String name )  {
		
		TrackerPreferences.createColorPreferences 	( name , "traceColor" 			, PURPLE ) ;
		TrackerPreferences.createBooleanPreferences ( name , "antiAliasing"    		, true 	) ;
		TrackerPreferences.createBooleanPreferences ( name , "drawYErrorInArea" 	, false	) ;
		TrackerPreferences.createBooleanPreferences ( name , "errorBarEnabled" 		, false ) ;
		TrackerPreferences.createBooleanPreferences ( name , "focusTraversable" 	, true	) ;
		TrackerPreferences.createIntPreferences 	( name , "areaAlpha"  			, 100 	) ;		
		TrackerPreferences.createIntPreferences 	( name , "errorBarCapWidth" 	, 1 	) ;		
		TrackerPreferences.createIntPreferences 	( name , "lineWidth" 			, 1 	) ;		
		TrackerPreferences.createIntPreferences 	( name , "pointSize" 			, 2 	) ;		
		TrackerPreferences.createColorPreferences 	( name , "errorBarColor" 		, PURPLE ) ;
		TrackerPreferences.createStringPreferences 	( name , "traceType" 			, TraceType.SOLID_LINE.toString ()	) ;
		TrackerPreferences.createStringPreferences 	( name , "pointStyle" 			, PointStyle.XCROSS.toString ()	) ;
		TrackerPreferences.createStringPreferences 	( name , "name" 				, name	) ;
		TrackerPreferences.createIntPreferences 	( name , "updateDisplayDelay" 	, 0 	) ;		
	}
	public void init ( Trace trace , CircularBufferDataProvider traceProvider ) {
		this.trace 			= trace ;
		this.traceProvider 	= traceProvider ;
	}

	@Inject @Optional 
	private void setTraceColor	( @Named ( "traceColor" ) Color traceColor ) {
		// TODO : trouver une astuce pour garder les couleurs qui changent !
		//if ( trace != null ) trace.setTraceColor ( traceColor ) ;
	}
	@Inject @Optional
	private void setAntiAliasing ( @Named ( "antiAliasing" ) boolean antiAliasing ) {
		if ( trace != null ) trace.setAntiAliasing ( antiAliasing ) ;
	}
	@Inject @Optional
	private void setDrawYErrorInArea ( @Named ( "drawYErrorInArea" ) boolean drawYErrorInArea ) {
		if ( trace != null ) trace.setDrawYErrorInArea ( drawYErrorInArea ); 
	}
	@Inject @Optional
	private void setErrorBarEnabled ( @Named ( "errorBarEnabled" ) boolean errorBarEnabled ) {
		if ( trace != null ) trace.setErrorBarEnabled ( errorBarEnabled ) ; 
	}
	@Inject @Optional
	private void setFocusTraversable ( @Named ( "focusTraversable" ) boolean focusTraversable ) {
		if ( trace != null ) trace.setFocusTraversable ( focusTraversable ) ; 
	}
	@Inject @Optional
	private void setAreaAlpha ( @Named ( "areaAlpha" ) int areaAlpha ) {
		if ( trace != null ) trace.setAreaAlpha ( areaAlpha ) ; 
	}
	@Inject @Optional
	private void setErrorBarCapWidth ( @Named ( "errorBarCapWidth" ) int errorBarCapWidth ) {
		if ( trace != null ) trace.setErrorBarCapWidth ( errorBarCapWidth ) ; 
	}
	@Inject @Optional
	private void setLineWidth ( @Named ( "lineWidth" ) int lineWidth ) {
		if ( trace != null ) trace.setLineWidth ( lineWidth ) ; 
	}
	@Inject @Optional
	private void setPointSize ( @Named ( "pointSize" ) int pointSize ) {
		if ( trace != null ) trace.setPointSize ( pointSize ) ; 
	}
	@Inject @Optional
	private void setPointStyle ( @Named ( "pointStyle" ) String pointStyleString ) {

		PointStyle pointStyle = PointStyle.XCROSS ;
		for ( PointStyle candidate : PointStyle.values () ) {
			if ( candidate.toString ().equals ( pointStyleString ) ) {
				pointStyle = candidate ;
			}
		}
		if ( trace != null ) trace.setPointStyle ( pointStyle ) ; 
	}
	@Inject @Optional
	private void setErrorBarColor ( @Named ( "errorBarColor" ) Color errorBarColor ) {
		if ( trace != null ) trace.setErrorBarColor ( errorBarColor ) ; 
	}
	@Inject @Optional
	private void setTraceType ( @Named ( "traceType" ) String traceTypeString ) {
		TraceType traceType = TraceType.SOLID_LINE ;
		for ( TraceType candidate : TraceType.values () ) {
			if ( candidate.toString ().equals ( traceTypeString ) ) {
				traceType = candidate ;
			}
		}
		if ( trace != null ) trace.setTraceType ( traceType ) ; 
	}
	@Inject @Optional
	private void setName ( @Named ( "name" ) String name ) {
		if ( trace != null ) {
			trace.setName ( name ) ; 
		}
	}
	@Inject @Optional
	private void setUpdateDelay ( @Named ( "updateDisplayDelay" ) int updateDisplayDelay ) {
		if ( traceProvider != null ) {
			traceProvider.setUpdateDelay ( updateDisplayDelay ) ; 
		}
	}
}
