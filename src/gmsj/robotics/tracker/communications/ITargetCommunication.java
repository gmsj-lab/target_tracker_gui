package gmsj.robotics.tracker.communications;

public interface ITargetCommunication {

	String 		getName 		( ) ;
	boolean		send			( String command ) ;
	boolean		send			( byte[] command ) ;
	boolean		receive			( ) ;
	void 		addComListener	( CommListener listener ) ;
	void 		closePort		( ) ;
}
