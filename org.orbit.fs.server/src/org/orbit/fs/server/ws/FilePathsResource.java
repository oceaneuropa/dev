package org.orbit.fs.server.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.server.service.FileSystemService;
import org.origin.common.rest.server.AbstractApplicationResource;

@javax.ws.rs.Path("/paths")
@Produces(MediaType.APPLICATION_JSON)
public class FilePathsResource extends AbstractApplicationResource {

	/**
	 * Get file paths with parent path.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/fs/v1/paths
	 * 
	 * @param parentPath
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("parentPath") String parentPath) {
		List<FilePath> resultPaths = new ArrayList<FilePath>();

		FileSystemService fs = getService(FileSystemService.class);
		FilePath parent = (parentPath != null) ? new FilePath(parentPath) : null;

		if (parent == null || parent.isEmpty() || parent.isRoot()) {
			FilePath[] paths = fs.listRoots();
			for (FilePath path : paths) {
				resultPaths.add(path);
			}
		} else {
			FilePath[] paths = fs.listFiles(parent);
			for (FilePath path : paths) {
				resultPaths.add(path);
			}
		}
		return Response.ok().entity(resultPaths).build();
	}

}
