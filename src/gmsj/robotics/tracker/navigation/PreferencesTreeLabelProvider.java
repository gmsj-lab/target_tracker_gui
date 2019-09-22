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

import gmsj.robotics.tracker.controler.Controler;
import gmsj.robotics.tracker.parts.DisplayOverviewPart;
import gmsj.robotics.tracker.parts.DisplayViewPart;
import gmsj.robotics.tracker.widgets.IWidget;
import gmsj.robotics.tracker.widgets.nebula.GraphAttribute;
import gmsj.robotics.tracker.widgets.nebula.GraphWidget;

public class PreferencesTreeLabelProvider extends LabelProvider implements IStyledLabelProvider {

		private ImageDescriptor globalImage ;
		private ImageDescriptor displayImage ;
		private ImageDescriptor widgetImage ;
		private ImageDescriptor attributeImage ;
		private ResourceManager resourceManager ;

		public PreferencesTreeLabelProvider () {
			this.globalImage 	= createImageDescriptor ( "icons/global.png"  ) ;
			this.displayImage 	= createImageDescriptor ( "icons/display.png" ) ;
			this.widgetImage 	= createImageDescriptor ( "icons/widget.png"  ) ;
			this.attributeImage = createImageDescriptor ( "icons/readAttribute.png"  ) ;
		}
		@Override
		public StyledString getStyledText ( Object element ) {

			if ( element instanceof ITreeElement ) {

				Integer size = 0 ;
				ITreeElement treeElement = (ITreeElement) element ;
				StyledString styledString = new StyledString ( treeElement.getName () ) ;

				ITreeElement[] treeElements = null ;
				
				if (   ( element instanceof DisplayViewPart ) 
					|| ( element instanceof Controler ) 
					|| ( element instanceof GraphWidget ) 
					|| ( element instanceof DisplayOverviewPart ) ) {
					treeElements = treeElement.getChildren () ;
				}
				if ( treeElements != null ) {
					size = treeElements.length ;
				}

				if ( size > 0 ) {
					styledString.append (" (" + size + ") ", StyledString.COUNTER_STYLER ) ;
				}
				return styledString ;
			}
			return null ;
		}  
		@Override
		public Image getImage ( Object element ) {

			if ( element instanceof ITreeElement ) { 	  
				if ( element instanceof Controler ) {
					return getResourceManager().createImage ( globalImage ) ;
				}
				else if ( element instanceof DisplayViewPart ) {
					return getResourceManager().createImage ( displayImage ) ;
				}
				else if ( element instanceof IWidget ) {
					return getResourceManager().createImage ( widgetImage ) ;
				}
				else if ( element instanceof GraphAttribute ) {
					return getResourceManager().createImage ( attributeImage ) ;
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
				resourceManager = new LocalResourceManager ( JFaceResources.getResources () ) ;
			}
			return resourceManager ;
		}
		private ImageDescriptor createImageDescriptor ( String image ) {

			Bundle bundle = FrameworkUtil.getBundle ( PreferencesTreeLabelProvider.class ) ;
			URL url = FileLocator.find ( bundle, new Path ( image ) , null ) ;
			return ImageDescriptor.createFromURL ( url ) ;
		}
	}
