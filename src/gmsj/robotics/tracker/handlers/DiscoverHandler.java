 
package gmsj.robotics.tracker.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.parts.TargetNavigationPart;

public class DiscoverHandler {
	@Execute
	public void execute() {
		Target target = TargetNavigationPart.getSelectedTarget () ;
		target.sendDiscoveryMsg () ;
	}
}