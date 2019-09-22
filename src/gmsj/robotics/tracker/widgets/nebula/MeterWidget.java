package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.nebula.visualization.widgets.figures.MeterFigure;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.prefs.MeterPrefs;

public class MeterWidget extends TrackerWidget {

	public  static  String 		name 		 = "Meter" ;	
	private 		MeterFigure	figure ;
	private 		MeterPrefs	meterPrefs ;
	
	static public void definePreferencesCatalog () {
		MeterPrefs.definePreferencesCatalog ( name ) ;
	}
	public MeterWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}	
	public void createWidget ( Composite parent ) {

		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;
		
		figure 	   = new MeterFigure  () ;
		meterPrefs = ContextInjectionFactory.make ( MeterPrefs.class, context ) ; 
		meterPrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;
		
		restoreAttributes () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		figure.setValue ( event.getValue() ) ;
	}
}