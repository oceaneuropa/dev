package org.nb.mgm.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nb.mgm.model.dto.Action;
import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * ProjectHome resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/hasAttribute?attribute={attibuteName}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/attribute?attribute={attributeName}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/action (Body parameter: Action)
 */
@Path("/projects/{projectId}/homes")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectHomeResource extends AbstractWSApplicationResource {

	/**
	 * Get ProjectHomes.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes
	 * 
	 * @param projectId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectHomes(@PathParam("projectId") String projectId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		List<ProjectHomeDTO> projectHomeDTOs = new ArrayList<ProjectHomeDTO>();
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Get ProjectHomes
			List<ProjectHome> projectHomes = mgm.getProjectHomes(projectId);
			for (ProjectHome projectHome : projectHomes) {
				// Create ProjectHomeDTO
				ProjectHomeDTO projectHomeDTO = DTOConverter.getInstance().toDTO(projectHome);

				// Set container ProjectDTO
				projectHomeDTO.setProject(projectDTO);

				// Set remote HomeDTO (if configured)
				Home remoteHome = projectHome.getDeploymentHome();
				if (remoteHome != null) {
					HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
					projectHomeDTO.setRemoteHome(remoteHomeDTO);
				}

				projectHomeDTOs.add(projectHomeDTO);
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(projectHomeDTOs).build();
	}

	/**
	 * Get ProjectHome.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
	 * 
	 * @param projectHomeId
	 * @return
	 */
	@GET
	@Path("{projectHomeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectHome(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId) {
		// Validate parameters.
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		ProjectHomeDTO projectHomeDTO = null;
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Find ProjectHome. Create ProjectHomeDTO.
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}
			projectHomeDTO = DTOConverter.getInstance().toDTO(projectHome);

			// Set container ProjectDTO
			projectHomeDTO.setProject(projectDTO);

			// Set remote HomeDTO (if configured)
			Home remoteHome = projectHome.getDeploymentHome();
			if (remoteHome != null) {
				HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
				projectHomeDTO.setRemoteHome(remoteHomeDTO);
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(projectHomeDTO).build();
	}

	/**
	 * Add a ProjectHome.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
	 * 
	 * @param projectId
	 * @param newProjectHomeRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectHome(@PathParam("projectId") String projectId, ProjectHomeDTO newProjectHomeRequestDTO) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (newProjectHomeRequestDTO == null) {
			ErrorDTO nullProjectHomeDTOError = new ErrorDTO("newProjectHomeRequestDTO is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		ProjectHomeDTO newProjectHomeDTO = null;
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Create new ProjectHome request
			ProjectHome newProjectHomeRequest = new ProjectHome();
			String id = newProjectHomeRequestDTO.getId();
			String name = newProjectHomeRequestDTO.getName();
			String description = newProjectHomeRequestDTO.getDescription();

			newProjectHomeRequest.setId(id);
			newProjectHomeRequest.setName(name);
			newProjectHomeRequest.setDescription(description);

			// Add ProjectHome to Project
			ProjectHome newProjectHome = mgm.addProjectHome(projectId, newProjectHomeRequest);
			if (newProjectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "New ProjectHome cannot be created.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}
			newProjectHomeDTO = DTOConverter.getInstance().toDTO(newProjectHome);

			// Set container ProjectDTO
			newProjectHomeDTO.setProject(projectDTO);

			// Set remote HomeDTO (if configured)
			Home remoteHome = newProjectHome.getDeploymentHome();
			if (remoteHome != null) {
				HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
				newProjectHomeDTO.setRemoteHome(remoteHomeDTO);
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(newProjectHomeDTO).build();
	}

	/**
	 * Update ProjectHome.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
	 * 
	 * @param projectHomeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProjectHome(@PathParam(value = "projectId") String projectId, ProjectHomeDTO projectHomeDTO) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("projectHomeDTO is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Create update ProjectHome request
			ProjectHome updateProjectHomeRequest = new ProjectHome();
			String projectHomeId = projectHomeDTO.getId();
			String name = projectHomeDTO.getName();
			String description = projectHomeDTO.getDescription();

			updateProjectHomeRequest.setId(projectHomeId);
			updateProjectHomeRequest.setName(name);
			updateProjectHomeRequest.setDescription(description);

			// Update ProjectHome
			mgm.updateProjectHome(projectId, updateProjectHomeRequest);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ProjectHome is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a ProjectHome.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 */
	@DELETE
	@Path("{projectHomeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProjectHome(@PathParam(value = "projectId") String projectId, @PathParam(value = "projectHomeId") String projectHomeId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Delete ProjectHome from Project
			mgm.deleteProjectHome(projectId, projectHomeId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ProjectHome is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Check whether ProjectHome has specified attribute.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/hasAttribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param attributeName
	 * @return
	 */
	@GET
	@Path("{projectHomeId}/hasAttribute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response hasAttribute(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @QueryParam("attribute") String attributeName) {
		// Validate parameters.
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (attributeName == null || attributeName.isEmpty()) {
			ErrorDTO nullAttributeError = new ErrorDTO("attribute name is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullAttributeError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find ProjectHome.
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}

			// Check has attribute
			Boolean hasAttribute = projectHome.hasAttribute(attributeName);
			return Response.ok().entity(hasAttribute).build();

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * Get ProjectHome attribute.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/attribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param attributeName
	 * @return
	 */
	@GET
	@Path("{projectHomeId}/attribute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttribute(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @QueryParam("attribute") String attributeName) {
		// Validate parameters.
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (attributeName == null || attributeName.isEmpty()) {
			ErrorDTO nullAttributeError = new ErrorDTO("attribute name is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullAttributeError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find ProjectHome.
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}

			// Get attribute
			Object attrValue = projectHome.getAttribute(attributeName);
			if (attrValue != null) {
				return Response.ok().entity(attrValue).build();
			}
			// no attribute value is retrieved --- return empty value
			return Response.ok().build();

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * On ProjectHome Action.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/action (Body: Action)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param action
	 * @return
	 */
	@POST
	@Path("{projectHomeId}/action")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response onAction(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, Action action) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (action == null) {
			ErrorDTO nullActionError = new ErrorDTO("action is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullActionError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Find ProjectHome.
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}

			String actionName = action.getName();

			boolean isActionSupported = false;
			boolean succeed = false;
			if ("set_project_deployment_home".equals(actionName)) {
				isActionSupported = true;
				String homeId = (String) action.getParameter("homeId");
				if (homeId == null || homeId.isEmpty()) {
					ErrorDTO homeIdNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "homeId parameter cannot be found.");
					return Response.status(Status.NOT_FOUND).entity(homeIdNotFoundError).build();
				}

				succeed = mgm.setProjectDeploymentHome(projectId, projectHomeId, homeId);

			} else if ("remove_project_deployment_home".equals(actionName)) {
				isActionSupported = true;
				String homeId = (String) action.getParameter("homeId");
				if (homeId == null || homeId.isEmpty()) {
					ErrorDTO homeIdNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "homeId parameter cannot be found.");
					return Response.status(Status.NOT_FOUND).entity(homeIdNotFoundError).build();
				}

				succeed = mgm.removeProjectDeploymentHome(projectId, projectHomeId, homeId);
			}

			if (!isActionSupported) {
				StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_400, StatusDTO.FAILED, "Action is not supported.");
				return Response.status(Status.BAD_REQUEST).entity(statusDTO).build();
			}
			if (!succeed) {
				StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_304, StatusDTO.FAILED, "Failed to process the Action.");
				return Response.status(Status.NOT_MODIFIED).entity(statusDTO).build();
			}

			StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "Action is processed successfully.");
			return Response.ok().entity(statusDTO).build();

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

}
