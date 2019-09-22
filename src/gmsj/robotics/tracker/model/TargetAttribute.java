package gmsj.robotics.tracker.model;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class TargetAttribute extends ModelObject implements ITargetAttribute , ITreeElement {
	
	private Target		target ;
	private String 		name ;
	private String 	 	id ;
	private String 		value ;
	private DataType 	type ;
	private boolean  	write ;
	private Double 		timeTag ;
	
	public TargetAttribute () {
		target 		= null ;
		name 		= new String () ;
		id	 		= new String () ;
		value 		= new String () ;
		type 		= DataType.INVALID ; 
		write 		= true ;
		timeTag 	= 0.0 ;
	}
	public TargetAttribute ( String attributeString ) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setParent ( ITreeElement parent ) {
		this.target	= ( Target ) parent ;	
	}
	public void setTarget ( Target target ) {
		this.target	= target ;	
	}
	public Target getTarget () {
		return target ;	
	}
	public ITreeElement getParent () {
		return target ;
	}
	public ITreeElement[] getChildren () {
		return null ;
	}
	public boolean hasChildren () {
		return false ;
	}
	public String getName () {
		return name ;
	}
	public void setName ( String name ) {
		this.name = name ;
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
		return type;
	}
	public void setWrite ( boolean write ) {
		this.write = write;
	}
	public boolean isWrite () {
		return write;
	}
	public void setLastUpdateTimeTag ( Double lastUpdateTimeTag ) {
		this.timeTag = lastUpdateTimeTag ;
	}
	public Double getLastUpdateTimeTag () {
		return timeTag / 1000.0 ;
	} 
	public String getValue () {
		return value;
	}
	public float getFloatValue () {
		return Float.valueOf ( value );
	}
	@Override
	public String getTargetName ( ) {
		return target.getName () ;
	}
}
