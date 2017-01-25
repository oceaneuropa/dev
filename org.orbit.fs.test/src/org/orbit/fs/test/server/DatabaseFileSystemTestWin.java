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
public class DatabaseFileSystemTestWin {

	protected FileSystemService fs;

	public DatabaseFileSystemTestWin() {
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

	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

		FilePath[] memberPaths = fs.listRoots();
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

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

	@Test
	public void test003_createEmptyFiles() throws IOException {
		System.out.println("--- --- --- test003_createEmptyFiles() --- --- ---");

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

	@Test
	public void test004_copyLocalFileToFsFileOrFsDir() throws IOException {
		System.out.println("--- --- --- test004_copyLocalFileToFsFileOrFsDir() --- --- ---");

		File localFile1 = new File("C:/downloads/test_source/dir2/hadoop-2.7.1-src.tar.gz");
		File localFile2 = new File("C:/downloads/test_source/dir2/hadoop-2.7.2.tar.gz");
		File localFile3 = new File("C:/downloads/test_source/dir2/karaf.rar");
		File localFile4 = new File("C:/downloads/test_source/dir2/Ribbon.rar");

		FilePath destDirPath = new FilePath("/test/dir2");

		fs.copyFileToFsFile(localFile1, new FilePath(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new FilePath(destDirPath, localFile2.getName()));
		fs.copyFileToFsDirectory(localFile3, destDirPath);
		fs.copyFileToFsDirectory(localFile4, destDirPath);

		FilePath[] memberPaths = fs.listFiles(destDirPath);
		for (FilePath memberPath : memberPaths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void test005_copyLocalDirToFsDir() throws IOException {
		System.out.println("--- --- --- test005_copyLocalDirToFsDir() --- --- ---");

		File localDir1 = new File("C:/downloads/test_source/dir3/temp1");
		File localDir2 = new File("C:/downloads/test_source/dir3/temp2");
		File localDir3 = new File("C:/downloads/test_source/dir5/");

		FilePath destDirPath1 = new FilePath("/test/dir3");
		FilePath destDirPath2 = new FilePath("/test/dir3");
		FilePath destDirPath3 = new FilePath("/test/");

		fs.copyDirectoryToFsDirectory(localDir1, destDirPath1, true);
		fs.copyDirectoryToFsDirectory(localDir2, destDirPath2, false);
		fs.copyDirectoryToFsDirectory(localDir3, destDirPath3, true);

		FilePath[] paths = fs.listFiles(destDirPath1);
		for (FilePath path : paths) {
			FileSystemServiceHelper.INSTANCE.walkFolders(fs, path, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test007_copyFsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test007_copyFsDirToLocalDir() --- --- ---");

		File localDir = new File("C:/downloads/test_target");

		FilePath[] paths = fs.listRoots();
		for (FilePath path : paths) {
			FileSystemServiceHelper.INSTANCE.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_deleteFsFiles() throws IOException {
		System.out.println("--- --- --- test008_deleteFsFiles() --- --- ---");

		FilePath filePath1 = new FilePath("/test/dir5/webgl/webgl-tutorials.css");
		FilePath filePath2 = new FilePath("/test/dir5/webgl/webgl-utils.js");

		fs.delete(filePath1);
		fs.delete(filePath2);

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_deleteFsDirs() throws IOException {
		System.out.println("--- --- --- test009_deleteFsDirs() --- --- ---");

		FilePath dirPath1 = new FilePath("/test/dir5/01_Study");
		FilePath dirPath2 = new FilePath("/test/dir5/06_Book");

		fs.delete(dirPath1);
		fs.delete(dirPath2);

		System.out.println();
	}

	@Test
	public void test010_copyFsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test010_copyFsDirToLocalDir() --- --- ---");

		File localDir = new File("C:/downloads/test_target");

		FilePath[] paths = fs.listRoots();
		for (FilePath path : paths) {
			FileSystemServiceHelper.INSTANCE.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(DatabaseFileSystemTestWin.class);

		System.out.println("--- --- --- DatabaseFileSystemTestWin.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
