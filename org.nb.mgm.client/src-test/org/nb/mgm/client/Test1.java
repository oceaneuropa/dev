package org.nb.mgm.client;

import java.util.List;

import osgi.mgm.common.util.ClientException;

public class Test1 {

	public static void main(String[] args) {
		Management mgm = MgmFactory.createManagement("http://127.0.0.1:9090", "/mgm", "admin", "123");
		addMachineTest1(mgm);
		getMachinesTest1(mgm);
	}

	public static void addMachineTest1(Management mgm) {
		try {
			for (int i = 1; i <= 10; i++) {
				Machine newMachine = mgm.addMachine("m" + i, "machine" + i, "127.0.0." + i);
				if (newMachine == null) {
					System.out.println("newMachine is: null");
				} else {
					System.out.println("newMachine is: " + newMachine.toString());
				}
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

	public static void getMachinesTest1(Management mgm) {
		try {
			List<Machine> machines = mgm.getMachines();
			System.out.println("machines.size() = " + machines.size());

			for (Machine machine : machines) {
				System.out.println(machine.toString());
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
