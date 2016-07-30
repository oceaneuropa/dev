package org.nb.mgm.ws;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.nb.mgm.exception.ManagementException;
import org.nb.mgm.model.runtime.Software;
import org.nb.mgm.service.ManagementService;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

/*
 * Project Software content resource.
 * 
 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content
 * URL (PST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content (FormDataParam: InputStream, FormDataContentDisposition)
 * 
 */
@Path("/projects/{projectId}/software/{softwareId}/content")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectSoftwareContentResource extends AbstractApplicationResource {

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	public Response getFileContent(@PathParam("projectId") String projectId, @PathParam("softwareId") String softwareId) {
		// Validate parameters
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (softwareId == null || softwareId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("softwareId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		byte[] bytes = null;
		String fileName = null;

		InputStream input = null;
		try {
			// Get Software
			Software software = mgm.getProjectSoftware(projectId, softwareId);
			if (software == null) {
				ErrorDTO softwareNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Software cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(softwareNotFoundError).build();
			}

			// Get file name
			String name = software.getName();
			String version = software.getVersion();
			String fileExtension = null;
			String localPath = software.getLocalPath();
			if (localPath != null) {
				fileExtension = FilenameUtils.getExtension(localPath);
			}
			fileName = name + "_" + version + "." + fileExtension;

			// Get file content
			input = mgm.getProjectSoftwareContent(projectId, softwareId);
			if (input != null) {
				bytes = IOUtil.toByteArray(input);
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} catch (IOException e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		if (bytes == null) {
			bytes = new byte[0];
		}
		return Response.ok(bytes, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fileName).build();
	}

	/**
	 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/projects/{projectId}/software/{softwareId}/content
	 * 
	 * (FormDataParam: InputStream, FormDataContentDisposition)
	 * 
	 * @param projectId
	 * @param softwareId
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile( //
			@PathParam("projectId") String projectId, //
			@PathParam("softwareId") String softwareId, //
			@FormDataParam("file") InputStream uploadedInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		if (projectId == null || projectId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("projectId is empty.")).build();
		}
		if (softwareId == null || softwareId.isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("softwareId is empty.")).build();
		}

		ManagementService mgm = getService(ManagementService.class);

		Software software = null;
		try {
			software = mgm.getProjectSoftware(projectId, softwareId);
			if (software == null) {
				ErrorDTO softwareNotFoundError = new ErrorDTO(String.valueOf(Status.NOT_FOUND.getStatusCode()), "Software cannot be found.");
				return Response.status(Status.NOT_FOUND).entity(softwareNotFoundError).build();
			}

			String fileName = fileDetail.getFileName();
			long length = fileDetail.getSize();
			Date lastModified = fileDetail.getModificationDate();

			boolean succeed = mgm.setProjectSoftwareContent(projectId, softwareId, fileName, length, lastModified, uploadedInputStream);
			if (succeed) {
				return Response.ok().entity(StatusDTO.status("200", "success", "File is uplaoded.")).build();
			} else {
				return Response.ok().entity(StatusDTO.status("201", "failed", "File is not uplaoded.")).build();
			}

		} catch (ManagementException e) {
			ErrorDTO error = handleError(e, e.getCode(), true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(uploadedInputStream, true);
		}
	}

}
