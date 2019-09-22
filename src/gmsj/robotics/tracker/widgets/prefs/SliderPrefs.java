package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.ScaledSliderFigure;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class SliderPrefs extends AbstractMarkedWidgetFigurePrefs {
	
	private ScaledSliderFigure	figure ;
	
	static public void definePreferencesCatalog ( String widgetName )  {

		TrackerPreferences.createBooleanPreferences ( widgetName , "effect3D"    		 , true ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "fillBackgroundColor" , BLUE ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "fillColor" 			 , BLUE ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "horizontal"    		 , true ) ;
		TrackerPreferences.createDoublePreferences  ( widgetName , "stepIncrement"    	 , 1.0  ) ;
		TrackerPreferences.createColorPreferences   ( widgetName , "thumbColor"    		 , RED  ) ;

		AbstractMarkedWidgetFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	public void init ( ScaledSliderFigure figure ) {
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
	public void setHorizontal 			( @Named ( "horizontal" ) boolean horizontal ) {
		if ( figure != null ) figure.setHorizontal ( horizontal ) ;
	}
	@Inject @Optional
	public void setStepIncrement 		( @Named ( "stepIncrement" ) double stepIncrement ) {
		if ( figure != null ) figure.setStepIncrement ( stepIncrement ) ;
	}
	@Inject @Optional
	public void setThumbColor 			( @Named ( "thumbColor" ) Color thumbColor ) {
		if ( figure != null ) figure.setThumbColor ( thumbColor ) ;
	}
}
