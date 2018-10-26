package org.orbit.infra.io;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.resource.Path;

public interface IConfigElement extends IAdaptable {

	CFG getCFG();

	IConfigRegistry getIConfigRegistry();

	ConfigElement getConfigElement();

	// -----------------------------------------------------------------------------------
	// Config Element (read)
	// -----------------------------------------------------------------------------------
	String getConfigRegistryId();

	String getParentElementId();

	String getElementId();

	Path getPath();

	String getName();

	String[] getAttributeNames();

	Map<String, Object> getAttributes();

	<T> T getAttribute(String attrName, Class<T> attrValueClass);

	long getDateCreated();

	long getDateModified();

	// -----------------------------------------------------------------------------------
	// Config element (write)
	// -----------------------------------------------------------------------------------
	boolean sync() throws IOException;

	boolean rename(String newName) throws IOException;

	boolean setAttributes(Map<String, Object> attributes) throws IOException;

	boolean removeAttributes(List<String> attributeNames) throws IOException;

	// -----------------------------------------------------------------------------------
	// Member Config Elements
	// -----------------------------------------------------------------------------------
	IConfigElement[] memberConfigElements() throws IOException;

	boolean memberConfigElementExists(String name) throws IOException;

	IConfigElement createMemberConfigElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

}
