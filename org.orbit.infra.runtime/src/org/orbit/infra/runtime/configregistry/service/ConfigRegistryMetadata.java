package org.orbit.infra.runtime.configregistry.service;

import java.util.Map;

public interface ConfigRegistryMetadata {

	String getId();

	void setId(String id);

	String getType();

	void setType(String type);

	String getName();

	void setName(String name);

	Map<String, Object> getProperties();

	void setProperties(Map<String, Object> properties);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
