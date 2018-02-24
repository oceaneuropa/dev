package org.orbit.platform.api.platform;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.platform.api.PlatformClients;
import org.orbit.platform.model.platform.dto.ServiceExtensionInfo;
import org.orbit.platform.model.platform.dto.ServiceInstanceInfo;
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
				resolvedPlatform = PlatformClients.getInstance().getPlatform(this.properties);
			}
			if (resolvedPlatform == null) {
				if (this.url != null) {
					resolvedPlatform = PlatformClients.getInstance().getPlatform(this.realm, this.username, this.url);
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
	public List<String> getExtensionTypeIds() throws ClientException {
		return resolve().getExtensionTypeIds();
	}

	@Override
	public List<ServiceExtensionInfo> getServiceExtensions(String extensionTypeId) throws ClientException {
		return resolve().getServiceExtensions(extensionTypeId);
	}

	@Override
	public ServiceExtensionInfo getServiceExtension(String extensionTypeId, String extensionId) throws ClientException {
		return resolve().getServiceExtension(extensionTypeId, extensionId);
	}

	@Override
	public List<ServiceInstanceInfo> getServiceInstances(String extensionTypeId) throws ClientException {
		return resolve().getServiceInstances(extensionTypeId);
	}

	@Override
	public ServiceInstanceInfo getServiceInstance(String extensionTypeId, String extensionId) throws ClientException {
		return resolve().getServiceInstance(extensionTypeId, extensionId);
	}

	@Override
	public boolean startService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
		return resolve().startService(extensionTypeId, extensionId, properties);
	}

	@Override
	public boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> properties) throws ClientException {
		return resolve().stopService(extensionTypeId, extensionId, properties);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		resolve().adapt(clazz, object);
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
		public List<String> getExtensionTypeIds() throws ClientException {
			return null;
		}

		@Override
		public List<ServiceExtensionInfo> getServiceExtensions(String extensionTypeId) {
			return null;
		}

		@Override
		public ServiceExtensionInfo getServiceExtension(String extensionTypeId, String extensionId) {
			return null;
		}

		@Override
		public List<ServiceInstanceInfo> getServiceInstances(String extensionTypeId) {
			return null;
		}

		@Override
		public ServiceInstanceInfo getServiceInstance(String extensionTypeId, String extensionId) {
			return null;
		}

		@Override
		public boolean startService(String extensionTypeId, String extensionId, Map<String, Object> properties) {
			return false;
		}

		@Override
		public boolean stopService(String extensionTypeId, String extensionId, Map<String, Object> properties) {
			return false;
		}

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
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
