package org.orbit.infra.runtime.datatube.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.runtime.datatube.service.MessageListener;
import org.orbit.infra.runtime.datatube.service.MessageListenerSupport;

public class MessageListenerSupportImpl implements MessageListenerSupport {

	protected List<MessageListener> messageListeners = new ArrayList<MessageListener>();

	@Override
	public boolean containsMessageListener(MessageListener listener) {
		if (listener != null && this.messageListeners.contains(listener)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addMessageListener(MessageListener listener) {
		if (listener == null || this.messageListeners.contains(listener)) {
			return false;
		}
		return this.messageListeners.add(listener);
	}

	@Override
	public synchronized boolean removeMessageListener(MessageListener listener) {
		if (listener == null || !this.messageListeners.contains(listener)) {
			return false;
		}
		return this.messageListeners.remove(listener);
	}

	@Override
	public List<MessageListener> getMessageListeners() {
		return this.messageListeners;
	}

	@Override
	public void dispose() {
		this.messageListeners.clear();
	}

}
