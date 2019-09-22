package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

public class PreferencesViewerComparator extends ViewerComparator {

	private int propertyIndex ;
	private static final int DESCENDING = 1 ;
	private int direction = DESCENDING ;

	public PreferencesViewerComparator () {
		this.propertyIndex = 0 ;
		direction = DESCENDING ;
	}

	public int getDirection () {
		return direction == 1 ? SWT.DOWN : SWT.UP ;
	}

	public void setColumn ( int column ) {
		if ( column == this.propertyIndex ) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column ;
			direction = DESCENDING ;
		}
	}

	@Override
	public int compare ( Viewer viewer , Object e1, Object e2 ) {
		Property p1 = (Property) e1 ;
		Property p2 = (Property) e2 ;
		int rc = 0 ;
		switch ( propertyIndex ) {
			case 0 :
				rc = p1.getName ().compareTo ( p2.getName () ) ;
				break;
			case 1 :
				rc = p1.getType ().compareTo ( p2.getType () ) ;
				break ;
			case 2 :
				String s1 = ( p1.isLocalOrigin () ) ? "Local" : "External" ;
				String s2 = ( p2.isLocalOrigin () ) ? "Local" : "External" ;
				rc = s1.compareTo ( s2 ) ;
				break ;
			case 3 :
				s1 = ( p1.isLocallyDefined () ) ? "YES" : "NO" ;
				s2 = ( p2.isLocallyDefined () ) ? "YES" : "NO" ;
				rc = s1.compareTo ( s2 ) ;
				break ;
			case 4 :
				rc = p1.getValue ().compareTo ( p2.getValue () ) ;
				break ;
			default :
				rc = 0 ;
		}
		// If descending order, flip the direction
		if ( direction == DESCENDING ) {
			rc = -rc ;
		}
		return rc ;
	}

} 
