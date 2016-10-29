package org.origin.mgm.ws;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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

/**
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/commandRequest (Body parameter: IndexItemCommandRequestDTO)
 * 
 */
@javax.ws.rs.Path("/indexitems/commandRequest")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsCommandRequestResource extends AbstractApplicationResource {

	protected boolean debug = true;

	/**
	 * Post an command request to the index service.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/commandRequest
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response commandRequest(IndexItemCommandRequestDTO commandRequestDTOs) {
		if (commandRequestDTOs == null) {
			ErrorDTO nullBody = new ErrorDTO("Body parameter (IndexItemCommandRequestDTO) is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		String command = commandRequestDTOs.getCommand();
		Map<String, Object> parameters = commandRequestDTOs.getParameters();
		String parametersString = JSONUtil.toJsonString(parameters);

		if (debug) {
			System.out.println("IndexItemsActionResource.commandRequest()");
			System.out.println("\tcommand=" + command);
			System.out.println("\tparameters=" + parametersString);
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		if (indexService instanceof CommandRequestHandler && ((CommandRequestHandler) indexService).isCommandSupported(command, parameters)) {
			succeed = ((CommandRequestHandler) indexService).performCommand(command, parameters);
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", "Command '" + command + "' is executed successfully.");
		} else {
			statusDTO = new StatusDTO("200", "fail", "Command '" + command + "' is not executed.");
		}
		return Response.ok().entity(statusDTO).build();
	}

}
