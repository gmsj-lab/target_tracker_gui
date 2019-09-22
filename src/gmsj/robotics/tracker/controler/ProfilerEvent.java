package gmsj.robotics.tracker.controler;

public class ProfilerEvent {
		long 	timeTag ;
		String 	event ;
		
		public ProfilerEvent ( long timeTag , String event ) {
			this.timeTag = timeTag ;
			this.event 	 = event ;
		}
}
