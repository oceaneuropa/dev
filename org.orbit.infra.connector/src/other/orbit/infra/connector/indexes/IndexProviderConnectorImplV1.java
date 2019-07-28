package other.orbit.infra.connector.indexes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

// import other.orbit.infra.api.indexes.IndexProviderConnectorV1;

public class IndexProviderConnectorImplV1 /* implements IndexProviderConnectorV1 */ {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, IndexProviderClient> serviceMap;

	public IndexProviderConnectorImplV1() {
		this.serviceMap = new HashMap<String, IndexProviderClient>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		// this.serviceRegistration = bundleContext.registerService(IndexProviderConnectorV1.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
		this.serviceMap.clear();
	}

	// @Override
	public synchronized IndexProviderClient getService(Map<Object, Object> properties) {
		IndexProviderClient indexProvider = null;
		String url = (String) properties.get(InfraConstants.SERVICE__HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.SERVICE__CONTEXT_ROOT);
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
