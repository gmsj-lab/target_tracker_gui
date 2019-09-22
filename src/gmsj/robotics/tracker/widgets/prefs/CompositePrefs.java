package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Control;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class CompositePrefs {

	final static Color WHITE = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_WHITE   ) ;
	final static Color GRAY  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_GRAY   ) ;
	private Control control ;
	
	static public void definePreferencesCatalog ( String widgetName )  {
		
		TrackerPreferences.createFontPreferences 	( widgetName , "font" 		 	 , new Font ( null , "", 0,0 ) ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "backgroundColor" , GRAY  ) ;
		TrackerPreferences.createColorPreferences 	( widgetName , "foregroundColor" , WHITE  ) ;
	}
	public void init ( Control control ) {
		this.control = control ;
	}
	
	@Inject @Optional
	public void setFont ( @Named ( "font" ) FontData font ) {
		if ( ( control != null ) && ( ! control.isDisposed () ) ) 
			control.setFont ( new Font ( null , font )  ) ;
	}
	@Inject @Optional 
	private void setBackgroundColor	( @Named ( "backgroundColor" ) Color backgroundColor ) {
		if ( ( control != null ) && ( ! control.isDisposed () ) ) 
			control.setBackground ( backgroundColor ) ; 
	}
	@Inject @Optional 
	private void setForegroundColor	( @Named ( "foregroundColor" ) Color foregroundColor ) {
		if ( ( control != null ) && ( ! control.isDisposed () ) ) 
			control.setForeground ( foregroundColor ) ; 
	}
}
