package org.orbit.infra.io.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.configregistry.ConfigRegistry;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigRegistry;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.ServiceMetadata;

public class CFGImpl extends CFG {

	/**
	 * 
	 * @param configRegistryServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 */
	public CFGImpl(String configRegistryServiceUrl, String indexServiceUrl, String accessToken) {
		super(configRegistryServiceUrl, indexServiceUrl, accessToken);
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
	public ServiceMetadata getServiceServiceMetadata() throws IOException {
		ServiceMetadata metadata = null;
		try {
			metadata = InfraClientsUtil.CONFIG_REGISTRY.getServiceMetadata(this.clientResolver, this.configRegistryServiceUrl, this.accessToken);
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
			ConfigRegistry[] configRegistries = InfraClientsUtil.CONFIG_REGISTRY.getConfigRegistries(this.clientResolver, this.configRegistryServiceUrl, this.accessToken);
			if (configRegistries != null) {
				for (ConfigRegistry configRegistry : configRegistries) {
					IConfigRegistry cfgReg = toConfigRegistry(configRegistry);
					cfgRegs.add(cfgReg);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgRegs.toArray(new IConfigRegistry[cfgRegs.size()]);
	}

	@Override
	public IConfigRegistry[] getConfigRegistries(String type) throws IOException {
		List<IConfigRegistry> cfgRegs = new ArrayList<IConfigRegistry>();
		try {
			ConfigRegistry[] configRegistries = InfraClientsUtil.CONFIG_REGISTRY.getConfigRegistries(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, type);
			if (configRegistries != null) {
				for (ConfigRegistry configRegistry : configRegistries) {
					IConfigRegistry cfgReg = toConfigRegistry(configRegistry);
					cfgRegs.add(cfgReg);
				}
			}
		} catch (ClientException e) {
			handle(e);
		}
		return cfgRegs.toArray(new IConfigRegistry[cfgRegs.size()]);
	}

	@Override
	public IConfigRegistry getConfigRegistryById(String id) throws IOException {
		IConfigRegistry cfgReg = null;
		try {
			ConfigRegistry configRegistry = InfraClientsUtil.CONFIG_REGISTRY.getConfigRegistryById(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id);
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
			ConfigRegistry configRegistry = InfraClientsUtil.CONFIG_REGISTRY.getConfigRegistryByName(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, name);
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
			exists = InfraClientsUtil.CONFIG_REGISTRY.configRegistryExistsById(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id);
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public boolean configRegistryExistsByName(String name) throws IOException {
		boolean exists = false;
		try {
			exists = InfraClientsUtil.CONFIG_REGISTRY.configRegistryExistsByName(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, name);
		} catch (ClientException e) {
			handle(e);
		}
		return exists;
	}

	@Override
	public IConfigRegistry createConfigRegistry(String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws IOException {
		IConfigRegistry cfgReg = null;
		try {
			ConfigRegistry configRegistry = InfraClientsUtil.CONFIG_REGISTRY.createConfigRegistry(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, type, name, properties, generateUniqueName);
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
			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.updateConfigRegistryType(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id, type);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean updateConfigRegistryName(String id, String name) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.updateConfigRegistryName(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id, name);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.setConfigRegistryProperties(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id, properties);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws IOException {
		boolean isUpdated = false;
		try {
			isUpdated = InfraClientsUtil.CONFIG_REGISTRY.removeConfigRegistryProperties(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id, propertyNames);
		} catch (ClientException e) {
			handle(e);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteConfigRegistryById(String id) throws IOException {
		boolean isDeleted = false;
		try {
			isDeleted = InfraClientsUtil.CONFIG_REGISTRY.deleteConfigRegistryById(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, id);
		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteConfigRegistryByName(String name) throws IOException {
		boolean isDeleted = false;
		try {
			isDeleted = InfraClientsUtil.CONFIG_REGISTRY.deleteConfigRegistryByName(this.clientResolver, this.configRegistryServiceUrl, this.accessToken, name);
		} catch (ClientException e) {
			handle(e);
		}
		return isDeleted;
	}

}
