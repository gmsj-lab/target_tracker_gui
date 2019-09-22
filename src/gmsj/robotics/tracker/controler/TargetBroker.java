package gmsj.robotics.tracker.controler;

import java.util.HashMap;
import java.util.Map;

import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.widgets.WidgetAttribute.IAttributeListener;

public class TargetBroker {

	private 	 	Map < String, AttributeBroker > brokers 		= new HashMap < String, AttributeBroker > () ;
	private 		Target 							target ;
	private 		String							targetName ;

	public TargetBroker( String targetName ) {
		this.targetName = targetName ;
		}

	public void setTarget ( Target target ) {
		this.target = target ;
		}
	public void register ( String attributeId , IAttributeListener listener , boolean updateService ) {
		AttributeBroker broker ;

		if ( ! brokers.containsKey ( attributeId ) ) {
			broker = new AttributeBroker () ;
			brokers.put ( attributeId , broker ) ;	
		}
		else {
			broker = brokers.get ( attributeId ) ;
		}
		broker.register ( listener , updateService ) ;
	}
	public void unRegister ( String attributeId , IAttributeListener listener ) {
		AttributeBroker broker ;

		if ( brokers.containsKey ( attributeId ) ) {
			broker = brokers.get ( attributeId ) ;
			broker.unRegister ( listener ) ;
		}
	}	
	public void sendAttribute ( AttributeEvent sendEvent ) {

		if ( target != null ) {
			target.attributeChangeNotification ( sendEvent ) ;
		}
		else {
			System.err.println ( "TargetBroker:: Not connected to a valid target. targetName:" + targetName ) ;
		}
	}
	public void attributeNotification ( AttributeEvent event ) {	
		AttributeBroker broker = brokers.get ( event.getAttributeIdString() ) ;
		if ( broker != null ) {
			broker.attributeNotification ( event ) ;
		}
	}
}

