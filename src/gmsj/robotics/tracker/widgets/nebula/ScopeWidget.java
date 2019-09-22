package gmsj.robotics.tracker.widgets.nebula;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import gmsj.robotics.tracker.controler.AttributeEvent;
import gmsj.robotics.tracker.widgets.TrackerWidget;
import gmsj.robotics.tracker.widgets.WidgetAttribute;
import gmsj.robotics.tracker.widgets.prefs.Scope;

public class ScopeWidget extends TrackerWidget {

	public  static  String 		name 		 = "Oscilloscope" ;
	private 		Composite 	widgetComposite ;
	private 		Scope 		scope ;
	private 		Spinner 	zoomScale ;
	private 		Group 		attributeGroup ;
	private 		Label 		attributeLabel ;
	private 		Text 		attributeTextValue ;
//	private 		float 		maxValue ;
//	private 		float 		minValue ;	

	public ScopeWidget () {
		setAllAttributeTypesAllowed () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	@Override
	public void createWidget ( Composite parent ) {
		widgetComposite = new Composite ( parent, SWT.NONE ) ;
		widgetComposite.setLayout ( new GridLayout ( 3 , false ) ) ;
		
		widgetControl = widgetComposite ;
		
		restoreAttributes () ;
	}
	@Override
	public boolean addAttribute ( WidgetAttribute targetAttribute ) {

		// Creer l'oscilloscope
		scope = new Scope ( widgetComposite , targetAttribute ) ;	

		// create the attribute Group
		attributeGroup = new Group 	 ( widgetComposite, SWT.NONE ) ;
		attributeGroup.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, false, false, 3, 1 ) ) ;		
		attributeGroup.setLayout 	 ( new GridLayout ( 3 , false ) ) ;

		// create the new target Attribute label
		attributeLabel = new Label	 ( attributeGroup, SWT.RIGHT ) ;
		attributeLabel.setText		 ( targetAttribute.getName () ) ;
		attributeLabel.setLayoutData ( new GridData ( SWT.LEFT, SWT.TOP, false, true, 1, 1 ) ) ;
		attributeLabel.setFont 		 ( JFaceResources.getHeaderFont() ) ;	


		String valueTODO = "" ;   //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!! was targetAttribute.getValue()

		
		// create the new target Attribute text
		attributeTextValue = new Text	 ( attributeGroup, SWT.RIGHT ) ;
		attributeTextValue.setLayoutData ( new GridData ( SWT.FILL, SWT.FILL, true, true, 1, 1 ) ) ;
		attributeTextValue.setText		 ( valueTODO ) ;
		attributeTextValue.setFont		 ( JFaceResources.getHeaderFont () ) ;

		// create the new target Attribute Spinner
		zoomScale = new Spinner 	( attributeGroup, SWT.BORDER ) ;
		zoomScale.setLayoutData		( new GridData( SWT.FILL, SWT.CENTER, false, true, 1, 1 ) ) ;
		zoomScale.setDigits			( 2 ) ;
		zoomScale.setMinimum		( 1 ) ;
		zoomScale.setMaximum		( 2000 ) ;
		zoomScale.setSelection		( 100 ) ;
		zoomScale.setBounds			( 0, 0, 10, 70) ;
		zoomScale.setIncrement		( 10 ) ;
		zoomScale.setPageIncrement	( 100 ) ;

		scope.setZoom ( zoomScale.getSelection() / Math.pow ( 10, zoomScale.getDigits() ) ) ;

		zoomScale.addListener (SWT.Selection, new Listener() {
			@Override
			public void handleEvent ( Event event ) {
				scope.setZoom ( zoomScale.getSelection() / Math.pow ( 10, zoomScale.getDigits() ) ) ;
			}
		});

		// Make it read only if needed
		attributeTextValue.setEnabled ( targetAttribute.isWrite() ) ;

		// Store the maximum value for user input checking
//		maxValue = targetAttribute.getType().getMaximumValue () ;
//		minValue = targetAttribute.getType().getMinimumValue () ;

		// Make it take the necessary space
		zoomScale.pack () ;
		attributeTextValue.pack () ;
		attributeGroup.pack () ;	
		widgetComposite.pack ();
		
		return true ;
	}
	@Override
	public void attributeChanged ( AttributeEvent event ) {
		attribute.setValue ( event.getValueString() ) ;
	}
}
