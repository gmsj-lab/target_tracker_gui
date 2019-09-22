package gmsj.robotics.tracker.parts;

import javax.inject.Inject;
import javax.annotation.PostConstruct; 

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import gmsj.robotics.tracker.events.GlobalPreferences;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.PreferencesViewerComparator;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.overview.preferencesviewer.PropertyFilter;
import gmsj.robotics.tracker.overview.preferencesviewer.PropertyLocallyDefinedEditingSupport;
import gmsj.robotics.tracker.overview.preferencesviewer.PropertyValueEditingSupport;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;

public class PreferencePart {

	private TableViewer						viewer ;
	private Text 							partNameText ;
	private Text 							propertyNameText ;
	private Text 							propertyValueText ;
	private Button 							defineLocallyButton ;
	private Button 							showAllButton ;
	private PreferencesViewerComparator 	comparator ;
	private PropertyFilter 					filter ;	
	private TableColumnLayout 				tableColumnLayout ;
	private ISelectedPreferenceTarget 		target ;

	@Inject	Shell							shell ;
	@Inject MApplication 					application ;

	@Inject @Optional
	private void setSelectedPreferenceTarget ( ISelectedPreferenceTarget target ) {
		this.target = target ;
		if ( ( viewer != null ) && ( viewer.getContentProvider () != null ) ) {
			if ( target != null ) {
				partNameText.setText ( target.getName () + " (" + target.getId () + ") " ) ;
				propertyNameText.setText  		 ( "" ) ;
				propertyValueText.setText 		 ( "" ) ;
				defineLocallyButton.setSelection ( false ) ;	
				
				if ( target instanceof GlobalPreferences ) {
					showAllButton.setSelection ( false ) ;
					showAllButton.setEnabled ( false ) ;
				}
				else {
					showAllButton.setEnabled ( true ) ;
				}
				if ( showAllButton.getSelection () ) {
					viewer.setInput ( target.getPreferences ( true ) ) ;
				}
				else {
					viewer.setInput ( target.getPreferences ( false ) ) ;
				}
				viewer.refresh () ;
			}
			else {
				viewer.setInput ( null ) ;
			}
			viewer.refresh () ;
		}
	}
	@PostConstruct
	public void postConstruct ( Composite parent ) {

		parent.setLayout ( new GridLayout ( 7, false ) ) ;
		createControl 	  ( parent ) ;
		parent.layout () ;
	}
	private void createControl ( Composite parent ) {

		Label searchLabel = new Label ( parent , SWT.NONE ) ;
		searchLabel.setText ( "Search: " ) ;
		final Text searchText = new Text ( parent, SWT.BORDER | SWT.SEARCH ) ;
		searchText.setLayoutData ( new GridData ( SWT.FILL ) ) ;

		partNameText = new Text 			( parent, SWT.BORDER |  SWT.READ_ONLY ) ;
		partNameText.setText 		  		( "" ) ;
		partNameText.setMessage 			( "Part name" ) ;
		partNameText.setEnabled 			( false ) ;
		partNameText.setLayoutData 			( new GridData ( GridData.FILL_HORIZONTAL ) ) ;

		propertyNameText = new Text 		( parent, SWT.BORDER |  SWT.READ_ONLY ) ;
		propertyNameText.setText 		  	( "" ) ;
		propertyNameText.setMessage 		( "Property name" ) ;
		propertyNameText.setEnabled 		( false ) ;
		propertyNameText.setLayoutData 		( new GridData ( GridData.FILL_HORIZONTAL ) ) ;

		defineLocallyButton = new Button 	( parent , SWT.CHECK ) ;
		defineLocallyButton.setText 		( "local" ) ;	
		defineLocallyButton.setVisible 		( true ) ;
		defineLocallyButton.setEnabled 		( false ) ;

		propertyValueText = new Text 		( parent, SWT.BORDER ) ;
		propertyValueText.setText 		  	( "" ) ;
		propertyValueText.setMessage 		( "value" ) ;
		propertyValueText.setEnabled 		( false ) ;
		propertyValueText.setLayoutData 	( new GridData ( GridData.FILL_HORIZONTAL ) ) ;

		showAllButton = new Button 			( parent , SWT.CHECK ) ;
		showAllButton.setText 				( "Show all" ) ;	
		showAllButton.setVisible 			( true ) ;
		showAllButton.setSelection 			( false ) ;
		showAllButton.addSelectionListener ( new SelectionListener () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				if ( target != null ) {
					if ( showAllButton.getSelection () ) {
						viewer.setInput ( target.getPreferences ( true ) ) ;
					}
					else {
						viewer.setInput ( target.getPreferences ( false ) ) ;
					}
				}
			}
			@Override
			public void widgetDefaultSelected ( SelectionEvent e ) {
				// TODO Auto-generated method stub	
			}
		});
		createTableViewer ( parent ) ;

		comparator = new PreferencesViewerComparator () ;
		viewer.setComparator ( comparator ) ;

		searchText.addKeyListener ( new KeyAdapter () {
			public void keyReleased ( KeyEvent key ) {
				filter.setSearchText ( searchText.getText () ) ;
				viewer.refresh () ;
			}
		});
		filter = new PropertyFilter () ;
		viewer.addFilter ( filter ) ;
	}	
	private void createTableViewer ( Composite parent ) {

		Composite tableComposite = new Composite ( parent, SWT.NONE ) ;
		tableColumnLayout 		 = new TableColumnLayout () ;
		tableComposite.setLayout ( tableColumnLayout ) ;

		viewer = new TableViewer ( tableComposite , SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER ) ;
		createColumns ( parent, viewer ) ;
		final Table table = viewer.getTable () ;
		table.setHeaderVisible ( true ) ;
		table.setLinesVisible  ( true ) ;

		viewer.setContentProvider ( new ArrayContentProvider () ) ; 
		viewer.setInput ( null ) ;

		GridData gridData 					= new GridData () ;
		gridData.horizontalSpan 			= 7 ; 
		gridData.grabExcessHorizontalSpace 	= false ;
		gridData.grabExcessVerticalSpace 	= true ;
		gridData.horizontalAlignment 		= SWT.FILL ;
		gridData.verticalAlignment 			= SWT.FILL ;
		tableComposite.setLayoutData 		( gridData ) ;

		viewer.addSelectionChangedListener ( new ISelectionChangedListener () {
			@Override
			public void selectionChanged ( SelectionChangedEvent event ) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection () ;
				Property property = ( Property ) selection.getFirstElement () ;

				if ( property != null ) {
					propertyNameText.setText  		 ( property.getName () ) ;
					propertyValueText.setText 		 ( property.getValue () ) ;
					defineLocallyButton.setSelection ( property.isLocallyDefined () ) ;	
				}
			}
		});
	}
	private void createColumns ( final Composite parent , final TableViewer viewer ) {
		String[] titles = { "Property name", "type", "Origin" , "Defined Locally" , "Value" } ;
		int[] bounds 	= { 80, 25, 25 , 20 , 80 } ;

		// "Property name" _____________________________________________________________________________
		TableViewerColumn col = createTableViewerColumn ( titles [0] , bounds [0] , 0 ) ;
		col.setLabelProvider ( new ColumnLabelProvider () {
			@Override
			public String getText ( Object element ) {
				Property property = ( Property ) element ;
				return property.getName () ;
			}
		});
		tableColumnLayout.setColumnData ( col.getColumn (), new ColumnWeightData ( 20 , 80 , true ) ) ;

		// "Property type"_____________________________________________________________________________
		col = createTableViewerColumn ( titles [1] , bounds [1] , 1 ) ;
		col.setLabelProvider ( new ColumnLabelProvider () {
			@Override
			public String getText ( Object element ) {
				Property property = ( Property ) element ;
				return property.getType () ;
			}
		});
		tableColumnLayout.setColumnData ( col.getColumn (), new ColumnWeightData ( 8 , 25 , true ) ) ;

		// "Property Origin"___________________________________________________________________________
		col = createTableViewerColumn ( titles [2] , bounds [2] , 2 ) ;
		col.setLabelProvider ( new ColumnLabelProvider () {
			@Override
			public String getText ( Object element ) {
				String text = new String () ;
				Property property = ( Property ) element ;
				if ( property.isLocalOrigin () ) {
					text = "Local" ;
				}
				else {
					text = "External" ;
				}
				return text ;
			}
		});
		tableColumnLayout.setColumnData ( col.getColumn (), new ColumnWeightData ( 8 , 15 , true ) ) ;

		// "Defined Locally"___________________________________________________________________________
		col = createTableViewerColumn ( titles [3] , bounds [3] , 3 ) ;
		col.setLabelProvider ( new ColumnLabelProvider () {
			@Override
			public String getText ( Object element ) {
				String text = new String () ;
				Property property = ( Property ) element ;
				if ( property.isLocallyDefined () ) {
					text = "YES" ;
				}
				else {
					text = "NO" ;
				}
				return text ;
			}
		});
		col.setEditingSupport ( new PropertyLocallyDefinedEditingSupport ( viewer , partNameText ) ) ;
		tableColumnLayout.setColumnData ( col.getColumn (), new ColumnWeightData ( 8 , 15 , true ) ) ;

		// "Property value______________________________________________________________________________
		col = createTableViewerColumn ( titles [4] , bounds [4] , 4 ) ;
		col.setLabelProvider ( new ColumnLabelProvider () {
			@Override
			public String getText ( Object element ) {
				Property property = ( Property ) element ;
				return property.getValue () ;			
			}
		});
		col.setEditingSupport ( new PropertyValueEditingSupport ( viewer ) ) ;
		tableColumnLayout.setColumnData ( col.getColumn () , new ColumnWeightData ( 20 , 100 , true ) ) ;
	}
	private TableViewerColumn createTableViewerColumn ( String title , int bound , final int colNumber ) {
		final TableViewerColumn viewerColumn = new TableViewerColumn ( viewer , SWT.NONE ) ;
		final TableColumn column = viewerColumn.getColumn () ;
		column.setText 		( title ) ;
		column.setWidth 	( bound ) ;
		column.setResizable ( true ) ;
		column.setMoveable 	( true ) ;
		column.addSelectionListener ( getSelectionAdapter ( column, colNumber ) ) ;
		return viewerColumn ;
	}
	private SelectionAdapter getSelectionAdapter ( final TableColumn column , final int index ) {
		SelectionAdapter selectionAdapter = new SelectionAdapter () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				comparator.setColumn ( index ) ;
				int direction = comparator.getDirection () ;
				viewer.getTable().setSortDirection ( direction ) ;
				viewer.getTable().setSortColumn ( column ) ;
				viewer.refresh () ;
			}
		} ;
		return selectionAdapter ;
	}
	@Focus
	public void onFocus () {
		viewer.getControl().setFocus () ;	
	}
}
