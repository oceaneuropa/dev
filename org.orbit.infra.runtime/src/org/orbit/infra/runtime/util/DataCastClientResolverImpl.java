package org.orbit.infra.runtime.util;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.origin.common.service.WebServiceAwareHelper;

public class DataCastClientResolverImpl implements DataCastClientResolver {

	protected String indexServiceUrl;

	/**
	 * 
	 * @param indexServiceUrl
	 */
	public DataCastClientResolverImpl(String indexServiceUrl) {
		if (indexServiceUrl == null || indexServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("indexServiceUrl is empty.");
		}

		this.indexServiceUrl = indexServiceUrl;
	}

	@Override
	public DataCastClient resolve(String dataCastServiceUrl, String accessToken) {
		if (dataCastServiceUrl == null || dataCastServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dataCastServiceUrl is empty.");
		}

		DataCastClient dataCastClient = InfraClientsUtil.DataCast.getDataCastClient(dataCastServiceUrl, accessToken);
		return dataCastClient;
	}

	@Override
	public String getURL(String dataCastId, String accessToken) throws IOException {
		if (dataCastId == null || dataCastId.isEmpty()) {
			throw new IllegalArgumentException("dataCastId is empty.");
		}

		IndexItem dataCastIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.Indexes.getIndexServiceClient(this.indexServiceUrl, accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDataCastId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__ID);
			if (dataCastId.equals(currDataCastId)) {
				dataCastIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (dataCastIndexItem != null) {
			String hostURL = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__HOST_URL);
			String contextRoot = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__CONTEXT_ROOT);
			serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		}
		return serviceURL;
	}

}
