 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

import gmsj.robotics.tracker.communications.OpenUdpConnectionPopup;


public class OpenUdpConnectionHandler {
	@Execute
	public void execute ( IEclipseContext context ) {
		OpenUdpConnectionPopup dialog = ContextInjectionFactory.make ( OpenUdpConnectionPopup.class, context ) ;   	
    	dialog.open () ;	
	}
}