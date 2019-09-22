package gmsj.robotics.tracker.widgets.swt;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.prefs.NumericPrefs;

public class NumericWidget extends TrackerWidget {

	public  static  String name = "Numeric" ;
	private 		NumericPrefs 	numericPrefs ;
	// Widget elements
	private 		Label 			attributeLabel ;
	private 		Text 			attributeTextValue ;
	private 		Label 			timeTagLabel ;
	private 		boolean 		isFloat  ;
	private 		boolean 		isSigned ;	
	private 		float 			minValue ;
	private 		float 			maxValue ;
	
	static public void definePreferencesCatalog () {
		NumericPrefs.definePreferencesCatalog ( name ) ;
	}
	
	public NumericWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public void createWidget ( Composite parent ) {	
		
		dispose () ;
		TrackerPreferences.defineCatalog ( getId () , getName () ) ;

		createComposite ( parent ) ;
		numericPrefs = ContextInjectionFactory.make ( NumericPrefs.class , context ) ; 
		numericPrefs.init ( this ) ;
		TrackerPreferences.restorePreferences ( getId () , context ) ;	
		createText () ;
		restoreAttributes () ;
	}
	public void createText () {
		if ( ( attributeLabel != null ) && ( ! attributeLabel.isDisposed () ) ) {
			attributeLabel.dispose () ;
			attributeTextValue.dispose () ;
			timeTagLabel.dispose () ;
		}
		attributeLabel = new Label( widgetComposite, numericPrefs.getStyle () ) ;
		
		attributeLabel.setText( numericPrefs.getLabel() ) ;

		attributeTextValue = new Text( widgetComposite, numericPrefs.getStyle ()  ) ;
		//attributeTextValue.setEnabled( attribute.isWrite() ) ;
		attributeTextValue.addListener(SWT.DefaultSelection, new Listener() {
		      public void handleEvent(Event e) {
		        if ( validateInput( attributeTextValue.getText() ) ) {
		        	attribute.setValue( attributeTextValue.getText() ) ; 
		        attributeTextValue.selectAll() ;
		        }
		      }
		    });
		timeTagLabel = new Label( widgetComposite, numericPrefs.getStyle ()  ) ;
		//timeTagLabel.setText( "Last update (Target time): " + attribute.getLastUpdateTimeTag() + "s" ) ;
		
			
		
		attributeTextValue.setLayoutData ( new GridData ( 
				( numericPrefs.getHorizontalAlign () ) ? SWT.FILL : SWT.CENTER , 
				( numericPrefs.getVerticalAlign () )   ? SWT.FILL : SWT.CENTER , true, true, 1, 1 ) ) ;

		widgetComposite.layout () ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		//System.out.println ( "NumericWidget::attributeChanged:" + attributeValue ) ;
		if ( ( attributeTextValue != null ) && ( ! attributeTextValue.isDisposed () ) ) {
			try {
				attributeTextValue.setText ( String.valueOf (event.getValue () ) ) ;
			} catch ( NumberFormatException e ) {
				e.printStackTrace () ;
			}
		}
	}
	@Inject @Optional
	public void setEnable ( @UIEventTopic (Tracker.TOPIC_DISPLAY_PERSPECTIVE) boolean displayPerspective ) {
		if ( ( attributeTextValue != null ) && ( ! attributeTextValue.isDisposed () ) ) {
			attributeTextValue.setEnabled ( displayPerspective ) ;
		}
	}
	private boolean validateInput ( String input ) {

		char[] chars = new char[ input.length () ] ;
		if ( input.length () < 1 )
			return false ;
		if ( ( input.length () > 1 ) && ( input.startsWith ("0") ) ) {
			attributeTextValue.setText( input.substring ( 1 ) ) ;				
		}
		input.getChars( 0, chars.length, chars, 0 ) ;

		for ( int i = 0; i < chars.length; i++ ) 
		{
			if ( ( ! ( '0' <= chars[i] && chars[i] <= '9' ) ) )
			{
				if ( ( isSigned ) && ( chars[i] == '-' ) ) 
				{
					// OK for signed types
				}
				else if ( ( isFloat ) && ( ( chars[i] == '.' ) || 
						( chars[i] == 'e' ) || ( chars[i] == 'E' ) ) ) 
				{
					// OK for float types
				}
				else
				{
					// Remove illegal input char from the widget
					attributeTextValue.setText( input.substring( 0, input.length() - 1 ) ) ;
					System.out.println("TextWidget: Illegal input character");
					return false ;
				}
			}
		}
		if( ( Float.valueOf( input ) > maxValue ) || 
			( Float.valueOf( input ) < minValue ) ) {
			// Input value out of bounds, clear the widget
			attributeTextValue.setText("") ;
			System.out.println("TextWidget: value out of bounds ["+ String.valueOf( minValue) + ":"+ String.valueOf( maxValue) + "]");
			return false ;
		}
		return true ; 
	} 
}
