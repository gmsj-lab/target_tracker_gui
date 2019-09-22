package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class BooleanWidgets extends WidgetCategory {

	private static  String 			name 		 = "Boolean Widgets" ;
	
	private 		ITreeElement 	onOffWidget ;
	private 		ITreeElement 	onOffToggleWidget ;
	private 		ITreeElement[] 	widgetTypes ;

	@PostConstruct	
	public void postConstruct () {

		onOffWidget 	  = WidgetFactory.create ( "On/Off Button" 		   , context ) ; onOffWidget.setParent 		 ( this ) ;	
		onOffToggleWidget = WidgetFactory.create ( "On/Off Toggle Buttons" , context ) ; onOffToggleWidget.setParent ( this ) ;
		
		widgetTypes 	  = new ITreeElement [] { onOffWidget , onOffToggleWidget } ;
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
