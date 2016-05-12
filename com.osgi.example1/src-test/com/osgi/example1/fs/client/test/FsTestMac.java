package com.osgi.example1.fs.client.test;

import java.io.File;
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

	@Ignore
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

	@Test
	public void test004_uploadFiles() throws IOException {
		System.out.println("--- --- --- test004_uploadFiles() --- --- ---");

		File localFile1 = new File("/Users/yayang/Downloads/test/source/Book.xsd");
		File localFile2 = new File("/Users/yayang/Downloads/test/source/swagger-parser-master.zip");
		File localFile3 = new File("/Users/yayang/Downloads/test/source/commons-io-2.5-src.zip");

		FileRef dir = FileRef.newInstance(fs, "/dir2");
		FileRef refFile1 = FileRef.newInstance(fs, dir, localFile1.getName());
		FileRef refFile2 = FileRef.newInstance(fs, dir, localFile2.getName());
		FileRef refFile3 = FileRef.newInstance(fs, dir, localFile3.getName());

		boolean succeed1 = fs.uploadFileToFsFile(localFile1, refFile1);
		boolean succeed2 = fs.uploadFileToFsFile(localFile2, refFile2);
		boolean succeed3 = fs.uploadFileToFsFile(localFile3, refFile3);

		if (succeed1) {
			System.out.println(localFile1.getAbsolutePath() + " is uploaded to " + refFile1.getPath());
		} else {
			System.out.println("Failed to upload " + localFile1.getAbsolutePath());
		}
		if (succeed2) {
			System.out.println(localFile2.getAbsolutePath() + " is uploaded to " + refFile2.getPath());
		} else {
			System.out.println("Failed to upload " + localFile2.getAbsolutePath());
		}
		if (succeed3) {
			System.out.println(localFile3.getAbsolutePath() + " is uploaded to " + refFile3.getPath());
		} else {
			System.out.println("Failed to upload " + localFile3.getAbsolutePath());
		}

		System.out.println();
	}

	@Test
	public void test005_downloadFiles() throws IOException {
		System.out.println("--- --- --- test005_downloadFiles() --- --- ---");

		FileRef refFile1 = FileRef.newInstance(fs, "/dir2/Book.xsd");
		FileRef refFile2 = FileRef.newInstance(fs, "/dir2/swagger-parser-master.zip");
		FileRef refFile3 = FileRef.newInstance(fs, "/dir2/commons-io-2.5-src.zip");

		File dir = new File("/Users/yayang/Downloads/test/target2/");
		File localFile1 = new File(dir, refFile1.getName());
		File localFile2 = new File(dir, refFile2.getName());

		boolean succeed1 = fs.downloadFsFileToFile(refFile1, localFile1);
		boolean succeed2 = fs.downloadFsFileToFile(refFile2, localFile2);
		boolean succeed3 = fs.downloadFsFileToDirectory(refFile3, dir);

		if (succeed1) {
			System.out.println(refFile1.getPath() + " is downloaded to " + localFile1.getAbsolutePath());
		} else {
			System.out.println("Failed to download " + refFile1.getPath());
		}
		if (succeed2) {
			System.out.println(refFile2.getPath() + " is downloaded to " + localFile2.getAbsolutePath());
		} else {
			System.out.println("Failed to download " + refFile2.getPath());
		}
		if (succeed3) {
			System.out.println(refFile3.getPath() + " is downloaded into " + dir.getAbsolutePath());
		} else {
			System.out.println("Failed to download " + refFile3.getPath());
		}

		System.out.println();
	}

	@Test
	public void test006_downloadDirectories() throws IOException {
		System.out.println("--- --- --- test006_downloadDirectories() --- --- ---");

		FileRef dirRef = FileRef.newInstance(fs, "/dir1");
		File dir = new File("/Users/yayang/Downloads/test/target2/");

		boolean succeed = fs.downloadFsDirectoryToDirectory(dirRef, dir, true);

		if (succeed) {
			System.out.println(dirRef.getPath() + " is downloaded to " + dir.getAbsolutePath());
		} else {
			System.out.println("Failed to download " + dirRef.getPath());
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test007_deleteFiles() throws IOException {
		System.out.println("--- --- --- test007_deleteFiles() --- --- ---");

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
	public void test008_listFiles() throws IOException {
		System.out.println("--- --- --- test008_listFiles() --- --- ---");

		try {
			FileRef dir1 = FileRef.newInstance(fs, "/dir1");
			FileRef dir2 = FileRef.newInstance(fs, "/dir2");

			FileRef[] subFiles1 = FileRef.listFiles(dir1);
			FileRef[] subFiles2 = FileRef.listFiles(dir2);

			System.out.println("dir1 = " + dir1.getPath());
			for (FileRef subFile : subFiles1) {
				FileSystemUtil.walkFolders(fs, subFile, 0);
			}
			System.out.println();

			System.out.println("dir2 = " + dir2.getPath());
			for (FileRef subFile : subFiles2) {
				FileSystemUtil.walkFolders(fs, subFile, 0);
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
