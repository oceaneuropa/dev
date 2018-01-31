package org.orbit.infra.connector;

import org.orbit.infra.connector.channel.ChannelsConnector;
import org.orbit.infra.connector.indexes.IndexProviderConnector;
import org.orbit.infra.connector.indexes.IndexServiceConnector;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraConnectors {

	protected static Logger LOG = LoggerFactory.getLogger(InfraConnectors.class);

	private static Object lock = new Object[0];
	private static InfraConnectors instance = null;

	public static InfraConnectors getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new InfraConnectors();
				}
			}
		}
		return instance;
	}

	protected IndexServiceConnector indexServiceConnector;
	protected IndexProviderConnector indexProviderConnector;
	protected ChannelsConnector channelConnector;

	public void start(BundleContext bundleContext) {
		this.indexProviderConnector = new IndexProviderConnector();
		this.indexProviderConnector.start(bundleContext);

		this.indexServiceConnector = new IndexServiceConnector();
		this.indexServiceConnector.start(bundleContext);

		this.channelConnector = new ChannelsConnector();
		this.channelConnector.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		if (this.channelConnector != null) {
			this.channelConnector.stop(bundleContext);
			this.channelConnector = null;
		}

		if (this.indexServiceConnector != null) {
			this.indexServiceConnector.stop(bundleContext);
			this.indexServiceConnector = null;
		}

		if (this.indexProviderConnector != null) {
			this.indexProviderConnector.stop(bundleContext);
			this.indexProviderConnector = null;
		}
	}

}
