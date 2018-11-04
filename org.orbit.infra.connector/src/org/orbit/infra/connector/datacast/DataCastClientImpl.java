package org.orbit.infra.connector.datacast;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.datacast.DataTubeConfig;
import org.orbit.infra.connector.util.ModelConverter;
import org.orbit.infra.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.ServiceMetadataDTO;
import org.origin.common.rest.util.ResponseUtil;

public class DataCastClientImpl extends ServiceClientImpl<DataCastClient, DataCastWSClient> implements DataCastClient {

	private static final DataTubeConfig[] EMPTY_DATATUBE_CONFIGS = new DataTubeConfig[0];
	private static final ChannelMetadata[] EMPTY_CHANNEL_METADATAS = new ChannelMetadata[0];

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DataCastClientImpl(ServiceConnector<DataCastClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DataCastWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new DataCastWSClient(config);
	}

	@Override
	public DataCastServiceMetadata getMetadata() throws ClientException {
		DataCastServiceMetadataImpl metadata = new DataCastServiceMetadataImpl();
		ServiceMetadataDTO metadataDTO = getWSClient().getMetadata();
		if (metadataDTO != null) {
			Map<String, Object> properties = metadataDTO.getProperties();
			if (properties != null && !properties.isEmpty()) {
				metadata.getProperties().putAll(properties);
			}
		}
		return metadata;
	}

	@Override
	public DataTubeConfig[] getDataTubeConfigs() throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__LIST_DATATUBE_CONFIGS);

		DataTubeConfig[] dataTubeConfigs = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataTubeConfigs = ModelConverter.DATA_CAST.getDataTubeConfigs(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (dataTubeConfigs == null) {
			dataTubeConfigs = EMPTY_DATATUBE_CONFIGS;
		}
		return dataTubeConfigs;
	}

	@Override
	public DataTubeConfig[] getDataTubeConfigs(boolean isEnabled) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__LIST_DATATUBE_CONFIGS);
		request.setParameter("enabled", isEnabled);

		DataTubeConfig[] dataTubeConfigs = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataTubeConfigs = ModelConverter.DATA_CAST.getDataTubeConfigs(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (dataTubeConfigs == null) {
			dataTubeConfigs = EMPTY_DATATUBE_CONFIGS;
		}
		return dataTubeConfigs;
	}

	@Override
	public DataTubeConfig getDataTubeConfig(String id) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__GET_DATATUBE_CONFIG);
		request.setParameter("id", id);

		DataTubeConfig dataTubeConfig = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataTubeConfig = ModelConverter.DATA_CAST.getDataTubeConfig(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return dataTubeConfig;
	}

	@Override
	public DataTubeConfig createDataTubeConfig(String dataTubeId, String dataTubeName, boolean isEnabled, Map<String, Object> properties) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__CREATE_DATATUBE_CONFIG);
		request.setParameter("data_tube_id", dataTubeId);
		request.setParameter("name", dataTubeName);
		request.setParameter("enabled", isEnabled);
		request.setParameter("properties", properties);

		DataTubeConfig dataTubeConfig = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				dataTubeConfig = ModelConverter.DATA_CAST.getDataTubeConfig(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return dataTubeConfig;
	}

	@Override
	public boolean updateDataTubeName(String id, String dataTubeName) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__UPDATE_DATATUBE_CONFIG);
		request.setParameter("id", id);
		request.setParameter("name", dataTubeName);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean updateDataTubeEnabled(String id, boolean isEnabled) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__UPDATE_DATATUBE_CONFIG);
		request.setParameter("id", id);
		request.setParameter("enabled", isEnabled);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setDataTubeProperties(String configId, Map<String, Object> properties) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__SET_DATATUBE_CONFIG_PROPERTIES);
		request.setParameter("id", configId);
		request.setParameter("properties", properties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeDataTubeProperties(String configId, List<String> propertyNames) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__REMOVE_DATATUBE_CONFIG_PROPERTIES);
		request.setParameter("id", configId);
		request.setParameter("property_names", propertyNames);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteDataTubeConfig(String configId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__DELETE_DATATUBE_CONFIG);
		request.setParameter("id", configId);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public ChannelMetadata[] getChannelMetadatas() throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__LIST_CHANNEL_METADATAS);

		ChannelMetadata[] channelMetadatas = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				channelMetadatas = ModelConverter.DATA_CAST.getChannelMetadatas(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (channelMetadatas == null) {
			channelMetadatas = EMPTY_CHANNEL_METADATAS;
		}
		return channelMetadatas;
	}

	@Override
	public ChannelMetadata[] getChannelMetadatas(String dataTubeId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__LIST_CHANNEL_METADATAS);
		request.setParameter("data_tube_id", dataTubeId);

		ChannelMetadata[] channelMetadatas = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				channelMetadatas = ModelConverter.DATA_CAST.getChannelMetadatas(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		if (channelMetadatas == null) {
			channelMetadatas = EMPTY_CHANNEL_METADATAS;
		}
		return channelMetadatas;
	}

	@Override
	public ChannelMetadata getChannelMetadataById(String channelId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__GET_CHANNEL_METADATA);
		request.setParameter("channel_id", channelId);

		ChannelMetadata channelMetadata = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				channelMetadata = ModelConverter.DATA_CAST.getChannelMetadata(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return channelMetadata;
	}

	@Override
	public ChannelMetadata getChannelMetadataByName(String name) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__GET_CHANNEL_METADATA);
		request.setParameter("name", name);

		ChannelMetadata channelMetadata = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				channelMetadata = ModelConverter.DATA_CAST.getChannelMetadata(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return channelMetadata;
	}

	@Override
	public boolean channelMetadataExistsById(String channelId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__CHANNEL_METADATA_EXISTS);
		request.setParameter("channel_id", channelId);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public boolean channelMetadataExistsByName(String name) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__CHANNEL_METADATA_EXISTS);
		request.setParameter("name", name);

		boolean exists = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				exists = ModelConverter.COMMON.exists(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return exists;
	}

	@Override
	public ChannelMetadata createChannelMetadata(String dataTubeId, String name, String accessType, String accessCode, String ownerAccountId, List<String> accountIds, Map<String, Object> properties) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__CREATE_CHANNEL_METADATA);
		request.setParameter("data_tube_id", dataTubeId);
		request.setParameter("name", name);
		if (accessType != null) {
			request.setParameter("access_type", accessType);
		}
		if (accessCode != null) {
			request.setParameter("access_code", accessCode);
		}
		if (ownerAccountId != null) {
			request.setParameter("owner_account_id", ownerAccountId);
		}
		if (accountIds != null) {
			request.setParameter("account_ids", accountIds);
		}
		request.setParameter("properties", properties);

		ChannelMetadata channelMetadata = null;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				channelMetadata = ModelConverter.DATA_CAST.getChannelMetadata(this, response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return channelMetadata;
	}

	@Override
	public boolean updateChannelMetadataById(String channelId, boolean updateName, String name, boolean updateAccessType, String accessType, boolean updateAccessCode, String accessCode, boolean updateOwnerAccountId, String ownerAccountId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__UPDATE_CHANNEL_METADATA);
		request.setParameter("channel_id", channelId);

		if (updateName) {
			request.setParameter("update_name", true);
			request.setParameter("name", name);
		}

		if (updateAccessType) {
			request.setParameter("update_access_type", true);
			request.setParameter("access_type", accessType);
		}

		if (updateAccessCode) {
			request.setParameter("update_access_code", true);
			request.setParameter("access_code", accessCode);
		}

		if (updateOwnerAccountId) {
			request.setParameter("update_owner_account_id", true);
			request.setParameter("owner_account_id", ownerAccountId);
		}

		if (updateOwnerAccountId) {
			request.setParameter("update_owner_account_id", true);
			request.setParameter("owner_account_id", ownerAccountId);
		}

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setChannelMetadataStatusById(String channelId, int status, boolean append) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__SET_CHANNEL_METADATA_STATUS);
		request.setParameter("channel_id", channelId);
		request.setParameter("status", status);
		if (append) {
			request.setParameter("append", append);
		}

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean clearChannelMetadataStatusById(String channelId, int status) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__CLEAR_CHANNEL_METADATA_STATUS);
		request.setParameter("channel_id", channelId);
		request.setParameter("status", status);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean addChannelMetadataAccountIdsById(String channelId, List<String> accountIds) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__ADD_CHANNEL_METADATA_ACCOUNT_IDS);
		request.setParameter("channel_id", channelId);
		request.setParameter("accountIds", accountIds);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeChannelMetadataAccountIdsById(String channelId, List<String> accountIds) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__REMOVE_CHANNEL_METADATA_ACCOUNT_IDS);
		request.setParameter("channel_id", channelId);
		request.setParameter("accountIds", accountIds);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean setChannelMetadataPropertiesById(String channelId, Map<String, Object> properties) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__SET_CHANNEL_METADATA_PROPERTIES);
		request.setParameter("channel_id", channelId);
		request.setParameter("properties", properties);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean removeChannelMetadataPropertiesById(String channelId, List<String> propertyNames) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__REMOVE_CHANNEL_METADATA_PROPERTIES);
		request.setParameter("channel_id", channelId);
		request.setParameter("property_names", propertyNames);

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.COMMON.isUpdated(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

	@Override
	public boolean deleteChannelMetadataById(String channelId) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__DELETE_CHANNEL_METADATA);
		request.setParameter("channel_id", channelId);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

	@Override
	public boolean deleteChannelMetadataByName(String name) throws ClientException {
		Request request = new Request(RequestConstants.DATACAST__DELETE_CHANNEL_METADATA);
		request.setParameter("name", name);

		boolean isDeleted = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isDeleted = ModelConverter.COMMON.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isDeleted;
	}

}

// boolean setChannelMetadataPropertiesByName(String channelId, Map<String, Object> properties) throws ClientException;
// boolean removeChannelMetadataPropertiesByName(String channelId, List<String> propertyNames) throws ClientException;
//
// @Override
// public boolean setChannelMetadataPropertiesByName(String name, Map<String, Object> properties) throws ClientException {
// Request request = new Request(RequestConstants.DATACAST__SET_CHANNEL_METADATA_PROPERTIES);
// request.setParameter("name", name);
// request.setParameter("properties", properties);
//
// boolean isUpdated = false;
// Response response = null;
// try {
// response = sendRequest(request);
// if (response != null) {
// isUpdated = ModelConverter.COMMON.isUpdated(response);
// }
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// return isUpdated;
// }
//
// @Override
// public boolean removeChannelMetadataPropertiesByName(String name, List<String> propertyNames) throws ClientException {
// Request request = new Request(RequestConstants.DATACAST__REMOVE_CHANNEL_METADATA_PROPERTIES);
// request.setParameter("name", name);
// request.setParameter("property_names", propertyNames);
//
// boolean isUpdated = false;
// Response response = null;
// try {
// response = sendRequest(request);
// if (response != null) {
// isUpdated = ModelConverter.COMMON.isUpdated(response);
// }
// } finally {
// ResponseUtil.closeQuietly(response, true);
// }
// return isUpdated;
// }
