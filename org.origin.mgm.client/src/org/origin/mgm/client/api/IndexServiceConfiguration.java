package org.origin.mgm.client.api;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.rest.client.ClientConfiguration;
import org.origin.mgm.client.ws.IndexServiceClient;

public class IndexServiceConfiguration {

	protected String url;
	protected String contextRoot;
	protected String username;
	protected String password;

	protected Map<ClientConfiguration, IndexServiceClient> clientMap = new HashMap<ClientConfiguration, IndexServiceClient>();

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public IndexServiceConfiguration(String url, String username, String password) {
		this(url, "/indexservice/v1", username, password);
	}

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public IndexServiceConfiguration(String url, String contextRoot, String username, String password) {
		this.url = url;
		this.contextRoot = contextRoot;
		this.username = username;
		this.password = password;
	}

	public synchronized IndexServiceClient getIndexServiceClient() {
		ClientConfiguration config = ClientConfiguration.get(url, contextRoot, username, password);
		IndexServiceClient fsClient = this.clientMap.get(config);
		if (fsClient == null) {
			fsClient = new IndexServiceClient(config);
			this.clientMap.put(config, fsClient);
		}
		return fsClient;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}