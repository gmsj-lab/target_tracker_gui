package gmsj.robotics.tracker.controler;

public final class Tracker {

	public final static String ID		 							= "gmsj.robotics.tracker" ;
	
	public final static String SETUP_PESPECTIVE_ID 					= "gmsj.robotics.tracker.setupperspective" ;
	public final static String DISPLAY_PESPECTIVE_ID 				= "gmsj.robotics.tracker.displayperspective" ;
	
	public final static String DISPLAYVIEW_PARTSTACK_ID 			= "gmsj.robotics.tracker.setupperspective.displayviewspartstack" ;
	public final static String CONSOLE_PARTSTACK_ID 				= "gmsj.robotics.tracker.setupperspective.consolepartstack" ;
	
	public final static String DISPLAYVIEW_ID 						= "gmsj.robotics.tracker.displaypartdescriptor" ;
	
	public final static String CONSOLE_ID 							= "gmsj.robotics.tracker.setupperspective.consoleviewspartstack.consolepart" ;	
	public final static String DISPLAY_OVERVIEW_ID 					= "gmsj.robotics.tracker.setupperspective.displayoverviewpartstack.displayoverviewpart" ;
	public final static String TARGET_NAVIGATION_ID 				= "gmsj.robotics.tracker.setupperspective.targetnavigationpartstack.targetnavigationpart" ;	
	public final static String WIDGET_NAVIGATION_ID 				= "gmsj.robotics.tracker.setupperspective.widgetnavigationpartstack.widgetnavigationpart" ;
	public final static String WIDGET_PREVIEW_ID 					= "gmsj.robotics.tracker.setupperspective.widgetpreviewpartstack.widgetpreviewpart" ;
	public final static String WIDGET_PREFERENCES_ID 				= "gmsj.robotics.tracker.setupperspective.consoleviewspartstack.preferencespart" ;
	
	public final static String TRIMMED_WINDOW_ID 					= "gmsj.robotics.tracker.maintrimmedwindow" ;	
	
	public final static String CLOSE_TARGET_COMMAND_ID 				= "gmsj.robotics.tracker.command.closetarget" ;	
	public final static String CLOSE_TARGET_PARAM_ID 				= "gmsj.robotics.tracker.commandparameter.targetName" ;
	public final static String DOWNLOAD_TARGET_PARAM_ID 			= "gmsj.robotics.tracker.commandparameter.targetName" ;
	
	public final static String VERTICAL_TOOL_ITEM_ID 				= "gmsj.robotics.tracker.handledtoolitem.verticaldisplayorientation" ;	
	public final static String TIMETAG_TOOL_ITEM_ID 				= "gmsj.robotics.tracker.handledtoolitem.timetag" ;	
	public final static String STICKTOLAST_TOOL_ITEM_ID 			= "gmsj.robotics.tracker.handledtoolitem.sticktoend" ;	
		
	public final static String DELETE_POPUP_MENU		 			= "gmsj.robotics.tracker.popupmenu.overviewremovewidget" ;	
	public static final String TARGET_POPUP_MENU 					= "gmsj.robotics.tracker.popupmenu.targetmenu";
	
	// Event topics	
	public static final String TOPIC_DOUBLECLICK_WIDGET 			= "TOPIC_TRACKER/DOUBLECLICK_WIDGET" ;
	public static final String TOPIC_LEFTCLICK_WIDGET	 			= "TOPIC_TRACKER/LEFTCLICK_WIDGET" ;
	
	public final static String TOPIC_DISPLAY_PERSPECTIVE 			= "TOPIC_TRACKER/DISPLAY_PERSPECTIVE" ;
	
	public final static String TOPIC_SEND_TO_TARGET				 	= "TOPIC_TRACKER/SEND_TO_TARGET" ;
	public final static String TOPIC_RECEIVED_CHANGE_FROM_TARGET 	= "TOPIC_TRACKER/RECEIVED_CHANGE_FROM_TARGET" ;
	public final static String TOPIC_RECEIVED_UPDATE_FROM_TARGET 	= "TOPIC_TRACKER/RECEIVED_UPDATE_FROM_TARGET" ;
}
