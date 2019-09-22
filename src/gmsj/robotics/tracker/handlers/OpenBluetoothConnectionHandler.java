 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

import gmsj.robotics.tracker.communications.OpenBluetoothConnectionPopup;

public class OpenBluetoothConnectionHandler {
	@Execute
	public void execute ( IEclipseContext context ) {
		OpenBluetoothConnectionPopup dialog = ContextInjectionFactory.make ( OpenBluetoothConnectionPopup.class, context ) ; 
    	dialog.open () ;
	}	
}