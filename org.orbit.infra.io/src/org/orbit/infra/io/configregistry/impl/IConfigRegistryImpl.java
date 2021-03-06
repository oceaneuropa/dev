package org.orbit.infra.io.configregistry.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.util.ConfigRegistryUtil;
import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.orbit.infra.io.util.ClientComparators;
import org.origin.common.resource.Path;
import org.origin.common.rest.client.ClientException;

public class IConfigRegistryImpl implements IConfigRegistry {

	protected CFG cfg;
	protected ConfigRegistry configRegistry;

	/**
	 * 
	 * @param cfg
	 * @param configRegistry
	 */
	public IConfigRegistryImpl(CFG cfg, ConfigRegistry configRegistry) {
		this.cfg = cfg;
		this.configRegistry = configRegistry;
	}

	@Override
	public CFG getCFG() {
		return this.cfg;
	}

	@Override
	public ConfigRegistry getConfigRegistry() {
		return this.configRegistry;
	}

	// -----------------------------------------------------------------------------------
	// Config registry (read)
	// -----------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.configRegistry.getId();
	}

	@Override
	public String getType() {
		return this.configRegistry.getType();
	}

	@Override
	public String getName() {
		return this.configRegistry.getName();
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.configRegistry.getProperties();
	}

	@Override
	public long getDateCreated() {
		return this.configRegistry.getDateCreated();
	}

	@Override
	public long getDateModified() {
		return this.configRegistry.getDateModified();
	}

	// -----------------------------------------------------------------------------------
	// Config registry (write)
	// -----------------------------------------------------------------------------------
	@Override
	public boolean sync() throws IOException {
		boolean isSynced = false;
		String configRegistryId = getId();
		IConfigRegistry cfgReg = this.cfg.getConfigRegistryById(configRegistryId);
		if (cfgReg != null) {
			this.configRegistry = cfgReg.getConfigRegistry();
			isSynced = true;
		}
		return isSynced;
	}

	@Override
	public boolean updatedType(String newType) throws IOException {
		if (newType == null) {
			throw new IllegalArgumentException("newType is null.");
		}

		String configRegistryId = getId();
		boolean isUpdated = this.cfg.updateConfigRegistryType(configRegistryId, newType);
		if (isUpdated) {
			if (!sync()) {
				this.configRegistry.setType(newType);
				this.configRegistry.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean rename(String newName) throws IOException {
		if (newName == null || newName.isEmpty()) {
			throw new IllegalArgumentException("newName is empty.");
		}

		String configRegistryId = getId();
		boolean isUpdated = this.cfg.updateConfigRegistryName(configRegistryId, newName);
		if (isUpdated) {
			if (!sync()) {
				this.configRegistry.setName(newName);
				this.configRegistry.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean setProperty(String oldName, String name, Object value) throws IOException {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("property name is empty.");
		}
		if (value == null) {
			throw new IllegalArgumentException("property value is null.");
		}

		String configRegistryId = getId();
		boolean isUpdated = this.cfg.setConfigRegistryProperty(configRegistryId, oldName, name, value);
		if (isUpdated) {
			if (!sync()) {
				Map<String, Object> properties = getProperties();
				if (oldName != null && !oldName.equals(name)) {
					// property name is changed
					properties.remove(oldName);
					properties.put(name, value);
				} else {
					// property name is not changed
					properties.put(name, value);
				}
				this.configRegistry.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean setProperties(Map<String, Object> properties) throws IOException {
		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("properties is empty.");
		}

		String configRegistryId = getId();
		boolean isUpdated = this.cfg.setConfigRegistryProperties(configRegistryId, properties);
		if (isUpdated) {
			if (!sync()) {
				this.configRegistry.getProperties().putAll(properties);
				this.configRegistry.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	@Override
	public boolean removeProperties(List<String> propertyNames) throws IOException {
		if (propertyNames == null || propertyNames.isEmpty()) {
			throw new IllegalArgumentException("propertyNames is empty.");
		}

		String configRegistryId = getId();
		boolean isUpdated = this.cfg.removeConfigRegistryProperties(configRegistryId, propertyNames);
		if (isUpdated) {
			if (!sync()) {
				for (String propertyName : propertyNames) {
					this.configRegistry.getProperties().remove(propertyName);
				}
				this.configRegistry.setDateModified(new Date().getTime());
			}
		}
		return isUpdated;
	}

	// -----------------------------------------------------------------------------------
	// Config Elements
	// -----------------------------------------------------------------------------------
	/**
	 * 
	 * @param e
	 * @throws IOException
	 */
	protected void handle(Exception e) throws IOException {
		if (e instanceof IOException) {
			throw (IOException) e;
		}
		throw new IOException(e);
	}

	/**
	 * 
	 * @param configElement
	 * @return
	 */
	protected IConfigElement toConfigElement(ConfigElement configElement) {
		IConfigElement cfgEle = new IConfigElementImpl(this, configElement);
		return cfgEle;
	}

	@Override
	public IConfigElement[] listRootElements() throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = ConfigRegistryUtil.listRootElements(clientResolver, accessToken, configRegistryId);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}

		Collections.sort(cfgEles, ClientComparators.ConfigElementComparator_ASC);

		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement getRootElement(String name) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.getElement(clientResolver, accessToken, configRegistryId, null, name);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement[] listElements(String parentElementId) throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = ConfigRegistryUtil.listElements(clientResolver, accessToken, configRegistryId, parentElementId);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}

		Collections.sort(cfgEles, ClientComparators.ConfigElementComparator_ASC);

		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement[] listElements(Path parentPath) throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = ConfigRegistryUtil.listElements(clientResolver, accessToken, configRegistryId, parentPath);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}

		Collections.sort(cfgEles, ClientComparators.ConfigElementComparator_ASC);

		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement getElement(String elementId) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.getElement(clientResolver, accessToken, configRegistryId, elementId);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement getElement(Path path) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.getElement(clientResolver, accessToken, configRegistryId, path);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement getElement(String parentElementId, String name) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.getElement(clientResolver, accessToken, configRegistryId, parentElementId, name);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement getElement(String parentElementId, Path path) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.getElement(clientResolver, accessToken, configRegistryId, parentElementId, path);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public Path getElementPath(String elementId) throws IOException {
		String configRegistryId = getId();

		Path path = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			path = ConfigRegistryUtil.getElementPath(clientResolver, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return path;
	}

	@Override
	public boolean elementExists(String elementId) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			exists = ConfigRegistryUtil.elementExists(clientResolver, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean elementExists(Path path) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			exists = ConfigRegistryUtil.elementExists(clientResolver, accessToken, configRegistryId, path);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean elementExists(String parentElementId, String name) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			exists = ConfigRegistryUtil.elementExists(clientResolver, accessToken, configRegistryId, parentElementId, name);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public IConfigElement createRootElement(String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		return createElement("-1", name, attributes, generateUniqueName);
	}

	@Override
	public IConfigElement createElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.createElement(clientResolver, accessToken, configRegistryId, path, attributes, generateUniqueName);
			if (configElement == null) {
				throw new IOException("Config element cannot be created.");
			}
			cfgEle = toConfigElement(configElement);

		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement createElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String configRegistryId = getId();

		IConfigElement element = null;
		try {
			ConfigRegistryClientResolver resolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.createElement(resolver, accessToken, configRegistryId, parentElementId, name, attributes, generateUniqueName);
			if (configElement == null) {
				throw new IOException("Config element cannot be created.");
			}
			element = toConfigElement(configElement);

		} catch (ClientException e) {
			handle(e);
		}
		return element;
	}

	@Override
	public IConfigElement createElement(String parentElementId, Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String configRegistryId = getId();

		IConfigElement element = null;
		try {
			ConfigRegistryClientResolver resolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = ConfigRegistryUtil.createElement(resolver, accessToken, configRegistryId, parentElementId, path, attributes, generateUniqueName);
			if (configElement == null) {
				throw new IOException("Config element cannot be created.");
			}
			element = toConfigElement(configElement);

		} catch (ClientException e) {
			handle(e);
		}
		return element;
	}

	@Override
	public boolean updateElementName(String elementId, String newName) throws IOException {
		String configRegistryId = getId();

		boolean succeed = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			succeed = ConfigRegistryUtil.updateElementName(clientResolver, accessToken, configRegistryId, elementId, newName);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean setElementAttribute(String elementId, String oldAttributeName, String attributeName, Object attributeValue) throws IOException {
		String configRegistryId = getId();

		boolean succeed = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			succeed = ConfigRegistryUtil.setElementAttribute(clientResolver, accessToken, configRegistryId, elementId, oldAttributeName, attributeName, attributeValue);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean setElementAttributes(String elementId, Map<String, Object> attributes) throws IOException {
		String configRegistryId = getId();

		boolean succeed = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			succeed = ConfigRegistryUtil.setElementAttributes(clientResolver, accessToken, configRegistryId, elementId, attributes);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean removeElementAttribute(String elementId, String attributeName) throws IOException {
		String configRegistryId = getId();

		boolean succeed = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			succeed = ConfigRegistryUtil.removeElementAttribute(clientResolver, accessToken, configRegistryId, elementId, attributeName);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean removeElementAttributes(String elementId, List<String> attributeName) throws IOException {
		String configRegistryId = getId();

		boolean succeed = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			succeed = ConfigRegistryUtil.removeElementAttributes(clientResolver, accessToken, configRegistryId, elementId, attributeName);

		} catch (ClientException e) {
			handle(e);
		}
		return succeed;
	}

	@Override
	public boolean deleteElement(String elementId) throws IOException {
		String configRegistryId = getId();

		boolean isDeleted = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			isDeleted = ConfigRegistryUtil.deleteElement(clientResolver, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteElement(Path path) throws IOException {
		String configRegistryId = getId();

		boolean isDeleted = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String accessToken = this.cfg.getAccessToken();

			isDeleted = ConfigRegistryUtil.deleteElement(clientResolver, accessToken, configRegistryId, path);

		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

}
