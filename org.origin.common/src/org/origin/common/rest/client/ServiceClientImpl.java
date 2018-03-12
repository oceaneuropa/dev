package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.model.Request;
import org.origin.common.service.InternalProxyService;

public abstract class ServiceClientImpl<SERVICE, WS_CLIENT extends AbstractWSClient> implements ServiceClient, InternalProxyService {

	protected Map<String, Object> properties;
	protected WS_CLIENT client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();
	protected boolean isProxy = false;

	/**
	 * 
	 * @param properties
	 */
	public ServiceClientImpl(Map<String, Object> properties) {
		this(null, properties);
	}

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public ServiceClientImpl(ServiceConnector<SERVICE> connector, Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		this.client = createWSClient(this.properties);

		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		this.client = createWSClient(this.properties);
	}

	protected abstract WS_CLIENT createWSClient(Map<String, Object> properties);

	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public String echo(String message) throws ClientException {
		return this.client.echo(message);
	}

	@Override
	public Response sendRequest(Request request) throws ClientException {
		return this.client.sendRequest(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean close() throws ClientException {
		ServiceConnector<SERVICE> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close((SERVICE) this);
		}
		return false;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

	@Override
	public boolean isProxy() {
		return this.isProxy;
	}

	@Override
	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}

}
