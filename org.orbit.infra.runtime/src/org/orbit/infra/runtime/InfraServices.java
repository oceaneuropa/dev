package org.orbit.infra.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.channel.service.ChannelService;
import org.orbit.infra.runtime.channel.ws.ChannelServiceAdapter;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.ws.IndexServiceAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraServices {

	protected static Logger LOG = LoggerFactory.getLogger(InfraServices.class);

	private static Object lock = new Object[0];
	private static InfraServices instance = null;

	public static InfraServices getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new InfraServices();
				}
			}
		}
		return instance;
	}

	protected IndexServiceAdapter indexServiceAdapter;
	protected ChannelServiceAdapter channelServiceAdapter;

	public void start(BundleContext bundleContext) {
		Map<Object, Object> configProperties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, configProperties, InfraConstants.COMPONENT_INDEX_SERVICE_URL);

		// Start service adapters
		this.indexServiceAdapter = new IndexServiceAdapter();
		this.indexServiceAdapter.start(bundleContext);

		this.channelServiceAdapter = new ChannelServiceAdapter(configProperties);
		this.channelServiceAdapter.start(bundleContext);
	}

	public void stop(BundleContext bundleContext) {
		// Stop service adapters
		if (this.channelServiceAdapter != null) {
			this.channelServiceAdapter.stop(bundleContext);
			this.channelServiceAdapter = null;
		}

		if (this.indexServiceAdapter != null) {
			this.indexServiceAdapter.stop(bundleContext);
			this.indexServiceAdapter = null;
		}
	}

	public IndexService getIndexService() {
		return (this.indexServiceAdapter != null) ? this.indexServiceAdapter.getService() : null;
	}

	public ChannelService getChannelService() {
		return (this.channelServiceAdapter != null) ? this.channelServiceAdapter.getService() : null;
	}

}
