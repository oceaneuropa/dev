package com.osgi.example1.fs.client.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;

import com.osgi.example1.fs.client.api.FileRef;
import com.osgi.example1.fs.client.api.FileRefInputStream;
import com.osgi.example1.fs.client.api.FileRefOutputStream;
import com.osgi.example1.fs.client.api.FileSystem;
import com.osgi.example1.fs.client.api.FileSystemConfiguration;
import com.osgi.example1.fs.client.ws.FileSystemUtil;

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
	public void test005_uploadUsingOutputStream() throws IOException {
		System.out.println("--- --- --- test005_uploadUsingOutputStream() --- --- ---");

		File localFile1 = new File("C:/downloads/test_source2/test2/doc.json");
		File localFile2 = new File("C:/downloads/test_source2/test2/DownloadAllNumbers.txt");

		FileRef refFile1 = FileRef.newInstance(fs, "/test2/doc.json");
		FileRef refFile2 = FileRef.newInstance(fs, "/test2/DownloadAllNumbers.txt");

		OutputStream output1 = null;
		try {
			output1 = new FileRefOutputStream(refFile1);
			FileUtil.copyFileToOutputStream(localFile1, output1);

			if (refFile1.exists()) {
				System.out.println(localFile1.getAbsolutePath() + " is uploaded to " + refFile1.getPath());
			} else {
				System.out.println(localFile1.getAbsolutePath() + " is not uploaded to " + refFile1.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output1, true);
		}

		FileRefOutputStream output2 = null;
		try {
			output2 = new FileRefOutputStream(refFile2);
			FileUtil.copyFileToOutputStream(localFile2, output2);

			if (refFile1.exists()) {
				System.out.println(localFile2.getAbsolutePath() + " is uploaded to " + refFile2.getPath());
			} else {
				System.out.println(localFile2.getAbsolutePath() + " is not uploaded to " + refFile2.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output2, true);
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
	public void test008_downloadUsingInputStream() throws IOException {
		System.out.println("--- --- --- test008_downloadUsingInputStream() --- --- ---");

		File localFile1 = new File("C:/downloads/test_target2/test2/readme(D).txt");
		File localFile2 = new File("C:/downloads/test_target2/test2/Smokingpipes_2016-01-22(D).pdf");
		File localFile3 = new File("C:/downloads/test_target2/test2/doc(D).json");
		File localFile4 = new File("C:/downloads/test_target2/test2/DownloadAllNumbers(D).txt");

		FileRef fileRef1 = FileRef.newInstance(fs, "/test2/readme.txt");
		InputStream input1 = null;
		try {
			input1 = new FileRefInputStream(fileRef1);
			FileUtil.copyInputStreamToFile(input1, localFile1);

			if (localFile1.exists()) {
				System.out.println(fileRef1.getPath() + " is downloaded to " + localFile1.getAbsolutePath());
			} else {
				System.out.println(fileRef1.getPath() + " is not downloaded to " + localFile1.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input1, true);
		}

		FileRef fileRef2 = FileRef.newInstance(fs, "/test2/Smokingpipes_2016-01-22.pdf");
		FileRefInputStream input2 = null;
		try {
			input2 = new FileRefInputStream(fileRef2);
			FileUtil.copyInputStreamToFile(input2, localFile2);

			if (localFile2.exists()) {
				System.out.println(fileRef2.getPath() + " is downloaded to " + localFile2.getAbsolutePath());
			} else {
				System.out.println(fileRef2.getPath() + " is not downloaded to " + localFile2.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input2, true);
		}

		FileRef fileRef3 = FileRef.newInstance(fs, "/test2/doc.json");
		FileRefInputStream input3 = null;
		try {
			input3 = new FileRefInputStream(fileRef3);
			FileUtil.copyInputStreamToFile(input3, localFile3);

			if (localFile3.exists()) {
				System.out.println(fileRef3.getPath() + " is downloaded to " + localFile3.getAbsolutePath());
			} else {
				System.out.println(fileRef3.getPath() + " is not downloaded to " + localFile3.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input3, true);
		}

		FileRef fileRef4 = FileRef.newInstance(fs, "/test2/DownloadAllNumbers.txt");
		FileRefInputStream input4 = null;
		try {
			input4 = new FileRefInputStream(fileRef4);
			FileUtil.copyInputStreamToFile(input4, localFile4);

			if (localFile4.exists()) {
				System.out.println(fileRef4.getPath() + " is downloaded to " + localFile4.getAbsolutePath());
			} else {
				System.out.println(fileRef4.getPath() + " is not downloaded to " + localFile4.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input4, true);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_deleteFiles() throws IOException {
		System.out.println("--- --- --- test009_deleteFiles() --- --- ---");

		FileRef dirRef1 = FileRef.newInstance(fs, "/test2/images");
		FileRef fileRef2 = FileRef.newInstance(fs, "/test2/readme.txt");
		FileRef dirRef3 = FileRef.newInstance(fs, "/test2");

		boolean succeed1 = dirRef1.delete();
		boolean succeed2 = fileRef2.delete();
		boolean succeed3 = dirRef3.delete();

		System.out.println(dirRef1.getPath() + " is deleted? " + succeed1);
		System.out.println(fileRef2.getPath() + " is deleted? " + succeed2);
		System.out.println(dirRef3.getPath() + " is deleted? " + succeed3);

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
