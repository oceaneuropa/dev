package org.orbit.infra.io.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.origin.common.service.WebServiceAwareHelper;

public class DefaultDataTubeClientResolver implements DataTubeClientResolver {

	@Override
	public DataTubeClient resolve(String dataTubeServiceUrl, String accessToken) {
		if (dataTubeServiceUrl == null || dataTubeServiceUrl.isEmpty()) {
			throw new IllegalArgumentException("dataTubeServiceUrl is empty.");
		}

		DataTubeClient dataTubeClient = InfraClientsUtil.DATA_TUBE.getDataTubeClient(dataTubeServiceUrl, accessToken);
		return dataTubeClient;
	}

	@Override
	public String getURL(String dataCastId, String dataTubeId, String accessToken) throws IOException {
		if (dataCastId == null || dataCastId.isEmpty()) {
			throw new IllegalArgumentException("dataCastId is empty.");
		}
		if (dataTubeId == null || dataTubeId.isEmpty()) {
			throw new IllegalArgumentException("dataTubeId is empty.");
		}

		IndexItem dataTubeIndexItem = null;
		IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDataCastId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
			String currDataTubeId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__ID);
			if (dataCastId.equals(currDataCastId) && dataTubeId.equals(currDataTubeId)) {
				dataTubeIndexItem = currIndexItem;
				break;
			}
		}

		String serviceURL = null;
		if (dataTubeIndexItem != null) {
			String hostURL = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
			serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
		}
		return serviceURL;
	}

	@Override
	public DataTubeClient[] resolve(String dataCastId, String accessToken, Comparator<?> indexItemsComparator) throws IOException {
		if (dataCastId == null || dataCastId.isEmpty()) {
			throw new IllegalArgumentException("dataCastId is empty.");
		}

		List<DataTubeClient> dataTubeClients = new ArrayList<DataTubeClient>();

		List<IndexItem> dataTubeIndexItems = new ArrayList<IndexItem>();
		IndexServiceClient indexService = InfraClientsUtil.INDEX_SERVICE.getIndexServiceClient(accessToken);
		List<IndexItem> indexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);
		for (IndexItem currIndexItem : indexItems) {
			String currDataCastId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
			if (currDataCastId != null && currDataCastId.equals(dataCastId)) {
				dataTubeIndexItems.add(currIndexItem);
			}
		}

		if (indexItemsComparator != null) {
			Collections.sort(dataTubeIndexItems, (Comparator<IndexItem>) indexItemsComparator);
		}

		for (IndexItem currDataTubeIndexItem : dataTubeIndexItems) {
			String hostURL = (String) currDataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
			String contextRoot = (String) currDataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);

			String serviceURL = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
			if (serviceURL != null) {
				DataTubeClient dataTubeClient = resolve(serviceURL, accessToken);
				if (dataTubeClient != null) {
					dataTubeClients.add(dataTubeClient);
				}
			}
		}

		return dataTubeClients.toArray(new DataTubeClient[dataTubeClients.size()]);
	}

	@Override
	public DataTubeClient resolve(String dataCastId, String dataTubeId, String accessToken) throws IOException {
		if (dataCastId == null || dataCastId.isEmpty()) {
			throw new IllegalArgumentException("dataCastId is empty.");
		}
		if (dataTubeId == null || dataTubeId.isEmpty()) {
			throw new IllegalArgumentException("dataTubeId is empty.");
		}

		DataTubeClient dataTubeClient = null;
		String serviceURL = getURL(dataCastId, dataTubeId, accessToken);
		if (serviceURL != null) {
			dataTubeClient = resolve(serviceURL, accessToken);
		}
		return dataTubeClient;
	}

}
