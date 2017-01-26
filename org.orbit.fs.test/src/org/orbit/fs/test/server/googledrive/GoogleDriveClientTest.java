package org.orbit.fs.test.server.googledrive;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.fs.server.googledrive.GoogleDriveClientV3;
import org.orbit.fs.server.googledrive.GoogleDriveFSConfig;
import org.orbit.fs.server.googledrive.util.Comparators;
import org.orbit.fs.server.googledrive.util.GoogleDriveConstants;
import org.orbit.fs.server.googledrive.util.GoogleDriveHelper;

import com.google.api.services.drive.model.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoogleDriveClientTest {

	protected GoogleDriveClientV3 client;

	public GoogleDriveClientTest() {
		setUp();
	}

	public void setUp() {
		// file to store user credentials
		java.io.File dataStoreDir = new java.io.File(System.getProperty("user.home"), ".credentials/GoogleDriveJunitTest");
		// URL url1 = GoogleDriveClientTest.class.getResource("drive-java-quickstart");
		// File dataStoreDir = new File(url1.getPath());

		// client secret file
		URL url2 = GoogleDriveClientTest.class.getResource("client_secret.json");
		java.io.File clientSecretFile = new java.io.File(url2.getPath());

		GoogleDriveFSConfig config = new GoogleDriveFSConfig();
		config.setDataStoreDir(dataStoreDir);
		config.setClientSecretFile(clientSecretFile);
		config.setScopes(GoogleDriveConstants.READ_WRITE_SCOPES);
		config.setApplicationName("GoogleDriveTest");

		this.client = new GoogleDriveClientV3(config);
		try {
			this.client.getDrive();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @Ignore
	@Test
	public void test001_listFiles() {
		System.out.println("--- --- --- test001_listFiles() --- --- ---");
		try {
			String fields = GoogleDriveClientV3.FILE_FIELDS_SIMPLE;

			Comparators.GoogleFileComparator comparator = Comparators.GoogleFileComparator.ASC;

			// get root files. then walk through folders
			List<File> rootFiles = this.client.getFiles(null, fields, comparator);
			System.out.println("rootFiles.size() = " + rootFiles.size());
			for (File file : rootFiles) {
				// System.out.println(GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));
				GoogleDriveHelper.INSTANCE.walkthrough(this.client, file, fields, comparator);
			}
			System.out.println();

			// get trashed files. then walk through folders
			// List<File> trashedFiles = this.client.getTrashedFiles(fields, comparator);
			// System.out.println("trashedFiles.size() = " + trashedFiles.size());
			// for (File file : trashedFiles) {
			// // System.out.println(GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));
			// GoogleDriveHelper.INSTANCE.walkthrough(this.client, file, fields, comparator);
			// }
			// System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test002_getFile() {
		System.out.println("--- --- --- test002_getFile() --- --- ---");
		try {
			String fields = GoogleDriveClientV3.FILE_FIELDS_SIMPLE;

			String driveFolderId = this.client.getDriveFolderId();

			List<String> filePaths = new ArrayList<String>();
			filePaths.add("dir1/test1");
			filePaths.add("dir1/test1/readme1.doc");
			filePaths.add("dir1/test2/readme2.doc");
			filePaths.add("dir1/test3/readme3.doc");
			filePaths.add("dir2/Errors_1.0.0.json");
			filePaths.add("TestCase.zip");

			for (String filePath : filePaths) {
				// System.out.println("filePath = " + filePath);

				File file = this.client.getFileByFullPath(filePath, fields);
				// System.out.println((file == null) ? "file '" + filePath + "' is not found." : "file = " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));

				if (file != null) {
					String fileId = file.getId();

					// get file by id
					// File fileById = this.client.getFileById(fileId, fields);
					// System.out.println((fileById == null) ? "fileById '" + fileId + "' is null." : "fileById is " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(fileById));

					// get full path of the file
					String filePath2 = this.client.getFullPathById(driveFolderId, fileId);

					String message = "file = " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(file) + " (path: " + filePath2 + ")";
					System.out.println(message);

				} else {
					System.out.println("file '" + filePath + "' is not found.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	// @Ignore
	@Test
	public void test010_createDirectory() {
		System.out.println("--- --- --- test010_createDirectory() --- --- ---");
		try {
			// create root folders
			File root1 = this.client.createDirectory(null, "root1");
			File root2 = this.client.createDirectory(null, "root2");

			System.out.println(root1 + " is created.");
			System.out.println(root2 + " is created.");

			// create tmp folders in dir1 folder
			// File dir1 = this.client.getFileByFullPath("dir1", GoogleDriveClientV3.FILE_FIELDS_SIMPLE);
			// if (dir1 != null) {
			// System.out.println(GoogleDriveHelper.INSTANCE.getSimpleFileName3(dir1));
			// String folderId = dir1.getId();
			// File newDir = this.client.createDirectory(folderId, "tmp5");
			// System.out.println("New dir is created: " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(newDir));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test020_moveFileToDirectory() {
		System.out.println("--- --- --- test020_moveFileToDirectory() --- --- ---");
		try {
			String fields = GoogleDriveClientV3.FILE_FIELDS_SIMPLE;

			File file = this.client.getFileByFullPath("dir1/Book1.xsd", GoogleDriveClientV3.FILE_FIELDS_SIMPLE);
			File dir = this.client.getFileByFullPath("dir2", fields);
			// File dir = this.client.getFile("dir2");

			if (file != null && dir != null) {
				System.out.println("file: " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));
				System.out.println("dir: " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(dir));

				File movedFile = this.client.move(file.getId(), dir.getId());
				System.out.println("moved to: " + GoogleDriveHelper.INSTANCE.getSimpleFileName3(movedFile));
			}

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
		Result result = JUnitCore.runClasses(GoogleDriveClientTest.class);
		System.out.println("--- --- --- GoogleDriveClientTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
