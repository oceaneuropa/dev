package org.orbit.infra.runtime;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.runtime.configregistry.service.ConfigRegistryService;
import org.orbit.infra.runtime.configregistry.ws.ConfigRegistryServiceAdapter;
import org.orbit.infra.runtime.datacast.service.DataCastService;
import org.orbit.infra.runtime.datacast.ws.DataCastServiceAdapter;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.ws.DataTubeServiceAdapter;
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
	protected ExtensionRegistryAdapter extensionRegistryAdapter;
	protected ConfigRegistryServiceAdapter configRegistryAdapter;
	protected DataCastServiceAdapter dataCastServiceAdapter;
	protected DataTubeServiceAdapter dataTubeServiceAdapter;

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_INDEX_SERVICE_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		PropertyUtil.loadProperty(bundleContext, properties, InfraConstants.ORBIT_HOST_URL);

		// Start service adapters
		this.indexServiceAdapter = new IndexServiceAdapter(properties);
		this.indexServiceAdapter.start(bundleContext);

		this.extensionRegistryAdapter = new ExtensionRegistryAdapter(properties);
		this.extensionRegistryAdapter.start(bundleContext);

		this.configRegistryAdapter = new ConfigRegistryServiceAdapter(properties);
		this.configRegistryAdapter.start(bundleContext);

		this.dataCastServiceAdapter = new DataCastServiceAdapter(properties);
		this.dataCastServiceAdapter.start(bundleContext);

		this.dataTubeServiceAdapter = new DataTubeServiceAdapter(properties);
		this.dataTubeServiceAdapter.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop service adapters
		if (this.dataTubeServiceAdapter != null) {
			this.dataTubeServiceAdapter.stop(bundleContext);
			this.dataTubeServiceAdapter = null;
		}

		if (this.dataCastServiceAdapter != null) {
			this.dataCastServiceAdapter.stop(bundleContext);
			this.dataCastServiceAdapter = null;
		}

		if (this.configRegistryAdapter != null) {
			this.configRegistryAdapter.stop(bundleContext);
			this.configRegistryAdapter = null;
		}

		if (this.extensionRegistryAdapter != null) {
			this.extensionRegistryAdapter.stop(bundleContext);
			this.extensionRegistryAdapter = null;
		}

		if (this.indexServiceAdapter != null) {
			this.indexServiceAdapter.stop(bundleContext);
			this.indexServiceAdapter = null;
		}
	}

	public IndexService getIndexService() {
		return (this.indexServiceAdapter != null) ? this.indexServiceAdapter.getService() : null;
	}

	public ExtensionRegistryService getExtensionRegistryService() {
		return (this.extensionRegistryAdapter != null) ? this.extensionRegistryAdapter.getService() : null;
	}

	public ConfigRegistryService getConfigRegistryService() {
		return (this.configRegistryAdapter != null) ? this.configRegistryAdapter.getService() : null;
	}

	public DataCastService getDataCastService() {
		return (this.dataCastServiceAdapter != null) ? this.dataCastServiceAdapter.getService() : null;
	}

	public DataTubeService getDataTubeService() {
		return (this.dataTubeServiceAdapter != null) ? this.dataTubeServiceAdapter.getService() : null;
	}

}
