package gmsj.robotics.tracker.overview;

import java.util.ArrayList;

import gmsj.robotics.tracker.overview.preferencesviewer.Property;

public interface ISelectedPreferenceTarget {

	public String 			getName 		() ;
	public String 			getId 			() ;
	ArrayList < Property > 	getPreferences 	( boolean swohAll ) ;
}
