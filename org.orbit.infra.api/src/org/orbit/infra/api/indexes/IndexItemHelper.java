package org.orbit.infra.api.indexes;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.model.ServiceMetadataDTO;
import org.origin.common.service.WebServiceAwareHelper;

public class IndexItemHelper {

	public static IndexItemHelper INSTANCE = new IndexItemHelper();

	/**
	 * 
	 * @param indexItem
	 * @return
	 */
	public boolean isOnline(IndexItem indexItem) {
		// if (indexItem != null && isLastHeartbeatWithinSeconds(indexItem, 16)) {
		String serviceURL = null;
		if (indexItem != null) {
			// String indexerId = indexItem.getIndexProviderId();
			//
			// boolean checkStarted = false;
			// if (InfraConstants.PLATFORM_INDEXER_ID.equals(indexerId)) {
			// checkStarted = true;
			// }
			//
			// if (checkStarted) {
			// String runtimeState = (String) indexItem.getProperties().get(PLATFORM_RUNTIME_STATE);
			// if ("STARTED".equalsIgnoreCase(runtimeState)) {
			// return true;
			// }
			// } else {
			// return true;
			// }
			serviceURL = (String) indexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
		}

		if (serviceURL != null && !serviceURL.isEmpty()) {
			Map<String, Object> properties = new HashMap<String, Object>();
			// properties.put(WSClientConstants.REALM, null);
			// properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
			properties.put(WSClientConstants.URL, serviceURL);

			try {
				WSClientConfiguration config = WSClientConfiguration.create(properties);
				WSClient wsClient = new WSClient(config);
				if (wsClient.doPing()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * @param indexItem
	 * @return
	 */
	public Map<String, Object> getMetadata(IndexItem indexItem) {
		Map<String, Object> metadataProperties = null;
		if (indexItem != null) {
			String serviceURL = (String) indexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
			if (serviceURL != null) {
				if (!serviceURL.endsWith("/")) {
					serviceURL += "/";
				}
				serviceURL += "metadata";
			}

			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(WSClientConstants.URL, serviceURL);
			try {
				WSClientConfiguration config = WSClientConfiguration.create(properties);
				WSClient wsClient = new WSClient(config);
				ServiceMetadataDTO metadataDTO = wsClient.getMetadata();

				if (metadataDTO != null) {
					metadataProperties = metadataDTO.getProperties();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (metadataProperties == null) {
			metadataProperties = new HashMap<String, Object>();
		}
		return metadataProperties;
	}

	/**
	 * 
	 * @param indexItem
	 * @return
	 */
	public String getServiceURL(IndexItem indexItem) {
		String serviceUrl = null;
		if (indexItem != null) {
			String baseUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
			if (baseUrl != null && !baseUrl.isEmpty()) {
				serviceUrl = baseUrl;
			} else {
				String hostUrl = (String) indexItem.getProperties().get(InfraConstants.SERVICE__HOST_URL);
				String contextRoot = (String) indexItem.getProperties().get(InfraConstants.SERVICE__CONTEXT_ROOT);
				serviceUrl = WebServiceAwareHelper.INSTANCE.getURL(hostUrl, contextRoot);
			}
		}
		return serviceUrl;
	}

}

// public static String PLATFORM_RUNTIME_STATE = "platform.runtime_state";
// /**
// *
// * @param indexItem
// * @param withinSeconds
// * @return
// */
// protected boolean isLastHeartbeatWithinSeconds(IndexItem indexItem, long withinSeconds) {
// Date lastHeartbeatTime = getLastUpdateTime(indexItem);
// Date now = new Date();
// if (lastHeartbeatTime != null) {
// long milliseconds = now.getTime() - lastHeartbeatTime.getTime();
// long withinMilliseconds = withinSeconds * 1000;
// if (milliseconds <= withinMilliseconds) {
// return true;
// }
// }
// return false;
// }
// /**
// *
// * @param indexItem
// * @return
// */
// public Date getLastUpdateTime(IndexItem indexItem) {
// Date date = null;
// if (indexItem != null) {
// Object value = indexItem.getProperties().get(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME);
// if (value instanceof Long) {
// date = new Date((Long) value);
// } else if (value instanceof Date) {
// date = (Date) value;
// }
// }
// return date;
// }

/// **
// *
// * @param serviceUrl
// * @param accessToken
// * @return
// */
// public boolean isOnline(String serviceUrl, String accessToken) {
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(WSClientConstants.REALM, null);
// if (accessToken != null) {
// properties.put(WSClientConstants.ACCESS_TOKEN, accessToken);
// }
// properties.put(WSClientConstants.URL, serviceUrl);
// WSClientConfiguration config = WSClientConfiguration.create(properties);
// WSClient wsClient = new WSClient(config);
// if (wsClient.doPing()) {
// return true;
// }
// return false;
// }
