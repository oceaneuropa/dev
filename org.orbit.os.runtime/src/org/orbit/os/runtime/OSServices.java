package org.orbit.os.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.os.runtime.service.GAIA;
import org.orbit.os.runtime.ws.GaiaAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSServices {

	protected static Logger LOG = LoggerFactory.getLogger(OSServices.class);

	private static Object lock = new Object[0];
	private static OSServices instance = null;

	public static OSServices getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OSServices();
				}
			}
		}
		return instance;
	}

	protected GaiaAdapter gaiaAdapter;

	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OSConstants.COMPONENT_INDEX_SERVICE_URL);

		// Start service adapter
		this.gaiaAdapter = new GaiaAdapter(properties);
		this.gaiaAdapter.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		if (this.gaiaAdapter != null) {
			this.gaiaAdapter.stop(bundleContext);
			this.gaiaAdapter = null;
		}
	}

	public GAIA getGAIA() {
		return (this.gaiaAdapter != null) ? this.gaiaAdapter.getService() : null;
	}

}

// Get IndexProvider load balancer
// IndexProviderLoadBalancer indexProviderLoadBalancer = IndexServiceUtil.getIndexProviderLoadBalancer(connector, properties);
