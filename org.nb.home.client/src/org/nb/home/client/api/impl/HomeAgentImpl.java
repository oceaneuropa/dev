package org.nb.home.client.api.impl;

import java.util.concurrent.TimeUnit;

import org.nb.home.client.api.HomeAgent;
import org.nb.home.client.ws.HomeAgentWSClient;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientConnector;
import org.origin.common.rest.client.ClientException;

public class HomeAgentImpl implements HomeAgent {

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	protected ClientConfiguration clientConfig;
	protected HomeAgentWSClient homeAdminWSClient;
	protected ClientConnector clientConnector;

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public HomeAgentImpl(String url, String contextRoot, String username, String password) {
		this.clientConfig = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfig.setPassword(password);

		// Web service client for Home API
		this.homeAdminWSClient = new HomeAgentWSClient(this.clientConfig);
	}

	/**
	 * 
	 * @param clientConfig
	 */
	public HomeAgentImpl(ClientConfiguration clientConfig) {
		this.clientConfig = clientConfig;

		// Web service client for Home API
		this.homeAdminWSClient = new HomeAgentWSClient(this.clientConfig);
	}

	// ------------------------------------------------------------------------------------------
	// Home Admin
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

		ping = homeAdminWSClient.ping();
		return ping;
	}

	/**
	 * Connect to remote Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean connect() throws ClientException {
		if (this.clientConnector == null) {
			// Ping the remote home every 10 seconds
			// this.clientConnector = new ClientConnector(this.homeAdminWSClient, 10, TimeUnit.SECONDS);
		}
		this.clientConnector.start();
		return isConnected();
	}

	/**
	 * Disconnect from remote Home.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean disconnect() throws ClientException {
		if (this.clientConnector != null) {
			this.clientConnector.stop();
			this.clientConnector.dispose();
			this.clientConnector = null;
		}
		return true;
	}

	@Override
	public boolean isConnected() throws ClientException {
		if (this.clientConnector != null) {
			boolean isStarted = this.clientConnector.isStarted();
			if (isStarted) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether remote home is still active.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public synchronized boolean isActive() throws ClientException {
		if (this.clientConnector != null && this.clientConnector.isStarted()) {
			// Remote home is considered to be not alive if there is no ping response in 10 seconds.
			boolean isExpired = this.clientConnector.isExpired(20, TimeUnit.SECONDS);
			if (!isExpired) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized void buildProject(String projectId, String projectHomeId) throws ClientException {
		checkConnection();

	}

	protected void checkConnection() throws ClientException {
		if (!isConnected()) {
			throw new ClientException(500, "Connection is not open.");
		}
		if (!isActive()) {
			throw new ClientException(500, "Home agent is not active.");
		}
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(HomeAgentWSClient homeAdminClient) throws ClientException {
		if (homeAdminClient == null) {
			throw new ClientException(401, "HomeAdminClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ClientConfiguration.class.equals(adapter)) {
			return (T) this.clientConfig;
		}
		if (HomeAgentWSClient.class.equals(adapter)) {
			return (T) this.homeAdminWSClient;
		}
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
