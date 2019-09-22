package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.MeterFigure;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class MeterPrefs  extends AbstractRoundRampedFigurePrefs {

	private MeterFigure	figure ;
		
	static public void definePreferencesCatalog ( String widgetName )  {

		TrackerPreferences.createColorPreferences 				( widgetName , "needleColor" , RED 			 ) ;
		TrackerPreferences.createBooleanPreferences 			( widgetName , "valueLabelVisibility" , true ) ;
		
		AbstractRoundRampedFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	public void init ( MeterFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	@Inject @Optional
	public void setNeedleColor 			( @Named ( "needleColor" ) Color needleColor ) {
		if ( figure != null ) figure.setNeedleColor ( needleColor ) ;
	}
	@Inject @Optional
	public void setValueLabelVisibility ( @Named ( "valueLabelVisibility" ) boolean valueLabelVisibility ) {
		if ( figure != null ) figure.setValueLabelVisibility ( valueLabelVisibility ) ;
	}
}
