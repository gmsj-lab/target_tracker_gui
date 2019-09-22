package gmsj.robotics.tracker.controler;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.nebula.visualization.xygraph.util.XYGraphMediaFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import gmsj.robotics.tracker.communications.ITargetCommunication;
import gmsj.robotics.tracker.communications.SerialCommunications;
import gmsj.robotics.tracker.communications.TcpCommunications;
import gmsj.robotics.tracker.communications.UdpCommunications;
import gmsj.robotics.tracker.events.GlobalPreferences;
import gmsj.robotics.tracker.model.Model;
import gmsj.robotics.tracker.model.ModelChange;
import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.parts.DisplayViewPart;
import gmsj.robotics.tracker.widgets.WidgetFactory;

// Controler is a e4xmi Addon 
@SuppressWarnings ( "restriction" )
public class Controler  implements ITreeElement {

	private	final static String					name = "Controler" ;
	public final 	Model 						model ;
	public 	  		Display 					display ;
	private static 	Controler 					instance 	= null ;
	private 		ArrayList < ITreeElement >  displayList = new ArrayList < ITreeElement > ()  ;
	private 		IEclipsePreferences 		prefs ;
	public 			boolean 					isDisplayPerspectiveOn ;
	public static 	String 						bundleName	= new String ( "gmsj.robotics.tracker" ) ;
	public static 	String 						bundleClass	= new String ( "bundleclass://" + bundleName ) ;
	private 		TargetBrokers 				brokers ;
	private 		MApplication 				application ;
	@Inject private IEclipseContext				context ;
	final static Color 							WHITE  = XYGraphMediaFactory.getInstance ().getColor ( XYGraphMediaFactory.COLOR_WHITE ) ;

	static public void definePreferencesCatalog ()  {
		TrackerPreferences.createBooleanPrivatePreferences ( name , "isDisplayPerspectiveOn" , false ) ;
	}
	@Inject 
	private void setPreferences ( @Preference ( nodePath = Tracker.ID )  IEclipsePreferences prefs, MApplication application ) {
		this.prefs = prefs ;
		TrackerPreferences.setPreferences ( prefs , application.getContext ()  ) ;
		System.out.println ( "preferences path : " + "runTrackerProduct/.metadata/.plugins/org.eclipse.core.runtime/.settings" + prefs.absolutePath () ) ;
	}
	public ArrayList < Property > getPreferences ( boolean showAll ) {
		return TrackerPreferences.getPreferences ( name , context , showAll) ;
	}
	@Inject
	public Controler ( MApplication application ) {

		instance 	 	 = this ;
		this.model 	 	 = new Model () ;
		this.application = application ;
		
		application.getContext().set ( Controler.class, instance ) ;
		application.getContext().set ( Model.class, model ) ;
		
		application.getContext ().set ( ISelectedPreferenceTarget.class, null ) ;
		application.getContext ().declareModifiable ( ISelectedPreferenceTarget.class ) ;	
	}
	@PostConstruct
	public void postConstruct (  MApplication application ) {
		TrackerPreferences.definePreferencesCatalog () ;
		TrackerPreferences.restorePreferences ( name , context ) ;
		TrackerPreferences.restorePreferences ( GlobalPreferences.name , application.getContext () ) ;
		
		WidgetFactory.setPreferences ( prefs ) ;
		
		brokers	 = ContextInjectionFactory.make ( TargetBrokers.class, context ) ;
	}
	public static Controler getInstance () {
		return instance ;
	}
	@Inject @Optional
	private void restorePerspective ( @Named ( "isDisplayPerspectiveOn" ) boolean isDisplayPerspectiveOn ) {
		this.isDisplayPerspectiveOn = isDisplayPerspectiveOn ;
	}
	@Inject 
	private void getDisplay ( Display display ) {
		this.display = display ;
	}
	public void serialConnectionRequested ( String portName, Integer initRate, Integer desiredRate ) {

		// Let the Injection context factory create the new target so inject will be possible within the target
		Target target 				= ContextInjectionFactory.make ( Target.class, context ) ;   
		SerialCommunications serial = ContextInjectionFactory.make ( SerialCommunications.class, context ) ;

		if ( serial.init ( portName, initRate ) ) {
			String targetName	  = "TARGET " + portName ;

			addTarget ( target , serial , targetName ) ;		
		
			// Change the line rate
			if ( ! desiredRate.equals ( initRate ) ) {
				target.sendBuildChangeRateMsg ( desiredRate.toString() ) ;
				serial.closePort () ;
				serial.init ( portName, desiredRate ) ;
				target.sendDiscoveryMsg () ;
			}
		}	
	}
	public void udpConnectionRequested ( String address, String port ) {

		// Let the Injection context factory create the new target so inject will be possible within the target
		Target target 		  = ContextInjectionFactory.make ( Target.class, context ) ; 
		UdpCommunications udp = new UdpCommunications () ;
		String targetName	  = "UDP TARGET " + address + ":" + port ;
		if ( udp.init ( address, Integer.valueOf ( port ) ) ) {
			addTarget ( target , udp , targetName ) ;		
		}	
	}
	public void tcpConnectionRequested ( String address, String port ) {

		// Let the Injection context factory create the new target so inject will be possible within the target
		Target target 		  = ContextInjectionFactory.make ( Target.class, context ) ; 
		TcpCommunications tcp = new TcpCommunications () ;
		String targetName	  = "TCP TARGET " + address + ":" + port ;
		if ( tcp.init ( address, Integer.valueOf ( port ) ) ) {
			addTarget ( target , tcp , targetName ) ;
		}
	}
	private void addTarget ( Target target , ITargetCommunication comm , String targetName ) {
		int colorFromHost 	= getColorFromHost   ( model.getNumberOfTargets () ) ;
		int colorFromTarget = getColorFromTarget ( model.getNumberOfTargets () ) ;

		target.setName ( targetName ) ;
		target.init ( context , comm , colorFromHost , colorFromTarget ) ;
		model.addTarget ( target ) ;
		brokers.setTarget( target ) ;
		
		application.getContext().set ( ModelChange.class, new ModelChange () ) ;	
	}
	private int getColorFromTarget ( int id ) {
		int color ;
		switch ( id ) {
			case 0 : 	color = SWT.COLOR_RED ; 		break ;
			case 1 : 	color = SWT.COLOR_MAGENTA ; 	break ;
			case 2 : 	color = SWT.COLOR_DARK_RED ; 	break ;
			default :	color = SWT.COLOR_DARK_GREEN ; 	break ;
		}
		return color ;
	}
	private int getColorFromHost (int id ) {
		int color ;
		switch ( id ) {
			case 0 : 	color = SWT.COLOR_GRAY ; 		break ;
			case 1 : 	color = SWT.COLOR_WHITE ; 		break ;
			case 2 : 	color = SWT.COLOR_CYAN ; 		break ;
			default :	color = SWT.COLOR_DARK_BLUE ; 	break ;
		}
		return color ;
	}
	public Integer getNumberOfDisplays ( ) {
		return getChildren ().length ;
	}
	@Override
	public ITreeElement getParent () {
		return null ;
	}
	@Override
	public boolean hasChildren () {
		return ( ! displayList.isEmpty () ) ;
	}
	@Override
	public ITreeElement [] getChildren () {
		return 	getDisplays () ;
	}
	public void addDisplay ( DisplayViewPart display ) {
		displayList.add ( display ) ;
	}
	public void removeDisplay ( DisplayViewPart display ) {
		displayList.remove ( display ) ;
	}
	public ITreeElement [] getDisplays () {
		ITreeElement [] displays = new ITreeElement [0] ;

		if ( displayList.size () > 0 ) {
			displays = displayList.toArray ( (ITreeElement []) new DisplayViewPart [0] ) ; 
		}
		return displays ;
	}
	@Override
	public String getName () {
		return "Global settings" ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {		
	}
	@PreDestroy
	void predestroy () {
		TrackerPreferences.storeValueBoolean ( name , "isDisplayPerspectiveOn" , isDisplayPerspectiveOn ) ;
		for ( Target target : model.getTargets() ) {
			target.close() ;
		}
	}
	public TargetBroker getBroker ( String targetName ) {
		return brokers.get ( targetName ) ;
	}
	public Model getModel() {
		return model;
	}
}
