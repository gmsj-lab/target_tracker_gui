package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.nebula.visualization.widgets.figures.GaugeFigure;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.prefs.GaugePrefs;

public class GaugeWidget extends TrackerWidget {

	public  static  String 			name 		 = "Gauge" ;
	private 		GaugeFigure		figure ;
	private 		GaugePrefs		gaugePrefs ;

	static public void definePreferencesCatalog () {
		GaugePrefs.definePreferencesCatalog ( name ) ;
	}
	public GaugeWidget () {		
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void createWidget ( Composite parent ) {
		
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		figure 		= new GaugeFigure  () ;
		gaugePrefs	= ContextInjectionFactory.make ( GaugePrefs.class , context ) ; 
		gaugePrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;
		
		restoreAttributes () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		figure.setValue ( event.getValue() ) ;
	}
}
