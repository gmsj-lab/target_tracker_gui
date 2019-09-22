package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class ReadWriteWidgets extends WidgetCategory {
	
	private static  String 			name 		 = "Display & Command Widgets" ;
	private 		ITreeElement 	numericWidget ;
	private 		ITreeElement 	colorWidget ;	
	private 		ITreeElement 	sliderWidget ;
	private 		ITreeElement[] 	widgetTypes ;

	@PostConstruct	
	public void postConstruct () {	

		numericWidget = WidgetFactory.create ( "Numeric" , context ) ; numericWidget.setParent ( this ) ;
		colorWidget   = WidgetFactory.create ( "Color"   , context ) ; colorWidget.setParent   ( this ) ;
		sliderWidget  = WidgetFactory.create ( "Slider"  , context ) ; sliderWidget.setParent  ( this ) ;
		
		widgetTypes   = new ITreeElement [] { numericWidget , colorWidget , sliderWidget } ;
	}
	@Override
	public ITreeElement[] getChildren () {
		return widgetTypes ;
	}
	@Override
	public String getName () {
		return name ;
	}
}