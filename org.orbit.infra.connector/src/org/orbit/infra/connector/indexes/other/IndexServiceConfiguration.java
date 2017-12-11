package org.orbit.infra.connector.indexes.other;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.connector.indexes.IndexServiceWSClient;
import org.origin.common.rest.client.ClientConfiguration;

public class IndexServiceConfiguration {

	protected String url;
	protected String contextRoot;
	protected String username;
	protected String password;

	protected Map<ClientConfiguration, IndexServiceWSClient> clientMap = new HashMap<ClientConfiguration, IndexServiceWSClient>();

	/**
	 * 
	 * @param fullUrl
	 *            full URL string with context root.
	 */
	public IndexServiceConfiguration(String fullUrl) {
		try {
			URI uri = URI.create(fullUrl);
			if (uri != null) {
				String path = uri.getPath();
				if (path != null && !path.isEmpty()) {
					this.url = fullUrl.substring(0, fullUrl.indexOf(path));
					this.contextRoot = path;
				} else {
					this.url = fullUrl;
					this.contextRoot = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 */
	public IndexServiceConfiguration(String url, String contextRoot) {
		this.url = url;
		this.contextRoot = contextRoot;
	}

	public synchronized IndexServiceWSClient getClient() {
		ClientConfiguration config = ClientConfiguration.get(this.url, this.contextRoot, this.username, this.password);
		IndexServiceWSClient fsClient = this.clientMap.get(config);
		if (fsClient == null) {
			fsClient = new IndexServiceWSClient(config);
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
