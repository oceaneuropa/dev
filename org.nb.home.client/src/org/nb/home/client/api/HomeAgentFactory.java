package org.nb.home.client.api;

import org.nb.home.client.api.impl.HomeAgentImpl;
import org.origin.common.rest.client.ClientConfiguration;

public class HomeAgentFactory {

	/**
	 * Create Home agent client.
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static HomeAgent createHomeAgent(String url, String username, String password) {
		return new HomeAgentImpl(url, "home/v1", username, password);
	}

	/**
	 * Create Home agent client.
	 * 
	 * @param clientConfig
	 * @return
	 */
	public static HomeAgent createHomeAgent(ClientConfiguration clientConfig) {
		return new HomeAgentImpl(clientConfig);
	}

}
