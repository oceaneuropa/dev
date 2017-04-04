package com.osgi.example1.nio;

import java.io.File;
import java.nio.file.spi.FileSystemProvider;
import java.text.MessageFormat;
import java.util.List;

import org.junit.FixMethodOrder;
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

	@Test
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

	@Test
	public void test002() {
		System.out.println("--- --- test002() --- --- ");

		try {
			File[] rootFiles = File.listRoots();
			for (File rootFile : rootFiles) {
				File[] files = rootFile.listFiles();
				System.out.println(MessageFormat.format("Root file [{0}] listFiles() length: {1}", new Object[] { rootFile, rootFiles.length }));
				for (File file : files) {
					System.out.println(file.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
