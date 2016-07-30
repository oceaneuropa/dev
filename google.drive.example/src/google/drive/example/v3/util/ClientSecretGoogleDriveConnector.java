package google.drive.example.v3.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
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
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;

/*
 * @see https://developers.google.com/identity/protocols/OAuth2
 * 
 * @see https://console.developers.google.com/iam-admin/projects
 * @see https://console.developers.google.com/ 
 * @see https://developers.google.com/oauthplayground/
 * 
 * @see https://developers.google.com/drive/v3/web/quickstart/java
 * @see https://developers.google.com/drive/v3/web/integrate-create
 * @see https://developers.google.com/drive/v3/web/manage-uploads
 * @see https://developers.google.com/drive/v3/web/integrate-open (export google documents as PDF files)
 * @see https://developers.google.com/drive/v3/web/manage-downloads
 * 
 * @see https://developers.google.com/drive/v3/web/mime-types
 * @see https://developers.google.com/drive/v3/web/about-auth?hl=en_US
 * 
 */
public class ClientSecretGoogleDriveConnector implements GoogleDriveConnector {

	protected String applicationName;
	protected InputStream clientSecretInput;
	protected File localDataStoreDir;
	protected List<String> scopes;

	protected DataStoreFactory dataStoreFactory;
	protected HttpTransport httpTransport;
	protected JsonFactory jsonFactory;

	/**
	 * 
	 * @param applicationName
	 * @param clientSecretFile
	 * @param localDataStoreDir
	 * @param scopes
	 */
	public ClientSecretGoogleDriveConnector(String applicationName, File clientSecretFile, File localDataStoreDir, List<String> scopes) {
		this.applicationName = applicationName;
		try {
			byte[] bytes = IOUtil.toByteArray(clientSecretFile);
			if (bytes != null) {
				this.clientSecretInput = IOUtil.toInputStream(bytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.localDataStoreDir = localDataStoreDir;
		this.scopes = scopes;
	}

	/**
	 * 
	 * @param applicationName
	 * @param clientSecretInput
	 * @param localDataStoreDir
	 * @param scopes
	 */
	public ClientSecretGoogleDriveConnector(String applicationName, InputStream clientSecretInput, File localDataStoreDir, List<String> scopes) {
		this.applicationName = applicationName;
		this.clientSecretInput = clientSecretInput;
		this.localDataStoreDir = localDataStoreDir;
		this.scopes = scopes;
	}

	protected String getApplicationName() {
		return this.applicationName;
	}

	/**
	 * Directory to store user credentials for this application.
	 * 
	 * @return
	 */
	public File getDataStoreDir() {
		return this.localDataStoreDir;
	}

	@Override
	public List<String> getScopes() {
		return this.scopes;
	}

	@Override
	public Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(getHttpTransport(), getJsonFactory(), credential).setApplicationName(getApplicationName()).build();
	}

	protected Credential authorize() throws IOException {
		// Load client secrets.
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(getJsonFactory(), new InputStreamReader(this.clientSecretInput));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(getHttpTransport(), getJsonFactory(), clientSecrets, getScopes()).setDataStoreFactory(getDataStoreFactory()).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + getDataStoreDir().getAbsolutePath());
		return credential;
	}

	protected synchronized DataStoreFactory getDataStoreFactory() throws IOException {
		if (this.dataStoreFactory == null) {
			this.dataStoreFactory = new FileDataStoreFactory(getDataStoreDir());
		}
		return this.dataStoreFactory;
	}

	protected synchronized HttpTransport getHttpTransport() {
		if (this.httpTransport == null) {
			try {
				this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			} catch (GeneralSecurityException | IOException e) {
				e.printStackTrace();
			}
		}
		return this.httpTransport;
	}

	protected synchronized JsonFactory getJsonFactory() {
		if (this.jsonFactory == null) {
			this.jsonFactory = JacksonFactory.getDefaultInstance();
		}
		return this.jsonFactory;
	}

}
