package org.orbit.spirit.api.gaia;

import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.spirit.api.SpiritClients;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;

public class GAIAProxy implements GAIAClient {

	private static GAIAProxyImpl PROXY = new GAIAProxyImpl();

	protected Map<?, ?> properties;
	protected String realm;
	protected String accessToken;
	protected String url;

	protected GAIAClient gaia = PROXY;

	public GAIAProxy(Map<?, ?> properties) {
		this.properties = properties;
	}

	public GAIAProxy(String url) {
		this(null, null, url);
	}

	public GAIAProxy(String realm, String accessToken, String url) {
		this.realm = realm;
		this.accessToken = accessToken;
		this.url = url;
	}

	protected synchronized GAIAClient resolve() {
		if (this.gaia == null || this.gaia.isProxy()) {
			GAIAClient resolvedGAIA = null;
			if (this.properties != null) {
				resolvedGAIA = SpiritClients.getInstance().getGAIA(this.properties);
			}
			if (resolvedGAIA == null) {
				if (this.url != null) {
					resolvedGAIA = SpiritClients.getInstance().getGAIA(this.realm, this.accessToken, this.url);
				}
			}
			if (resolvedGAIA != null && !resolvedGAIA.isProxy()) {
				this.gaia = resolvedGAIA;
			}
		}
		if (this.gaia == null) {
			this.gaia = PROXY;
		}
		return this.gaia;
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
	public String getName() throws ClientException {
		return resolve().getName();
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

	public static class GAIAProxyImpl implements GAIAClient {

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
		public String getName() throws ClientException {
			return null;
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
