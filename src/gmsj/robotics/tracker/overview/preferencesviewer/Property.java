package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

public class Property {

	private IEclipseContext context ;
	private String  		name ;
	private String 			node ; 
	private String 			prefType ;
	private boolean 		localOrigin ;
	private boolean 		locallyDefined ;
	private String 			value ;
	private String 			stringValue ;
	private int 			intValue ;
	private boolean 		booleanValue ;
	private Color 			colorValue ;
	private double 			doubleValue ;
	private FontData 		fontValue ;
	private String 			parentNode ;

	public String getName () {
		return  name ;
	}
	public void setName ( String name ) {
		this.name = name;
	}
	public String getType () {
		return prefType ;
	}
	public void setType ( String prefType ) {
		this.prefType = prefType ;
	}
	public boolean isLocalOrigin () {
		return localOrigin ;
	}
	public void setLocalOrigin ( boolean value ) {
		this.localOrigin = value ;
	}
	public void setLocalOrigin () {
		this.localOrigin = true ;
	}
	public void unsetLocalOrigin () {
		this.localOrigin = false ;		
	}
	public void setLocallyDefined (  boolean value ) {
		this.locallyDefined = value ;
	}
	public boolean isLocallyDefined () {
		 return locallyDefined ;
	}
	public void setValue ( String input ) {

		if ( getType ().equals ( "String" ) ) {
			setStringValue ( String.valueOf ( input ) ) ;
		}
		else  if ( getType ().equals ( "boolean" ) ) {
			setBooleanValue ( input.equals ( "true" ) ) ;
		}
		else if ( getType ().equals ( "int" ) ) {
			try {
				setIntValue ( Integer.valueOf ( input ) ) ;
			} catch ( NumberFormatException e ) {
				setIntValue ( 0 ) ;
			}
		}
		else if ( getType ().equals ( "double" ) ) {
			try {
				setDoubleValue ( Double.valueOf ( input ) ) ;
			} catch ( NumberFormatException e ) {
				setDoubleValue ( 0 ) ;
			}
		}
		else if ( getType ().equals ( "Color" ) ) {
			// TODO setColorValue ( String.valueOf ( input ) ) ;
		}
		else {
			System.out.println ( "Property ::setValue: Uknown type of preference in catalog for :" + getType () ) ;
		}
	}
	public String getValue () {
		return value ;
	}
	
	public void setStringValue ( String value ) {
		this.stringValue = value ;
		this.value 		 = value ;
	}
	public String getStringValue () {
		return stringValue ;
	}

	public void setIntValue ( int value ) {
		this.intValue 	= value ;
		this.value 		= String.valueOf ( value ) ;
	}
	public int getIntValue () {
		return intValue ;
	}
	public void setBooleanValue ( boolean value ) {
		this.booleanValue = value ;
		this.value 		  = String.valueOf ( value ) ;
	}
	public boolean getBooleanValue () {
		return booleanValue ;
	}

	public void setDoubleValue ( double value ) {
		this.doubleValue  = value ;
		this.value 		  = String.valueOf ( value ) ;
	}
	public double getDoubleValue () {
		return doubleValue ;
	}

	public void setColorValue ( Color value ) {
		this.colorValue = value ;
		this.value 		= String.valueOf ( value ) ;
	}
	public Color getColorValue () {
		return colorValue ;
	}
	public void setFontValue ( FontData value ) {
		this.fontValue = value ;
		this.value = String.valueOf ( value ) ;
	}
	public FontData getFontValue () {
		return fontValue ;
	}
	public void setNode ( String node ) {
		this.node = node ;
	}
	public String getNode () {
		return node ;
	}
	public void setContext ( IEclipseContext context ) {
		this.context = context ;		
	}
	public IEclipseContext getContext () {
		return context ;		
	}
	public void setParentNode ( String parentNode ) {
		this.parentNode = parentNode ;
	}
	public String getParentNode () {
		return parentNode ;
	}
}
