package org.origin.common.cluster;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.cluster.impl.jgroup.EventBusImpl;

public class EventBusUtil {

	public static String TYPE_JGROUP = "TYPE_JGROUP";

	private static Map<String, EventBus> eventBusMap = new HashMap<String, EventBus>();
	private static EventBus defaultEventBus = new EventBusImpl();

	public static EventBus getEventBus() {
		return getEventBus(TYPE_JGROUP);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static EventBus getEventBus(String type) {
		EventBus eventBus = null;
		if (type != null) {
			synchronized (eventBusMap) {
				eventBus = eventBusMap.get(type);
				if (eventBus == null) {
					if (TYPE_JGROUP.equals(type)) {
						eventBus = defaultEventBus;
						eventBusMap.put(TYPE_JGROUP, eventBus);
					}
				}
			}
		}
		if (eventBus == null) {
			eventBus = defaultEventBus;
		}
		return eventBus;
	}

}
