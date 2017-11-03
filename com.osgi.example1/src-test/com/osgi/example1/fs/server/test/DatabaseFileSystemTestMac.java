package com.osgi.example1.fs.server.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.io.ZipUtil;
import org.origin.common.jdbc.DatabaseUtil;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.FileSystemUtil;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystem;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystemConfiguration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseFileSystemTestMac {

	protected FileSystem fs;

	public DatabaseFileSystemTestMac() {
		this.fs = getDatabaseFileSystem();
	}

	protected void setUp() {
		this.fs = getDatabaseFileSystem();
	}

	protected FileSystem getDatabaseFileSystem() {
		Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		DatabaseFileSystemConfiguration config = new DatabaseFileSystemConfiguration(properties);
		return new DatabaseFileSystem(config);
	}

	@Ignore
	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

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
	public void test002_createDir() throws IOException {
		System.out.println("--- --- --- test002_createDir() --- --- ---");

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
	public void test002_createEmptyFiles() throws IOException {
		System.out.println("--- --- --- test002_createEmptyFiles() --- --- ---");

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

	@Ignore
	@Test
	public void test003_listFiles() throws IOException {
		System.out.println("--- --- --- test003_listFiles() --- --- ---");

		Path testDir = new Path("/test");
		Path[] memberPaths = fs.listFiles(testDir);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test004_deleteFiles() throws IOException {
		System.out.println("--- --- --- test004_deleteFiles() --- --- ---");

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
	public void test005_deleteDir() throws IOException {
		System.out.println("--- --- --- test005_deleteDir() --- --- ---");

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

	@Ignore
	@Test
	public void test007_localFileToFsFile() throws IOException {
		System.out.println("--- --- --- test007_localFileToFsFile() --- --- ---");

		File localFile1 = new File("/Users/example/Downloads/apache/commons-io-2.4.jar");
		File localFile2 = new File("/Users/example/Downloads/apache/commons-io-2.5-bin.zip");

		Path destDirPath = new Path("/test/dir2");

		fs.copyFileToFsFile(localFile1, new Path(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new Path(destDirPath, localFile2.getName()));

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_localFileToFsDir() throws IOException {
		System.out.println("--- --- --- test008_localFileToFsDir() --- --- ---");

		File localFile1 = new File("/Users/example/Downloads/apache/hadoop/hadoop-common-2.7.1-sources.jar");
		File localFile2 = new File("/Users/example/Downloads/apache/hadoop/hadoop-common-2.7.1.jar");

		Path destDirPath = new Path("/test/dir3");
		fs.copyFileToFsDirectory(localFile1, destDirPath);
		fs.copyFileToFsDirectory(localFile2, destDirPath);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void test009_inputStreamToFsDir() throws IOException {
		System.out.println("--- --- --- test009_inputStreamToFsDir() --- --- ---");

		// String dirPath = "/Users/example/Downloads/apache/myfolder";
		// String zipPath = dirPath + ".zip";
		// Path filePath = new Path(zipPath);
		// if (fs.exists(filePath)) {
		// fs.delete(filePath);
		// }
		// fs.copyInputStreamToFsFile(ZipUtil.getZipInputStream(new File(sourcePath)), new Path(targetPath));
	}

	@Ignore
	@Test
	public void test020_localDirToFsDir1() throws IOException {
		System.out.println("--- --- --- test020_localDirToFsDir1() --- --- ---");

		File localDir = new File("/Users/example/Downloads/testdir");
		Path destDirPath = new Path("/test/dir4");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, true);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test021_localDirToFsDir2() throws IOException {
		System.out.println("--- --- --- test021_localDirToFsDir2() --- --- ---");

		File localDir = new File("/Users/example/Downloads/testdir");
		Path destDirPath = new Path("/test/dir5");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, false);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test030_fsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/example/Downloads/test_target");

		Path[] paths = fs.listRoots();
		for (Path path : paths) {
			FileSystemUtil.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test031_fsFileToLocalFile() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/example/Downloads/test_target");

		Path fsPath = new Path("/Users/example/Downloads/apache/myfolder.zip");
		FileSystemUtil.copyFsFileToLocalDirectory(fs, fsPath, localDir);

		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(DatabaseFileSystemTestMac.class);

		System.out.println("--- --- --- DatabaseFileSystemTestMac.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
