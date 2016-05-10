package com.osgi.example1.fs.server.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.server.AbstractApplicationResource;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;

@javax.ws.rs.Path("/paths")
@Produces(MediaType.APPLICATION_JSON)
public class FileSystemPathsResource extends AbstractApplicationResource {

	/**
	 * Get files.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/fs/v1/paths
	 * 
	 * @param parentPath
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFiles(@QueryParam("parentPath") String parentPath) {
		List<Path> results = new ArrayList<Path>();

		FileSystem fs = getService(FileSystem.class);

		Path parent = null;
		if (parentPath != null) {
			parent = new Path(parentPath);
		}

		if (parent == null || parent.isEmpty() || parent.isRoot()) {
			Path[] paths = fs.listRootFiles();
			for (Path path : paths) {
				results.add(path);
			}
		} else {
			Path[] paths = fs.listFiles(parent);
			for (Path path : paths) {
				results.add(path);
			}
		}

		return Response.ok().entity(results).build();
	}

}