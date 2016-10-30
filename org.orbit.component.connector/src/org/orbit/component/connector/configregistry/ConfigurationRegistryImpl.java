package org.orbit.component.connector.configregistry;

import java.util.Map;

import org.orbit.component.api.configregistry.ConfigurationRegistry;
import org.orbit.component.api.configregistry.EPath;
import org.origin.common.rest.client.ClientException;

public class ConfigurationRegistryImpl implements ConfigurationRegistry {

	protected String userId;

	/**
	 * 
	 * @param userId
	 */
	public ConfigurationRegistryImpl(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	@Override
	public Map<String, String> getProperties(EPath path) throws ClientException {
		return null;
	}

	@Override
	public String getProperty(EPath path, String key) throws ClientException {
		return null;
	}

	@Override
	public void setProperties(EPath path, Map<String, String> properties) throws ClientException {

	}

	@Override
	public void setProperty(EPath path, String key, String value) throws ClientException {

	}

	@Override
	public void removeProperty(EPath path, String key) throws ClientException {

	}

	@Override
	public void removeAll(EPath path) throws ClientException {

	}

	@Override
	public void removeAll() throws ClientException {

	}

}
