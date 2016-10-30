package org.orbit.component.connector.appstore;

import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.appstore.AppStore;
import org.orbit.component.api.appstore.AppStoreManager;
import org.origin.common.loadbalance.LoadBalancePolicy;
import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.loadbalance.RoundRobinLoadBalancePolicy;
import org.origin.mgm.client.api.IndexService;
import org.origin.mgm.client.api.IndexServiceConfiguration;
import org.origin.mgm.client.api.IndexServiceFactory;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalanceService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DistributedAppStoreManager implements AppStoreManager {

	protected BundleContext bundleContext;
	protected Map<Object, Object> props;
	protected List<URL> indexServiceURLs;

	protected ServiceRegistration<?> serviceRegistration;
	protected IndexServiceLoadBalancer indexServiceLoadBalancer;

	/**
	 * 
	 * @param bundleContext
	 * @param props
	 */
	public DistributedAppStoreManager(BundleContext bundleContext, Map<Object, Object> props) {
		this.bundleContext = bundleContext;
		this.props = props;
		this.indexServiceURLs = getInitialIndexServiceURLs(this.props);

		List<LoadBalanceService<IndexService>> indexServiceLBServices = new ArrayList<LoadBalanceService<IndexService>>();
		List<IndexService> indexServices = new ArrayList<IndexService>();
		for (URL indexServiceURL : this.indexServiceURLs) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(indexServiceURL.toExternalForm(), null, null);
			IndexService indexService = IndexServiceFactory.getInstance().createIndexService(config);
			indexServices.add(indexService);
			IndexServiceLoadBalanceService indexServiceLBService = new IndexServiceLoadBalanceService(indexService);
			indexServiceLBServices.add(indexServiceLBService);
		}
		this.indexServiceLoadBalancer = new IndexServiceLoadBalancer(indexServiceLBServices);
		RoundRobinLoadBalancePolicy<IndexService> indexServicePolicy = new RoundRobinLoadBalancePolicy<IndexService>();
		this.indexServiceLoadBalancer.setPolicy(indexServicePolicy);
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	protected List<URL> getInitialIndexServiceURLs(Map<Object, Object> props) {
		List<URL> indexServiceURIs = new ArrayList<URL>();
		if (props != null) {
			Object obj = props.get("indexservice.url");
			if (obj instanceof String) {
				String str = (String) obj;
				if (str.contains(";")) {
					// multiple URLs in the props
					String[] segments = str.split(";");
					if (segments != null) {
						for (String segment : segments) {
							try {
								URL url = new URL(segment.trim());
								if (!indexServiceURIs.contains(url)) {
									indexServiceURIs.add(url);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} else {
					// one URL in the props
					try {
						URL url = new URL(str.trim());
						if (!indexServiceURIs.contains(url)) {
							indexServiceURIs.add(url);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return indexServiceURIs;
	}

	/**
	 * Start the app store manager.
	 */
	public void start() {
		// // Start the pool
		// if (this.appStorePool != null) {
		// this.appStorePool.stop();
		// this.appStorePool = null;
		// }
		// Properties properties = new Properties();
		// properties.put("indexservice",
		// "url=http://127.0.0.1:9090/indexservice;url=http://127.0.0.1:9091/indexservice;url=http://127.0.0.1:9092/indexservice");
		// this.appStorePool = new AppStorePool(properties);
		// this.appStorePool.start();

		// Start the LoadBalancer for accessing index service
		this.indexServiceLoadBalancer.start();

		// IndexService indexService = this.indexServiceLoadBalancer.getService(1000);

		// Register AppStoreManager service
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = this.bundleContext.registerService(AppStoreManager.class, this, props);
	}

	/**
	 * Stop the app store manager.
	 */
	public void stop() {
		// Unregister AppStoreManager service
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}

		// // Stop the pool
		// if (this.appStorePool != null) {
		// this.appStorePool.stop();
		// this.appStorePool = null;
		// }

		// Stop the LoadBalancer for accessing index service
		this.indexServiceLoadBalancer.stop();
	}

	@Override
	public synchronized AppStore getAppStore() {
		// LoadBalancePolicy<AppStore> loadBalancePolicy = getLoadBalancePolicy();
		// List<AppStore> oldAppStores = loadBalancePolicy.getResources();
		// List<AppStore> newAppStores = this.appStorePool.getAppStores();
		// return loadBalancePolicy.next();

		return null;
	}

	@Override
	public synchronized Iterator<AppStore> getAppStores() {
		return null;
	}

	@Override
	public synchronized AppStore getAppStore(String url, String username, String password) {
		return null;
	}

	@Override
	public void setLoadBalancePolicy(LoadBalancePolicy<AppStore> loadBalancePolicy) {
		// this.loadBalancePolicy = loadBalancePolicy;
	}

	@Override
	public LoadBalancePolicy<AppStore> getLoadBalancePolicy() {
		// if (this.loadBalancePolicy == null) {
		// this.loadBalancePolicy = createLoadBalancePolicy();
		// }
		// return this.loadBalancePolicy;
		return null;
	}

	// public LoadBalancePolicy<AppStore> createLoadBalancePolicy() {
	// return new RoundRobinLoadBalancePolicy<AppStore>();
	// }

}
