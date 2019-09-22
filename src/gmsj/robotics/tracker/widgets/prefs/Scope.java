package gmsj.robotics.tracker.widgets.prefs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import java.io.File;

import org.eclipse.draw2d.GridData;
import org.eclipse.nebula.widgets.oscilloscope.multichannel.Oscilloscope;
import org.eclipse.nebula.widgets.oscilloscope.multichannel.OscilloscopeDispatcher;
import gmsj.robotics.tracker.widgets.WidgetAttribute;

public class Scope {

	private Oscilloscope 			oscilloscope ;
	private OscilloscopeDispatcher 	scopeDispatcher ;
	private double 					zoom ;

	public Scope ( Composite parent, final WidgetAttribute targetAttribute ) {
		final double SCOPE_RANGE 	= 200 ;
		final int MIN_SCOPE_VALUE 	= 100 ;
	
		
		scopeDispatcher = new OscilloscopeDispatcher () {

			double minimumValue = targetAttribute.getType ().getMinimumValue () ;
			double range = targetAttribute.getType ().getMaximumValue () - minimumValue ;
			
			@Override
			public void hookSetValues ( int channel ) {
				
				
				String valueTODO = "" ;   //TODO !!!!!!!!!!!!!!!!!!!!!!!!!!! was targetAttribute.getValue()
				
				
				int value = (int) ( ( ( Integer.parseInt ( valueTODO ) - minimumValue ) / range ) * SCOPE_RANGE - MIN_SCOPE_VALUE ) ;
				value = (int)( value * zoom ) ;

				oscilloscope.setValue ( 0, value ) ;
			}
			@Override
			public int getDelayLoop () {
				return 50 ; 
			}
			@Override
			public int getPulse () {
				return 1 ;
			}
//			public int getProgression (){
//				return 5 ;
//			}
			@Override
			public boolean getFade () {
				return false;
			}

			@Override
			public int getTailSize () {
				return Oscilloscope.TAILSIZE_MAX;
			}
			@Override
			public File getActiveSoundfile () {
				return null ; 
			};
			@Override
			public Oscilloscope getOscilloscope () {
				return oscilloscope ;
			}
			@Override
			public void hookChangeAttributes () {
				super.hookChangeAttributes () ;
				getOscilloscope ().setSteady ( 0 , true , Oscilloscope.STEADYPOSITION_75PERCENT ) ;
				getOscilloscope ().setLineWidth ( 0, 2 ) ;

			}
		};
		
		oscilloscope = new Oscilloscope ( 2 , scopeDispatcher, parent, SWT.NONE ) ;
		oscilloscope.setLayoutData 		( new GridData ( SWT.FILL, SWT.FILL, true, true ) ) ;
		oscilloscope.setPercentage		( 0 , true ) ;
		oscilloscope.setAntialias		( 0 , true ) ;
		oscilloscope.setLineWidth		( 0 , 2 ) ;
		oscilloscope.setSteady			( 0 , true , Oscilloscope.STEADYPOSITION_75PERCENT ) ;	

		scopeDispatcher.dispatch () ;	
	}
	public void setZoom ( double zoom ) {
		this.zoom = zoom ;
	}
	public void dispose() {
		scopeDispatcher.stop() ;
		oscilloscope.dispose() ;
	}
	public void setForeground ( Color color ) {
		oscilloscope.setForeground ( 0 , color ) ;
		//oscilloscope.setForeground ( color ) ;		
	}
	public void setBackground ( Color color ) {
		oscilloscope.setBackground ( color ) ;		
	}
}
