package other.orbit.infra.connector.indexes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.connector.InfraConstants;
import org.orbit.infra.connector.indexes.IndexProviderClientImpl;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import other.orbit.infra.api.indexes.IndexServiceConnectorV1;

public class IndexServiceConnectorImplV1 implements IndexServiceConnectorV1 {

	protected static final String KEY_PARTS_SEPARATOR = "::";

	protected ServiceRegistration<?> serviceRegistration;
	protected Map<String, IndexServiceClient> serviceMap;

	public IndexServiceConnectorImplV1() {
		this.serviceMap = new HashMap<String, IndexServiceClient>();
	}

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(IndexServiceConnectorV1.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
		this.serviceMap.clear();
	}

	@Override
	public synchronized IndexServiceClient getService(Map<Object, Object> properties) {
		IndexServiceClient indexService = null;
		String url = (String) properties.get(InfraConstants.INDEX_SERVICE_HOST_URL);
		String contextRoot = (String) properties.get(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT);
		if (url != null && contextRoot != null) {
			String key = url + KEY_PARTS_SEPARATOR + contextRoot;
			indexService = this.serviceMap.get(key);
			if (indexService == null) {
				// indexService = new IndexProviderImpl(properties);
				this.serviceMap.put(key, indexService);
			}
		}
		return indexService;
	}

}
