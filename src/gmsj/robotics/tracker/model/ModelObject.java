package gmsj.robotics.tracker.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelObject {

	protected PropertyChangeSupport propertyChange; 

	/** 
	 * Ajoute un element a la liste des ecouteurs des modifications de cette classe 
	 * @param propertyName la propriete que l'element veut surveiller 
	 * @param listener l'element qui s'abonne 
	 */ 
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) { 
		propertyChange.addPropertyChangeListener(propertyName, listener); 
	} 

	/** 
	 * Supprime un element a la liste des ecouteurs des modifications de cette classe 
	 * @param listener l'element a supprimer 
	 */ 
	public void removePropertyChangeListener(PropertyChangeListener listener) { 
		propertyChange.removePropertyChangeListener(listener); 
	}
}
