package org.origin.common.cluster;

public interface EventBus {

	public static final String GLOBAL_EVENTS_GROUP = "_GLOBAL_EVENTS_GROUP";

	/**
	 * Add event listener to specified clusters.
	 * 
	 * @param eventListener
	 * @param clusterNames
	 * @return
	 */
	boolean addEventListener(EventListener eventListener, String... clusterNames);

	/**
	 * Remove event listener from specified clusters.
	 * 
	 * @param eventListener
	 * @param clusterNames
	 * @return
	 */
	boolean removeEventListener(EventListener eventListener, String... clusterNames);

	/**
	 * 
	 * @param event
	 * @param clusterNames
	 */
	void sendEvent(Event event, String... clusterNames);

}
