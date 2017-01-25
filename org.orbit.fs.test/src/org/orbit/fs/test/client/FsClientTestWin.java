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
public class FsClientTestWin {

	protected FileSystemWSClient fsClient;

	public FsClientTestWin() {
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
			FilePath dir1Path = new FilePath(FilePath.ROOT, "test/dir1");
			FilePath dir2Path = new FilePath(FilePath.ROOT, "test/dir2");
			FilePath dir3Path = new FilePath(FilePath.ROOT, "test/dir3");
			FilePath dir5Path = new FilePath(FilePath.ROOT, "test/dir5/07_Document/patent");

			FilePath[] subPaths1 = this.fsClient.listFiles(dir1Path);
			FilePath[] subPaths2 = this.fsClient.listFiles(dir2Path);
			FilePath[] subPaths3 = this.fsClient.listFiles(dir3Path);
			FilePath[] subPaths5 = this.fsClient.listFiles(dir5Path);

			System.out.println("dir1Path = " + dir1Path.getPathString());
			for (FilePath subPath1 : subPaths1) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fsClient, subPath1, 0);
			}
			System.out.println();

			System.out.println("dir2Path = " + dir2Path.getPathString());
			for (FilePath subPath2 : subPaths2) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fsClient, subPath2, 0);
			}
			System.out.println();

			System.out.println("dir3Path = " + dir3Path.getPathString());
			for (FilePath subPath3 : subPaths3) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fsClient, subPath3, 0);
			}
			System.out.println();

			System.out.println("dir5Path = " + dir5Path.getPathString());
			for (FilePath subPath5 : subPaths5) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fsClient, subPath5, 0);
			}
			System.out.println();
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
		Result result = JUnitCore.runClasses(FsClientTestWin.class);
		System.out.println("--- --- --- FsClientTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
