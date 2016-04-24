package org.nb.mgm.home.client.api.impl;

import org.nb.mgm.home.client.api.HomeManagement;
import org.nb.mgm.home.client.ws.HomeApiClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public class HomeManagementImpl implements HomeManagement {

	private ClientConfiguration clientConfiguration;
	private HomeApiClient homeApiClient;

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public HomeManagementImpl(String url, String contextRoot, String username, String password) {
		this.clientConfiguration = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfiguration.setPassword(password);

		// Web service client for Home API
		this.homeApiClient = new HomeApiClient(this.clientConfiguration);
	}

	// ------------------------------------------------------------------------------------------
	// Home management API
	// ------------------------------------------------------------------------------------------
	/**
	 * Ping the Home management server.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public int ping() throws ClientException {
		int ping = 0;
		HomeApiClient homeApiClient = this.getAdapter(HomeApiClient.class);
		checkClient(homeApiClient);

		ping = homeApiClient.ping();
		return ping;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(HomeApiClient homeApiClient) throws ClientException {
		if (homeApiClient == null) {
			throw new ClientException(401, "HomeApiClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (HomeApiClient.class.equals(adapter)) {
			return (T) this.homeApiClient;
		}
		return null;
	}

}
