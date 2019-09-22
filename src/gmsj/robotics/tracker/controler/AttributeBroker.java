package gmsj.robotics.tracker.controler;

import java.util.ArrayList;

import gmsj.robotics.tracker.widgets.WidgetAttribute.IAttributeListener;

public class AttributeBroker {

	private int								 nbClient		 = 0 ;								
	private ArrayList < IAttributeListener > listeners   	 = new ArrayList < IAttributeListener > () ;
	private ArrayList < IAttributeListener > updateListeners = new ArrayList < IAttributeListener > () ;

	public void register ( IAttributeListener listener , boolean updateService ) {
		if ( updateService ) {
			updateListeners.add ( listener ) ;
		}
		else {
			listeners.add ( listener ) ;
		}
		nbClient ++ ;
	}
	public void unRegister ( IAttributeListener listener ) {
		listeners.remove    	( listener ) ;
		updateListeners.remove  ( listener ) ;
		nbClient -- ;
	}
	public boolean isEmpty () {
		return ( nbClient == 0 ) ;
	}	
	public void attributeNotification ( AttributeEvent event ) {
		for ( IAttributeListener listener : listeners ) {
			listener.attributeChanged ( event ) ;
		}
		for ( IAttributeListener listener : updateListeners ) {
			listener.attributeChanged ( event ) ;
		}
	}
}