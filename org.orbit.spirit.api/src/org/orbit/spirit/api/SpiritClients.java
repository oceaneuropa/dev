package org.orbit.spirit.api;

import java.util.HashMap;
import java.util.Map;

import org.orbit.spirit.api.gaia.GAIAClient;
import org.orbit.spirit.api.gaia.GAIAProxy;
import org.origin.common.rest.client.GlobalContext;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.client.WSClientConstants;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiritClients {

	protected static Logger LOG = LoggerFactory.getLogger(SpiritClients.class);

	private static Object lock = new Object[0];
	private static SpiritClients instance = null;

	public static SpiritClients getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new SpiritClients();
				}
			}
		}
		return instance;
	}

	protected ServiceConnectorAdapter<GAIAClient> gaiaConnectorAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
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
			url = (String) properties.get(Constants.ORBIT_GAIA_URL);
		}
		if (url == null) {
			LOG.error("'" + Constants.ORBIT_GAIA_URL + "' property is not found.");
			throw new IllegalStateException("'" + Constants.ORBIT_GAIA_URL + "' property is not found.");
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
	 * @param accessToken
	 * @param url
	 * @return
	 */
	public GAIAClient getGAIA(String realm, String accessToken, String url) {
		realm = GlobalContext.getInstance().checkRealm(realm);
		accessToken = GlobalContext.getInstance().checkAccessToken(realm, accessToken);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.REALM, realm);
		properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		properties.put(WSClientConstants.URL, url);

		GAIAClient gaia = this.gaiaConnectorAdapter.getService(properties);
		if (gaia == null) {
			LOG.error("GAIA is not available.");
			throw new RuntimeException("GAIA is not available.");
		}
		return gaia;
	}

}
