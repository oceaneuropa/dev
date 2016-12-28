package org.orbit.component.connector.appstore;

import java.util.Map;

import org.orbit.component.connector.OrbitConstants;
import org.origin.common.rest.client.ClientConfiguration;

public class AppStoreUtil {

	/**
	 * Get app store client configuration.
	 * 
	 * @param properties
	 * @return
	 */
	public static ClientConfiguration getClientConfiguration(Map<String, Object> properties) {
		String url = (String) properties.get(OrbitConstants.APPSTORE_URL);
		String contextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
		return ClientConfiguration.get(url, contextRoot, null, null);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public static String getLoadBalanceId(Map<String, Object> properties) {
		String appStoreName = (String) properties.get(OrbitConstants.APPSTORE_NAME);
		String url = (String) properties.get(OrbitConstants.APPSTORE_URL);
		String contextRoot = (String) properties.get(OrbitConstants.APPSTORE_CONTEXT_ROOT);
		String key = url + "::" + contextRoot + "::" + appStoreName;
		return key;
	}

}

/// **
// * Get app store client configuration.
// *
// * @param properties
// * @return
// */
// public static ClientConfiguration getClientConfiguration(String url, String contextRoot, String username, String password) {
// return ClientConfiguration.get(url, contextRoot, username, password);
// }
/// **
// * Create app store connection properties.
// *
// * @param driver
// * @param url
// * @param username
// * @param password
// * @return
// */
// public static Properties getProperties(String url, String contextRoot, String username, String password) {
// Properties properties = new Properties();
// properties.setProperty(OrbitConstants.APPSTORE_URL, url);
// properties.setProperty(OrbitConstants.APPSTORE_CONTEXT_ROOT, contextRoot);
// properties.setProperty(OrbitConstants.APPSTORE_USERNAME, username);
// properties.setProperty(OrbitConstants.APPSTORE_PASSWORD, password);
// return properties;
// }
