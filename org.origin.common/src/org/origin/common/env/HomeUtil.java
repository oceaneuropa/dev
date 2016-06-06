package org.origin.common.env;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.origin.common.io.IOUtil;
import org.osgi.framework.BundleContext;

public class HomeUtil {

	private static Path originHome;
	private static boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 * @return
	 */
	public static Path getOriginHome(BundleContext bundleContext) {
		if (originHome == null) {
			synchronized (HomeUtil.class) {
				if (originHome == null) {
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

					originHome = originHomePath;
				}
			}
		}
		return originHome;
	}

	/**
	 * 
	 * @param originHome
	 * @param filePathStrng
	 * @return
	 */
	public static Properties getProperties(Path originHome, String filePathStrng) {
		Path configFilePath = originHome.resolve(filePathStrng);
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
