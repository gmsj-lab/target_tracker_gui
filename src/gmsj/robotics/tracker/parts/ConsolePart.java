
package gmsj.robotics.tracker.parts;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.jface.dialogs.MessageDialog;

import gmsj.robotics.tracker.controler.Tracker;
import gmsj.robotics.tracker.controler.TrackerPreferences;
import gmsj.robotics.tracker.logging.ILogging;

public class ConsolePart implements ILogging {

	private Composite 			parent;
	private StyledText 			styledText;
	private StyleRange 			lineStyle ;
	private StyleRange 			timetagStyle ;
	private StyleRange 			sourceStyle ;
	private String 				outBuf 					= new String () ;
	private String 				errBuf 					= new String () ;
	private int 				byteCount 				= 0 ;
	final int 					stickToLastEntryPeriod 	= 100 ;
	private boolean			 	stickToLastEntry 		= true ;
	private boolean 			showTimeTag 			= false ;
	private long 				timeTag 				= 0 ;
	private long 				startTime ;
	private StringBuilder 		msg 					= new StringBuilder ( 1024 ) ;

	@Inject MDirtyable 			dirty ;
	@Inject IEclipseContext 	partContext ;	
	@Inject Shell 				shell ;
	@Inject MPart 				part ;

	@Inject @Optional
	private void setStickToLast ( @Named ( "stickToLastEntry" ) boolean stickToLastEntry ) {
		this.stickToLastEntry = stickToLastEntry ;
		System.out.println( "-----" ) ;
	}	
	@Inject @Optional
	public void setShowTimeTag ( @Named ( "showTimeTag" ) boolean showTimeTag ) {

		if ( showTimeTag ) {
			this.showTimeTag = showTimeTag ;
			System.out.println ( "-- Time Tagging ON --" ) ;
		}
		else {
			System.out.println ( "-- Time Tagging OFF --" ) ;
			this.showTimeTag = showTimeTag ;
		}	
	}
	@PostConstruct
	public void postConstruct ( MApplication application , Composite parent ) {
		this.parent = parent ;
		startTime 	= System.currentTimeMillis () ;
		dirty.setDirty ( false ) ;

		definePreferencesCatalog () ;
		TrackerPreferences.restorePreferences ( "Console" , partContext ) ;	

		// set ourself in the eclipse context as the logging service for all tracker objects
		application.getContext().set ( ILogging.class, this ) ;
		createControl () ;

		for ( MToolBarElement toolBarElement : part.getToolbar ().getChildren () ) {
			if ( toolBarElement.getElementId ().equals ( Tracker.TIMETAG_TOOL_ITEM_ID ) ) {
				if ( ((MHandledToolItem)toolBarElement).isSelected () ) {
					showTimeTag = true ;
				}
				else {
					showTimeTag = false ;
				}
			}
			if ( toolBarElement.getElementId ().equals ( Tracker.STICKTOLAST_TOOL_ITEM_ID ) ) {
				if ( ((MHandledToolItem)toolBarElement).isSelected () ) {
					stickToLastEntry = true ;
				}
				else {
					stickToLastEntry = false ;
				}
			}
		}
	}
	private void definePreferencesCatalog ()  {

		TrackerPreferences.createBooleanPrivatePreferences ( "Console" , "stickToLastEntry" , true  ) ;		
		TrackerPreferences.createBooleanPrivatePreferences ( "Console" , "showTimeTag" 	 	, false ) ;		 
	}
	private void createControl () {

		styledText = new StyledText ( parent, SWT.READ_ONLY | SWT.MULTI| SWT.H_SCROLL | SWT.V_SCROLL ) ;
		styledText.setBackground ( new Color ( null, 0, 0, 0 ) ) ;

		timetagStyle 			= new StyleRange () ;    
		timetagStyle.fontStyle 	= SWT.ITALIC ;
		timetagStyle.foreground = parent.getDisplay().getSystemColor( SWT.COLOR_DARK_GRAY ) ;

		sourceStyle 			= new StyleRange () ;    
		sourceStyle.fontStyle 	= SWT.BOLD ;
		sourceStyle.foreground 	= parent.getDisplay().getSystemColor ( SWT.COLOR_WHITE ) ;

		startStickToLastTimer () ;
		redirectStreams () ;	
	}
	@Override
	public void logFromTarget ( String source, int color, byte [] message , int length ) {
		try {
			msg.setLength ( 0 ) ;
			for ( int i = 0 ; i < length ; i ++ ) {
				if ( (char) message [ i ] == SWT.LF ) {
					msg.append ( ' ' ) ;
				}
				msg.append ( (char) message [ i ] ) ;
			}
			msg.append ( "\n" ) ;
			print ( source, msg.toString () , color , SWT.NONE ) ;
		} catch ( Exception e ) {
			e.printStackTrace () ;
		}
	}

	@Override
	public void logFromHost ( String source, int color , String message ) {
		print ( source, message, color , SWT.ITALIC ) ;
	}	
	private void print ( String source, String message, int color, int fontStyle ) {
		if ( ( styledText != null ) && ( ! styledText.isDisposed () ) ) {

			styledText.setLayoutDeferred ( true ) ;
			timeTag = System.currentTimeMillis () - startTime ;
			dirty.setDirty ( true ) ;

			// The time tag...
			if ( showTimeTag )  {

				String timeTagString     = formatTime ( timeTag ) ;
				styledText.append 		 ( timeTagString ) ;
				timetagStyle.start 		 = byteCount ;
				timetagStyle.length 	 = timeTagString.length () ;
				styledText.setStyleRange ( timetagStyle ) ;
				byteCount 				+= timeTagString.length () ;
			}
			// The source...
			styledText.append 		( source ) ;
			sourceStyle.start 		= byteCount ;
			sourceStyle.length 		= source.length () ;
			styledText.setStyleRange ( sourceStyle ) ;
			byteCount 				+= source.length () ;

			// The message...
			styledText.append 		( message ) ;
			lineStyle 				= new StyleRange () ;    
			lineStyle.start 		= byteCount ;
			lineStyle.length 		= message.length () ;
			lineStyle.foreground 	= parent.getDisplay().getSystemColor ( color ) ;
			lineStyle.fontStyle 	= fontStyle ;
			styledText.setStyleRange ( lineStyle ) ;
			byteCount 				+= message.length () ;
			styledText.setLayoutDeferred ( false ) ;
		}
	}
	private static String formatTime ( long time ) {
		long msec    = time % 1000 ;
		long seconds = time / 1000 ;
		long s		 = seconds % 60 ;
		long m		 = ( seconds / 60 ) % 60 ;
		long h		 = ( seconds / ( 3600 ) ) % 24 ;
		return String.format ( "%d:%02d:%02d.%03d: " , h , m , s , msec ) ;
	}
	private void startStickToLastTimer () {
		Runnable timer = new Runnable () {

			@Override
			public void run () {
				if ( ( ! parent.isDisposed () ) && ( ! styledText.isDisposed () ) ) {

					if ( (stickToLastEntry) && ( ( startTime + timeTag + stickToLastEntryPeriod ) > System.currentTimeMillis () ) ) {
						// Go at the bottom of the text if something new since last timeout
						styledText.setTopIndex ( styledText.getLineCount () - 1 ) ;
					}
					parent.getDisplay().timerExec ( stickToLastEntryPeriod , this ) ;
				}
			}
		};
		parent.getDisplay().timerExec ( stickToLastEntryPeriod, timer ) ;		
	}
	private void redirectStreams () {
		OutputStream out = new OutputStream () {
			@Override
			public void write ( int b ) throws IOException {

				outBuf += String.valueOf ( ( char ) b ) ;

				if ( outBuf.endsWith ( "\n" ) ) { //$NON-NLS-1$
					print ( "" , outBuf,  SWT.COLOR_GREEN, SWT.ITALIC ) ;
					outBuf = "" ; //$NON-NLS-1$
				}	
			}
		} ;
		final PrintStream oldOut = System.out ;
		System.setOut( new PrintStream ( out ) ) ;

		styledText.addDisposeListener ( new DisposeListener () {

			@Override
			public void widgetDisposed ( DisposeEvent e ) {
				System.setOut ( oldOut ) ;
			}
		} ) ;

		OutputStream err = new OutputStream () {
			@Override
			public void write ( int b ) throws IOException {

				errBuf += String.valueOf ( ( char ) b ) ;

				if ( errBuf.endsWith ( "\n" ) ) { //$NON-NLS-1$
					print ( "" , errBuf,  SWT.COLOR_DARK_YELLOW, SWT.BOLD ) ;
					errBuf = "" ; //$NON-NLS-1$
				}	
			}
		} ;

		final PrintStream oldErr = System.err ;
		System.setErr ( new PrintStream ( err ) ) ;

		styledText.addDisposeListener ( new DisposeListener() {

			@Override
			public void widgetDisposed ( DisposeEvent e ) {
				System.setErr ( oldErr ) ;
			}
		} ) ;		
	}
	@Focus
	public void onFocus () {
		styledText.setFocus () ;
	}
	@Persist
	public void save () {
		// TODO : save to file the target trace
		MessageDialog dialog = new MessageDialog ( shell , "Closing Console " , null , 
				"Do you wish to save trace ?" ,
				MessageDialog.QUESTION, new String [] { "Close", "Save" } , 0 ) ;

		if ( dialog.open () == 1 ) {
			System.out.println ( "SAVE" ) ; 

		}
		dirty.setDirty ( false ) ;
	}	
}