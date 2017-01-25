package google.drive.example.v3.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.util.Clock;
import com.google.api.services.drive.model.File;

public class GoogleDriveUtil {

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFolder(File file) {
		return "application/vnd.google-apps.folder".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTextPlain(File file) {
		return "text/plain".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTextXml(File file) {
		return "text/xml".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isJson(File file) {
		return "application/json".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isJpeg(File file) {
		return "image/jpeg".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isPdf(File file) {
		return "application/pdf".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isGoogleDoc(File file) {
		return "application/vnd.google-apps.document".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isGoogleSpreadsheet(File file) {
		return "application/vnd.google-apps.spreadsheet".equals(file.getMimeType()) ? true : false;
	}

	/**
	 * 
	 * @param credential
	 */
	public static void print(Credential credential) {
		String accessToken = credential.getAccessToken();
		String refreshToken = credential.getRefreshToken();
		Long expiresInSeconds = credential.getExpiresInSeconds();
		Clock clock = credential.getClock();
		AccessMethod accessMethod = credential.getMethod();

		System.out.println("accessToken = " + accessToken);
		System.out.println("refreshToken = " + refreshToken);
		System.out.println("expiresInSeconds = " + expiresInSeconds);
		System.out.println("clock = " + clock);
		System.out.println("accessMethod = " + accessMethod);
	}

}
