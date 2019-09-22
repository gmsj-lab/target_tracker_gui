package gmsj.robotics.tracker.widgets.prefs;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.SWT;

import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.swt.NumericWidget;

public class NumericPrefs extends CompositePrefs {

	private NumericWidget numericWidget ;
	private Boolean 	horizontalAlign = true ;
	private Boolean 	verticalAlign 	= true ;
	private String 		numericText 	= "" ;
	private int 		numericStyle	= SWT.TOGGLE ;
	private String 		labelText 		= "" ;

	static public void definePreferencesCatalog ( String widgetName )  {
		
		TrackerPreferences.createBooleanPreferences ( widgetName , "horizontalAlign" 	, true 			) ;
		TrackerPreferences.createBooleanPreferences ( widgetName , "verticalAlign" 		, true 			) ;
		TrackerPreferences.createStringPreferences 	( widgetName , "numericText" 		, "numericText" ) ;
		TrackerPreferences.createIntPreferences 	( widgetName , "numericStyle" 		, SWT.TOGGLE  	) ;

		CompositePrefs.definePreferencesCatalog 	( widgetName ) ;
	}

	public void init ( NumericWidget numericWidget ) {
		super.init ( numericWidget.getDnDControl () ) ;
		this.numericWidget = numericWidget ;
	}
	public Boolean getHorizontalAlign () {
		return horizontalAlign ;
	}
	public Boolean getVerticalAlign () {
		return verticalAlign ;
	}	
	public String getNumericText () {
		return numericText;
	}
	public int getStyle () {
		return numericStyle ;
	}
	@Inject @Optional 
	private void setHorizontalAlign ( @Named ( "horizontalAlign" ) Boolean horizontalAlign ) {
		this.horizontalAlign = horizontalAlign ;
		if ( numericWidget != null ) numericWidget.createText () ; 
	}
	@Inject @Optional 
	private void setVerticalAlign ( @Named ( "verticalAlign" ) Boolean verticalAlign ) {
		this.verticalAlign = verticalAlign ;
		if ( numericWidget != null ) numericWidget.createText () ; 
	}
	@Inject @Optional 
	private void setText ( @Named ( "numericText" ) String numericText ) {
		this.numericText = numericText ;
		if ( numericWidget != null ) numericWidget.createText () ; 
	}
	@Inject @Optional 
	private void setStyle ( @Named ( "numericStyle" ) int numericStyle ) {
		this.numericStyle = numericStyle ;
		if ( numericWidget != null ) numericWidget.createText () ; 
	}

	public String getLabel() {
		return labelText;
	}	
}
