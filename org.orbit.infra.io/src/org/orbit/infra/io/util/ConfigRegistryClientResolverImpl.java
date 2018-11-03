package org.orbit.infra.io.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.configregistry.ConfigRegistryClient;
import org.orbit.infra.api.configregistry.ConfigRegistryClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class ConfigRegistryClientResolverImpl implements ConfigRegistryClientResolver {

	protected String indexServiceUrl;

	/**
	 * 
	 * @param indexServiceUrl
	 */
	public ConfigRegistryClientResolverImpl(String indexServiceUrl) {
		if (indexServiceUrl == null || indexServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("indexServiceUrl is empty.");
		}
		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public ConfigRegistryClient resolve(String configRegistryUrl, String accessToken) {
		if (configRegistryUrl == null || configRegistryUrl.isEmpty()) {
			throw new IllegalArgumentException("configRegistryUrl is empty.");
		}

		ConfigRegistryClient configRegistryClient = InfraClientsHelper.CONFIG_REGISTRY.getConfigRegistryClient(configRegistryUrl, accessToken);
		return configRegistryClient;
	}

	@Override
	public String getURL(String configRegistryName, String accessToken) throws IOException {
		if (configRegistryName == null || configRegistryName.isEmpty()) {
			throw new IllegalArgumentException("configRegistryName is empty.");
		}

		IndexItem cfgIndexItem = null;
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(this.indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__CONFIG_REGISTRY__INDEXER_ID, InfraConstants.IDX__CONFIG_REGISTRY__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currConfigRegistryName = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__CONFIG_REGISTRY__NAME);
			if (configRegistryName.equals(currConfigRegistryName)) {
				cfgIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (cfgIndexItem != null) {
			String hostURL = (String) cfgIndexItem.getProperties().get(InfraConstants.IDX_PROP__CONFIG_REGISTRY__HOST_URL);
			String contextRoot = (String) cfgIndexItem.getProperties().get(InfraConstants.IDX_PROP__CONFIG_REGISTRY__CONTEXT_ROOT);
			String baseURL = (String) cfgIndexItem.getProperties().get(InfraConstants.IDX_PROP__CONFIG_REGISTRY__BASE_URL);
			if (hostURL != null && baseURL != null) {
				serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
			} else if (baseURL != null) {
				serviceURL = baseURL;
			}
		}
		return serviceURL;
	}

}
