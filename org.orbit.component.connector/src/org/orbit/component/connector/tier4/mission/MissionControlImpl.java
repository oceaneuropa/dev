package org.orbit.component.connector.tier4.mission;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.component.api.tier4.mission.MissionControl;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.model.Request;

public class MissionControlImpl implements MissionControl {

	protected Map<String, Object> properties;
	protected MissionControlWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public MissionControlImpl(ServiceConnector<MissionControl> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	private Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<MissionControl> connector = getAdapter(ServiceConnector.class);
		if (connector != null) {
			return connector.close(this);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void update(Map<String, Object> properties) {
		this.properties = checkProperties(properties);
		initClient();
	}

	protected void initClient() {
		String realm = (String) this.properties.get(OrbitConstants.REALM);
		String username = (String) this.properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) this.properties.get(OrbitConstants.URL);

		ClientConfiguration config = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new MissionControlWSClient(config);
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
	}

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

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
