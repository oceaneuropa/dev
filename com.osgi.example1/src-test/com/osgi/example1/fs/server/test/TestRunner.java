package com.osgi.example1.fs.server.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * http://www.tutorialspoint.com/junit/junit_ignore_test.htm
 * 
 */
public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(LocalFileSystemTest.class);

		System.out.println("--- --- --- TestRunner.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}