package org.nb.mgm.client.test;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.IMetaSector;
import org.nb.mgm.client.api.IMetaSpace;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManagementTest {

	protected ManagementClient mgm;

	public ManagementTest() {
		this.mgm = getManagement();
	}

	protected void setUp() {
		this.mgm = getManagement();
	}

	protected ManagementClient getManagement() {
		return ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getMachines() {
		System.out.println("--- --- --- test001_getMachines() --- --- ---");
		try {
			List<IMachine> machines = mgm.getMachines();
			for (IMachine machine : machines) {
				System.out.println(machine.toString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test002_getHomes() {
		System.out.println("--- --- --- test002_getHomes() --- --- ---");
		try {
			List<IMachine> machines = mgm.getMachines();
			for (IMachine machine : machines) {
				String machineId = machine.getId();
				System.out.println("machineId = " + machineId);

				List<IHome> homes = mgm.getHomes(machineId);
				for (IHome home : homes) {
					System.out.println("\t" + home.toString());
				}

				System.out.println();
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test003_getMetaSectors() {
		System.out.println("--- --- --- test003_getMetaSectors() --- --- ---");
		try {
			List<IMetaSector> metaSectors = mgm.getMetaSectors();
			for (IMetaSector metaSector : metaSectors) {
				System.out.println(metaSector.toString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test004_getMetaSpaces() {
		System.out.println("--- --- --- test004_getMetaSpaces() --- --- ---");
		try {
			List<IMetaSector> metaSectors = mgm.getMetaSectors();
			for (IMetaSector metaSector : metaSectors) {
				String metaSectorId = metaSector.getId();
				System.out.println("metaSectorId = " + metaSectorId);

				List<IMetaSpace> metaSpaces = mgm.getMetaSpaces(metaSectorId);
				for (IMetaSpace metaSpace : metaSpaces) {
					System.out.println("\t" + metaSpace.toString());
				}
				System.out.println();
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(ManagementTest.class);
		System.out.println("--- --- --- ManagementTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
