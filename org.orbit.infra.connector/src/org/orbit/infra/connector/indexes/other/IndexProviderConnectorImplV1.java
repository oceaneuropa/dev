package org.orbit.infra.connector.indexes.other;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.infra.api.indexes.other.IndexProviderConnectorV1;
import org.orbit.infra.connector.InfraConstants;
import org.orbit.infra.connector.indexes.IndexProviderImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexProviderConnectorImplV1 implements IndexProviderConnectorV1 {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, IndexProvider> serviceMap;

	public IndexProviderConnectorImplV1() {
		this.serviceMap = new HashMap<String, IndexProvider>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(IndexProviderConnectorV1.class, this, props);
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
		String url = (String) properties.get(InfraConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			indexProvider = this.serviceMap.get(key);
			if (indexProvider == null) {
				// indexProvider = new IndexProviderImpl(properties);
				this.serviceMap.put(key, indexProvider);
			}
		}
		return indexProvider;
	}

}
