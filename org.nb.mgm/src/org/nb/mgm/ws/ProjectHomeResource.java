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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * ProjectHome resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes (Body parameter: ProjectHomeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}
 * 
 */
@Path("/projects/{projectId}/homes")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectHomeResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

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
				Home remoteHome = projectHome.getRemoteHome();
				if (remoteHome != null) {
					HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
					projectHomeDTO.setRemoteHome(remoteHomeDTO);
				}

				projectHomeDTOs.add(projectHomeDTO);
			}

		} catch (MgmException e) {
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
			Home remoteHome = projectHome.getRemoteHome();
			if (remoteHome != null) {
				HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
				projectHomeDTO.setRemoteHome(remoteHomeDTO);
			}

		} catch (MgmException e) {
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
			Home remoteHome = newProjectHome.getRemoteHome();
			if (remoteHome != null) {
				HomeDTO remoteHomeDTO = DTOConverter.getInstance().toDTO(remoteHome);
				newProjectHomeDTO.setRemoteHome(remoteHomeDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

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
			String id = projectHomeDTO.getId();
			String name = projectHomeDTO.getName();
			String description = projectHomeDTO.getDescription();

			updateProjectHomeRequest.setId(id);
			updateProjectHomeRequest.setName(name);
			updateProjectHomeRequest.setDescription(description);

			// Update ProjectHome
			mgm.updateProjectHome(projectId, updateProjectHomeRequest);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectHome is updated successfully.");
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

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectHome is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
