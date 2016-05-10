package com.osgi.example1.fs.client.test;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.rest.client.ClientException;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsTestWin {

	protected FileSystem fs;

	public FsTestWin() {
		this.fs = getFileSystem();
	}

	protected void setUp() {
		this.fs = getFileSystem();
	}

	protected FileSystem getFileSystem() {
		FileSystemConfiguration config = new FileSystemConfiguration("http://127.0.0.1:9090", "/fs/v1", "root", "admin");
		return FileSystem.newInstance(config);
	}

	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

		try {
			FileRef[] files = FileRef.listRoots(fs);
			for (FileRef file : files) {
				FileSystemUtil.walkFolders(fs, file, 0);
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
			FileRef dir1 = FileRef.newInstance(fs, "/test/dir1");
			FileRef dir2 = FileRef.newInstance(fs, "/test/dir2");
			FileRef dir3 = FileRef.newInstance(fs, "/test/dir3");
			FileRef dir5 = FileRef.newInstance(fs, fs.root(), "test/dir5/07_Document/patent");

			FileRef[] subFiles1 = FileRef.listFiles(dir1);
			FileRef[] subFiles2 = FileRef.listFiles(dir2);
			FileRef[] subFiles3 = FileRef.listFiles(dir3);
			FileRef[] subFiles5 = FileRef.listFiles(dir5);

			System.out.println("dir1 = " + dir1.getPath());
			for (FileRef subFile1 : subFiles1) {
				FileSystemUtil.walkFolders(fs, subFile1, 0);
			}
			System.out.println();

			System.out.println("dir2 = " + dir2.getPath());
			for (FileRef subFile2 : subFiles2) {
				FileSystemUtil.walkFolders(fs, subFile2, 0);
			}
			System.out.println();

			System.out.println("dir3 = " + dir3.getPath());
			for (FileRef subFile3 : subFiles3) {
				FileSystemUtil.walkFolders(fs, subFile3, 0);
			}
			System.out.println();

			System.out.println("dir5 = " + dir5.getPath());
			for (FileRef subFile5 : subFiles5) {
				FileSystemUtil.walkFolders(fs, subFile5, 0);
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
		Result result = JUnitCore.runClasses(FsTestWin.class);
		System.out.println("--- --- --- FsTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
