package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.TankFigure;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class TankPrefs extends AbstractMarkedWidgetFigurePrefs {
	
	private TankFigure figure ;
	
	static public void definePreferencesCatalog ( String widgetName )  {

		TrackerPreferences.createBooleanPreferences ( widgetName , "effect3D"    		 , true ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "fillBackgroundColor" , BLUE ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "fillColor" 			 , BLUE ) ;
		TrackerPreferences.createColorPreferences   ( widgetName , "foregroundColor"     , RED  ) ;		
		
		AbstractMarkedWidgetFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	public void init ( TankFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	@Inject @Optional
	public void setEffect3D 			( @Named ( "effect3D" ) boolean effect3D ) {
		if ( figure != null ) figure.setEffect3D ( effect3D ) ;
	}
	@Inject @Optional
	public void setFillBackgroundColor 	( @Named ( "fillBackgroundColor" ) Color fillBackgroundColor ) {
		if ( figure != null ) figure.setFillBackgroundColor ( fillBackgroundColor ) ;
	}
	@Inject @Optional
	public void setFillColor 			( @Named ( "fillColor" ) Color fillColor ) {
		if ( figure != null ) figure.setFillColor ( fillColor ) ;
	}
	@Inject @Optional
	public void setForegroundColor 		( @Named ( "foregroundColor" ) Color foregroundColor ) {
		System.out.println ( "TankFigure::setForegroundColor" ) ;
		if ( figure != null ) figure.setForegroundColor ( foregroundColor ) ;
	}
}
