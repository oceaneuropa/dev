package org.orbit.infra.connector.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(IndexServiceTestWin.class);

		System.out.println("--- --- --- TestRunner.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
