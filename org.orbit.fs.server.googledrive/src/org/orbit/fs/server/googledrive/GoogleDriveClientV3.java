package org.orbit.fs.server.googledrive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.server.googledrive.util.Comparators;
import org.orbit.fs.server.googledrive.util.GoogleDriveConstants;
import org.orbit.fs.server.googledrive.util.GoogleDriveHelper;
import org.orbit.fs.server.googledrive.util.GoogleDriveMimeTypes;
import org.origin.common.io.IOUtil;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Create;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/*
 * Jar download
 * https://mvnrepository.com/artifact/com.google.apis
 * 
 * About Fields and query
 * https://developers.google.com/drive/v3/web/search-parameters
 * 
 * Java API doc
 * https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/model/File.html
 * https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.Files.Create.html
 * https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.Files.List.html
 * 
 * REST API doc
 * https://developers.google.com/drive/v3/reference/files
 * 
 * REST API explorer
 * https://developers.google.com/apis-explorer/#p/drive/v3/
 *
 */
public class GoogleDriveClientV3 {

	protected GoogleDriveFSConfig config;
	// file to store user credentials
	protected java.io.File dataStoreDir;
	// client secret dir
	protected java.io.File clientSecretDir;
	protected List<String> scopes;
	protected String applicationName;

	protected HttpTransport httpTransport;
	protected JsonFactory jsonFactory;
	protected FileDataStoreFactory dataStoreFactory;

	protected Credential credential;
	protected Drive drive;

	protected boolean debug = true;

	/**
	 * 
	 * @param config
	 */
	public GoogleDriveClientV3(GoogleDriveFSConfig config) {
		this.config = config;
		if (debug) {
			System.out.println(config);
		}

		this.dataStoreDir = config.getDataStoreFile();
		this.clientSecretDir = config.getClientSecretDir();
		this.scopes = config.getScopes();
		this.applicationName = config.getApplicationName();

		try {
			this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			this.jsonFactory = JacksonFactory.getDefaultInstance();
			this.dataStoreFactory = new FileDataStoreFactory(this.dataStoreDir);

			this.credential = authorize();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized Credential authorize() throws IOException {
		InputStream in = null;
		try {
			// Load client secrets.
			in = new FileInputStream(this.clientSecretDir);
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(this.httpTransport, this.jsonFactory, clientSecrets, this.scopes).setDataStoreFactory(this.dataStoreFactory).setAccessType("offline").build();
			Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			if (debug) {
				System.out.println("Credentials saved to " + this.dataStoreDir.getAbsolutePath());
				System.out.println();
			}

			return credential;

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new IOException(e);
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public synchronized Drive getDrive() throws IOException {
		if (this.drive == null) {
			if (this.credential == null) {
				this.credential = authorize();
			}
			if (debug) {
				GoogleDriveHelper.INSTANCE.print(this.credential);
			}

			this.drive = new Drive.Builder(this.httpTransport, this.jsonFactory, this.credential).setApplicationName(this.applicationName).build();
			if (debug) {
				GoogleDriveHelper.INSTANCE.print(drive);
			}
		}
		return this.drive;
	}

	/**
	 * Get 'My Drive' folder id.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String getDriveFolderId() throws IOException {
		String driveFolderId = null;
		List<File> subFiles = getFiles(null, GoogleDriveConstants.FILE_FIELDS_SIMPLE, null);
		if (subFiles != null) {
			for (File subFile : subFiles) {
				List<String> parentIds = subFile.getParents();
				if (parentIds != null && !parentIds.isEmpty()) {
					driveFolderId = parentIds.get(0);
				}
			}
		}
		return driveFolderId;
	}

	/**
	 * List files in parent directory.
	 * 
	 * @param parentId
	 * @param fields
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getFiles(String parentId, String fields, Comparator<File> comparator) throws IOException {
		if (parentId == null || parentId.isEmpty()) {
			parentId = "root";
		}
		List<File> subFiles = queryFiles("'" + parentId + "' in parents and trashed = false", fields, comparator);
		return subFiles;
	}

	/**
	 * List trashed files.
	 * 
	 * @param fields
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getTrashedFiles(String fields, Comparator<File> comparator) throws IOException {
		return queryFiles("trashed = true", fields, comparator);
	}

	/**
	 * 
	 * @param file
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public File getParentFile(File file, String fields) throws IOException {
		File parentFile = null;
		List<String> parentIds = file.getParents();
		if (parentIds != null) {
			for (String parentId : parentIds) {
				File currParentFile = getFileById(parentId, fields);
				if (currParentFile != null) {
					parentFile = currParentFile;
					break;
				}
			}
		}
		return parentFile;
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public List<File> getParentFiles(File file) throws IOException {
		List<File> parentFiles = new ArrayList<File>();
		List<String> parents = file.getParents();
		if (parents != null) {
			for (String parentId : parents) {
				File currParentFile = getFileById(parentId, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
				if (currParentFile != null && !parentFiles.contains(currParentFile)) {
					parentFiles.add(currParentFile);
				}
			}
		}
		return parentFiles;
	}

	/**
	 * Get file by file id.
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public File getFileById(String fileId, String fields) throws IOException {
		File file = null;
		Get get = getDrive().files().get(fileId);
		if (get != null) {
			// drive.files.get fields:
			// appProperties,capabilities,contentHints,createdTime,description,explicitlyTrashed,fileExtension,folderColorRgb,fullFileExtension,hasThumbnail,headRevisionId,iconLink,id,imageMediaMetadata,isAppAuthorized,kind,lastModifyingUser,md5Checksum,mimeType,modifiedByMe,modifiedByMeTime,modifiedTime,name,originalFilename,ownedByMe,owners,parents,permissions,properties,quotaBytesUsed,shared,sharedWithMeTime,sharingUser,size,spaces,starred,thumbnailLink,thumbnailVersion,trashed,version,videoMediaMetadata,viewedByMe,viewedByMeTime,viewersCanCopyContent,webContentLink,webViewLink,writersCanShare
			// @see https://developers.google.com/apis-explorer/#p/drive/v3/drive.files.get
			if (fields != null) {
				get.setFields(fields);
			}

			file = get.execute();
		}
		return file;
	}

	/**
	 * Get file by folder id and file name.
	 * 
	 * @param folderId
	 * @param name
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public File getFileByName(String folderId, String name, String fields) throws IOException {
		File file = null;
		List<File> subFiles = queryFiles("'" + folderId + "' in parents and name = '" + name + "' and trashed = false", fields, Comparators.GoogleFileComparator.ASC);
		if (subFiles != null && !subFiles.isEmpty()) {
			file = subFiles.get(0);
		}
		return file;
	}

	/**
	 * Get file by file full path string.
	 * 
	 * @param fullPath
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public File getFileByFullPath(String fullPath, String fields) throws IOException {
		if ("/".equals(fullPath)) {
			String driveFolderId = getDriveFolderId();
			if (driveFolderId != null) {
				File driveFolder = this.getFileById(driveFolderId, fields);
				return driveFolder;
			}
		}

		File file = null;

		FilePath path = new FilePath(fullPath);
		List<File> matchesFiles = new ArrayList<File>();
		getFileByFullPath_(null, path, fields, matchesFiles);

		if (!matchesFiles.isEmpty()) {
			String fileName = new FilePath(fullPath).getLastSegment();
			List<String> candidateMimeTypes = GoogleDriveHelper.INSTANCE.getCandidateMimeTypes(fileName);

			for (File matchesFile : matchesFiles) {
				String mimeType = matchesFile.getMimeType();
				if (candidateMimeTypes.contains(mimeType)) {
					file = matchesFile;
					break;
				}
			}
			// still not found one. return the first one.
			if (file == null) {
				file = matchesFiles.get(0);
			}
		}
		return file;
	}

	/**
	 * 
	 * @param parentFile
	 * @param path
	 * @param fields
	 * @param matchesFiles
	 * @throws IOException
	 */
	protected void getFileByFullPath_(File parentFile, FilePath path, String fields, List<File> matchesFiles) throws IOException {
		Comparator<File> comparator = Comparators.GoogleFileComparator.ASC;

		String[] segments = path.getSegments();
		if (segments != null && segments.length > 0) {
			int len = segments.length;

			String parentId = "root";
			if (parentFile != null) {
				parentId = parentFile.getId();
			}

			for (int i = 0; i < len; i++) {
				// boolean isFirst = (i == 0) ? true : false;
				boolean isLast = (i == len - 1) ? true : false;
				String segment = segments[i];

				String query = "'" + parentId + "' in parents and name = '" + segment + "'";
				if (!isLast) {
					query += " and mimeType = '" + GoogleDriveMimeTypes.FOLDER + "'";
				}
				query += " and trashed = false";

				List<File> subFiles = queryFiles(query, fields, comparator);

				// file cannot be found at current segment. no need search any more.
				if (subFiles == null || subFiles.isEmpty()) {
					return;
				}

				if (isLast) {
					// reach the end of the segment. subFiles are the target files.
					for (File subFile : subFiles) {
						if (!matchesFiles.contains(subFile)) {
							matchesFiles.add(subFile);
						}
					}
				} else {
					// not last segment - current file is a directory.
					// look into each matching sub folders to look for target files.
					FilePath subPath = path.getPath(i + 1);
					for (File subFolder : subFiles) {
						getFileByFullPath_(subFolder, subPath, fields, matchesFiles);
					}
				}
			}
		}
	}

	/**
	 * Get files by query.
	 * 
	 * @see https://developers.google.com/drive/v3/web/search-parameters
	 * 
	 * @see http://stackoverflow.com/questions/17266168/whats-the-right-way-to-find-files-by-full-path-in-google-drive-api-v2
	 * 
	 *      q = 'root' in parents and title = 'path0' and mimeType = 'application/vnd.google-apps.folder'
	 * 
	 *      q = '<id of path0>' in parents and title = 'path1' and mimeType='application/vnd.google-apps.folder'
	 * 
	 * @param query
	 * @param fields
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> queryFiles(String query, String fields, Comparator<File> comparator) throws IOException {
		List<File> result = new ArrayList<File>();

		Drive.Files.List list = getDrive().files().list();
		if (query != null) {
			list.setQ(query);
		}

		// Drive.Files.List's fields are files,kind,nextPageToken
		// see https://developers.google.com/apis-explorer/#p/drive/v3/drive.files.list for available fields
		FileList fileList = list.execute();
		if (fileList != null) {
			List<File> files = fileList.getFiles();
			if (files != null) {
				for (File file : files) {
					File file2 = getFileById(file.getId(), fields);
					if (file2 != null) {
						result.add(file2);
					}
				}
			}
		}

		if (comparator != null && !result.isEmpty()) {
			Collections.sort(result, comparator);
		}

		return result;
	}

	/**
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public String getFullPathById(String fileId) throws IOException {
		String driveFolderId = getDriveFolderId();
		return getFullPathById(driveFolderId, fileId);
	}

	/**
	 * @param driveFolderId
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public String getFullPathById(String driveFolderId, String fileId) throws IOException {
		String path = null;

		String fields = GoogleDriveConstants.FILE_FIELDS_SIMPLE;

		File file = getFileById(fileId, fields);
		if (file != null) {
			path = "";
		}
		while (file != null) {
			// if (debug) {
			// String prettyString = file.toPrettyString();
			// System.out.println(prettyString);
			// }

			String name = file.getName();
			if (name == null || name.isEmpty()) {
				break;
			}

			if (!path.isEmpty()) {
				path = "/" + path;
			}
			path = name + path;

			File parentFile = getParentFile(file, fields);
			if (parentFile == null) {
				break;
			}

			String parentFolderId = parentFile.getId();
			if (parentFolderId != null && parentFolderId.equals(driveFolderId)) {
				break;
			}
			file = getFileById(parentFolderId, fields);
		}

		if (path != null) {
			path = "/" + path;
		}

		return path;
	}

	/**
	 * Create a root folder.
	 * 
	 * @see http://www.programcreek.com/java-api-examples/index.php?source_dir=mytracks-master/MyTracks/src/com/google/android/apps/mytracks/io/sync/SyncUtils.
	 *      java
	 * 
	 * @param drive
	 * @param name
	 * @throws IOException
	 */
	public File createDirectory(String folderId, String name) throws IOException {
		File existingDir = getFileByName(folderId, name, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
		if (existingDir != null) {
			if (GoogleDriveHelper.INSTANCE.isDirectory(existingDir)) {
				// dir exists and is a folder - return the folder
				return existingDir;

			} else {
				// dir exists but is not a folder - throw exception
				throw new IOException("File '" + existingDir.getName() + "' exists but is not a directory.");
			}
		}

		Drive drive = getDrive();

		File fileMetadata = new File();
		fileMetadata.setName(name);
		fileMetadata.setMimeType(GoogleDriveMimeTypes.FOLDER);
		if (folderId != null) {
			List<String> parentIds = new ArrayList<String>();
			parentIds.add(folderId);
			fileMetadata.setParents(parentIds);
		}

		Create create = drive.files().create(fileMetadata);

		// Drive.Files.Create available fields are:
		// appProperties,capabilities,contentHints,createdTime,description,explicitlyTrashed,fileExtension,folderColorRgb,fullFileExtension,hasThumbnail,headRevisionId,iconLink,id,imageMediaMetadata,isAppAuthorized,kind,lastModifyingUser,md5Checksum,mimeType,modifiedByMe,modifiedByMeTime,modifiedTime,name,originalFilename,ownedByMe,owners,parents,permissions,properties,quotaBytesUsed,shared,sharedWithMeTime,sharingUser,size,spaces,starred,thumbnailLink,thumbnailVersion,trashed,version,videoMediaMetadata,viewedByMe,viewedByMeTime,viewersCanCopyContent,webContentLink,webViewLink,writersCanShare
		// @see https://developers.google.com/apis-explorer/#p/drive/v3/drive.files.create

		File createdDir = create.execute();
		if (debug) {
			System.out.println("Directory '" + createdDir.getName() + "' is created.");
		}

		return createdDir;
	}

	/**
	 * Move file between folders.
	 * 
	 * @see https://developers.google.com/drive/v3/web/folder
	 * 
	 * @param fileId
	 *            file to be moved
	 * @param targetFolderId
	 *            directory to be moved into
	 * @return
	 * @throws IOException
	 */
	public File moveToFolder(String fileId, String targetFolderId) throws IOException {
		Drive drive = getDrive();

		StringBuilder parentIdsToRemove = new StringBuilder();
		// File fileWithParents = drive.files().get(fileId).setFields("parents").execute();
		File fileWithParents = getFileById(fileId, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
		List<String> existingParentIds = fileWithParents.getParents();
		if (existingParentIds != null) {
			for (String parent : existingParentIds) {
				parentIdsToRemove.append(parent);
				parentIdsToRemove.append(',');
			}
		}

		// Move the file to the new folder
		Update update = drive.files().update(fileId, null);
		update.setRemoveParents(parentIdsToRemove.toString());
		update.setAddParents(targetFolderId);
		// update.setFields("id, parents");
		File movedFile = update.execute();
		return movedFile;
	}

	public File moveToTrash(String fileId) throws IOException {
		Drive drive = getDrive();

		Update update = drive.files().update(fileId, null);
		// update.setRemoveParents(parentIdsToRemove.toString());
		// update.setAddParents(targetFolderId);
		// update.setFields("id, parents");
		File movedFile = update.execute();
		return movedFile;
	}

	/**
	 * Upload a local file to a google drive folder.
	 * 
	 * @param localFile
	 * @param folderId
	 * @param mimeType
	 * @return
	 * @throws IOException
	 */
	public File uploadFileToGdfsDirectory(java.io.File localFile, String folderId, String mimeType) throws IOException {
		// Example code1 - from https://developers.google.com/drive/v3/web/folder
		// String folderId = "0BwwA4oUTeiV1TGRPeTVjaWRDY1E";
		// File fileMetadata = new File();
		// fileMetadata.setName("photo.jpg");
		// fileMetadata.setParents(Collections.singletonList(folderId));
		// java.io.File filePath = new java.io.File("files/photo.jpg");
		// FileContent mediaContent = new FileContent("image/jpeg", filePath);
		// File file = driveService.files().create(fileMetadata, mediaContent).setFields("id, parents").execute();
		// System.out.println("File ID: " + file.getId());

		// Example code2 - https://developers.google.com/drive/v3/web/manage-uploads
		// File fileMetadata = new File();
		// fileMetadata.setName("My Report");
		// fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
		// java.io.File filePath = new java.io.File("files/report.csv");
		// FileContent mediaContent = new FileContent("text/csv", filePath);
		// File file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();
		// System.out.println("File ID: " + file.getId());

		// derive mime type from file extension
		// if (mimeType == null) {
		// List<String> candidateMimeTypes = GoogleDriveHelper.INSTANCE.getCandidateMimeTypes(localFile.getName());
		// if (candidateMimeTypes != null && !candidateMimeTypes.isEmpty()) {
		// mimeType = candidateMimeTypes.get(0);
		// }
		// }

		// Check source file
		if (localFile == null) {
			throw new IllegalArgumentException("Local file is null.");
		}
		if (!localFile.exists()) {
			throw new IllegalArgumentException("Local file '" + localFile.getAbsolutePath() + "' does not exists.");
		}
		if (!localFile.isFile()) {
			throw new IllegalArgumentException("Local file '" + localFile.getAbsolutePath() + "' is not a file.");
		}

		// Check target directory
		if (folderId == null) {
			throw new IllegalArgumentException("folderId is null.");
		}

		if (debug) {
			System.out.println("Upload file '" + localFile.getAbsolutePath() + "'...");
		}

		String fileName = localFile.getName();

		File existingFile = getFileByName(folderId, fileName, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
		if (existingFile == null) {
			// target file not exists
		} else {
			// target file exists
			if (debug) {
				System.out.println("File '" + existingFile.getName() + "' already exists.");
			}
			if (GoogleDriveHelper.INSTANCE.isDirectory(existingFile)) {
				throw new IOException("Target '" + existingFile.getName() + "' exists, but is a directory.");
			}

			// delete existing gdfs target file
			delete(existingFile.getId(), true);
		}

		File fileMetadata = new File();
		fileMetadata.setName(fileName);
		fileMetadata.setParents(Collections.singletonList(folderId));
		fileMetadata.setMimeType(mimeType);

		FileContent fileContent = new FileContent(mimeType, localFile);
		File uploadedFile = getDrive().files().create(fileMetadata, fileContent).setFields(GoogleDriveConstants.FILE_FIELDS_SIMPLE).execute();

		if (debug) {
			System.out.println("File '" + uploadedFile.getName() + "' is uploaded.");
		}
		return uploadedFile;
	}

	/**
	 * Upload a local directory to a google drive folder.
	 * 
	 * @param localFile
	 * @param folderId
	 * @param includingSourceDir
	 * @return
	 * @throws IOException
	 */
	public boolean uploadDirectoryToGdfsDirectory(java.io.File localFile, String folderId, boolean includingSourceDir) throws IOException {
		// check source file
		if (localFile == null) {
			throw new IllegalArgumentException("Local file is null.");
		}
		if (!localFile.exists()) {
			throw new IllegalArgumentException("Local file '" + localFile.getAbsolutePath() + "' does not exists.");
		}
		if (!localFile.isDirectory()) {
			throw new IllegalArgumentException("Local file '" + localFile.getAbsolutePath() + "' is not a directory.");
		}

		// check target directory
		if (folderId == null) {
			throw new IllegalArgumentException("folderId is null.");
		}

		List<java.io.File> encounteredLocalFiles = new ArrayList<java.io.File>();
		if (includingSourceDir) {
			// Copy the local folder into gdfs directory
			boolean succeed = doUploadDirectoryToGdfsDirectory(localFile, folderId, encounteredLocalFiles);
			if (!succeed) {
				return false;
			}

		} else {
			// Copy the sub files in the local folder to gdfs directory
			java.io.File[] subFiles = localFile.listFiles();
			for (java.io.File subFile : subFiles) {
				boolean succeed = doUploadDirectoryToGdfsDirectory(subFile, folderId, encounteredLocalFiles);
				if (!succeed) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param localFile
	 * @param folderId
	 * @param encounteredLocalFiles
	 * @return
	 * @throws IOException
	 */
	protected boolean doUploadDirectoryToGdfsDirectory(java.io.File localFile, String folderId, List<java.io.File> encounteredLocalFiles) throws IOException {
		if (encounteredLocalFiles.contains(localFile)) {
			return true;
		}
		encounteredLocalFiles.add(localFile);

		if (localFile.isDirectory()) {
			// Create a dir in the gdfs dir and copy the local sub files to that dir
			String folderName = localFile.getName();

			File targetDir = getFileByName(folderId, folderName, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
			if (targetDir == null) {
				targetDir = createDirectory(folderId, folderName);
				if (targetDir == null) {
					throw new IOException("Cannot create '" + folderName + "' directory in '" + getFullPathById(folderId) + "'.");
				}
			} else {
				if (!GoogleDriveHelper.INSTANCE.isDirectory(targetDir)) {
					throw new IOException("Target '" + targetDir.getName() + "' exists, but is not a directory.");
				}
			}

			String newFolderId = targetDir.getId();
			java.io.File[] subFiles = localFile.listFiles();
			for (java.io.File subFile : subFiles) {
				boolean succeed = doUploadDirectoryToGdfsDirectory(subFile, newFolderId, encounteredLocalFiles);
				if (!succeed) {
					return false;
				}
			}

		} else if (localFile.isFile()) {
			// Copy the file into the gdfs dir
			File uploadedFile = uploadFileToGdfsDirectory(localFile, folderId, null);
			if (uploadedFile == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param fileId
	 * @param localDir
	 * @throws IOException
	 */
	public boolean downloadGdfsFileToDirectory(String fileId, java.io.File localDir) throws IOException {
		// check source file
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		File file = getFileById(fileId, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
		if (file == null) {
			throw new IllegalArgumentException("File is not found. Id is '" + fileId + "'.");
		}
		return downloadGdfsFileToDirectory(file, localDir);
	}

	/**
	 * 
	 * @param file
	 * @param localDir
	 * @throws IOException
	 */
	public boolean downloadGdfsFileToDirectory(File file, java.io.File localDir) throws IOException {
		// @see https://developers.google.com/drive/v3/web/manage-downloads

		// Example code1:
		// String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
		// OutputStream outputStream = new ByteArrayOutputStream();
		// driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);

		// Example code2:
		// String fileId = "1ZdR3L3qP4Bkq8noWLJHSr_iBau0DNT4Kli4SxNc2YEo";
		// OutputStream outputStream = new ByteArrayOutputStream();
		// driveService.files().export(fileId, "application/pdf").executeMediaAndDownloadTo(outputStream);

		// check source file
		if (file == null) {
			throw new IllegalArgumentException("file is null.");
		}
		if (GoogleDriveHelper.INSTANCE.isDirectory(file)) {
			throw new IllegalArgumentException("File '" + file.getName() + "' exists, but is not a file.");
		}

		// check target directory
		if (localDir == null) {
			throw new IllegalArgumentException("localDir is null.");
		}
		if (!localDir.exists()) {
			localDir.mkdirs();
			if (!localDir.exists()) {
				throw new IOException("Cannot create '" + localDir.getAbsolutePath() + "' directory.");
			}
		} else {
			if (!localDir.isDirectory()) {
				throw new IllegalArgumentException("Target '" + localDir.getName() + "' exists, but is not a directory.");
			}
		}

		// check target file
		java.io.File targetFile = new java.io.File(localDir, file.getName());
		if (!targetFile.exists()) {
			// target file not exists
		} else {
			// target file exists
			if (debug) {
				System.out.println("File '" + targetFile.getAbsolutePath() + "' already exists.");
			}
			if (targetFile.isDirectory()) {
				throw new IOException("Target '" + targetFile.getAbsolutePath() + "' exists, but is a directory.");
			}
		}

		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(targetFile);
			getDrive().files().get(file.getId()).executeMediaAndDownloadTo(outputStream);
			// "Export only supports Google Docs."
			// getDrive().files().export(file.getId(), file.getMimeType()).executeMediaAndDownloadTo(outputStream);

			if (debug) {
				System.out.println("File '" + file.getName() + "' is downloaded into '" + targetFile.getAbsolutePath() + "'.");
			}

		} finally {
			IOUtil.closeQuietly(outputStream, true);
		}

		return true;
	}

	/**
	 * 
	 * @param fileId
	 * @param localDir
	 * @param includingSourceDir
	 * @throws IOException
	 */
	public boolean downloadGdfsDirectoryToDirectory(String fileId, java.io.File localDir, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (fileId == null) {
			throw new IllegalArgumentException("fileId is null.");
		}
		File dir = getFileById(fileId, GoogleDriveConstants.FILE_FIELDS_SIMPLE);
		if (dir == null) {
			throw new IllegalArgumentException("Directory is not found. Id is '" + fileId + "'.");
		}
		return downloadGdfsDirectoryToDirectory(dir, localDir, includingSourceDir);
	}

	/**
	 * 
	 * @param dir
	 * @param localDir
	 * @param includingSourceDir
	 * @throws IOException
	 */
	public boolean downloadGdfsDirectoryToDirectory(File dir, java.io.File localDir, boolean includingSourceDir) throws IOException {
		// Check source directory
		if (dir == null) {
			throw new IllegalArgumentException("dir is null.");
		}
		if (!GoogleDriveHelper.INSTANCE.isDirectory(dir)) {
			throw new IllegalArgumentException("File '" + dir.getName() + "' exists, but is not a directory.");
		}

		// Check target directory
		if (localDir == null) {
			throw new IllegalArgumentException("localDir is null.");
		}
		if (!localDir.exists()) {
			localDir.mkdirs();
			if (!localDir.exists()) {
				throw new IOException("Cannot create '" + localDir.getAbsolutePath() + "' directory.");
			}
		} else {
			if (!localDir.isDirectory()) {
				throw new IllegalArgumentException("Target '" + localDir.getName() + "' exists, but is not a directory.");
			}
		}

		List<String> encounteredGdfsFileIds = new ArrayList<String>();

		if (includingSourceDir) {
			// Copy the gdfs dir into local directory
			doDownloadGdfsDirectoryToDirectory(dir, localDir, encounteredGdfsFileIds);

		} else {
			// Copy the sub files in the gdfs dir to local directory
			List<File> subFiles = getFiles(dir.getId(), GoogleDriveConstants.FILE_FIELDS_SIMPLE, Comparators.GoogleFileComparator.ASC);
			for (File subFile : subFiles) {
				doDownloadGdfsDirectoryToDirectory(subFile, localDir, encounteredGdfsFileIds);
			}
		}

		return true;
	}

	/**
	 * 
	 * @param file
	 * @param localDir
	 * @param encounteredGdfsFileIds
	 * @throws IOException
	 */
	protected void doDownloadGdfsDirectoryToDirectory(File file, java.io.File localDir, List<String> encounteredGdfsFileIds) throws IOException {
		if (file == null || encounteredGdfsFileIds.contains(file.getId())) {
			return;
		}
		encounteredGdfsFileIds.add(file.getId());

		if (GoogleDriveHelper.INSTANCE.isDirectory(file)) {
			// Create a dir in the local dir and copy the gdfs sub files to that dir
			String folderName = file.getName();
			java.io.File newLocalDir = new java.io.File(localDir, folderName);
			if (!newLocalDir.exists()) {
				newLocalDir.mkdirs();
				if (!newLocalDir.exists()) {
					throw new IOException("Cannot create '" + folderName + "' directory in '" + localDir.getAbsolutePath() + "'.");
				}
			} else {
				if (!newLocalDir.isDirectory()) {
					throw new IOException("Target '" + newLocalDir.getAbsolutePath() + "' exists, but is not a directory.");
				}
			}

			List<File> subFiles = getFiles(file.getId(), GoogleDriveConstants.FILE_FIELDS_SIMPLE, Comparators.GoogleFileComparator.ASC);
			for (File subFile : subFiles) {
				doDownloadGdfsDirectoryToDirectory(subFile, newLocalDir, encounteredGdfsFileIds);
			}

		} else {
			// Copy gdfs file into the local dir
			downloadGdfsFileToDirectory(file, localDir);
		}
	}

	/**
	 * Delete a file/directory.
	 * 
	 * @param fileId
	 * @param permanently
	 * @return
	 * @throws IOException
	 */
	public boolean delete(String fileId, boolean permanently) throws IOException {
		Drive drive = getDrive();
		File file = getFileById(fileId, GoogleDriveConstants.FILE_FIELDS_ID_PARENTS);
		if (file != null) {
			if (permanently) {
				// Permanently deletes a file owned by the user without moving it to the trash.
				// If the target is a folder, all descendants owned by the user are also deleted.
				drive.files().delete(fileId).execute();

				if (debug) {
					System.out.println("File is permanently deleted: " + fileId);
				}
				return true;

			} else {
				// TODO: move file to trash. don't know how to do that yet.
			}
		}
		return false;
	}

}
