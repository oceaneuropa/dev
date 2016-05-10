package com.osgi.example1.fs.client.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.JSONUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public class FileSystemClient extends AbstractClient {

	protected static List<Path> EMPTY_PATHS = new ArrayList<Path>();

	/**
	 * 
	 * @param config
	 */
	public FileSystemClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get root paths.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/fs/v1/paths
	 * 
	 * @return
	 * @throws ClientException
	 */
	public Path[] listRootFiles() throws ClientException {
		return listFiles(null);
	}

	/**
	 * Get files in parent path.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/fs/v1/paths?parentPath={parentPath}
	 * 
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public Path[] listFiles(Path parent) throws ClientException {
		List<Path> paths = new ArrayList<Path>();
		try {
			WebTarget target = getRootPath().path("paths");
			if (parent != null) {
				target = target.queryParam("parentPath", parent.getPathString());
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			String responseString = response.readEntity(String.class);
			// paths = response.readEntity(new GenericType<List<Path>>() {
			// });

			ObjectMapper mapper = JSONUtil.createObjectMapper(false);
			List<Map<String, Object>> responseItems = mapper.readValue(responseString, List.class);
			if (responseItems != null) {
				for (Map<String, Object> responseItem : responseItems) {
					Path path = toPath(responseItem);
					if (path != null) {
						paths.add(path);
					}
				}
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		}
		return paths.toArray(new Path[paths.size()]);
	}

	/**
	 * Get file metadata by path.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	@SuppressWarnings("unchecked")
	public FileMetadata getFileMetadata(Path path) throws ClientException {
		FileMetadata metadata = null;
		WebTarget target = getRootPath().path("metadata").queryParam("path", path.getPathString());
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = JSONUtil.createObjectMapper(false);
			Map<String, Object> responseItem = mapper.readValue(responseString, Map.class);
			if (responseItem != null) {
				metadata = toFileMetadata(responseItem);
			}

			// metadata = response.readEntity(new GenericType<FileMetadata>() {
			// });
		} catch (ClientException | IOException e) {
			handleException(e);
		}
		return metadata;
	}

	/**
	 * Convert a response item into a Path object.
	 * 
	 * @param responseItem
	 * @return
	 */
	protected Path toPath(Map<String, Object> responseItem) {
		Path path = null;
		if (responseItem != null) {
			Object pathString = responseItem.get("pathString");
			if (pathString != null) {
				path = new Path(pathString.toString());
			}
		}
		return path;
	}

	/**
	 * Convert a response item into a FileMetadata object.
	 * 
	 * @param responseItem
	 * @return
	 */
	protected FileMetadata toFileMetadata(Map<String, Object> responseItem) {
		FileMetadata metadata = new FileMetadata();
		if (responseItem != null) {
			int fileId = (int) responseItem.get("fileId");
			int parentFileId = (int) responseItem.get("parentFileId");
			String name = (String) responseItem.get("name");
			boolean isDirectory = (boolean) responseItem.get("isDirectory");
			boolean isHidden = (boolean) responseItem.get("isHidden");
			String path = (String) responseItem.get("path");
			String parentPath = (String) responseItem.get("parentPath");
			boolean exists = (boolean) responseItem.get("exists");
			boolean canExecute = (boolean) responseItem.get("canExecute");
			boolean canRead = (boolean) responseItem.get("canRead");
			boolean canWrite = (boolean) responseItem.get("canWrite");
			long length = Long.valueOf((Integer) responseItem.get("length"));
			long lastModified = (long) responseItem.get("lastModified");

			metadata.setFileId(fileId);
			metadata.setParentFileId(parentFileId);
			metadata.setName(name);
			metadata.setIsDirectory(isDirectory);
			metadata.setHidden(isHidden);
			metadata.setPath(path);
			metadata.setParentPath(parentPath);
			metadata.setExists(exists);
			metadata.setCanExecute(canExecute);
			metadata.setCanRead(canRead);
			metadata.setCanWrite(canWrite);
			metadata.setLength(length);
			metadata.setLastModified(lastModified);
		}
		return metadata;
	}

}
