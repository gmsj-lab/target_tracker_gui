package gmsj.robotics.tracker.controler;

import org.eclipse.swt.widgets.Control;

public interface IDropTarget {
	public Control	getDnDControl 	() ;
	public boolean 	isDropOk 		() ;
	public void 	receiveDrop 	( String dropString ) ;
}
