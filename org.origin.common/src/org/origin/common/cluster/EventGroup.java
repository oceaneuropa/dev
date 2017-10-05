package org.origin.common.cluster;

public interface EventGroup {

	void start() throws Exception;

	void stop() throws Exception;

	boolean addEventListener(EventListener eventListener);

	boolean removeEventListener(EventListener eventListener);

	EventListener[] getEventListeners();

	void sendEvent(Event event) throws Exception;

}
