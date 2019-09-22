package gmsj.robotics.tracker.controler;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import gmsj.robotics.tracker.communications.OpenSerialConnectionPopup;
import gmsj.robotics.tracker.communications.OpenBluetoothConnectionPopup;
import gmsj.robotics.tracker.communications.OpenUdpConnectionPopup;
import gmsj.robotics.tracker.events.GlobalPreferences;
import gmsj.robotics.tracker.model.OpenDownloadFilePopup;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.parts.DisplayViewPart;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.nebula.GaugeWidget;
import gmsj.robotics.tracker.widgets.nebula.GraphAttribute;
import gmsj.robotics.tracker.widgets.nebula.GraphWidget;
import gmsj.robotics.tracker.widgets.nebula.KnobWidget;
import gmsj.robotics.tracker.widgets.nebula.MeterWidget;
import gmsj.robotics.tracker.widgets.nebula.SliderWidget;
import gmsj.robotics.tracker.widgets.nebula.TankWidget;
import gmsj.robotics.tracker.widgets.swt.OnOffWidget;

public class TrackerPreferences  {

	final    static String				CATALOG 			= "Catalog" ;
	final    static String				PRIVATE_CATALOG 	= "PrivateCatalog" ;
	final    static	String				VALUES  			= "Values" ;
	final    static	String				COLORS  			= "Colors" ;
	final    static	String				PREFS  				= "PREFERENCES" ;
	final    static	String				ATTRIBUTES  		= "WidgetAttribute" ;
	final    static	String				PART_WIDGET  		= "PartWidgets" ;

	private static 	boolean				coldStart			= true ;
	public  static 	IEclipsePreferences eclipsePrefs ;
	public  static 	Preferences 		prefs ;

	public static void setPreferences ( IEclipsePreferences prefs , IEclipseContext context ) {
		TrackerPreferences.eclipsePrefs = prefs ;
		try {
			if ( prefs.nodeExists ( PREFS ) ) {
				coldStart = false ;
			}
			TrackerPreferences.prefs = prefs.node ( PREFS ) ;
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
	}
	public static void createBooleanPrivatePreferences ( String node , String prefName , boolean defaultValue ) {
		storePrivateCatalog ( node , prefName , "boolean" ) ;
		if ( coldStart ) {
			storeValueBoolean ( node , prefName , defaultValue ) ; 
		}
		flushPref () ;
	}
	public static void createBooleanPreferences ( String node , String prefName , boolean defaultValue ) {
		storeCatalog ( node , prefName , "boolean" ) ; 
		if ( coldStart ) {
			storeValueBoolean ( node , prefName , defaultValue ) ; 
			storeValueBoolean ( GlobalPreferences.name , prefName , defaultValue ) ; 
		}
		flushPref () ;
	}
	public static void createStringPrivatePreferences ( String node , String prefName , String defaultValue ) {
		storePrivateCatalog ( node , prefName , "String" ) ; 
		if ( coldStart ) {
			storeValueString ( node , prefName , defaultValue ) ; 
		}
		flushPref () ;
	}
	public static void createStringPreferences ( String node , String prefName , String defaultValue ) {
		storeCatalog ( node , prefName , "String" ) ; 
		if ( coldStart ) {
			storeValueString ( node , prefName , defaultValue ) ; 
			storeValueString ( GlobalPreferences.name , prefName , defaultValue ) ; 
		}
		flushPref () ;
	}	
	public static void createIntPrivatePreferences ( String node , String prefName , int defaultValue ) {
		storePrivateCatalog ( node , prefName , "int" ) ; 
		if ( coldStart ) {
			storeValueInt ( node , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}
	public static void createIntPreferences ( String node , String prefName , int defaultValue ) {
		storeCatalog ( node , prefName , "int" ) ; 
		if ( coldStart ) {
			storeValueInt ( node , prefName , defaultValue ) ; 		
			storeValueInt ( GlobalPreferences.name , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}	
	public static void createDoublePrivatePreferences ( String node , String prefName , Double defaultValue ) {
		storePrivateCatalog ( node , prefName , "double" ) ; 
		if ( coldStart ) {
			storeValueDouble ( node , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}
	public static void createDoublePreferences ( String node , String prefName , Double defaultValue ) {
		storeCatalog ( node , prefName , "double" ) ;
		if ( coldStart ) {
			storeValueDouble ( node , prefName , defaultValue ) ; 		
			storeValueDouble ( GlobalPreferences.name , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}	
	public static void createColorPrivatePreferences ( String node , String prefName , Color defaultValue ) {
		storePrivateCatalog ( node , prefName , "Color" ) ; 
		if ( coldStart ) {
			storeValueColor ( node , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}
	public static void createColorPreferences ( String node , String prefName , Color defaultValue ) {
		storeCatalog ( node , prefName , "Color" ) ; 
		if ( coldStart ) {
			storeValueColor ( node , prefName , defaultValue ) ; 		
			storeValueColor ( GlobalPreferences.name , prefName , defaultValue ) ; 		
		}
		flushPref () ;
	}	
	public static void createFontPrivatePreferences ( String node , String prefName , FontData defaultValue ) {
		storeCatalog ( node , prefName , "Font" ) ; 
		storeValueFont ( node , prefName , defaultValue ) ; 		
		storeValueFont ( GlobalPreferences.name , prefName , defaultValue ) ; 	
		}
	public static void createFontPreferences ( String node , String prefName , Font defaultValue ) {
		storeCatalog ( node , prefName , "Font" ) ; 
		storeValueFont ( node , prefName , defaultValue.getFontData ()[0] ) ; 		
		storeValueFont ( GlobalPreferences.name , prefName , defaultValue.getFontData ()[0] ) ; 	
		}
// NEW
	public static void createAttributePrivatePreferences ( String node , String prefName , WidgetAttribute defaultValue ) {
		storeCatalog ( node , prefName , "WidgetAttribute" ) ; 
		storeValueAttribute ( node , prefName , defaultValue ) ; 		
		storeValueAttribute ( GlobalPreferences.name , prefName , defaultValue ) ; 	
		}
	public static void createAttributePreferences ( String node , String prefName , WidgetAttribute defaultValue ) {
		storeCatalog ( node , prefName , "WidgetAttribute" ) ; 
		storeValueAttribute ( node , prefName , defaultValue ) ; 		
		storeValueAttribute ( GlobalPreferences.name , prefName , defaultValue ) ; 	
		}
	private static void storePrivateCatalog ( String catalogName , String key , String value ) {
		try {
			if ( ! prefs.node ( catalogName ).node ( PRIVATE_CATALOG ).nodeExists( catalogName ) ) { 
				   prefs.node ( catalogName ).node ( PRIVATE_CATALOG ).put ( key , value ) ;
				   //System.out.println("storePrivateCatalog::key:"+key+"  , value:" + value );
			}
		} catch (BackingStoreException e) {
			e.printStackTrace () ;
		}
	}
	private static void storeCatalog ( String catalogName , String key , String value ) {
		try {
			if ( ! prefs.node ( catalogName ).node ( CATALOG ).nodeExists( catalogName ) ) { 
				   prefs.node ( catalogName ).node ( CATALOG ).put ( key , value ) ;
			}
			if ( ! prefs.node ( GlobalPreferences.name ).node ( CATALOG ).nodeExists( catalogName ) ) { 
				   prefs.node ( GlobalPreferences.name ).node ( CATALOG ).put ( key , value ) ;
			}
		} catch (BackingStoreException e) {
			e.printStackTrace () ;
		}
	}
	public static void defineCatalog ( String node , String catalogName ) {
		defineCatalog ( prefs.node ( node ) , catalogName ) ;
	}
	public static void defineCatalog ( String parentId , String node , String catalogName ) {
		defineCatalog ( prefs.node ( parentId ).node ( node ) , catalogName ) ;
	}
	private static void defineCatalog ( Preferences prefNode , String catalogName ) {
		prefNode.put ( "linkToCatalog" , catalogName ) ;
		
		try {
			if ( ! prefNode.nodeExists ( VALUES ) ) {
				// initialize values with blueprint
				Preferences blueprintValues = prefs.node ( catalogName ).node ( VALUES ) ;
				Preferences blueprintColors = blueprintValues.node ( COLORS ) ;
				
				for ( String prefName : blueprintValues.keys () ) {
					prefNode.node ( VALUES ).put ( prefName , blueprintValues.get ( prefName , null ) ) ;
				}
				for ( String prefName : blueprintColors.keys () ) {
					prefNode.node ( VALUES ).node ( COLORS ).put ( prefName , blueprintColors.get ( prefName , null ) ) ;
				}
			flushPref () ;
			}
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
	}
	public static void storeValue ( Property property ) {

		Preferences prefNode = ( property.getParentNode () == null ) ? 
				prefs.node ( property.getNode () ) : 
				prefs.node ( property.getParentNode () ).node ( property.getNode () ) ;
				
		if ( property.getType () == null ) {
			System.out.println ( "TrackerPreferences::storeValue: Uknown type of preference in catalog for :" + property.getName () ) ;
		}
		else if ( property.getType ().equals ( "String" ) ) {
			prefNode.node ( VALUES ).put ( property.getName () , property.getStringValue () ) ;
			flushPref () ;
		}
		else if ( property.getType ().equals ( "boolean" ) ) {
			prefNode.node ( VALUES ).putBoolean ( property.getName () , property.getBooleanValue () ) ;
			flushPref () ;
		}
		else if ( property.getType ().equals ( "int" ) ) {
			prefNode.node ( VALUES ).putInt ( property.getName () , property.getIntValue () ) ;
			flushPref () ;
		}
		else if ( property.getType ().equals ( "double" ) ) {
			prefNode.node ( VALUES ).putDouble ( property.getName () , property.getDoubleValue () ) ;
			flushPref () ;
		}
		else if ( property.getType ().equals ( "Color" ) ) {
			int red   = property.getColorValue ().getRed   () ;
			int green = property.getColorValue ().getGreen () ;
			int blue  = property.getColorValue ().getBlue  () ;

			prefNode.node ( VALUES ).put ( property.getName () , "COLOR"+ property.getName ()   ) ;
			prefNode.node ( VALUES ).node ( COLORS ).putInt ( property.getName () + "Red"   , red   ) ;
			prefNode.node ( VALUES ).node ( COLORS ).putInt ( property.getName () + "Green" , green ) ;
			prefNode.node ( VALUES ).node ( COLORS ).putInt ( property.getName () + "Blue"  , blue  ) ;
			flushPref () ;
		}
		else if ( property.getType ().equals ( "Font" ) ) {
			storeValueFont ( property.getNode ()  , property.getName () , property.getFontValue () ) ;
			prefNode.node ( VALUES ).put ( property.getName () , property.getFontValue ().toString () ) ;
			flushPref () ;
		}
// NEW
		else if ( property.getType ().equals ( "WidgetAttribute" ) ) {
			prefNode.node ( VALUES ).put ( property.getName () , property.getStringValue () ) ;
			flushPref () ;
		}
		else {
			System.out.println ( "TrackerPreferences::storeValue: Uknown type of preference in catalog for :" + property.getName () ) ;
		}
	}
	public static void storeValueString ( String node , String key , String value ) {
		prefs.node ( node ).node ( VALUES ).put ( key , value ) ;
		flushPref () ;
	}
	public static void storeValueBoolean ( String node , String key , boolean value ) {
		prefs.node ( node ).node ( VALUES ).putBoolean ( key , value ) ;
		flushPref () ;
	}
	public static void storeValueInt ( String node , String key , int value ) {
		prefs.node ( node ).node ( VALUES ).putInt ( key , value ) ;
		flushPref () ;
	}
	public static void storeValueDouble ( String node , String key , double value ) {
		prefs.node ( node ).node ( VALUES ).putDouble ( key , value ) ;
		flushPref () ;
	}
	public static void storeValueColor ( String node , String key , Color value ) {
		int red   = value.getRed   () ;
		int green = value.getGreen () ;
		int blue  = value.getBlue  () ;

		prefs.node ( node ).node ( VALUES ).put ( key , "COLOR" + key   ) ;
		prefs.node ( node ).node ( VALUES ).node ( COLORS ).putInt ( key + "Red"   , red   ) ;
		prefs.node ( node ).node ( VALUES ).node ( COLORS ).putInt ( key + "Green" , green ) ;
		prefs.node ( node ).node ( VALUES ).node ( COLORS ).putInt ( key + "Blue"  , blue  ) ;
		flushPref () ;
	}
	public static void storeValueFont ( String node , String key , FontData value ) {
		prefs.node ( node ).node ( VALUES ).put ( key , value.toString () ) ;
		flushPref () ;
	}
// NEW
	public static void storeValueAttribute ( String node , String key , WidgetAttribute value ) {
		prefs.node ( node ).node ( VALUES ).put ( key , value.toString () ) ;
		flushPref () ;
	}
	public static void unstoreValue ( Property property ) {
		
		Preferences prefNode = ( property.getParentNode () == null ) ? 
				prefs.node ( property.getNode () ) : 
				prefs.node ( property.getParentNode () ).node ( property.getNode () ) ;
				
		prefNode.node (VALUES).remove ( property.getName ()  ) ;
		property.getContext ().remove ( property.getName () ) ;
		flushPref () ;
	}
	private static void flushPref () {
		try {
			eclipsePrefs.flush () ;
			prefs.flush () ;
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
	}
	private static Color getColor ( Preferences localPrefs , String property ) {
		int blue, green, red ;

		red   = localPrefs.node ( COLORS ).getInt ( property + "Red"   , 0 ) ;
		green = localPrefs.node ( COLORS ).getInt ( property + "Green" , 0 ) ;
		blue  = localPrefs.node ( COLORS ).getInt ( property + "Blue"  , 0 ) ;

		return new Color ( null, new RGB ( red , green , blue ) ) ;
	}
	public static void restorePreferences ( Property property ) {
		Preferences prefNode = ( property.getParentNode () == null ) ? 
					prefs.node ( property.getNode () ) : 
					prefs.node ( property.getParentNode () ).node ( property.getNode () ) ;

		String catalogName = prefNode.get ( "linkToCatalog" , property.getNode () ) ;	
		Preferences values = prefNode.node ( VALUES ) ;

		restorePreferences ( catalogName , property.getContext () , values ) ;		
	}
	public static void restorePreferences (  String node , IEclipseContext context ) {
		String catalogName = prefs.node ( node ).get ( "linkToCatalog" , node ) ;	
		Preferences values = prefs.node ( node ).node ( VALUES ) ;
		restorePreferences ( catalogName , context, values ) ;		
	}
	public static void restorePreferences (  String parentNode , String node , IEclipseContext context ) {
		String catalogName = prefs.node ( parentNode ).node ( node ).get ( "linkToCatalog" , node ) ;	
		Preferences values = prefs.node ( parentNode ).node ( node ).node ( VALUES ) ;
		restorePreferences ( catalogName , context, values ) ;		
	}
	private static void restorePreferences ( String catalogName , IEclipseContext context , Preferences values ) {
		try {
			Preferences privateCatalog 	= prefs.node ( catalogName ).node ( PRIVATE_CATALOG ) ;
			Preferences globalCatalog 	= prefs.node ( GlobalPreferences.name ).node ( CATALOG ) ;

			for ( String prefName : values.keys () ) {
				
				String prefType = globalCatalog.get ( prefName , null ) ;

				if ( prefType == null ) {
					prefType = privateCatalog.get ( prefName , null ) ;
				}
				if ( prefType == null ) {
					System.out.println ( "TrackerPreferences:: " + catalogName + " restorePreferences: Uknown type of preference in catalog for :" + "=="+ prefName ) ;
				}
				else if ( prefType.equals ( "String" ) ) {
					context.set (  prefName , values.get ( prefName , null ) ) ;
				}
				else if ( prefType.equals ( "boolean" ) ) {
					context.set (  prefName , values.getBoolean ( prefName , false ) ) ;
				}
				else if ( prefType.equals ( "int" ) ) {
					context.set (  prefName , values.getInt ( prefName , 0 ) ) ;
				}
				else if ( prefType.equals ( "double" ) ) {
					context.set (  prefName , values.getDouble ( prefName , 0 ) ) ;
				}
				else if ( prefType.equals ( "Color" ) ) {
					context.set (  prefName , getColor ( values , prefName ) ) ;
				}
				else if ( prefType.equals ( "Font" ) ) {
					context.set (  prefName , new FontData ( values.get ( prefName , null ) ) ) ;
				}
// NEW
				else if ( prefType.equals ( "WidgetAttribute" ) ) {
					context.set (  prefName , values.get ( prefName , null ) ) ;
				}
				else {
					System.out.println ( "TrackerPreferences::restorePreferences: Uknown type of preference in catalog for :" + prefName ) ;
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace () ;
		}
	}
	public static ArrayList < Property > getPreferences ( String node , IEclipseContext context , boolean showAllPreferences ) {
		return getPreferences ( node , null , context , showAllPreferences ) ;
	}
	public static ArrayList < Property > getPreferences ( String node , String parentNode , IEclipseContext context , boolean showAllPreferences ) {
		
		ArrayList < Property > widgetProperties = new ArrayList < Property > () ;
		Preferences prefNode = ( parentNode == null ) ? prefs.node ( node ) : prefs.node ( parentNode ).node ( node ) ;
		String 		catalogName 	= prefNode.get ( "linkToCatalog" , node ) ;
		Preferences values	 		= prefNode.node ( VALUES  ) ;
		Preferences localCatalog 	= prefs.node ( catalogName ).node ( CATALOG ) ;
		Preferences globalcatalog 	= prefs.node ( GlobalPreferences.name ).node ( CATALOG ) ;
		
		try {
			for ( String prefName : globalcatalog.keys () ) {

				Property widgetProperty = new Property () ;
				String prefType = globalcatalog.get ( prefName , null ) ;
				widgetProperty.setContext 	  ( context	  		) ;
				widgetProperty.setParentNode  ( parentNode	  	) ;
				widgetProperty.setNode 		  ( node	  		) ;
				widgetProperty.setName 		  ( prefName 		) ;
				widgetProperty.setType 		  ( prefType 		) ;		
				

				if ( localCatalog.get ( prefName, null ) == null ) {
					widgetProperty.setLocalOrigin ( false ) ;
				}
				else {
					widgetProperty.setLocalOrigin ( true ) ;			
				}
				if ( values.get ( prefName, null ) != null ) {

					widgetProperty.setLocallyDefined ( true ) ;

					if ( prefType == null ) {
						System.out.println ( "TrackerPreferences::getPreferences: Unknown type of preference in catalog for :" + prefName + "=="+ values.absolutePath () ) ;
					}
					else if ( prefType.equals ( "String" ) ) {
						widgetProperty.setStringValue ( values.get ( prefName , null ) ) ;
					}
					else if ( prefType.equals ( "boolean" ) ) {
						widgetProperty.setBooleanValue ( values.getBoolean ( prefName , false ) ) ;
					}
					else if ( prefType.equals ( "int" ) ) {
						widgetProperty.setIntValue ( values.getInt ( prefName , 0 ) ) ;
					}
					else if ( prefType.equals ( "double" ) ) {
						widgetProperty.setDoubleValue ( values.getDouble ( prefName , 0 ) ) ;
					}
					else if ( prefType.equals ( "Color" ) ) {
						widgetProperty.setColorValue ( getColor ( values , prefName ) ) ;
					}
					else if ( prefType.equals ( "Font" ) ) {
						widgetProperty.setFontValue ( new FontData ( values.get ( prefName , null ) ) ) ;
					}
// NEW
					else if ( prefType.equals ( "WidgetAttribute" ) ) {
						widgetProperty.setStringValue ( values.get ( prefName , null ) ) ;
					}
					else {
						System.out.println ( "TrackerPreferences::getPreferences: Unknown type of preference in catalog for :" + prefName + ", type: " + prefType ) ;
					}			
				}
				else {		
					widgetProperty.setLocallyDefined ( false ) ;
					if ( prefType == null ) {
						System.out.println ( "TrackerPreferences::getPreferences: Unknown type of preference in catalog for :" + prefName + "=="+ values.absolutePath () ) ;
					}
					else if ( prefType.equals ( "String" ) ) {
						widgetProperty.setStringValue ( ( String ) context.get ( prefName ) ) ;
					}
					else if ( prefType.equals ( "boolean" ) ) {
						widgetProperty.setBooleanValue ( ( boolean ) context.get ( prefName ) ) ;
					}
					else if ( prefType.equals ( "int" ) ) {
						widgetProperty.setIntValue ( ( int ) context.get ( prefName ) ) ;
					}
					else if ( prefType.equals ( "double" ) ) {
						widgetProperty.setDoubleValue ( ( double ) context.get ( prefName ) ) ;
					}
					else if ( prefType.equals ( "Color" ) ) {
						widgetProperty.setColorValue ( ( Color ) context.get ( prefName ) ) ;
					}
					else if ( prefType.equals ( "Font" ) ) {
						widgetProperty.setFontValue ( ((FontData)context.get ( prefName ) ) ) ;
					}
// NEW
					else if ( prefType.equals ( "WidgetAttribute" ) ) {
						widgetProperty.setStringValue ( ( String ) context.get ( prefName ) ) ;
					}
					else {
						System.out.println ( "TrackerPreferences::getPreferences: Unknown type of preference in catalog for :" + prefName + ", type: " + prefType ) ;
					}
				}
				if ( showAllPreferences || widgetProperty.isLocallyDefined () || widgetProperty.isLocalOrigin () ) {
					widgetProperties.add ( widgetProperty ) ;	
				}
			}
		} catch ( Exception e ) {
			System.out.println ( "TrackerPreferences::getPreferences: Exception" ) ;

			e.printStackTrace () ;
		}
		return widgetProperties ;
	}
	public static ArrayList < String > getPartWidgets ( String node ) {
		ArrayList < String > widgetIds = new ArrayList < String > () ;
		try {
			for ( String key :  eclipsePrefs.node ( node ).node ( PART_WIDGET ).keys () ) {
				widgetIds.add ( eclipsePrefs.node ( node ).node ( PART_WIDGET ).get ( key , null ) ) ;
			}
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
		return widgetIds ;
	}

	public static void storeWidgets ( String node , ArrayList < IWidget > widgetList ) {
		try {

			eclipsePrefs.node ( node ).node ( PART_WIDGET  ).removeNode () ;
			flushPref () ;

			for ( int i = 0 ; i < widgetList.size () ; i ++ ) {
				String key = "widgetNumber_" + String.valueOf ( 10 + i ) ;
				eclipsePrefs.node ( node ).node ( PART_WIDGET ).put ( key , widgetList.get ( i ).getId () ) ;
				
			}
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
		flushPref () ;
	}
	public static void storeAttributes ( String node , Map < String , WidgetAttribute > attributesList ) {
		try {

			eclipsePrefs.node ( node ).node ( ATTRIBUTES  ).removeNode () ;
			flushPref () ;
			int i = 0 ;
			for ( WidgetAttribute widgetAttribute : attributesList.values () ) {
				String key = "attributeNumber_" + String.valueOf ( 10 + i ) ;
				eclipsePrefs.node ( node ).node ( ATTRIBUTES  ).put ( key , widgetAttribute.toString () ) ;
				i ++ ;
			}
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
		flushPref () ;
	}
	public static ArrayList < String >  getTargetAttributes ( String node ) {
		ArrayList < String > attributes = new ArrayList < String > () ;
		
		try {
			for ( String key :  eclipsePrefs.node ( node ).node ( ATTRIBUTES ).keys () ) {
				attributes.add ( eclipsePrefs.node ( node ).node ( ATTRIBUTES ).get ( key , null ) ) ;
			}
		} catch ( BackingStoreException e ) {
			e.printStackTrace () ;
		}
		return attributes ;
	}
	public static void definePreferencesCatalog () {
		
		DisplayViewPart.definePreferencesCatalog 			() ;
		Controler.definePreferencesCatalog 					() ;
		GraphWidget.definePreferencesCatalog 				() ;
		GraphAttribute.definePreferencesCatalog 			() ;
		//ScopeWidget.definePreferencesCatalog 				() ;
		MeterWidget.definePreferencesCatalog 				() ;
		GaugeWidget.definePreferencesCatalog 				() ;
		TankWidget.definePreferencesCatalog 				() ;
		//NumericWidget.definePreferencesCatalog 			() ;
		//ColorWidget.definePreferencesCatalog 				() ;
		SliderWidget.definePreferencesCatalog 				() ;
		//ScaleWidget.definePreferencesCatalog	 			() ;
		KnobWidget.definePreferencesCatalog 				() ;
		OnOffWidget.definePreferencesCatalog 				() ;
		//OnOffToggleWidget.definePreferencesCatalog 		() ;
		OpenUdpConnectionPopup.definePreferencesCatalog 	() ;
		OpenBluetoothConnectionPopup.definePreferencesCatalog 	() ;
		OpenSerialConnectionPopup.definePreferencesCatalog 	() ;
		OpenDownloadFilePopup.definePreferencesCatalog 		() ;
	}
}
