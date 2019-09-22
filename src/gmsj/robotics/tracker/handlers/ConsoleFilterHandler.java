 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import gmsj.robotics.tracker.logging.ILogging;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class ConsoleFilterHandler {
	@Execute
	public void execute ( ILogging logger ) {
	    
		System.out.println("ConsoleFilterHandler: not implemented yet");
	}
	
	
	@CanExecute
	public boolean canExecute () {
		
		return true ;
	}
		
}