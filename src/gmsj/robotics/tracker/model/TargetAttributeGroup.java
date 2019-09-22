package gmsj.robotics.tracker.model;

import java.util.ArrayList;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class TargetAttributeGroup extends TargetAttribute {

	private ArrayList<TargetSubAttribute> 	attributes ;

	public TargetAttributeGroup () {
		super () ;
		this.attributes = new ArrayList<TargetSubAttribute>() ;
	}

	public void setTarget ( Target target ) {
		super.setTarget ( target ) ;
		
		for ( TargetSubAttribute attribute : attributes ) {
			attribute.setTarget ( target ) ;
		}
	}
	@Override
	public ITreeElement[] getChildren () {
		return attributes.toArray( new ITreeElement [ attributes.size() ] ) ;
	}
	@Override
	public boolean hasChildren () {
		
		return ( ! attributes.isEmpty () ) ;
	}
	public void addSubAttribute ( TargetSubAttribute subAttribute ) {
		subAttribute.setTarget ( getTarget () ) ;
		subAttribute.setGroup ( this ) ;
		attributes.add ( subAttribute ) ;
	}
	public ArrayList<TargetSubAttribute> getAttributes () {
		return attributes ;
	}
}
