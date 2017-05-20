package org.origin.mgm.ws;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.origin.common.json.JSONUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.common.util.CommandRequestHandler;
import org.origin.mgm.model.dto.IndexItemCommandRequestDTO;
import org.origin.mgm.service.IndexService;

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
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IndexServiceResource extends AbstractApplicationResource {

	protected boolean debug = true;

	/**
	 * Ping the index service.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/ping
	 * 
	 * @return
	 */
	@Path("ping")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ping() {
		IndexService indexService = getService(IndexService.class);
		if (indexService == null) {
			return Response.status(Status.SERVICE_UNAVAILABLE).entity(String.valueOf(0)).build();
		}
		return Response.ok().entity(String.valueOf(1)).build();
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

		if (debug) {
			System.out.println("IndexServiceResource.onCommandRequest()");
			System.out.println("\tcommand=" + command);
			System.out.println("\tparameters=" + parametersString);
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
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