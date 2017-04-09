package com.osgi.example1.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PassThroughTest {

	public PassThroughTest() {
		setup();
	}

	protected void setup() {
	}

	@Ignore
	// @Test
	public void test001() {
		System.out.println("--- --- test001() --- --- ");

		try {
			List<FileSystemProvider> fsProviders = FileSystemProvider.installedProviders();
			System.out.println();

			System.out.println(MessageFormat.format("PassThroughTest.test001() fsProviders.size = {0}", new Object[] { fsProviders.size() }));

			System.out.println();
			for (FileSystemProvider fsProvider : fsProviders) {
				System.out.println(MessageFormat.format("PassThroughTest.test001() FileSystemProvider [{0}] (scheme={1})", new Object[] { fsProvider, fsProvider.getScheme() }));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	// @Test
	public void test002() {
		System.out.println("--- --- test002() --- --- ");

		try {
			// java.io.FileSystem is not overwritten
			// File[] rootFiles = File.listRoots();
			// for (File rootFile : rootFiles) {
			// File[] files = rootFile.listFiles();
			// System.out.println(MessageFormat.format("Root file [{0}] listFiles() length: {1}", new Object[] { rootFile, rootFiles.length }));
			// for (File file : files) {
			// System.out.println(file.getAbsolutePath());
			// }
			// }

			// java.nio.file.FileSystem can be overwritten
			Iterable<Path> rootDirsItab = FileSystems.getDefault().getRootDirectories();
			for (Iterator<Path> rootDirsItor = rootDirsItab.iterator(); rootDirsItor.hasNext();) {
				Path rootPath = rootDirsItor.next();
				System.out.println("rootPath = " + rootPath.toString());

				// Path directoryPath = Paths.get("C:", "Program Files/Java/jdk1.7.0_40/src/java/nio/file");
				if (Files.isDirectory(rootPath)) {
					DirectoryStream<Path> paths = Files.newDirectoryStream(rootPath);
					for (Path path : paths) {
						System.out.println("\t" + path.toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	/**
	 * @see http://howtodoinjava.com/best-practices/java-8-list-all-files-example/
	 * 
	 * @see http://stackoverflow.com/questions/16581724/how-to-use-nio-to-write-inputstream-to-file
	 * 
	 */
	// @Ignore
	@Test
	public void test003() {
		FileInputStream is = null;
		try {
			System.out.println("--- --- test003() --- --- ");

			// Files.list(Paths.get(".")).forEach(System.out::println);

			File file = new File("test.txt");
			is = new FileInputStream(file);

			Files.copy(is, Paths.get("test2.txt"));

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(PassThroughTest.class);
		System.out.println("--- --- --- PassThroughTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
