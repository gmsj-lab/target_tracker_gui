package gmsj.robotics.tracker.controler;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class Drag {

	private final int 			dndAttribute = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT ;
	private DragSource 			dragSource ;
	private DragSourceListener 	dragListener ;

	private DragSourceListener getDragSourceListener ( IDragSource source ) {
		return new DragSourceListener () {
			@Override
			public void dragStart ( DragSourceEvent event ) {
				event.doit = false ;
				if ( source.isDragOk ( ) ) {
					event.doit = true ;
				}
			}
			@Override
			public void dragSetData ( DragSourceEvent event ) {
				// Set the data to be the first selected item's attributeId
				event.data = source.getDraggedElement () ;
			}
			@Override
			public void dragFinished ( DragSourceEvent event ) {
				if ( event.doit ) {
					source.dragFinishedSuccessfully () ;
				}
				else {
					source.dragAborted () ;
				}
			}
		} ;
	}
	public void setDrag ( IDragSource source ) {

		if ( ( dragSource == null ) && ( source.getDnDControl () != null ) ) {
			dragListener = getDragSourceListener ( source ) ;
			// Create the drag source on the tree
			dragSource 				   = new DragSource ( source.getDnDControl () , dndAttribute ) ; 
			dragSource.setTransfer 	   ( new Transfer[] { TextTransfer.getInstance () } ) ;
			dragSource.addDragListener ( dragListener ) ;
		}
	}
	public void unsetDrag () {

		if ( ( dragSource != null ) && ( dragSource.isDisposed () == false ) ){
			dragSource.removeDragListener ( dragListener ) ; 
			dragSource.dispose () ;
			dragSource = null ;
		}
	}
}
