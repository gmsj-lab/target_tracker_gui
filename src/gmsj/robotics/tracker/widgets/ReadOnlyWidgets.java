package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class ReadOnlyWidgets extends WidgetCategory {

	private static  String 			name 		 = "Display Widgets" ;
	private 		ITreeElement 	graphWidget ;
	private 		ITreeElement 	scopeWidget ;
	private 		ITreeElement 	meterWidget ;
	private 		ITreeElement 	gaugeWidget ;
	private 		ITreeElement 	tankWidget  ;
	private 		ITreeElement[] 	widgetTypes ;

	@PostConstruct	
	public void postConstruct () {
		
		graphWidget = WidgetFactory.create ( "Graph" 		, context ) ; graphWidget.setParent ( this ) ;
		scopeWidget = WidgetFactory.create ( "Oscilloscope" , context ) ; scopeWidget.setParent ( this ) ;
		meterWidget	= WidgetFactory.create ( "Meter" 		, context ) ; meterWidget.setParent ( this ) ;
		gaugeWidget	= WidgetFactory.create ( "Gauge" 		, context ) ; gaugeWidget.setParent ( this ) ; 
		tankWidget 	= WidgetFactory.create ( "Tank" 		, context ) ; tankWidget.setParent  ( this ) ; 
		
		widgetTypes = new ITreeElement [] { graphWidget, scopeWidget , meterWidget , gaugeWidget , tankWidget } ;
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