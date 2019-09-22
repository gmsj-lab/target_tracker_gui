package gmsj.robotics.tracker.model;

import gmsj.robotics.tracker.navigation.ITreeElement;

public interface ITargetAttribute extends ITreeElement {

	void 		setTarget 				( Target target ) ;
	String 		getTargetName 			() ;
	
	void 		setName 				( String attributeName ) ;
	
	void 		setId 					( String string ) ;
	String 		getId					() ;
			
	void 		setType					( DataType type ) ;
	DataType 	getType					() ;
	
	void 		setWrite				( boolean writeOnly ) ;
	boolean 	isWrite					() ;
}
