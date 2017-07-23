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
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.nb.mgm.model.dto.SoftwareDTO;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractWSApplicationResource;

/*
 * ProjectNode resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes (Body parameter: ProjectNodeDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/hasAttribute?attribute={attributeName}
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/attribute?attribute={attributeName}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/action (Body parameter: Action)
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/software
 * 
 */
@Path("/projects/{projectId}/homes/{projectHomeId}/nodes")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectNodeResource extends AbstractWSApplicationResource {

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
		} catch (ManagementException e) {
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

		} catch (ManagementException e) {
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

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

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

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ProjectNode is updated successfully.");
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

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "ProjectNode is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Check whether ProjectNode has specified attribute.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/hasAttribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param attributeName
	 * @return
	 */
	@GET
	@Path("{projectNodeId}/hasAttribute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response hasAttribute(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId, @QueryParam("attribute") String attributeName) {
		// Validate parameters.
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
		if (attributeName == null || attributeName.isEmpty()) {
			ErrorDTO nullAttributeError = new ErrorDTO("attribute name is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullAttributeError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find ProjectNode.
			ProjectNode projectNode = mgm.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}

			// Check has attribute
			Boolean hasAttribute = projectNode.hasAttribute(attributeName);
			return Response.ok().entity(hasAttribute).build();

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

	/**
	 * Get ProjectNode attribute.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/attribute?attribute={attributeName}
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param attributeName
	 * @return
	 */
	@GET
	@Path("{projectNodeId}/attribute")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttribute(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId, @QueryParam("attribute") String attributeName) {
		// Validate parameters.
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
		if (attributeName == null || attributeName.isEmpty()) {
			ErrorDTO nullAttributeError = new ErrorDTO("attribute name is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullAttributeError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find ProjectNode.
			ProjectNode projectNode = mgm.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}

			// Get attribute
			Object attrValue = projectNode.getAttribute(attributeName);
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
	 * On ProjectNode Action.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/action (Body parameter: Action)
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param action
	 * @return
	 */
	@POST
	@Path("{projectNodeId}/action")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response onAction(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId, Action action) {
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
		if (action == null) {
			ErrorDTO nullActionError = new ErrorDTO("action is empty.");
			return Response.status(Status.BAD_REQUEST).entity(nullActionError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Find ProjectNode.
			ProjectNode projectNode = mgm.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}

			String actionName = action.getName();

			boolean isActionSupported = false;
			boolean succeed = false;
			if ("install_project_node_software".equals(actionName)) {
				isActionSupported = true;
				String softwareId = (String) action.getParameter("softwareId");
				if (softwareId == null || softwareId.isEmpty()) {
					ErrorDTO softwareIdNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "softwareId parameter cannot be found.");
					return Response.status(Status.NOT_FOUND).entity(softwareIdNotFoundError).build();
				}

				succeed = mgm.installProjectNodeSoftware(projectId, projectHomeId, projectNodeId, softwareId);

			} else if ("uninstall_project_node_software".equals(actionName)) {
				isActionSupported = true;
				String softwareId = (String) action.getParameter("softwareId");
				if (softwareId == null || softwareId.isEmpty()) {
					ErrorDTO softwareIdNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "softwareId parameter cannot be found.");
					return Response.status(Status.NOT_FOUND).entity(softwareIdNotFoundError).build();
				}

				succeed = mgm.uninstallProjectNodeSoftware(projectId, projectHomeId, projectNodeId, softwareId);
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

	/**
	 * Get a list of Project Software.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/homes/{projectHomeId}/nodes/{projectNodeId}/software
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 */
	@GET
	@Path("{projectNodeId}/software")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectSoftware(@PathParam("projectId") String projectId, @PathParam("projectHomeId") String projectHomeId, @PathParam("projectNodeId") String projectNodeId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		List<SoftwareDTO> softwareDTOs = new ArrayList<SoftwareDTO>();
		try {
			// Find ProjectNode.
			ProjectNode projectNode = mgm.getProjectNode(projectId, projectHomeId, projectNodeId);
			if (projectNode == null) {
				ErrorDTO projectNodeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectNode cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNodeNotFoundError).build();
			}

			// Get Project. Create ProjectDTO.
			Project project = projectNode.getProject();
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Get list of Software
			List<Software> softwareList = projectNode.getInstalledSoftware();
			for (Software software : softwareList) {
				// Ignore Software that cannot be found from Project
				if (software.isProxy()) {
					continue;
				}
				// Create SoftwareDTO
				SoftwareDTO softwareDTO = DTOConverter.getInstance().toDTO(software);

				// Set container ProjectDTO
				softwareDTO.setProject(projectDTO);

				softwareDTOs.add(softwareDTO);
			}
		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(softwareDTOs).build();
	}

}
