package google.drive.example.v3.util;

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
	public static boolean isJpeg(File file) {
		return "image/jpeg".equals(file.getMimeType()) ? true : false;
	}

}
