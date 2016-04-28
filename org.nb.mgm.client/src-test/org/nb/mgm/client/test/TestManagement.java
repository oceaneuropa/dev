package org.nb.mgm.client.test;

import java.util.List;

import org.junit.Test;
import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MetaSector;
import org.nb.mgm.client.api.MetaSpace;
import org.nb.mgm.client.api.MgmFactory;
import org.origin.common.rest.client.ClientException;

public class TestManagement {

	protected Management mgm;

	public TestManagement() {
		this.mgm = getManagement();
	}

	protected void setUp() {
		this.mgm = getManagement();
	}

	protected Management getManagement() {
		return MgmFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void testGetMachines() {
		System.out.println("--- --- --- testGetMachines() --- --- ---");
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
	public void testGetHomes() {
		System.out.println("--- --- --- testGetHomes() --- --- ---");
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
	public void testGetMetaSectors() {
		System.out.println("--- --- --- testGetMetaSectors() --- --- ---");
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
	public void testGetMetaSpaces() {
		System.out.println("--- --- --- testGetMetaSpaces() --- --- ---");
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

}
