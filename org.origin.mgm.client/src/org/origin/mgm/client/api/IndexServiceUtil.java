package org.origin.mgm.client.api;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexServiceUtil {

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static List<IndexService> getIndexServices(Map<Object, Object> props) {
		List<URL> urls = getInitialIndexServiceURLs(props);

		List<IndexService> indexServices = new ArrayList<IndexService>();
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm(), null, null);
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
		List<URL> urls = getInitialIndexServiceURLs(props);

		List<IndexProvider> indexProviders = new ArrayList<IndexProvider>();
		for (URL url : urls) {
			IndexServiceConfiguration config = new IndexServiceConfiguration(url.toExternalForm(), null, null);
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
	protected static List<URL> getInitialIndexServiceURLs(Map<Object, Object> props) {
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

}
