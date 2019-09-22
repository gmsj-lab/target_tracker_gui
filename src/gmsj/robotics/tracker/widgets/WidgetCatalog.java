package gmsj.robotics.tracker.widgets;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class WidgetCatalog extends WidgetCategory {
	
	private static  String 			name 		 = "Widgets" ;
	private 		ITreeElement 	readWriteWidgets ;
	private 		ITreeElement 	readOnlyWidgets ;
	private 		ITreeElement 	writeOnlyWidgets ;
	private 		ITreeElement 	booleanWidgets ;
	private 		ITreeElement 	unsignedWidgets ;	
	private 		ITreeElement[] 	widgetTypes ;
	@PostConstruct	
	public void postConstruct () {

		booleanWidgets 	 = ContextInjectionFactory.make ( BooleanWidgets.class   , context ) ; booleanWidgets.setParent   ( this ) ;
		unsignedWidgets  = ContextInjectionFactory.make ( UnsignedWidgets.class  , context ) ; unsignedWidgets.setParent  ( this ) ;			
		readWriteWidgets = ContextInjectionFactory.make ( ReadWriteWidgets.class , context ) ; readWriteWidgets.setParent ( this ) ;
		readOnlyWidgets  = ContextInjectionFactory.make ( ReadOnlyWidgets.class  , context ) ; readOnlyWidgets.setParent  ( this ) ;
		writeOnlyWidgets = ContextInjectionFactory.make ( WriteOnlyWidgets.class , context ) ; writeOnlyWidgets.setParent ( this ) ;
		
		widgetTypes = new ITreeElement [] { readWriteWidgets , readOnlyWidgets , writeOnlyWidgets , booleanWidgets , unsignedWidgets } ;
	}
	@Override
	public ITreeElement getParent () {
		return null ;
	}
	@Override
	public ITreeElement[] getChildren () {
		return widgetTypes ;
	}
	@Override
	public String getName () {
		return name;
	}
}
