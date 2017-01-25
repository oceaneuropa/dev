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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class GoogleDriveClient {

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
	public GoogleDriveClient(GoogleDriveFSConfig config) {
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
	 * List all files.
	 * 
	 * @param drive
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getAllFiles(Comparator<File> comparator) throws IOException {
		return doGetFiles(null, comparator);
	}

	/**
	 * List root files.
	 * 
	 * @param drive
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getRootFiles(Comparator<File> comparator) throws IOException {
		return doGetFiles("'root' in parents and trashed = false", comparator);
	}

	/**
	 * List files in parent directory.
	 * 
	 * @param parent
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getFiles(File parent, Comparator<File> comparator) throws IOException {
		return getFiles(parent.getId(), comparator);
	}

	/**
	 * List files in parent directory.
	 * 
	 * @param parentId
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getFiles(String parentId, Comparator<File> comparator) throws IOException {
		return doGetFiles("'" + parentId + "' in parents and trashed = false", comparator);
	}

	/**
	 * List trashed files.
	 * 
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> getTrashedFiles(Comparator<File> comparator) throws IOException {
		return doGetFiles("trashed = true", comparator);
	}

	public File getFileById(String fileId) throws IOException {
		File file = null;
		List<File> files = doGetFiles("trashed = true", Comparators.GoogleFileComparator.ASC);
		return file;
	}

	public File getFile(String filePath) {
		File file = null;
		FilePath path = new FilePath(filePath);
		String[] pathSegments = path.getSegments();
		return file;
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
	 * @param comparator
	 * @return
	 * @throws IOException
	 */
	public List<File> doGetFiles(String query, Comparator<File> comparator) throws IOException {
		List<File> files = new ArrayList<File>();

		Drive.Files.List list = getDrive().files().list();
		if (query != null) {
			list.setQ(query);
		}
		FileList fileListObj = list.execute();

		if (fileListObj != null) {
			List<File> fileList = fileListObj.getFiles();
			if (fileList != null) {
				for (File file : fileList) {
					files.add(file);
				}
			}
		}

		if (comparator != null && !files.isEmpty()) {
			Collections.sort(files, comparator);
		}

		return files;
	}

	/**
	 * Create a root folder.
	 * 
	 * @param drive
	 * @param name
	 * @throws IOException
	 */
	public void createDirectory(String name) throws IOException {
		File fileMetadata = new File();
		fileMetadata.setName(name);
		fileMetadata.setMimeType(GoogleDriveMimeTypes.FOLDER);
		File file = getDrive().files().create(fileMetadata).setFields("id").execute();
		if (debug) {
			System.out.println("Folder ID: " + file.getId());
		}
	}

	/**
	 * 
	 * @param file
	 * @param targetDirectory
	 * @throws IOException
	 */
	public void move(File file, File targetDirectory) throws IOException {
		move(file, targetDirectory.getId());
	}

	/**
	 * 
	 * @param file
	 * @param targetDirectoryId
	 * @throws IOException
	 */
	public void move(File file, String targetDirectoryId) throws IOException {
		Drive drive = getDrive();
		String fileId = file.getId();
		StringBuilder previousParents = new StringBuilder();
		for (String parent : file.getParents()) {
			previousParents.append(parent);
			previousParents.append(',');
		}
		// Move the file to the new folder
		file = drive.files().update(fileId, null).setAddParents(targetDirectoryId).setRemoveParents(previousParents.toString()).setFields("id, parents").execute();
	}

}
