package gmsj.robotics.tracker.logging;

public interface ILogging {

	void logFromHost 	( String source , int color , String message ) ;
	void logFromTarget  ( String source , int color , byte [] buffer, int length ) ;
}
