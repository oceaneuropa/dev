package org.orbit.fs.connector;

import java.util.HashMap;
import java.util.Map;

import org.orbit.fs.api.FileSystemConfiguration;
import org.orbit.fs.connector.ws.FileSystemWSClient;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;

public class FileSystemConfigurationImpl implements FileSystemConfiguration {

	protected Map<ClientConfiguration, FileSystemWSClient> clientMap = new HashMap<ClientConfiguration, FileSystemWSClient>();

	protected String url;
	protected String contextRoot;
	protected String username;
	protected String password;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public FileSystemConfigurationImpl(String url, String username, String password) {
		this(url, "/fs/v1", username, password);
	}

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public FileSystemConfigurationImpl(String url, String contextRoot, String username, String password) {
		this.url = url;
		this.contextRoot = contextRoot;
		this.username = username;
		this.password = password;
	}

	public synchronized FileSystemWSClient getFileSystemClient() {
		ClientConfiguration config = ClientConfiguration.create("orbit", this.username, this.url, this.contextRoot);
		FileSystemWSClient fsClient = this.clientMap.get(config);
		if (fsClient == null) {
			fsClient = new FileSystemWSClient(config);
			this.clientMap.put(config, fsClient);
		}
		return fsClient;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getContextRoot() {
		return contextRoot;
	}

	@Override
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		if (FileSystemWSClient.class.isAssignableFrom(adapter)) {
			return (T) this.getFileSystemClient();
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
