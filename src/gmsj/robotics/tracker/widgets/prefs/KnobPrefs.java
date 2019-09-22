package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.KnobFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class KnobPrefs extends AbstractRoundRampedFigurePrefs {

	private KnobFigure figure ;
		
	
	static public void definePreferencesCatalog ( String widgetName )  {
		
		TrackerPreferences.createBooleanPreferences ( widgetName , "effect3D"			  , true ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "bulbeColor" 		  , GRAY  ) ;
		TrackerPreferences.createFontPreferences 	( widgetName , "font" 		  		  , new Font ( null , "", 0,0 ) ) ;
		TrackerPreferences.createDoublePreferences 	( widgetName , "increment" 			  , 1.0 	) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "thumbColor" 		  , WHITE  ) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "valueLabelVisibility" , true ) ;

		AbstractRoundRampedFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	public void init ( KnobFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	@Inject @Optional
	public void setEffect3D 			( @Named ( "effect3D" ) boolean effect3D ) {
		if ( figure != null ) figure.setEffect3D ( effect3D ) ;
	}
	@Inject @Optional
	public void setBulbColor 			( @Named ( "bulbeColor" ) Color bulbeColor ) {
		if ( figure != null ) figure.setBulbColor ( bulbeColor ) ;
	}
	@Inject @Optional
	public void setFont 				( @Named ( "font" ) FontData font ) {
		if ( figure != null ) figure.setFont ( new Font ( null , font ) ) ;
	}
	@Inject @Optional
	public void setIncrement 			( @Named ( "increment" ) double increment ) {
		if ( figure != null ) figure.setIncrement ( increment ) ;
	}
	@Inject @Optional
	public void setThumbColor 			( @Named ( "thumbColor" ) Color thumbColor ) {
		if ( figure != null ) figure.setThumbColor ( thumbColor ) ;
	}
	@Inject @Optional
	public void setValueLabelVisibility ( @Named ( "valueLabelVisibility" ) boolean valueLabelVisibility ) {
		if ( figure != null ) figure.setValueLabelVisibility ( valueLabelVisibility ) ;
	}
}
