package org.origin.mgm.client.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.origin.common.loadbalance.LoadBalanceService;
import org.origin.common.loadbalance.RoundRobinLoadBalancePolicy;
import org.origin.mgm.client.OriginConstants;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalanceService;
import org.origin.mgm.client.loadbalance.IndexProviderLoadBalancer;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalanceService;
import org.origin.mgm.client.loadbalance.IndexServiceLoadBalancer;

public class IndexServiceUtil {

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static List<IndexService> getIndexServices(Map<Object, Object> props) {
		List<URL> urls = getIndexServiceURLs(props);

		List<IndexService> indexServices = new ArrayList<IndexService>();
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm());
			IndexService indexService = IndexServiceFactory.getInstance().createIndexService(config);
			indexServices.add(indexService);
		}

		return indexServices;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static List<IndexProvider> getIndexProviders(Map<Object, Object> props) {
		List<URL> urls = getIndexServiceURLs(props);

		List<IndexProvider> indexProviders = new ArrayList<IndexProvider>();
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm());
			IndexProvider indexProvider = IndexProviderFactory.getInstance().createIndexProvider(config);
			indexProviders.add(indexProvider);
		}

		return indexProviders;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static IndexServiceLoadBalancer getIndexServiceLoadBalancer(Map<Object, Object> props) {
		List<LoadBalanceService<IndexService>> lbServices = new ArrayList<LoadBalanceService<IndexService>>();

		List<URL> urls = getIndexServiceURLs(props);
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm());
			IndexService indexService = IndexServiceFactory.getInstance().createIndexService(config);
			lbServices.add(new IndexServiceLoadBalanceService(indexService));
		}

		IndexServiceLoadBalancer loadBalancer = new IndexServiceLoadBalancer(lbServices);
		loadBalancer.setPolicy(new RoundRobinLoadBalancePolicy<IndexService>());

		return loadBalancer;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static IndexProviderLoadBalancer getIndexProviderLoadBalancer(Map<Object, Object> props) {
		List<LoadBalanceService<IndexProvider>> lbServices = new ArrayList<LoadBalanceService<IndexProvider>>();

		List<URL> urls = getIndexServiceURLs(props);
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm());
			IndexProvider indexProvider = IndexProviderFactory.getInstance().createIndexProvider(config);
			lbServices.add(new IndexProviderLoadBalanceService(indexProvider));
		}

		IndexProviderLoadBalancer loadBalancer = new IndexProviderLoadBalancer(lbServices);
		loadBalancer.setPolicy(new RoundRobinLoadBalancePolicy<IndexProvider>());

		return loadBalancer;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	private static List<URL> getIndexServiceURLs(Map<Object, Object> props) {
		List<URL> indexServiceURIs = new ArrayList<URL>();
		if (props != null) {
			Object obj = props.get(OriginConstants.COMPONENT_INDEX_SERVICE_URL_PROP);
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

}
