package org.orbit.spirit.connector;

import org.orbit.spirit.connector.gaia.GAIAConnector;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connectors {

	protected static Logger LOG = LoggerFactory.getLogger(Connectors.class);

	private static Object lock = new Object[0];
	private static Connectors instance = null;

	public static Connectors getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new Connectors();
				}
			}
		}
		return instance;
	}

	protected GAIAConnector gaiaConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.gaiaConnector = new GAIAConnector();
		this.gaiaConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		if (this.gaiaConnector != null) {
			this.gaiaConnector.stop(bundleContext);
			this.gaiaConnector = null;
		}
	}

}
