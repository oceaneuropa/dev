package org.orbit.platform.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.api.gaia.GAIAClient;
import org.orbit.platform.api.gaia.GAIAProxy;
import org.orbit.platform.api.platform.PlatformClient;
import org.orbit.platform.api.platform.PlatformProxy;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformClients {

	protected static Logger LOG = LoggerFactory.getLogger(PlatformClients.class);

	private static Object lock = new Object[0];
	private static PlatformClients instance = null;

	public static PlatformClients getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new PlatformClients();
				}
			}
		}
		return instance;
	}

	protected ServiceConnectorAdapter<PlatformClient> platformConnectorAdapter;
	protected ServiceConnectorAdapter<GAIAClient> gaiaConnectorAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.platformConnectorAdapter = new ServiceConnectorAdapter<PlatformClient>(PlatformClient.class);
		this.platformConnectorAdapter.start(bundleContext);

		this.gaiaConnectorAdapter = new ServiceConnectorAdapter<GAIAClient>(GAIAClient.class);
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
	public PlatformClient getPlatform(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(PlatformConstants.ORBIT_GAIA_URL);
		}
		if (url == null) {
			LOG.error("'" + PlatformConstants.ORBIT_GAIA_URL + "' property is not found.");
			throw new IllegalStateException("'" + PlatformConstants.ORBIT_GAIA_URL + "' property is not found.");
		}
		return getPlatform(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public PlatformClient getPlatform(String url) {
		return getPlatform(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public PlatformClient getPlatform(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PlatformConstants.REALM, realm);
		properties.put(PlatformConstants.USERNAME, username);
		properties.put(PlatformConstants.URL, url);

		PlatformClient platform = this.platformConnectorAdapter.getService(properties);
		if (platform == null) {
			LOG.error("Platform is not available.");
			throw new IllegalStateException("Platform is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return platform;
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public GAIAClient getGAIAProxy(Map<?, ?> properties) {
		return new GAIAProxy(properties);
	}

	/**
	 * 
	 * @param properties
	 * @return
	 */
	public GAIAClient getGAIA(Map<?, ?> properties) {
		String url = null;
		if (properties != null) {
			url = (String) properties.get(PlatformConstants.ORBIT_GAIA_URL);
		}
		if (url == null) {
			LOG.error("'" + PlatformConstants.ORBIT_GAIA_URL + "' property is not found.");
			throw new IllegalStateException("'" + PlatformConstants.ORBIT_GAIA_URL + "' property is not found.");
		}
		return getGAIA(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public GAIAClient getGAIA(String url) {
		return getGAIA(null, null, url);
	}

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @return
	 */
	public GAIAClient getGAIA(String realm, String username, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		username = GlobalContext.getInstance().checkUsername(realm, username);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PlatformConstants.REALM, realm);
		properties.put(PlatformConstants.USERNAME, username);
		properties.put(PlatformConstants.URL, url);

		GAIAClient gaia = this.gaiaConnectorAdapter.getService(properties);
		if (gaia == null) {
			LOG.error("GAIA is not available.");
			throw new IllegalStateException("GAIA is not available. realm='" + realm + "', username='" + username + "', url='" + url + "'.");
		}
		return gaia;
	}

}
