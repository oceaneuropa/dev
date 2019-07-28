package org.orbit.infra.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.configregistry.ws.ConfigRegistryServiceAdapter;
import org.orbit.infra.runtime.extensionregistry.service.ExtensionRegistryService;
import org.orbit.infra.runtime.extensionregistry.ws.ExtensionRegistryAdapter;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.infra.runtime.indexes.ws.IndexServiceAdapter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfraServices {

	protected static Logger LOG = LoggerFactory.getLogger(InfraServices.class);

	private static InfraServices instance = new InfraServices();

	public static InfraServices getInstance() {
		return instance;
	}

	protected IndexServiceAdapter indexServiceAdapter;
	protected ConfigRegistryServiceAdapter configRegistryAdapter;
	protected ExtensionRegistryAdapter extensionRegistryAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, properties, org.orbit.infra.api.InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, org.orbit.infra.api.InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);

		// Start service adapters
		this.indexServiceAdapter = new IndexServiceAdapter(properties);
		this.indexServiceAdapter.start(bundleContext);

		this.configRegistryAdapter = new ConfigRegistryServiceAdapter(properties);
		this.configRegistryAdapter.start(bundleContext);

		this.extensionRegistryAdapter = new ExtensionRegistryAdapter(properties);
		this.extensionRegistryAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop service adapters
		if (this.extensionRegistryAdapter != null) {
			this.extensionRegistryAdapter.stop(bundleContext);
			this.extensionRegistryAdapter = null;
		}

		if (this.configRegistryAdapter != null) {
			this.configRegistryAdapter.stop(bundleContext);
			this.configRegistryAdapter = null;
		}

		if (this.indexServiceAdapter != null) {
			this.indexServiceAdapter.stop(bundleContext);
			this.indexServiceAdapter = null;
		}
	}

	public IndexService getIndexService() {
		return (this.indexServiceAdapter != null) ? this.indexServiceAdapter.getService() : null;
	}

	public ConfigRegistryService getConfigRegistryService() {
		return (this.configRegistryAdapter != null) ? this.configRegistryAdapter.getService() : null;
	}

	public ExtensionRegistryService getExtensionRegistryService() {
		return (this.extensionRegistryAdapter != null) ? this.extensionRegistryAdapter.getService() : null;
	}
}
