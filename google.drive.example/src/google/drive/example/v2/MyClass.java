package google.drive.example.v2;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * https://developers.google.com/drive/v2/reference/files/get#examples
 * 
 */
public class MyClass {

	public static void printFileS(Drive service) {
		try {
			Files filesObj = service.files();
			// FileList result = filesObj.list().setFields("nextPageToken, files(id, name)").execute();
			FileList result = filesObj.list().execute();
			// java.util.List<File> files = result.getItems();
			java.util.List<File> files = result.getFiles();

			// com.google.api.services.drive.Drive,Files.List fileList = (com.google.api.services.drive.Drive,Files.List) filesObj.list();
			// FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)").execute();
			// java.util.List<File> files = result.getFiles();

			System.out.println("Files:");
			boolean doList1 = true;
			boolean doList2 = false;

			if (doList1) {
				for (File file : files) {
					// System.out.printf("%s (%s)\n", file.getTitle(), file.getId());
					System.out.printf("%s (%s)\n", file.getName(), file.getId());
				}
			}

			if (doList2) {
				for (File file : files) {
					String id = file.getId();
					// String title = file.getTitle();
					String title = file.getName();
					String fileExtension = file.getFileExtension();
					// Long fileSize = file.getFileSize();
					String mimeType = file.getMimeType();
					String kind = file.getKind();
					// String downloadUrl = file.getDownloadUrl();
					String descrition = file.getDescription();
					// DateTime createdData = file.getCreatedDate();
					// DateTime modifiedData = file.getModifiedDate();

					// System.out.println(MessageFormat.format( //
					// "id=''{0}'', title=''{1}'', fileExtension=''{2}'', fileSize=''{3}'', mimeType=''{4}'', kind=''{5}'', downloadUrl=''{6}'',
					// descrition=''{7}'', createdData=''{8}'', modifiedData=''{9}''", //
					// new Object[] { id, title, fileExtension, fileSize, mimeType, kind, downloadUrl, descrition, createdData, modifiedData } //
					// ));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print a file's metadata.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param fileId
	 *            ID of the file to print metadata for.
	 */
	public static void printFile(Drive service, String fileId) {
		try {
			File file = service.files().get(fileId).execute();

			// System.out.println("Title: " + file.getTitle());
			System.out.println("Title: " + file.getName());
			System.out.println("Description: " + file.getDescription());
			System.out.println("MIME type: " + file.getMimeType());
		} catch (IOException e) {
			System.out.println("An error occured: " + e);
		}
	}

	/**
	 * Download a file's content.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param file
	 *            Drive File instance.
	 * @return InputStream containing the file's content if successful, {@code null} otherwise.
	 */
	private static InputStream downloadFile(Drive service, File file) {
//		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
//			try {
//				HttpResponse resp = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
//				return resp.getContent();
//			} catch (IOException e) {
//				// An error occurred.
//				e.printStackTrace();
//				return null;
//			}
//		} else {
//			// The file doesn't have any content stored on Drive.
//			return null;
//		}
		return null;
	}

}