package org.origin.common.cluster.impl.jgroup;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jgroups.Channel;
import org.jgroups.JChannel;
import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventBus;
import org.origin.common.cluster.EventGroup;
import org.origin.common.cluster.EventListener;
import org.origin.common.cluster.PropertyChangeListener;
import org.origin.common.cluster.PropertyListValueChangeListener;
import org.origin.common.cluster.PropertyMapValueChangeListener;

public class EventBusImpl implements EventBus {

	protected static EventListener[] EMPTY_EVENT_LISTENERS = new EventListener[0];
	protected static PropertyChangeListener[] EMPTY_PROPERTY_CHANGE_LISTENERS = new PropertyChangeListener[0];
	protected static PropertyMapValueChangeListener[] EMPTY_PROPERTY_MAP_VALUE_CHANGE_LISTENERS = new PropertyMapValueChangeListener[0];
	protected static PropertyListValueChangeListener[] EMPTY_PROPERTY_LIST_VALUE_CHANGE_LISTENERS = new PropertyListValueChangeListener[0];

	protected Map<String, Channel> clusterNameToChannelMap = new HashMap<String, Channel>();
	protected Map<String, EventGroup> clusterNameToEventGroupMap = new HashMap<String, EventGroup>();

	protected final ReadWriteLock rwlock = new ReentrantReadWriteLock();

	// --------------------------------------------
	// Helper
	// --------------------------------------------

	protected Channel findOrCreateChannel(String clusterName) {
		this.rwlock.writeLock().lock();
		try {
			Channel channel = this.clusterNameToChannelMap.get(clusterName);
			if (channel == null) {
				try {
					channel = new JChannel();
					channel.connect(clusterName);
					this.clusterNameToChannelMap.put(clusterName, channel);

				} catch (Exception e) {
					// Do not throw out internal error.
					e.printStackTrace();
				}
			}
			return channel;

		} finally {
			this.rwlock.writeLock().unlock();
		}
	}

	protected EventGroup findOrCreateEventGroup(String clusterName, Channel channel) {
		this.rwlock.writeLock().lock();
		try {
			EventGroup eventGroup = this.clusterNameToEventGroupMap.get(clusterName);
			if (eventGroup == null) {
				try {
					eventGroup = new EventGroupImpl(clusterName, channel);
					eventGroup.start();
					this.clusterNameToEventGroupMap.put(clusterName, eventGroup);

				} catch (Exception e) {
					// Do not throw out internal error.
					e.printStackTrace();
				}
			}
			return eventGroup;

		} finally {
			this.rwlock.writeLock().unlock();
		}
	}

	protected Channel findChannel(String clusterName) {
		this.rwlock.readLock().lock();
		try {
			Channel channel = this.clusterNameToChannelMap.get(clusterName);
			return channel;

		} finally {
			this.rwlock.readLock().unlock();
		}
	}

	protected EventGroup findEventGroup(String clusterName) {
		this.rwlock.readLock().lock();
		try {
			EventGroup eventGroup = this.clusterNameToEventGroupMap.get(clusterName);
			return eventGroup;

		} finally {
			this.rwlock.readLock().unlock();
		}
	}

	protected String checkClusterName(String clusterName) {
		if (clusterName == null) {
			clusterName = GLOBAL_EVENTS_GROUP;
		}
		return clusterName;
	}

	protected void checkClusterNamesArg(String... clusterNames) {
		if (clusterNames == null) {
			throw new IllegalArgumentException("clusterNames is null.");
		}
		if (clusterNames.length == 0) {
			throw new IllegalArgumentException("clusterNames are empty.");
		}
	}

	protected void checkEventArg(Event event) {
		if (event == null) {
			throw new IllegalArgumentException("event is null.");
		}
	}

	protected void checkListenerArg(EventListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("EventListener is null.");
		}
	}

	protected void checkListenerArg(PropertyChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("PropertyChangeListener is null.");
		}
	}

	protected void checkListenerArg(PropertyMapValueChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("PropertyMapValueChangeListener is null.");
		}
	}

	protected void checkListenerArg(PropertyListValueChangeListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("PropertyListValueChangeListener is null.");
		}
	}

	protected void checkPropertyNameArg(Object propName) {
		if (propName == null) {
			throw new IllegalArgumentException("propName is null.");
		}
	}

	// --------------------------------------------
	// Life cycle
	// --------------------------------------------
	/**
	 * Close EventGroup and JCannel.
	 * 
	 * Note: JCannel is passed to EventGroup, so the life cycle of the JCannel is handled outside of EventGroup. e.g. EventGroup does not start or disconnect or
	 * close JChannel by itself, just like the code in a method do not close InputStream/OutputStream passed to the method.
	 * 
	 * @param clusterName
	 */
	@Override
	public void close(String clusterName) throws Exception {
		this.rwlock.writeLock().lock();
		try {
			clusterName = checkClusterName(clusterName);

			Exception ex1 = null;
			Exception ex2 = null;

			EventGroup eventGroup = this.clusterNameToEventGroupMap.remove(clusterName);
			if (eventGroup != null) {
				try {
					eventGroup.stop();
				} catch (Exception e) {
					ex1 = e;
					e.printStackTrace();
				}
			}

			Channel channel = this.clusterNameToChannelMap.remove(clusterName);
			if (channel != null) {
				try {
					channel.close();
				} catch (Exception e) {
					ex2 = e;
					e.printStackTrace();
				}
			}

			if (ex1 != null) {
				throw ex1;
			}
			if (ex2 != null) {
				throw ex2;
			}

		} finally {
			this.rwlock.writeLock().unlock();
		}
	}

	@Override
	public EventGroup getEventGroup(String clusterName) {
		clusterName = checkClusterName(clusterName);
		return findEventGroup(clusterName);
	}

	// --------------------------------------------
	// EventListener
	// --------------------------------------------
	@Override
	public EventListener[] getEventListeners(String clusterName) {
		checkClusterNamesArg(clusterName);

		EventListener[] eventListeners = null;

		EventGroup eventGroup = findEventGroup(clusterName);
		if (eventGroup != null) {
			eventListeners = eventGroup.getEventListeners();
		}

		if (eventListeners == null) {
			eventListeners = EMPTY_EVENT_LISTENERS;
		}
		return eventListeners;
	}

	@Override
	public boolean addEventListener(EventListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			Channel channel = findOrCreateChannel(clusterName);
			if (channel == null) {
				hasFailed = true;
				continue;
			}

			EventGroup eventGroup = findOrCreateEventGroup(clusterName, channel);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean added = eventGroup.addEventListener(listener);
			if (added) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public boolean removeEventListener(EventListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean removed = eventGroup.removeEventListener(listener);
			if (removed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	// --------------------------------------------
	// PropertyChangeListener
	// --------------------------------------------
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners(String clusterName) {
		checkClusterNamesArg(clusterName);

		PropertyChangeListener[] propertyChangeListeners = null;

		EventGroup eventGroup = findEventGroup(clusterName);
		if (eventGroup != null) {
			propertyChangeListeners = eventGroup.getPropertyChangeListeners();
		}

		if (propertyChangeListeners == null) {
			propertyChangeListeners = EMPTY_PROPERTY_CHANGE_LISTENERS;
		}
		return propertyChangeListeners;
	}

	@Override
	public boolean addPropertyChangeListener(PropertyChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			Channel channel = findOrCreateChannel(clusterName);
			if (channel == null) {
				hasFailed = true;
				continue;
			}

			EventGroup eventGroup = findOrCreateEventGroup(clusterName, channel);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean added = eventGroup.addPropertyChangeListener(listener);
			if (added) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public boolean removePropertyChangeListener(PropertyChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean removed = eventGroup.removePropertyChangeListener(listener);
			if (removed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	// --------------------------------------------
	// PropertyMapValueChangeListener
	// --------------------------------------------
	@Override
	public PropertyMapValueChangeListener[] getPropertyMapValueChangeListeners(String clusterName) {
		checkClusterNamesArg(clusterName);

		PropertyMapValueChangeListener[] listeners = null;

		EventGroup eventGroup = findEventGroup(clusterName);
		if (eventGroup != null) {
			listeners = eventGroup.getPropertyMapValueChangeListeners();
		}

		if (listeners == null) {
			listeners = EMPTY_PROPERTY_MAP_VALUE_CHANGE_LISTENERS;
		}
		return listeners;
	}

	@Override
	public boolean addPropertyMapValueChangeListener(PropertyMapValueChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			Channel channel = findOrCreateChannel(clusterName);
			if (channel == null) {
				hasFailed = true;
				continue;
			}

			EventGroup eventGroup = findOrCreateEventGroup(clusterName, channel);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean added = eventGroup.addPropertyMapValueChangeListener(listener);
			if (added) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public boolean removePropertyMapValueChangeListener(PropertyMapValueChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean removed = eventGroup.removePropertyMapValueChangeListener(listener);
			if (removed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	// --------------------------------------------
	// PropertyListValueChangeListener
	// --------------------------------------------
	@Override
	public PropertyListValueChangeListener[] getPropertyListValueChangeListeners(String clusterName) {
		checkClusterNamesArg(clusterName);

		PropertyListValueChangeListener[] listeners = null;

		EventGroup eventGroup = findEventGroup(clusterName);
		if (eventGroup != null) {
			listeners = eventGroup.getPropertyListValueChangeListeners();
		}

		if (listeners == null) {
			listeners = EMPTY_PROPERTY_LIST_VALUE_CHANGE_LISTENERS;
		}
		return listeners;
	}

	@Override
	public boolean addPropertyListValueChangeListener(PropertyListValueChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			Channel channel = findOrCreateChannel(clusterName);
			if (channel == null) {
				hasFailed = true;
				continue;
			}

			EventGroup eventGroup = findOrCreateEventGroup(clusterName, channel);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean added = eventGroup.addPropertyListValueChangeListener(listener);
			if (added) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	@Override
	public boolean removePropertyListValueChangeListener(PropertyListValueChangeListener listener, String... clusterNames) {
		checkListenerArg(listener);
		checkClusterNamesArg(clusterNames);

		boolean hasSucceed = false;
		boolean hasFailed = false;

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup == null) {
				hasFailed = true;
				continue;
			}

			boolean removed = eventGroup.removePropertyListValueChangeListener(listener);
			if (removed) {
				hasSucceed = true;
			} else {
				hasFailed = true;
			}
		}

		return (hasSucceed && !hasFailed) ? true : false;
	}

	// --------------------------------------------
	// Event
	// --------------------------------------------
	@Override
	public void sendEvent(Event event, String... clusterNames) {
		checkEventArg(event);
		checkClusterNamesArg(clusterNames);

		for (String clusterName : clusterNames) {
			clusterName = checkClusterName(clusterName);

			Channel channel = findOrCreateChannel(clusterName);
			if (channel == null) {
				continue;
			}

			EventGroup eventGroup = findOrCreateEventGroup(clusterName, channel);
			if (eventGroup == null) {
				continue;
			}

			try {
				eventGroup.sendEvent(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// --------------------------------------------
	// Properties
	// --------------------------------------------
	@Override
	public Map<Object, Object> getLocalProperties(String clusterName) {
		Map<Object, Object> localProperties = null;

		EventGroup eventGroup = findEventGroup(clusterName);
		if (eventGroup != null) {
			localProperties = eventGroup.getLocalProperties();
		}

		if (localProperties == null) {
			localProperties = Collections.emptyMap();
		}
		return localProperties;
	}

	@Override
	public void setProperties(Object source, Map<Object, Object> properties, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);

		if (properties == null) {
			throw new IllegalArgumentException("properties is null.");
		}

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertiesEvent(source, properties);
			}
		}
	}

	@Override
	public void setProperty(Object source, Object propName, Object propValue, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);

		if (propName == null) {
			throw new IllegalArgumentException("propName is null.");
		}

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyEvent(source, propName, propValue);
			}
		}
	}

	@Override
	public void removeProperty(Object source, Object propName, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendRemovePropertyEvent(source, propName);
			}
		}
	}

	@Override
	public void clearProperties(Object source, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendClearPropertiesEvent(source);
			}
		}
	}

	// --------------------------------------------
	// Property with Map value
	// --------------------------------------------
	@Override
	public void setPropertyMapPutAll(Object source, Object propName, Map<Object, Object> map, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyMapPutAll(source, propName, map);
			}
		}
	}

	@Override
	public void setPropertyMapPut(Object source, Object propName, Object mapKey, Object mapValue, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyMapPut(source, propName, mapKey, mapValue);
			}
		}
	}

	@Override
	public void setPropertyMapRemove(Object source, Object propName, Object mapKey, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyMapRemove(source, propName, mapKey);
			}
		}
	}

	@Override
	public void setPropertyMapClear(Object source, Object propName, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyMapClear(source, propName);
			}
		}
	}

	// --------------------------------------------
	// Property with List value
	// --------------------------------------------
	@Override
	public void setPropertyListAddAll(Object source, Object propName, List<Object> list, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyListAddAll(source, propName, list);
			}
		}
	}

	@Override
	public void setPropertyListAdd(Object source, Object propName, Object listItem, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyListAdd(source, propName, listItem);
			}
		}
	}

	@Override
	public void setPropertyListRemoveAll(Object source, Object propName, List<Object> list, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyListRemoveAll(source, propName, list);
			}
		}
	}

	@Override
	public void setPropertyListRemove(Object source, Object propName, Object listItem, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyListRemove(source, propName, listItem);
			}
		}
	}

	@Override
	public void setPropertyListClear(Object source, Object propName, String... clusterNames) throws Exception {
		checkClusterNamesArg(clusterNames);
		checkPropertyNameArg(propName);

		for (String clusterName : clusterNames) {
			EventGroup eventGroup = findEventGroup(clusterName);
			if (eventGroup != null) {
				eventGroup.sendSetPropertyListClear(source, propName);
			}
		}
	}

}
