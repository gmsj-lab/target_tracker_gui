package gmsj.robotics.tracker.widgets;

import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.navigation.ITreeElement;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;
import gmsj.robotics.tracker.parts.DisplayViewPart;

public interface IWidget extends ITreeElement , ISelectedPreferenceTarget {

	String 					getName 			() ;
	void 					dispose 			() ;
	void 					createWidget 		( Composite parent 			  ) ;
	void 					restorePreferences 	( IEclipsePreferences prefs   ) ;
	ArrayList < Property > 	getPreferences 		( boolean swohAll 			  ) ;
	void 					setId 				( String widgetId 			  ) ;
	String 					getId 				() ;
	boolean					isWriteOnly 		() ;
	void 					setDrag 			() ;
	void 					addMouseListener	( MouseListener mouseListener ) ;
	void 					removeMouseListener	( MouseListener mouseListener ) ;
	DisplayViewPart 		getDisplay 			() ;
	void 					removeAttribute 	( WidgetAttribute attribute ) ;
}

