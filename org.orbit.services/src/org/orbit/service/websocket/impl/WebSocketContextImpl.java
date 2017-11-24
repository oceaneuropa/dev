package org.orbit.service.websocket.impl;

import org.orbit.service.websocket.WebSocketContext;
import org.osgi.framework.Bundle;

public class WebSocketContextImpl implements WebSocketContext {

	protected Bundle bundle;

	public WebSocketContextImpl() {
	}

	@Override
	public Bundle getBundle() {
		return this.bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

}
