package gmsj.robotics.tracker.controler;

import java.util.ArrayList;

public class Profiler {

	private static ArrayList < ProfilerEvent > records = new ArrayList < ProfilerEvent > () ;
	
	public static void add ( String event ) {
		records.add ( new ProfilerEvent ( System.nanoTime() , event ) ) ;
	}
	public static void display () {
		for ( ProfilerEvent timeEvent : records ) {
			System.out.println ( timeEvent.timeTag + ":" +  timeEvent.event ) ;
		}
	}
}
