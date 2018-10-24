package org.orbit.infra.io.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigElement;
import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
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
	public IConfigElement[] listRootConfigElements() throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = InfraClientsUtil.CONFIG_REGISTRY.listRootConfigElements(clientResolver, serviceUrl, accessToken, configRegistryId);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement[] listConfigElements(String parentElementId) throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = InfraClientsUtil.CONFIG_REGISTRY.listConfigElements(clientResolver, serviceUrl, accessToken, configRegistryId, parentElementId);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement[] listConfigElements(Path parentPath) throws IOException {
		String configRegistryId = getId();

		List<IConfigElement> cfgEles = new ArrayList<IConfigElement>();
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement[] configElements = InfraClientsUtil.CONFIG_REGISTRY.listConfigElements(clientResolver, serviceUrl, accessToken, configRegistryId, parentPath);
			if (configElements != null) {
				for (ConfigElement configElement : configElements) {
					IConfigElement cfgEle = toConfigElement(configElement);
					cfgEles.add(cfgEle);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEles.toArray(new IConfigElement[cfgEles.size()]);
	}

	@Override
	public IConfigElement getConfigElement(String elementId) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = InfraClientsUtil.CONFIG_REGISTRY.getConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, elementId);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement getConfigElement(Path path) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = InfraClientsUtil.CONFIG_REGISTRY.getConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, path);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public IConfigElement getConfigElement(String parentElementId, String name) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = InfraClientsUtil.CONFIG_REGISTRY.getConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, parentElementId, name);
			if (configElement != null) {
				cfgEle = toConfigElement(configElement);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgEle;
	}

	@Override
	public Path getConfigElementPath(String elementId) throws IOException {
		String configRegistryId = getId();

		Path path = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			path = InfraClientsUtil.CONFIG_REGISTRY.getConfigElementPath(clientResolver, serviceUrl, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return path;
	}

	@Override
	public boolean configElementExists(String elementId) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			exists = InfraClientsUtil.CONFIG_REGISTRY.configElementExists(clientResolver, serviceUrl, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean configElementExists(Path path) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			exists = InfraClientsUtil.CONFIG_REGISTRY.configElementExists(clientResolver, serviceUrl, accessToken, configRegistryId, path);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean configElementExists(String parentElementId, String name) throws IOException {
		String configRegistryId = getId();

		boolean exists = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			exists = InfraClientsUtil.CONFIG_REGISTRY.configElementExists(clientResolver, serviceUrl, accessToken, configRegistryId, parentElementId, name);

		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public IConfigElement createConfigElement(Path path, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = InfraClientsUtil.CONFIG_REGISTRY.createConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, path, attributes, generateUniqueName);
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
	public IConfigElement createConfigElement(String parentElementId, String name, Map<String, Object> attributes, boolean generateUniqueName) throws IOException {
		String configRegistryId = getId();

		IConfigElement cfgEle = null;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			ConfigElement configElement = InfraClientsUtil.CONFIG_REGISTRY.createConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, parentElementId, name, attributes, generateUniqueName);
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
	public boolean updateConfigElementName(String elementId, String newName) throws IOException {
		String configRegistryId = getId();

		boolean isUpdated = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.updateConfigElementName(clientResolver, serviceUrl, accessToken, configRegistryId, elementId, newName);

		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigElementAttributes(String elementId, Map<String, Object> attributes) throws IOException {
		String configRegistryId = getId();

		boolean isUpdated = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.setConfigElementAttributes(clientResolver, serviceUrl, accessToken, configRegistryId, elementId, attributes);

		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigElementAttributes(String elementId, List<String> attributeName) throws IOException {
		String configRegistryId = getId();

		boolean isUpdated = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.removeConfigElementAttributes(clientResolver, serviceUrl, accessToken, configRegistryId, elementId, attributeName);

		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigElement(String elementId) throws IOException {
		String configRegistryId = getId();

		boolean isDeleted = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			isDeleted = InfraClientsUtil.CONFIG_REGISTRY.deleteConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, elementId);

		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigElement(Path path) throws IOException {
		String configRegistryId = getId();

		boolean isDeleted = false;
		try {
			ConfigRegistryClientResolver clientResolver = this.cfg.getClientResolver();
			String serviceUrl = this.cfg.getConfigRegistryServiceUrl();
			String accessToken = this.cfg.getAccessToken();

			isDeleted = InfraClientsUtil.CONFIG_REGISTRY.deleteConfigElement(clientResolver, serviceUrl, accessToken, configRegistryId, path);

		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

}
