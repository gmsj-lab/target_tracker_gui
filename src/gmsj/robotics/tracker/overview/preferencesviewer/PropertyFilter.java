package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class PropertyFilter extends ViewerFilter {

	  private String searchString ;

	  public void setSearchText ( String s ) {
	    // ensure that the value can be used for matching 
	    this.searchString = ".*" + s + ".*";
	  }

	  @Override
	  public boolean select ( Viewer viewer , Object parentElement , Object element ) {
	    if ( searchString == null || searchString.length () == 0 ) {
	      return true ;
	    }
	    Property property = (Property) element ;
	    if ( property.getName().matches ( searchString ) ) {
	      return true ;
	    }
	    if (property.getType().matches ( searchString ) ) {
	      return true ;
	    }
	    return false ;
	  }
	}
