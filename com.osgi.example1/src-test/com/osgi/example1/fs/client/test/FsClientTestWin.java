package com.osgi.example1.fs.client.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.ws.FileSystemClient;
import com.osgi.example1.fs.client.ws.FileSystemUtil;
import com.osgi.example1.fs.common.Path;

public class FsClientTestWin {

	protected FileSystemClient fsClient;

	public FsClientTestWin() {
		this.fsClient = getClient();
	}

	protected void setUp() {
		this.fsClient = getClient();
	}

	protected FileSystemClient getClient() {
		ClientConfiguration config = ClientConfiguration.get("http://127.0.0.1:9090", "/fs/v1", "root", "admin");
		return new FileSystemClient(config);
	}

	@Test
	public void test001_listRootFiles() throws IOException {
		System.out.println("--- --- --- test001_listRootFiles() --- --- ---");

		try {
			Path[] paths = this.fsClient.listRootFiles();
			for (Path path : paths) {
				FileSystemUtil.walkFolders(fsClient, path, 0);
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
			Path dir1Path = new Path(Path.ROOT, "test/dir1");
			Path dir2Path = new Path(Path.ROOT, "test/dir2");
			Path dir3Path = new Path(Path.ROOT, "test/dir3");
			Path dir5Path = new Path(Path.ROOT, "test/dir5/07_Document/patent");

			Path[] subPaths1 = this.fsClient.listFiles(dir1Path);
			Path[] subPaths2 = this.fsClient.listFiles(dir2Path);
			Path[] subPaths3 = this.fsClient.listFiles(dir3Path);
			Path[] subPaths5 = this.fsClient.listFiles(dir5Path);

			System.out.println("dir1Path = " + dir1Path.getPathString());
			for (Path subPath1 : subPaths1) {
				FileSystemUtil.walkFolders(fsClient, subPath1, 0);
			}
			System.out.println();

			System.out.println("dir2Path = " + dir2Path.getPathString());
			for (Path subPath2 : subPaths2) {
				FileSystemUtil.walkFolders(fsClient, subPath2, 0);
			}
			System.out.println();

			System.out.println("dir3Path = " + dir3Path.getPathString());
			for (Path subPath3 : subPaths3) {
				FileSystemUtil.walkFolders(fsClient, subPath3, 0);
			}
			System.out.println();

			System.out.println("dir5Path = " + dir5Path.getPathString());
			for (Path subPath5 : subPaths5) {
				FileSystemUtil.walkFolders(fsClient, subPath5, 0);
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
