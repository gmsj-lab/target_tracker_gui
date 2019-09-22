package gmsj.robotics.tracker.navigation;

public abstract interface ITreeElement {
	
	public ITreeElement 	getParent 	() ;
	public ITreeElement[]	getChildren () ;
	public boolean			hasChildren () ;
	public String			getName 	() ;
	public void 			setParent	( ITreeElement parent ) ;
}
