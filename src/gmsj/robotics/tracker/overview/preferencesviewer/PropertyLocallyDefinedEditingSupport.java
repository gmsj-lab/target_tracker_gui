package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class PropertyLocallyDefinedEditingSupport extends EditingSupport {

	  private final TableViewer 		viewer ;
	  private 		Text 				partNameText ;
	  private final CheckboxCellEditor	checkboxEditor ; 

	  public PropertyLocallyDefinedEditingSupport ( TableViewer viewer, Text partNameText ) {
	    super ( viewer ) ;
	    this.viewer = viewer ;
	    this.partNameText = partNameText ;
		this.checkboxEditor = new CheckboxCellEditor ( viewer.getTable () , SWT.CHECK | SWT.READ_ONLY ) ;
	  }

	  @Override
	  protected CellEditor getCellEditor ( Object element ) {
	  String[] defineLocally = new String [ 2 ] ;
	  	defineLocally [0] = "YES" ;
	  	defineLocally [1] = "NO" ;

	    return checkboxEditor ;
	  }

	  @Override
	  protected boolean canEdit ( Object element ) {

		if ( partNameText.getText ().contains ( "General Preferences" ) ) {
			return false ;	
		}
	    return true ;
	  }

	  @Override
	  protected Object getValue ( Object element ) {
	    Property property = ( Property ) element ;
	    return property.isLocallyDefined () ;
	  }

	  @Override
	  protected void setValue ( Object element , Object value ) {
	    Property property = ( Property ) element ;
	    property.setLocallyDefined ( (boolean) value ) ;
	    if ( property.isLocallyDefined () ) {
		    TrackerPreferences.storeValue 		  ( property ) ;
		    TrackerPreferences.restorePreferences ( property ) ;
		    viewer.update ( element , null ) ;
	    }
	    else {
		    TrackerPreferences.unstoreValue 	  ( property ) ;	
	    }
	    viewer.refresh () ;
	  }
	}