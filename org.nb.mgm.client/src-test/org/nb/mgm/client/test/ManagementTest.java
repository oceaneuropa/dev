package org.nb.mgm.client.test;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MetaSector;
import org.nb.mgm.client.api.MetaSpace;
import org.nb.mgm.client.api.MgmFactory;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManagementTest {

	protected Management mgm;

	public ManagementTest() {
		this.mgm = getManagement();
	}

	protected void setUp() {
		this.mgm = getManagement();
	}

	protected Management getManagement() {
		return MgmFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getMachines() {
		System.out.println("--- --- --- test001_getMachines() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
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
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				System.out.println("machineId = " + machineId);

				List<Home> homes = mgm.getHomes(machineId);
				for (Home home : homes) {
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
			List<MetaSector> metaSectors = mgm.getMetaSectors();
			for (MetaSector metaSector : metaSectors) {
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
			List<MetaSector> metaSectors = mgm.getMetaSectors();
			for (MetaSector metaSector : metaSectors) {
				String metaSectorId = metaSector.getId();
				System.out.println("metaSectorId = " + metaSectorId);

				List<MetaSpace> metaSpaces = mgm.getMetaSpaces(metaSectorId);
				for (MetaSpace metaSpace : metaSpaces) {
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
