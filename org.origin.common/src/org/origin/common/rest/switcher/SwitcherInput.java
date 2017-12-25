package org.origin.common.rest.switcher;

import java.util.List;

import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;

public interface SwitcherInput<ITEM> {

	static String P_ITEM_ADDED = "p_item_added";
	static String P_ITEM_REMOVED = "p_item_removed";
	static String P_ITEM_PROPERTY_ADDED = "p_item_property_added";
	static String P_ITEM_PROPERTY_REMOVED = "p_item_property_removed";

	List<ITEM> getItems();

	boolean contains(ITEM item);

	boolean add(ITEM item);

	boolean remove(ITEM item);

	String[] getPropertyNames(ITEM item);

	boolean hasProperty(ITEM item, String propName);

	Object getProperty(ITEM item, String propName);

	<T> T getProperty(ITEM item, String propName, Class<T> clazz);

	void setProperty(ITEM item, String propName, Object propValue);

	Object removeProperty(ITEM item, String propName);

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void notifyPropertyChangeEvent(PropertyChangeEvent event);

}
