package org.orbit.component.server.tier3.transferagent.util;

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

	/**
	 * Get {TA_HOME}/config.ini path.
	 * 
	 * @param taHome
	 * @return
	 */
	public static Path getTAConfigIniPath(Path taHome) {
		Path configIniPath = taHome.resolve("config.ini");
		return configIniPath;
	}

	/**
	 * Get {TA_HOME}/config.ini Properties.
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Properties getTAHomeConfigIniProperties(BundleContext bundleContext) {
		Path taHome = getTAHome(bundleContext);
		if (taHome == null) {
			return new Properties();
		}
		Path configIniPath = getTAConfigIniPath(taHome);
		Properties configIniProps = org.origin.common.env.SetupUtil.getProperties(taHome, "config.ini");
		System.out.println("taHome = " + taHome);
		System.out.println(configIniPath + " properties:");
		Printer.pl(configIniProps);
		return configIniProps;
	}

	/**
	 * Load {TA_HOME}/config.ini properties into given props Map.
	 * 
	 * @param bundleContext
	 * @param props
	 */
	public static void loadTAConfigIniProperties(BundleContext bundleContext, Map<Object, Object> props) {
		// load config.ini properties
		Properties configIniProps = getTAHomeConfigIniProperties(bundleContext);

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
	}

}
