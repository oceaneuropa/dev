package org.orbit.fs.test.server.googledrive;

import java.net.URL;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.fs.server.googledrive.GoogleDriveClient;
import org.orbit.fs.server.googledrive.GoogleDriveFSConfig;
import org.orbit.fs.server.googledrive.util.Comparators;
import org.orbit.fs.server.googledrive.util.GoogleDriveConstants;
import org.orbit.fs.server.googledrive.util.GoogleDriveHelper;

import com.google.api.services.drive.model.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoogleDriveClientTest {

	protected GoogleDriveClient client;

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

		this.client = new GoogleDriveClient(config);
	}

	@Test
	public void test001_listFiles() {
		System.out.println("--- --- --- test001_listFiles() --- --- ---");
		try {
			Comparators.GoogleFileComparator comparator = Comparators.GoogleFileComparator.ASC;

			// get root files. then walk through folders
			List<File> rootFiles = this.client.getRootFiles(comparator);
			System.out.println("rootFiles.size() = " + rootFiles.size());
			for (File file : rootFiles) {
				// System.out.println(GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));
				GoogleDriveHelper.INSTANCE.walkthrough(this.client, file, comparator);
			}
			System.out.println();

			// get trashed files. then walk through folders
			List<File> trashedFiles = this.client.getTrashedFiles(comparator);
			System.out.println("trashedFiles.size() = " + trashedFiles.size());
			for (File file : trashedFiles) {
				// System.out.println(GoogleDriveHelper.INSTANCE.getSimpleFileName3(file));
				GoogleDriveHelper.INSTANCE.walkthrough(this.client, file, comparator);
			}
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	@Test
	public void test010_createDirectory() {
		System.out.println("--- --- --- test010_createDirectory() --- --- ---");
		try {
			this.client.createDirectory("Invoices");

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test020_moveFileToDirectory() {
		System.out.println("--- --- --- test020_moveFileToDirectory() --- --- ---");
		try {
			this.client.createDirectory("Invoices");

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
