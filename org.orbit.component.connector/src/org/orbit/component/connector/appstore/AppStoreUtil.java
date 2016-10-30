package org.orbit.component.connector.appstore;

import java.util.Properties;

import org.origin.common.rest.client.ClientConfiguration;

public class AppStoreUtil {

	public static final String APPSTORE_URL = "appstore.url";
	public static final String APPSTORE_CONTEXT_ROOT = "appstore.context_root";
	public static final String APPSTORE_USERNAME = "appstore.username";
	public static final String APPSTORE_PASSWORD = "appstore.password";

	/**
	 * Create app store connection properties.
	 * 
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static Properties getProperties(String url, String contextRoot, String username, String password) {
		Properties properties = new Properties();
		properties.setProperty(APPSTORE_URL, url);
		properties.setProperty(APPSTORE_CONTEXT_ROOT, contextRoot);
		properties.setProperty(APPSTORE_USERNAME, username);
		properties.setProperty(APPSTORE_PASSWORD, password);
		return properties;
	}

	/**
	 * Get app store client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	public static ClientConfiguration getClientConfiguration(Properties properties) {
		String url = properties.getProperty(APPSTORE_URL);
		String contextRoot = properties.getProperty(APPSTORE_CONTEXT_ROOT);
		String username = properties.getProperty(APPSTORE_USERNAME);
		String password = properties.getProperty(APPSTORE_PASSWORD);
		return getClientConfiguration(url, contextRoot, username, password);
	}

	/**
	 * Get app store client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	public static ClientConfiguration getClientConfiguration(String url, String contextRoot, String username, String password) {
		return ClientConfiguration.get(url, contextRoot, username, password);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static String getLoadBalanceId(Properties properties) {
		String url = properties.getProperty(APPSTORE_URL);
		String contextRoot = properties.getProperty(APPSTORE_CONTEXT_ROOT);
		String username = properties.getProperty(APPSTORE_USERNAME);
		String key = url + "::" + contextRoot + "::" + username;
		return key;
	}

}
