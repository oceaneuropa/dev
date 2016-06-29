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
import org.nb.mgm.model.dto.ProjectHomeConfigDTO;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHomeConfig;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;
import org.origin.common.util.Util;

/*
 * ProjectHomeConfig resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
 * 
 */
@Path("/{projectId}/homeConfigs")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectHomeConfigResource extends AbstractApplicationResource {

	protected void handleSave(ManagementService mgm) {
		if (!mgm.isAutoSave()) {
			mgm.save();
		}
	}

	/**
	 * Get ProjectHomeConfigs in a Project.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs
	 * 
	 * @param projectId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHomeConfigs(@PathParam("projectId") String projectId) {
		List<ProjectHomeConfigDTO> homeDTOs = new ArrayList<ProjectHomeConfigDTO>();

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find Project by projectId
			ProjectDTO projectDTO = null;
			Project project = mgm.getProject(projectId);
			if (project != null) {
				projectDTO = DTOConverter.getInstance().toDTO(project);
			}

			// 2. Get ProjectHomeConfigs in the Project.
			List<ProjectHomeConfig> homeConfigs = mgm.getProjectHomeConfigs(projectId);
			for (ProjectHomeConfig homeConfig : homeConfigs) {
				ProjectHomeConfigDTO homeConfigDTO = DTOConverter.getInstance().toDTO(homeConfig);

				// 3. Set ProjectDTO
				homeConfigDTO.setProject(projectDTO);

				// 4. Set HomeDTO
				Home home = homeConfig.getHome();
				if (home != null) {
					HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);
					homeConfigDTO.setHome(homeDTO);
				}

				homeDTOs.add(homeConfigDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
		return Response.ok().entity(homeDTOs).build();
	}

	/**
	 * Get ProjectHomeConfig by Id.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
	 * 
	 * @param homeConfigId
	 * @return
	 */
	@GET
	@Path("{homeConfigId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHomeConfig(@PathParam("projectId") String projectId, @PathParam("homeConfigId") String homeConfigId) {
		ProjectHomeConfigDTO homeConfigDTO = null;

		ManagementService mgm = getService(ManagementService.class);
		try {
			// 1. Find ProjectHomeConfig by Id and convert to DTO
			ProjectHomeConfig homeConfig = mgm.getProjectHomeConfig(homeConfigId);
			if (homeConfig == null) {
				ErrorDTO homeNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "ProjectHomeConfig cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(homeNotFoundError).build();
			}
			if (projectId != null && (homeConfig.getProject() == null || !projectId.equals(homeConfig.getProject().getId()))) {
				ErrorDTO projectIdNotMatchError = new ErrorDTO(String.valueOf(Status.INTERNAL_SERVER_ERROR.getStatusCode()), "The projectId that the ProjectHomeConfig belongs to does not match the given projectId.");
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(projectIdNotMatchError).build();
			}
			homeConfigDTO = DTOConverter.getInstance().toDTO(homeConfig);

			// 2. Set ProjectDTO
			Project project = mgm.getProject(projectId);
			if (project != null) {
				ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);
				homeConfigDTO.setProject(projectDTO);
			}

			// 3. Set HomeDTO
			Home home = homeConfig.getHome();
			if (home != null) {
				HomeDTO homeDTO = DTOConverter.getInstance().toDTO(home);
				homeConfigDTO.setHome(homeDTO);
			}

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(homeConfigDTO).build();
	}

	/**
	 * Add a ProjectHomeConfig to a Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
	 * 
	 * @param projectId
	 * @param homeConfigDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createHomeConfig(@PathParam("projectId") String projectId, ProjectHomeConfigDTO homeConfigDTO) {
		// 1. Validate parameters.
		if (homeConfigDTO == null) {
			ErrorDTO nullProjectHomeConfigDTOError = new ErrorDTO("homeConfigDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullProjectHomeConfigDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		// 3. Create Home runtime model with parameters.
		ProjectHomeConfig newHomeConfigRequest = new ProjectHomeConfig();

		String id = homeConfigDTO.getId();
		String name = homeConfigDTO.getName();
		String description = homeConfigDTO.getDescription();

		newHomeConfigRequest.setId(id);
		newHomeConfigRequest.setName(name);
		newHomeConfigRequest.setDescription(description);

		// 4. Add ProjectHomeConfig to Project.
		try {
			mgm.addProjectHomeConfig(projectId, newHomeConfigRequest);

			if (Util.compare(id, newHomeConfigRequest.getId()) != 0) {
				homeConfigDTO.setId(newHomeConfigRequest.getId());
			}
			if (Util.compare(name, newHomeConfigRequest.getName()) != 0) {
				homeConfigDTO.setName(newHomeConfigRequest.getName());
			}
		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		return Response.ok().entity(homeConfigDTO).build();
	}

	/**
	 * Update ProjectHomeConfig information.
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs (Body parameter: ProjectHomeConfigDTO)
	 * 
	 * @param homeConfigDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateHomeConfig(ProjectHomeConfigDTO homeConfigDTO) {
		// 1. Validate parameters.
		if (homeConfigDTO == null) {
			ErrorDTO nullDTOError = new ErrorDTO("homeConfigDTO is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullDTOError).build();
		}

		// 2. Always get management service first.
		ManagementService mgm = getService(ManagementService.class);

		try {
			// 3. Create ProjectHomeConfig runtime model with parameters.
			ProjectHomeConfig updateHomeConfigRequest = new ProjectHomeConfig();

			String id = homeConfigDTO.getId();
			String name = homeConfigDTO.getName();
			String description = homeConfigDTO.getDescription();

			updateHomeConfigRequest.setId(id);
			updateHomeConfigRequest.setName(name);
			updateHomeConfigRequest.setDescription(description);

			// 4. Update ProjectHomeConfig.
			mgm.updateProjectHomeConfig(updateHomeConfigRequest);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectHomeConfig is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Delete a ProjectHomeConfig from a Project.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/{projectId}/homeConfigs/{homeConfigId}
	 * 
	 * @param projectId
	 * @param homeConfigId
	 * @return
	 */
	@DELETE
	@Path("/{homeConfigId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteHomeConfig(@PathParam(value = "projectId") String projectId, @PathParam(value = "homeConfigId") String homeConfigId) {
		if (homeConfigId == null || homeConfigId.isEmpty()) {
			ErrorDTO nullHomeConfigIdError = new ErrorDTO("homeConfigId is null.");
			return Response.status(Status.BAD_REQUEST).entity(nullHomeConfigIdError).build();
		}

		ManagementService mgm = getService(ManagementService.class);
		try {
			mgm.deleteProjectHomeConfig(homeConfigId);

		} catch (MgmException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		handleSave(mgm);

		StatusDTO statusDTO = new StatusDTO("200", "success", "ProjectHomeConfig is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
