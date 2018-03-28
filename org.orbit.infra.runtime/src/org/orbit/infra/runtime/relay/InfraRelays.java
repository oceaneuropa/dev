package org.orbit.infra.runtime.relay;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.origin.common.service.WebServiceAware;
import org.origin.common.service.WebServiceAwareImpl;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraRelays {

	protected static Logger LOG = LoggerFactory.getLogger(InfraRelays.class);

	private static Object lock = new Object[0];
	private static InfraRelays instance = null;

	public static InfraRelays getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new InfraRelays();
				}
			}
		}
		return instance;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param initProperties
	 * @return
	 */
	public IndexServiceWSApplicationRelay createIndexServiceRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS);

		final String hostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
		final String name = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_NAME);
		final String contextRoot = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_HOSTS);
		String urls = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}
		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		WebServiceAware webServiceAware = new WebServiceAwareImpl(name, hostURL, contextRoot);
		Switcher<URI> uriSwitcher = RelayHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		IndexServiceWSApplicationRelay relay = new IndexServiceWSApplicationRelay(webServiceAware, uriSwitcher);
		return relay;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param initProperties
	 * @return
	 */
	public ChannelWSApplicationRelay createChannelRelay(BundleContext bundleContext, Map<Object, Object> initProperties) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		if (initProperties != null) {
			properties.putAll(initProperties);
		}
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_NAME);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_URLS);

		final String hostURL = (String) properties.get(InfraConstants.ORBIT_HOST_URL);
		final String name = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_RELAY_NAME);
		final String contextRoot = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT);
		String hosts = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_RELAY_HOSTS);
		String urls = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_RELAY_URLS);

		if (contextRoot == null) {
			return null;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return null;
		}

		WebServiceAware webServiceAware = new WebServiceAwareImpl(name, hostURL, contextRoot);
		Switcher<URI> uriSwitcher = RelayHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);

		ChannelWSApplicationRelay relay = new ChannelWSApplicationRelay(webServiceAware, uriSwitcher);
		return relay;
	}

	protected List<URI> toList(String baseURIsString) {
		return URIUtil.toList(baseURIsString);
	}

	protected List<URI> toList(String hostURLsString, String contextRoot) {
		return URIUtil.toList(hostURLsString, contextRoot);
	}

}

// protected IndexServiceWSApplicationRelay indexServiceRelay;
// protected ChannelWSApplicationRelay channelRelay;

// public void start(BundleContext bundleContext) {
// LOG.info("start()");

// Map<Object, Object> properties = new Hashtable<Object, Object>();
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_NAME);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_RELAY_URLS);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_NAME);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_HOSTS);
// PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_RELAY_URLS);
// startIndexServiceRelay(bundleContext, properties);
// startChannelRelay(bundleContext, properties);
// }

// public void stop(BundleContext bundleContext) {
// LOG.info("stop()");

// stopIndexServiceRelay(bundleContext);
// stopChannelRelay(bundleContext);
// }

// protected void startIndexServiceRelay(BundleContext bundleContext, Map<Object, Object> properties) {
// this.indexServiceRelay = createIndexServiceRelay(bundleContext, properties);
// this.indexServiceRelay.start(bundleContext);
// }

// protected void stopIndexServiceRelay(BundleContext bundleContext) {
// if (this.indexServiceRelay != null) {
// this.indexServiceRelay.stop(bundleContext);
// this.indexServiceRelay = null;
// }
// }

// protected void startChannelRelay(BundleContext bundleContext, Map<Object, Object> properties) {
// this.channelRelay = createChannelRelay(bundleContext, properties);
// this.channelRelay.start(bundleContext);
// }
//
// protected void stopChannelRelay(BundleContext bundleContext) {
// if (this.channelRelay != null) {
// this.channelRelay.stop(bundleContext);
// this.channelRelay = null;
// }
// }
