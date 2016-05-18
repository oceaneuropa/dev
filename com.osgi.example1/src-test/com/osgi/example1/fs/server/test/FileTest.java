package com.osgi.example1.fs.server.test;

import java.io.File;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class FileTest {

	public FileTest() {
	}

	public void setUp() {
	}

	@Test
	public void test001() {
		System.out.println("--- --- --- test001 --- --- ---");

		File file = new File("C:/something_not_exists.txt");
		boolean exists = file.exists();
		boolean isFile = file.isFile();
		boolean isDirectory = file.isDirectory();

		System.out.println("File '" + file.getAbsolutePath() + "':");
		System.out.println("exists = " + exists);
		System.out.println("isFile = " + isFile);
		System.out.println("isDirectory = " + isDirectory);

		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(FileTest.class);
		System.out.println("--- --- --- FileTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
