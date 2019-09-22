package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class FigurePrefs {

	final static Color WHITE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_WHITE ) ;
	final static Color GRAY  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_GRAY   ) ;
	final static Font FONT  = XYGraphMediaFactory.getInstance ().getFont( XYGraphMediaFactory.FONT_TAHOMA ) ;

	private Figure	figure ;
	
	public void init ( Figure figure ) {
		this.figure = figure ;
	}
	public static void definePreferencesCatalog ( String widgetName ) {

		TrackerPreferences.createColorPreferences 	( widgetName , "backgroundColor" , GRAY 	) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "foregroundColor" , WHITE 	) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "opaque" 		 , true 	) ;
		TrackerPreferences.createFontPreferences 	( widgetName , "font" 		 	 , FONT ) ;
	}
	@Inject @Optional
	public void setBackgroundColor ( @Named ( "backgroundColor" ) Color backgroundColor ) {
		if ( figure != null ) figure.setBackgroundColor ( backgroundColor ) ;
	}
	@Inject @Optional
	public void setForegroundColor ( @Named ( "foregroundColor" ) Color foregroundColor ) {
		if ( figure != null ) figure.setForegroundColor ( foregroundColor ) ;
	}
	@Inject @Optional
	public void setOpaque ( @Named ( "opaque" ) boolean opaque ) {
		if ( figure != null ) figure.setOpaque ( opaque ) ;
	}
	@Inject @Optional
	public void setFont ( @Named ( "font" ) FontData font ) {
		if ( figure != null ) figure.setFont ( new Font ( null , font )  ) ;
	}
	@Inject @Optional
	public void setBorder ( @Named ( "border" ) Border border ) {
		// TODO
		if ( figure != null ) figure.setBorder ( border  ) ;
	}
}
