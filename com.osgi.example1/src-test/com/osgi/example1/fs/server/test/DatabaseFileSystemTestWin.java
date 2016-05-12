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
import org.origin.common.jdbc.DatabaseUtil;

import com.osgi.example1.fs.common.Path;
import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.FileSystemUtil;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystem;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystemConfiguration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseFileSystemTestWin {

	protected FileSystem fs;

	public DatabaseFileSystemTestWin() {
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

	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

		Path[] memberPaths = fs.listRoots();
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

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

	@Test
	public void test003_createEmptyFiles() throws IOException {
		System.out.println("--- --- --- test003_createEmptyFiles() --- --- ---");

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
	public void test004_copyLocalFileToFsFileOrFsDir() throws IOException {
		System.out.println("--- --- --- test004_copyLocalFileToFsFileOrFsDir() --- --- ---");

		File localFile1 = new File("C:/downloads/test_source/dir2/hadoop-2.7.1-src.tar.gz");
		File localFile2 = new File("C:/downloads/test_source/dir2/hadoop-2.7.2.tar.gz");
		File localFile3 = new File("C:/downloads/test_source/dir2/karaf.rar");
		File localFile4 = new File("C:/downloads/test_source/dir2/Ribbon.rar");

		Path destDirPath = new Path("/test/dir2");

		fs.copyFileToFsFile(localFile1, new Path(destDirPath, localFile1.getName()));
		fs.copyFileToFsFile(localFile2, new Path(destDirPath, localFile2.getName()));
		fs.copyFileToFsDirectory(localFile3, destDirPath);
		fs.copyFileToFsDirectory(localFile4, destDirPath);

		Path[] memberPaths = fs.listFiles(destDirPath);
		for (Path memberPath : memberPaths) {
			FileSystemUtil.walkFolders(fs, memberPath, 0);
		}

		System.out.println();
	}

	@Test
	public void test005_copyLocalDirToFsDir() throws IOException {
		System.out.println("--- --- --- test005_copyLocalDirToFsDir() --- --- ---");

		File localDir1 = new File("C:/downloads/test_source/dir3/temp1");
		File localDir2 = new File("C:/downloads/test_source/dir3/temp2");
		File localDir3 = new File("C:/downloads/test_source/dir5/");

		Path destDirPath1 = new Path("/test/dir3");
		Path destDirPath2 = new Path("/test/dir3");
		Path destDirPath3 = new Path("/test/");

		fs.copyDirectoryToFsDirectory(localDir1, destDirPath1, true);
		fs.copyDirectoryToFsDirectory(localDir2, destDirPath2, false);
		fs.copyDirectoryToFsDirectory(localDir3, destDirPath3, true);

		Path[] paths = fs.listFiles(destDirPath1);
		for (Path path : paths) {
			FileSystemUtil.walkFolders(fs, path, 0);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test007_copyFsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test007_copyFsDirToLocalDir() --- --- ---");

		File localDir = new File("C:/downloads/test_target");

		Path[] paths = fs.listRoots();
		for (Path path : paths) {
			FileSystemUtil.copyFsFileToLocalDirectory(fs, path, localDir);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_deleteFsFiles() throws IOException {
		System.out.println("--- --- --- test008_deleteFsFiles() --- --- ---");

		Path filePath1 = new Path("/test/dir5/webgl/webgl-tutorials.css");
		Path filePath2 = new Path("/test/dir5/webgl/webgl-utils.js");

		fs.delete(filePath1);
		fs.delete(filePath2);

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_deleteFsDirs() throws IOException {
		System.out.println("--- --- --- test009_deleteFsDirs() --- --- ---");

		Path dirPath1 = new Path("/test/dir5/01_Study");
		Path dirPath2 = new Path("/test/dir5/06_Book");

		fs.delete(dirPath1);
		fs.delete(dirPath2);

		System.out.println();
	}

	@Test
	public void test010_copyFsDirToLocalDir() throws IOException {
		System.out.println("--- --- --- test010_copyFsDirToLocalDir() --- --- ---");

		File localDir = new File("C:/downloads/test_target");

		Path[] paths = fs.listRoots();
		for (Path path : paths) {
			FileSystemUtil.copyFsFileToLocalDirectory(fs, path, localDir);
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
