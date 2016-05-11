package com.osgi.example1.fs.server.ws;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.origin.common.rest.dto.ErrorDTO;
import org.origin.common.rest.dto.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.util.IOUtil;

@javax.ws.rs.Path("/content")
@Produces(MediaType.APPLICATION_JSON)
public class FileContentResource extends AbstractApplicationResource {

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile( //
			@QueryParam(value = "path") String pathString, //
			@FormDataParam("file") InputStream uploadedInputStream, //
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("File path is null.")).build();
		}

		FileSystem fs = getService(FileSystem.class);
		Path path = new Path(pathString);

		try {
			if (fs.exists(path) && fs.isDirectory(path)) {
				return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("Path '" + path.getPathString() + "' exists but is a directory.")).build();
			}

			fs.copyInputStreamToFsFile(uploadedInputStream, path);

			return Response.ok().entity(StatusDTO.status("200", "success", "File is uplaoded to path '" + pathString + "'.")).build();

		} catch (IOException e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		} finally {
			IOUtil.closeQuietly(uploadedInputStream, true);
		}
	}

}
