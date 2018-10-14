package org.orbit.infra.api.datacast;

import java.util.List;
import java.util.Map;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DataCastClient extends ServiceClient {

	@Override
	DataCastServiceMetadata getMetadata() throws ClientException;

	DataTubeConfig[] getDataTubeConfigs() throws ClientException;

	DataTubeConfig[] getDataTubeConfigs(boolean isEnabled) throws ClientException;

	DataTubeConfig getDataTubeConfig(String id) throws ClientException;

	DataTubeConfig createDataTubeConfig(String dataTubeId, String dataTubeName, boolean isEnabled, Map<String, Object> properties) throws ClientException;

	boolean updateDataTubeName(String id, String dataTubeName) throws ClientException;

	boolean updateDataTubeEnabled(String id, boolean isEnabled) throws ClientException;

	boolean setDataTubeProperties(String configId, Map<String, Object> properties) throws ClientException;

	boolean removeDataTubeProperties(String configId, List<String> propertyNames) throws ClientException;

	boolean deleteDataTubeConfig(String configId) throws ClientException;

}
