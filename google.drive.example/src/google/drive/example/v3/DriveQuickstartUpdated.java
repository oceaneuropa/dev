package google.drive.example.v3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

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
public class DriveQuickstartUpdated {
	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at ~/.credentials/drive-java-quickstart.json
	 */
	// private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA,
	// DriveScopes.DRIVE_FILE);
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		// InputStream in = DriveQuickstart.class.getResourceAsStream("/client_secret.json"); // load from "/bin/client_secret.json"
		InputStream in = DriveQuickstartUpdated.class.getResourceAsStream("/google/drive/example/v3/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	public static void main(String[] args) throws IOException {
		// Build a new authorized API client service.
		Drive driveService = getDriveService();
		// MyClass.printFileS(service);

		// ------------------------------------------------------------------------------------------------
		// List all files
		// ------------------------------------------------------------------------------------------------
		// Print the names and IDs for up to 10 files.
		// FileList result = service.files().list().setFields("nextPageToken, files(id, name)").execute();
		// List<File> files = result.getItems();
		FileList result = driveService.files().list().setPageSize(1000).execute();

		List<File> files = result.getFiles();
		if (files == null || files.size() == 0) {
			System.out.println("No files found.");
			return;
		}

		System.out.println("Files: " + files.size());
		for (File file : files) {
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
			System.out.printf("%s (%s)\n", name, mimeType);
			// System.out.printf(prettyStr);
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
