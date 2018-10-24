package org.orbit.infra.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.origin.common.resource.Path;

public interface IConfigRegistry {

	CFG getCFG();

	ConfigRegistry getConfigRegistry();

	// -----------------------------------------------------------------------------------
	// Config registry (read)
	// -----------------------------------------------------------------------------------
	String getId();

	String getType();

	String getName();

	Map<String, Object> getProperties();

	long getDateCreated();

	long getDateModified();

	// -----------------------------------------------------------------------------------
	// Config registry (write)
	// -----------------------------------------------------------------------------------
	boolean sync() throws IOException;

	boolean updatedType(String newType) throws IOException;

	boolean rename(String newName) throws IOException;

	boolean setProperties(Map<String, Object> properties) throws IOException;

	boolean removeProperties(List<String> propertyNames) throws IOException;

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	IConfigElement[] listRootConfigElements() throws IOException;

	IConfigElement[] listConfigElements(String parentElementId) throws IOException;

	IConfigElement[] listConfigElements(Path parentPath) throws IOException;

	IConfigElement getConfigElement(String elementId) throws IOException;

	IConfigElement getConfigElement(Path path) throws IOException;

	IConfigElement getConfigElement(String parentElementId, String name) throws IOException;

	Path getConfigElementPath(String elementId) throws IOException;

	boolean configElementExists(String elementId) throws IOException;

	boolean configElementExists(Path path) throws IOException;

	boolean configElementExists(String parentElementId, String name) throws IOException;

	IConfigElement createConfigElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	IConfigElement createConfigElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	boolean updateConfigElementName(String elementId, String newName) throws IOException;

	boolean setConfigElementAttributes(String elementId, Map<String, Object> attributes) throws IOException;

	boolean removeConfigElementAttributes(String elementId, List<String> attributeName) throws IOException;

	boolean deleteConfigElement(String elementId) throws IOException;

	boolean deleteConfigElement(Path path) throws IOException;

}
