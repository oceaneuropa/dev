package org.orbit.os.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.os.api.gaia.GAIA;
import org.orbit.os.api.gaia.GAIAProxy;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSClients {

	protected static Logger LOG = LoggerFactory.getLogger(OSClients.class);

	private static Object lock = new Object[0];
	private static OSClients instance = null;

	public static OSClients getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OSClients();
				}
			}
		}
		return instance;
	}

	protected ServiceConnectorAdapter<GAIA> gaiaConnectorAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.gaiaConnectorAdapter = new ServiceConnectorAdapter<GAIA>(GAIA.class);
		this.gaiaConnectorAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.gaiaConnectorAdapter != null) {
			this.gaiaConnectorAdapter.stop(bundleContext);
			this.gaiaConnectorAdapter = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public GAIA getGAIAProxy(Map<?, ?> properties) {
		return new GAIAProxy(properties);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public GAIA getGAIA(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(OSConstants.ORBIT_GAIA_URL);
		}
		if (url == null) {
			LOG.error("'" + OSConstants.ORBIT_GAIA_URL + "' property is not found.");
			throw new IllegalStateException("'" + OSConstants.ORBIT_GAIA_URL + "' property is not found.");
		}
		return getGAIA(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public GAIA getGAIA(String url) {
		return getGAIA(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public GAIA getGAIA(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(OSConstants.REALM, realm);
		properties.put(OSConstants.USERNAME, username);
		properties.put(OSConstants.URL, url);

		GAIA gaia = this.gaiaConnectorAdapter.getService(properties);
		if (gaia == null) {
			LOG.error("GAIA is not available.");
			throw new IllegalStateException("GAIA is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return gaia;
	}

}
