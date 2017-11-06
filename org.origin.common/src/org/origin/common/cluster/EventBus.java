package org.origin.common.cluster;

import java.util.List;
import java.util.Map;

public interface EventBus {

	public static final String GLOBAL_EVENTS_GROUP = "_GLOBAL_EVENTS_GROUP";

	// --------------------------------------------
	// Life cycle
	// --------------------------------------------
	void joinCluster(String clusterName) throws Exception;

	void leaveCluster(String clusterName) throws Exception;

	void closeCluster(String clusterName) throws Exception;

	EventGroup getEventGroup(String clusterName);

	// --------------------------------------------
	// EventListener
	// --------------------------------------------
	EventListener[] getEventListeners(String clusterName);

	boolean addEventListener(EventListener listener, String... clusterNames);

	boolean removeEventListener(EventListener listener, String... clusterNames);

	// --------------------------------------------
	// PropertyChangeListener
	// --------------------------------------------
	PropertyChangeListener[] getPropertyChangeListeners(String clusterName);

	boolean addPropertyChangeListener(PropertyChangeListener listener, String... clusterNames);

	boolean removePropertyChangeListener(PropertyChangeListener listener, String... clusterNames);

	// --------------------------------------------
	// PropertyMapValueChangeListener
	// --------------------------------------------
	PropertyMapValueChangeListener[] getPropertyMapValueChangeListeners(String clusterName);

	boolean addPropertyMapValueChangeListener(PropertyMapValueChangeListener listener, String... clusterNames);

	boolean removePropertyMapValueChangeListener(PropertyMapValueChangeListener listener, String... clusterNames);

	// --------------------------------------------
	// PropertyListValueChangeListener
	// --------------------------------------------
	PropertyListValueChangeListener[] getPropertyListValueChangeListeners(String clusterName);

	boolean addPropertyListValueChangeListener(PropertyListValueChangeListener listener, String... clusterNames);

	boolean removePropertyListValueChangeListener(PropertyListValueChangeListener listener, String... clusterNames);

	// --------------------------------------------
	// Event
	// --------------------------------------------
	void sendEvent(Event event, String... clusterNames);

	// --------------------------------------------
	// Properties
	// --------------------------------------------
	Map<Object, Object> getLocalProperties(String clusterName);

	void setProperties(Object source, Map<Object, Object> properties, String... clusterNames) throws Exception;

	void setProperty(Object source, Object propName, Object propValue, String... clusterNames) throws Exception;

	void removeProperty(Object source, Object propName, String... clusterNames) throws Exception;

	void clearProperties(Object source, String... clusterNames) throws Exception;

	// --------------------------------------------
	// Property with Map value
	// --------------------------------------------
	void setPropertyMapPutAll(Object source, Object propName, Map<Object, Object> map, String... clusterNames) throws Exception;

	void setPropertyMapPut(Object source, Object propName, Object mapKey, Object mapValue, String... clusterNames) throws Exception;

	void setPropertyMapRemove(Object source, Object propName, Object mapKey, String... clusterNames) throws Exception;

	void setPropertyMapClear(Object source, Object propName, String... clusterNames) throws Exception;

	// --------------------------------------------
	// Property with List value
	// --------------------------------------------
	void setPropertyListAddAll(Object source, Object propName, List<Object> list, String... clusterNames) throws Exception;

	void setPropertyListAdd(Object source, Object propName, Object listItem, String... clusterNames) throws Exception;

	void setPropertyListRemoveAll(Object source, Object propName, List<Object> list, String... clusterNames) throws Exception;

	void setPropertyListRemove(Object source, Object propName, Object listItem, String... clusterNames) throws Exception;

	void setPropertyListClear(Object source, Object propName, String... clusterNames) throws Exception;

}
