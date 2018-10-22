package org.orbit.infra.runtime.configregistry.service;

import java.io.IOException;
import java.util.Map;

import org.origin.common.resource.Path;

public interface ConfigRegistry {

	ConfigRegistryService getConfigRegistryService();

	ConfigRegistryMetadata getMetadata();

	void setMetadata(ConfigRegistryMetadata metadata);

	String getId();

	String getType();

	String getName();

	ConfigElement[] listRoots() throws IOException;

	ConfigElement[] listElements(String parentElementId) throws IOException;

	ConfigElement[] listElements(Path parentPath) throws IOException;

	ConfigElement getElement(String elementId) throws IOException;

	ConfigElement getElement(Path path) throws IOException;

	ConfigElement getElement(String parentElementId, String name) throws IOException;

	Path getPath(String elementId) throws IOException;

	boolean exists(String elementId) throws IOException;

	boolean exists(Path path) throws IOException;

	ConfigElement createElement(Path path, Map<String, Object> attributes) throws IOException;

	ConfigElement createElement(String parentElementId, String name, Map<String, Object> attributes) throws IOException;

	boolean updateName(String elementId, String newName) throws IOException;

	boolean updateAttributes(String configId, Map<String, Object> attributes) throws IOException;

	boolean delete(String elementId) throws IOException;

	boolean delete(Path path) throws IOException;

}
