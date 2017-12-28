package org.orbit.infra.connector.indexes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexServiceConnector;
import org.orbit.infra.connector.OrbitConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexServiceConnectorImpl implements IndexServiceConnector {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, IndexService> serviceMap;

	public IndexServiceConnectorImpl() {
		this.serviceMap = new HashMap<String, IndexService>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(IndexServiceConnector.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
		this.serviceMap.clear();
	}

	@Override
	public synchronized IndexService getService(Map<Object, Object> properties) {
		IndexService indexService = null;
		String url = (String) properties.get(OrbitConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(OrbitConstants.INDEX_SERVICE_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			indexService = this.serviceMap.get(key);
			if (indexService == null) {
				indexService = new IndexProviderImpl(properties);
				this.serviceMap.put(key, indexService);
			}
		}
		return indexService;
	}

}