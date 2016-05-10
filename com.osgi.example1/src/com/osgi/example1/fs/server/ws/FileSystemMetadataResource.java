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

@javax.ws.rs.Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class FileSystemMetadataResource extends AbstractApplicationResource {

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
		FileSystem fs = getService(FileSystem.class);

		Path path = null;
		if (pathString != null) {
			path = new Path(pathString);
		}

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

}
