package com.osgi.example1.zipfs;

import java.net.URI;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ZipfsTest {

	public ZipfsTest() {
		setup();
	}

	protected void setup() {
	}

	public static FileSystemProvider getZipFSProvider() {
		for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
			if ("jar2".equals(provider.getScheme())) {
				return provider;
			}
		}
		return null;
	}

	@Test
	public void test001() {
		FileSystemProvider provider = getZipFSProvider();
		System.out.println("provider is " + provider);

		try {
			Map<String, ?> env = Collections.emptyMap();
			ZipFileSystem zfs = (ZipFileSystem) provider.newFileSystem(new URI("jar:file:/Users/example/dev/test/zipfs/tmp.zip!/tmp"), env);
			System.out.println("zfs is " + zfs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(ZipfsTest.class);
		System.out.println("--- --- --- ZipfsTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
