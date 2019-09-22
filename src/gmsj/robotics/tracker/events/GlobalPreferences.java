package gmsj.robotics.tracker.events;

import java.util.ArrayList;

import org.eclipse.e4.core.contexts.IEclipseContext;

import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.overview.ISelectedPreferenceTarget;
import gmsj.robotics.tracker.overview.preferencesviewer.Property;

public class GlobalPreferences implements ISelectedPreferenceTarget {
	public  static 	String 			name = "All Preferences" ;
	private static 	IEclipseContext context ;

	public GlobalPreferences ( IEclipseContext context ) {
		GlobalPreferences.context = context ;
	}
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public String getId () {
		return "All" ;
	}
	@Override
	public ArrayList < Property > getPreferences ( boolean showAll ) {
		return TrackerPreferences.getPreferences ( name , context , showAll ) ;
	}
}
