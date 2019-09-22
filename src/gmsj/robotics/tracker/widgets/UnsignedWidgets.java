package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class UnsignedWidgets extends WidgetCategory {
	
	private static  String 	name 		 = "Unsigned Only Widgets" ;
	private ITreeElement 	scaleWidget ;
	private ITreeElement[] 	widgetTypes ;

	@PostConstruct	
	public void postConstruct () {
		scaleWidget = WidgetFactory.create ( "Scale" , context ) ; scaleWidget.setParent ( this ) ;
		widgetTypes  = new ITreeElement [] { scaleWidget } ;
	}
	@Override
	public ITreeElement[] getChildren() {
		return widgetTypes ;
	}
	@Override
	public String getName () {
		return name;
	}
}