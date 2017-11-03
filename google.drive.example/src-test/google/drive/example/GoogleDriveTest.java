package google.drive.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;

import com.google.api.client.http.FileContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import google.drive.example.v3.DriveQuickstartUpdated;
import google.drive.example.v3.util.ClientSecretGoogleDriveConnector;
import google.drive.example.v3.util.GoogleDriveConnector;
import google.drive.example.v3.util.GoogleDriveMimeTypes;
import google.drive.example.v3.util.GoogleDriveUtil;
import google.drive.example.v3.util.IOUtil;
import google.drive.example.v3.util.PrettyPrinter;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoogleDriveTest {

	protected static String[] FILE_TITLES = new String[] { "ID", "Kind", "Name", "Mime Type", "Is Empty", "Parents" };

	protected Drive driveService;

	public GoogleDriveTest() {
		try {
			this.driveService = getDriveService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void setUp() {
		try {
			this.driveService = getDriveService();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see https://developers.google.com/identity/protocols/OAuth2
	 * 
	 * @return
	 * @throws IOException
	 */
	public Drive getDriveService() throws IOException {
		String applicationName = "Drive API Java Quickstart";
		InputStream clientSecretInput = DriveQuickstartUpdated.class.getResourceAsStream("/google/drive/example/v3/client_secret.json");
		java.io.File localStoreDir = new java.io.File(System.getProperty("user.home"), ".credentials/drive-java-quickstart.json");
		List<String> scopes = Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_APPDATA, DriveScopes.DRIVE_FILE);
		GoogleDriveConnector connector = new ClientSecretGoogleDriveConnector(applicationName, clientSecretInput, localStoreDir, scopes);
		return connector.getDriveService();
	}

	@Test
	public void test001_listFiles() {
		System.out.println("--- --- --- test001_listFiles() --- --- ---");
		try {
			FileList fileList = this.driveService.files().list().setPageSize(1000).execute();
			List<File> files = fileList.getFiles();
			for (File file : files) {
				// String prettyStr = file.toPrettyString();
				// String id = file.getId();
				// String kind = file.getKind();
				// String mimeType = file.getMimeType();
				// String name = file.getName();
				// boolean isEmpty = file.isEmpty();
				// String fileExt = file.getFileExtension(); // null
				// Capabilities capab = file.getCapabilities(); // null
				// ContentHints cHints = file.getContentHints(); // null
				// User lastModifiedUser = file.getLastModifyingUser(); // null
				// String checksum = file.getMd5Checksum(); // null
				// DateTime dt = file.getCreatedTime(); // null
				// String desc = file.getDescription(); // null
				// long size = file.getSize(); // NPE
				// boolean trashed1 = file.getTrashed(); // NPE
				// boolean trashed2 = file.getExplicitlyTrashed(); // NPE
				// System.out.printf(prettyStr);
				// System.out.printf("id=\"%s\", kind=\"%s\", name=\"%s\", mimeType=\"%s\", isEmpty=\"%s\" \n", id, kind, name, mimeType, isEmpty);
			}

			String[][] rows = new String[files.size()][FILE_TITLES.length];
			int rowIndex = 0;
			for (File file : files) {
				String id = file.getId();
				String kind = file.getKind();
				String mimeType = file.getMimeType();
				String name = file.getName();
				boolean isEmpty = file.isEmpty();
				List<String> parents = file.getParents();
				String parentsString = parents != null ? Arrays.toString(parents.toArray(new String[parents.size()])) : "n/a";
				rows[rowIndex++] = new String[] { id, kind, name, mimeType, String.valueOf(isEmpty), parentsString };
			}
			PrettyPrinter.prettyPrint(FILE_TITLES, rows);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test002_listDirs() {
		System.out.println("--- --- --- test002_listDirs() --- --- ---");
		try {
			FileList fileList = this.driveService.files().list().setPageSize(1000).execute();
			List<File> files = fileList.getFiles();

			List<File> dirs = new ArrayList<File>();
			for (File file : files) {
				if (GoogleDriveUtil.isFolder(file)) {
					dirs.add(file);
				}
			}

			String[][] rows = new String[dirs.size()][FILE_TITLES.length];
			int rowIndex = 0;
			for (File dir : dirs) {
				String id = dir.getId();
				String kind = dir.getKind();
				String mimeType = dir.getMimeType();
				String name = dir.getName();
				boolean isEmpty = dir.isEmpty();
				List<String> parents = dir.getParents();
				String parentsString = parents != null ? Arrays.toString(parents.toArray(new String[parents.size()])) : "n/a";
				rows[rowIndex++] = new String[] { id, kind, name, mimeType, String.valueOf(isEmpty), parentsString };
			}
			PrettyPrinter.prettyPrint(FILE_TITLES, rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Create files: https://developers.google.com/drive/v3/web/integrate-create
	 * 
	 */
	@Ignore
	public void test003_createFiles() {
		System.out.println("--- --- --- test003_createFiles() --- --- ---");
		try {
			// Create a shortcut to a file
			File fileMetadata = new File();
			fileMetadata.setName("Project plan");
			fileMetadata.setMimeType("application/vnd.google-apps.drive-sdk");
			File file = this.driveService.files().create(fileMetadata).setFields("id").execute();
			System.out.println("File ID: " + file.getId());

			// Update file modified time
			// String fileId = "1sTWaJ_j7PkjzaBWtNc3IzovK5hQf21FbOw9yLeeLPNQ";
			String fileId = file.getId();
			File fileMetadata2 = new File();
			fileMetadata2.setModifiedTime(new DateTime(System.currentTimeMillis()));
			File file2 = this.driveService.files().update(fileId, fileMetadata2).setFields("id, modifiedTime").execute();
			System.out.println("Modified time: " + file2.getModifiedTime());

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Upload files: https://developers.google.com/drive/v3/web/manage-uploads
	 * 
	 */
	@Ignore
	public void test004_uploadFiles() {
		System.out.println("--- --- --- test004_uploadFiles() --- --- ---");
		try {
			// File fileMetadata = new File();
			// fileMetadata.setName("My Report");
			// fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
			// java.io.File filePath = new java.io.File("files/report.csv");
			// FileContent mediaContent = new FileContent("text/csv", filePath);
			// File file = driveService.files().create(fileMetadata, mediaContent).setFields("id").execute();
			// System.out.println("File ID: " + file.getId());

			// Upload a txt file
			File fileMetadata1 = new File();
			fileMetadata1.setName("readme1.txt");
			fileMetadata1.setMimeType(GoogleDriveMimeTypes.TEXT_PLAIN);
			java.io.File srcFile1 = new java.io.File("/Users/example/Downloads/test_software/readme1.txt");
			FileContent mediaContent1 = new FileContent(GoogleDriveMimeTypes.TEXT_PLAIN, srcFile1);
			File gdFile1 = this.driveService.files().create(fileMetadata1, mediaContent1).setFields("id").execute();
			// System.out.println("File ID: " + gdFile1.getId());
			System.out.printf("%s is uploaded. File ID is %s.\n", srcFile1, gdFile1.getId());

			// Upload a xsd file
			File fileMetadata2 = new File();
			fileMetadata2.setName("Book.xsd");
			fileMetadata2.setMimeType(GoogleDriveMimeTypes.TEXT_PLAIN);
			java.io.File srcFile2 = new java.io.File("/Users/example/Downloads/test_software/Book.xsd");
			FileContent mediaContent2 = new FileContent(GoogleDriveMimeTypes.TEXT_XML, srcFile2);
			File gdFile2 = this.driveService.files().create(fileMetadata2, mediaContent2).setFields("id").execute();
			// System.out.println("File ID: " + gdFile2.getId());
			System.out.printf("%s is uploaded. File ID is %s.\n", srcFile2, gdFile2.getId());

			// Upload a zip file
			File fileMetadata3 = new File();
			fileMetadata3.setName("uber.wsdl.zip");
			fileMetadata3.setMimeType(GoogleDriveMimeTypes.TEXT_PLAIN);
			java.io.File srcFile3 = new java.io.File("/Users/example/Downloads/test_software/uber.wsdl.zip");
			FileContent mediaContent3 = new FileContent(GoogleDriveMimeTypes.OCTET_STREAM, srcFile3);
			File gdFile3 = this.driveService.files().create(fileMetadata3, mediaContent3).setFields("id").execute();
			// System.out.println("File ID: " + gdFile3.getId());
			System.out.printf("%s is uploaded. File ID is %s.\n", srcFile3, gdFile3.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * Download files: https://developers.google.com/drive/v3/web/manage-downloads
	 * 
	 */
	@Ignore
	public void test005_downloadFiles() {
		System.out.println("--- --- --- test005_downloadFiles() --- --- ---");

		FileOutputStream output1 = null;
		FileOutputStream output2 = null;
		FileOutputStream output3 = null;
		try {
			// Sample code
			// String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
			// OutputStream outputStream = new ByteArrayOutputStream();
			// driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);

			// Download text file
			String fileId1 = "0B6KJ1gH-MjKgMHlfZnJRSTNLclU";
			java.io.File targetFile1 = new java.io.File("/Users/example/Downloads/test_software2/readme1.txt");
			output1 = new FileOutputStream(targetFile1);
			this.driveService.files().get(fileId1).executeMediaAndDownloadTo(output1);
			System.out.printf("%s is downloaded.\n", targetFile1);

			// Download xsd file
			String fileId2 = "0B6KJ1gH-MjKgcXp0bm84VGVvak0";
			java.io.File targetFile2 = new java.io.File("/Users/example/Downloads/test_software2/Book.xsd");
			output2 = new FileOutputStream(targetFile2);
			this.driveService.files().get(fileId2).executeMediaAndDownloadTo(output2);
			System.out.printf("%s is downloaded.\n", targetFile2);

			// Download zip file
			String fileId3 = "0B6KJ1gH-MjKgUUhrQV9RbUxoN3c";
			java.io.File targetFile3 = new java.io.File("/Users/example/Downloads/test_software2/uber.wsdl.zip");
			output3 = new FileOutputStream(targetFile3);
			this.driveService.files().get(fileId3).executeMediaAndDownloadTo(output3);
			System.out.printf("%s is downloaded.\n", targetFile3);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output1, true);
			IOUtil.closeQuietly(output2, true);
			IOUtil.closeQuietly(output3, true);
		}
		System.out.println();
	}

	/**
	 * Export files: https://developers.google.com/drive/v3/web/integrate-open
	 * 
	 * Export files: https://developers.google.com/drive/v3/web/manage-downloads
	 * 
	 */
	@Ignore
	public void test006_exportFiles() {
		System.out.println("--- --- --- test006_exportFiles() --- --- ---");

		FileOutputStream output1 = null;
		FileOutputStream output2 = null;
		try {
			// Sample code
			// String fileId = "1ZdR3L3qP4Bkq8noWLJHSr_iBau0DNT4Kli4SxNc2YEo";
			// OutputStream outputStream = new ByteArrayOutputStream();
			// driveService.files().export(fileId, "application/pdf").executeMediaAndDownloadTo(outputStream);

			// Export google document to local PDF file
			String fileId1 = "1aJYS4JCIUNn0R2wZc39zzxMSKK-Lb6vlJqu2G4H3cgI";
			java.io.File targetFile1 = new java.io.File("/Users/example/Downloads/test_software2/TestGoogDoc.pdf");
			output1 = new FileOutputStream(targetFile1);
			this.driveService.files().export(fileId1, GoogleDriveMimeTypes.PDF).executeMediaAndDownloadTo(output1);
			System.out.printf("%s is exported.\n", targetFile1);

			// Export google spreadsheet to local PDF file
			String fileId2 = "1t3PAzSmJv68vpkCWsFbQjFU2oAytMb8BY5pdo9Wn5eY";
			java.io.File targetFile2 = new java.io.File("/Users/example/Downloads/test_software2/PowerBall.pdf");
			output2 = new FileOutputStream(targetFile2);
			this.driveService.files().export(fileId2, GoogleDriveMimeTypes.PDF).executeMediaAndDownloadTo(output2);
			System.out.printf("%s is exported.\n", targetFile2);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output1, true);
			IOUtil.closeQuietly(output2, true);
		}
		System.out.println();
	}

	/**
	 * Delete files.
	 * 
	 */
	@Ignore
	public void test007_deleteFiles() {
		System.out.println("--- --- --- test005_downloadFiles() --- --- ---");
		try {
			// Delete text file
			String fileId1 = "0B6KJ1gH-MjKgMHlfZnJRSTNLclU";
			File gdFile1 = this.driveService.files().get(fileId1).execute();
			String fileName1 = gdFile1.getName();
			this.driveService.files().delete(fileId1).execute();
			System.out.printf("%s is deleted.\n", fileName1);

			// Delete xsd file
			String fileId2 = "0B6KJ1gH-MjKgcXp0bm84VGVvak0";
			File gdFile2 = this.driveService.files().get(fileId2).execute();
			String fileName2 = gdFile2.getName();
			this.driveService.files().delete(fileId2).execute();
			System.out.printf("%s is deleted.\n", fileName2);

			// Delete zip file
			String fileId3 = "0B6KJ1gH-MjKgUUhrQV9RbUxoN3c";
			File gdFile3 = this.driveService.files().get(fileId3).execute();
			String fileName3 = gdFile3.getName();
			this.driveService.files().delete(fileId3).execute();
			System.out.printf("%s is deleted.\n", fileName3);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(GoogleDriveTest.class);
		System.out.println("--- --- --- GoogleDriveTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
