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
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * ProjectNode resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * 
 */
@Path("/projects/{projectId}/homes/{projectHomeId}/nodes")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectNodeResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get ProjectNodes.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectNodes(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		List<ProjectNodeDTO> projectNodesDTOs = new ArrayList<ProjectNodeDTO>();
		try {
			// Find Project. Create ProjectDTO
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Find ProjectHome. Create ProjectHomeDTO
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}
			ProjectHomeDTO projectHomeDTO = DTOConverter.getInstance().toDTO(projectHome);

			// Get ProjectNodes
			List<ProjectNode> projectNodes = mgm.getProjectNodes(projectId, projectHomeId);
			for (ProjectNode projectNode : projectNodes) {
				// Create ProjectNodeDTO
				ProjectNodeDTO projectNodeDTO = DTOConverter.getInstance().toDTO(projectNode);

				// Set container ProjectDTO
				projectNodeDTO.setProject(projectDTO);

				// Set container ProjectHomeDTO
				projectNodeDTO.setProjectHome(projectHomeDTO);

				projectNodesDTOs.add(projectNodeDTO);
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(projectNodesDTOs).build();
	}

	/**
	 * Get ProjectNode.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 */
	@GET
	@Path("{projectNodeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectNode(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (projectNodeId == null || projectNodeId.isEmpty()) {
			ErrorDTO nullProjectNodeIdError = new ErrorDTO("projectNodeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectNodeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		ProjectNodeDTO projectNodeDTO = null;
		try {
			// Find Project. Create ProjectDTO
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Find ProjectHome. Create ProjectHomeDTO
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}
			ProjectHomeDTO projectHomeDTO = DTOConverter.getInstance().toDTO(projectHome);

			// Find ProjectNode. Create ProjectNodeDTO.
			ProjectNode projectNode = mgm.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}
			projectNodeDTO = DTOConverter.getInstance().toDTO(projectNode);

			// Set container ProjectDTO
			projectNodeDTO.setProject(projectDTO);

			// Set container ProjectHomeDTO
			projectNodeDTO.setProjectHome(projectHomeDTO);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(projectNodeDTO).build();
	}

	/**
	 * Add a ProjectNode
	 * 
	 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param newProjectNodeRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectNode(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, ProjectNodeDTO newProjectNodeRequestDTO) {
		// Validate parameters
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (newProjectNodeRequestDTO == null) {
			ErrorDTO nullProjectHomeDTOError = new ErrorDTO("projectNodeDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		ProjectNodeDTO newProjectNodeDTO = null;
		try {
			// Find Project. Create ProjectDTO
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Find ProjectHome. Create ProjectHomeDTO
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}
			ProjectHomeDTO projectHomeDTO = DTOConverter.getInstance().toDTO(projectHome);

			// Create new ProjectNode request
			ProjectNode newProjectNodeRequest = new ProjectNode();
			String id = newProjectNodeRequestDTO.getId();
			String name = newProjectNodeRequestDTO.getName();
			String description = newProjectNodeRequestDTO.getDescription();

			newProjectNodeRequest.setId(id);
			newProjectNodeRequest.setName(name);
			newProjectNodeRequest.setDescription(description);

			// Add ProjectNode to ProjectHome
			ProjectNode newProjectNode = mgm.addProjectNode(projectId, projectHomeId, newProjectNodeRequest);
			if (newProjectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "New ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}
			newProjectNodeDTO = DTOConverter.getInstance().toDTO(newProjectNode);

			// Set container ProjectDTO
			newProjectNodeDTO.setProject(projectDTO);

			// Set container ProjectHomeDTO
			newProjectNodeDTO.setProjectHome(projectHomeDTO);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

		return Response.ok().entity(newProjectNodeDTO).build();
	}

	/**
	 * Update ProjectNode.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProjectNode(@PathParam(value = "projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, ProjectNodeDTO projectNodeDTO) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (projectNodeDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("projectNodeDTO is empty.");
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

			// Find ProjectHome
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}

			// Create update ProjectNode request
			ProjectNode updateProjectNodeRequest = new ProjectNode();
			String id = projectNodeDTO.getId();
			String name = projectNodeDTO.getName();
			String description = projectNodeDTO.getDescription();

			updateProjectNodeRequest.setId(id);
			updateProjectNodeRequest.setName(name);
			updateProjectNodeRequest.setDescription(description);

			// Update ProjectNode
			mgm.updateProjectNode(projectId, projectHomeId, updateProjectNodeRequest);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectNode is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a ProjectNode.
	 * 
	 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 */
	@DELETE
	@Path("{projectNodeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProjectHome(@PathParam(value = "projectId") String projectId, @PathParam(value = "projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}
		if (projectHomeId == null || projectHomeId.isEmpty()) {
			ErrorDTO nullProjectHomeIdError = new ErrorDTO("projectHomeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeIdError).build();
		}
		if (projectNodeId == null || projectNodeId.isEmpty()) {
			ErrorDTO nullProjectNodeIdError = new ErrorDTO("projectNodeId is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectNodeIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Find ProjectHome
			ProjectHome projectHome = mgm.getProjectHome(projectId, projectHomeId);
			if (projectHome == null) {
				ErrorDTO projectHomeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHome cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectHomeNotFoundError).build();
			}

			// Delete ProjectNode
			mgm.deleteProjectNode(projectId, projectHomeId, projectNodeId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		// Save changes
		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectNode is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
