package org.orbit.spirit.api.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.spirit.api.SpiritConstants;
import org.orbit.spirit.api.earth.EarthClient;
import org.orbit.spirit.api.earth.EarthClientProxy;
import org.orbit.spirit.api.gaia.GaiaClient;
import org.orbit.spirit.api.gaia.GaiaClientProxy;
import org.origin.common.rest.client.ServiceConnectorAdapter;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.util.LifecycleAware;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiritClients implements LifecycleAware {

	protected static Logger LOG = LoggerFactory.getLogger(SpiritClients.class);

	public static SpiritClients INSTANCE = new SpiritClients();

	public static SpiritClients getInstance() {
		return INSTANCE;
	}

	protected ServiceConnectorAdapter<GaiaClient> gaiaConnector;
	protected ServiceConnectorAdapter<EarthClient> earthConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void start(final BundleContext bundleContext) {
		this.earthConnector = new ServiceConnectorAdapter<EarthClient>(EarthClient.class);
		this.earthConnector.start(bundleContext);

		this.gaiaConnector = new ServiceConnectorAdapter<GaiaClient>(GaiaClient.class);
		this.gaiaConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	@Override
	public void stop(final BundleContext bundleContext) {
		if (this.gaiaConnector != null) {
			this.gaiaConnector.stop(bundleContext);
			this.gaiaConnector = null;
		}

		if (this.earthConnector != null) {
			this.earthConnector.stop(bundleContext);
			this.earthConnector = null;
		}
	}

	/**
	 * 
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public GaiaClient getGaiaClient(Map<?, ?> properties, boolean useProxy) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(SpiritConstants.ORBIT__GAIA_SERVICE_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		GaiaClient gaiaClient = this.gaiaConnector.getService(theProperties);
		if (gaiaClient == null) {
			if (useProxy) {
				gaiaClient = new GaiaClientProxy(theProperties);
			} else {
				throw new RuntimeException("GaiaClient is not retrieved from connector.");
			}
		}
		return gaiaClient;
	}

	/**
	 * 
	 * @param properties
	 * @param useProxy
	 * @return
	 */
	public EarthClient getEarthClient(Map<?, ?> properties, boolean useProxy) {
		String realm = (String) properties.get(WSClientConstants.REALM);
		String accessToken = (String) properties.get(WSClientConstants.ACCESS_TOKEN);
		String url = (String) properties.get(WSClientConstants.URL);
		if (url == null) {
			url = (String) properties.get(SpiritConstants.ORBIT__EARTH_SERVICE_URL);
		}

		Map<String, Object> theProperties = new HashMap<String, Object>();
		theProperties.put(WSClientConstants.REALM, realm);
		theProperties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
		theProperties.put(WSClientConstants.URL, url);

		EarthClient earthClient = this.earthConnector.getService(theProperties);
		if (earthClient == null && useProxy) {
			if (useProxy) {
				earthClient = new EarthClientProxy(theProperties);
			} else {
				throw new RuntimeException("EarthClient is not retrieved from connector.");
			}
		}
		return earthClient;
	}

}
