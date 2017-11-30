package org.orbit.component.server.tier0.channel.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.component.model.tier0.channel.ChannelConverter;
import org.orbit.component.model.tier0.channel.ChannelException;
import org.orbit.component.model.tier0.channel.dto.ChannelMessageDTO;
import org.orbit.component.server.tier0.channel.service.ChannelService;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel web service resource.
 * 
 * {contextRoot} example: /orbit/v1/channel
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/inbound (Body parameter: ChannelMessageDTO)
 *
 */
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class ChannelWebServiceResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(ChannelWebServiceResource.class);

	@Inject
	public ChannelService service;

	protected ChannelService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("ChannelService is not available.");
		}
		return this.service;
	}

	@POST
	@Path("inbound")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response inbound(ChannelMessageDTO messageDTO) {
		LOG.debug("inbound()");
		try {
			String channelId = messageDTO.getChannelId();
			String senderId = messageDTO.getSenderId();
			String message = messageDTO.getMessage();

			LOG.debug("\tchannelId = " + channelId);
			LOG.debug("\tsenderId = " + senderId);
			LOG.debug("\tmessage = " + message);

			int result = getService().inbound(channelId, senderId, message);

			return Response.ok(result).build();

		} catch (ChannelException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(ChannelConverter.getInstance().toDTO(e)).build();
		}
	}

}
