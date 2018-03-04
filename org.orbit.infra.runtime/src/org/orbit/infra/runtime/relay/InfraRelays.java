package org.orbit.infra.runtime.relay;

import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.InfraConstants;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
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

	protected IndexServiceWSApplicationRelay indexServiceSwitcher;
	protected ChannelWSApplicationRelay channelSwitcher;

	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		Map<Object, Object> properties = new Hashtable<Object, Object>();

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_URLS);

		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_SWITCHER_CONTEXT_ROOT);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_SWITCHER_HOSTS);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.COMPONENT_CHANNEL_SWITCHER_URLS);

		WSClientFactory factory = createClientFactory(bundleContext, properties);

		startIndexServiceSwitcher(bundleContext, factory, properties);
		startChannelSwitcher(bundleContext, factory, properties);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		stopIndexServiceSwitcher(bundleContext);
		stopChannelSwitcher(bundleContext);
	}

	protected List<URI> toList(String baseURIsString) {
		return URIUtil.toList(baseURIsString);
	}

	protected List<URI> toList(String hostURLsString, String contextRoot) {
		return URIUtil.toList(hostURLsString, contextRoot);
	}

	protected WSClientFactory createClientFactory(BundleContext bundleContext, Map<Object, Object> properties) {
		return new WSClientFactoryImpl();
	}

	protected void startIndexServiceSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_HOSTS);
		String urls = (String) properties.get(InfraConstants.COMPONENT_INDEX_SERVICE_SWITCHER_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		startIndexServiceSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startIndexServiceSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = RelayHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.indexServiceSwitcher = new IndexServiceWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.indexServiceSwitcher.start(bundleContext);
	}

	protected void stopIndexServiceSwitcher(BundleContext bundleContext) {
		if (this.indexServiceSwitcher != null) {
			this.indexServiceSwitcher.stop(bundleContext);
			this.indexServiceSwitcher = null;
		}
	}

	protected void startChannelSwitcher(BundleContext bundleContext, WSClientFactory factory, Map<Object, Object> properties) {
		String contextRoot = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_SWITCHER_CONTEXT_ROOT);
		String hosts = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_SWITCHER_HOSTS);
		String urls = (String) properties.get(InfraConstants.COMPONENT_CHANNEL_SWITCHER_URLS);

		if (contextRoot == null) {
			return;
		}

		List<URI> uriList = null;
		if (urls != null) {
			uriList = toList(urls);
		} else if (hosts != null) {
			uriList = toList(hosts, contextRoot);
		}
		if (uriList == null || uriList.isEmpty()) {
			return;
		}

		startChannelSwitcher(bundleContext, factory, contextRoot, uriList);
	}

	protected void startChannelSwitcher(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		Switcher<URI> uriSwitcher = RelayHelper.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		this.channelSwitcher = new ChannelWSApplicationRelay(contextRoot, uriSwitcher, factory);
		this.channelSwitcher.start(bundleContext);
	}

	protected void stopChannelSwitcher(BundleContext bundleContext) {
		if (this.channelSwitcher != null) {
			this.channelSwitcher.stop(bundleContext);
			this.channelSwitcher = null;
		}
	}

}
