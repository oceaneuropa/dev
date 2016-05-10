package com.osgi.example1.fs.client.api;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.rest.client.ClientConfiguration;

import com.osgi.example1.fs.client.ws.FileSystemClient;
import com.osgi.example1.fs.common.Configuration;

public class FileSystemConfiguration extends Configuration {

	protected String url;
	protected String contextRoot;
	protected String username;
	protected String password;

	protected Map<ClientConfiguration, FileSystemClient> clientMap = new HashMap<ClientConfiguration, FileSystemClient>();

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public FileSystemConfiguration(String url, String contextRoot, String username, String password) {
		this.url = url;
		this.contextRoot = contextRoot;
		this.username = username;
		this.password = password;
	}

	public synchronized FileSystemClient getFileSystemClient() {
		ClientConfiguration config = ClientConfiguration.get(url, contextRoot, username, password);
		FileSystemClient fsClient = this.clientMap.get(config);
		if (fsClient == null) {
			fsClient = new FileSystemClient(config);
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
