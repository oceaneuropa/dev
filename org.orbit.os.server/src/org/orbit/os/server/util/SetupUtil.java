package org.orbit.os.server.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.origin.common.util.Printer;
import org.osgi.framework.BundleContext;

public class SetupUtil {

	public static final String PROP_TA_HOME = "TA_HOME";
	public static final String PROP_NODE_HOME = "NODE_HOME";

	// -----------------------------------------------------
	// TA
	// -----------------------------------------------------
	/**
	 * Get {TA_HOME}
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Path getTAHome(BundleContext bundleContext) {
		String pathString = null;
		if (bundleContext != null) {
			pathString = bundleContext.getProperty(PROP_TA_HOME);
		}
		if (pathString == null) {
			pathString = System.getProperty(PROP_TA_HOME);
		}
		if (pathString == null) {
			pathString = System.getenv(PROP_TA_HOME);
		}
		if (pathString == null) {
			System.err.println(SetupUtil.class.getSimpleName() + ".getTAHome() " + PROP_TA_HOME + " is not set.");
			return null;
		}

		Path taHome = Paths.get(pathString).toAbsolutePath();
		if (!Files.exists(taHome)) {
			throw new RuntimeException(String.format("Directory '%s' does not exist.", taHome));
		}
		return taHome;
	}

	/**
	 * Get {TA_HOME}/apps/ path.
	 * 
	 * @param taHome
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getTAAppsPath(Path taHome, boolean createIfNotExist) {
		Path appsPath = taHome.resolve("apps");
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
	 * Get {TA_HOME}/downloads/ path.
	 * 
	 * @param taHome
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getTADownloadsPath(Path taHome, boolean createIfNotExist) {
		Path appsPath = taHome.resolve("downloads");
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
	 * Get {TA_HOME}/nodes/ path.
	 * 
	 * @param taHome
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getTANodesPath(Path taHome, boolean createIfNotExist) {
		Path nodesPath = taHome.resolve("nodes");
		if (!Files.exists(nodesPath) && createIfNotExist) {
			try {
				nodesPath = Files.createDirectory(nodesPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return nodesPath;
	}

	/**
	 * Get {TA_HOME}/system/ path.
	 * 
	 * @param taHome
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getTASystemPath(Path taHome, boolean createIfNotExist) {
		Path systemPath = taHome.resolve("system");
		if (!Files.exists(systemPath) && createIfNotExist) {
			try {
				systemPath = Files.createDirectory(systemPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return systemPath;
	}

	// -----------------------------------------------------
	// Node
	// -----------------------------------------------------
	/**
	 * Get {NODE_HOME}
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Path getNodeHome(BundleContext bundleContext) {
		String pathString = null;
		if (bundleContext != null) {
			pathString = bundleContext.getProperty(PROP_NODE_HOME);
		}
		if (pathString == null) {
			pathString = System.getProperty(PROP_NODE_HOME);
		}
		if (pathString == null) {
			pathString = System.getenv(PROP_NODE_HOME);
		}
		if (pathString == null) {
			System.err.println(SetupUtil.class.getSimpleName() + ".getNodeHome() " + PROP_NODE_HOME + " is not set.");
			return null;
		}

		Path nodeHome = Paths.get(pathString).toAbsolutePath();
		if (!Files.exists(nodeHome)) {
			throw new RuntimeException(String.format("Directory '%s' does not exist.", nodeHome));
		}
		return nodeHome;
	}

	/**
	 * Get {NODE_HOME}/apps/ path.
	 * 
	 * @param nodeHome
	 * @param createIfNotExist
	 * @return
	 */
	public static Path getNodeAppsPath(Path nodeHome, boolean createIfNotExist) {
		Path appsPath = nodeHome.resolve("apps");
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
	 * Get {NODE_HOME}/configuration/config.ini path.
	 * 
	 * @param nodeHome
	 * @return
	 */
	public static Path getNodeConfigIniPath(Path nodeHome) {
		Path configIniPath = nodeHome.resolve("configuration").resolve("config.ini");
		return configIniPath;
	}

	/**
	 * Get {NODE_HOME}/config.ini Properties.
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Properties getNodeHomeConfigIniProperties(BundleContext bundleContext) {
		Path nodeHome = getNodeHome(bundleContext);
		if (nodeHome == null) {
			return new Properties();
		}
		Path configIniPath = getNodeConfigIniPath(nodeHome);
		Properties configIniProps = org.origin.common.env.SetupUtil.getProperties(nodeHome, "configuration/config.ini");
		System.out.println("nodeHome = " + nodeHome);
		System.out.println(configIniPath + " properties:");
		Printer.pl(configIniProps);
		return configIniProps;
	}

	/**
	 * Load {NODE_HOME}/config.ini properties into given props Map.
	 * 
	 * @param bundleContext
	 * @param props
	 */
	public static void loadNodeConfigIniProperties(BundleContext bundleContext, Map<Object, Object> props) {
		try {
			// load config.ini properties
			Properties configIniProps = getNodeHomeConfigIniProperties(bundleContext);

			if (configIniProps != null && !configIniProps.isEmpty()) {
				@SuppressWarnings("unchecked")
				Enumeration<String> enumr = ((Enumeration<String>) configIniProps.propertyNames());
				while (enumr.hasMoreElements()) {
					String propName = (String) enumr.nextElement();
					String propValue = configIniProps.getProperty(propName);
					if (propName != null && propValue != null) {
						props.put(propName, propValue);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
