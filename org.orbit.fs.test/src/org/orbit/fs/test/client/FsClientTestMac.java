package org.orbit.fs.test.client;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.orbit.fs.api.FilePath;
import org.orbit.fs.connector.ws.FileSystemWSClient;
import org.orbit.fs.connector.ws.FileSystemWSClientHelper;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsClientTestMac {

	protected FileSystemWSClient fsClient;

	public FsClientTestMac() {
		this.fsClient = getClient();
	}

	protected void setUp() {
		this.fsClient = getClient();
	}

	protected FileSystemWSClient getClient() {
		ClientConfiguration config = ClientConfiguration.get("http://127.0.0.1:9090", "/fs/v1", "root", "admin");
		return new FileSystemWSClient(config);
	}

	@Test
	public void test001_listRootFiles() throws IOException {
		System.out.println("--- --- --- test001_listRootFiles() --- --- ---");

		try {
			FilePath[] paths = this.fsClient.listRoots();
			for (FilePath path : paths) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fsClient, path, 0);
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Test
	public void test002_listFiles() throws IOException {
		System.out.println("--- --- --- test002_listFiles() --- --- ---");

		try {
			FilePath dir1Path = new FilePath(FilePath.ROOT, "dir1");
			FilePath dir2Path = new FilePath(FilePath.ROOT, "dir2");
			FilePath dir3Path = new FilePath(FilePath.ROOT, "dir3");
			FilePath testPath = new FilePath(FilePath.ROOT, "test");

			FilePath[] subPaths1 = this.fsClient.listFiles(dir1Path);
			FilePath[] subPaths2 = this.fsClient.listFiles(dir2Path);
			FilePath[] subPaths3 = this.fsClient.listFiles(dir3Path);
			FilePath[] subPaths4 = this.fsClient.listFiles(testPath);

			System.out.println("subPaths1.length = " + subPaths1.length);
			System.out.println("subPaths2.length = " + subPaths2.length);
			System.out.println("subPaths3.length = " + subPaths3.length);
			System.out.println("subPaths4.length = " + subPaths4.length);

		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FsClientTestMac.class);
		System.out.println("--- --- --- FsClientTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
