package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;

import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.swt.OnOffWidget;

public class OnOffPrefs extends CompositePrefs {

	private OnOffWidget onOffWidget ;
	private Boolean 	horizontalAlign = true ;
	private Boolean 	verticalAlign 	= true ;
	private String 		buttonText 		= "" ;
	private int 		buttonStyle		= SWT.TOGGLE ;

	static public void definePreferencesCatalog ( String widgetName )  {
		
		TrackerPreferences.createBooleanPreferences ( widgetName , "horizontalAlign" 	, true 			) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "verticalAlign" 		, true 			) ;
		TrackerPreferences.createStringPreferences 	( widgetName , "buttonText" 		, "buttonText"  ) ;
		TrackerPreferences.createIntPreferences 	( widgetName , "buttonStyle" 		, SWT.TOGGLE  	) ;

		CompositePrefs.definePreferencesCatalog 	( widgetName ) ;
	}

	public void init ( OnOffWidget onOffWidget ) {
		super.init ( onOffWidget.getDnDControl () ) ;
		this.onOffWidget = onOffWidget ;
	}
	public Boolean getHorizontalAlign () {
		return horizontalAlign ;
	}
	public Boolean getVerticalAlign () {
		return verticalAlign ;
	}	
	public String getButtonText () {
		return buttonText;
	}
	public int getStyle () {
		return buttonStyle ;
	}
	@Inject @Optional 
	private void setHorizontalAlign ( @Named ( "horizontalAlign" ) Boolean horizontalAlign ) {
		this.horizontalAlign = horizontalAlign ;
		if ( onOffWidget != null ) onOffWidget.createButton () ; 
	}
	@Inject @Optional 
	private void setVerticalAlign ( @Named ( "verticalAlign" ) Boolean verticalAlign ) {
		this.verticalAlign = verticalAlign ;
		if ( onOffWidget != null ) onOffWidget.createButton () ; 
	}
	@Inject @Optional 
	private void setText ( @Named ( "buttonText" ) String buttonText ) {
		this.buttonText = buttonText ;
		if ( onOffWidget != null ) onOffWidget.createButton () ; 
	}
	@Inject @Optional 
	private void setStyle ( @Named ( "buttonStyle" ) int buttonStyle ) {
		this.buttonStyle = buttonStyle ;
		if ( onOffWidget != null ) onOffWidget.createButton () ; 
	}	
}
