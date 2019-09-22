package gmsj.robotics.tracker.controler;

import org.eclipse.swt.widgets.Control;

public interface IDragSource {
	public Control	getDnDControl				() ;
	public boolean 	isDragOk 					() ;
	public String 	getDraggedElement			() ;
	public void 	dragFinishedSuccessfully 	() ;
	public void 	dragAborted 				() ;
}
