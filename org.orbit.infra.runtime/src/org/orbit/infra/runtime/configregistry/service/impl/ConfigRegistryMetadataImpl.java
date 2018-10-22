package org.orbit.infra.runtime.configregistry.service.impl;

import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryMetadata;

public class ConfigRegistryMetadataImpl implements ConfigRegistryMetadata {

	protected String id;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public ConfigRegistryMetadataImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @param name
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ConfigRegistryMetadataImpl(String id, String type, String name, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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
