package org.orbit.infra.io.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.origin.common.service.WebServiceAwareHelper;

public class ConfigRegistryClientResolverImpl implements ConfigRegistryClientResolver {

	@Override
	public ConfigRegistryClient resolve(String accessToken) {
		ConfigRegistryClient configRegistryClient = InfraClientsUtil.CONFIG_REGISTRY.getConfigRegistryClient(accessToken);
		return configRegistryClient;
	}

	@Override
	public String getURL(String configRegistryName, String accessToken) throws IOException {
		if (configRegistryName == null || configRegistryName.isEmpty()) {
			throw new IllegalArgumentException("configRegistryName is empty.");
		}

		IndexItem cfgIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, InfraConstants.IDX__CONFIG_REGISTRY__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currConfigRegistryName = (String) currIndexItem.getProperties().get(InfraConstants.SERVICE__NAME);
			if (configRegistryName.equals(currConfigRegistryName)) {
				cfgIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (cfgIndexItem != null) {
			String hostURL = (String) cfgIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) cfgIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
			String baseURL = (String) cfgIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
			if (hostURL != null && baseURL != null) {
				serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
			} else if (baseURL != null) {
				serviceURL = baseURL;
			}
		}
		return serviceURL;
	}

}
