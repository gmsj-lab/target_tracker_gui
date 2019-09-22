package gmsj.robotics.tracker.navigation;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public void inputChanged ( Viewer v, Object oldInput, Object newInput ) {
	}
	@Override
	public Object[] getElements ( Object parent ) {
		return getChildren ( parent ) ;
	}
	@Override
	public Object getParent ( Object child ) {
		Object parent = null ;

		if ( child instanceof ITreeElement ) {
			parent = ( ( ITreeElement ) child ).getParent () ;  
		}
		return parent ;
	}
	@Override
	public Object [] getChildren ( Object parent ) {
		Object [] children = new Object [0] ;

		if ( parent instanceof ITreeElement ) {
			children = ( ( ITreeElement ) parent ).getChildren () ;
		}
		return children ;
	}
	@Override
	public boolean hasChildren ( Object parent ) {
		boolean hasChildren = false ;

		if ( parent instanceof ITreeElement ) {
			hasChildren = ( ( ( ITreeElement ) parent).hasChildren () ) ;
		}
		return hasChildren ;
	}
	@Override
	public void dispose ( ) {		
	}
}
