package org.orbit.infra.runtime.datatube.service;

import java.util.List;

public interface MessageListenerSupport {

	boolean containsMessageListener(MessageListener listener);

	boolean addMessageListener(MessageListener listener);

	boolean removeMessageListener(MessageListener listener);

	List<MessageListener> getMessageListeners();

	void dispose();

}
