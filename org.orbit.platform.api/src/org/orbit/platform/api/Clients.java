package org.orbit.platform.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.api.impl.PlatformProxy;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clients {

	protected static Logger LOG = LoggerFactory.getLogger(Clients.class);

	private static Object lock = new Object[0];
	private static Clients instance = null;

	public static Clients getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Clients();
				}
			}
		}
		return instance;
	}

	protected ServiceConnectorAdapter<PlatformClient> platformConnectorAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.platformConnectorAdapter = new ServiceConnectorAdapter<PlatformClient>(PlatformClient.class);
		this.platformConnectorAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.platformConnectorAdapter != null) {
			this.platformConnectorAdapter.stop(bundleContext);
			this.platformConnectorAdapter = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public PlatformClient getPlatformProxy(Map<?, ?> properties) {
		return new PlatformProxy(properties);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public PlatformClient getPlatformClient(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(Constants.ORBIT_PLATFORM_URL);
		}
		if (url == null) {
			LOG.error("'" + Constants.ORBIT_PLATFORM_URL + "' property is not found.");
			throw new IllegalStateException("'" + Constants.ORBIT_PLATFORM_URL + "' property is not found.");
		}
		return getPlatformClient(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public PlatformClient getPlatformClient(String url) {
		return getPlatformClient(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public PlatformClient getPlatformClient(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(Constants.REALM, realm);
		properties.put(Constants.USERNAME, username);
		properties.put(Constants.URL, url);

		PlatformClient platform = this.platformConnectorAdapter.getService(properties);
		if (platform == null) {
			LOG.error("PlatformClient is not available.");
			throw new IllegalStateException("PlatformClient is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return platform;
	}

}