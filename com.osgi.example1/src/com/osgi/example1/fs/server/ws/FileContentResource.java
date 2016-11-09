package com.osgi.example1.fs.server.ws;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.origin.common.io.IOUtil;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.ModelConverter;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;

@javax.ws.rs.Path("/content")
@Produces(MediaType.APPLICATION_JSON)
public class FileContentResource extends AbstractApplicationResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
	public Response getFileContent(@QueryParam(value = "path") String pathString) {
		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("File path is null.")).build();
		}
		Path path = new Path(pathString);
		FileSystem fs = getService(FileSystem.class);

		boolean exists = fs.exists(path);
		if (!exists) {
			return Response.status(Status.NOT_FOUND).entity(new ErrorDTO("Path '" + path.getPathString() + "' does not exist.")).build();
		}
		boolean isDirectory = fs.isDirectory(path);
		if (isDirectory) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("Path '" + path.getPathString() + "' exists but is a directory.")).build();
		}

		String fileName = path.getLastSegment();

		byte[] bytes = null;
		InputStream input = null;
		try {
			input = fs.getInputStream(path);
			if (input != null) {
				bytes = IOUtil.toByteArray(input);
			}
		} catch (IOException e) {
			ErrorDTO error = handleError(e, StatusDTO.RESP_500, true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		if (bytes == null) {
			bytes = new byte[0];
		}

		return Response.ok(bytes, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + fileName).build();
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile( //
			@QueryParam(value = "path") String pathString, //
			@FormDataParam("file") InputStream uploadedInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("File path is null.")).build();
		}
		Path path = new Path(pathString);
		FileSystem fs = getService(FileSystem.class);

		try {
			if (fs.exists(path) && fs.isDirectory(path)) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorDTO("Path '" + path.getPathString() + "' exists but is a directory.")).build();
			}

			fs.copyInputStreamToFsFile(uploadedInputStream, path);

			return Response.ok().entity(new StatusDTO(StatusDTO.RESP_200, StatusDTO.SUCCESS, "File is uplaoded to path '" + pathString + "'.")).build();

		} catch (IOException e) {
			ErrorDTO error = handleError(e, StatusDTO.RESP_500, true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(uploadedInputStream, true);
		}
	}

}
