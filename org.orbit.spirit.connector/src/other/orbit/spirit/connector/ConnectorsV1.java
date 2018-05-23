package other.orbit.spirit.connector;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectorsV1 {

	protected static Logger LOG = LoggerFactory.getLogger(ConnectorsV1.class);

	private static Object lock = new Object[0];
	private static ConnectorsV1 instance = null;

	public static ConnectorsV1 getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ConnectorsV1();
				}
			}
		}
		return instance;
	}

	// protected GAIAConnector gaiaConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		// this.gaiaConnector = new GAIAConnector();
		// this.gaiaConnector.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// if (this.gaiaConnector != null) {
		// this.gaiaConnector.stop(bundleContext);
		// this.gaiaConnector = null;
		// }
	}

}
