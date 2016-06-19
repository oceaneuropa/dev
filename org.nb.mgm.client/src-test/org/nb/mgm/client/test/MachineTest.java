package org.nb.mgm.client.test;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MgmFactory;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MachineTest {

	protected Management mgm;

	public MachineTest() {
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

	@Ignore
	public void test002_deleteMachines() {
		System.out.println("--- --- --- test002_deleteMachines() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				boolean succeed = mgm.deleteMachine(machine.getId());

				if (succeed) {
					System.out.println("Machine '" + machineName + "' (" + machineId + ") is deleted.");
				} else {
					System.out.println("Machine '" + machineName + "' (" + machineId + ") failed to be deleted.");
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addMachines() {
		System.out.println("--- --- --- test003_addMachines() --- --- ---");

		try {
			for (int i = 0; i < 9; i++) {
				String machineName = "Machine" + (i + 1);
				String ipAddress = "127.0.0." + (i + 1);
				String description = "description of " + machineName;

				Machine newMachine = mgm.addMachine(machineName, ipAddress, description);
				if (newMachine == null) {
					System.out.println("new Machine is: null");
				} else {
					System.out.println("new Machine is: " + newMachine.toString());
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test004_updateMachines() {
		System.out.println("--- --- --- test004_updateMachines() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();

				boolean isNameChanged = false;
				if ("Machine0(1)".equals(machineName)) {
					machine.setName("Machine4");
					isNameChanged = true;
				}
				if ("Machine1(1)".equals(machineName)) {
					machine.setName("Machine5");
					isNameChanged = true;
				}
				if ("Machine2(1)".equals(machineName)) {
					machine.setName("Machine6");
					isNameChanged = true;
				}
				if ("Machine0(2)".equals(machineName)) {
					machine.setName("Machine7");
					isNameChanged = true;
				}
				if ("Machine1(2)".equals(machineName)) {
					machine.setName("Machine8");
					isNameChanged = true;
				}
				if ("Machine2(2)".equals(machineName)) {
					machine.setName("Machine9");
					isNameChanged = true;
				}

				if (isNameChanged) {
					String description = "description of " + machine.getName();
					machine.setDescription(description);
					boolean succeed = machine.update();

					if (succeed) {
						System.out.println("Machine '" + machineName + "' (" + machineId + ") is updated.");
					} else {
						System.out.println("Machine '" + machineName + "' (" + machineId + ") failed to be updated.");
					}
				} else {
					System.out.println("Machine '" + machineName + "' (" + machineId + ") is not required to be updated.");
				}
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
		Result result = JUnitCore.runClasses(MachineTest.class);
		System.out.println("--- --- --- MachineTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
