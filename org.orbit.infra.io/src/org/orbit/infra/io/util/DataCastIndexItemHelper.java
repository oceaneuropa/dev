package org.orbit.infra.io.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.origin.common.service.WebServiceAwareHelper;

public class DataCastIndexItemHelper {

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getDataCastIndexItemsList(String indexServiceUrl, String accessToken) throws IOException {
		List<IndexItem> dataCastIndexItems = null;
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			dataCastIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE);
		}
		if (dataCastIndexItems == null) {
			dataCastIndexItems = new ArrayList<IndexItem>();
		}
		return dataCastIndexItems;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	public static Map<String, IndexItem> getDataCastIndexItemsMap(String indexServiceUrl, String accessToken) throws IOException {
		Map<String, IndexItem> dataCastIndexItemMap = new HashMap<String, IndexItem>();
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			List<IndexItem> dataCastIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE);
			if (dataCastIndexItems != null) {
				for (IndexItem dataCastIndexItem : dataCastIndexItems) {
					String dataCastId = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__ID);
					dataCastIndexItemMap.put(dataCastId, dataCastIndexItem);
				}
			}
		}
		return dataCastIndexItemMap;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dataCastId
	 * @return
	 * @throws IOException
	 */
	public static IndexItem getDataCastIndexItem(String indexServiceUrl, String accessToken, String dataCastId) throws IOException {
		IndexItem dataCastIndexItem = null;
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			List<IndexItem> dataCastIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATACAST__INDEXER_ID, InfraConstants.IDX__DATACAST__TYPE);
			if (dataCastIndexItems != null) {
				for (IndexItem currIndexItem : dataCastIndexItems) {
					String currDfsId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__ID);
					if (dataCastId != null && dataCastId.equals(currDfsId)) {
						dataCastIndexItem = currIndexItem;
						break;
					}
				}
			}
		}
		return dataCastIndexItem;
	}

	/**
	 * 
	 * @param dataCastIndexItem
	 * @return
	 * @see OrbitClientHelper.getNodeControlClient(String indexServiceUrl, String accessToken, String platformId) throws IOException
	 */
	public static String getDataCastServiceUrl(IndexItem dataCastIndexItem) {
		String dataCastServiceUrl = null;
		if (dataCastIndexItem != null) {
			String baseURL = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
			String hostURL = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__HOST_URL);
			String contextRoot = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__CONTEXT_ROOT);

			if (baseURL != null && !baseURL.isEmpty()) {
				dataCastServiceUrl = baseURL;
			} else {
				if (hostURL != null && contextRoot != null) {
					dataCastServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
				}
			}
		}
		return dataCastServiceUrl;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dataCastId
	 * @return
	 * @throws IOException
	 */
	public static List<IndexItem> getDataTubeIndexItems(String indexServiceUrl, String accessToken, String dataCastId) throws IOException {
		List<IndexItem> dataTubeIndexItems = new ArrayList<IndexItem>();
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null) {
			List<IndexItem> allDataTubeIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);

			for (IndexItem currIndexItem : allDataTubeIndexItems) {
				String currDataCastId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
				// String currDataTubeId = (String) currIndexItem.getProperties().get(SubstanceConstants.IDX_PROP__DATATUBE__ID);
				if (dataCastId != null && dataCastId.equals(currDataCastId)) {
					dataTubeIndexItems.add(currIndexItem);
				}
			}
		}
		return dataTubeIndexItems;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dataCastId
	 * @return
	 * @throws IOException
	 */
	public static Map<String, IndexItem> getDataTubeIndexItemsMap(String indexServiceUrl, String accessToken, String dataCastId) throws IOException {
		Map<String, IndexItem> dataTubeIndexItemMap = new HashMap<String, IndexItem>();
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (dataCastId != null && indexService != null) {
			List<IndexItem> allDataTubeIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);

			for (IndexItem dataTubeIndexItem : allDataTubeIndexItems) {
				String currDataCastId = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
				String currDataTubeId = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__ID);

				if (dataCastId.equals(currDataCastId)) {
					dataTubeIndexItemMap.put(currDataTubeId, dataTubeIndexItem);
				}
			}
		}
		return dataTubeIndexItemMap;
	}

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 * @param dataCastId
	 * @return
	 * @throws IOException
	 */
	public static IndexItem getDataTubeIndexItem(String indexServiceUrl, String accessToken, String dataCastId, String dataTubeId) throws IOException {
		IndexItem dataTubeIndexItem = null;
		IndexServiceClient indexService = InfraClientsHelper.INDEX_SERVICE.getIndexServiceClient(indexServiceUrl, accessToken);
		if (indexService != null && dataCastId != null && dataTubeId != null) {
			List<IndexItem> allDataTubeIndexItems = indexService.getIndexItems(InfraConstants.IDX__DATATUBE__INDEXER_ID, InfraConstants.IDX__DATATUBE__TYPE);

			for (IndexItem currIndexItem : allDataTubeIndexItems) {
				String currDataCastId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__DATACAST_ID);
				String currDataTubeId = (String) currIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__ID);
				if (dataCastId.equals(currDataCastId) && dataTubeId.equals(currDataTubeId)) {
					dataTubeIndexItem = currIndexItem;
					break;
				}
			}
		}
		return dataTubeIndexItem;
	}

	/**
	 * 
	 * @param dataTubeIndexItem
	 * @return
	 * @see OrbitClientHelper.getNodeControlClient(String indexServiceUrl, String accessToken, String platformId) throws IOException
	 */
	public static String getDataTubeServiceUrl(IndexItem dataTubeIndexItem) {
		String dataTubeServiceUrl = null;
		if (dataTubeIndexItem != null) {
			String baseURL = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__BASE_URL);
			String hostURL = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__HOST_URL);
			String contextRoot = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__CONTEXT_ROOT);

			if (baseURL != null && !baseURL.isEmpty()) {
				dataTubeServiceUrl = baseURL;
			} else {
				if (hostURL != null && contextRoot != null) {
					dataTubeServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostURL, contextRoot);
				}
			}
		}
		return dataTubeServiceUrl;
	}

}
