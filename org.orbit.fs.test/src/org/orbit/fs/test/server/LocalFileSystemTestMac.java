package org.orbit.fs.test.server;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.orbit.fs.api.FilePath;
import org.orbit.fs.common.FileSystem;
import org.orbit.fs.common.FileSystemHelper;
import org.orbit.fs.common.local.LocalFileSystem;
import org.orbit.fs.common.local.LocalFileSystemConfig;
import org.origin.common.resource.IPath;

public class LocalFileSystemTestMac {

	protected FileSystem fs;

	public LocalFileSystemTestMac() {
		this.fs = getLocalFileSystem();
	}

	protected void setUp() {
		this.fs = getLocalFileSystem();
	}

	protected FileSystem getLocalFileSystem() {
		// File homeDirector = new File("/Users/example/Downloads/apache"); // For Mac
		// File homeDirector = new File("/Users/example/Downloads/Swagger"); // For Mac
		File homeDir = new File("/Users/example/Downloads/ear"); // For Mac
		LocalFileSystemConfig config = new LocalFileSystemConfig(homeDir);
		return new LocalFileSystem(config);
	}

	@Ignore
	@Test
	public void testListRootFiles() throws IOException {
		System.out.println("--- --- --- testListRootFiles() --- --- ---");

		IPath[] memberPaths = fs.listRoots();
		for (IPath memberPath : memberPaths) {
			// FileMetaData fileMetaData = fs.getFileMetaData(rootPath);
			// System.out.println("rootPath.getName() = " + rootPath.getName());
			// System.out.println("\tfileMetaData " + fileMetaData.toString());
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testListFiles() throws IOException {
		System.out.println("--- --- --- testListFiles() --- --- ---");

		IPath testDir = new FilePath("/test");
		IPath[] memberPaths = fs.listFiles(testDir);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCreateDir() throws IOException {
		System.out.println("--- --- --- testCreateDir() --- --- ---");

		FilePath path1 = new FilePath("/test/dir1");
		FilePath path2 = new FilePath("/test/dir2");
		FilePath path3 = new FilePath("/test/dir3");
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

		FilePath file1 = new FilePath("/test/dirToDelete1/gwt-dev.jar");
		FilePath file2 = new FilePath("/test/dirToDelete1/gwt-user.jar");
		FilePath file3 = new FilePath("/test/dirToDelete1/readme2.txt");

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

		FilePath file6 = new FilePath("/test/dirToDelete2/jackson-databind.jar");
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

		FilePath dirPath1 = new FilePath("/test/dirToDelete1");
		System.out.println();
		System.out.println(dirPath1.getPathString() + ":");
		IPath[] memberPaths1 = fs.listFiles(dirPath1);
		for (IPath memberPath : memberPaths1) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		FilePath dirPath2 = new FilePath("/test/dirToDelete2");
		System.out.println();
		System.out.println(dirPath2.getPathString() + ":");
		IPath[] memberPaths2 = fs.listFiles(dirPath2);
		for (IPath memberPath : memberPaths2) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testDeleteDir() throws IOException {
		System.out.println("--- --- --- testDeleteDir() --- --- ---");

		FilePath dirPath1 = new FilePath("/test/dirToDelete1");
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

		FilePath dirPath2 = new FilePath("/test/dirToDelete2");
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

		FilePath path1 = new FilePath("/test/dir1/newFile1.txt");
		FilePath path2 = new FilePath("/test/dir1/newFile2.txt");
		FilePath path3 = new FilePath("/test/dir1/newFile3.txt");

		if (!fs.exists(path1)) {
			fs.createNewFile(path1);
		}
		if (!fs.exists(path2)) {
			fs.createNewFile(path2);
		}
		if (!fs.exists(path3)) {
			fs.createNewFile(path3);
		}

		IPath destDir = new FilePath("/test/dir1");
		IPath[] memberPaths = fs.listFiles(destDir);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyFileToFile() throws IOException {
		System.out.println("--- --- --- testCopyFileToFile() --- --- ---");

		File localFile1 = new File("/Users/example/Downloads/apache/commons-io-2.4.jar");
		File localFile2 = new File("/Users/example/Downloads/apache/commons-io-2.5-bin.zip");

		FilePath destDirPath = new FilePath("/test/dir2");

		fs.copyFileToFsFile(localFile1, new FilePath(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new FilePath(destDirPath, localFile2.getName()));

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void testCopyFileToDir() throws IOException {
		System.out.println("--- --- --- testCopyFileToDir() --- --- ---");

		File localFile1 = new File("/Users/example/Downloads/apache/hadoop/hadoop-common-2.7.1-sources.jar");
		File localFile2 = new File("/Users/example/Downloads/apache/hadoop/hadoop-common-2.7.1.jar");

		FilePath destDirPath = new FilePath("/test/dir3");
		fs.copyFileToFsDirectory(localFile1, destDirPath);
		fs.copyFileToFsDirectory(localFile2, destDirPath);

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testCopyDirToDir1() throws IOException {
		System.out.println("--- --- --- testCopyDirToDir1() --- --- ---");

		File localDir = new File("/Users/example/Downloads/testdir");
		FilePath destDirPath = new FilePath("/test/dir4");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, true);

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void testCopyDirToDir2() throws IOException {
		System.out.println("--- --- --- testCopyDirToDir2() --- --- ---");

		File localDir = new File("/Users/example/Downloads/testdir");
		FilePath destDirPath = new FilePath("/test/dir5");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, false);

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
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
