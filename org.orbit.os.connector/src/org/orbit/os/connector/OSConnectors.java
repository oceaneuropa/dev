package org.orbit.os.connector;

import org.orbit.os.connector.gaia.GAIAConnector;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSConnectors {

	protected static Logger LOG = LoggerFactory.getLogger(OSConnectors.class);

	private static Object lock = new Object[0];
	private static OSConnectors instance = null;

	public static OSConnectors getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new OSConnectors();
				}
			}
		}
		return instance;
	}

	protected GAIAConnector gaiaConnector;

	public void start(BundleContext bundleContext) {
		this.gaiaConnector = new GAIAConnector();
		this.gaiaConnector.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		if (this.gaiaConnector != null) {
			this.gaiaConnector.stop(bundleContext);
			this.gaiaConnector = null;
		}
	}

}
