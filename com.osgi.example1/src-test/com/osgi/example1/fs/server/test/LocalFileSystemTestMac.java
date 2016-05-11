package com.osgi.example1.fs.server.test;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.FileSystemUtil;
import com.osgi.example1.fs.server.service.local.LocalFileSystem;
import com.osgi.example1.fs.server.service.local.LocalFileSystemConfiguration;

public class LocalFileSystemTestMac {

	protected FileSystem fs;

	public LocalFileSystemTestMac() {
		this.fs = getLocalFileSystem();
	}

	protected void setUp() {
		this.fs = getLocalFileSystem();
	}

	protected FileSystem getLocalFileSystem() {
		// File homeDirector = new File("/Users/yayang/Downloads/apache"); // For Mac
		// File homeDirector = new File("/Users/yayang/Downloads/Swagger"); // For Mac
		File homeDirector = new File("/Users/yayang/Downloads/ear"); // For Mac
		LocalFileSystemConfiguration config = new LocalFileSystemConfiguration(homeDirector);
		return new LocalFileSystem(config);
	}

	@Ignore
	@Test
	public void testListRootFiles() throws IOException {
		System.out.println("--- --- --- testListRootFiles() --- --- ---");

		Path[] memberPaths = fs.listRoots();
		for (Path memberPath : memberPaths) {
			// FileMetaData fileMetaData = fs.getFileMetaData(rootPath);
			// System.out.println("rootPath.getName() = " + rootPath.getName());
			// System.out.println("\tfileMetaData " + fileMetaData.toString());
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testListFiles() throws IOException {
		System.out.println("--- --- --- testListFiles() --- --- ---");

		Path testDir = new Path("/test");
		Path[] memberPaths = fs.listFiles(testDir);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCreateDir() throws IOException {
		System.out.println("--- --- --- testCreateDir() --- --- ---");

		Path path1 = new Path("/test/dir1");
		Path path2 = new Path("/test/dir2");
		Path path3 = new Path("/test/dir3");
		if (!fs.exists(path1)) {
			fs.mkdirs(path1);
		}
		if (!fs.exists(path2)) {
			fs.mkdirs(path2);
		}
		if (!fs.exists(path3)) {
			fs.mkdirs(path3);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testDeleteFiles() throws IOException {
		System.out.println("--- --- --- testDeleteFiles() --- --- ---");

		Path file1 = new Path("/test/dirToDelete1/gwt-dev.jar");
		Path file2 = new Path("/test/dirToDelete1/gwt-user.jar");
		Path file3 = new Path("/test/dirToDelete1/readme2.txt");

		if (fs.exists(file1)) {
			boolean succeed = fs.delete(file1);
			if (succeed) {
				System.out.println(file1.getPathString() + " is deleted.");
			} else {
				System.out.println(file1.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(file1.getPathString() + " does not exist.");
		}
		if (fs.exists(file2)) {
			boolean succeed = fs.delete(file2);
			if (succeed) {
				System.out.println(file2.getPathString() + " is deleted.");
			} else {
				System.out.println(file2.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(file2.getPathString() + " does not exist.");
		}
		if (fs.exists(file3)) {
			boolean succeed = fs.delete(file3);
			if (succeed) {
				System.out.println(file3.getPathString() + " is deleted.");
			} else {
				System.out.println(file3.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(file3.getPathString() + " does not exist.");
		}

		Path file6 = new Path("/test/dirToDelete2/jackson-databind.jar");
		if (fs.exists(file6)) {
			boolean succeed = fs.delete(file6);
			if (succeed) {
				System.out.println(file6.getPathString() + " is deleted.");
			} else {
				System.out.println(file6.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(file6.getPathString() + " does not exist.");
		}

		Path dirPath1 = new Path("/test/dirToDelete1");
		System.out.println();
		System.out.println(dirPath1.getPathString() + ":");
		Path[] memberPaths1 = fs.listFiles(dirPath1);
		for (Path memberPath : memberPaths1) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		Path dirPath2 = new Path("/test/dirToDelete2");
		System.out.println();
		System.out.println(dirPath2.getPathString() + ":");
		Path[] memberPaths2 = fs.listFiles(dirPath2);
		for (Path memberPath : memberPaths2) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testDeleteDir() throws IOException {
		System.out.println("--- --- --- testDeleteDir() --- --- ---");

		Path dirPath1 = new Path("/test/dirToDelete1");
		if (fs.exists(dirPath1)) {
			boolean succeed = fs.delete(dirPath1);
			if (succeed) {
				System.out.println(dirPath1.getPathString() + " is deleted.");
			} else {
				System.out.println(dirPath1.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(dirPath1.getPathString() + " does not exist.");
		}

		Path dirPath2 = new Path("/test/dirToDelete2");
		if (fs.exists(dirPath2)) {
			boolean succeed = fs.delete(dirPath2);
			if (succeed) {
				System.out.println(dirPath2.getPathString() + " is deleted.");
			} else {
				System.out.println(dirPath2.getPathString() + " --- failed to delete.");
			}
		} else {
			System.out.println(dirPath2.getPathString() + " does not exist.");
		}

		System.out.println();
	}

	@Test
	public void testCreateEmptyFiles() throws IOException {
		System.out.println("--- --- --- testCreateEmptyFiles() --- --- ---");

		Path path1 = new Path("/test/dir1/newFile1.txt");
		Path path2 = new Path("/test/dir1/newFile2.txt");
		Path path3 = new Path("/test/dir1/newFile3.txt");

		if (!fs.exists(path1)) {
			fs.createNewFile(path1);
		}
		if (!fs.exists(path2)) {
			fs.createNewFile(path2);
		}
		if (!fs.exists(path3)) {
			fs.createNewFile(path3);
		}

		Path destDir = new Path("/test/dir1");
		Path[] memberPaths = fs.listFiles(destDir);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyFileToFile() throws IOException {
		System.out.println("--- --- --- testCopyFileToFile() --- --- ---");

		File localFile1 = new File("/Users/yayang/Downloads/apache/commons-io-2.4.jar");
		File localFile2 = new File("/Users/yayang/Downloads/apache/commons-io-2.5-bin.zip");

		Path destDirPath = new Path("/test/dir2");

		fs.copyLocalFileToFsFile(localFile1, new Path(destDirPath, localFile1.getName()));
		fs.copyLocalFileToFsFile(localFile2, new Path(destDirPath, localFile2.getName()));

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyFileToDir() throws IOException {
		System.out.println("--- --- --- testCopyFileToDir() --- --- ---");

		File localFile1 = new File("/Users/yayang/Downloads/apache/hadoop/hadoop-common-2.7.1-sources.jar");
		File localFile2 = new File("/Users/yayang/Downloads/apache/hadoop/hadoop-common-2.7.1.jar");

		Path destDirPath = new Path("/test/dir3");
		fs.copyLocalFileToFsDirectory(localFile1, destDirPath);
		fs.copyLocalFileToFsDirectory(localFile2, destDirPath);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyDirToDir1() throws IOException {
		System.out.println("--- --- --- testCopyDirToDir1() --- --- ---");

		File localDir = new File("/Users/yayang/Downloads/testdir");
		Path destDirPath = new Path("/test/dir4");
		fs.copyLocalDirectoryToFsDirectory(localDir, destDirPath, true);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyDirToDir2() throws IOException {
		System.out.println("--- --- --- testCopyDirToDir2() --- --- ---");

		File localDir = new File("/Users/yayang/Downloads/testdir");
		Path destDirPath = new Path("/test/dir5");
		fs.copyLocalDirectoryToFsDirectory(localDir, destDirPath, false);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(LocalFileSystemTestMac.class);

		System.out.println("--- --- --- LocalFileSystemTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
