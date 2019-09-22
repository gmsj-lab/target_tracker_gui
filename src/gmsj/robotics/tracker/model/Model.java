package gmsj.robotics.tracker.model;

import java.util.ArrayList;

import gmsj.robotics.tracker.navigation.ITreeElement;

public class Model implements ITreeElement {
	
	private ArrayList<Target> 	targetList	= new ArrayList<Target> () ;
	private String 				name 		= new String ( "Model" ) ;
	
	public void addTarget ( Target target ) {
		target.setParent( this ) ;
		targetList.add( target ) ;	
	}
	@Override
	public ITreeElement getParent () {
		return null;
	}
	@Override
	public ITreeElement[] getChildren () {
		return targetList.toArray( new ITreeElement[targetList.size()] ) ;
	}
	@Override
	public boolean hasChildren () {
		return ! targetList.isEmpty () ;
	}
	@Override
	public String getName () {
		return name ;
	}
	public int getNumberOfTargets () {
		return targetList.size () ;
	}
	public ArrayList < Target > getTargets () {
		return targetList ;
	}
	public Target getTarget ( String targetName ) {
		Target target = null ;
		
		for ( Target candidate : targetList ) {
			if ( candidate.getName().equals ( targetName ) ) {
				target = candidate ;
				break ;
			}
		}
		return target ;
	}
	public int getNumberOfAttributes () {
		int numberOfAttributes = 0 ;
		for ( Target target : targetList ) {
			numberOfAttributes += target.getNumberOfAttributes () ;
		}
		return numberOfAttributes ;
	}
	public String[] getTargetAttributesString () {
		int i = 0 ;
		String targetAttributesString [] = new String [ getNumberOfAttributes () ]  ;
		
		for ( Target target : targetList ) {
			for ( ITreeElement attribute : target.getChildren () )
			if ( attribute.hasChildren () ) {
				for ( ITreeElement subAttribute : attribute.getChildren () ) {
					targetAttributesString[ i ++ ] = subAttribute.getName () ;
				}
			}
			else {
				targetAttributesString[ i ++ ] = attribute.getName () ;
				}				
			}
		return targetAttributesString ;
	}
	public void closeTarget ( String targetName ) {
		Target target = getTarget ( targetName )  ;
		
		if ( target != null ) {
			targetList.remove ( target ) ;
			target.close () ;
		}
	}
	@Override
	public void setParent ( ITreeElement parent ) {		
	}
}
