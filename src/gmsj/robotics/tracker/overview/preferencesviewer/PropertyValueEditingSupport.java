package gmsj.robotics.tracker.overview.preferencesviewer;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColorCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

import gmsj.robotics.tracker.controler.TrackerPreferences;

public class PropertyValueEditingSupport extends EditingSupport {

	private final 	TableViewer 			viewer ;
	private final 	CellEditor 				textEditor ;
	private final 	ComboBoxCellEditor 		booleanEditor ;
	private final 	ComboBoxCellEditor 		traceTypeEditor ;
	private final 	ComboBoxCellEditor 		pointStyleEditor ;
	private final 	ComboBoxCellEditor 		buttonStyleEditor ;
	private final 	ColorCellEditor 		colorEditor ;
	private final 	FontDialogCellEditor 	fontDialogEditor ;

	private String[] booleanChoices = new String [ 2 ] ;
	private String[] buttonStyleChoices = new String [ 3 ] ;

	public PropertyValueEditingSupport ( TableViewer viewer ) {
		super ( viewer ) ;
		booleanChoices [0] = "false" ;
		booleanChoices [1] = "true" ;

		buttonStyleChoices  [0] = "RADIO" ;
		buttonStyleChoices  [1] = "CHECK" ;
		buttonStyleChoices  [2] = "TOGGLE" ;

		this.viewer 			= viewer ;
		this.textEditor 		= new TextCellEditor 	 	( viewer.getTable () ) ;
		this.booleanEditor 		= new ComboBoxCellEditor 	( viewer.getTable () , booleanChoices 			  , SWT.READ_ONLY ) ;
		this.traceTypeEditor	= new ComboBoxCellEditor	( viewer.getTable () , TraceType.stringValues ()  , SWT.READ_ONLY ) ;
		this.pointStyleEditor	= new ComboBoxCellEditor	( viewer.getTable () , PointStyle.stringValues () , SWT.READ_ONLY ) ;
		this.buttonStyleEditor	= new ComboBoxCellEditor	( viewer.getTable () , buttonStyleChoices 		  , SWT.READ_ONLY ) ;
		this.colorEditor		= new ColorCellEditor	 	( viewer.getTable () ) ;
		this.fontDialogEditor	= new FontDialogCellEditor	( viewer.getTable () ) ;
	}

	@Override
	protected CellEditor getCellEditor ( Object element ) {
		Property property = (Property) element ;

		// By name
		if ( property.getName ().equals ( "buttonStyle" ) ) {
			return buttonStyleEditor ;
		}
		if ( property.getName ().equals ( "traceType" ) ) {
			return traceTypeEditor ;
		}
		else if ( property.getName ().equals ( "pointStyle" ) ) {
			return pointStyleEditor ;
		}
		// By type 
		else if ( property.getType ().equals ( "boolean" ) ) {
			return booleanEditor ;
		}
		else if ( property.getType ().equals ( "String" ) ) {
			return textEditor ;
		}
		else if ( property.getType ().equals ( "Color" ) ) {
			return colorEditor ;
		}
		else if ( property.getType ().equals ( "int" ) ) {
			return textEditor ;
		}
		else if ( property.getType ().equals ( "double" ) ) {
			return textEditor ;
		}
		else if ( property.getType ().equals ( "Font" ) ) {
			return fontDialogEditor ;
		}
		else {
			return textEditor ;
		}
	}

	@Override
	protected boolean canEdit ( Object element ) {
		return true ;
	}

	@Override
	protected Object getValue ( Object element ) {
		Property property = (Property) element ;

		// By Name
		if ( property.getName ().equals ( "traceType" ) ) {
			int i = 0 ;
			for ( String type : TraceType.stringValues () ) {
				if ( property.getStringValue ().equals ( type ) ) {
					return i ;
				}
				i ++ ;
			}
			return i ;
		}
		else if ( property.getName ().equals ( "pointStyle" ) ) {
			int i = 0 ;
			for ( String style : PointStyle.stringValues () ) {
				if ( property.getStringValue ().equals ( style ) ) {
					return i ;
				}
				i ++ ;
			}
			return i ;
		}
		else if ( property.getName ().equals ( "buttonStyle" ) ) {
			if ( property.getIntValue () == SWT.RADIO ) {
				return 0 ;
			}		
			else if ( property.getIntValue () == SWT.CHECK ) {
				return 1 ;
			}
			else if ( property.getIntValue () == SWT.TOGGLE ) {
				return 2 ;
			}
			else {
				System.out.println ( "PropertyValueEditingSupport: ERROR : invalid buttonStyle " + property.getIntValue () ) ;
				return 0 ;
			}
		}
		// By type 
		else if ( property.getType ().equals ( "boolean" ) ) {
			if ( property.getBooleanValue () ) {
				return 1 ;
			}
			else {
				return 0 ;
			}
		}
		else if ( property.getType ().equals ( "Color" ) ) {
			return property.getColorValue ().getRGB () ;
		}
		else {
			return property.getValue () ;
		}
	}
	@Override
	protected void setValue ( Object element, Object userInputValue ) {
		Property property = (Property) element ;

		// By name 
		if ( property.getName ().equals ( "traceType" ) ) {
			property.setValue ( TraceType.stringValues () [ ( int ) userInputValue ] ) ;
		}
		else if ( property.getName ().equals ( "pointStyle" ) ) {
			property.setValue ( PointStyle.stringValues () [ ( int ) userInputValue ] ) ;
		}
		else if ( property.getName ().equals ( "buttonStyle" ) ) {
			int style = SWT.TOGGLE ;
			if 		( buttonStyleChoices [ ( int ) userInputValue ].equals ( "RADIO" ) )	style = SWT.RADIO ;
			else if ( buttonStyleChoices [ ( int ) userInputValue ].equals ( "CHECK" ) )	style = SWT.CHECK ;

			property.setIntValue ( style ) ;
		}
		// By type 
		else if ( property.getType ().equals ( "boolean" ) ) {
			property.setBooleanValue ( ( (Integer) userInputValue == 0 ) ? false : true ) ;
		}
		else if ( property.getType ().equals ( "Color" ) ) {
			property.setColorValue ( new Color ( null , (RGB) userInputValue ) ) ;
		}
		else if ( property.getType ().equals ( "Font" ) ) {
			System.out.println ( "PropertyValueEditingSupport::setValue FONT" + userInputValue ) ;
			property.setFontValue ( (FontData) userInputValue ) ;
		}
		else {
			property.setValue ( (String) userInputValue ) ; 
		}

		property.setLocallyDefined ( true ) ;
		TrackerPreferences.storeValue 		  ( property ) ;
		TrackerPreferences.restorePreferences ( property ) ;
		viewer.update ( element , null ) ;
		viewer.refresh () ;
	}
} 
