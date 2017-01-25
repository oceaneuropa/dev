package google.drive.example.v3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import google.drive.example.v3.util.GoogleDriveUtil;

/**
 * https://developers.google.com/drive/v3/web/quickstart/java#step_3_set_up_the_sample
 * 
 * Project name: yangyang4j-project1
 * 
 * Project id: yangyang4j-project1
 * 
 * Product name: Drive API Example
 * 
 * Download credentials
 * 
 * Client ID: 717580645461-bo64j8vkjbb70020901ujoo5nts3lvkh.apps.googleusercontent.com
 * 
 */
public class DriveQuickstartUpdatedV2 {

	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at ~/.credentials/drive-java-quickstart.json
	 */
	public static final List<String> READ_ONLY_SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);
	public static final List<String> READ_WRITE_SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);

	// file to store user credentials
	protected File dataStoreFile;
	// client secret file
	protected File clientSecretFile;
	protected List<String> scopes;
	protected HttpTransport httpTransport;
	protected JsonFactory jsonFactory;
	protected FileDataStoreFactory dataStoreFactory;

	/**
	 * 
	 * @param dataStoreFile
	 * @param clientSecretFile
	 * @param scopes
	 */
	public DriveQuickstartUpdatedV2(File dataStoreFile, File clientSecretFile, List<String> scopes) {
		this.dataStoreFile = dataStoreFile;
		this.clientSecretFile = clientSecretFile;
		this.scopes = scopes;
		try {
			this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			this.jsonFactory = JacksonFactory.getDefaultInstance();

			this.dataStoreFactory = new FileDataStoreFactory(dataStoreFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public Credential authorize() throws IOException {
		InputStream in = null;
		try {
			// Load client secrets.
			in = new FileInputStream(this.clientSecretFile);
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(this.httpTransport, jsonFactory, clientSecrets, this.scopes).setDataStoreFactory(this.dataStoreFactory).setAccessType("offline").build();
			Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			System.out.println("Credentials saved to " + dataStoreFile.getAbsolutePath());
			return credential;

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
	public Drive getDrive() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(this.httpTransport, this.jsonFactory, credential).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * 
	 * @param driver
	 * @return
	 * @throws IOException
	 */
	public List<com.google.api.services.drive.model.File> getFiles(Drive driver) throws IOException {
		List<com.google.api.services.drive.model.File> files = new ArrayList<com.google.api.services.drive.model.File>();
		com.google.api.services.drive.model.FileList fileListObj = driver.files().list().setPageSize(1000).execute();
		if (fileListObj != null) {
			List<com.google.api.services.drive.model.File> fileList = fileListObj.getFiles();
			if (fileList != null) {
				for (com.google.api.services.drive.model.File file : fileList) {
					files.add(file);
				}
			}
		}
		return files;
	}

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String user_home = System.getProperty("user.home");
		System.out.println("user.home = " + user_home);

		// file to store user credentials
		File dataStoreFile = new File(System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");
		// URL url1 = DriveQuickstartUpdatedV2.class.getResource("drive-java-quickstart.json");
		// File dataStoreFile = new File(url1.getPath());

		// client secret file
		URL url2 = DriveQuickstartUpdatedV2.class.getResource("client_secret.json");
		File clientSecretFile = new File(url2.getPath());

		DriveQuickstartUpdatedV2 client = new DriveQuickstartUpdatedV2(dataStoreFile, clientSecretFile, READ_WRITE_SCOPES);
		Drive driver = client.getDrive();
		List<com.google.api.services.drive.model.File> files = client.getFiles(driver);

		System.out.println("Files: " + files.size());
		for (com.google.api.services.drive.model.File file : files) {
			if (!GoogleDriveUtil.isFolder(file)) {
				// continue;
			}
			if (!GoogleDriveUtil.isTextPlain(file)) {
				// continue;
			}

			// System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
			String id = file.getId();
			String name = file.getName();
			DateTime dt = file.getCreatedTime();
			String desc = file.getDescription();
			String prettyStr = file.toPrettyString();
			String mimeType = file.getMimeType();
			// System.out.printf("%s (%s)\n", name, mimeType);
			System.out.println(prettyStr);
		}

		// ------------------------------------------------------------------------------------------------
		// Create a folder
		// ------------------------------------------------------------------------------------------------
		// https://console.developers.google.com/iam-admin/projects
		// https://console.developers.google.com/
		// https://developers.google.com/oauthplayground/

		// https://developers.google.com/drive/v3/web/mime-types
		// application/vnd.google-apps.audio
		// application/vnd.google-apps.document
		// application/vnd.google-apps.drawing
		// application/vnd.google-apps.file
		// application/vnd.google-apps.folder
		// application/vnd.google-apps.form
		// application/vnd.google-apps.fusiontable
		// application/vnd.google-apps.map
		// application/vnd.google-apps.photo
		// application/vnd.google-apps.presentation
		// application/vnd.google-apps.script
		// application/vnd.google-apps.sites
		// application/vnd.google-apps.spreadsheet
		// application/vnd.google-apps.unknown
		// application/vnd.google-apps.video

		// https://developers.google.com/drive/v3/web/about-auth?hl=en_US
		// Full, permissive scope to access all of a user's files. Request this scope only when it is strictly necessary.
		// https://www.googleapis.com/auth/drive
		// Allows read-only access to file metadata and file content
		// https://www.googleapis.com/auth/drive.readonly
		// Allows access to the Application Data folder
		// https://www.googleapis.com/auth/drive.appfolder
		// Per-file access to files created or opened by the app
		// https://www.googleapis.com/auth/drive.file
		// Special scope used to let users approve installation of an app.
		// https://www.googleapis.com/auth/drive.install
		// Allows read-write access to file metadata, but does not allow any access to read, download,
		// write or upload file content. Does not support file creation, trashing or deletion. Also does not allow changing folders or sharing in
		// order to prevent access escalation.
		// https://www.googleapis.com/auth/drive.metadata
		// Allows read-only access to file metadata, but does not allow any access to read or download file content
		// https://www.googleapis.com/auth/drive.metadata.readonly
		// Allows read-only access to all photos. The spaces parameter must be set to photos.
		// https://www.googleapis.com/auth/drive.photos.readonly
		// https://www.googleapis.com/auth/drive.scripts

		// File fileMetadata = new File();
		// fileMetadata.setName("Invoices");
		// fileMetadata.setMimeType("application/vnd.google-apps.folder");
		// File file = driveService.files().create(fileMetadata).setFields("id").execute();
		// System.out.println("Folder ID: " + file.getId());

		// File metadata1 = new File();
		// metadata1.setKind("drive#file");
		// metadata1.setName("test1.txt");
		// metadata1.setMimeType("text/plain");
		// File newFile = driveService.files().create(metadata1).execute();
		// System.out.println("New File: " + newFile.getId());
	}

}
