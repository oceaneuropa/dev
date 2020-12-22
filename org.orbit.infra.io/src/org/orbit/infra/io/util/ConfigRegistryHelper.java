package org.orbit.infra.io.util;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.CFGFactory;
import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.origin.common.osgi.OSGiVersionUtil;
import org.origin.common.resource.Path;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ConfigRegistryHelper {

	protected static final String REGISTRY__PLATFORMS = "platforms";
	protected static final String TYPE__PLATFORM = "Platform";

	protected static final String PLATFORM_ID = "platform.id";
	protected static final String SERVICES = "services";
	protected static final String CONFIG_INI = "config.ini";

	protected static final String REGISTRY__USERS = "Users";
	protected static final String TYPE__SETTINGS = "Settings";

	/**
	 * Get "platforms" IConfigRegistry.
	 * 
	 * @param accessToken
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public static synchronized IConfigRegistry getPlatformsRegistry(String accessToken, boolean createIfNotExist) throws IOException {
		IConfigRegistry registry = null;
		CFG cfg = CFGFactory.getInstance().createCFG(accessToken);
		if (cfg != null && cfg.isOnline()) {
			registry = cfg.getConfigRegistryByName(REGISTRY__PLATFORMS);
			if (registry == null) {
				if (createIfNotExist) {
					registry = cfg.createConfigRegistry(TYPE__PLATFORM, REGISTRY__PLATFORMS, null, false);
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
	public static synchronized IConfigElement getPlatformElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		IConfigElement platformElement = registry.getRootElement(platformId);

		if (platformElement == null && createIfNotExist) {
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put(PLATFORM_ID, platformId);
			platformElement = registry.createRootElement(platformId, attributes, true);
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
	public static synchronized IConfigElement getProgramsElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		String name = "programs";

		String pathString = MessageFormat.format("/{0}/programs", new Object[] { platformId });
		IConfigElement programsElement = registry.getElement(new Path(pathString));

		if (programsElement == null) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, createIfNotExist);
			if (platformElement != null) {
				programsElement = platformElement.getChildElement(name);

				if (programsElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					programsElement = platformElement.createChildElement(name, attributes, false);
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
	public static synchronized IConfigElement[] getProgramsElementChildren(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return new IConfigElement[0];
		}

		IConfigElement[] childrenElements = null;
		IConfigElement programsElement = getProgramsElement(registry, platformId, createIfNotExist);
		if (programsElement != null) {
			childrenElements = programsElement.getChildrenElements();
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
	public static synchronized IConfigElement getPluginsElement(IConfigRegistry registry, String platformId, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null) {
			return null;
		}

		String name = "plugins";

		String pathString = MessageFormat.format("/{0}/plugins", new Object[] { platformId });
		IConfigElement pluginsElement = registry.getElement(new Path(pathString));

		if (pluginsElement == null) {
			IConfigElement platformElement = getPlatformElement(registry, platformId, createIfNotExist);
			if (platformElement != null) {
				pluginsElement = platformElement.getChildElement(name);

				if (pluginsElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					pluginsElement = platformElement.createChildElement(name, attributes, false);
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
	public static synchronized IConfigElement getPluginsElementChild(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}", new Object[] { platformId, bundleSymbolicName });
		IConfigElement bundleElement = registry.getElement(new Path(pathString));

		if (bundleElement == null) {
			IConfigElement pluginsElement = getPluginsElement(registry, platformId, createIfNotExist);
			if (pluginsElement != null) {
				bundleElement = pluginsElement.getChildElement(bundleSymbolicName);

				if (bundleElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					bundleElement = pluginsElement.createChildElement(bundleSymbolicName, attributes, false);
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
	public static synchronized IConfigElement getPluginServicesElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services", new Object[] { platformId, bundleSymbolicName });
		IConfigElement servicesElement = registry.getElement(new Path(pathString));

		if (servicesElement == null) {
			IConfigElement bundleElement = getPluginsElementChild(registry, platformId, bundleSymbolicName, createIfNotExist);
			if (bundleElement != null) {
				servicesElement = bundleElement.getChildElement(SERVICES);

				if (servicesElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					servicesElement = bundleElement.createChildElement(SERVICES, attributes, false);
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
	public static synchronized IConfigElement getPluginServicesElementChild(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services/{2}", new Object[] { platformId, bundleSymbolicName, serviceName });
		IConfigElement serviceElement = registry.getElement(new Path(pathString));

		if (serviceElement == null) {
			IConfigElement bundleServicesElement = getPluginServicesElement(registry, platformId, bundleSymbolicName, createIfNotExist);
			if (bundleServicesElement != null) {
				serviceElement = bundleServicesElement.getChildElement(serviceName);

				if (serviceElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					serviceElement = bundleServicesElement.createChildElement(serviceName, attributes, false);
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
	public static synchronized IConfigElement getPluginServiceConfigIniElement(IConfigRegistry registry, String platformId, String bundleSymbolicName, String serviceName, boolean createIfNotExist) throws IOException {
		if (registry == null || platformId == null || bundleSymbolicName == null || serviceName == null) {
			return null;
		}

		String pathString = MessageFormat.format("/{0}/plugins/{1}/services/{2}/config.ini", new Object[] { platformId, bundleSymbolicName, serviceName });
		IConfigElement configIniElement = registry.getElement(new Path(pathString));

		if (configIniElement == null) {
			IConfigElement bundleServiceElement = getPluginServicesElementChild(registry, platformId, bundleSymbolicName, serviceName, createIfNotExist);
			if (bundleServiceElement != null) {
				configIniElement = bundleServiceElement.getChildElement(CONFIG_INI);

				if (configIniElement == null && createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					configIniElement = bundleServiceElement.createChildElement(CONFIG_INI, attributes, false);
				}
			}
		}
		return configIniElement;
	}

	/**
	 * 
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigRegistry getUsersConfigRegistry(String accessToken) throws IOException {
		IConfigRegistry usersConfigReg = null;
		CFG cfg = CFGFactory.getInstance().createCFG(accessToken);
		if (cfg != null) {
			usersConfigReg = cfg.getConfigRegistryByName(REGISTRY__USERS);
			if (usersConfigReg == null) {
				usersConfigReg = cfg.createConfigRegistry(TYPE__SETTINGS, REGISTRY__USERS, null, false);
			}
		}
		return usersConfigReg;
	}

	/**
	 * /Users/<username>/
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement getUserElement(IConfigRegistry usersConfigReg, String username, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}

		IConfigElement userElement = null;
		if (usersConfigReg != null && username != null) {
			IConfigElement[] userElements = usersConfigReg.listRootElements();
			if (userElements != null) {
				for (IConfigElement currUserElement : userElements) {
					String currUsername = currUserElement.getAttribute("username", String.class);
					if (username.equals(currUsername)) {
						userElement = currUserElement;
						break;
					}
				}
			}

			if (userElement == null) {
				if (createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("username", username);
					userElement = usersConfigReg.createRootElement(username, attributes, false);
				}
			}
		}
		return userElement;
	}

	/**
	 * /Users/<username>/installation/
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement getUserInstallationElement(IConfigRegistry usersConfigReg, String username, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}

		IConfigElement installationElement = null;

		IConfigElement userElement = getUserElement(usersConfigReg, username, createIfNotExist);
		if (userElement != null) {
			installationElement = userElement.getChildElement("installation");
			if (installationElement == null) {
				if (createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					installationElement = userElement.createChildElement("installation", attributes, false);
				}
			}
		}

		return installationElement;
	}

	/**
	 * /Users/<username>/installation/<children>
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement[] getUserInstallationProgramElements(IConfigRegistry usersConfigReg, String username, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}

		IConfigElement[] childrenElements = null;
		IConfigElement installationElement = getUserInstallationElement(usersConfigReg, username, createIfNotExist);
		if (installationElement != null) {
			childrenElements = installationElement.getChildrenElements();
		}
		if (childrenElements == null) {
			childrenElements = new IConfigElement[0];
		}
		return childrenElements;
	}

	/**
	 * /Users/<username>/programs/<programId>
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param programId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement getUserInstallationProgramElement(IConfigRegistry usersConfigReg, String username, String programId, String programVersion, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}
		if (programId == null) {
			throw new IllegalArgumentException("programId is null.");
		}
		if (programVersion == null) {
			throw new IllegalArgumentException("programVersion is null.");
		}

		IConfigElement programElement = null;

		IConfigElement installationElement = getUserInstallationElement(usersConfigReg, username, createIfNotExist);
		if (installationElement != null) {
			String name = OSGiVersionUtil.getProgramIdAndVersionString(programId, programVersion, "|");

			IConfigElement[] childrenProgramElements = installationElement.getChildrenElements();
			for (IConfigElement childProgramElement : childrenProgramElements) {
				String currName = childProgramElement.getName();
				if (name.equals(currName)) {
					programElement = childProgramElement;
					break;
				}
			}

			if (programElement == null) {
				if (createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put("programId", programId);
					attributes.put("programVersion", programVersion);
					programElement = installationElement.createChildElement(name, attributes, false);
				}
			}
		}

		return programElement;
	}

	/**
	 * /Users/<username>/system/
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement getUserSystemElement(IConfigRegistry usersConfigReg, String username, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}

		IConfigElement systemElement = null;

		IConfigElement userElement = getUserElement(usersConfigReg, username, createIfNotExist);
		if (userElement != null) {
			systemElement = userElement.getChildElement("system");
			if (systemElement == null) {
				if (createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					systemElement = userElement.createChildElement("system", attributes, false);
				}
			}
		}

		return systemElement;
	}

	/**
	 * /Users/<username>/programs/<programId>
	 * 
	 * @param usersConfigReg
	 * @param username
	 * @param programId
	 * @param createIfNotExist
	 * @return
	 * @throws IOException
	 */
	public synchronized static IConfigElement getUserProgramElement(IConfigRegistry usersConfigReg, String username, String programId, boolean createIfNotExist) throws IOException {
		if (usersConfigReg == null) {
			throw new IllegalArgumentException("usersConfigReg is null.");
		}
		if (username == null) {
			throw new IllegalArgumentException("username is null.");
		}
		if (programId == null) {
			throw new IllegalArgumentException("programId is null.");
		}

		IConfigElement programElement = null;

		IConfigElement userElement = getUserElement(usersConfigReg, username, createIfNotExist);
		if (userElement != null) {
			IConfigElement programsElement = userElement.getChildElement("programs");
			if (programsElement == null) {
				if (createIfNotExist) {
					Map<String, Object> attributes = new HashMap<String, Object>();
					programsElement = userElement.createChildElement("programs", attributes, false);
				}
			}

			if (programsElement != null) {
				programElement = programsElement.getChildElement(programId);
				if (programElement == null) {
					if (createIfNotExist) {
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put("programId", programId);
						programElement = programsElement.createChildElement(programId, attributes, false);
					}
				}
			}
		}

		return programElement;
	}

}

// /**
// * /Users/<username>/programs/<programId>/<programVersion>/
// *
// * @param usersConfigReg
// * @param username
// * @param programId
// * @param programVersion
// * @param createIfNotExist
// * @return
// * @throws IOException
// */
// private synchronized static IConfigElement getUserProgramElement(IConfigRegistry usersConfigReg, String username, String programId, String programVersion, boolean createIfNotExist) throws IOException {
// if (usersConfigReg == null) {
// throw new IllegalArgumentException("usersConfigReg is null.");
// }
// if (username == null) {
// throw new IllegalArgumentException("username is null.");
// }
// if (programId == null) {
// throw new IllegalArgumentException("programId is null.");
// }
// if (programVersion == null) {
// throw new IllegalArgumentException("programVersion is null.");
// }
//
// IConfigElement programVersionElement = null;
//
// IConfigElement userElement = getUserElement(usersConfigReg, username, createIfNotExist);
// if (userElement != null) {
// IConfigElement programsElement = userElement.getChildElement("programs");
// if (programsElement == null) {
// if (createIfNotExist) {
// Map<String, Object> attributes = new HashMap<String, Object>();
// programsElement = userElement.createChildElement("programs", attributes, false);
// }
// }
//
// if (programsElement != null) {
// IConfigElement programElement = programsElement.getChildElement(programId);
// if (programElement == null) {
// if (createIfNotExist) {
// Map<String, Object> attributes = new HashMap<String, Object>();
// attributes.put("programId", programId);
// programElement = programsElement.createChildElement(programId, attributes, false);
// }
// }
//
// if (programElement != null) {
// if (programVersion == null || programVersion.isEmpty()) {
// programVersion = "0.0.0";
// }
//
// programVersionElement = programElement.getChildElement(programVersion);
// if (programVersionElement == null) {
// if (createIfNotExist) {
// Map<String, Object> attributes = new HashMap<String, Object>();
// attributes.put("programId", programId);
// attributes.put("programVersion", programVersion);
// programVersionElement = programElement.createChildElement(programVersion, attributes, false);
// }
// }
// }
// }
// }
//
// return programVersionElement;
// }

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
