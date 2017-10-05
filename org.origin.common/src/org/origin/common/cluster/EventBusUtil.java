package org.origin.common.cluster;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.cluster.impl.jgroup.EventBusImpl;

public class EventBusUtil {

	public static String EVENT_BUS__JGROUP = "EVENT_BUS__JGROUP";

	private static Map<String, EventBus> eventBusMap = new HashMap<String, EventBus>();
	private static EventBus defaultEventBus = new EventBusImpl();

	public static EventBus getEventBus() {
		return getEventBus(EVENT_BUS__JGROUP);
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
					if (EVENT_BUS__JGROUP.equals(type)) {
						eventBus = defaultEventBus;
						eventBusMap.put(EVENT_BUS__JGROUP, eventBus);
					}
				}
			}
		}
		if (eventBus == null) {
			eventBus = defaultEventBus;
		}
		return eventBus;
	}

	/**
	 * 
	 * @param eventListener
	 * @param clusterNames
	 * @return
	 */
	public static boolean addEventListener(EventListener eventListener, String... clusterNames) {
		return getEventBus().addEventListener(eventListener, clusterNames);
	}

	/**
	 * 
	 * @param eventListener
	 * @param clusterNames
	 * @return
	 */
	public static boolean removeEventListener(EventListener eventListener, String... clusterNames) {
		return getEventBus().removeEventListener(eventListener, clusterNames);
	}

	/**
	 * 
	 * @param event
	 * @param clusterNames
	 */
	public static void sendEvent(Event event, String... clusterNames) {
		getEventBus().sendEvent(event, clusterNames);
	}

}
