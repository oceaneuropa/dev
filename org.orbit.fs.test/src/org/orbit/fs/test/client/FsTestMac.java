package org.orbit.fs.test.client;

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
import org.orbit.fs.api.FileRef;
import org.orbit.fs.api.FileSystem;
import org.orbit.fs.api.FileSystemConfiguration;
import org.orbit.fs.connector.FileRefInputStream;
import org.orbit.fs.connector.FileRefOutputStream;
import org.orbit.fs.connector.FileSystemConfigurationImpl;
import org.orbit.fs.connector.FileSystemImpl;
import org.orbit.fs.connector.ws.FileSystemWSClientHelper;
import org.origin.common.io.FileUtil;
import org.origin.common.io.IOUtil;

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
		FileSystemConfiguration config = new FileSystemConfigurationImpl("http://127.0.0.1:9090", "/fs/v1", "root", "admin");
		return FileSystemImpl.newInstance(config);
	}

	@Ignore
	@Test
	public void test000_deleteAllFiles() throws IOException {
		System.out.println("--- --- --- test000_deleteAllFiles() --- --- ---");

		FileRef[] fileRefs = FileRef.listRoots(fs);
		for (FileRef fileRef : fileRefs) {
			boolean succeed = fileRef.delete();
			System.out.println(fileRef.getPath() + " is deleted? " + succeed);
		}

		System.out.println();
	}

	@Test
	public void test001_listRoots() throws IOException {
		System.out.println("--- --- --- test001_listRoots() --- --- ---");

		try {
			FileRef[] fileRefs = FileRef.listRoots(fs);
			for (FileRef fileRef : fileRefs) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fs, fileRef, 0);
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
			// FileRef dirRef1 = FileRefImpl.newInstance(fs, "/dir1");
			// FileRef dirRef2 = FileRefImpl.newInstance(fs, "/dir2");
			// FileRef dirRef3 = FileRefImpl.newInstance(fs, "/dir3");
			FileRef dirRef1 = fs.getFile("/dir1");
			FileRef dirRef2 = fs.getFile("/dir2");
			FileRef dirRef3 = fs.getFile("/dir3");

			FileRef[] subFileRefs1 = FileRef.listFiles(dirRef1);
			FileRef[] subFileRefs2 = FileRef.listFiles(dirRef2);
			FileRef[] subFileRefs3 = FileRef.listFiles(dirRef3);

			System.out.println("Path '" + dirRef1.getPath() + "' members:");
			for (FileRef subFile1 : subFileRefs1) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fs, subFile1, 0);
			}
			System.out.println();

			System.out.println("Path '" + dirRef2.getPath() + "' members:");
			for (FileRef subFile2 : subFileRefs2) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fs, subFile2, 0);
			}
			System.out.println();

			System.out.println("Path '" + dirRef3.getPath() + "' members:");
			for (FileRef subFile3 : subFileRefs3) {
				FileSystemWSClientHelper.INSTANCE.walkFolders(fs, subFile3, 0);
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

		// FileRef dirRef1 = FileRefImpl.newInstance(fs, "/newDir/tmp1");
		// FileRef dirRef2 = FileRefImpl.newInstance(fs, "/newDir/tmp2");
		// FileRef dirRef3 = FileRefImpl.newInstance(fs, "/newDir/tmp3");

		// FileRef fileRef1 = FileRefImpl.newInstance(fs, "/newDir/newEmptyFile1.txt");
		// FileRef fileRef2 = FileRefImpl.newInstance(fs, "/newDir/tmp2/newEmptyFile2.txt");
		// FileRef fileRef3 = FileRefImpl.newInstance(fs, "/newDir/tmp3/newEmptyFile3.txt");

		FileRef dirRef1 = fs.getFile("/newDir/tmp1");
		FileRef dirRef2 = fs.getFile("/newDir/tmp2");
		FileRef dirRef3 = fs.getFile("/newDir/tmp3");

		FileRef fileRef1 = fs.getFile("/newDir/newEmptyFile1.txt");
		FileRef fileRef2 = fs.getFile("/newDir/tmp2/newEmptyFile2.txt");
		FileRef fileRef3 = fs.getFile("/newDir/tmp3/newEmptyFile3.txt");

		boolean succeed1 = false;
		if (!dirRef1.exists()) {
			succeed1 = dirRef1.mkdirs();
		} else {
			System.out.println("Path '" + dirRef1.getPath() + "' already exists.");
		}

		boolean succeed2 = false;
		if (!dirRef2.exists()) {
			succeed2 = dirRef2.mkdirs();
		} else {
			System.out.println("Path '" + dirRef2.getPath() + "' already exists.");
		}

		boolean succeed3 = false;
		if (!dirRef3.exists()) {
			succeed3 = dirRef3.mkdirs();
		} else {
			System.out.println("Path '" + dirRef3.getPath() + "' already exists.");
		}

		boolean succeed4 = false;
		if (!fileRef1.exists()) {
			succeed4 = fileRef1.createNewFile();
		} else {
			System.out.println("Path '" + fileRef1.getPath() + "' already exists.");
		}

		boolean succeed5 = false;
		if (!fileRef2.exists()) {
			succeed5 = fileRef2.createNewFile();
		} else {
			System.out.println("Path '" + fileRef2.getPath() + "' already exists.");
		}

		boolean succeed6 = false;
		if (!fileRef3.exists()) {
			succeed6 = fileRef3.createNewFile();
		} else {
			System.out.println("Path '" + fileRef3.getPath() + "' already exists.");
		}

		System.out.println("Path '" + dirRef1.getPath() + "' is created (succeed=" + succeed1 + ")? " + dirRef1.exists());
		System.out.println("Path '" + dirRef2.getPath() + "' is created (succeed=" + succeed2 + ")? " + dirRef2.exists());
		System.out.println("Path '" + dirRef3.getPath() + "' is created (succeed=" + succeed3 + ")? " + dirRef3.exists());
		System.out.println("Path '" + fileRef1.getPath() + "' is created (succeed=" + succeed4 + ")? " + fileRef1.exists());
		System.out.println("Path '" + fileRef2.getPath() + "' is created (succeed=" + succeed5 + ")? " + fileRef2.exists());
		System.out.println("Path '" + fileRef3.getPath() + "' is created (succeed=" + succeed6 + ")? " + fileRef3.exists());

		System.out.println();
		System.out.println(dirRef1);
		System.out.println(dirRef2);
		System.out.println(dirRef3);
		System.out.println(fileRef1);
		System.out.println(fileRef2);
		System.out.println(fileRef3);

		System.out.println();
	}

	@Ignore
	@Test
	public void test004_uploadFiles() throws IOException {
		System.out.println("--- --- --- test004_uploadFiles() --- --- ---");

		File localFile1 = new File("/Users/yayang/Downloads/test_source1/invoke.timeout.zip");
		File localFile2 = new File("/Users/yayang/Downloads/test_source1/japanese_issue.zip");

		// FileRef fileRef1 = FileRefImpl.newInstance(fs, fs.root(), localFile1.getName());
		// FileRef fileRef2 = FileRefImpl.newInstance(fs, fs.root(), localFile2.getName());
		FileRef fileRef1 = fs.getFile(fs.root(), localFile1.getName());
		FileRef fileRef2 = fs.getFile(fs.root(), localFile2.getName());

		boolean succeed1 = fs.uploadFileToFsFile(localFile1, fileRef1);
		boolean succeed2 = fs.uploadFileToFsFile(localFile2, fileRef2);

		System.out.println("Local file '" + localFile1.getAbsolutePath() + "' is uploaded to path '" + fileRef1.getPath() + "' (succeed=" + succeed1 + ")? " + fileRef1.exists());
		System.out.println("Local file '" + localFile2.getAbsolutePath() + "' is uploaded to path '" + fileRef2.getPath() + "' (succeed=" + succeed2 + ")? " + fileRef2.exists());

		System.out.println();
	}

	@Ignore
	@Test
	public void test005_uploadUsingOutputStream() throws IOException {
		System.out.println("--- --- --- test005_uploadUsingOutputStream() --- --- ---");

		File localFile1 = new File("/Users/yayang/Downloads/test_source2/log_01.txt");
		File localFile2 = new File("/Users/yayang/Downloads/test_source2/xsd-sourcedoc-2.10.0.zip");

		// FileRef fileRef1 = FileRefImpl.newInstance(fs, "/log_01.txt");
		// FileRef fileRef2 = FileRefImpl.newInstance(fs, "/xsd-sourcedoc-2.10.0.zip");
		FileRef fileRef1 = fs.getFile("/log_01.txt");
		FileRef fileRef2 = fs.getFile("/xsd-sourcedoc-2.10.0.zip");

		OutputStream output1 = null;
		try {
			output1 = new FileRefOutputStream(fileRef1);
			FileUtil.copyFileToOutputStream(localFile1, output1);
			System.out.println("Local file '" + localFile1.getAbsolutePath() + "' is uploaded to path '" + fileRef1.getPath() + "'? " + fileRef1.exists());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output1, true);
		}

		FileRefOutputStream output2 = null;
		try {
			output2 = new FileRefOutputStream(fileRef2);
			FileUtil.copyFileToOutputStream(localFile2, output2);
			System.out.println("Local file '" + localFile2.getAbsolutePath() + "' is uploaded to path '" + fileRef2.getPath() + "'? " + fileRef2.exists());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output2, true);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test006_uploadDirectories() throws IOException {
		System.out.println("--- --- --- test006_uploadDirectories() --- --- ---");

		// File localDir = new File("/Users/yayang/Downloads/test_source1");
		// FileRef dirRef = fs.root();
		File localDir = new File("/Users/yayang/osgi");
		// FileRef dirRef = FileRefImpl.newInstance(fs, "/osgi");
		FileRef dirRef = fs.getFile("/osgi");

		boolean succeed = fs.uploadDirectoryToFsDirectory(localDir, dirRef, false);
		System.out.println("Local directory '" + localDir.getAbsolutePath() + "' is uploaded to path '" + dirRef.getPath() + "'? " + succeed);

		System.out.println();
	}

	@Ignore
	@Test
	public void test007_downloadFiles() throws IOException {
		System.out.println("--- --- --- test007_downloadFiles() --- --- ---");

		// FileRef fileRef1 = FileRefImpl.newInstance(fs, "/invoke.timeout.zip");
		// FileRef fileRef2 = FileRefImpl.newInstance(fs, "/japanese_issue.zip");
		// FileRef fileRef3 = FileRefImpl.newInstance(fs, "/log_01.txt");
		// FileRef fileRef4 = FileRefImpl.newInstance(fs, "/xsd-sourcedoc-2.10.0.zip");
		FileRef fileRef1 = fs.getFile("/invoke.timeout.zip");
		FileRef fileRef2 = fs.getFile("/japanese_issue.zip");
		FileRef fileRef3 = fs.getFile("/log_01.txt");
		FileRef fileRef4 = fs.getFile("/xsd-sourcedoc-2.10.0.zip");

		File localDir = new File("/Users/yayang/Downloads/test_target2/");
		File localFile1 = new File(localDir, "invoke.timeout(A).zip");
		File localFile2 = new File(localDir, "japanese_issue(A).zip");
		File localFile3 = new File(localDir, "log_01(A).txt");
		File localFile4 = new File(localDir, "xsd-sourcedoc-2.10.0(A).zip");

		boolean succeed1 = fs.downloadFsFileToFile(fileRef1, localFile1);
		boolean succeed2 = fs.downloadFsFileToFile(fileRef2, localFile2);
		boolean succeed3 = fs.downloadFsFileToFile(fileRef3, localFile3);
		boolean succeed4 = fs.downloadFsFileToFile(fileRef4, localFile4);

		System.out.println("Path '" + fileRef1.getPath() + "' is downloaded to local file '" + localFile1.getAbsolutePath() + "'? " + succeed1);
		System.out.println("Path '" + fileRef2.getPath() + "' is downloaded to local file '" + localFile2.getAbsolutePath() + "'? " + succeed2);
		System.out.println("Path '" + fileRef3.getPath() + "' is downloaded to local file '" + localFile3.getAbsolutePath() + "'? " + succeed3);
		System.out.println("Path '" + fileRef4.getPath() + "' is downloaded to local file '" + localFile4.getAbsolutePath() + "'? " + succeed4);

		System.out.println();
	}

	@Ignore
	@Test
	public void test008_downloadDirectories() throws IOException {
		System.out.println("--- --- --- test008_downloadDirectories() --- --- ---");

		File localDir = new File("/Users/yayang/Downloads/test_target1");
		FileRef[] fileRefs = FileRef.listRoots(fs);
		for (FileRef fileRef : fileRefs) {
			boolean succeed = false;
			if (fileRef.isDirectory()) {
				succeed = fs.downloadFsDirectoryToDirectory(fileRef, localDir, true);
			} else {
				succeed = fs.downloadFsFileToDirectory(fileRef, localDir);
			}
			System.out.println("Path '" + fileRef.getPath() + "' is downloaded to local dir '" + localDir.getAbsolutePath() + "'? " + succeed);
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test009_downloadUsingInputStream() throws IOException {
		System.out.println("--- --- --- test009_downloadUsingInputStream() --- --- ---");

		File localDir = new File("/Users/yayang/Downloads/test_target2/");
		File localFile1 = new File(localDir, "invoke.timeout(B).zip");
		File localFile2 = new File(localDir, "japanese_issue(B).zip");
		File localFile3 = new File(localDir, "log_01(B).txt");
		File localFile4 = new File(localDir, "xsd-sourcedoc-2.10.0(B).zip");

		// FileRef fileRef1 = FileRefImpl.newInstance(fs, "/invoke.timeout.zip");
		// FileRef fileRef2 = FileRefImpl.newInstance(fs, "/japanese_issue.zip");
		// FileRef fileRef3 = FileRefImpl.newInstance(fs, "/log_01.txt");
		// FileRef fileRef4 = FileRefImpl.newInstance(fs, "/xsd-sourcedoc-2.10.0.zip");
		FileRef fileRef1 = fs.getFile("/invoke.timeout.zip");
		FileRef fileRef2 = fs.getFile("/japanese_issue.zip");
		FileRef fileRef3 = fs.getFile("/log_01.txt");
		FileRef fileRef4 = fs.getFile("/xsd-sourcedoc-2.10.0.zip");

		InputStream input1 = null;
		try {
			input1 = new FileRefInputStream(fileRef1);
			FileUtil.copyInputStreamToFile(input1, localFile1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input1, true);
		}

		InputStream input2 = null;
		try {
			input2 = new FileRefInputStream(fileRef2);
			FileUtil.copyInputStreamToFile(input2, localFile2);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input2, true);
		}

		InputStream input3 = null;
		try {
			input3 = new FileRefInputStream(fileRef3);
			FileUtil.copyInputStreamToFile(input3, localFile3);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input3, true);
		}

		InputStream input4 = null;
		try {
			input4 = new FileRefInputStream(fileRef4);
			FileUtil.copyInputStreamToFile(input4, localFile4);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input4, true);
		}

		System.out.println("Path '" + fileRef1.getPath() + " is downloaded to '" + localFile1.getAbsolutePath() + "'? " + localFile1.exists());
		System.out.println("Path '" + fileRef2.getPath() + " is downloaded to '" + localFile2.getAbsolutePath() + "'? " + localFile2.exists());
		System.out.println("Path '" + fileRef3.getPath() + " is downloaded to '" + localFile3.getAbsolutePath() + "'? " + localFile3.exists());
		System.out.println("Path '" + fileRef4.getPath() + " is downloaded to '" + localFile4.getAbsolutePath() + "'? " + localFile4.exists());

		System.out.println();
	}

	@Test
	public void test010_deleteFiles() throws IOException {
		System.out.println("--- --- --- test010_deleteFiles() --- --- ---");

		// FileRef fileRef1 = FileRef.newInstance(fs, "/newDir/tmp2/newEmptyFile2.txt");
		// FileRef dirRef2 = FileRef.newInstance(fs, "/newDir/tmp3");
		// FileRef dirRef3 = FileRefImpl.newInstance(fs, "/osgi");
		FileRef dirRef3 = fs.getFile("/osgi");

		// boolean succeed1 = fileRef1.delete();
		// boolean succeed2 = dirRef2.delete();
		boolean succeed3 = dirRef3.delete();

		// System.out.println("Path '" + fileRef1.getPath() + "' is deleted (succeed=" + succeed1 + ")? " + !fileRef1.exists());
		// System.out.println("Path '" + dirRef2.getPath() + "' is deleted (succeed=" + succeed2 + ")? " + !dirRef2.exists());
		System.out.println("Path '" + dirRef3.getPath() + "' is deleted (succeed=" + succeed3 + ")? " + !dirRef3.exists());

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
