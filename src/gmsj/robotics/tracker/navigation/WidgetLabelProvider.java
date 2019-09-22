package gmsj.robotics.tracker.navigation;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class WidgetLabelProvider extends LabelProvider implements IStyledLabelProvider {
    
    private ImageDescriptor folderImage ;
    private ImageDescriptor widgetImage ;
    private ResourceManager resourceManager ;

    public WidgetLabelProvider () {
        this.folderImage = createImageDescriptor ( "icons/package10.png" ) ;
        this.widgetImage = createImageDescriptor ( "icons/widget.png" ) ;
    }

    @Override
    public StyledString getStyledText ( Object element ) {
    	
      if ( element instanceof ITreeElement ) {
    	  
    	ITreeElement treeElement = (ITreeElement) element ;
        StyledString styledString = new StyledString ( treeElement.getName() ) ;
        ITreeElement[] treeElements = treeElement.getChildren () ;
        
        if ( ( treeElements != null ) &&  ( treeElements.length > 0 ) ) {
          styledString.append (" (" + treeElements.length + ") ", StyledString.COUNTER_STYLER ) ;
        }
        return styledString;
      }
      return null;
    }
    
    @Override
    public Image getImage ( Object element ) {
    	
      if ( element instanceof ITreeElement ) {
    	  
        if ( ( ( ITreeElement ) element).hasChildren() ) {
          return getResourceManager().createImage ( folderImage ) ;
        }
        else {
        	return getResourceManager().createImage ( widgetImage ) ;
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
