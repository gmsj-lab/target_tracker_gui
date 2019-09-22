package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.nebula.visualization.widgets.datadefinition.IManualValueChangeListener;
import org.eclipse.nebula.visualization.widgets.figures.KnobFigure;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.Profiler;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.prefs.KnobPrefs;

public class KnobWidget extends TrackerWidget {

	public  static  String 		name 		 = "Knob" ;
	
	private 		KnobFigure	figure ;
	private 		KnobPrefs	knobPrefs ;
	private 		IManualValueChangeListener 	manualValueChangeListener ;
	
	public KnobWidget () {
		isWriteOnly = true ;
		setAllAttributeTypesAllowed () ;
	}
	static public void definePreferencesCatalog () {
		KnobPrefs.definePreferencesCatalog ( name ) ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void createWidget ( Composite parent ) {

		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;
		
		figure 	  = new KnobFigure  () ;
		knobPrefs = ContextInjectionFactory.make ( KnobPrefs.class, context ) ; 
		knobPrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;
		
		restoreAttributes () ;
		
		figure.addManualValueChangeListener ( manualValueChangeListener = new IManualValueChangeListener () {			
			@Override
			public void manualValueChanged ( double newValue ) {
				if ( newValue == 1023 ) { 							// TODO remove after tests
					Profiler.add ("knobValue: <"+ newValue + ">" ); // TODO remove after tests
				}													// TODO remove after tests

				if ( attribute != null ) {
					attribute.setValue ( String.valueOf ( (int) newValue ) ) ;
				}
			}
		});
	}
	public void removeAttribute ( WidgetAttribute attribute ) {
		super.removeAttribute ( attribute ) ;
		figure.removeManualValueChangeListener ( manualValueChangeListener ) ;
		attribute = null ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//figure.setValue ( Double.parseDouble ( attributeValue ) ) ;
	}
}