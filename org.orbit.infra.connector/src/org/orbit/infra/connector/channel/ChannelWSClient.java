package org.orbit.infra.connector.channel;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.infra.model.channel.ChannelMessageDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;

/*
 * Channel web service client.
 * 
 * {contextRoot} example:
 * /orbit/v1/channel
 * 
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/inbound (Body parameter: ChannelMessageDTO)
 * 
 */
public class ChannelWSClient extends AbstractWSClient {

	public ChannelWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Send out message
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/inbound (Body parameter: ChannelMessageDTO)
	 * 
	 * @param messageDTO
	 * @return
	 * @throws ClientException
	 */
	public int sendMessage(ChannelMessageDTO messageDTO) throws ClientException {
		int result = 0;
		Response response = null;
		try {
			Entity<?> formData = Entity.json(new GenericEntity<ChannelMessageDTO>(messageDTO) {
			});

			WebTarget target = getRootPath().path("inbound");
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(formData);
			checkResponse(target, response);

			result = response.readEntity(Integer.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

}
