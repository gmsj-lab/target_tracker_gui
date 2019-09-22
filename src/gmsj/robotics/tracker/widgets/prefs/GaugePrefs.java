package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.GaugeFigure;
import org.eclipse.swt.graphics.Color;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class GaugePrefs extends AbstractRoundRampedFigurePrefs {

	private GaugeFigure	figure ;
	
	static public void definePreferencesCatalog ( String widgetName )  {
		
		TrackerPreferences.createBooleanPreferences ( widgetName , "effect3D"    , true ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "needleColor" , RED  ) ;

		AbstractRoundRampedFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	public void init ( GaugeFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	@Inject @Optional
	private void setEffect3D ( @Named ( "effect3D" ) boolean effect3D ) {
		if ( figure != null ) figure.setEffect3D ( effect3D ) ;
	}
	@Inject @Optional 
	private void setNeedleColor	( @Named ( "needleColor" ) Color needleColor ) {
		if ( figure != null ) figure.setNeedleColor ( needleColor ) ;
	}
}
