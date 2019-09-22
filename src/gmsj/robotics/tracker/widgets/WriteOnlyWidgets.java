package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class WriteOnlyWidgets extends WidgetCategory {

	private static  String 	name 		 = "Command Only Widgets" ;
	private ITreeElement 	knobWidget ;
	private ITreeElement[] 	widgetTypes ;

	@PostConstruct	
	public void postConstruct () {

		knobWidget  = WidgetFactory.create ( "Knob" , context ) ; knobWidget.setParent ( this ) ;
		widgetTypes = new ITreeElement [] { knobWidget } ;
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