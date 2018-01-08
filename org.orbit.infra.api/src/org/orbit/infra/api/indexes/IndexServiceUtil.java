package org.orbit.infra.api.indexes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.other.IndexProviderConnectorV1;
import org.orbit.infra.api.indexes.other.IndexServiceConnectorV1;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalanceResourceImpl;
import org.origin.common.loadbalance.policy.RoundRobinLoadBalancePolicy;

public class IndexServiceUtil {

	/**
	 * 
	 * @param connector
	 * @param props
	 * @return
	 */
	public static List<IndexService> getIndexServices(IndexServiceConnectorV1 connector, Map<Object, Object> props) {
		List<IndexService> indexServices = new ArrayList<IndexService>();

		if (connector != null) {
			List<URL> urls = getIndexServiceURLs(props);
			for (URL url : urls) {
				Map<Object, Object> properties = toProperties(url);
				IndexService indexService = connector.getService(properties);
				if (indexService != null) {
					indexServices.add(indexService);
				}
			}
		}

		return indexServices;
	}

	/**
	 * 
	 * @param connector
	 * @param props
	 * @return
	 */
	public static IndexServiceLoadBalancer getIndexServiceLoadBalancer(IndexServiceConnectorV1 connector, Map<Object, Object> props) {
		List<LoadBalanceResource<IndexService>> resources = new ArrayList<LoadBalanceResource<IndexService>>();

		if (connector != null) {
			List<URL> urls = getIndexServiceURLs(props);
			for (URL url : urls) {
				Map<Object, Object> properties = toProperties(url);
				IndexService indexService = connector.getService(properties);
				if (indexService != null) {
					String resourceId = indexService.getURL();
					resources.add(new LoadBalanceResourceImpl<IndexService>(indexService, resourceId));
				}
			}
		}

		IndexServiceLoadBalancer loadBalancer = new IndexServiceLoadBalancer(resources);
		loadBalancer.setPolicy(new RoundRobinLoadBalancePolicy<IndexService>());
		return loadBalancer;
	}

	/**
	 * 
	 * @param connector
	 * @param props
	 * @return
	 */
	public static List<IndexProvider> getIndexProviders(IndexProviderConnectorV1 connector, Map<Object, Object> props) {
		List<IndexProvider> indexProviders = new ArrayList<IndexProvider>();

		if (connector != null) {
			List<URL> urls = getIndexServiceURLs(props);
			for (URL url : urls) {
				Map<Object, Object> properties = toProperties(url);
				IndexProvider indexProvider = connector.getService(properties);
				if (indexProvider != null) {
					indexProviders.add(indexProvider);
				}
			}
		}

		return indexProviders;
	}

	/**
	 * 
	 * @param connector
	 * @param props
	 * @return
	 */
	public static IndexProviderLoadBalancer getIndexProviderLoadBalancer(IndexProviderConnectorV1 connector, Map<Object, Object> props) {
		List<LoadBalanceResource<IndexProvider>> resources = new ArrayList<LoadBalanceResource<IndexProvider>>();

		if (connector != null) {
			List<URL> urls = getIndexServiceURLs(props);
			for (URL url : urls) {
				Map<Object, Object> properties = toProperties(url);
				IndexProvider indexProvider = connector.getService(properties);
				if (indexProvider != null) {
					String resourceId = indexProvider.getURL();
					resources.add(new LoadBalanceResourceImpl<IndexProvider>(indexProvider, resourceId));
				}
			}
		}

		IndexProviderLoadBalancer loadBalancer = new IndexProviderLoadBalancer(resources);
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
			Object obj = props.get(InfraConstants.ORBIT_INDEX_SERVICE_URL);
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
	 * 
	 * @param url
	 * @return
	 */
	private static Map<Object, Object> toProperties(URL url) {
		Map<Object, Object> properties = new HashMap<Object, Object>();
		try {
			String hostURL = null;
			String contextRoot = null;

			String fullURL = url.toExternalForm();
			String path = url.getPath();
			if (path != null && !path.isEmpty()) {
				hostURL = fullURL.substring(0, fullURL.indexOf(path));
				contextRoot = path;
			} else {
				hostURL = fullURL;
			}

			if (hostURL == null) {
				hostURL = "";
			}
			if (contextRoot == null) {
				contextRoot = "";
			}

			properties.put(InfraConstants.INDEX_SERVICE_HOST_URL, hostURL);
			properties.put(InfraConstants.INDEX_SERVICE_CONTEXT_ROOT, contextRoot);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

}
