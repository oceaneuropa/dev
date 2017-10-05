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
import org.orbit.fs.common.Constants;
import org.orbit.fs.common.FileSystem;
import org.orbit.fs.common.FileSystemHelper;
import org.orbit.fs.common.database.DatabaseFileSystem;
import org.orbit.fs.common.database.DatabaseFileSystemConfig;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.resource.IPath;

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
		properties.put(Constants.METADATA_TABLE_NAME, Constants.METADATA_TABLE_NAME_DEFAULT_VALUE);
		properties.put(Constants.CONTENT_TABLE_NAME, Constants.CONTENT_TABLE_NAME_DEFAULT_VALUE);

		DatabaseFileSystemConfig config = new DatabaseFileSystemConfig(properties);
		return new DatabaseFileSystem(config);
	}

	// @Ignore
	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

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
	public void test002_createDir() throws IOException {
		System.out.println("--- --- --- test002_createDir() --- --- ---");

		IPath path1 = new FilePath("/test/dir1");
		IPath path2 = new FilePath("/test/dir2");
		IPath path3 = new FilePath("/test/dir3");
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

		IPath path1 = new FilePath("/test/dir1/newFile1.txt");
		IPath path2 = new FilePath("/test/dir1/newFile2.txt");
		IPath path3 = new FilePath("/test/dir1/newFile3.txt");

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

	@Ignore
	@Test
	public void test003_listFiles() throws IOException {
		System.out.println("--- --- --- test003_listFiles() --- --- ---");

		IPath testDir = new FilePath("/test");
		IPath[] memberPaths = fs.listFiles(testDir);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test004_deleteFiles() throws IOException {
		System.out.println("--- --- --- test004_deleteFiles() --- --- ---");

		IPath file1 = new FilePath("/test/dirToDelete1/gwt-dev.jar");
		IPath file2 = new FilePath("/test/dirToDelete1/gwt-user.jar");
		IPath file3 = new FilePath("/test/dirToDelete1/readme2.txt");

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

		IPath file6 = new FilePath("/test/dirToDelete2/jackson-databind.jar");
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

		IPath dirPath1 = new FilePath("/test/dirToDelete1");
		System.out.println();
		System.out.println(dirPath1.getPathString() + ":");
		IPath[] memberPaths1 = fs.listFiles(dirPath1);
		for (IPath memberPath : memberPaths1) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		IPath dirPath2 = new FilePath("/test/dirToDelete2");
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
	public void test005_deleteDir() throws IOException {
		System.out.println("--- --- --- test005_deleteDir() --- --- ---");

		IPath dirPath1 = new FilePath("/test/dirToDelete1");
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

		IPath dirPath2 = new FilePath("/test/dirToDelete2");
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

		IPath destDirPath = new FilePath("/test/dir2");

		fs.copyFileToFsFile(localFile1, new FilePath(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new FilePath(destDirPath, localFile2.getName()));

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_localFileToFsDir() throws IOException {
		System.out.println("--- --- --- test008_localFileToFsDir() --- --- ---");

		File localFile1 = new File("/Users/oceaneuropa/Downloads/apache/hadoop/hadoop-common-2.7.1-sources.jar");
		File localFile2 = new File("/Users/oceaneuropa/Downloads/apache/hadoop/hadoop-common-2.7.1.jar");

		IPath destDirPath = new FilePath("/test/dir3");
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
		IPath destDirPath = new FilePath("/test/dir4");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, true);

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test021_localDirToFsDir2() throws IOException {
		System.out.println("--- --- --- test021_localDirToFsDir2() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/testdir");
		IPath destDirPath = new FilePath("/test/dir5");
		fs.copyDirectoryToFsDirectory(localDir, destDirPath, false);

		IPath[] memberPaths = fs.listFiles(destDirPath);
		for (IPath memberPath : memberPaths) {
			FileSystemHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test030_fsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/test_target");

		IPath[] paths = fs.listRoots();
		for (IPath path : paths) {
			FileSystemHelper.INSTANCE.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test031_fsFileToLocalFile() throws IOException {
		System.out.println("--- --- --- test030_fsDirToLocalDir() --- --- ---");

		File localDir = new File("/Users/oceaneuropa/Downloads/test_target");

		IPath fsPath = new FilePath("/Users/oceaneuropa/Downloads/apache/myfolder.zip");
		FileSystemHelper.INSTANCE.copyFsFileToLocalDirectory(fs, fsPath, localDir);

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
