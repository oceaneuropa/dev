package org.orbit.component.runtime.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.origin.common.env.SetupUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformSetupUtil {

	protected static Logger LOG = LoggerFactory.getLogger(PlatformSetupUtil.class);

	public static final String PLATFORM_HOME = "platform.home";

	/**
	 * Load {platform.home}/config.ini properties into given props Map.
	 * 
	 * @param bundleContext
	 * @param collectedProperties
	 */
	public static void loadPlatformConfigProperties(BundleContext bundleContext, Map<Object, Object> collectedProperties) {
		Properties properties = getPlatformConfigProperties(bundleContext);
		// Printer.pl(configIniProps);

		if (properties != null && !properties.isEmpty()) {
			@SuppressWarnings("unchecked")
			Enumeration<String> enumr = ((Enumeration<String>) properties.propertyNames());
			while (enumr.hasMoreElements()) {
				String propName = (String) enumr.nextElement();
				String propValue = properties.getProperty(propName);
				if (propName != null && propValue != null) {
					collectedProperties.put(propName, propValue);
				}
			}
		}
	}

	/**
	 * Get {platform.home}/config.ini Properties.
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Properties getPlatformConfigProperties(BundleContext bundleContext) {
		Path homePath = getHomePath(bundleContext);
		if (homePath == null) {
			return new Properties();
		}
		return SetupUtil.getProperties(homePath, "config.ini");
	}

	/**
	 * Get {platform.home} path.
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Path getHomePath(BundleContext bundleContext) {
		String pathString = null;
		if (bundleContext != null) {
			pathString = bundleContext.getProperty(PLATFORM_HOME);
		}
		if (pathString == null) {
			pathString = System.getProperty(PLATFORM_HOME);
		}
		if (pathString == null) {
			pathString = System.getenv(PLATFORM_HOME);
		}

		if (pathString == null) {
			LOG.error("getHomePath(BundleContext) '" + PLATFORM_HOME + "' property is not found.");
			return null;
		}

		Path homePath = Paths.get(pathString).toAbsolutePath();
		if (!Files.exists(homePath)) {
			throw new RuntimeException(String.format("Platform directory '%s' does not exist.", homePath));
		}

		return homePath;
	}

	/**
	 * Get {platform.home}/apps/ path.
	 * 
	 * @param homePath
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getAppsPath(Path homePath, boolean createIfNotExist) {
		Path appsPath = homePath.resolve("apps");
		if (!Files.exists(appsPath) && createIfNotExist) {
			try {
				appsPath = Files.createDirectory(appsPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appsPath;
	}

	/**
	 * Get {platform.home}/downloads/ path.
	 * 
	 * @param homePath
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getDownloadsPath(Path homePath, boolean createIfNotExist) {
		Path appsPath = homePath.resolve("downloads");
		if (!Files.exists(appsPath) && createIfNotExist) {
			try {
				appsPath = Files.createDirectory(appsPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appsPath;
	}

	/**
	 * Get {platform.home}/plugins/ path.
	 * 
	 * @param homePath
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getPluginsPath(Path homePath, boolean createIfNotExist) {
		Path pluginsPath = homePath.resolve("plugins");
		if (!Files.exists(pluginsPath) && createIfNotExist) {
			try {
				pluginsPath = Files.createDirectory(pluginsPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pluginsPath;
	}

	/**
	 * Get {platform.home}/system/ path.
	 * 
	 * @param homePath
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getSystemPath(Path homePath, boolean createIfNotExist) {
		Path systemPath = homePath.resolve("system");
		if (!Files.exists(systemPath) && createIfNotExist) {
			try {
				systemPath = Files.createDirectory(systemPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return systemPath;
	}

}

// /**
// * Get {TA_HOME}/nodespaces/ path.
// *
// * @param home
// * @param createIfNotExist
// * @return
// */
// public static Path getNodespacesPath(Path home, boolean createIfNotExist) {
// Path nodespacesPath = home.resolve("nodespace");
// if (!Files.exists(nodespacesPath) && createIfNotExist) {
// try {
// nodespacesPath = Files.createDirectory(nodespacesPath);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// return nodespacesPath;
// }

// /**
// * Get {TA_HOME}/nodes/ path.
// *
// * @param taHome
// * @param createIfNotExist
// * @return
// */
// public static Path getNodesPath(Path taHome, boolean createIfNotExist) {
// Path nodesPath = taHome.resolve("nodes");
// if (!Files.exists(nodesPath) && createIfNotExist) {
// try {
// nodesPath = Files.createDirectory(nodesPath);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// return nodesPath;
// }
