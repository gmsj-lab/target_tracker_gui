package gmsj.robotics.tracker.controler;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class Drop {

	private final 	int 				dndAttribute = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT ;
	private 		DropTarget 			dropTarget ;
	private 		DropTargetAdapter 	dropTargetAdapter ;
	
	private DropTargetAdapter getDropAdapter ( IDropTarget target ) {
		
		return new DropTargetAdapter () {
			@Override
			public void drop ( DropTargetEvent event ) {
				if ( event.data != null ) {
					target.receiveDrop ( event.data.toString () ) ;
				}	
			}
			@Override
			public void dragOver ( DropTargetEvent event ) {
				event.feedback = DND.FEEDBACK_SELECT ;
				event.detail = DND.DROP_NONE ;
				
				if ( target.isDropOk () ) {
					event.detail = DND.DROP_COPY ;
				}
			}
		} ;
	}
	public void setDrop ( IDropTarget target ) {

		unsetDrop () ;
		// Create the drop target
		dropTarget = new DropTarget ( target.getDnDControl () , dndAttribute ) ;

		dropTargetAdapter 			= getDropAdapter ( target ) ;
		dropTarget.setTransfer		( new Transfer[] { TextTransfer.getInstance() } ) ;
		dropTarget.addDropListener	( dropTargetAdapter ) ;
	}
	public void unsetDrop () {
		
		if ( ( dropTarget != null ) && ( dropTarget.isDisposed () == false ) ){
			dropTarget.removeDropListener ( dropTargetAdapter ) ; 
			dropTarget.dispose () ;
			dropTarget = null ;
		}
	}
}
