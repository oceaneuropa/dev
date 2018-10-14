package org.orbit.infra.runtime.datacast.service;

import java.util.Map;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface DataCastService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	String getDataCastId();

	// -----------------------------------------------------------------
	// DataTube configs
	// -----------------------------------------------------------------
	DataTubeConfig[] getDataTubeConfigs() throws ServerException;

	DataTubeConfig[] getDataTubeConfigs(boolean isEnabled) throws ServerException;

	DataTubeConfig getDataTubeConfig(String configId) throws ServerException;

	DataTubeConfig createDataTubeConfig(String dataTubeId, String dataTubeName, boolean isEnabled, Map<String, Object> properties) throws ServerException;

	boolean updateDataTubeName(String configId, String dataTubeName) throws ServerException;

	boolean updateDataTubeEnabled(String configId, boolean isEnabled) throws ServerException;

	boolean updateDataTubeProperties(String configId, Map<String, Object> properties) throws ServerException;

	boolean deleteDataTubeConfig(String configId) throws ServerException;

	// -----------------------------------------------------------------
	// Channel metadatas
	// -----------------------------------------------------------------
	ChannelMetadata[] getChannels() throws ServerException;

	ChannelMetadata[] getChannels(String dataTubeId) throws ServerException;

	boolean channelExistsById(String channelId) throws ServerException;

	boolean channelExistsByName(String channelName) throws ServerException;

	ChannelMetadata getChannelById(String channelId) throws ServerException;

	ChannelMetadata getChannelByName(String channelName) throws ServerException;

	ChannelMetadata createChannel(String channelName, Map<String, Object> properties) throws ServerException;

	boolean deleteChannelById(String channelId) throws ServerException;

	boolean deleteChannelByName(String channelName) throws ServerException;

}
