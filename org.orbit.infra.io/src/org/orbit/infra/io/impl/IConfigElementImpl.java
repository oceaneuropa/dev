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
import org.origin.common.model.DateRecordSupport;
import org.origin.common.model.TransientPropertySupport;
import org.origin.common.resource.Path;

public class IConfigElementImpl implements IConfigElement {

	protected IConfigRegistry registry;
	protected IConfigElement parent;
	protected ConfigElement configElement;

	protected DateRecordSupport<Long> dateRecordSupport = new DateRecordSupport<Long>();
	protected TransientPropertySupport transientPropertySupport = new TransientPropertySupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param registry
	 * @param configElement
	 */
	public IConfigElementImpl(IConfigRegistry registry, ConfigElement configElement) {
		this.registry = registry;
		this.configElement = configElement;

		long dateCreated = this.configElement.getDateCreated();
		long dateModified = this.configElement.getDateModified();
		this.dateRecordSupport.setDateCreated(dateCreated);
		this.dateRecordSupport.setDateModified(dateModified);
	}

	@Override
	public CFG getCFG() {
		return this.registry.getCFG();
	}

	@Override
	public IConfigRegistry getConfigRegistry() {
		return this.registry;
	}

	@Override
	public IConfigElement getParent() {
		return this.parent;
	}

	public void setParent(IConfigElement parent) {
		this.parent = parent;
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
	public Object getAttribute(String attrName) {
		return this.configElement.getAttribute(attrName);
	}

	@Override
	public <T> T getAttribute(String attrName, Class<T> attrValueClass) {
		return this.configElement.getAttribute(attrName, attrValueClass);
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
		IConfigElement cfgEle = this.registry.getElement(elementId);
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
		boolean isUpdated = this.registry.updateElementName(elementId, newName);
		if (isUpdated) {
			if (!sync()) {
				this.configElement.setName(newName);
				this.configElement.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean setAttribute(String oldName, String attributeName, Object attributeValue) throws IOException {
		if (attributeName == null || attributeName.isEmpty()) {
			throw new IllegalArgumentException("attribute name is empty.");
		}
		if (attributeValue == null) {
			throw new IllegalArgumentException("attribute value is null.");
		}

		String elementId = getElementId();
		boolean isUpdated = this.registry.setElementAttribute(elementId, oldName, attributeName, attributeValue);
		if (isUpdated) {
			if (!sync()) {
				Map<String, Object> attributes = getAttributes();
				if (oldName != null && !oldName.equals(attributeName)) {
					// attribute name is changed
					attributes.remove(oldName);
					attributes.put(attributeName, attributeValue);
				} else {
					// attribute name is not changed
					attributes.put(attributeName, attributeValue);
				}
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
		boolean isUpdated = this.registry.setElementAttributes(elementId, attributes);
		if (isUpdated) {
			if (!sync()) {
				this.configElement.getAttributes().putAll(attributes);
				this.configElement.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean removeAttribute(String attributeName) throws IOException {
		if (attributeName == null || attributeName.isEmpty()) {
			throw new IllegalArgumentException("attribute name is empty.");
		}
		String elementId = getElementId();
		boolean isUpdated = this.registry.removeElementAttribute(elementId, attributeName);
		if (isUpdated) {
			if (!sync()) {
				this.configElement.getAttributes().remove(attributeName);
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
		boolean isUpdated = this.registry.removeElementAttributes(elementId, attributeNames);
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
	public IConfigElement[] getChildrenElements() throws IOException {
		String parentElementId = getElementId();
		IConfigElement[] children = this.registry.listElements(parentElementId);
		if (children != null) {
			for (IConfigElement child : children) {
				if (child instanceof IConfigElementImpl) {
					((IConfigElementImpl) child).setParent(this);
				}
			}
		}
		return children;
	}

	@Override
	public IConfigElement getChildElement(String name) throws IOException {
		String parentElementId = getElementId();
		IConfigElement child = this.registry.getElement(parentElementId, name);
		if (child instanceof IConfigElementImpl) {
			((IConfigElementImpl) child).setParent(this);
		}
		return child;
	}

	@Override
	public IConfigElement getChildElement(Path path) throws IOException {
		String parentElementId = getElementId();
		IConfigElement child = this.registry.getElement(parentElementId, path);
		if (child instanceof IConfigElementImpl) {
			((IConfigElementImpl) child).setParent(this);
		}
		return child;
	}

	@Override
	public boolean childElementExists(String name) throws IOException {
		String parentElementId = getElementId();
		return this.registry.elementExists(parentElementId, name);
	}

	@Override
	public IConfigElement createChildElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String parentElementId = getElementId();
		IConfigElement child = this.registry.createElement(parentElementId, name, attributes, generateUniqueName);
		if (child instanceof IConfigElementImpl) {
			((IConfigElementImpl) child).setParent(this);
		}
		return child;
	}

	@Override
	public IConfigElement createChildElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String parentElementId = getElementId();
		return this.registry.createElement(parentElementId, path, attributes, generateUniqueName);
	}

	/** DateRecordAware */
	@Override
	public Long getDateCreated() {
		return this.dateRecordSupport.getDateCreated();
	}

	@Override
	public void setDateCreated(Long dateCreated) {
		this.dateRecordSupport.setDateCreated(dateCreated);
	}

	@Override
	public Long getDateModified() {
		return this.dateRecordSupport.getDateModified();
	}

	@Override
	public void setDateModified(Long dateModified) {
		this.dateRecordSupport.setDateModified(dateModified);
	}

	/** TransientPropertyAware */
	@Override
	public <T> T getTransientProperty(String key) {
		return this.transientPropertySupport.getTransientProperty(key);
	}

	@Override
	public <T> T setTransientProperty(String key, T value) {
		return this.transientPropertySupport.setTransientProperty(key, value);
	}

	@Override
	public <T> T removeTransientProperty(String key) {
		return this.transientPropertySupport.removeTransientProperty(key);
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

// long getDateCreated();
// long getDateModified();
// @Override
// public long getDateCreated() {
// return this.configElement.getDateCreated();
// }
// @Override
// public long getDateModified() {
// return this.configElement.getDateModified();
// }
