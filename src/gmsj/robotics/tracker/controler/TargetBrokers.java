package gmsj.robotics.tracker.controler;

import java.util.HashMap;
import java.util.Map;

import gmsj.robotics.tracker.model.Target;

public class TargetBrokers {

	private Map < String, TargetBroker > brokers = new HashMap < String, TargetBroker > () ;

	public void setTarget ( Target target ) {
		TargetBroker broker = get ( target.getName() ) ;
		broker.setTarget ( target ) ;
	}
	public TargetBroker get ( String targetName ) {
		if ( ! brokers.containsKey ( targetName ) ) {
			TargetBroker broker = new TargetBroker ( targetName ) ;
			brokers.put ( targetName , broker ) ;	
		}
		return brokers.get ( targetName ) ;
	}
}
