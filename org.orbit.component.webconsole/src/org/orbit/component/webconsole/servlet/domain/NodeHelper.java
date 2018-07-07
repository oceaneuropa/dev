package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

public class NodeHelper {

	public static NodeHelper INSTANCE = new NodeHelper();

	/**
	 * 
	 * @param indexService
	 * @param platformParentId
	 * @return
	 * @throws IOException
	 */
	public Map<String, IndexItem> getNodePlatformIdToIndexItem(IndexService indexService, String platformParentId) throws IOException {
		Map<String, IndexItem> platformIdToIndexItem = new HashMap<String, IndexItem>();
		if (indexService != null && platformParentId != null) {
			List<IndexItem> indexItems = indexService.getIndexItems(WebConstants.PLATFORM_INDEXER_ID, WebConstants.PLATFORM_INDEXER_TYPE);
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					String currPlatformId = (String) indexItem.getProperties().get(WebConstants.PLATFORM_ID);
					String currPlatformParentId = (String) indexItem.getProperties().get(WebConstants.PLATFORM_PARENT_ID);
					String currPlatformType = (String) indexItem.getProperties().get(WebConstants.PLATFORM_TYPE);

					if (WebConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId)) {
						platformIdToIndexItem.put(currPlatformId, indexItem);
					}
				}
			}
		}
		return platformIdToIndexItem;
	}

	/**
	 * 
	 * @param indexService
	 * @param platformParentId
	 * @param nodePlatformid
	 * @return
	 * @throws IOException
	 */
	public IndexItem getNodeIndexItem(IndexService indexService, String platformParentId, String nodePlatformid) throws IOException {
		IndexItem nodeIndexItem = null;
		if (indexService != null && platformParentId != null && nodePlatformid != null) {
			List<IndexItem> indexItems = indexService.getIndexItems(WebConstants.PLATFORM_INDEXER_ID, WebConstants.PLATFORM_INDEXER_TYPE);
			if (indexItems != null) {
				for (IndexItem indexItem : indexItems) {
					String currPlatformId = (String) indexItem.getProperties().get(WebConstants.PLATFORM_ID);
					String currPlatformParentId = (String) indexItem.getProperties().get(WebConstants.PLATFORM_PARENT_ID);
					String currPlatformType = (String) indexItem.getProperties().get(WebConstants.PLATFORM_TYPE);

					if (WebConstants.PLATFORM_TYPE__NODE.equalsIgnoreCase(currPlatformType) && platformParentId.equals(currPlatformParentId) && nodePlatformid.equals(currPlatformId)) {
						nodeIndexItem = indexItem;
						break;
					}
				}
			}
		}
		return nodeIndexItem;
	}

}
