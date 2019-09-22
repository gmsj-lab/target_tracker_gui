package gmsj.robotics.tracker.widgets.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;

public class ColorWidget extends TrackerWidget {

	public  static  String 		name = "Color" ;	
	private 		Composite 	widgetComposite ;
	
	static public void definePreferencesCatalog () {
		//TODO prefs
	}
	
	public ColorWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void createWidget ( Composite parent ) {
		
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		widgetComposite 		= new Composite ( parent, SWT.NONE ) ;
		GridLayout gridLayout 	= new GridLayout () ;
		gridLayout.numColumns 	= 3 ;
		widgetComposite.setLayout ( gridLayout ) ;
		
		widgetControl = widgetComposite ;
		
		//textWidget = new TextWidget ( this, widgetComposite, targetAttribute ) ;
		
		restoreAttributes () ;
		}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//TODO
	}
}
