package gmsj.robotics.tracker.widgets;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;

import gmsj.robotics.tracker.navigation.ITreeElement;

public abstract class WidgetCategory implements ITreeElement {
	
	ITreeElement 						parent ;
	@Inject protected IEclipseContext 	context ;
	
	@Override
	public boolean hasChildren () {
		return true ;
	}
	@Override
	public ITreeElement getParent () {
		return parent ;
	}
	@Override
	public void setParent ( ITreeElement parent ) {
		this.parent = parent ;
	}
}
