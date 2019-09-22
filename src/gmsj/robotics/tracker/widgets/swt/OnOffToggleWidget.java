package gmsj.robotics.tracker.widgets.swt;

import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;

public class OnOffToggleWidget extends TrackerWidget {

	public  static  String name = "On/Off Toggle Buttons" ;
	
	static public void definePreferencesCatalog () {
		//TODO prefs
	}
	
	public OnOffToggleWidget () {
		isWriteOnly  = true ;
		setBooleanTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public void createWidget ( Composite parent ) {	
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		restoreAttributes () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//TODO
	}
}