package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.nebula.visualization.widgets.datadefinition.IManualValueChangeListener;
import org.eclipse.nebula.visualization.widgets.figures.ScaledSliderFigure;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.prefs.SliderPrefs;

public class SliderWidget extends TrackerWidget {

	public  static  String 						name 		 = "Slider" ;	
	private 		ScaledSliderFigure			figure ;
	private 		SliderPrefs					sliderPrefs ;
	private 		IManualValueChangeListener 	manualValueChangeListener;
	
	static public void definePreferencesCatalog () {
		SliderPrefs.definePreferencesCatalog ( name ) ;
	}
	public SliderWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}	
	public void createWidget ( Composite parent ) {

		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		figure 		= new ScaledSliderFigure  () ;
		sliderPrefs = ContextInjectionFactory.make ( SliderPrefs.class, context ) ; 
		sliderPrefs.init ( figure ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	

		createCanvas ( parent , figure ) ;
		
		restoreAttributes () ;
		
		figure.addManualValueChangeListener ( manualValueChangeListener = new IManualValueChangeListener () {			
			@Override
			public void manualValueChanged ( double newValue ) {
				if ( attribute != null ) {
					attribute.setValue ( String.valueOf ( (int) newValue ) ) ;
				}
			}
		});
	}
	protected boolean addAttribute ( WidgetAttribute attribute ) {
		this.attribute = attribute ;

		return super.addAttribute ( attribute ) ;

	}
	public void removeAttribute ( WidgetAttribute attribute ) {
		super.removeAttribute ( attribute ) ;
		figure.removeManualValueChangeListener ( manualValueChangeListener ) ;
		attribute = null ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		figure.setValue ( event.getValue() ) ;
	}
}
