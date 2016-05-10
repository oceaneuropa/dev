package com.osgi.example1.fs.server.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.server.AbstractApplicationResource;

import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.database.FsTableUtil;

@javax.ws.rs.Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class FileSystemMetadataResource extends AbstractApplicationResource {

	/**
	 * Get file metadata.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}
	 * 
	 * @param pathString
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFile(@QueryParam("path") String pathString) {
		FileSystem fs = getService(FileSystem.class);

		Path path = null;
		if (pathString != null) {
			path = new Path(pathString);
		}

		FileMetadata metadata = null;
		if (path != null) {
			metadata = fs.getFileMetaData(path);
		} else {
			metadata = FsTableUtil.createNullFileMetadata();
		}
		return Response.ok().entity(metadata).build();
	}

}
