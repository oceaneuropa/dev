package org.orbit.infra.io.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.origin.common.resource.Path;

public class ConfigRegistryHelper {

	protected static final String PLATFORM_ID = "platform.id";
	protected static final String PLATFORM_TYPE = "Platform";
	protected static final String PLATFORM_NAME = "platforms";
	protected static final String SERVICES = "services";
	protected static final String CONFIG_INI = "config.ini";

	public static ConfigRegistryHelper INSTANCE = new ConfigRegistryHelper();

	/**
	 * Get "platforms" IConfigRegistry.
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

	/**
	 * Get "[platforms]/{platformId}" IConfigElement.
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
	 * Get "[platforms]/{platformId}/programs" element.
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

		String pathString = MessageFormat.format("/{0}/programs", new Object[] { platformId });
		IConfigElement programsElement = registry.getConfigElement(new Path(pathString));

		if (programsElement == null) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, createIfNotExist);
			if (platformElement != null) {
				programsElement = platformElement.getMemberConfigElement(name);

				if (programsElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					programsElement = platformElement.createMemberConfigElement(name, attributes, false);
				}
			}
		}
		return programsElement;
	}

	/**
	 * Get "[platforms]/{platformId}/programs"'s children elements.
	 * 
	 * @param registry
	 * @param platformId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement[] getProgramsElementChildren(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return new IConfigElement[0];
		}

		IConfigElement[] childrenElements = null;
		IConfigElement programsElement = getProgramsElement(registry, platformId, createIfNotExist);
		if (programsElement != null) {
			childrenElements = programsElement.memberConfigElements();
		}

		if (childrenElements == null) {
			childrenElements = new IConfigElement[0];
		}
		return childrenElements;
	}

	/**
	 * Get "[platforms]/{platformId}/plugins" element.
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

		String pathString = MessageFormat.format("/{0}/plugins", new Object[] { platformId });
		IConfigElement pluginsElement = registry.getConfigElement(new Path(pathString));

		if (pluginsElement == null) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, createIfNotExist);
			if (platformElement != null) {
				pluginsElement = platformElement.getMemberConfigElement(name);

				if (pluginsElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					pluginsElement = platformElement.createMemberConfigElement(name, attributes, false);
				}
			}
		}
		return pluginsElement;
	}

	/**
	 * Get "[platforms]/{platformId}/plugins/{bundleSymbolicName}" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPluginsElementChild(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}", new Object[] { platformId, bundleSymbolicName });
		IConfigElement bundleElement = registry.getConfigElement(new Path(pathString));

		if (bundleElement == null) {
			IConfigElement pluginsElement = getPluginsElement(registry, platformId, createIfNotExist);
			if (pluginsElement != null) {
				bundleElement = pluginsElement.getMemberConfigElement(bundleSymbolicName);

				if (bundleElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					bundleElement = pluginsElement.createMemberConfigElement(bundleSymbolicName, attributes, false);
				}
			}
		}
		return bundleElement;
	}

	/**
	 * Get "[platforms]/{platformId}/plugins/{bundleSymbolicName}/services" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPluginServicesElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services", new Object[] { platformId, bundleSymbolicName });
		IConfigElement servicesElement = registry.getConfigElement(new Path(pathString));

		if (servicesElement == null) {
			IConfigElement bundleElement = getPluginsElementChild(registry, platformId, bundleSymbolicName, createIfNotExist);
			if (bundleElement != null) {
				servicesElement = bundleElement.getMemberConfigElement(SERVICES);

				if (servicesElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					servicesElement = bundleElement.createMemberConfigElement(SERVICES, attributes, false);
				}
			}
		}
		return servicesElement;
	}

	/**
	 * Get "[platforms]/{platformId}/plugins/{bundleSymbolicName}/services/{serviceName}" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param serviceName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPluginServicesElementChild(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services/{2}", new Object[] { platformId, bundleSymbolicName, serviceName });
		IConfigElement serviceElement = registry.getConfigElement(new Path(pathString));

		if (serviceElement == null) {
			IConfigElement bundleServicesElement = getPluginServicesElement(registry, platformId, bundleSymbolicName, createIfNotExist);
			if (bundleServicesElement != null) {
				serviceElement = bundleServicesElement.getMemberConfigElement(serviceName);

				if (serviceElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					serviceElement = bundleServicesElement.createMemberConfigElement(serviceName, attributes, false);
				}
			}
		}
		return serviceElement;
	}

	/**
	 * Get "[platforms]/{platformId}/plugins/{bundleSymbolicName}/services/{serviceName}/config.ini" element.
	 * 
	 * @param registry
	 * @param platformId
	 * @param bundleSymbolicName
	 * @param serviceName
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized IConfigElement getPluginServiceConfigIniElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services/{2}/config.ini", new Object[] { platformId, bundleSymbolicName, serviceName });
		IConfigElement configIniElement = registry.getConfigElement(new Path(pathString));

		if (configIniElement == null) {
			IConfigElement bundleServiceElement = getPluginServicesElementChild(registry, platformId, bundleSymbolicName, serviceName, createIfNotExist);
			if (bundleServiceElement != null) {
				configIniElement = bundleServiceElement.getMemberConfigElement(CONFIG_INI);

				if (configIniElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					configIniElement = bundleServiceElement.createMemberConfigElement(CONFIG_INI, attributes, false);
				}
			}
		}
		return configIniElement;
	}

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

// /**
// * Get "[platforms]/{platformId}/plugins"'s children elements.
// *
// * @param registry
// * @param platformId
// * @param createIfNotExist
// * @return
// * @throws IOException
// */
// public synchronized IConfigElement[] getPluginsElementChildren(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException
// {
// if (registry == null || platformId == null) {
// return null;
// }
//
// IConfigElement[] childrenElements = null;
// IConfigElement pluginsElement = getPluginsElement(registry, platformId, createIfNotExist);
// if (pluginsElement != null) {
// childrenElements = pluginsElement.memberConfigElements();
// }
//
// if (childrenElements == null) {
// childrenElements = new IConfigElement[0];
// }
// return childrenElements;
// }
