package org.nb.home.client.api.impl;

import org.nb.home.client.api.IHomeControl;
import org.nb.home.client.ws.HomeControlClient;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

public class HomeControlImpl implements IHomeControl {

	private ClientConfiguration clientConfiguration;
	private HomeControlClient homeControlClient;
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public HomeControlImpl(String url, String contextRoot, String username, String password) {
		this.clientConfiguration = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfiguration.setPassword(password);

		// Web service client for Home API
		this.homeControlClient = new HomeControlClient(this.clientConfiguration);
	}

	// ------------------------------------------------------------------------------------------
	// Home control
	// ------------------------------------------------------------------------------------------
	/**
	 * Ping the Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public int ping() throws ClientException {
		int ping = 0;
		HomeControlClient homeControlClient = getAdapter(HomeControlClient.class);
		checkClient(homeControlClient);

		ping = homeControlClient.ping();
		return ping;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(HomeControlClient homeControlClient) throws ClientException {
		if (homeControlClient == null) {
			throw new ClientException(401, "HomeControlClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ClientConfiguration.class.equals(adapter)) {
			return (T) this.clientConfiguration;
		}
		if (HomeControlClient.class.equals(adapter)) {
			return (T) this.homeControlClient;
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
