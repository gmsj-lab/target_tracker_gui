package gmsj.robotics.tracker.widgets;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.service.prefs.Preferences;

import gmsj.robotics.tracker.widgets.nebula.GaugeWidget;
import gmsj.robotics.tracker.widgets.nebula.GraphWidget;
import gmsj.robotics.tracker.widgets.nebula.ScopeWidget;
import gmsj.robotics.tracker.widgets.nebula.MeterWidget;
import gmsj.robotics.tracker.widgets.nebula.TankWidget;
import gmsj.robotics.tracker.widgets.nebula.SliderWidget;
import gmsj.robotics.tracker.widgets.nebula.KnobWidget;
import gmsj.robotics.tracker.widgets.swt.NumericWidget;
import gmsj.robotics.tracker.widgets.swt.ColorWidget;
import gmsj.robotics.tracker.widgets.swt.ScaleWidget;
import gmsj.robotics.tracker.widgets.swt.OnOffWidget;
import gmsj.robotics.tracker.widgets.swt.OnOffToggleWidget;
 
public class WidgetFactory {
	
	private static Preferences 	prefs ;
	final   static String		NODE_WIDGETS	= "Widgets" ;

	public static void setPreferences ( Preferences preferences ) {
		prefs = preferences ;
	}
	public static String createNewWidgetId ( String widgetName ) {
		
		long nextWidgetId = prefs.node ( NODE_WIDGETS ).getLong ( "NextWidgetId" , 0 ) ;
		
		String widgetId = NODE_WIDGETS + "_" + String.valueOf ( nextWidgetId ++ )  ;
		
		prefs.node ( NODE_WIDGETS ).put 	 ( widgetId 	  , widgetName   ) ;
		prefs.node ( NODE_WIDGETS ).putLong ( "NextWidgetId" , nextWidgetId ) ;
				
		return widgetId ;
	}
	public static IWidget create ( String widgetName , IEclipseContext context ) {
		
		return create ( widgetName , widgetName, context ) ;
	}

	public static IWidget get ( String widgetId , IEclipseContext context ) {
		
		String widgetName = prefs.node ( NODE_WIDGETS ).get ( widgetId , null ) ;
		return create ( widgetName , widgetId, context ) ;
	}
	private static IWidget create ( String widgetName , String widgetId, IEclipseContext context ) {
		IWidget widget ;

		if ( widgetName.equals ( GraphWidget.name ) ) {
			widget = ContextInjectionFactory.make ( GraphWidget.class , context ) ; 
		} 
		else 
		if ( widgetName.equals ( ScopeWidget.name ) ) {
			widget = ContextInjectionFactory.make ( ScopeWidget.class , context ) ;
		} 
		else 
		if ( widgetName.equals ( MeterWidget.name ) ) {
			widget = ContextInjectionFactory.make ( MeterWidget.class , context ) ; 
		} 
		else 
		if ( widgetName.equals ( GaugeWidget.name ) ) {
			widget = ContextInjectionFactory.make ( GaugeWidget.class , context ) ; 
		} 
		else 
		if ( widgetName.equals ( TankWidget.name ) ) {
			widget = ContextInjectionFactory.make ( TankWidget.class , context ) ;
		} 			
		else 
		if ( widgetName.equals ( NumericWidget.name ) ) {
			widget = ContextInjectionFactory.make ( NumericWidget.class , context ) ; 
		} 
		else 
		if ( widgetName.equals ( ColorWidget.name ) ) {
			widget = ContextInjectionFactory.make ( ColorWidget.class , context ) ;
		} 
		else 
		if ( widgetName.equals ( SliderWidget.name ) ) {
			widget = ContextInjectionFactory.make ( SliderWidget.class , context ) ; 
		} 
		else 
		if ( widgetName.equals ( ScaleWidget.name ) ) {
			widget = ContextInjectionFactory.make ( ScaleWidget.class , context ) ;
		} 		
		else 
		if ( widgetName.equals ( KnobWidget.name ) ) {
			widget = ContextInjectionFactory.make ( KnobWidget.class , context ) ; 
		} 		
		else 
		if ( widgetName.equals ( OnOffWidget.name ) ) {
			widget = ContextInjectionFactory.make ( OnOffWidget.class , context ) ;
		}
		else 
		if ( widgetName.equals ( OnOffToggleWidget.name ) ) {
			widget = ContextInjectionFactory.make ( OnOffToggleWidget.class , context ) ;
		}
		else 
		{
			return null ; 
		}
		
		widget.setId ( widgetId ) ;
		return widget ;
	}
}
