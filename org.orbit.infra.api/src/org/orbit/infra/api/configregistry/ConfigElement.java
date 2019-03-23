package org.orbit.infra.api.configregistry;

import java.util.Map;

import org.origin.common.resource.Path;

public interface ConfigElement {

	ConfigRegistryClient getConfigRegistryClient();

	String getConfigRegistryId();

	String getParentElementId();

	void setParentElementId(String parentElementId);

	String getElementId();

	void setElementId(String elementId);

	Path getPath();

	void setPath(Path path);

	String getName();

	void setName(String name);

	String[] getAttributeNames();

	Map<String, Object> getAttributes();

	Object getAttribute(String attrName);

	<T> T getAttribute(String attrName, Class<T> attrValueClass);

	void setAttributes(Map<String, Object> attributes);

	long getDateCreated();

	void setDateCreated(long dateCreated);

	long getDateModified();

	void setDateModified(long dateModified);

}
