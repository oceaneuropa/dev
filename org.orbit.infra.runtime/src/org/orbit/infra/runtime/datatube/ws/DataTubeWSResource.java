package org.orbit.infra.runtime.datatube.ws;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.channel.ChannelException;
import org.orbit.infra.model.channel.ChannelMessageDTO;
import org.orbit.infra.runtime.datatube.service.Channel;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.util.ModelConverter;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data tube web service resource.
 * 
 * {contextRoot} example: /orbit/v1/datatube
 *
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/inbound (Body parameter: ChannelMessageDTO)
 *
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.USER })
@javax.ws.rs.Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DataTubeWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(DataTubeWSResource.class);

	@Inject
	public DataTubeService service;

	public DataTubeService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("DataTubeService is not available.");
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

			int result = 0;
			Channel channel = getService().getChannel(channelId);
			if (channel != null) {
				result = channel.onMessage(senderId, message);
			}

			return Response.status(Status.OK).entity(result).build();

		} catch (ChannelException e) {
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity(ModelConverter.Channel.toDTO(e)).build();
		}
	}

}
