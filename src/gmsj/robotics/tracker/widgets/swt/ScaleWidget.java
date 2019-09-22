package gmsj.robotics.tracker.widgets.swt;

import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.widgets.TrackerWidget;

public class ScaleWidget extends TrackerWidget {

	public  static  String name = "Scale" ;
	
	static public void definePreferencesCatalog () {
		//TODO prefs
	}
	
	public ScaleWidget () {
		isWriteOnly  = true ;
		setUnsignedTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public void createWidget ( Composite parent ) {	
		
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//TODO
	}
}
