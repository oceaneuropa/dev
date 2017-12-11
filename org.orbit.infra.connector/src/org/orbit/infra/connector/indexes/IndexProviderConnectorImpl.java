package org.orbit.infra.connector.indexes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.IndexProviderConnector;
import org.orbit.infra.connector.OrbitConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexProviderConnectorImpl implements IndexProviderConnector {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, IndexProvider> serviceMap;

	public IndexProviderConnectorImpl() {
		this.serviceMap = new HashMap<String, IndexProvider>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(IndexProviderConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
		this.serviceMap.clear();
	}

	@Override
	public synchronized IndexProvider getService(Map<Object, Object> properties) {
		IndexProvider indexProvider = null;
		String url = (String) properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			indexProvider = this.serviceMap.get(key);
			if (indexProvider == null) {
				indexProvider = new IndexProviderImpl(properties);
				this.serviceMap.put(key, indexProvider);
			}
		}
		return indexProvider;
	}

}
