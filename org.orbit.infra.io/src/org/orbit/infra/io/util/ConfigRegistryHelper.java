package org.orbit.infra.io.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;

public class ConfigRegistryHelper {

	protected static final String PLATFORM_ID = "platform.id";
	protected static final String PLATFORM_TYPE = "Platform";
	protected static final String PLATFORM_NAME = "platforms";
	protected static final String SERVICES = "services";
	protected static final String CONFIG_INI = "config.ini";

	public static ConfigRegistryHelper INSTANCE = new ConfigRegistryHelper();

	/**
	 * Get "/platforms" IConfigRegistry.
	 * 
	 * @param accessToken
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigRegistry getPlatformsRegistry(String accessToken, boolean createIfNotExist) throws IOException {
		IConfigRegistry registry = null;
		CFG cfg = CFG.getDefault(accessToken);
		if (cfg != null && cfg.isOnline()) {
			registry = cfg.getConfigRegistryByName(PLATFORM_NAME);
			if (registry == null) {
				if (createIfNotExist) {
					registry = cfg.createConfigRegistry(PLATFORM_TYPE, PLATFORM_NAME, null, false);
				}
			}
		}
		return registry;
	}

	// /**
	// * Get "/{platformId}/services" IConfigElement
	// *
	// * @param registry
	// * @param platformId
	// * @return
	// * @throws IOException
	// */
	// private synchronized IConfigElement getServicesElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
	// if (registry == null || platformId == null) {
	// return null;
	// }
	//
	// IConfigElement servicesElement = null;
	// IConfigElement platformElement = getPlatformElement(registry, platformId, createIfNotExist);
	// if (platformElement != null) {
	// servicesElement = platformElement.getMemberConfigElement(SERVICES);
	// }
	// if (servicesElement == null && createIfNotExist) {
	// Map<String, Object> attributes = new HashMap<String, Object>();
	// servicesElement = platformElement.createMemberConfigElement(SERVICES, attributes, false);
	// }
	// return servicesElement;
	// }

	// /**
	// * Get "/{platformId}/services/{serviceName}" IConfigElement
	// *
	// * @param registry
	// * @param platformId
	// * @param serviceName
	// * @param createIfNotExist
	// * @return
	// * @throws IOException
	// */
	// private synchronized IConfigElement getServiceElement(IConfigRegistry registry, String platformId, String serviceName, boolean createIfNotExist) throws
	// IOException {
	// if (registry == null || platformId == null || serviceName == null) {
	// return null;
	// }
	//
	// IConfigElement serviceElement = null;
	// IConfigElement servicesElement = getServicesElement(registry, platformId, createIfNotExist);
	// if (servicesElement != null) {
	// serviceElement = servicesElement.getMemberConfigElement(serviceName);
	// }
	// if (serviceElement == null && createIfNotExist) {
	// Map<String, Object> attributes = new HashMap<String, Object>();
	// serviceElement = servicesElement.createMemberConfigElement(serviceName, attributes, false);
	// }
	// return serviceElement;
	// }

	// /**
	// * Get "/{platformId}/services/{serviceName}/config.ini" IConfigElement
	// *
	// * @param registry
	// * @param platformId
	// * @param serviceName
	// * @param createIfNotExist
	// * @return
	// * @throws IOException
	// */
	// private synchronized IConfigElement getServiceConfigIniElement(IConfigRegistry registry, String platformId, String serviceName, boolean createIfNotExist)
	// throws IOException {
	// if (registry == null || platformId == null || serviceName == null) {
	// return null;
	// }
	//
	// IConfigElement configIniElement = null;
	// IConfigElement serviceElement = getServiceElement(registry, platformId, serviceName, createIfNotExist);
	// if (serviceElement != null) {
	// configIniElement = serviceElement.getMemberConfigElement(CONFIG_INI);
	// }
	// if (configIniElement == null && createIfNotExist) {
	// Map<String, Object> attributes = new HashMap<String, Object>();
	// configIniElement = serviceElement.createMemberConfigElement(CONFIG_INI, attributes, false);
	// }
	// return configIniElement;
	// }

	/**
	 * Get "/platforms/{platformId}" IConfigElement.
	 * 
	 * @param registry
	 * @param platformId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPlatformElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		IConfigElement platformElement = registry.getRootConfigElement(platformId);
		if (platformElement == null && createIfNotExist) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put(PLATFORM_ID, platformId);
			platformElement = registry.createRootConfigElement(platformId, attributes, true);
		}
		return platformElement;
	}

	/**
	 * Get "/platforms/{platformId}/plugins" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPluginsElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		String name = "plugins";

		IConfigElement pluginsElement = null;
		if (createIfNotExist) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, true);
			pluginsElement = platformElement.getMemberConfigElement(name);
			if (pluginsElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				pluginsElement = platformElement.createMemberConfigElement(name, attributes, false);
			}
		} else {
			IConfigElement platformElement = getPlatformElement(registry, platformId, false);
			if (platformElement != null) {
				pluginsElement = platformElement.getMemberConfigElement(name);
			}
		}
		return pluginsElement;
	}

	/**
	 * Get "/platforms/{platformId}/programs" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getProgramsElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		String name = "programs";

		IConfigElement programsElement = null;
		if (createIfNotExist) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, true);
			programsElement = platformElement.getMemberConfigElement(name);
			if (programsElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				programsElement = platformElement.createMemberConfigElement(name, attributes, false);
			}
		} else {
			IConfigElement platformElement = getPlatformElement(registry, platformId, false);
			if (platformElement != null) {
				programsElement = platformElement.getMemberConfigElement(name);
			}
		}
		return programsElement;
	}

	/**
	 * Get "/platforms/{platformId}/programs"'s children elements.
	 * 
	 * @param registry
	 * @param platformId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement[] getProgramsChildrenElements(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return new IConfigElement[0];
		}

		IConfigElement[] programElements = null;
		IConfigElement programsElement = getProgramsElement(registry, platformId, createIfNotExist);
		if (programsElement != null) {
			programElements = programsElement.memberConfigElements();
		}

		if (programElements == null) {
			programElements = new IConfigElement[0];
		}
		return programElements;
	}

	/**
	 * Get "/platforms/{platformId}/{bundleSymbolicName}" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getBundleElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		IConfigElement bundleElement = null;
		if (createIfNotExist) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, true);
			bundleElement = platformElement.getMemberConfigElement(bundleSymbolicName);
			if (bundleElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				bundleElement = platformElement.createMemberConfigElement(bundleSymbolicName, attributes, false);
			}
		} else {
			IConfigElement platformElement = getPlatformElement(registry, platformId, false);
			if (platformElement != null) {
				bundleElement = platformElement.getMemberConfigElement(bundleSymbolicName);
			}
		}
		return bundleElement;
	}

	/**
	 * Get "/platforms/{platformId}/{bundleSymbolicName}/services" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getBundleServicesElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		IConfigElement servicesElement = null;
		if (createIfNotExist) {
			IConfigElement bundleElement = getBundleElement(registry, platformId, bundleSymbolicName, true);
			servicesElement = bundleElement.getMemberConfigElement(SERVICES);
			if (servicesElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				servicesElement = bundleElement.createMemberConfigElement(SERVICES, attributes, false);
			}
		} else {
			IConfigElement bundleElement = getBundleElement(registry, platformId, bundleSymbolicName, false);
			if (bundleElement != null) {
				servicesElement = bundleElement.getMemberConfigElement(SERVICES);
			}
		}
		return servicesElement;
	}

	/**
	 * Get "/platforms/{platformId}/{bundleSymbolicName}/services/{serviceName}" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param serviceName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getBundleServiceElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		IConfigElement serviceElement = null;
		if (createIfNotExist) {
			IConfigElement bundleServicesElement = getBundleServicesElement(registry, platformId, bundleSymbolicName, true);
			serviceElement = bundleServicesElement.getMemberConfigElement(serviceName);
			if (serviceElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				serviceElement = bundleServicesElement.createMemberConfigElement(serviceName, attributes, false);
			}
		} else {
			IConfigElement bundleServicesElement = getBundleServicesElement(registry, platformId, bundleSymbolicName, false);
			if (bundleServicesElement != null) {
				serviceElement = bundleServicesElement.getMemberConfigElement(serviceName);
			}
		}
		return serviceElement;
	}

	/**
	 * Get "/platforms/{platformId}/{bundleSymbolicName}/services/{serviceName}/config.ini" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param serviceName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getBundleServiceConfigIniElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		IConfigElement configIniElement = null;
		if (createIfNotExist) {
			IConfigElement bundleServiceElement = getBundleServiceElement(registry, platformId, bundleSymbolicName, serviceName, true);
			configIniElement = bundleServiceElement.getMemberConfigElement(CONFIG_INI);
			if (configIniElement == null && createIfNotExist) {
				Map<String, Object> attributes = new HashMap<String, Object>();
				configIniElement = bundleServiceElement.createMemberConfigElement(CONFIG_INI, attributes, false);
			}
		} else {
			IConfigElement bundleServiceElement = getBundleServiceElement(registry, platformId, bundleSymbolicName, serviceName, false);
			if (bundleServiceElement != null) {
				configIniElement = bundleServiceElement.getMemberConfigElement(CONFIG_INI);
			}
		}
		return configIniElement;
	}

}
