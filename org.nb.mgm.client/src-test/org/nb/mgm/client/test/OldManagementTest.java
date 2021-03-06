package org.nb.mgm.client.test;

import java.util.List;

import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.IMetaSector;
import org.nb.mgm.client.api.IMetaSpace;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;

public class OldManagementTest {

	public static void main(String[] args) {
		ManagementClient mgm = ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");

		// addMachine(mgm);
		// addMetaSector(mgm);
		// addHomeTest1(mgm, "b51ed9ef-1d68-4c78-b616-f4b96ce23f7b", 0);
		// getHomesTest1(mgm, "b51ed9ef-1d68-4c78-b616-f4b96ce23f7b");

		getMachines(mgm);
		getMetaSector(mgm);
	}

	public static void addMachine(ManagementClient mgm) {
		try {
			for (int i = 0; i < 3; i++) {
				IMachine newMachine = mgm.addMachine("m" + i, "127.0.0." + i, "machine" + i);
				if (newMachine == null) {
					System.out.println("newMachine is: null");
				} else {
					System.out.println("newMachine is: " + newMachine.toString());

					String machineId = newMachine.getId();
					for (int j = 1; j <= 2; j++) {
						mgm.addHome(machineId, "home" + i + j, "home_" + i + "_" + j);
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void getMachines(ManagementClient mgm) {
		System.out.println();
		try {
			List<IMachine> machines = mgm.getMachines();
			// System.out.println("machines.size() = " + machines.size());

			for (IMachine machine : machines) {
				System.out.println(machine.toString());

				String machineId = machine.getId();
				List<IHome> homes = mgm.getHomes(machineId);
				// System.out.println("\thomes.size() = " + homes.size());
				for (IHome home : homes) {
					System.out.println("\t" + home.toString());
				}
				System.out.println();
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void addMetaSector(ManagementClient mgm) {
		try {
			for (int i = 1; i <= 3; i++) {
				IMetaSector newMetaSector = mgm.addMetaSector("sector" + i, "meta sector" + i);
				if (newMetaSector == null) {
					System.out.println("newMetaSector is: null");
				} else {
					System.out.println("newMetaSector is: " + newMetaSector.toString());

					String metaSectorId = newMetaSector.getId();
					for (int j = 1; j <= 2; j++) {
						mgm.addMetaSpace(metaSectorId, "space" + i + j, "meta_space" + i + "_" + j);
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void getMetaSector(ManagementClient mgm) {
		try {
			List<IMetaSector> metaSectors = mgm.getMetaSectors();
			// System.out.println("metaSectors.size() = " + metaSectors.size());

			for (IMetaSector metaSector : metaSectors) {
				System.out.println(metaSector.toString());

				String metaSectorId = metaSector.getId();
				List<IMetaSpace> metaSpaces = mgm.getMetaSpaces(metaSectorId);
				// System.out.println("\tmetaSpaces.size() = " + metaSpaces.size());
				for (IMetaSpace metaSpace : metaSpaces) {
					System.out.println("\t" + metaSpace.toString());
				}
				System.out.println();
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
