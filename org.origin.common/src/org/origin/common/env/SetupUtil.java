package org.origin.common.env;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.origin.common.io.IOUtil;
import org.osgi.framework.BundleContext;

public class SetupUtil {

	private static boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static synchronized Path getHomePath(BundleContext bundleContext, String homePropName) {
		String homePathStr = null;

		// load property from bundle context
		if (bundleContext != null) {
			homePathStr = bundleContext.getProperty(homePropName);
		}

		// load property from system property
		if (homePathStr == null) {
			homePathStr = System.getProperty(homePropName);
		}

		// load property from environment variable
		if (homePathStr == null) {
			homePathStr = System.getenv(homePropName);
		}

		if (homePathStr == null) {
			System.err.println("SetupUtil.getHomePath() cannot find property value for '" + homePropName + "'.");
			return null;
		}

		Path homePath = Paths.get(homePathStr).toAbsolutePath();
		if (!Files.exists(homePath)) {
			throw new RuntimeException("Path '" + homePath + "' does not exist.");
		}

		return homePath;
	}

	/**
	 * 
	 * @param props
	 * @param homePropName
	 * @return
	 */
	public static synchronized Path getHomePath(Map<Object, Object> props, String homePropName) {
		String homePathStr = (String) props.get(homePropName);

		if (homePathStr == null) {
			System.err.println("SetupUtil.getHomePath() cannot find property value for '" + homePropName + "'.");
			return null;
		}

		Path homePath = Paths.get(homePathStr).toAbsolutePath();
		if (!Files.exists(homePath)) {
			throw new RuntimeException("Path '" + homePath + "' does not exist.");
		}

		return homePath;
	}

	/**
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Path getOriginHome(BundleContext bundleContext) {
		String propName = "ORIGIN_HOME";

		String originHomeStr = null;

		// load property from bundle context
		if (bundleContext != null) {
			originHomeStr = bundleContext.getProperty(propName);
		}

		// load property from system property
		if (originHomeStr == null) {
			originHomeStr = System.getProperty(propName);
		}

		// load property from environment variable
		if (originHomeStr == null) {
			originHomeStr = System.getenv(propName);
		}

		if (originHomeStr == null) {
			System.err.println("HomeUtil.getOriginHome() cannot find property value for '" + propName + "'.");
			return null;
		}

		Path originHomePath = Paths.get(originHomeStr).toAbsolutePath();

		if (!Files.exists(originHomePath)) {
			throw new RuntimeException("Path '" + originHomePath + "' does not exist.");
		}

		return originHomePath;
	}

	/**
	 * 
	 * @param homePath
	 * @param filePathStrng
	 * @return
	 */
	public static Properties getProperties(Path homePath, String filePathStrng) {
		Path configFilePath = homePath.resolve(filePathStrng);
		if (!Files.exists(configFilePath)) {
			if (debug) {
				System.err.println("File '" + configFilePath + "' does not exist.");
			}
			return null;
		}

		Properties configProperties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(configFilePath.toFile());
			configProperties.load(fis);

		} catch (IOException e) {
			e.printStackTrace();
			if (debug) {
				System.err.println("Failed to read config file '" + configFilePath + "'.");
			}
			return null;
		} finally {
			IOUtil.closeQuietly(fis, true);
		}
		return configProperties;
	}

}
