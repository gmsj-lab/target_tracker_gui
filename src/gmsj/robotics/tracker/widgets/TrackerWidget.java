package gmsj.robotics.tracker.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.controler.Drag;
import gmsj.robotics.tracker.controler.Drop;
import gmsj.robotics.tracker.controler.IDragSource;
import gmsj.robotics.tracker.controler.IDropTarget;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.model.DataType;
import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.parts.DisplayViewPart;
import gmsj.robotics.tracker.widgets.WidgetAttribute.IAttributeListener;

public abstract class TrackerWidget implements IWidget , IDragSource , IDropTarget , IAttributeListener {

	private   			String 								id ;
	private   			Composite 							parent ; 
	private   			Drag								drag 					= new Drag () ;
	private   			Drop								drop 					= new Drop () ;
	private				DisplayViewPart 					draggedFromdisplay ;
	protected 			Control 							widgetControl 			= null ;
	protected 			Composite 							widgetComposite ;
	protected 			ITreeElement 						treeParent ;
	protected 			boolean								isWriteOnly  			= false ;
	protected 			Map < String, WidgetAttribute >		attributes 				= new HashMap < String, WidgetAttribute > () ;
	protected 			WidgetAttribute 					attribute ;
	protected 			ArrayList < DataType >				allowedAttributeTypes 	= new ArrayList < DataType > () ;
	protected			IEclipseContext 					parentContext ;
	protected			IEclipseContext 					context ;
	private 			WidgetAttribute 					selectedAttribute ;

	@Inject 			MApplication 						application ;

	@Inject
	private void setContext ( IEclipseContext parentContext ) {
		this.parentContext = parentContext ;
		if ( context != null ) context.dispose () ;

		context = EclipseContextFactory.create () ;

		context.setParent ( parentContext ) ;
	}
	@Optional @Inject
	private void dndOfAttributeInProgress ( WidgetAttribute selectedAttribute ) {
		this.selectedAttribute = selectedAttribute ;
		if ( widgetControl != null ) {
			if ( selectedAttribute != null ) {
				drop.setDrop ( this ) ;	
			}
			else {
				drop.unsetDrop () ;					
			}
		}
	}
	@Override
	public ITreeElement getParent () {
		return treeParent ;
	}
	@Override
	public ITreeElement[] getChildren () {
		ITreeElement [] children = new ITreeElement [0] ;
		children = ( ITreeElement [] ) attributes.values ().toArray ( new  ITreeElement [0] ) ;

		return children ;
	}
	@Override
	public boolean hasChildren () {
		return ! attributes.isEmpty () ;
	}
	@Override
	public void dispose () {	
		drag.unsetDrag () ;
		if ( widgetControl != null ) {
			widgetControl.dispose () ;
			widgetControl = null ;
		}
		attribute = null ;
		if ( attributes != null ) {
			for ( WidgetAttribute att : attributes.values () ) {
				att.unRegisterForAttribute () ;
			}
			attributes.clear () ;
		}
	}
	@Override
	public void setParent ( ITreeElement parent ) {
		this.treeParent	= parent ;
	}
	@Override
	public boolean isWriteOnly () {
		return isWriteOnly ;
	}
	protected boolean addAttribute ( WidgetAttribute targetAttribute ) {
		if ( isAllowed ( targetAttribute ) ) {

			if ( attributes.size () > 0 ) {
				for ( WidgetAttribute attribute : attributes.values () ) {
					attribute.unRegisterForAttribute () ;
				}
			}
			attributes.clear () ;

			targetAttribute.setParent ( this ) ;
			targetAttribute.registerForAttribute ( this , true ) ;
			this.attribute = targetAttribute ;

			attributes.put ( targetAttribute.getId () , targetAttribute ) ;

			application.getContext ().modify ( ISelectedPreferenceTarget.class, this ) ;				

			TrackerPreferences.storeAttributes ( id , attributes ) ;
			return true ;
		}
		return false ;
	}
	public void removeAttribute ( WidgetAttribute targetAttribute ) {
		targetAttribute.unRegisterForAttribute () ;
		attributes.remove ( targetAttribute.getId () ) ;
		attribute = null ;
		TrackerPreferences.storeAttributes ( id , attributes ) ;
	}
	protected void restoreAttributes () {
		for ( String attributeString : TrackerPreferences.getTargetAttributes ( id ) ) {	
			addAttribute ( new WidgetAttribute ( attributeString ) ) ;
		}
	}
	@Override
	public void setId ( String id ) {
		this.id = id ; 
	}
	@Override
	public String getId () {
		return id ;
	}	
	protected void createComposite ( Composite parent ) {
		this.parent = parent ;

		widgetComposite = new Composite ( parent, SWT.NONE ) ;
		widgetComposite.setLayout ( new GridLayout () ) ;

		widgetControl = widgetComposite ;
		setMouseListener ( this ) ;
	}
	protected void createCanvas ( Composite parent , IFigure figure ) {
		this.parent = parent ;

		FormData formData 	= new FormData			() ;
		formData.top 		= new FormAttachment	( 	0, 0 ) ;
		formData.left 		= new FormAttachment	( 	0, 0 ) ;
		formData.bottom 	= new FormAttachment	( 100, 0 ) ;
		formData.right 		= new FormAttachment	( 100, 0 ) ;

		Canvas canvas = new Canvas  ( parent , SWT.NONE ) ;
		canvas.setLayoutData ( formData ) ;

		//use LightweightSystem to create the bridge between SWT and draw2D
		LightweightSystem lws = new LightweightSystem ( canvas ) ;	
		lws.setContents ( figure ) ;

		widgetControl = canvas ;
		setMouseListener ( this ) ;

		context.setParent ( null ) ; 
		context.setParent ( parentContext ) ;
	}
	private void setMouseListener ( final IWidget widget ) {
		widgetControl.addMouseListener ( new MouseListener () {
			@Override
			public void mouseDoubleClick ( MouseEvent e ) {				
			}
			@Override
			public void mouseDown ( MouseEvent e ) {
				application.getContext ().modify ( ISelectedPreferenceTarget.class, widget ) ;				
			}
			@Override
			public void mouseUp ( MouseEvent e ) {				
			}
		}) ;
	}
	public void setDrag () {
		if ( ( widgetControl != null ) && ( ! Controler.getInstance ().isDisplayPerspectiveOn ) ) {
			drag.setDrag ( this ) ;					
		}
	}
	public void unsetDrag () {
		drag.unsetDrag () ;		
	}
	@Inject @Optional
	public void toggleDrag ( @UIEventTopic (Tracker.TOPIC_DISPLAY_PERSPECTIVE) boolean displayPerspective ) {
		if ( displayPerspective ) {
			unsetDrag () ;
		}
		else {
			setDrag () ;
		}
	}
	protected boolean isAllowed ( WidgetAttribute targetAttribute ) {

		if ( isWriteOnly () && ( ! targetAttribute.isWrite () ) ) {
			return false ;
		}

		if ( allowedAttributeTypes.contains ( targetAttribute.getType () ) ) {
			return true ;
		}
		return false ;
	}
	protected void setAllAttributeTypesAllowed () {
		allowedAttributeTypes.add ( DataType.BOOLEAN  ) ;
		allowedAttributeTypes.add ( DataType.UINT_8   ) ;
		allowedAttributeTypes.add ( DataType.INT_8 	  ) ;
		allowedAttributeTypes.add ( DataType.UINT_10  ) ;
		allowedAttributeTypes.add ( DataType.UINT_16  ) ;
		allowedAttributeTypes.add ( DataType.INT_16   ) ;
		allowedAttributeTypes.add ( DataType.UINT_32  ) ;
		allowedAttributeTypes.add ( DataType.INT_32   ) ;
		allowedAttributeTypes.add ( DataType.FLOAT_32 ) ;
	}
	protected void setUnsignedTypesAllowed () {
		allowedAttributeTypes.add ( DataType.BOOLEAN  ) ;
		allowedAttributeTypes.add ( DataType.UINT_8   ) ;
		allowedAttributeTypes.add ( DataType.UINT_10  ) ;
		allowedAttributeTypes.add ( DataType.UINT_16  ) ;
		allowedAttributeTypes.add ( DataType.UINT_32  ) ;
	}
	protected void setBooleanTypesAllowed ( ) {
		allowedAttributeTypes.add ( DataType.BOOLEAN  ) ;		
	}
	public void restorePreferences ( IEclipsePreferences prefs ) {
	}
	@Override
	public Control getDnDControl () {
		return widgetControl ;
	}
	@Override
	public boolean isDragOk () {
		return true ;
	}
	@Override
	public String getDraggedElement () {
		draggedFromdisplay = (DisplayViewPart) parent.getData () ;
		return id ;
	}
	@Override
	public void dragFinishedSuccessfully () {
		draggedFromdisplay.removeWidget ( this ) ;
	}
	@Override
	public void dragAborted () {
		//System.out.println ( "TrackerWidget::dragAborted" ) ;
	}
	@Override
	public void receiveDrop ( String dropString ) {
		//System.out.println ( "TrackerWidget::receiveDrop:"  + dropString ) ;
		addAttribute ( WidgetAttribute.valueOf ( dropString ) ) ;
	}
	@Override
	public boolean isDropOk () {
		// TODO : find which widget is about to be dragged
		return isAllowed ( selectedAttribute ) ;
	}
	@Override
	public void addMouseListener ( MouseListener mouseListener ) {
		widgetControl.addMouseListener ( mouseListener ) ;
	}
	@Override
	public void removeMouseListener ( MouseListener mouseListener ) {
		widgetControl.removeMouseListener ( mouseListener ) ;
	}
	@Override
	public ArrayList < Property > getPreferences ( boolean showAll ) {
		return TrackerPreferences.getPreferences ( this.getId () , context , showAll ) ;
	}
	@Override
	public DisplayViewPart getDisplay () {
		DisplayViewPart display = null ;
		if ( ( parent != null ) && ( parent.isDisposed () == false ) ) {
			display = ( DisplayViewPart ) parent.getData () ;
		}
		return display ;
	}
}
