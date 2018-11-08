package org.orbit.infra.runtime.datatube.ws;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.datatube.ChannelMessageDTO;
import org.orbit.infra.runtime.datatube.service.DataTubeService;
import org.orbit.infra.runtime.datatube.service.RuntimeChannel;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.rest.server.ServerException;
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
	@Path("request")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response request(@Context HttpHeaders httpHeaders, Request request) {
		return super.request(httpHeaders, request);
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

			RuntimeChannel channel = null;
			try {
				channel = getService().getRuntimeChannelById(channelId);
			} catch (ServerException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			int result = 0;
			if (channel != null) {
				result = channel.onMessage(senderId, message);
			}

			return Response.status(Status.OK).entity(result).build();

		} catch (IOException e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

}
