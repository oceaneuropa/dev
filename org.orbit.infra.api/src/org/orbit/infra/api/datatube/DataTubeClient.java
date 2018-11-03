package org.orbit.infra.api.datatube;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DataTubeClient extends ServiceClient {

	@Override
	DataTubeServiceMetadata getMetadata() throws ClientException;

	/**
	 * Send message to a channel
	 * 
	 * @param channelId
	 * @param senderId
	 * @param message
	 * @return
	 * @throws ClientException
	 */
	boolean send(String channelId, String senderId, String message) throws ClientException;

	RuntimeChannel[] getRuntimeChannels() throws ClientException;

	RuntimeChannel getRuntimeChannelId(String channelId, boolean createIfNotExist) throws ClientException;

	RuntimeChannel getRuntimeChannelByName(String name, boolean createIfNotExist) throws ClientException;

	boolean runtimeChannelExistsById(String channelId) throws ClientException;

	boolean runtimeChannelExistsByName(String name) throws ClientException;

	RuntimeChannel createRuntimeChannelId(String channelId, boolean useExisting) throws ClientException;

	RuntimeChannel createRuntimeChannelByName(String name, boolean useExisting) throws ClientException;

	boolean syncChannelMetadataId(String channelId) throws ClientException;

	boolean syncChannelMetadataByName(String name) throws ClientException;

	boolean deleteRuntimeChannelId(String channelId) throws ClientException;

	boolean deleteRuntimeChannelByName(String name) throws ClientException;

}
