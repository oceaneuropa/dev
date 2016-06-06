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
import org.origin.mgm.model.dto.IndexItemActionDTO;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.IndexServiceActionAware;

@javax.ws.rs.Path("/indexitems/action")
@Produces(MediaType.APPLICATION_JSON)
public class IndexItemsActionResource extends AbstractApplicationResource {

	protected boolean debug = true;

	/**
	 * Post an action to the index service.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/indexitems/action
	 * 
	 * @param action
	 * @param parameters
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAction(IndexItemActionDTO actionDTOs) {
		if (actionDTOs == null) {
			ErrorDTO nullBody = new ErrorDTO("Body parameter (IndexItemActionDTO) is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullBody).build();
		}

		String action = actionDTOs.getAction();
		Map<String, Object> parameters = actionDTOs.getParameters();
		String parametersString = JSONUtil.toJsonString(parameters);

		if (debug) {
			System.out.println("IndexItemsActionResource.postAction()");
			System.out.println("\taction=" + action);
			System.out.println("\tparameters=" + parametersString);
		}

		boolean succeed = false;
		IndexService indexService = getService(IndexService.class);
		if (indexService instanceof IndexServiceActionAware) {
			succeed = ((IndexServiceActionAware) indexService).onAction(action, parameters);
		}

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", "Action '" + action + "' is executed successfully.");
		} else {
			statusDTO = new StatusDTO("200", "fail", "Action '" + action + "' is not executed.");
		}
		return Response.ok().entity(statusDTO).build();
	}

}
