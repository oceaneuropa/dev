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
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Project resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects (Body parameter: ProjectDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
 * 
 */
@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get Projects
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjects() {
		List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// Get Projects
			List<Project> projects = mgm.getProjects();
			for (Project project : projects) {
				ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

				List<ProjectHomeDTO> homeConfigDTOs = new ArrayList<ProjectHomeDTO>();
				for (ProjectHome homeConfig : mgm.getProjectHomes(project.getId())) {
					ProjectHomeDTO homeConfigDTO = DTOConverter.getInstance().toDTO(homeConfig);
					homeConfigDTOs.add(homeConfigDTO);
				}
				projectDTO.setHomeConfigs(homeConfigDTOs);

				projectDTOs.add(projectDTO);
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(projectDTOs).build();
	}

	/**
	 * Get Project by Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
	 * 
	 * @param projectId
	 * @return
	 */
	@GET
	@Path("{projectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProject(@PathParam("projectId") String projectId) {
		ProjectDTO projectDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			projectDTO = DTOConverter.getInstance().toDTO(project);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(projectDTO).build();
	}

	/**
	 * Add a Project to the cluster.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects
	 * 
	 * Body parameter: ProjectDTO
	 * 
	 * @param projectDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProject(ProjectDTO projectDTO) {
		if (projectDTO == null) {
			ErrorDTO nullProjectDTOError = new ErrorDTO("projectDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		ProjectDTO newProjectDTO = null;
		try {
			String id = projectDTO.getId();
			String name = projectDTO.getName();
			String description = projectDTO.getDescription();

			Project newProjectRequest = new Project(mgm.getRoot());
			newProjectRequest.setId(id);
			newProjectRequest.setName(name);
			newProjectRequest.setDescription(description);

			Project newProject = mgm.addProject(newProjectRequest);
			if (newProject == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "New Project cannot be created.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			newProjectDTO = DTOConverter.getInstance().toDTO(newProject);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		return Response.ok().entity(newProjectDTO).build();
	}

	/**
	 * Update Project information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects
	 * 
	 * Body parameter: ProjectDTO
	 * 
	 * @param projectDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProject(ProjectDTO projectDTO) {
		if (projectDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("projectDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			String id = projectDTO.getId();
			String name = projectDTO.getName();
			String description = projectDTO.getDescription();

			Project updateProjectRequest = new Project();
			updateProjectRequest.setId(id);
			updateProjectRequest.setName(name);
			updateProjectRequest.setDescription(description);

			mgm.updateProject(updateProjectRequest);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "Project is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a Project from the cluster.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}
	 * 
	 * @param projectId
	 * @return
	 */
	@DELETE
	@Path("/{projectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProject(@PathParam(value = "projectId") String projectId) {
		if (projectId == null || projectId.isEmpty()) {
			ErrorDTO nullProjectIdError = new ErrorDTO("projectId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectIdError).build();
		}

		boolean succeed = false;
		ManagementService mgm = getService(ManagementService.class);
		try {
			succeed = mgm.deleteProject(projectId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = null;
		if (succeed) {
			statusDTO = new StatusDTO("200", "success", "Project is deleted successfully.");
		} else {
			statusDTO = new StatusDTO("200", "failed", "Project is not deleted successfully.");
		}
		return Response.ok().entity(statusDTO).build();
	}

}
