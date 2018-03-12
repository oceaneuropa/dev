package org.orbit.platform.api.impl;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.platform.api.Clients;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.model.dto.ExtensionInfo;
import org.orbit.platform.model.dto.ProcessInfo;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public class PlatformProxy implements PlatformClient {

	private static PlatformProxyImpl PROXY = new PlatformProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String username;
	protected String url;

	protected PlatformClient platform = PROXY;

	/**
	 * 
	 * @param properties
	 */
	public PlatformProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	/**
	 * 
	 * @param url
	 */
	public PlatformProxy(String url) {
		this(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 */
	public PlatformProxy(String realm, String username, String url) {
		this.realm = realm;
		this.username = username;
		this.url = url;
	}

	protected synchronized PlatformClient resolve() {
		if (this.platform == null || this.platform.isProxy()) {
			PlatformClient resolvedPlatform = null;
			if (this.properties != null) {
				resolvedPlatform = Clients.getInstance().getPlatformClient(this.properties);
			}
			if (resolvedPlatform == null) {
				if (this.url != null) {
					resolvedPlatform = Clients.getInstance().getPlatformClient(this.realm, this.username, this.url);
				}
			}
			if (resolvedPlatform != null && !resolvedPlatform.isProxy()) {
				this.platform = resolvedPlatform;
			}
		}
		if (this.platform == null) {
			this.platform = PROXY;
		}
		return this.platform;
	}

	@Override
	public boolean close() throws ClientException {
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return resolve().getProperties();
	}

	@Override
	public void update(Map<String, Object> properties) {
		resolve().update(properties);
	}

	@Override
	public String getURL() {
		return resolve().getURL();
	}

	@Override
	public boolean ping() {
		return resolve().ping();
	}

	@Override
	public String echo(String message) throws ClientException {
		return resolve().echo(message);
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return resolve().sendRequest(request);
	}

	@Override
	public List<ExtensionInfo> getExtensions() throws ClientException {
		return resolve().getExtensions();
	}

	@Override
	public List<ExtensionInfo> getExtensions(String extensionTypeId) throws ClientException {
		return resolve().getExtensions(extensionTypeId);
	}

	@Override
	public ExtensionInfo getExtension(String extensionTypeId, String extensionId) throws ClientException {
		return resolve().getExtension(extensionTypeId, extensionId);
	}

	@Override
	public List<ProcessInfo> getProcesses() throws ClientException {
		return resolve().getProcesses();
	}

	@Override
	public List<ProcessInfo> getProcesses(String extensionTypeId) throws ClientException {
		return resolve().getProcesses(extensionTypeId);
	}

	@Override
	public List<ProcessInfo> getProcesses(String extensionTypeId, String extensionId) throws ClientException {
		return resolve().getProcesses(extensionTypeId, extensionId);
	}

	@Override
	public ProcessInfo startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
		return resolve().startService(extensionTypeId, extensionId, properties);
	}

	@Override
	public boolean stopService(int pid) throws ClientException {
		return resolve().stopService(pid);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		resolve().adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		resolve().adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return resolve().getAdapter(adapter);
	}

	@Override
	public boolean isProxy() {
		return false;
	}

	public static class PlatformProxyImpl implements PlatformClient {

		@Override
		public boolean close() throws ClientException {
			return false;
		}

		@Override
		public Map<String, Object> getProperties() {
			return null;
		}

		@Override
		public void update(Map<String, Object> properties) {
		}

		@Override
		public String getURL() {
			return null;
		}

		@Override
		public boolean ping() {
			return false;
		}

		@Override
		public String echo(String message) throws ClientException {
			return null;
		}

		@Override
		public Response sendRequest(Request request) throws ClientException {
			return null;
		}

		@Override
		public List<ExtensionInfo> getExtensions() throws ClientException {
			return null;
		}

		@Override
		public List<ExtensionInfo> getExtensions(String extensionTypeId) {
			return null;
		}

		@Override
		public ExtensionInfo getExtension(String extensionTypeId, String extensionId) {
			return null;
		}

		@Override
		public List<ProcessInfo> getProcesses() throws ClientException {
			return null;
		}

		@Override
		public List<ProcessInfo> getProcesses(String extensionTypeId) {
			return null;
		}

		@Override
		public List<ProcessInfo> getProcesses(String extensionTypeId, String extensionId) {
			return null;
		}

		@Override
		public ProcessInfo startService(String extensionTypeId, String extensionId, Map<String, Object> properties) {
			return null;
		}

		@Override
		public boolean stopService(int pid) {
			return false;
		}

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
		}

		@Override
		public <T> void adapt(Class<T>[] classes, T object) {
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return null;
		}

		@Override
		public boolean isProxy() {
			return true;
		}
	}

}
