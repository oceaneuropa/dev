package org.orbit.infra.connector.datacast;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

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
				dataTubeConfigs = ModelConverter.DataCast.getDataTubeConfigs(this, response);
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
				dataTubeConfigs = ModelConverter.DataCast.getDataTubeConfigs(this, response);
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
				dataTubeConfig = ModelConverter.DataCast.getDataTubeConfig(this, response);
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
				dataTubeConfig = ModelConverter.DataCast.getDataTubeConfig(this, response);
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
				isUpdated = ModelConverter.DataCast.isUpdated(response);
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
				isUpdated = ModelConverter.DataCast.isUpdated(response);
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
				isUpdated = ModelConverter.DataCast.isUpdated(response);
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
				isUpdated = ModelConverter.DataCast.isUpdated(response);
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

		boolean isUpdated = false;
		Response response = null;
		try {
			response = sendRequest(request);
			if (response != null) {
				isUpdated = ModelConverter.DataCast.isDeleted(response);
			}
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return isUpdated;
	}

}
