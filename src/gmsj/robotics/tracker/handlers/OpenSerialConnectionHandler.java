 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

import gmsj.robotics.tracker.communications.OpenSerialConnectionPopup;

public class OpenSerialConnectionHandler {
	@Execute
	public void execute ( IEclipseContext context ) {
		OpenSerialConnectionPopup dialog = ContextInjectionFactory.make ( OpenSerialConnectionPopup.class, context ) ;   	
    	dialog.open () ;
	}	
}