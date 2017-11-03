package org.origin.common.cluster;

public interface EventListener {

	/**
	 * 
	 * @param context
	 * @param event
	 */
	void onReceiveEvent(EventContext context, Event event);

}
