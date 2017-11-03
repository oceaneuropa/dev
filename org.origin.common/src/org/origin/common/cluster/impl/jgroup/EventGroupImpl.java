package org.origin.common.cluster.impl.jgroup;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.blocks.AsyncRequestHandler;
import org.jgroups.blocks.MessageDispatcher;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.Response;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.Buffer;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;
import org.origin.common.cluster.Event;
import org.origin.common.cluster.EventContext;
import org.origin.common.cluster.EventGroup;
import org.origin.common.cluster.EventListener;
import org.origin.common.cluster.PropertyChangeListener;
import org.origin.common.cluster.PropertyListValueChangeListener;
import org.origin.common.cluster.PropertyMapValueChangeListener;
import org.origin.common.cluster.impl.DataPackage;
import org.origin.common.cluster.impl.EventConstants;
import org.origin.common.cluster.impl.EventContextImpl;

/**
 * @see org.jgroups.blocks.ReplicatedHashMap
 * 
 */
public class EventGroupImpl implements EventGroup {

	protected String clusterName;
	protected Channel channel;

	protected RequestOptions options;
	protected MessageDispatcher messageDispatcher;

	protected List<Address> members = new ArrayList<Address>();
	protected List<EventListener> eventListeners = new ArrayList<EventListener>();
	protected List<PropertyChangeListener> propertyChangeListeners = new ArrayList<PropertyChangeListener>();
	protected List<PropertyMapValueChangeListener> propertyMapValueChangeListeners = new ArrayList<PropertyMapValueChangeListener>();
	protected List<PropertyListValueChangeListener> propertyListValueChangeListeners = new ArrayList<PropertyListValueChangeListener>();

	protected Map<Object, Object> localProperties = new HashMap<Object, Object>();

	/**
	 * 
	 * @param clusterName
	 * @param channel
	 */
	public EventGroupImpl(String clusterName, Channel channel) {
		this.clusterName = clusterName;
		this.channel = channel;
		this.options = new RequestOptions(ResponseMode.GET_NONE, 5000);
	}

	public Channel getChannel() {
		return this.channel;
	}

	public boolean isConnected() {
		return this.channel.isConnected();
	}

	public Address getLocalAddress() {
		return this.channel != null ? this.channel.getAddress() : null;
	}

	public boolean isBlockingUpdates() {
		return this.options.getMode() == ResponseMode.GET_ALL;
	}

	/**
	 * Whether updates across the cluster should be asynchronous (default) or synchronous.
	 * 
	 * @param blockingUpdates
	 */
	public void setBlockingUpdates(boolean blockingUpdates) {
		this.options.setMode(blockingUpdates ? ResponseMode.GET_ALL : ResponseMode.GET_NONE);
	}

	/**
	 * The timeout (in milliseconds) for blocking updates
	 */
	public long getTimeout() {
		return this.options.getTimeout();
	}

	/**
	 * Sets the cluster call timeout (until all acks have been received)
	 * 
	 * @param timeout
	 *            The timeout (in milliseconds) for blocking updates
	 */
	public void setTimeout(long timeout) {
		this.options.setTimeout(timeout);
	}

	@Override
	public void start() throws Exception {
		this.messageDispatcher = createMessageDispatcher(this.channel);

		long getStateTimeout = getTimeout();
		if (getStateTimeout < 0) {
			getStateTimeout = 0;
		}
		this.channel.getState(null, getStateTimeout);
	}

	@Override
	public void stop() throws Exception {
		if (this.messageDispatcher != null) {
			this.messageDispatcher.stop();
			this.messageDispatcher = null;
		}
	}

	/**
	 * 
	 * @param channel
	 * @return
	 */
	protected MessageDispatcher createMessageDispatcher(Channel channel) {
		Receiver receiver = new Receiver() {
			@Override
			public void viewAccepted(View newView) {
				System.out.println(getClass().getSimpleName() + ".viewAccepted(View)");
			}

			@Override
			public void receive(Message message) {
				System.out.println(getClass().getSimpleName() + ".viewAccepted(Message)");
			}

			@Override
			public void getState(OutputStream outputStream) throws Exception {
				System.out.println(getClass().getSimpleName() + ".getState(OutputStream)");
				writeGroupStatus(outputStream);
			}

			@Override
			public void setState(InputStream inputStream) throws Exception {
				System.out.println(getClass().getSimpleName() + ".setState(InputStream)");
				readGroupStatus(inputStream);
			}

			/**
			 * Called when a member is suspected
			 * 
			 */
			@Override
			public void suspect(Address suspectedMember) {
				System.out.println(getClass().getSimpleName() + ".suspect(Address)");
			}

			/**
			 * Block sending and receiving of messages until ViewAccepted is called
			 * 
			 */
			@Override
			public void block() {
				System.out.println(getClass().getSimpleName() + ".block()");
			}

			@Override
			public void unblock() {
				System.out.println(getClass().getSimpleName() + ".unblock()");
			}
		};

		AsyncRequestHandler requestHandler = new AsyncRequestHandler() {
			@Override
			public void handle(Message receivedMessage, Response response) throws Exception {
				Object returnValue = handle(receivedMessage);
				if (response != null && returnValue != null) {
					response.send(returnValue, false);
				}
			}

			@Override
			public Object handle(Message receivedMessage) throws Exception {
				DataPackage receivedDataPackage = getDataPackage(receivedMessage);
				if (receivedDataPackage != null) {
					Event receivedEvent = receivedDataPackage.getEvent();

					if (receivedEvent != null) {
						String namespace = receivedEvent.getNamespace();
						Object source = receivedEvent.getSource();

						if (EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES.equals(namespace)) {
							updateLocalProperties(receivedDataPackage);

						} else {
							String clusterName = receivedDataPackage.getClusterName();
							EventContextImpl context = new EventContextImpl();
							context.set(EventContext.PROP_CLUSTER_NAME, clusterName);
							context.set(EventContext.PROP_SOURCE, source);

							localNotifyEvent(context, receivedEvent);
						}

						return "received";
					}
				}
				return "not_received";
			}

			/**
			 * 
			 * @param receivedMessage
			 * @return
			 */
			protected DataPackage getDataPackage(Message receivedMessage) {
				DataPackage dataPackage = null;
				if (receivedMessage != null) {
					Object body = receivedMessage.getObject();
					if (!(body instanceof DataPackage)) {
						throw new IllegalArgumentException("The received message does not contain an DataPackage object");
					}

					dataPackage = (DataPackage) body;
				}
				return dataPackage;
			}

			/**
			 * 
			 * @param dataPackage
			 */
			protected void updateLocalProperties(DataPackage dataPackage) {
				if (dataPackage == null) {
					return;
				}
				String clusterName = dataPackage.getClusterName();
				Event receivedEvent = dataPackage.getEvent();
				if (receivedEvent == null) {
					return;
				}

				String namespace = receivedEvent.getNamespace();
				String localPart = receivedEvent.getLocalPart();
				Object source = receivedEvent.getSource();
				Object args = receivedEvent.getArgs();

				if (!EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES.equals(namespace)) {
					throw new IllegalArgumentException("Event namespace is unexpected: " + namespace);
				}

				EventContextImpl context = new EventContextImpl();
				context.set(EventContext.PROP_CLUSTER_NAME, clusterName);
				context.set(EventContext.PROP_SOURCE, source);

				try {
					if (EventConstants.SET_PROPERTIES.equals(localPart)) {
						if (!(args instanceof Map)) {
							throw new IllegalArgumentException(EventConstants.SET_PROPERTIES + " unexpected args: " + args);
						}

						@SuppressWarnings("unchecked")
						Map<Object, Object> propertiesCopy = (Map<Object, Object>) args;

						localSetProperties(propertiesCopy);
						localNotifySetProperties(context, localProperties);

					} else if (EventConstants.SET_PROPERTY.equals(localPart)) {
						if (!(args instanceof Map)) {
							throw new IllegalArgumentException(EventConstants.SET_PROPERTY + " unexpected args: " + args);
						}

						@SuppressWarnings("unchecked")
						Map<Object, Object> propertyCopy = (Map<Object, Object>) args;
						if (propertyCopy.isEmpty()) {
							throw new IllegalArgumentException(EventConstants.SET_PROPERTY + " unexpected args: " + propertyCopy);
						}

						Entry<Object, Object> entry = propertyCopy.entrySet().iterator().next();
						Object key = entry.getKey();
						Object value = entry.getValue();

						localSetProperty(key, value);
						localNotifySetProperty(context, key, value);

					} else if (EventConstants.REMOVE_PROPERTY.equals(localPart)) {
						if (args == null) {
							throw new IllegalArgumentException(EventConstants.REMOVE_PROPERTY + " unexpected args: " + args);
						}

						Object key = args;

						localRemoveProperty(key);
						localNotifyRemoveProperty(context, key);

					} else if (EventConstants.CLEAR_PROPERTIES.equals(localPart)) {
						localClearProperties();
						localNotifyClearProperty(context);

					} else if (EventConstants.SET_PROPERTY_MAP_PUTALL.equals(localPart)) {
						if (!(args instanceof Map)) {
							throw new IllegalArgumentException(EventConstants.SET_PROPERTY_MAP_PUTALL + " unexpected args: " + args);
						}

						Map<Object, Object> argsMap = (Map<Object, Object>) args;
						String propName = (String) argsMap.get(EventConstants.PROP_NAME);
						Map<Object, Object> map = (Map) argsMap.get(EventConstants.MAP);

					} else if (EventConstants.SET_PROPERTY_MAP_PUT.equals(localPart)) {
						if (!(args instanceof Map)) {
							throw new IllegalArgumentException(EventConstants.SET_PROPERTY_MAP_PUT + " unexpected args: " + args);
						}

					} else if (EventConstants.SET_PROPERTY_MAP_REMOVE.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_MAP_CLEAR.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_LIST_ADDALL.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_LIST_ADD.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_LIST_REMOVEALL.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_LIST_REMOVE.equals(localPart)) {

					} else if (EventConstants.SET_PROPERTY_LIST_CLEAR.equals(localPart)) {

					} else {
						System.err.println("updateLocalProperties() unsupported event: " + namespace + "." + localPart);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		MessageDispatcher dispatcher = new MessageDispatcher(channel, receiver, receiver, requestHandler);
		return dispatcher;
	}

	// --------------------------------------------------------
	// Update local properties
	// --------------------------------------------------------
	protected void localSetProperties(Map<Object, Object> map) {
		if (map != null && !map.isEmpty()) {
			synchronized (this.localProperties) {
				this.localProperties.putAll(map);
			}
		}
	}

	protected void localSetProperty(Object key, Object value) {
		synchronized (this.localProperties) {
			this.localProperties.put(key, value);
		}
	}

	protected void localRemoveProperty(Object key) {
		synchronized (this.localProperties) {
			this.localProperties.remove(key);
		}
	}

	protected void localClearProperties() {
		synchronized (this.localProperties) {
			this.localProperties.clear();
		}
	}

	// --------------------------------------------------------
	// Update local property with Map value
	// --------------------------------------------------------
	protected void localSetPropertyMapPutAll(String propName, Map<Object, Object> map) {
		synchronized (this.localProperties) {
			Map<Object, Object> propValueMap = getPropertyValueMap(propName);
			propValueMap.putAll(map);
		}
	}

	protected void localSetPropertyMapPut(String propName, Object mapKey, Object mapValue) {
		synchronized (this.localProperties) {
			Map<Object, Object> propValueMap = getPropertyValueMap(propName);
			propValueMap.put(mapKey, mapValue);
		}
	}

	protected void localSetPropertyMapRemove(String propName, Object mapKey) {
		synchronized (this.localProperties) {
			Map<Object, Object> propValueMap = getPropertyValueMap(propName);
			propValueMap.remove(mapKey);
		}
	}

	protected void localSetPropertyMapClear(String propName) {
		synchronized (this.localProperties) {
			Map<Object, Object> propValueMap = getPropertyValueMap(propName);
			propValueMap.clear();
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Object> getPropertyValueMap(String propName) {
		Map<Object, Object> propValueMap = null;
		synchronized (this.localProperties) {
			Object value = this.localProperties.get(propName);
			if (value != null && !(value instanceof Map)) {
				System.err.println("Property value for " + propName + " is unexpected: " + value);
			}

			if (value instanceof Map) {
				propValueMap = (Map<Object, Object>) value;
			}
			if (propValueMap == null) {
				propValueMap = new HashMap<Object, Object>();
				this.localProperties.put(propName, propValueMap);
			}
		}
		return propValueMap;
	}

	// --------------------------------------------------------
	// Update local property with List value
	// --------------------------------------------------------
	protected void localSetPropertyListAddAll(String propName, List<Object> list) {
		synchronized (this.localProperties) {
			List<Object> propValueList = getPropertyValueList(propName);
			propValueList.addAll(list);
		}
	}

	protected void localSetPropertyListAll(String propName, Object listItem) {
		synchronized (this.localProperties) {
			List<Object> propValueList = getPropertyValueList(propName);
			propValueList.add(listItem);
		}
	}

	protected void localSetPropertyListRemoveAll(String propName, List<Object> list) {
		synchronized (this.localProperties) {
			List<Object> propValueList = getPropertyValueList(propName);
			propValueList.removeAll(list);
		}
	}

	protected void localSetPropertyListRemove(String propName, Object listItem) {
		synchronized (this.localProperties) {
			List<Object> propValueList = getPropertyValueList(propName);
			propValueList.remove(listItem);
		}
	}

	protected void localSetPropertyListClear(String propName) {
		synchronized (this.localProperties) {
			List<Object> propValueList = getPropertyValueList(propName);
			propValueList.clear();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object> getPropertyValueList(String propName) {
		List<Object> propValueList = null;
		synchronized (this.localProperties) {
			Object value = this.localProperties.get(propName);
			if (value != null && !(value instanceof List)) {
				System.err.println("Property value for " + propName + " is unexpected: " + value);
			}

			if (value instanceof List) {
				propValueList = (List<Object>) value;
			}
			if (propValueList == null) {
				propValueList = new ArrayList<Object>();
				this.localProperties.put(propName, propValueList);
			}
		}
		return propValueList;
	}

	// -------------------------------------------------------
	// Group status
	// -------------------------------------------------------
	/**
	 * I am the group coordinator. A new group member send request to me to get the status of the group.
	 * 
	 * - I need to write the status to output stream.
	 * 
	 * @param outputStream
	 */
	protected void writeGroupStatus(OutputStream outputStream) {
		System.out.println("writeGroupStatus()");

		ObjectOutputStream objectOutput = null;
		try {
			Map<Object, Object> statusMap = new HashMap<Object, Object>();
			{
				Map<Object, Object> propertiesCopy = new HashMap<Object, Object>();
				propertiesCopy.putAll(this.localProperties);
				statusMap.put("properties", propertiesCopy);
			}

			objectOutput = new ObjectOutputStream(new BufferedOutputStream(outputStream, 1024));
			objectOutput.writeObject(statusMap);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (objectOutput != null) {
				try {
					objectOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * I am a new group member. I have requested to get the status of the group.
	 * 
	 * - the coordinator number has sent me the status of the group
	 * 
	 * - I need to read the group status from the input stream and keep a local copy of the status myself.
	 * 
	 * @param inputStream
	 */
	protected void readGroupStatus(InputStream inputStream) {
		System.out.println("readGroupStatus()");

		ObjectInputStream objectInput = null;
		try {
			objectInput = new ObjectInputStream(inputStream);

			Object object = objectInput.readObject();
			if (!(object instanceof Map)) {
				throw new IllegalArgumentException("Status object is unexpected: " + object);
			}

			EventContextImpl context = new EventContextImpl();
			context.set(EventContext.PROP_CLUSTER_NAME, this.clusterName);

			Map<Object, Object> statusMap = (Map<Object, Object>) object;
			Map<Object, Object> propertiesCopy = (Map<Object, Object>) statusMap.get("properties");
			if (propertiesCopy != null) {
				this.localProperties.clear();
				localSetProperties(propertiesCopy);
				localNotifySetProperties(context, this.localProperties);
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (objectInput != null) {
				try {
					objectInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// --------------------------------------------
	// EventListener
	// --------------------------------------------
	@Override
	public EventListener[] getEventListeners() {
		synchronized (this.eventListeners) {
			return this.eventListeners.toArray(new EventListener[this.eventListeners.size()]);
		}
	}

	@Override
	public boolean addEventListener(EventListener eventListener) {
		synchronized (this.eventListeners) {
			if (eventListener != null && !this.eventListeners.contains(eventListener)) {
				return this.eventListeners.add(eventListener);
			}
			return false;
		}
	}

	@Override
	public boolean removeEventListener(EventListener eventListener) {
		synchronized (this.eventListeners) {
			if (eventListener != null && this.eventListeners.contains(eventListener)) {
				return this.eventListeners.remove(eventListener);
			}
			return false;
		}
	}

	// --------------------------------------------
	// PropertyChangeListener
	// --------------------------------------------
	@Override
	public PropertyChangeListener[] getPropertyChangeListeners() {
		synchronized (this.propertyChangeListeners) {
			return this.propertyChangeListeners.toArray(new PropertyChangeListener[this.propertyChangeListeners.size()]);
		}
	}

	@Override
	public boolean addPropertyChangeListener(PropertyChangeListener listener) {
		synchronized (this.propertyChangeListeners) {
			if (listener != null && !this.propertyChangeListeners.contains(listener)) {
				return this.propertyChangeListeners.add(listener);
			}
			return false;
		}
	}

	@Override
	public boolean removePropertyChangeListener(PropertyChangeListener listener) {
		synchronized (this.propertyChangeListeners) {
			if (listener != null && this.propertyChangeListeners.contains(listener)) {
				return this.propertyChangeListeners.remove(listener);
			}
			return false;
		}
	}

	// --------------------------------------------
	// PropertyMapValueChangeListener
	// --------------------------------------------
	@Override
	public PropertyMapValueChangeListener[] getPropertyMapValueChangeListeners() {
		synchronized (this.propertyMapValueChangeListeners) {
			return this.propertyMapValueChangeListeners.toArray(new PropertyMapValueChangeListener[this.propertyMapValueChangeListeners.size()]);
		}
	}

	@Override
	public boolean addPropertyMapValueChangeListener(PropertyMapValueChangeListener listener) {
		synchronized (this.propertyMapValueChangeListeners) {
			if (listener != null && !this.propertyMapValueChangeListeners.contains(listener)) {
				return this.propertyMapValueChangeListeners.add(listener);
			}
			return false;
		}
	}

	@Override
	public boolean removePropertyMapValueChangeListener(PropertyMapValueChangeListener listener) {
		synchronized (this.propertyMapValueChangeListeners) {
			if (listener != null && this.propertyMapValueChangeListeners.contains(listener)) {
				return this.propertyMapValueChangeListeners.remove(listener);
			}
			return false;
		}
	}

	// --------------------------------------------
	// PropertyListValueChangeListener
	// --------------------------------------------
	@Override
	public PropertyListValueChangeListener[] getPropertyListValueChangeListeners() {
		synchronized (this.propertyListValueChangeListeners) {
			return this.propertyListValueChangeListeners.toArray(new PropertyListValueChangeListener[this.propertyListValueChangeListeners.size()]);
		}
	}

	@Override
	public boolean addPropertyListValueChangeListener(PropertyListValueChangeListener listener) {
		synchronized (this.propertyListValueChangeListeners) {
			if (listener != null && !this.propertyListValueChangeListeners.contains(listener)) {
				return this.propertyListValueChangeListeners.add(listener);
			}
			return false;
		}
	}

	@Override
	public boolean removePropertyListValueChangeListener(PropertyListValueChangeListener listener) {
		synchronized (this.propertyListValueChangeListeners) {
			if (listener != null && this.propertyListValueChangeListeners.contains(listener)) {
				return this.propertyListValueChangeListeners.remove(listener);
			}
			return false;
		}
	}

	// --------------------------------------------
	// Event
	// --------------------------------------------
	@Override
	public void sendEvent(Event event) throws Exception {
		// System.out.println(getClass().getSimpleName() + ".sendEvent(Event) event: " + (event != null ? event.toString() : null));
		if (event == null) {
			throw new IllegalArgumentException("event is null.");
		}

		DataPackage eventPackage = new DataPackage(event, this.clusterName);

		Buffer buf = Util.objectToBuffer(eventPackage);
		Message message = new Message().setBuffer(buf);

		@SuppressWarnings("unused")
		RspList<?> responseList = this.messageDispatcher.castMessage(null, message, options);
	}

	// -------------------------------------------------------
	// Properties
	// -------------------------------------------------------
	@Override
	public Map<Object, Object> getLocalProperties() {
		synchronized (this.localProperties) {
			Map<Object, Object> copy = new TreeMap<Object, Object>();
			copy.putAll(this.localProperties);
			return copy;
		}
	}

	@Override
	public void sendSetPropertiesEvent(Object source, Map<Object, Object> map) throws Exception {
		if (map == null || map.isEmpty()) {
			return;
		}
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.putAll(map);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTIES, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyEvent(Object source, Object key, Object value) throws Exception {
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(key, value);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY, source, args);
		sendEvent(event);
	}

	@Override
	public void sendRemovePropertyEvent(Object source, Object key) throws Exception {
		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.REMOVE_PROPERTY, source, key);
		sendEvent(event);
	}

	@Override
	public void sendClearPropertiesEvent(Object source) throws Exception {
		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.CLEAR_PROPERTIES, source, null);
		sendEvent(event);
	}

	// --------------------------------------------
	// Property with Map value
	// --------------------------------------------
	@Override
	public void sendSetPropertyMapPutAll(Object source, Object propName, Map<Object, Object> map) throws Exception {
		if (map == null || map.isEmpty()) {
			return;
		}
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.MAP, map);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_MAP_PUTALL, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyMapPut(Object source, Object propName, Object mapKey, Object mapValue) throws Exception {
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.MAP_KEY, mapKey);
		args.put(EventConstants.MAP_VALUE, mapValue);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_MAP_PUT, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyMapRemove(Object source, Object propName, Object mapKey) throws Exception {
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.MAP_KEY, mapKey);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_MAP_REMOVE, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyMapClear(Object source, Object propName) throws Exception {
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_MAP_CLEAR, source, args);
		sendEvent(event);
	}

	// --------------------------------------------
	// Property with List value
	// --------------------------------------------
	@Override
	public void sendSetPropertyListAddAll(Object source, Object propName, List<Object> list) throws Exception {
		if (list == null || list.isEmpty()) {
			return;
		}

		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.LIST, list);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_LIST_ADDALL, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyListAdd(Object source, Object propName, Object listItem) throws Exception {
		if (listItem == null) {
			return;
		}

		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.LIST_ITEM, listItem);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_LIST_ADD, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyListRemoveAll(Object source, Object propName, List<Object> list) throws Exception {
		if (list == null || list.isEmpty()) {
			return;
		}

		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.LIST, list);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_LIST_REMOVEALL, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyListRemove(Object source, Object propName, Object listItem) throws Exception {
		if (listItem == null) {
			return;
		}

		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);
		args.put(EventConstants.LIST_ITEM, listItem);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_LIST_REMOVE, source, args);
		sendEvent(event);
	}

	@Override
	public void sendSetPropertyListClear(Object source, Object propName) throws Exception {
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.put(EventConstants.PROP_NAME, propName);

		Event event = new Event(EventConstants.CLUSTER_EVENT_GROUP_PROPERTIES, EventConstants.SET_PROPERTY_LIST_CLEAR, source, args);
		sendEvent(event);
	}

	// -------------------------------------------------------
	// Local Notification
	// -------------------------------------------------------
	/**
	 * 
	 * @param context
	 * @param event
	 */
	protected void localNotifyEvent(EventContext context, Event event) {
		synchronized (this.eventListeners) {
			for (EventListener listener : eventListeners) {
				listener.onReceiveEvent(context, event);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param properties
	 */
	protected void localNotifySetProperties(EventContext context, Map<Object, Object> properties) {
		synchronized (this.propertyChangeListeners) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.onSetProperties(context, properties);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	protected void localNotifySetProperty(EventContext context, Object key, Object value) {
		synchronized (this.propertyChangeListeners) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.onSetProperty(context, key, value);
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param key
	 */
	protected void localNotifyRemoveProperty(EventContext context, Object key) {
		synchronized (this.propertyChangeListeners) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.onRemoveProperty(context, key);
			}
		}
	}

	/**
	 * 
	 * @param context
	 */
	protected void localNotifyClearProperty(EventContext context) {
		synchronized (this.propertyChangeListeners) {
			for (PropertyChangeListener listener : this.propertyChangeListeners) {
				listener.onClearProperty(context);
			}
		}
	}

}

// System.out.println("\tEvent is sent successfully.");
// if (responseList != null) {
// System.out.println("\tresponse size: " + responseList.size());
// for (Iterator<Address> addrItor = responseList.keySet().iterator(); addrItor.hasNext();) {
// Address address = addrItor.next();
// Rsp<?> response = responseList.get(address);
//
// Address sender = response.getSender();
// Object responseValue = response.getValue();
// Throwable exception = response.getException();
// System.out.println("\t\tsender = " + sender);
// System.out.println("\t\tresponseValue = " + responseValue);
// System.out.println("\t\texception = " + exception);
// }
// }