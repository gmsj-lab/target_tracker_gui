
package gmsj.robotics.tracker.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.events.GlobalPreferences;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.navigation.PreferencesTreeContentProvider;
import gmsj.robotics.tracker.navigation.PreferencesTreeLabelProvider;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.widgets.IWidget;

public class DisplayOverviewPart implements ITreeElement {

	private TreeViewer 			viewer ;
	private Composite 			parent ;
	private MPart 				activeDisplay ;
	private Text 				displayNameText ;

	@Inject EMenuService 		menuService ;
	@Inject MApplication 		application ;

	@PostConstruct
	public void postConstruct (  MApplication application , Composite parent ,  
			@Named ( IServiceConstants.ACTIVE_PART ) MPart activePart , EPartService partService ) {
		this.parent = parent ;
		createControl ( parent ) ;
		menuService.registerContextMenu ( viewer.getControl ()  , Tracker.DELETE_POPUP_MENU ) ;
		parent.layout () ;		
	}
	private void createControl ( Composite parent ) {

		parent.setLayout ( new GridLayout ( 1, false ) ) ;

		displayNameText = new Text 		  ( parent, SWT.BORDER ) ;
		displayNameText.setText 		  ( "" ) ;
		displayNameText.setMessage 		  ( "Rename active display" ) ;
		displayNameText.setEnabled 		  ( false ) ;
		displayNameText.setLayoutData 	  ( new GridData ( GridData.FILL_HORIZONTAL ) ) ;
		displayNameText.addModifyListener ( new ModifyListener () {
			@Override
			public void modifyText ( ModifyEvent e ) {
				//TODO :TrackerPreferences.changeNodeName ( "Display_" + activeDisplay.getLabel () , "Display_" + displayNameText.getText () ) ;
				activeDisplay.setLabel ( displayNameText.getText () ) ;
			}
		});
		createTreeViewer ( parent ) ;
	}
	private void createTreeViewer ( Composite parent ) {
		//		this.parent = parent ;
		final int treeStyle = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER ;

		viewer = new TreeViewer		( parent , treeStyle ) ;
		viewer.setContentProvider	( new PreferencesTreeContentProvider () ) ;
		viewer.setLabelProvider 	( new DelegatingStyledCellLabelProvider ( new PreferencesTreeLabelProvider ( ) ) ) ;
		viewer.setInput				( this ) ;
		viewer.expandAll 			() ;
		viewer.getTree ().setLayoutData ( new GridData ( GridData.FILL_BOTH ) ) ;
		viewer.addSelectionChangedListener ( new ISelectionChangedListener () {
			@Override
			public void selectionChanged ( SelectionChangedEvent event ) {

				application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;

				IStructuredSelection selection = viewer.getStructuredSelection () ;
				if ( selection == null ) {
					return ;
				}
				if ( selection.getFirstElement () instanceof ISelectedPreferenceTarget ) {

					ISelectedPreferenceTarget selectedPreference = ( ISelectedPreferenceTarget ) selection.getFirstElement () ; 
					application.getContext ().modify ( ISelectedPreferenceTarget.class, selectedPreference ) ;
				}
				else if ( selection.getFirstElement () instanceof Controler ) {
					application.getContext ().modify ( ISelectedPreferenceTarget.class, new GlobalPreferences ( application.getContext () ) ) ;
				}
			}
		});
	}
	public void setViewerInput () {
		if ( viewer != null ) {
			viewer.refresh 	 () ;			
			viewer.expandAll () ;
			parent.layout 	 () ;
		}
	}
	@Inject
	@Optional
	private void receiveActivePart ( @Named ( IServiceConstants.ACTIVE_PART ) MPart activePart , EPartService partService ) {
		if ( displayNameText != null ) {
			if ( activePart != null ) {
				if ( activePart.getElementId().equals ( Tracker.DISPLAYVIEW_ID ) ) {

					activeDisplay = activePart ;
					if ( activePart.getLabel () != null ) {
						displayNameText.setText 	( activePart.getLabel () ) ;
						displayNameText.setEnabled 	( true ) ;

						DisplayViewPart display =  (DisplayViewPart) activePart.getObject () ;
						application.getContext ().modify ( ISelectedPreferenceTarget.class, null ) ;
						application.getContext ().modify ( ISelectedPreferenceTarget.class, display ) ;
					}
				}
			} 
		}
	}
	@Inject @Optional
	private void setSelectedPreferenceTarget ( ISelectedPreferenceTarget selectedTarget ) {
		if ( viewer != null ) {
			setViewerInput () ;
			if ( selectedTarget != null ) {
				for ( TreeItem item : viewer.getTree ().getItems ()[0].getItems () ) {
					if ( item.getData () instanceof DisplayViewPart ) {
						if ( ((DisplayViewPart)item.getData ()).getName ().equals ( selectedTarget.getName () ) ) {
							viewer.getTree ().setSelection ( item ) ;
							viewer.refresh () ; 
							return ;
						}
					}
					for ( TreeItem leafItem : item.getItems () ) {
						if ( leafItem.getData () instanceof IWidget ) {
							if ( ((IWidget)leafItem.getData ()).getId ().equals ( selectedTarget.getId () ) ) {
								viewer.getTree ().setSelection ( leafItem ) ;
								viewer.refresh () ; 
								return ;
							}
						}
					}
				}
			}
		}
	}
	@Focus
	public void onFocus () {
		if ( viewer != null ) {
			viewer.getControl().setFocus () ;
		}
	}
	@Override
	public ITreeElement getParent () {
		return null ;
	}
	@Override
	public ITreeElement [] getChildren () {
		ITreeElement [] topElement = { Controler.getInstance () } ;
		return topElement ;
	}
	@Override
	public boolean hasChildren () {
		return true ;
	}
	@Override
	public String getName () {
		return "General Parameters" ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {		
	}
}