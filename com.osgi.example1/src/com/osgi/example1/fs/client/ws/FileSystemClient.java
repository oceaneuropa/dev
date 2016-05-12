package com.osgi.example1.fs.client.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.dto.StatusDTO;
import org.origin.common.util.JSONUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osgi.example1.fs.common.FileMetadata;
import com.osgi.example1.fs.common.Path;
import com.osgi.example1.util.IOUtil;

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
	protected <T> T getFileAttribute(Path path, String attribute, Class<T> clazz) throws ClientException {
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
	 * Check whether a file is a directory.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean isDirectory(Path path) throws ClientException {
		boolean isDirectory = getFileAttribute(path, FileMetadata.IS_DIRECTORY, Boolean.class);
		return isDirectory;
	}

	/**
	 * Check whether a file is hidden.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean isHidden(Path path) throws ClientException {
		boolean isHidden = getFileAttribute(path, FileMetadata.IS_HIDDEN, Boolean.class);
		return isHidden;
	}

	/**
	 * Check whether a file exists.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean exists(Path path) throws ClientException {
		boolean exists = getFileAttribute(path, FileMetadata.EXISTS, Boolean.class);
		return exists;
	}

	/**
	 * Check whether a file can be executed.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean canExecute(Path path) throws ClientException {
		boolean canExecute = getFileAttribute(path, FileMetadata.CAN_EXECUTE, Boolean.class);
		return canExecute;
	}

	/**
	 * Check whether a file can be read.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean canRead(Path path) throws ClientException {
		boolean canRead = getFileAttribute(path, FileMetadata.CAN_READ, Boolean.class);
		return canRead;
	}

	/**
	 * Check whether a file can be written.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public boolean canWrite(Path path) throws ClientException {
		boolean canWrite = getFileAttribute(path, FileMetadata.CAN_WRITE, Boolean.class);
		return canWrite;
	}

	/**
	 * Get file length.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public long getLength(Path path) throws ClientException {
		long length = getFileAttribute(path, FileMetadata.LENGTH, Long.class);
		// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LENGTH, Long.class);
		// if (attrValue instanceof Long) {
		// length = (long) attrValue;
		// } else if (attrValue instanceof Integer) {
		// length = Long.valueOf((Integer) attrValue);
		// } else if (attrValue instanceof String) {
		// length = Long.valueOf((String) attrValue);
		// }
		return length;
	}

	/**
	 * Get file last modified time.
	 * 
	 * @param path
	 * @return
	 * @throws ClientException
	 */
	public long getLastModified(Path path) throws ClientException {
		long lastModified = getFileAttribute(path, FileMetadata.LAST_MODIFIED, Long.class);
		// Object attrValue = getClient().getFileAttribute(this.path, FileMetadata.LAST_MODIFIED, Long.class);
		// if (attrValue instanceof Long) {
		// lastModified = (long) attrValue;
		// } else if (attrValue instanceof Integer) {
		// lastModified = Long.valueOf((Integer) attrValue);
		// } else if (attrValue instanceof String) {
		// lastModified = Long.valueOf((String) attrValue);
		// }
		return lastModified;
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
	 * Upload file content read from InputStream to dest file path.
	 * 
	 * @param input
	 * @param destFilePath
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean uploadInputStreamToFsFile(InputStream input, Path destFilePath) throws ClientException, IOException {
		try {
			MultiPart multipart = new FormDataMultiPart();
			StreamDataBodyPart filePart = new StreamDataBodyPart("file", input, "file", MediaType.APPLICATION_OCTET_STREAM_TYPE);
			multipart.bodyPart(filePart);

			WebTarget target = getRootPath().path("content").queryParam("path", destFilePath.getPathString());
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.entity(multipart, multipart.getMediaType()));
			checkResponse(response);

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
	public boolean uploadFileToFsFile(File localFile, Path destFilePath) throws ClientException, IOException {
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
					FormDataContentDisposition.FormDataContentDispositionBuilder formBuilder = FormDataContentDisposition.name("file");
					formBuilder.fileName(URLEncoder.encode(localFile.getName(), "UTF-8"));
					formBuilder.size(localFile.length());
					formBuilder.modificationDate(new Date(localFile.lastModified()));
					filePart.setFormDataContentDisposition(formBuilder.build());
				}
				multipart.bodyPart(filePart);
			}

			WebTarget target = getRootPath().path("content").queryParam("path", destFilePath.getPathString());
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).post(Entity.entity(multipart, multipart.getMediaType()));
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
	public boolean uploadFileToFsDirectory(File localFile, Path destDirPath) throws ClientException, IOException {
		Path destFilePath = new Path(destDirPath, localFile.getName());
		return uploadFileToFsFile(localFile, destFilePath);
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
	public boolean uploadDirectoryToFsDirectory(File localDir, Path destDirPath, boolean includingSourceDir) throws ClientException, IOException {
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
			boolean succeed = doUploadDirectoryToFsDirectory(memberFile, destDirPath, encounteredFiles);
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
	protected boolean doUploadDirectoryToFsDirectory(File localFile, Path destDirPath, List<File> encounteredFiles) throws IOException {
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
				boolean succeed = doUploadDirectoryToFsDirectory(memberFile, newDestDirPath, encounteredFiles);
				if (!succeed) {
					return false;
				}
			}
			return true;

		} else {
			try {
				return uploadFileToFsDirectory(localFile, destDirPath);
			} catch (ClientException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * Download file from FS and write the file content to a OutputStream.
	 * 
	 * @param sourceFilePath
	 * @param output
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean downloadFsFileToOutputStream(Path sourceFilePath, OutputStream output) throws ClientException, IOException {
		// Check source
		if (sourceFilePath.isRoot()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is not a file.");
		}
		if (sourceFilePath.isEmpty()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is empty.");
		}
		boolean exists = exists(sourceFilePath);
		if (!exists) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' does not exist.");
		}
		boolean isDirectory = isDirectory(sourceFilePath);
		if (isDirectory) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' exists but is a directory.");
		}

		InputStream input = null;
		try {
			WebTarget target = getRootPath().path("content").queryParam("path", sourceFilePath.getPathString());
			Builder builder = target.request(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			input = response.readEntity(InputStream.class);

			if (input != null) {
				IOUtil.copy(input, output);
			}
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(input, true);
		}
		return true;
	}

	/**
	 * Download file from FS to local file.
	 * 
	 * @param sourceFilePath
	 * @param destLocalFile
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean downloadFsFileToFile(Path sourceFilePath, File destLocalFile) throws ClientException, IOException {
		// Check source
		if (sourceFilePath.isRoot()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is not a file.");
		}
		if (sourceFilePath.isEmpty()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is empty.");
		}
		boolean exists = exists(sourceFilePath);
		if (!exists) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' does not exist.");
		}
		boolean isDirectory = isDirectory(sourceFilePath);
		if (isDirectory) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' exists but is a directory.");
		}

		// Check dest
		if (destLocalFile.exists() && destLocalFile.isDirectory()) {
			throw new IOException("File '" + destLocalFile.getAbsolutePath() + "' exists but is a directory.");
		}
		if (destLocalFile.getParentFile() != null && !destLocalFile.getParentFile().exists()) {
			destLocalFile.getParentFile().mkdirs();
		}

		InputStream input = null;
		OutputStream output = null;
		try {
			WebTarget target = getRootPath().path("content").queryParam("path", sourceFilePath.getPathString());
			Builder builder = target.request(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			input = response.readEntity(InputStream.class);
			output = new FileOutputStream(destLocalFile);

			if (input != null && output != null) {
				IOUtil.copy(input, output);
			}
		} catch (ClientException e) {
			handleException(e);
		} finally {
			IOUtil.closeQuietly(output, true);
			IOUtil.closeQuietly(input, true);
		}
		return true;
	}

	/**
	 * Download file from FS to local directory.
	 * 
	 * @param sourceFilePath
	 * @param destLocalDir
	 * @return
	 * @throws ClientException
	 * @throws IOException
	 */
	public boolean downloadFsFileToDirectory(Path sourceFilePath, File destLocalDir) throws ClientException, IOException {
		// Check source
		if (sourceFilePath.isRoot()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is not a file.");
		}
		if (sourceFilePath.isEmpty()) {
			throw new IOException("Source path '" + sourceFilePath.getPathString() + "' is empty.");
		}

		// Check dest
		if (destLocalDir.exists() && !destLocalDir.isDirectory()) {
			throw new IOException("File '" + destLocalDir.getAbsolutePath() + "' exists but is not a directory.");
		}

		File destLocalFile = new File(destLocalDir, sourceFilePath.getLastSegment());
		return downloadFsFileToFile(sourceFilePath, destLocalFile);
	}

	/**
	 * Download a directory from FS to local directory.
	 * 
	 * @param sourceFilePath
	 * @param destLocalDir
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 * @throws ClientException
	 */
	public boolean downloadFsDirectoryToDirectory(Path sourceDirPath, File destLocalDir, boolean includingSourceDir) throws ClientException, IOException {
		// Check source
		if (sourceDirPath.isRoot()) {
			throw new IOException("Source path '" + sourceDirPath.getPathString() + "' is not a directory.");
		}
		if (sourceDirPath.isEmpty()) {
			throw new IOException("Source path '" + sourceDirPath.getPathString() + "' is empty.");
		}
		boolean exists = exists(sourceDirPath);
		if (!exists) {
			throw new IOException("Source path '" + sourceDirPath.getPathString() + "' does not exist.");
		}
		boolean isDirectory = isDirectory(sourceDirPath);
		if (!isDirectory) {
			throw new IOException("Source path '" + sourceDirPath.getPathString() + "' exists but is not a directory.");
		}

		// Check dest
		if (destLocalDir.exists() && !destLocalDir.isDirectory()) {
			throw new IOException("File '" + destLocalDir.getAbsolutePath() + "' exists but is not a directory.");
		}

		if (includingSourceDir) {
			destLocalDir = new File(destLocalDir, sourceDirPath.getLastSegment());
		}

		// Recursively copy every member path from the sourceDirPath to the destLocalDir.
		List<Path> encounteredPaths = new ArrayList<Path>();
		Path[] memberPaths = listFiles(sourceDirPath);
		for (Path memberPath : memberPaths) {
			doDownloadPathToDirectory(memberPath, destLocalDir, encounteredPaths);
		}

		return true;
	}

	/**
	 * 
	 * @param path
	 * @param destLocalDir
	 * @param encounteredPaths
	 * @throws ClientException
	 * @throws IOException
	 */
	protected void doDownloadPathToDirectory(Path path, File destLocalDir, List<Path> encounteredPaths) throws ClientException, IOException {
		if (path == null || encounteredPaths.contains(path)) {
			return;
		} else {
			encounteredPaths.add(path);
		}

		if (isDirectory(path)) {
			File newDestLocalDir = new File(destLocalDir, path.getLastSegment());
			if (!newDestLocalDir.exists()) {
				newDestLocalDir.mkdirs();
			}

			Path[] memberPaths = listFiles(path);
			for (Path memberPath : memberPaths) {
				doDownloadPathToDirectory(memberPath, newDestLocalDir, encounteredPaths);
			}
		} else {
			downloadFsFileToDirectory(path, destLocalDir);
		}
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
