package org.orbit.infra.io.configregistry;

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

	boolean setProperty(String oldName, String name, Object value) throws IOException;

	boolean setProperties(Map<String, Object> properties) throws IOException;

	boolean removeProperties(List<String> propertyNames) throws IOException;

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	IConfigElement[] listRootElements() throws IOException;

	IConfigElement getRootElement(String name) throws IOException;

	IConfigElement[] listElements(String parentElementId) throws IOException;

	IConfigElement[] listElements(Path parentPath) throws IOException;

	IConfigElement getElement(String elementId) throws IOException;

	IConfigElement getElement(Path path) throws IOException;

	IConfigElement getElement(String parentElementId, String name) throws IOException;

	IConfigElement getElement(String parentElementId, Path path) throws IOException;

	Path getElementPath(String elementId) throws IOException;

	boolean elementExists(String elementId) throws IOException;

	boolean elementExists(Path path) throws IOException;

	boolean elementExists(String parentElementId, String name) throws IOException;

	IConfigElement createRootElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	IConfigElement createElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	IConfigElement createElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	IConfigElement createElement(String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	boolean updateElementName(String elementId, String newName) throws IOException;

	boolean setElementAttribute(String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws IOException;

	boolean setElementAttributes(String elementId, Map<String, Object> attributes) throws IOException;

	boolean removeElementAttribute(String elementId, String attributeName) throws IOException;

	boolean removeElementAttributes(String elementId, List<String> attributeNames) throws IOException;

	boolean deleteElement(String elementId) throws IOException;

	boolean deleteElement(Path path) throws IOException;

}
