package com.osgi.example1.fs.client.test;

import java.io.IOException;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FsTestMac {

	protected FileSystem fs;

	public FsTestMac() {
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
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test002_listFiles() throws IOException {
		System.out.println("--- --- --- test002_listFiles() --- --- ---");

		try {
			FileRef dir1 = FileRef.newInstance(fs, "/test/dir1");
			FileRef dir2 = FileRef.newInstance(fs, "/test/dir2");
			FileRef dir3 = FileRef.newInstance(fs, "/test/dir3");
			FileRef dir5 = FileRef.newInstance(fs, fs.root(), "test/dir5/jackson");

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

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Test
	public void test003_createFiles() throws IOException {
		System.out.println("--- --- --- test003_createFiles() --- --- ---");

		FileRef file1 = FileRef.newInstance(fs, "/dir1/newFile1.txt");
		FileRef file2 = FileRef.newInstance(fs, "/dir1/newFile2.txt");
		FileRef file3 = FileRef.newInstance(fs, "/dir1/newFile3.txt");
		FileRef dir1 = FileRef.newInstance(fs, "/dir1/new/path/to/somewhere1");
		FileRef dir2 = FileRef.newInstance(fs, "/dir1/new/path/to/somewhere2");
		FileRef dir3 = FileRef.newInstance(fs, "/dir1/new/path/to/somewhere3");

		boolean succeed1 = false;
		if (!file1.exists()) {
			succeed1 = file1.createNewFile();
		} else {
			System.out.println("File '" + file1.getPath() + "' already exists.");
		}

		boolean succeed2 = false;
		if (!file2.exists()) {
			succeed2 = file2.createNewFile();
		} else {
			System.out.println("File '" + file2.getPath() + "' already exists.");
		}

		boolean succeed3 = false;
		if (!file3.exists()) {
			succeed3 = file3.createNewFile();
		} else {
			System.out.println("File '" + file3.getPath() + "' already exists.");
		}

		boolean succeed4 = false;
		if (!dir1.exists()) {
			succeed4 = dir1.mkdirs();
		} else {
			System.out.println("Directory '" + dir1.getPath() + "' already exists.");
		}

		boolean succeed5 = false;
		if (!dir2.exists()) {
			succeed5 = dir2.mkdirs();
		} else {
			System.out.println("Directory '" + dir2.getPath() + "' already exists.");
		}

		boolean succeed6 = false;
		if (!dir3.exists()) {
			succeed6 = dir3.mkdirs();
		} else {
			System.out.println("Directory '" + dir3.getPath() + "' already exists.");
		}

		System.out.println(file1.getPath() + " is created? " + succeed1);
		System.out.println(file2.getPath() + " is created? " + succeed2);
		System.out.println(file3.getPath() + " is created? " + succeed3);
		System.out.println(dir1.getPath() + " is created? " + succeed4);
		System.out.println(dir2.getPath() + " is created? " + succeed5);
		System.out.println(dir3.getPath() + " is created? " + succeed6);

		System.out.println();
	}

	@Ignore
	@Test
	public void test004_deleteFiles() throws IOException {
		System.out.println("--- --- --- test004_deleteFiles() --- --- ---");

		FileRef file1 = FileRef.newInstance(fs, "/dir1/newFile1.txt");
		FileRef file2 = FileRef.newInstance(fs, "/dir1/newFile2.txt");
		FileRef file3 = FileRef.newInstance(fs, "/dir1/newFile3.txt");
		FileRef dir1New = FileRef.newInstance(fs, "/dir1/new");

		boolean succeed1 = file1.delete();
		boolean succeed2 = file2.delete();
		boolean succeed3 = file3.delete();
		boolean succeed4 = dir1New.delete();

		System.out.println(file1.getPath() + " is deleted? " + succeed1);
		System.out.println(file2.getPath() + " is deleted? " + succeed2);
		System.out.println(file3.getPath() + " is deleted? " + succeed3);
		System.out.println(dir1New.getPath() + " is deleted? " + succeed4);

		System.out.println();
	}

	@Test
	public void test005_listFiles() throws IOException {
		System.out.println("--- --- --- test005_listFiles() --- --- ---");

		try {
			FileRef dir1 = FileRef.newInstance(fs, "/dir1");

			FileRef[] subFiles1 = FileRef.listFiles(dir1);

			System.out.println("dir1 = " + dir1.getPath());
			for (FileRef subFile1 : subFiles1) {
				FileSystemUtil.walkFolders(fs, subFile1, 0);
			}
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FsTestMac.class);
		System.out.println("--- --- --- FsTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
