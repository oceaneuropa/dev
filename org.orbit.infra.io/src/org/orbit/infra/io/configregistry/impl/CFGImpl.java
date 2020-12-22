package org.orbit.infra.io.configregistry.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.util.ConfigRegistryUtil;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.ClientComparators;
import org.orbit.infra.io.util.ConfigRegistryClientResolverImpl;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.model.ServiceMetadata;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class CFGImpl implements CFG {

	protected String accessToken;
	protected ConfigRegistryClientResolver resolver;

	/**
	 * 
	 * @param accessToken
	 */
	public CFGImpl(String accessToken) {
		this.accessToken = accessToken;
		this.resolver = new ConfigRegistryClientResolverImpl();
	}

	@Override
	public String getAccessToken() {
		return this.accessToken;
	}

	@Override
	public ConfigRegistryClientResolver getClientResolver() {
		return this.resolver;
	}

	@Override
	public ServiceClient getServiceClient() {
		ServiceClient serviceClient = null;
		ConfigRegistryClientResolver resolver = getClientResolver();
		if (resolver != null) {
			String accessToken = getAccessToken();
			serviceClient = resolver.resolve(accessToken);
		}
		return serviceClient;
	}

	@Override
	public boolean isOnline() {
		ServiceClient client = getServiceClient();
		return (client != null) ? client.ping() : false;
	}

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
	 * @param configRegistry
	 * @return
	 */
	protected IConfigRegistry toConfigRegistry(ConfigRegistry configRegistry) {
		IConfigRegistry cfgReg = new IConfigRegistryImpl(this, configRegistry);
		return cfgReg;
	}

	@Override
	public ServiceMetadata getServiceMetadata() throws IOException {
		ServiceMetadata metadata = null;
		try {
			metadata = ConfigRegistryUtil.getServiceMetadata(this.resolver, this.accessToken);
		} catch (ClientException e) {
			handle(e);
		}
		if (metadata == null) {
			throw new IOException("Cannot get config registry service metadata.");
		}
		return metadata;
	}

	@Override
	public IConfigRegistry[] getConfigRegistries() throws IOException {
		List<IConfigRegistry> cfgRegs = new ArrayList<IConfigRegistry>();
		try {
			ConfigRegistry[] configRegistries = ConfigRegistryUtil.getConfigRegistries(this.resolver, this.accessToken);
			if (configRegistries != null) {
				for (ConfigRegistry configRegistry : configRegistries) {
					IConfigRegistry cfgReg = toConfigRegistry(configRegistry);
					cfgRegs.add(cfgReg);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}

		Collections.sort(cfgRegs, ClientComparators.ConfigRegistryComparator_ASC);

		return cfgRegs.toArray(new IConfigRegistry[cfgRegs.size()]);
	}

	@Override
	public IConfigRegistry[] getConfigRegistries(String type) throws IOException {
		List<IConfigRegistry> cfgRegs = new ArrayList<IConfigRegistry>();
		try {
			ConfigRegistry[] configRegistries = ConfigRegistryUtil.getConfigRegistries(this.resolver, this.accessToken, type);
			if (configRegistries != null) {
				for (ConfigRegistry configRegistry : configRegistries) {
					IConfigRegistry cfgReg = toConfigRegistry(configRegistry);
					cfgRegs.add(cfgReg);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}

		Collections.sort(cfgRegs, ClientComparators.ConfigRegistryComparator_ASC);

		return cfgRegs.toArray(new IConfigRegistry[cfgRegs.size()]);
	}

	@Override
	public IConfigRegistry getConfigRegistryById(String id) throws IOException {
		IConfigRegistry cfgReg = null;
		try {
			ConfigRegistry configRegistry = ConfigRegistryUtil.getConfigRegistryById(this.resolver, this.accessToken, id);
			if (configRegistry != null) {
				cfgReg = toConfigRegistry(configRegistry);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgReg;
	}

	@Override
	public IConfigRegistry getConfigRegistryByName(String name) throws IOException {
		IConfigRegistry cfgReg = null;
		try {
			ConfigRegistry configRegistry = ConfigRegistryUtil.getConfigRegistryByName(this.resolver, this.accessToken, name);
			if (configRegistry != null) {
				cfgReg = toConfigRegistry(configRegistry);
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgReg;
	}

	@Override
	public boolean configRegistryExistsById(String id) throws IOException {
		boolean exists = false;
		try {
			exists = ConfigRegistryUtil.configRegistryExistsById(this.resolver, this.accessToken, id);
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean configRegistryExistsByName(String name) throws IOException {
		boolean exists = false;
		try {
			exists = ConfigRegistryUtil.configRegistryExistsByName(this.resolver, this.accessToken, name);
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public IConfigRegistry createConfigRegistry(String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws IOException {
		IConfigRegistry cfgReg = null;
		try {
			ConfigRegistry configRegistry = ConfigRegistryUtil.createConfigRegistry(this.resolver, this.accessToken, type, name, properties, generateUniqueName);
			if (configRegistry == null) {
				throw new IOException("Config registry cannot be created.");
			}
			cfgReg = toConfigRegistry(configRegistry);

		} catch (ClientException e) {
			handle(e);
		}
		return cfgReg;
	}

	@Override
	public boolean updateConfigRegistryType(String id, String type) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = ConfigRegistryUtil.updateConfigRegistryType(this.resolver, this.accessToken, id, type);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean updateConfigRegistryName(String id, String name) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = ConfigRegistryUtil.updateConfigRegistryName(this.resolver, this.accessToken, id, name);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigRegistryProperty(String id, String oldName, String name, Object value) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = ConfigRegistryUtil.setConfigRegistryProperty(this.resolver, this.accessToken, id, oldName, name, value);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = ConfigRegistryUtil.setConfigRegistryProperties(this.resolver, this.accessToken, id, properties);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = ConfigRegistryUtil.removeConfigRegistryProperties(this.resolver, this.accessToken, id, propertyNames);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigRegistryById(String id) throws IOException {
		boolean isDeleted = false;
		try {
			isDeleted = ConfigRegistryUtil.deleteConfigRegistryById(this.resolver, this.accessToken, id);
		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigRegistryByName(String name) throws IOException {
		boolean isDeleted = false;
		try {
			isDeleted = ConfigRegistryUtil.deleteConfigRegistryByName(this.resolver, this.accessToken, name);
		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

}
