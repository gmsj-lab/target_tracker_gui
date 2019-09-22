package gmsj.robotics.tracker.navigation;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import gmsj.robotics.tracker.model.Target;
import gmsj.robotics.tracker.model.TargetAttribute;
import gmsj.robotics.tracker.model.TargetAttributeGroup;
import gmsj.robotics.tracker.model.TargetSubAttribute;

public class AttributeLabelProvider extends LabelProvider implements IStyledLabelProvider {

	private ImageDescriptor serialTargetImage ;
	private ImageDescriptor bluetoothTargetImage ;
	private ImageDescriptor wifiTargetImage ;
	private ImageDescriptor readAttributeImage ;
	private ImageDescriptor readWriteAttributeImage ;
	private ImageDescriptor attributeGroupImage ;
	private ResourceManager resourceManager ;

	public AttributeLabelProvider () {
		this.wifiTargetImage 			= createImageDescriptor ( "icons/wifi2.png" 				) ;
		this.serialTargetImage 			= createImageDescriptor ( "icons/usbSymbol.png" 			) ;
		this.bluetoothTargetImage 		= createImageDescriptor ( "icons/bluetooth.png" 			) ;
		this.readAttributeImage 		= createImageDescriptor ( "icons/readattribute.png" 		) ;
		this.readWriteAttributeImage 	= createImageDescriptor ( "icons/readwriteattribute.png" 	) ;
		this.attributeGroupImage 		= createImageDescriptor ( "icons/attributegroup.png" 		) ;
	}
	@Override
	public StyledString getStyledText ( Object element ) {

		if ( element instanceof ITreeElement ) {

			ITreeElement treeElement = (ITreeElement) element ;
			StyledString styledString = new StyledString ( treeElement.getName() ) ;

			ITreeElement[] treeElements = treeElement.getChildren() ;
			Integer size = 0 ;
			
			if ( treeElements != null ) {
				size = treeElements.length ;
			}
			if ( element instanceof Target ) {
				size = ((Target)element).getNumberOfAttributes() ;
			}

			if ( size > 0 ) {
				styledString.append(" (" + size + ") ", StyledString.COUNTER_STYLER ) ;
			}
			return styledString;
		}
		return null;
	}  
	@Override
	public Image getImage ( Object element ) {

		if ( element instanceof ITreeElement ) { 	  
			if ( element instanceof Target ) {
				if ( ((Target) element ).getComm().getName().contains("SERIAL") ) {
					return getResourceManager().createImage ( serialTargetImage ) ;
				}
				else if ( ((Target) element ).getComm().getName().contains("UDP") ) {
					return getResourceManager().createImage ( wifiTargetImage ) ;
				}
				else if ( ((Target) element ).getComm().getName().contains("TCP") ) {
					return getResourceManager().createImage ( wifiTargetImage ) ;
				}				else {
					return getResourceManager().createImage ( bluetoothTargetImage ) ;
				}
			}
			else if ( element instanceof TargetAttributeGroup ) {
				return getResourceManager().createImage ( attributeGroupImage ) ;
			}
			else if ( ( element instanceof TargetAttribute ) || ( element instanceof TargetSubAttribute ) ) {
				if ( ( (TargetAttribute)element).isWrite() ) {
					return getResourceManager().createImage ( readWriteAttributeImage ) ;
				}
				else {
					return getResourceManager().createImage ( readAttributeImage ) ;
				}
			}
		}
		return super.getImage ( element ) ;
	}
	@Override
	public void dispose () {
		// garbage collect system resources
		if ( resourceManager != null ) {
			resourceManager.dispose() ;
			resourceManager = null ;
		}
	}
	protected ResourceManager getResourceManager () {
		if ( resourceManager == null ) {
			resourceManager = new LocalResourceManager ( JFaceResources.getResources() ) ;
		}
		return resourceManager ;
	}
	private ImageDescriptor createImageDescriptor ( String image ) {

		Bundle bundle = FrameworkUtil.getBundle ( WidgetLabelProvider.class ) ;
		URL url = FileLocator.find ( bundle, new Path ( image ) , null ) ;
		return ImageDescriptor.createFromURL ( url ) ;
	}
}