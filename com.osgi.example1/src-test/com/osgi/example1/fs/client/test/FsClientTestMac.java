package com.osgi.example1.fs.client.test;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.ws.FileSystemUtil;
import com.osgi.example1.fs.client.ws.FileSystemWSClient;
import com.osgi.example1.fs.common.Path;

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
		WSClientConfiguration config = WSClientConfiguration.create(null, "root", "http://127.0.0.1:9090", "/fs/v1");
		return new FileSystemWSClient(config);
	}

	@Test
	public void test001_listRootFiles() throws IOException {
		System.out.println("--- --- --- test001_listRootFiles() --- --- ---");

		try {
			Path[] paths = this.fsClient.listRoots();
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
			Path dir1Path = new Path(Path.ROOT, "dir1");
			Path dir2Path = new Path(Path.ROOT, "dir2");
			Path dir3Path = new Path(Path.ROOT, "dir3");
			Path testPath = new Path(Path.ROOT, "test");

			Path[] subPaths1 = this.fsClient.listFiles(dir1Path);
			Path[] subPaths2 = this.fsClient.listFiles(dir2Path);
			Path[] subPaths3 = this.fsClient.listFiles(dir3Path);
			Path[] subPaths4 = this.fsClient.listFiles(testPath);

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
