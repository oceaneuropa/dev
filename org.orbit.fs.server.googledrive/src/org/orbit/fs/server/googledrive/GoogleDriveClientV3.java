package org.orbit.fs.server.googledrive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.orbit.fs.api.FilePath;
import org.orbit.fs.server.googledrive.util.Comparators;
import org.orbit.fs.server.googledrive.util.GoogleDriveHelper;
import org.orbit.fs.server.googledrive.util.GoogleDriveMimeTypes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
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
 * @see https://mvnrepository.com/artifact/com.google.apis
 * 
 * Aboout Fields and query
 * https://developers.google.com/drive/v3/web/search-parameters
 * 
 * Online API doc
 * https://developers.google.com/apis-explorer/#p/drive/v3/
 * 
 * Java API doc
 * @see https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/model/File.html
 * @see https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.Files.Create.html
 * @see https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.Files.List.html
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class GoogleDriveClientV3 {

	public static final String FILE_FIELDS_SIMPLE = "id,parents,name,mimeType,trashed";
	public static final String FILE_FIELDS_ALL = "appProperties,capabilities,contentHints,createdTime,description,explicitlyTrashed,fileExtension,folderColorRgb,fullFileExtension,hasThumbnail,headRevisionId,iconLink,id,imageMediaMetadata,isAppAuthorized,kind,lastModifyingUser,md5Checksum,mimeType,modifiedByMe,modifiedByMeTime,modifiedTime,name,originalFilename,ownedByMe,owners,parents,permissions,properties,quotaBytesUsed,shared,sharedWithMeTime,sharingUser,size,spaces,starred,thumbnailLink,thumbnailVersion,trashed,version,videoMediaMetadata,viewedByMe,viewedByMeTime,viewersCanCopyContent,webContentLink,webViewLink,writersCanShare";

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
		List<File> subFiles = getFiles(null, FILE_FIELDS_SIMPLE, null);
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
				File currParentFile = getFileById(parentId, FILE_FIELDS_SIMPLE);
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
	 * Get file by file full path string.
	 * 
	 * @param fullPath
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public File getFileByFullPath(String fullPath, String fields) throws IOException {
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

		String fields = FILE_FIELDS_SIMPLE;

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
	 * @see http://www.programcreek.com/java-api-examples/index.php?source_dir=mytracks-master/MyTracks/src/com/google/android/apps/mytracks/io/sync/SyncUtils.java
	 * 
	 * @param drive
	 * @param name
	 * @throws IOException
	 */
	public File createDirectory(String folderId, String name) throws IOException {
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
			System.out.println("Folder ID: " + createdDir.getId());
		}

		return createdDir;
	}

	/**
	 * Move a file to a directory.
	 * 
	 * @param fileId
	 *            file to be moved
	 * @param targetFolderId
	 *            directory to be moved into
	 * @return
	 * @throws IOException
	 */
	public File move(String fileId, String targetFolderId) throws IOException {
		Drive drive = getDrive();

		StringBuilder parentIdsToRemove = new StringBuilder();
		// File fileWithParents = drive.files().get(fileId).setFields("parents").execute();
		File fileWithParents = getFileById(fileId, FILE_FIELDS_SIMPLE);
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

}
