package org.nb.home.client.test;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.nb.home.client.api.HomeAgentFactory;
import org.nb.home.client.api.HomeAgent;
import org.origin.common.rest.client.ClientException;

public class HomeTest {

	protected HomeAgent homeMgm;

	public HomeTest() {
		this.homeMgm = getHomeControl();
	}

	protected void setUp() {
		this.homeMgm = getHomeControl();
	}

	protected HomeAgent getHomeControl() {
		return HomeAgentFactory.createHomeAgent("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void testPing() {
		System.out.println("--- --- --- testPing() --- --- ---");
		try {
			int result = this.homeMgm.ping();
			if (result > 0) {
				System.out.println("Home is running.");
			} else {
				System.out.println("Home is down.");
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(HomeTest.class);

		System.out.println("--- --- --- HomeTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}

		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
