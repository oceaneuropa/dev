package org.orbit.component.connector.configregistry;

import java.util.Map;

import org.orbit.component.api.configregistry.ConfigRegistry;
import org.orbit.component.api.configregistry.EPath;
import org.origin.common.rest.client.ClientException;

public class ConfigRegistryImpl implements ConfigRegistry {

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
