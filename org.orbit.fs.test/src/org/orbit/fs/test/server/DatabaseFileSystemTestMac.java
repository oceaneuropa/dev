package org.orbit.fs.test.server;

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
import org.orbit.fs.api.FilePath;
import org.orbit.fs.server.service.FileSystemService;
import org.orbit.fs.server.service.FileSystemServiceHelper;
import org.orbit.fs.server.service.database.DatabaseFS;
import org.orbit.fs.server.service.database.DatabaseFSConfig;
import org.origin.common.jdbc.DatabaseUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseFileSystemTestMac {

	protected FileSystemService fs;

	public DatabaseFileSystemTestMac() {
		this.fs = getDatabaseFileSystem();
	}

	protected void setUp() {
		this.fs = getDatabaseFileSystem();
	}

	protected FileSystemService getDatabaseFileSystem() {
		Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		DatabaseFSConfig config = new DatabaseFSConfig(properties);
		return new DatabaseFS(config);
	}

	// @Ignore
	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

		FilePath[] memberPaths = fs.listRoots();
		for (FilePath memberPath : memberPaths) {
			// FileMetaData fileMetaData = fs.getFileMetaData(rootPath);
			// System.out.println("rootPath.getName() = " + rootPath.getName());
			// System.out.println("\tfileMetaData " + fileMetaData.toString());
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test002_createDir() throws IOException {
		System.out.println("--- --- --- test002_createDir() --- --- ---");

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
	public void test002_createEmptyFiles() throws IOException {
		System.out.println("--- --- --- test002_createEmptyFiles() --- --- ---");

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

		FilePath destDir = new FilePath("/test/dir1");
		FilePath[] memberPaths = fs.listFiles(destDir);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test003_listFiles() throws IOException {
		System.out.println("--- --- --- test003_listFiles() --- --- ---");

		FilePath testDir = new FilePath("/test");
		FilePath[] memberPaths = fs.listFiles(testDir);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test004_deleteFiles() throws IOException {
		System.out.println("--- --- --- test004_deleteFiles() --- --- ---");

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
		FilePath[] memberPaths1 = fs.listFiles(dirPath1);
		for (FilePath memberPath : memberPaths1) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		FilePath dirPath2 = new FilePath("/test/dirToDelete2");
		System.out.println();
		System.out.println(dirPath2.getPathString() + ":");
		FilePath[] memberPaths2 = fs.listFiles(dirPath2);
		for (FilePath memberPath : memberPaths2) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test005_deleteDir() throws IOException {
		System.out.println("--- --- --- test005_deleteDir() --- --- ---");

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

	@Ignore
	@Test
	public void test007_localFileToFsFile() throws IOException {
		System.out.println("--- --- --- test007_localFileToFsFile() --- --- ---");

		File localFile1 = new File("/Users/oceaneuropa/Downloads/apache/commons-io-2.4.jar");
		File localFile2 = new File("/Users/oceaneuropa/Downloads/apache/commons-io-2.5-bin.zip");

		FilePath destDirPath = new FilePath("/test/dir2");

		fs.copyFileToFsFile(localFile1, new FilePath(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new FilePath(destDirPath, localFile2.getName()));

		FilePath[] memberPaths = fs.listFiles(destDirPath);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_localFileToFsDir() throws IOException {
		System.out.println("--- --- --- test008_localFileToFsDir() --- --- ---");

		File localFile1 = new File("/Users/oceaneuropa/Downloads/apache/hadoop/hadoop-common-2.7.1-sources.jar");
		File localFile2 = new File("/Users/oceaneuropa/Downloads/apache/hadoop/hadoop-common-2.7.1.jar");

		FilePath destDirPath = new FilePath("/test/dir3");
		fs.copyFileToFsDirectory(localFile1, destDirPath);
		fs.copyFileToFsDirectory(localFile2, destDirPath);

		FilePath[] memberPaths = fs.listFiles(destDirPath);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_inputStreamToFsDir() throws IOException {
		System.out.println("--- --- --- test009_inputStreamToFsDir() --- --- ---");

		// String dirPath = "/Users/oceaneuropa/Downloads/apache/myfolder";
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

		File localDir = new File("/Users/oceaneuropa/Downloads/testdir");
		FilePath destDirPath = new FilePath("/test/dir4");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, true);

		FilePath[] memberPaths = fs.listFiles(destDirPath);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test021_localDirToFsDir2() throws IOException {
		System.out.println("--- --- --- test021_localDirToFsDir2() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/testdir");
		FilePath destDirPath = new FilePath("/test/dir5");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, false);

		FilePath[] memberPaths = fs.listFiles(destDirPath);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test030_fsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/test_target");

		FilePath[] paths = fs.listRoots();
		for (FilePath path : paths) {
			FileSystemServiceHelper.INSTANCE.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test031_fsFileToLocalFile() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/test_target");

		FilePath fsPath = new FilePath("/Users/oceaneuropa/Downloads/apache/myfolder.zip");
		FileSystemServiceHelper.INSTANCE.copyFsFileToLocalDirectory(fs, fsPath, localDir);

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
