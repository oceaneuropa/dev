package org.nb.home.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nb.home.model.dto.DTOConverter;
import org.nb.home.model.dto.WorkspaceDTO;
import org.nb.home.model.exception.HomeException;
import org.nb.home.model.runtime.Workspace;
import org.nb.home.service.HomeAgentService;
import org.origin.common.rest.agent.AbstractAgentResource;
import org.origin.common.rest.model.ErrorDTO;

/*
 * Home workspaces resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/workspaces?managementId={managementId}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/workspaces/{name}?managementId={managementId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/workspaces (Body parameter: WorkspaceDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/workspaces/{name}?managementId={managementId}
 * 
 */
@Path("/workspaces")
@Produces(MediaType.APPLICATION_JSON)
public class HomeWorkspacesResource extends AbstractAgentResource {

	/**
	 * Get workspaces.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/workspaces?managementId={managementId}
	 * 
	 * @param managementId
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWorkspaces(@QueryParam("managementId") String managementId) {
		List<WorkspaceDTO> workspaceDTOs = new ArrayList<WorkspaceDTO>();

		HomeAgentService agentService = getService(HomeAgentService.class);
		try {
			List<Workspace> workspaces = agentService.getWorkspaces(managementId);

			for (Workspace workspace : workspaces) {
				WorkspaceDTO workspaceDTO = DTOConverter.getInstance().toDTO(workspace);
				if (workspaceDTO != null) {
					// List<HomeDTO> homeDTOs = new ArrayList<HomeDTO>();
					// for (Home home : agentService.getHomes(workspace.getId())) {
					// HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);
					// // homeDTO.setMachine(machineDTO);
					// homeDTOs.add(homeDTO);
					// }
					// machineDTO.setHomes(homeDTOs);

					workspaceDTOs.add(workspaceDTO);
				}
			}

		} catch (HomeException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(workspaceDTOs).build();
	}

	/**
	 * Get workspace.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/workspaces/{name}?managementId={managementId}
	 * 
	 * @param name
	 * @param managementId
	 * @return
	 */
	@GET
	@Path("{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHome(@PathParam("name") String name, @QueryParam("managementId") String managementId) {
		WorkspaceDTO workspaceDTO = null;

		HomeAgentService agentService = getService(HomeAgentService.class);
		try {
			Workspace workspace = agentService.getWorkspace(managementId, name);
			if (workspace == null) {
				ErrorDTO workspaceNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Workspace cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(workspaceNotFoundError).build();
			}

			workspaceDTO = DTOConverter.getInstance().toDTO(workspace);

		} catch (HomeException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(workspaceDTO).build();
	}

}
