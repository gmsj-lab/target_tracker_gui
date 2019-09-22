package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.widgets.figures.AbstractRoundRampedFigure;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class AbstractRoundRampedFigurePrefs extends AbstractMarkedWidgetFigurePrefs {

	private AbstractRoundRampedFigure figure ;
	
	public void init ( AbstractRoundRampedFigure figure ) {
		super.init ( figure ) ;
		this.figure = figure ;
	}
	public static void definePreferencesCatalog ( String widgetName ) {

		TrackerPreferences.createBooleanPreferences ( widgetName , "gradient" , true ) ;

		AbstractMarkedWidgetFigurePrefs.definePreferencesCatalog ( widgetName ) ;
	}
	@Inject @Optional
	public void setGradient	( @Named ( "gradient" ) boolean gradient ) {
		if ( figure != null ) figure.setGradient ( gradient ) ;
	}
}
