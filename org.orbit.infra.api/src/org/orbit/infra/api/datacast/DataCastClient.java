package org.orbit.infra.api.datacast;

import java.util.List;
import java.util.Map;

import org.origin.common.model.AccountConfig;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DataCastClient extends ServiceClient {

	@Override
	DataCastServiceMetadata getMetadata() throws ClientException;

	// ----------------------------------------------------------------------------------------------------------------
	// Data tube configs
	// ----------------------------------------------------------------------------------------------------------------
	DataTubeConfig[] getDataTubeConfigs() throws ClientException;

	DataTubeConfig[] getDataTubeConfigs(boolean isEnabled) throws ClientException;

	DataTubeConfig getDataTubeConfig(String id) throws ClientException;

	DataTubeConfig createDataTubeConfig(String dataTubeId, String dataTubeName, boolean isEnabled, Map<String, Object> properties) throws ClientException;

	boolean updateDataTubeName(String id, String dataTubeName) throws ClientException;

	boolean updateDataTubeEnabled(String id, boolean isEnabled) throws ClientException;

	boolean setDataTubeProperties(String configId, Map<String, Object> properties) throws ClientException;

	boolean removeDataTubeProperties(String configId, List<String> propertyNames) throws ClientException;

	boolean deleteDataTubeConfig(String configId) throws ClientException;

	// ----------------------------------------------------------------------------------------------------------------
	// Channel metadatas
	// ----------------------------------------------------------------------------------------------------------------
	ChannelMetadata[] getChannelMetadatas() throws ClientException;

	ChannelMetadata[] getChannelMetadatas(String dataTubeId) throws ClientException;

	ChannelMetadata getChannelMetadataById(String channelId) throws ClientException;

	ChannelMetadata getChannelMetadataByName(String name) throws ClientException;

	boolean channelMetadataExistsById(String channelId) throws ClientException;

	boolean channelMetadataExistsByName(String name) throws ClientException;

	ChannelMetadata createChannelMetadata(String dataTubeId, String name, String accessType, String accessCode, String ownerAccountId, List<AccountConfig> accountConfigs, Map<String, Object> properties) throws ClientException;

	String allocateDataTubeIdGet() throws ClientException;

	String allocateDataTubeIdCmd() throws ClientException;

	boolean updateChannelMetadataById(String channelId, boolean updateName, String name, boolean updateAccessType, String accessType, boolean updateAccessCode, String accessCode, boolean updateOwnerAccountId, String ownerAccountId) throws ClientException;

	boolean setChannelMetadataStatusById(String channelId, ChannelStatus channelStatus, boolean append) throws ClientException;

	boolean clearChannelMetadataStatusById(String channelId, ChannelStatus channelStatus) throws ClientException;

	boolean setChannelMetadataAccountConfigsById(String channelId, List<AccountConfig> accountConfigs, boolean appendAccountConfigs, boolean appendAccountConfig) throws ClientException;

	boolean removeChannelMetadataAccountConfigsById(String channelId, List<String> accountIds) throws ClientException;

	boolean setChannelMetadataPropertiesById(String channelId, Map<String, Object> properties) throws ClientException;

	boolean removeChannelMetadataPropertiesById(String channelId, List<String> propertyNames) throws ClientException;

	boolean deleteChannelMetadataById(String channelId) throws ClientException;

	boolean deleteChannelMetadataByName(String name) throws ClientException;

}
