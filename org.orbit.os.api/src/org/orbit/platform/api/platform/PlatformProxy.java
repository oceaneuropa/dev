package org.orbit.platform.api.platform;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.platform.api.PlatformClients;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public class PlatformProxy implements PlatformClient {

	private static PlatformProxyImpl PROXY = new PlatformProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String username;
	protected String url;

	protected PlatformClient platform = PROXY;

	public PlatformProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	public PlatformProxy(String url) {
		this(null, null, url);
	}

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
