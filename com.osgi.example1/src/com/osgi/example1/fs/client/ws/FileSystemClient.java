package com.osgi.example1.fs.client.ws;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.dto.StatusDTO;
import org.origin.common.util.JSONUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;

public class FileSystemClient extends AbstractClient {

	protected static List<Path> EMPTY_PATHS = new ArrayList<Path>();
	protected static String ACTION_MKDIRS = "mkdirs";
	protected static String ACTION_CREATE_NEW_FILE = "createNewFile";

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
	public Path[] listRoots() throws ClientException {
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

		} catch (ClientException | IOException e) {
			handleException(e);
		}
		return metadata;
	}

	/**
	 * Get file attribute by path and attribute name.
	 * 
	 * Request URL (GET): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}&attribute={attribute}
	 * 
	 * @param path
	 * @param attribute
	 * @param clazz
	 * @return
	 * @throws ClientException
	 */
	public <T> T getFileAttribute(Path path, String attribute, Class<T> clazz) throws ClientException {
		T result = null;
		WebTarget target = getRootPath().path("metadata").queryParam("path", path.getPathString()).queryParam("attribute", attribute);
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			result = response.readEntity(clazz);

		} catch (ClientException e) {
			handleException(e);
		}
		return result;
	}

	/**
	 * Create directories for a Path.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}&action=mkdirs
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean mkdirs(Path path) throws ClientException {
		WebTarget target = getRootPath().path("metadata").queryParam("path", path.getPathString()).queryParam("action", ACTION_MKDIRS);
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(null);
			checkResponse(response);

			// String responseString = response.readEntity(String.class);
			// int responseStatus = response.getStatus();
			// System.out.println("responseString = " + responseString);
			// System.out.println("responseStatus = " + responseStatus);

			StatusDTO status = response.readEntity(StatusDTO.class);
			if (status != null && "success".equals(status.getStatus())) {
				return true;
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return false;
	}

	/**
	 * Create new empty file for a Path.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}&action=createNewFile
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean createNewFile(Path path) throws ClientException {
		WebTarget target = getRootPath().path("metadata").queryParam("path", path.getPathString()).queryParam("action", ACTION_CREATE_NEW_FILE);
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(null);
			checkResponse(response);

			// String responseString = response.readEntity(String.class);
			// int responseStatus = response.getStatus();
			// System.out.println("responseString = " + responseString);
			// System.out.println("responseStatus = " + responseStatus);

			StatusDTO status = response.readEntity(StatusDTO.class);
			if (status != null && "success".equals(status.getStatus())) {
				return true;
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return false;
	}

	/**
	 * Upload local file to dest file path.
	 * 
	 * @param localFile
	 * @param destFilePath
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean uploadFileToFile(File localFile, Path destFilePath) throws ClientException, IOException {
		// Check source file
		if (!localFile.exists()) {
			throw new IOException("Local file '" + localFile + "' does not exist.");
		}
		if (localFile.isDirectory()) {
			throw new IOException("Local file '" + localFile + "' exists but is a directory.");
		}

		try {
			MultiPart multipart = new FormDataMultiPart();
			{
				FileDataBodyPart filePart = new FileDataBodyPart("file", localFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
				{
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name(localFile.getName());
					formBuilder.fileName(URLEncoder.encode(localFile.getName(), "UTF-8"));
					formBuilder.size(localFile.length());
					formBuilder.modificationDate(new Date(localFile.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("content").queryParam("path", destFilePath.getPathString());
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA));
			checkResponse(response);

			StatusDTO status = response.readEntity(StatusDTO.class);
			if (status != null && "success".equals(status.getStatus())) {
				return true;
			}

		} catch (ClientException e) {
			handleException(e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Upload local file to dest directory path.
	 * 
	 * @param localFile
	 * @param destFilePath
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean uploadFileToDirectory(File localFile, Path destDirPath) throws ClientException, IOException {
		Path destFilePath = new Path(destDirPath, localFile.getName());
		return uploadFileToFile(localFile, destFilePath);
	}

	/**
	 * Upload local directory to dest directory path.
	 * 
	 * @param localDir
	 * @param destDirPath
	 * @param includingSourceDir
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean uploadDirectoryToDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws ClientException, IOException {
		// Check source directory
		if (!localDir.exists()) {
			throw new IOException("Local directory '" + localDir + "' does not exist.");
		}
		if (!localDir.isDirectory()) {
			throw new IOException("Local directory '" + localDir + "' exists but is not a directory.");
		}

		if (includingSourceDir) {
			destDirPath = new Path(destDirPath, localDir.getName());
		}

		// Recursively copy every file from the localDir to the destDirPath.
		List<File> encounteredFiles = new ArrayList<File>();
		File[] memberFiles = localDir.listFiles();
		for (File memberFile : memberFiles) {
			boolean succeed = doUploadDirectoryToDirectory(memberFile, destDirPath, encounteredFiles);
			if (!succeed) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param localFile
	 * @param destDirPath
	 * @param encounteredFiles
	 * @throws IOException
	 * @throws ClientException
	 */
	protected boolean doUploadDirectoryToDirectory(File localFile, Path destDirPath, List<File> encounteredFiles) throws IOException {
		if (localFile == null || encounteredFiles.contains(localFile)) {
			return true;
		} else {
			encounteredFiles.add(localFile);
		}

		if (localFile.isDirectory()) {
			Path newDestDirPath = new Path(destDirPath, localFile.getName());
			try {
				mkdirs(newDestDirPath);
			} catch (ClientException e) {
				e.printStackTrace();
				return false;
			}

			File[] memberFiles = localFile.listFiles();
			for (File memberFile : memberFiles) {
				boolean succeed = doUploadDirectoryToDirectory(memberFile, newDestDirPath, encounteredFiles);
				if (!succeed) {
					return false;
				}
			}
			return true;

		} else {
			try {
				return uploadFileToDirectory(localFile, destDirPath);
			} catch (ClientException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Delete a file or a directory.
	 * 
	 * Request URL (POST): {scheme}://{host}:{port}/fs/v1/metadata?path={pathString}&action=createNewFile
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean delete(Path path) throws ClientException {
		WebTarget target = getRootPath().path("metadata").queryParam("path", path.getPathString());
		try {
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).delete();
			checkResponse(response);

			// String responseString = response.readEntity(String.class);
			// int responseStatus = response.getStatus();
			// System.out.println("responseString = " + responseString);
			// System.out.println("responseStatus = " + responseStatus);

			StatusDTO status = response.readEntity(StatusDTO.class);
			if (status != null && "success".equals(status.getStatus())) {
				return true;
			}

		} catch (ClientException e) {
			handleException(e);
		}
		return false;
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
