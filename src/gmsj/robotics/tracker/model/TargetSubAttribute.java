package gmsj.robotics.tracker.model;

public class TargetSubAttribute extends TargetAttribute {

	private TargetAttributeGroup group ;
		
	public TargetSubAttribute () {
		super () ;
		group = null ;
	}
	public void setGroup ( TargetAttributeGroup group ) {
		this.group = group ;
	}
	public TargetAttributeGroup getGroup () {
		return group ;
	}
}
