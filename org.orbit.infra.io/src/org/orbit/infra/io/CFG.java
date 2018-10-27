package org.orbit.infra.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.util.ConfigRegistryConfigPropertiesHandler;
import org.orbit.infra.io.impl.CFGImpl;
import org.orbit.infra.io.util.ConfigRegistryClientResolverImpl;
import org.origin.common.rest.model.ServiceMetadata;

public abstract class CFG {

	protected static Map<String, CFG> CFG_MAP = new HashMap<String, CFG>();

	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public synchronized static CFG getDefault(String accessToken) {
		String indexServiceUrl = ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_CONFIG_REGISTRY_URL);
		String configRegistryServiceUrl = ConfigRegistryConfigPropertiesHandler.getInstance().getProperty(InfraConstants.ORBIT_CONFIG_REGISTRY_URL);
		CFG cfg = get(indexServiceUrl, configRegistryServiceUrl, accessToken);
		return cfg;
	}

	/**
	 * 
	 * @param configRegistryServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 */
	public synchronized static CFG get(String configRegistryServiceUrl, String indexServiceUrl, String accessToken) {
		String key = configRegistryServiceUrl + "|" + indexServiceUrl + "|" + accessToken;
		CFG cfg = CFG_MAP.get(key);
		if (cfg == null) {
			cfg = new CFGImpl(configRegistryServiceUrl, indexServiceUrl, accessToken);
			CFG_MAP.put(key, cfg);
		}
		return cfg;
	}

	public synchronized static void clear() {
		CFG_MAP.clear();
	}

	protected String configRegistryServiceUrl;
	protected String indexServiceUrl;
	protected String accessToken;
	protected ConfigRegistryClientResolver clientResolver;

	/**
	 * 
	 * @param configRegistryServiceUrl
	 * @param indexServiceUrl
	 * @param accessToken
	 */
	public CFG(String configRegistryServiceUrl, String indexServiceUrl, String accessToken) {
		if (configRegistryServiceUrl == null) {
			throw new IllegalArgumentException("configRegistryServiceUrl is null.");
		}
		if (indexServiceUrl == null) {
			throw new IllegalArgumentException("indexServiceUrl is null.");
		}

		this.configRegistryServiceUrl = configRegistryServiceUrl;
		this.indexServiceUrl = indexServiceUrl;
		this.accessToken = accessToken;

		this.clientResolver = new ConfigRegistryClientResolverImpl(indexServiceUrl);
	}

	public String getConfigRegistryServiceUrl() {
		return this.configRegistryServiceUrl;
	}

	public String getIndexServiceUrl() {
		return this.indexServiceUrl;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public ConfigRegistryClientResolver getClientResolver() {
		return this.clientResolver;
	}

	// -----------------------------------------------------------------------------------
	// Config Registries
	// -----------------------------------------------------------------------------------
	public abstract ServiceMetadata getServiceServiceMetadata() throws IOException;

	public abstract IConfigRegistry[] getConfigRegistries() throws IOException;

	public abstract IConfigRegistry[] getConfigRegistries(String type) throws IOException;

	public abstract IConfigRegistry getConfigRegistryById(String id) throws IOException;

	public abstract IConfigRegistry getConfigRegistryByName(String name) throws IOException;

	public abstract boolean configRegistryExistsById(String id) throws IOException;

	public abstract boolean configRegistryExistsByName(String name) throws IOException;

	public abstract IConfigRegistry createConfigRegistry(String type, String name, Map<String, Object> properties, boolean generateUniqueName) throws IOException;

	public abstract boolean updateConfigRegistryType(String id, String type) throws IOException;

	public abstract boolean updateConfigRegistryName(String id, String name) throws IOException;

	public abstract boolean setConfigRegistryProperties(String id, Map<String, Object> properties) throws IOException;

	public abstract boolean removeConfigRegistryProperties(String id, List<String> propertyNames) throws IOException;

	public abstract boolean deleteConfigRegistryById(String id) throws IOException;

	public abstract boolean deleteConfigRegistryByName(String name) throws IOException;

}

// String indexServiceUrl = Activator.getInstance().getProperty(InfraConstants.ORBIT_INDEX_SERVICE_URL);
// String configRegistryServiceUrl = Activator.getInstance().getProperty(InfraConstants.ORBIT_CONFIG_REGISTRY_URL);