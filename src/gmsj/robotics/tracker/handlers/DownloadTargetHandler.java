 
package gmsj.robotics.tracker.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.model.Model;
import gmsj.robotics.tracker.model.OpenDownloadFilePopup;
import gmsj.robotics.tracker.parts.TargetNavigationPart;

public class DownloadTargetHandler {
	@Execute
	public void execute ( @Optional @Named ( Tracker.DOWNLOAD_TARGET_PARAM_ID ) String targetName , Model model , IEclipseContext context ) {
		if ( ( targetName == null ) || ( targetName.contains ( "selection" ) ) ) {
			if (TargetNavigationPart.getSelectedTarget () != null ) {
				targetName = TargetNavigationPart.getSelectedTarget ().getName () ;		
			}
		}
		if ( targetName != null ) {
			OpenDownloadFilePopup dialog = ContextInjectionFactory.make ( OpenDownloadFilePopup.class , context ) ;
			String selectedTargetName = (String) dialog.open ( model , targetName ) ;	
	
			if ( selectedTargetName != null ) {
				model.closeTarget ( selectedTargetName ) ;
			}
		}
	}	
}