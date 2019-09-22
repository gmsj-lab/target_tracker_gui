package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.nebula.visualization.widgets.figures.TankFigure;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.prefs.TankPrefs;

public class TankWidget extends TrackerWidget {

	public  static  String 				name 		 = "Tank" ;	
	private 		TankFigure			figure ;
	private 		TankPrefs			tankPrefs ;
	
	static public void definePreferencesCatalog () {
		TankPrefs.definePreferencesCatalog ( name ) ;
	}
	public TankWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}	
	public void createWidget ( Composite parent ) {

		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;
		
		figure 	  = new TankFigure  () ;
		tankPrefs = ContextInjectionFactory.make ( TankPrefs.class, context ) ; 
		tankPrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;
		
		restoreAttributes () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		figure.setValue ( event.getValue() ) ;
	}
}
