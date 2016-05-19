package com.osgi.example1.fs.server.ws;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.server.AbstractApplicationResource;

import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;

@javax.ws.rs.Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class FileMetadataResource extends AbstractApplicationResource {

	protected static String ACTION_MKDIRS = "mkdirs";
	protected static String ACTION_CREATE_NEW_FILE = "createNewFile";

	/**
	 * Get file metadata.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}
	 * 
	 * @param pathString
	 * @param attrName
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("path") String pathString, @QueryParam("attribute") String attrName) {
		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("File path is null.")).build();
		}

		FileSystem fs = getService(FileSystem.class);
		Path path = new Path(pathString);

		FileMetadata metadata = fs.getFileMetaData(path);
		if (attrName != null) {
			// attribute name is specified --- return attribute value only
			Object attrValue = metadata.getAttribute(attrName);
			if (attrValue != null) {
				return Response.ok().entity(attrValue).build();
			}
			// no attribute value is retrieved --- return empty value
			return Response.ok().build();
		} else {
			// attribute name is not specified --- return whole metadata
			return Response.ok().entity(metadata).build();
		}
	}

	/**
	 * Create new directories for a Path or create new file for a Path.
	 * 
	 * URL (POST): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}&action={action}
	 * 
	 * @param pathString
	 * @param action
	 *            Actions are "mkdirs" and "createNewFile".
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response post(@QueryParam("path") String pathString, @QueryParam("action") String action) {
		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("File path is null.")).build();
		}
		if (action == null) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("Action is null.")).build();
		}
		boolean isActionSupported = false;
		if (ACTION_MKDIRS.equalsIgnoreCase(action) || ACTION_CREATE_NEW_FILE.equalsIgnoreCase(action)) {
			isActionSupported = true;
		}
		if (!isActionSupported) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("Action '" + action + "' is not supported.")).build();
		}

		FileSystem fs = getService(FileSystem.class);
		Path path = new Path(pathString);

		if (ACTION_MKDIRS.equalsIgnoreCase(action)) {
			try {
				boolean succeed = fs.mkdirs(path);
				if (succeed) {
					return Response.ok().entity(StatusDTO.status("200", "success", "Path '" + pathString + "' is created.")).build();
				} else {
					return Response.ok().entity(StatusDTO.status("200", "success", "Path '" + pathString + "' is not created.")).build();
				}
			} catch (IOException e) {
				ErrorDTO error = handleError(e, "500", true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}

		} else if (ACTION_CREATE_NEW_FILE.equalsIgnoreCase(action)) {
			try {
				boolean succeed = fs.createNewFile(path);
				if (succeed) {
					return Response.ok().entity(StatusDTO.status("200", "success", "New file '" + pathString + "' is created.")).build();
				} else {
					return Response.ok().entity(StatusDTO.status("200", "failed", "New file '" + pathString + "' is not created.")).build();
				}
			} catch (IOException e) {
				ErrorDTO error = handleError(e, "500", true);
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
			}
		}

		return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("Action '" + action + "' is not supported.")).build();
	}

	/**
	 * Delete a file or a directory.
	 * 
	 * URL (DELETE): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}
	 * 
	 * @param pathString
	 * @return
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("path") String pathString) {
		if (pathString == null) {
			return Response.status(Status.BAD_REQUEST).entity(ErrorDTO.newInstance("File path is null.")).build();
		}

		try {
			Path path = new Path(pathString);
			FileSystem fs = getService(FileSystem.class);

			if (!fs.exists(path)) {
				return Response.ok().entity(StatusDTO.status("200", "failed", "File '" + pathString + "' does not exist.")).build();
			}

			boolean isDirectory = fs.isDirectory(path);
			String label = isDirectory ? "Directory" : "File";

			boolean succeed = fs.delete(path);

			if (succeed) {
				return Response.ok().entity(StatusDTO.status("200", "success", label + " '" + pathString + "' is deleted.")).build();
			} else {
				return Response.ok().entity(StatusDTO.status("200", "failed", label + " '" + pathString + "' is not deleted.")).build();
			}

		} catch (IOException e) {
			ErrorDTO error = handleError(e, "500", true);
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build();
		}
	}

}
