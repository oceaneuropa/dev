package org.orbit.infra.connector.configregistry;

import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;

public class ConfigRegistryImpl implements ConfigRegistry {

	protected ConfigRegistryClient configRegistryClient;

	protected String id;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param configRegistryClient
	 */
	public ConfigRegistryImpl(ConfigRegistryClient configRegistryClient) {
		this.configRegistryClient = configRegistryClient;
	}

	/**
	 * 
	 * @param configRegistryClient
	 * @param id
	 * @param type
	 * @param name
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ConfigRegistryImpl(ConfigRegistryClient configRegistryClient, String id, String type, String name, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.configRegistryClient = configRegistryClient;

		this.id = id;
		this.type = type;
		this.name = name;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public ConfigRegistryClient getConfigRegistryClient() {
		return this.configRegistryClient;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	@Override
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
