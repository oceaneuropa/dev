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
import com.osgi.example1.fs.client.api.FileRefInputStream;
import com.osgi.example1.fs.client.api.FileRefOutputStream;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemUtil;
import com.osgi.example1.util.FileUtil;
import com.osgi.example1.util.IOUtil;

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

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Test
	public void test003_createFiles() throws IOException {
		System.out.println("--- --- --- test003_createFiles() --- --- ---");

		FileRef dir1 = FileRef.newInstance(fs, "/test1");
		FileRef dir2 = FileRef.newInstance(fs, "/test1/tmp1");
		FileRef dir3 = FileRef.newInstance(fs, "/test1/tmp2");
		FileRef file1 = FileRef.newInstance(fs, "/test1/newFile1.txt");
		FileRef file2 = FileRef.newInstance(fs, "/test1/tmp1/newFile2.txt");
		FileRef file3 = FileRef.newInstance(fs, "/test1/tmp2/newFile3.txt");

		if (!dir1.exists()) {
			boolean succeed = dir1.mkdirs();
			if (succeed) {
				System.out.println("Directory '" + dir1.getPath() + "' is created.");
			} else {
				System.out.println("Directory '" + dir1.getPath() + "' is not created.");
			}
		} else {
			System.out.println("Directory '" + dir1.getPath() + "' already exists.");
		}

		if (!dir2.exists()) {
			boolean succeed = dir2.mkdirs();
			if (succeed) {
				System.out.println("Directory '" + dir2.getPath() + "' is created.");
			} else {
				System.out.println("Directory '" + dir2.getPath() + "' is not created.");
			}
		} else {
			System.out.println("Directory '" + dir2.getPath() + "' already exists.");
		}

		if (!dir3.exists()) {
			boolean succeed = dir2.mkdirs();
			if (succeed) {
				System.out.println("Directory '" + dir3.getPath() + "' is created.");
			} else {
				System.out.println("Directory '" + dir3.getPath() + "' is not created.");
			}
		} else {
			System.out.println("Directory '" + dir3.getPath() + "' already exists.");
		}

		if (!file1.exists()) {
			boolean succeed = file1.createNewFile();
			if (succeed) {
				System.out.println("File '" + file1.getPath() + "' is created.");
			} else {
				System.out.println("File '" + file1.getPath() + "' is not created.");
			}
		} else {
			System.out.println("File '" + file1.getPath() + "' already exists.");
		}

		if (!file2.exists()) {
			boolean succeed = file2.createNewFile();
			if (succeed) {
				System.out.println("File '" + file2.getPath() + "' is created.");
			} else {
				System.out.println("File '" + file2.getPath() + "' is not created.");
			}
		} else {
			System.out.println("File '" + file2.getPath() + "' already exists.");
		}

		if (!file3.exists()) {
			boolean succeed = file3.createNewFile();
			if (succeed) {
				System.out.println("File '" + file3.getPath() + "' is created.");
			} else {
				System.out.println("File '" + file3.getPath() + "' is not created.");
			}
		} else {
			System.out.println("File '" + file3.getPath() + "' already exists.");
		}

		System.out.println();
	}

	@Test
	public void test004_uploadFiles() throws IOException {
		System.out.println("--- --- --- test004_uploadFiles() --- --- ---");

		File localDir = new File("C:/downloads/test_source2/test2");
		FileRef refDir = FileRef.newInstance(fs, "/");
		boolean succeed = fs.uploadDirectoryToFsDirectory(localDir, refDir, true);

		if (succeed) {
			System.out.println(localDir.getAbsolutePath() + " is uploaded to " + refDir.getPath());
		} else {
			System.out.println("Failed to upload " + localDir.getAbsolutePath());
		}

		System.out.println();
	}

	@Test
	public void test005_uploadOutputStream() throws IOException {
		System.out.println("--- --- --- test005_uploadOutputStream() --- --- ---");

		File localFile1 = new File("C:/downloads/test_source2/test2/doc.json");
		File localFile2 = new File("C:/downloads/test_source2/test2/DownloadAllNumbers.txt");

		FileRef refFile1 = FileRef.newInstance(fs, "/test2/doc.json");
		FileRef refFile2 = FileRef.newInstance(fs, "/test2/DownloadAllNumbers.txt");

		FileRefOutputStream fos1 = null;
		try {
			fos1 = new FileRefOutputStream(refFile1);
			FileUtil.copyFileToOutputStream(localFile1, fos1);

			if (refFile1.exists()) {
				System.out.println(localFile1.getAbsolutePath() + " is uploaded to " + refFile1.getPath());
			} else {
				System.out.println(localFile1.getAbsolutePath() + " is not uploaded to " + refFile1.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fos1, true);
		}

		FileRefOutputStream fos2 = null;
		try {
			fos2 = new FileRefOutputStream(refFile2);
			FileUtil.copyFileToOutputStream(localFile2, fos2);

			if (refFile1.exists()) {
				System.out.println(localFile2.getAbsolutePath() + " is uploaded to " + refFile2.getPath());
			} else {
				System.out.println(localFile2.getAbsolutePath() + " is not uploaded to " + refFile2.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fos2, true);
		}

		System.out.println();
	}

	@Test
	public void test006_downloadFiles() throws IOException {
		System.out.println("--- --- --- test006_downloadFiles() --- --- ---");

		FileRef refFile1 = FileRef.newInstance(fs, "/test2/readme.txt");
		FileRef refFile2 = FileRef.newInstance(fs, "/test2/Smokingpipes_2016-01-22.pdf");

		File dir = new File("C:/downloads/test_target2/test2");
		File localFile1 = new File(dir, refFile1.getName());
		File localFile2 = new File(dir, refFile2.getName());

		boolean succeed1 = fs.downloadFsFileToFile(refFile1, localFile1);
		boolean succeed2 = fs.downloadFsFileToFile(refFile2, localFile2);

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

		System.out.println();
	}

	@Test
	public void test007_downloadDirectories() throws IOException {
		System.out.println("--- --- --- test007_downloadDirectories() --- --- ---");

		FileRef refDir = FileRef.newInstance(fs, "/test2");
		File dir = new File("C:/downloads/test_target2");

		boolean succeed = fs.downloadFsDirectoryToDirectory(refDir, dir, true);

		if (succeed) {
			System.out.println(refDir.getPath() + " is downloaded to " + dir.getAbsolutePath());
		} else {
			System.out.println("Failed to download " + refDir.getPath());
		}

		System.out.println();
	}

	@Test
	public void test008_downloadInputStream() throws IOException {
		System.out.println("--- --- --- test008_downloadInputStream() --- --- ---");

		File destLocalFile1 = new File("C:/downloads/test_target2/test2/readme(D).txt");
		File destLocalFile2 = new File("C:/downloads/test_target2/test2/Smokingpipes_2016-01-22(D).pdf");
		File destLocalFile3 = new File("C:/downloads/test_target2/test2/doc(D).json");
		File destLocalFile4 = new File("C:/downloads/test_target2/test2/DownloadAllNumbers(D).txt");

		FileRef refFile1 = FileRef.newInstance(fs, "/test2/readme.txt");
		FileRefInputStream fis1 = null;
		try {
			fis1 = new FileRefInputStream(refFile1);
			FileUtil.copyInputStreamToFile(fis1, destLocalFile1);

			if (destLocalFile1.exists()) {
				System.out.println(refFile1.getPath() + " is downloaded to " + destLocalFile1.getAbsolutePath());
			} else {
				System.out.println(refFile1.getPath() + " is not downloaded to " + destLocalFile1.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fis1, true);
		}

		FileRef refFile2 = FileRef.newInstance(fs, "/test2/Smokingpipes_2016-01-22.pdf");
		FileRefInputStream fis2 = null;
		try {
			fis2 = new FileRefInputStream(refFile2);
			FileUtil.copyInputStreamToFile(fis2, destLocalFile2);

			if (destLocalFile2.exists()) {
				System.out.println(refFile2.getPath() + " is downloaded to " + destLocalFile2.getAbsolutePath());
			} else {
				System.out.println(refFile2.getPath() + " is not downloaded to " + destLocalFile2.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fis2, true);
		}

		FileRef refFile3 = FileRef.newInstance(fs, "/test2/doc.json");
		FileRefInputStream fis3 = null;
		try {
			fis3 = new FileRefInputStream(refFile3);
			FileUtil.copyInputStreamToFile(fis3, destLocalFile3);

			if (destLocalFile3.exists()) {
				System.out.println(refFile3.getPath() + " is downloaded to " + destLocalFile3.getAbsolutePath());
			} else {
				System.out.println(refFile3.getPath() + " is not downloaded to " + destLocalFile3.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fis3, true);
		}

		FileRef refFile4 = FileRef.newInstance(fs, "/test2/DownloadAllNumbers.txt");
		FileRefInputStream fis4 = null;
		try {
			fis4 = new FileRefInputStream(refFile4);
			FileUtil.copyInputStreamToFile(fis4, destLocalFile4);

			if (destLocalFile4.exists()) {
				System.out.println(refFile4.getPath() + " is downloaded to " + destLocalFile4.getAbsolutePath());
			} else {
				System.out.println(refFile4.getPath() + " is not downloaded to " + destLocalFile4.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fis4, true);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_deleteFiles() throws IOException {
		System.out.println("--- --- --- test009_deleteFiles() --- --- ---");

		FileRef refDir = FileRef.newInstance(fs, "/test2/images");
		FileRef refFile = FileRef.newInstance(fs, "/test2/readme.txt");
		FileRef refDir2 = FileRef.newInstance(fs, "/test2");

		boolean succeed1 = refDir.delete();
		boolean succeed2 = refFile.delete();
		boolean succeed3 = refDir2.delete();

		System.out.println(refDir.getPath() + " is deleted? " + succeed1);
		System.out.println(refFile.getPath() + " is deleted? " + succeed2);
		System.out.println(refDir2.getPath() + " is deleted? " + succeed3);

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
