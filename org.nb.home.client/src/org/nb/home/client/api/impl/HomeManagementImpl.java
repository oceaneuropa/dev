package org.nb.home.client.api.impl;

import org.nb.home.client.api.HomeManagement;
import org.nb.home.client.ws.HomeApiClient;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public class HomeManagementImpl implements HomeManagement {

	private ClientConfiguration clientConfiguration;
	private HomeApiClient homeApiClient;
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

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
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

}
