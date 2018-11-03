package org.orbit.infra.io.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.resource.Path;

public class IConfigElementImpl implements IConfigElement {

	protected IConfigRegistry cfgReg;
	protected ConfigElement configElement;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param cfgReg
	 * @param configElement
	 */
	public IConfigElementImpl(IConfigRegistry cfgReg, ConfigElement configElement) {
		this.cfgReg = cfgReg;
		this.configElement = configElement;
	}

	@Override
	public CFG getCFG() {
		return this.cfgReg.getCFG();
	}

	@Override
	public IConfigRegistry getIConfigRegistry() {
		return this.cfgReg;
	}

	@Override
	public ConfigElement getConfigElement() {
		return this.configElement;
	}

	// -----------------------------------------------------------------------------------
	// Config Element
	// -----------------------------------------------------------------------------------
	@Override
	public String getConfigRegistryId() {
		return this.configElement.getConfigRegistryId();
	}

	@Override
	public String getParentElementId() {
		return this.configElement.getParentElementId();
	}

	@Override
	public String getElementId() {
		return this.configElement.getElementId();
	}

	@Override
	public Path getPath() {
		return this.configElement.getPath();
	}

	@Override
	public String getName() {
		return this.configElement.getName();
	}

	@Override
	public String[] getAttributeNames() {
		return this.configElement.getAttributeNames();
	}

	@Override
	public Map<String, Object> getAttributes() {
		return this.configElement.getAttributes();
	}

	@Override
	public <T> T getAttribute(String attrName, Class<T> attrValueClass) {
		return this.configElement.getAttribute(attrName, attrValueClass);
	}

	@Override
	public long getDateCreated() {
		return this.configElement.getDateCreated();
	}

	@Override
	public long getDateModified() {
		return this.configElement.getDateModified();
	}

	@Override
	public boolean isEnabled() {
		boolean isEnabled = this.configElement.getAttribute("enabled", Boolean.class);
		return isEnabled;
	}

	// -----------------------------------------------------------------------------------
	// Config element (update)
	// -----------------------------------------------------------------------------------
	@Override
	public boolean sync() throws IOException {
		boolean isSynced = false;
		String elementId = getElementId();
		IConfigElement cfgEle = this.cfgReg.getConfigElement(elementId);
		if (cfgEle != null) {
			this.configElement = cfgEle.getConfigElement();
			isSynced = true;
		}
		return isSynced;
	}

	@Override
	public boolean rename(String newName) throws IOException {
		if (newName == null || newName.isEmpty()) {
			throw new IllegalArgumentException("newName is empty.");
		}

		String elementId = getElementId();
		boolean isUpdated = this.cfgReg.updateConfigElementName(elementId, newName);
		if (isUpdated) {
			if (!sync()) {
				this.configElement.setName(newName);
				this.configElement.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean setAttributes(Map<String, Object> attributes) throws IOException {
		if (attributes == null || attributes.isEmpty()) {
			throw new IllegalArgumentException("attributes is empty.");
		}

		String elementId = getElementId();
		boolean isUpdated = this.cfgReg.setConfigElementAttributes(elementId, attributes);
		if (isUpdated) {
			if (!sync()) {
				this.configElement.getAttributes().putAll(attributes);
				this.configElement.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean removeAttributes(List<String> attributeNames) throws IOException {
		if (attributeNames == null || attributeNames.isEmpty()) {
			throw new IllegalArgumentException("attributeNames is empty.");
		}

		String elementId = getElementId();
		boolean isUpdated = this.cfgReg.removeConfigElementAttributes(elementId, attributeNames);
		if (isUpdated) {
			if (!sync()) {
				for (String attributeName : attributeNames) {
					this.configElement.getAttributes().remove(attributeName);
				}
				this.configElement.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	// -----------------------------------------------------------------------------------
	// Member Config Elements
	// -----------------------------------------------------------------------------------
	@Override
	public IConfigElement[] memberConfigElements() throws IOException {
		String parentElementId = getElementId();
		return this.cfgReg.listConfigElements(parentElementId);
	}

	@Override
	public boolean memberConfigElementExists(String name) throws IOException {
		String parentElementId = getElementId();
		return this.cfgReg.configElementExists(parentElementId, name);
	}

	@Override
	public IConfigElement createMemberConfigElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String parentElementId = getElementId();
		return this.cfgReg.createConfigElement(parentElementId, name, attributes, generateUniqueName);
	}

	/** IAdaptable */
	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}
