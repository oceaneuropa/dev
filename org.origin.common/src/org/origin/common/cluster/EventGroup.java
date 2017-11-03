package org.origin.common.cluster;

import java.util.List;
import java.util.Map;

public interface EventGroup {

	// --------------------------------------------
	// Life cycle
	// --------------------------------------------
	void start() throws Exception;

	void stop() throws Exception;

	// --------------------------------------------
	// EventListener
	// --------------------------------------------
	EventListener[] getEventListeners();

	boolean addEventListener(EventListener eventListener);

	boolean removeEventListener(EventListener eventListener);

	// --------------------------------------------
	// PropertyChangeListener
	// --------------------------------------------
	PropertyChangeListener[] getPropertyChangeListeners();

	boolean addPropertyChangeListener(PropertyChangeListener listener);

	boolean removePropertyChangeListener(PropertyChangeListener listener);

	// --------------------------------------------
	// PropertyMapValueChangeListener
	// --------------------------------------------
	PropertyMapValueChangeListener[] getPropertyMapValueChangeListeners();

	boolean addPropertyMapValueChangeListener(PropertyMapValueChangeListener listener);

	boolean removePropertyMapValueChangeListener(PropertyMapValueChangeListener listener);

	// --------------------------------------------
	// PropertyListValueChangeListener
	// --------------------------------------------
	PropertyListValueChangeListener[] getPropertyListValueChangeListeners();

	boolean addPropertyListValueChangeListener(PropertyListValueChangeListener listener);

	boolean removePropertyListValueChangeListener(PropertyListValueChangeListener listener);

	// --------------------------------------------
	// Event
	// --------------------------------------------
	void sendEvent(Event event) throws Exception;

	// --------------------------------------------
	// Properties
	// --------------------------------------------
	Map<Object, Object> getLocalProperties();

	void sendSetPropertiesEvent(Object source, Map<Object, Object> properties) throws Exception;

	void sendSetPropertyEvent(Object source, Object propName, Object propValue) throws Exception;

	void sendRemovePropertyEvent(Object source, Object propName) throws Exception;

	void sendClearPropertiesEvent(Object source) throws Exception;

	// --------------------------------------------
	// Property with Map value
	// --------------------------------------------
	void sendSetPropertyMapPutAll(Object source, Object propName, Map<Object, Object> map) throws Exception;

	void sendSetPropertyMapPut(Object source, Object propName, Object mapKey, Object mapValue) throws Exception;

	void sendSetPropertyMapRemove(Object source, Object propName, Object mapKey) throws Exception;

	void sendSetPropertyMapClear(Object source, Object propName) throws Exception;

	// --------------------------------------------
	// Property with List value
	// --------------------------------------------
	void sendSetPropertyListAddAll(Object source, Object propName, List<Object> list) throws Exception;

	void sendSetPropertyListAdd(Object source, Object propName, Object listItem) throws Exception;

	void sendSetPropertyListRemoveAll(Object source, Object propName, List<Object> list) throws Exception;

	void sendSetPropertyListRemove(Object source, Object propName, Object listItem) throws Exception;

	void sendSetPropertyListClear(Object source, Object propName) throws Exception;

}
