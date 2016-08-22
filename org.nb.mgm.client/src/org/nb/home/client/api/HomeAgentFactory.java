package org.nb.home.client.api;

import org.nb.home.client.api.impl.HomeAgentImpl;
import org.nb.mgm.client.api.ManagementClient;
import org.origin.common.rest.client.ClientConfiguration;

public class HomeAgentFactory {

	/**
	 * Create Home agent client.
	 * 
	 * @param managementClient
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static HomeAgent createHomeAgent(ManagementClient managementClient, String url, String username, String password) {
		HomeAgentImpl agent = new HomeAgentImpl(url, "home/v1", username, password);
		agent.setManagementClient(managementClient);
		return agent;
	}

	/**
	 * Create Home agent client.
	 * 
	 * @param managementClient
	 * @param clientConfig
	 * @return
	 */
	public static HomeAgent createHomeAgent(ManagementClient managementClient, ClientConfiguration clientConfig) {
		HomeAgentImpl agent = new HomeAgentImpl(clientConfig);
		agent.setManagementClient(managementClient);
		return agent;
	}

}
