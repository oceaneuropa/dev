package org.nb.mgm.home.client;

import org.junit.Test;
import org.nb.mgm.home.client.api.HomeManagement;
import org.nb.mgm.home.client.api.HomeMgmFactory;
import org.origin.common.rest.client.ClientException;

public class TestHomeManagement {

	protected HomeManagement homeMgm;

	public TestHomeManagement() {
		this.homeMgm = getManagement();
	}

	protected void setUp() {
		this.homeMgm = getManagement();
	}

	protected HomeManagement getManagement() {
		return HomeMgmFactory.createHomeManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void testPing() {
		System.out.println("--- --- --- testPing() --- --- ---");
		try {
			int result = this.homeMgm.ping();
			if (result > 0) {
				System.out.println("Home management server is running.");
			} else {
				System.out.println("Home management server is down.");
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

}
