package org.orbit.infra.runtime.datatube.service;

public interface MessageListener {

	void onMessage(String senderId, String message);

}
