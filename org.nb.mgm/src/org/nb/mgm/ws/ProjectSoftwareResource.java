package org.nb.mgm.ws;

import java.util.ArrayList;
import java.util.Date;
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

import org.nb.mgm.model.dto.DTOConverter;
import org.nb.mgm.model.dto.ProjectDTO;
import org.nb.mgm.model.dto.SoftwareDTO;
import org.nb.mgm.model.exception.ManagementException;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.service.ManagementService;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Project Software resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
 * URL (DEL): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
 * 
 */
@Path("/projects/{projectId}/software")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectSoftwareResource extends AbstractApplicationResource {

	/**
	 * Get Project Software lists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software
	 * 
	 * @param projectId
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectSoftware(@PathParam("projectId") String projectId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		List<SoftwareDTO> softwareDTOs = new ArrayList<SoftwareDTO>();
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Get all Project Software
			List<Software> softwareList = mgm.getProjectSoftware(projectId);
			for (Software software : softwareList) {
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

	/**
	 * Get Project Software.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
	 * 
	 * @param softwareId
	 * @return
	 */
	@GET
	@Path("{softwareId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProjectSoftware(@PathParam("projectId") String projectId, @PathParam("softwareId") String softwareId) {
		// Validate parameters.
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (softwareId == null || softwareId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("softwareId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		SoftwareDTO softwareDTO = null;
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Find Software. Create SoftwareDTO.
			Software software = mgm.getProjectSoftware(projectId, softwareId);
			if (software == null) {
				ErrorDTO softwareNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Software cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(softwareNotFoundError).build();
			}
			softwareDTO = DTOConverter.getInstance().toDTO(software);

			// Set container ProjectDTO
			softwareDTO.setProject(projectDTO);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(softwareDTO).build();
	}

	/**
	 * Add Software to a Project.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
	 * 
	 * @param projectId
	 * @param newSoftwareRequestDTO
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addProjectSoftware(@PathParam("projectId") String projectId, SoftwareDTO newSoftwareRequestDTO) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (newSoftwareRequestDTO == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("softwareDTO is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		SoftwareDTO newSoftwareDTO = null;
		try {
			// Find Project. Create ProjectDTO.
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}
			ProjectDTO projectDTO = DTOConverter.getInstance().toDTO(project);

			// Create new Software request
			Software newSoftwareRequest = new Software();
			String id = newSoftwareRequestDTO.getId();
			String type = newSoftwareRequestDTO.getType();
			String name = newSoftwareRequestDTO.getName();
			String version = newSoftwareRequestDTO.getVersion();
			String description = newSoftwareRequestDTO.getDescription();
			long length = newSoftwareRequestDTO.getLength();
			Date lastModified = newSoftwareRequestDTO.getLastModified();
			String md5 = newSoftwareRequestDTO.getMd5();

			newSoftwareRequest.setId(id);
			newSoftwareRequest.setType(type);
			newSoftwareRequest.setName(name);
			newSoftwareRequest.setVersion(version);
			newSoftwareRequest.setDescription(description);
			newSoftwareRequest.setLength(length);
			newSoftwareRequest.setLastModified(lastModified);
			newSoftwareRequest.setMd5(md5);

			// Add Software to Project
			Software newSoftware = mgm.addProjectSoftware(projectId, newSoftwareRequest);
			if (newSoftware == null) {
				ErrorDTO softwareNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "New Software cannot be added.");
				return Response.status(Status.NOT_FOUND).entity(softwareNotFoundError).build();
			}
			newSoftwareDTO = DTOConverter.getInstance().toDTO(newSoftware);

			// Set container ProjectDTO
			newSoftwareDTO.setProject(projectDTO);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		return Response.ok().entity(newSoftwareDTO).build();
	}

	/**
	 * Update Project Software
	 * 
	 * URL (PUT): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software (Body parameter: SoftwareDTO)
	 * 
	 * @param updateSoftwareRequestDTO
	 * @return
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateProjectSoftware(@PathParam(value = "projectId") String projectId, SoftwareDTO updateSoftwareRequestDTO) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (updateSoftwareRequestDTO == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("updateSoftwareRequestDTO is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Create update Software request
			Software updateSoftwareRequest = new Software();
			String id = updateSoftwareRequestDTO.getId();
			String type = updateSoftwareRequestDTO.getType();
			String name = updateSoftwareRequestDTO.getName();
			String version = updateSoftwareRequestDTO.getVersion();
			String description = updateSoftwareRequestDTO.getDescription();
			long length = updateSoftwareRequestDTO.getLength();
			Date lastModified = updateSoftwareRequestDTO.getLastModified();
			String md5 = updateSoftwareRequestDTO.getMd5();

			updateSoftwareRequest.setId(id);
			updateSoftwareRequest.setType(type);
			updateSoftwareRequest.setName(name);
			updateSoftwareRequest.setVersion(version);
			updateSoftwareRequest.setDescription(description);
			updateSoftwareRequest.setLength(length);
			updateSoftwareRequest.setLastModified(lastModified);
			updateSoftwareRequest.setMd5(md5);

			// Update Software
			mgm.updateProjectSoftware(projectId, updateSoftwareRequest);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Project Software is updated successfully.");
		return Response.ok().entity(statusDTO).build();
	}

	/**
	 * Remove a Software from a Project.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 */
	@DELETE
	@Path("{softwareId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteProjectSoftware(@PathParam(value = "projectId") String projectId, @PathParam(value = "softwareId") String softwareId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (softwareId == null || softwareId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("softwareId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		try {
			// Find Project
			Project project = mgm.getProject(projectId);
			if (project == null) {
				ErrorDTO projectNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Project cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(projectNotFoundError).build();
			}

			// Delete Software from Project
			mgm.deleteProjectSoftware(projectId, softwareId);

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}

		StatusDTO statusDTO = new StatusDTO("200", "success", "Project Software is deleted successfully.");
		return Response.ok().entity(statusDTO).build();
	}

}
