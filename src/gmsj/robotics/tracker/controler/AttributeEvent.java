package gmsj.robotics.tracker.controler;

public class AttributeEvent
{
	int 	attributeId ;
	double 	attributeValue ;
	double 	timeTag ;

	public AttributeEvent () {
		this.attributeId 	= 0 ;
		this.attributeValue = 0.0 ;
		this.timeTag 		= 0.0 ;
	}
	public AttributeEvent ( String attributeId , String attributeValue , Double timeTag ) {
		this.attributeId 	= Integer.parseInt ( attributeId ) ;
		this.attributeValue = Double.parseDouble ( attributeValue ) ;
		this.timeTag 		= timeTag ;
	}
	public AttributeEvent ( String attributeId , String attributeValue ) {
		this.attributeId 	= Integer.parseInt ( attributeId ) ;
		this.attributeValue = Double.parseDouble ( attributeValue ) ;
	}
	public AttributeEvent copy ( AttributeEvent event ) {
		this.attributeId 	= event.attributeId ;
		this.attributeValue = event.attributeValue ;
		this.timeTag 		= event.timeTag ;
		return this ;
	}
	public void setTimeTag ( Double timeTag ) {
		this.timeTag = timeTag ;
	}
	public Double getTimeTag () {
		return timeTag ;
	}
	public boolean setValue ( String attributeValue ) {
		try {
			this.attributeValue = Double.parseDouble ( attributeValue ) ;
		} catch (NumberFormatException e) {
			System.err.println("AttributeEvent::setValue: invalid value :" + attributeValue ) ;
			return false;

		}
		return true;
	}
	public double getValue () {
		return attributeValue ;
	}
	public String getValueString () {
		return String.valueOf( (long)attributeValue ) ;
	}
	public boolean setAttributeId ( String attributeId ) {
		try {
			this.attributeId = Integer.parseInt ( attributeId ) ;
		} catch (NumberFormatException e) {
			System.err.println("AttributeEvent::setAttributeId: invalid value :" + attributeId ) ;
			this.attributeId = 0 ;
			return false;
		}
		return true;
	}
	public int getAttributeId () {
		return attributeId ;
	}
	public String getAttributeIdString () {
		return String.valueOf( attributeId ) ;
	}
	public String toString () {
		return "event:" +":" + attributeId +":" + attributeValue +":" + timeTag ;
	}
}
//___________________________________________________________________________________________