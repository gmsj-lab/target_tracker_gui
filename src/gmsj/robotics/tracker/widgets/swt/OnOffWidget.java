package gmsj.robotics.tracker.widgets.swt;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.prefs.OnOffPrefs;

public class OnOffWidget extends TrackerWidget {

	public  static  String 		name 			= "On/Off Button" ;
	private 		Button 		button ;
	private 		OnOffPrefs 	onOffPrefs ;

	static public void definePreferencesCatalog () {
		OnOffPrefs.definePreferencesCatalog ( name ) ;
	}

	public OnOffWidget () {
		isWriteOnly  = true ;
		setBooleanTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public void createWidget ( Composite parent ) {
		
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;
		createComposite ( parent ) ;
		onOffPrefs = ContextInjectionFactory.make ( OnOffPrefs.class , context ) ; 
		onOffPrefs.init ( this ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	
		createButton () ;
		restoreAttributes () ;
	}
	public void createButton () {
		if ( ( button != null ) && ( ! button.isDisposed () ) ) {
			button.dispose () ;
		}
		button = new Button	 ( widgetComposite , onOffPrefs.getStyle () ) ;
		button.setLayoutData ( new GridData ( 
				( onOffPrefs.getHorizontalAlign () ) ? SWT.FILL : SWT.CENTER , 
				( onOffPrefs.getVerticalAlign () )   ? SWT.FILL : SWT.CENTER , true, true, 1, 1 ) ) ;
		button.setText ( onOffPrefs.getButtonText () ) ;
		button.setEnabled ( false ) ;
		button.addSelectionListener ( new SelectionListener () {
			@Override
			public void widgetSelected ( SelectionEvent e ) {
				if ( attribute != null ) {
					attribute.setValue ( String.valueOf ( ( button.getSelection () ) ? 1 : 0 ) ) ;
				}
			}
			@Override
			public void widgetDefaultSelected ( SelectionEvent e ) {
			}
		});

		widgetComposite.layout () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//System.out.println ( "OnOffWidget::attributeChanged:" + attributeValue + attributeValue.equals ( "0" ) + attributeValue.length () ) ;
		if ( ( button != null ) && ( ! button.isDisposed () ) ) {
			try {
				button.setSelection ( ( event.getValue() == 1 ) ) ;
			} catch ( NumberFormatException e ) {
				e.printStackTrace () ;
			}
		}
	}
	@Inject @Optional
	public void setEnable ( @UIEventTopic (Tracker.TOPIC_DISPLAY_PERSPECTIVE) boolean displayPerspective ) {
		if ( ( button != null ) && ( ! button.isDisposed () ) ) {
			button.setEnabled ( displayPerspective ) ;
		}
	}
}

