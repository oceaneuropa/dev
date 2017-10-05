package org.origin.common.cluster;

public interface EventListener {

	void receivedEvent(EventContext context, Event event);

}
