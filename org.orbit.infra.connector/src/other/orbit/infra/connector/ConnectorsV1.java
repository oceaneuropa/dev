package other.orbit.infra.connector;

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

	// protected IndexServiceConnector indexServiceConnector;
	// protected IndexProviderConnector indexProviderConnector;
	// protected ChannelsConnector channelConnector;

	public void start(BundleContext bundleContext) {
		// this.indexProviderConnector = new IndexProviderConnector();
		// this.indexProviderConnector.start(bundleContext);
		//
		// this.indexServiceConnector = new IndexServiceConnector();
		// this.indexServiceConnector.start(bundleContext);
		//
		// this.channelConnector = new ChannelsConnector();
		// this.channelConnector.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		// if (this.channelConnector != null) {
		// this.channelConnector.stop(bundleContext);
		// this.channelConnector = null;
		// }
		//
		// if (this.indexServiceConnector != null) {
		// this.indexServiceConnector.stop(bundleContext);
		// this.indexServiceConnector = null;
		// }
		//
		// if (this.indexProviderConnector != null) {
		// this.indexProviderConnector.stop(bundleContext);
		// this.indexProviderConnector = null;
		// }
	}

}
