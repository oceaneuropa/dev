package org.orbit.fs.server.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.common.FileSystem;
import org.origin.common.resource.IPath;
import org.origin.common.rest.server.AbstractWSApplicationResource;

@javax.ws.rs.Path("/paths")
@Produces(MediaType.APPLICATION_JSON)
public class FilePathsResource extends AbstractWSApplicationResource {

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
		List<IPath> resultPaths = new ArrayList<IPath>();

		FileSystem fs = getService(FileSystem.class);
		FilePath parent = (parentPath != null) ? new FilePath(parentPath) : null;

		if (parent == null || parent.isEmpty() || parent.isRoot()) {
			IPath[] paths = fs.listRoots();
			for (IPath path : paths) {
				resultPaths.add(path);
			}
		} else {
			IPath[] paths = fs.listFiles(parent);
			for (IPath path : paths) {
				resultPaths.add(path);
			}
		}
		return Response.ok().entity(resultPaths).build();
	}

}
