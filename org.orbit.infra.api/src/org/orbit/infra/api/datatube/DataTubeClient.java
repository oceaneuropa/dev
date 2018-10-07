package org.orbit.infra.api.datatube;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DataTubeClient extends ServiceClient {

	@Override
	DataTubeServiceMetadata getMetadata() throws ClientException;

	/**
	 * 
	 * @param channelId
	 * @param senderId
	 * @param message
	 * @return
	 * @throws ClientException
	 */
	boolean send(String channelId, String senderId, String message) throws ClientException;

}
