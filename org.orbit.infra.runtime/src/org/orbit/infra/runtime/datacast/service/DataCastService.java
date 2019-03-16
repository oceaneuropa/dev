package org.orbit.infra.runtime.datacast.service;

import java.util.List;
import java.util.Map;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.model.AccountConfig;
import org.origin.common.model.UniqueValueAware;
import org.origin.common.rest.editpolicy.EditPoliciesAware;
import org.origin.common.rest.server.ServerException;
import org.origin.common.service.AccessTokenAware;
import org.origin.common.service.WebServiceAware;

public interface DataCastService extends UniqueValueAware, WebServiceAware, ConnectionAware, EditPoliciesAware, AccessTokenAware {

	Map<Object, Object> getInitProperties();

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
	ChannelMetadata[] getChannelMetadatas() throws ServerException;

	ChannelMetadata[] getChannelMetadatas(String dataTubeId) throws ServerException;

	boolean channelMetadataExistsById(String channelId) throws ServerException;

	boolean channelMetadataExistsByName(String name) throws ServerException;

	ChannelMetadata getChannelMetadataById(String channelId) throws ServerException;

	ChannelMetadata getChannelMetadataByName(String name) throws ServerException;

	ChannelMetadata createChannelMetadata(String dataTubeId, String name, String accessType, String accessCode, String ownerAccountId, List<AccountConfig> accountConfigs, Map<String, Object> properties) throws ServerException;

	String allocateDataTubeIdForNewChannel() throws ServerException;

	boolean updateChannelMetadataDataTubeId(String channelId, String dataTubeId) throws ServerException;

	boolean updateChannelMetadataName(String channelId, String name) throws ServerException;

	boolean updateChannelMetadataAccessType(String channelId, String accessType) throws ServerException;

	boolean updateChannelMetadataAccessCode(String channelId, String accessCode) throws ServerException;

	boolean updateChannelMetadataOwnerAccountId(String channelId, String accountId) throws ServerException;

	boolean updateChannelMetadataAccountConfigsById(String channelId, List<AccountConfig> accountConfigs) throws ServerException;

	boolean updateChannelMetadataPropertiesById(String channelId, Map<String, Object> properties) throws ServerException;

	boolean deleteChannelMetadataById(String channelId) throws ServerException;

	boolean deleteChannelMetadataByName(String name) throws ServerException;

}
