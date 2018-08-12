package org.orbit.infra.runtime.indexes.ws;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.orbit.infra.model.indexes.IndexItemCommandRequestDTO;
import org.orbit.infra.runtime.indexes.service.IndexService;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.json.JSONUtil;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;
import org.origin.common.util.CommandRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * IndexService resource
 * 
 * {contextRoot} example:
 * /orbit/v1/indexservice/
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/commandrequest (Body parameter: IndexItemCommandRequestDTO)
 * 
 */
@Secured(roles = { OrbitRoles.SYSTEM_COMPONENT, OrbitRoles.SYSTEM_ADMIN, OrbitRoles.INDEX_ADMIN })
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IndexServiceWSResource extends AbstractWSApplicationResource {

	protected static Logger LOG = LoggerFactory.getLogger(IndexServiceWSResource.class);

	@Inject
	public IndexService service;

	protected IndexService getService() throws RuntimeException {
		if (this.service == null) {
			throw new RuntimeException("IndexService is not available.");
		}
		return this.service;
	}

	/**
	 * Post an command request to the index service.
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/commandrequest (Body parameter: IndexItemCommandRequestDTO)
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 */
	@Path("commandrequest")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response onCommandRequest(IndexItemCommandRequestDTO commandRequestDTOs) {
		if (commandRequestDTOs == null) {
			ErrorDTO nullBody = new ErrorDTO("Body parameter (IndexItemCommandRequestDTO) is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		String command = commandRequestDTOs.getCommand();
		Map<String, Object> parameters = commandRequestDTOs.getParameters();
		String parametersString = JSONUtil.toJsonString(parameters);

		LOG.info("onCommandRequest()");
		LOG.info("\tcommand=" + command);
		LOG.info("\tparameters=" + parametersString);

		boolean succeed = false;
		IndexService indexService = getService();
		if (indexService instanceof CommandRequestHandler && ((CommandRequestHandler) indexService).isCommandSupported(command, parameters)) {
			succeed = ((CommandRequestHandler) indexService).performCommand(command, parameters);
		}

		if (succeed) {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Command '" + command + "' is executed successfully.");
			return Response.ok().entity(statusDTO).build();
		} else {
			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Command '" + command + "' is not executed.");
			return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
		}
	}

}

/// **
// * Ping the index service.
// *
// * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
// *
// * @return
// */
// @Path("ping")
// @GET
// @Produces(MediaType.APPLICATION_JSON)
// public Response ping() {
// IndexService indexService = getService(IndexService.class);
// if (indexService == null) {
// return Response.status(Status.SERVICE_UNAVAILABLE).entity(String.valueOf(0)).build();
// }
// return Response.ok().entity(String.valueOf(1)).build();
// }
