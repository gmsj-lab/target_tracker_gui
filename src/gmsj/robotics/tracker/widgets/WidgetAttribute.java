package gmsj.robotics.tracker.widgets;

import java.util.ArrayList;
import java.util.StringTokenizer;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.TargetBroker;
import gmsj.robotics.tracker.model.DataType;
import gmsj.robotics.tracker.model.ITargetAttribute;
import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;

public class WidgetAttribute implements ITreeElement , ISelectedPreferenceTarget {
	static final String SEPARATOR = ":";
	
	private String 				targetName ;
	private String 				name ;
	private String 				id ;
	private DataType 			type ;
	private boolean 			writeOnly ;
	private IAttributeListener 	listener ;
	private TargetBroker 		broker ;
	private ITreeElement 		parent ;
	private AttributeEvent 		sendEvent  	= new AttributeEvent ()  ;

	
	public interface IAttributeListener {
		void attributeChanged ( AttributeEvent event ) ;
	}
	public WidgetAttribute () {
	}
	public WidgetAttribute ( ITargetAttribute  targetAttribute ) {
		targetName  = targetAttribute.getTargetName  () ;
		name	  	= targetAttribute.getName () ;
		id		  	= targetAttribute.getId () ;
		type		= targetAttribute.getType () ;
		writeOnly 	= targetAttribute.isWrite () ;
		parent 		= ( Target ) targetAttribute.getParent () ;
		broker 		= Controler.getInstance ().getBroker ( targetName ) ;
		
		sendEvent.setAttributeId ( id ) ;
	}
	public WidgetAttribute ( String WidgetAttributeString ) {
	
		StringTokenizer tokenizer = new StringTokenizer ( WidgetAttributeString , SEPARATOR , false ) ; 

		targetName  = tokenizer.nextToken () ;
		name	  	= tokenizer.nextToken () ;
		id		  	= tokenizer.nextToken () ;
		type		= DataType.valueOf ( tokenizer.nextToken () ) ;
		writeOnly 	= ( tokenizer.nextToken ().equals ( "true" ) ) ? true : false ;
		broker 		= Controler.getInstance ().getBroker ( targetName ) ;
		
		sendEvent.setAttributeId ( id ) ;
	}
	public String toString () {
		return ( targetName + SEPARATOR + name + SEPARATOR + id + SEPARATOR + type.toString () + SEPARATOR + String.valueOf ( writeOnly ) ) ;
	}
	public static WidgetAttribute valueOf ( String WidgetAttributeString ) {
//		WidgetAttribute widgetAttribute = new WidgetAttribute () ;
//		
//		StringTokenizer tokenizer = new StringTokenizer ( WidgetAttributeString , SEPARATOR , false ) ; 
//
//		widgetAttribute.targetName  = tokenizer.nextToken () ;
//		widgetAttribute.name	  	= tokenizer.nextToken () ;
//		widgetAttribute.id		  	= tokenizer.nextToken () ;
//		widgetAttribute.type		= DataType.valueOf ( tokenizer.nextToken () ) ;
//		widgetAttribute.writeOnly 	= ( tokenizer.nextToken ().equals ( "true" ) ) ? true : false ;
//		widgetAttribute.broker = Controler.getInstance ().getBroker ( widgetAttribute.targetName ) ;
//		
//		widgetAttribute.sendEvent.setAttributeId ( widgetAttribute.id ) ;
		
		return new WidgetAttribute ( WidgetAttributeString ) ;
	}
	public void setName ( String name ) {
		this.name = name ;
	}
	public String getName () {
		return name ;
	}
	public void setId ( String id ) {
		this.id = id ;
	}
	public String getId () {
		return id ;
	}	
	public void setType ( DataType type ) {
		this.type = type ;
	}
	public DataType getType () {
		return type ;
	}
	public void setWrite ( boolean writeOnly ) {
		this.writeOnly = writeOnly ;
	}
	public boolean isWrite () {
		return writeOnly ;
	}
	@Override
	public ITreeElement getParent () {
		return parent ;
	}
	@Override
	public ITreeElement [] getChildren () {
		return null ;
	}
	@Override
	public boolean hasChildren () {
		return false ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {
		this.parent = parent ;
	}
	public void setValue ( String value ) {
		sendEvent.setValue( value ) ;
		broker.sendAttribute ( sendEvent ) ;
	}
	public void registerForAttribute ( IAttributeListener listener , boolean updateService ) {
		this.listener = listener ;
		broker.register ( id , listener , updateService ) ;
	}
	public void unRegisterForAttribute () {
		broker.unRegister ( id , listener ) ;
	}		
	public boolean isValid ( String value ) {
		boolean status = false ;
		if( ( Float.valueOf ( value ) >= getType ().getMinimumValue () ) &&
			( Float.valueOf ( value ) <= getType ().getMaximumValue () )  ) {
			status = true ;
		}
		else {
			System.out.println ( "Attribute " + name + " out of bounds: " + value ) ;
		}
		return status ;
	}
	@Override
	public ArrayList < Property > getPreferences ( boolean swohAll ) {
		return null ;
	}
}

