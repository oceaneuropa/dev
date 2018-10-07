package org.orbit.infra.runtime.datatube.service;

import java.io.IOException;

public interface Channel extends MessageListenerSupport {

	int onMessage(String senderId, String message) throws IOException;

	void dispose();

}
