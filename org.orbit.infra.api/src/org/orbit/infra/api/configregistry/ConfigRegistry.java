package org.orbit.infra.api.configregistry;

import java.util.Map;

public interface ConfigRegistry {

	ConfigRegistryClient getClient();

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
