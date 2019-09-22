package gmsj.robotics.tracker.events;

import gmsj.robotics.tracker.widgets.IWidget;

public class SelectedWidget {
	
	private IWidget widget ;

	public SelectedWidget ( IWidget widget ) {
		
		this.widget = widget ;
	}
	
	public IWidget get () {
		
		return widget ;
	}
}
