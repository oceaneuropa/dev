package org.orbit.infra.io.configregistry;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.model.DateRecordAware;
import org.origin.common.model.TransientPropertyAware;
import org.origin.common.resource.Path;

public interface IConfigElement extends DateRecordAware<Long>, TransientPropertyAware, IAdaptable {

	CFG getCFG();

	IConfigRegistry getConfigRegistry();

	IConfigElement getParent();

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

	Object getAttribute(String attrName);

	<T> T getAttribute(String attrName, Class<T> attrValueClass);

	boolean isEnabled();

	// -----------------------------------------------------------------------------------
	// Config element (write)
	// -----------------------------------------------------------------------------------
	boolean sync() throws IOException;

	boolean rename(String newName) throws IOException;

	boolean setAttribute(String oldAttributeName, String attributeName, Object attributeValue) throws IOException;

	boolean setAttributes(Map<String, Object> attributes) throws IOException;

	boolean removeAttribute(String attributeName) throws IOException;

	boolean removeAttributes(List<String> attributeNames) throws IOException;

	/**
	 * Delete the config element itself.
	 * 
	 * @return
	 * @throws IOException
	 */
	boolean delete() throws IOException;

	// -----------------------------------------------------------------------------------
	// Children Config Elements
	// -----------------------------------------------------------------------------------
	IConfigElement[] getChildrenElements() throws IOException;

	boolean childElementExists(String name) throws IOException;

	IConfigElement getChildElement(String name) throws IOException;

	IConfigElement getChildElement(Path path) throws IOException;

	IConfigElement createChildElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	IConfigElement createChildElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException;

	/**
	 * Delete child element by child element path.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	boolean deleteChildElement(Path path) throws IOException;

}
