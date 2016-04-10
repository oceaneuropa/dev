package google.drive.example.v2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * http://stackoverflow.com/questions/32476746/com-google-api-client-googleapis-json-googlejsonresponseexception-403-forbidden
 * 
 */
public class DriveCommandLine {

	private static String CLIENT_ID = "CLIENT_ID";
	private static String CLIENT_SECRET = "CLIENT_SECRET";
	private static String REDIRECT_URI = "REDIRECT_URI";

	public static void main(String[] args) throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE_FILE)).setAccessType("online").setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
		System.out.println("Please open the following URL in your browser then type the authorization code:");
		System.out.println("  " + url);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();

		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		// Create a new authorized API client
		Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

		// Insert a file
		File body = new File();
		// body.setTitle("My document");
		body.setName("My document");
		body.setDescription("A test document");
		body.setMimeType("text/plain");

		java.io.File fileContent = new java.io.File("/path/to/TextFile.txt");
		FileContent mediaContent = new FileContent("text/plain", fileContent);

		// File file = service.files().insert(body, mediaContent).execute();
		File file = service.files().create(body, mediaContent).execute();
		System.out.println("File ID: " + file.getId());
	}

}
