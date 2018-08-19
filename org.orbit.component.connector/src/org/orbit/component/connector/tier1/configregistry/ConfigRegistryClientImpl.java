package org.orbit.component.connector.tier1.configregistry;

import java.util.Map;

import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.api.tier1.configregistry.EPath;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;

public class ConfigRegistryClientImpl extends ServiceClientImpl<ConfigRegistryClient, ConfigRegistryWSClient> implements ConfigRegistryClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public ConfigRegistryClientImpl(ServiceConnector<ConfigRegistryClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected ConfigRegistryWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration clientConfig = WSClientConfiguration.create(properties);
		return new ConfigRegistryWSClient(clientConfig);
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

}
