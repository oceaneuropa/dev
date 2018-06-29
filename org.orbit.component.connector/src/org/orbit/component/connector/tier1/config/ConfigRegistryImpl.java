package org.orbit.component.connector.tier1.config;

import java.util.HashMap;
import java.util.Map;

import org.orbit.component.api.tier1.registry.Registry;
import org.orbit.component.api.tier1.registry.EPath;
import org.orbit.component.connector.OrbitConstants;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceConnector;

public class ConfigRegistryImpl implements Registry {

	protected Map<String, Object> properties;
	protected ConfigRegistryWSClient client;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public ConfigRegistryImpl(ServiceConnector<Registry> connector, Map<String, Object> properties) {
		if (connector != null) {
			adapt(ServiceConnector.class, connector);
		}
		this.properties = checkProperties(properties);
		initClient();
	}

	protected Map<String, Object> checkProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean close() throws ClientException {
		@SuppressWarnings("unchecked")
		ServiceConnector<Registry> connector = getAdapter(ServiceConnector.class);
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
		String realm = (String) properties.get(OrbitConstants.REALM);
		String username = (String) properties.get(OrbitConstants.USERNAME);
		String fullUrl = (String) properties.get(OrbitConstants.URL);

		ClientConfiguration clientConfig = ClientConfiguration.create(realm, username, fullUrl);
		this.client = new ConfigRegistryWSClient(clientConfig);
	}

	// ------------------------------------------------------------------------------------------------
	// Configuration methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public String getName() {
		String name = (String) this.properties.get(OrbitConstants.CONFIG_REGISTRY_NAME);
		return name;
	}

	@Override
	public String getURL() {
		String fullUrl = (String) properties.get(OrbitConstants.URL);
		return fullUrl;
	}

	// ------------------------------------------------------------------------------------------------
	// Web service client methods
	// ------------------------------------------------------------------------------------------------
	@Override
	public boolean ping() {
		return this.client.doPing();
	}

	@Override
	public Map<String, String> getProperties(String userId, EPath path) throws ClientException {
		return null;
	}

	@Override
	public String getProperty(String userId, EPath path, String key) throws ClientException {
		return null;
	}

	@Override
	public void setProperties(String userId, EPath path, Map<String, String> properties) throws ClientException {

	}

	@Override
	public void setProperty(String userId, EPath path, String key, String value) throws ClientException {

	}

	@Override
	public void removeProperty(String userId, EPath path, String key) throws ClientException {

	}

	@Override
	public void removeAll(String userId, EPath path) throws ClientException {

	}

	@Override
	public void removeAll(String userId) throws ClientException {

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

}
