package org.orbit.infra.runtime.configregistry.service;

import java.io.IOException;
import java.util.Map;

import org.origin.common.resource.Path;

public interface ConfigRegistry {

	ConfigRegistryService getService();

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

	ConfigElement getElement(String parentElementId, Path path) throws IOException;

	Path getPath(String elementId) throws IOException;

	boolean elementExists(String elementId) throws IOException;

	boolean elementExists(Path path) throws IOException;

	boolean elementExists(String parentElementId, String name) throws IOException;

	ConfigElement createElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	ConfigElement createElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	ConfigElement createElement(String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	boolean updateName(String elementId, String newName) throws IOException;

	boolean updateAttributes(String elementId, Map<String, Object> attributes) throws IOException;

	boolean deleteElement(String elementId) throws IOException;

	boolean deleteElement(Path path) throws IOException;

}
