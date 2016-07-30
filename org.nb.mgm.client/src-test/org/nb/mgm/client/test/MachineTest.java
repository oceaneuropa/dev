package org.nb.mgm.client.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.client.api.ManagementFactory;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MachineTest {

	protected ManagementClient management;

	public MachineTest() {
		this.management = getManagement();
	}

	protected void setUp() {
		this.management = getManagement();
	}

	protected ManagementClient getManagement() {
		return ManagementFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getMachines() {
		System.out.println("--- --- --- test001_getMachines() --- --- ---");
		try {
			List<IMachine> machines = this.management.getMachines();
			for (IMachine machine : machines) {
				String machineName = machine.getName();
//				if (!machineName.startsWith("Machine")) {
//					continue;
//				}

				System.out.println(machine.toString());

				Map<String, Object> machineProperties = machine.getProperties();
				System.out.println("\tproperties (size=" + machineProperties.size() + "):");
				for (Iterator<String> propNameItor = machineProperties.keySet().iterator(); propNameItor.hasNext();) {
					String propName = propNameItor.next();
					Object propValue = machineProperties.get(propName);
					Class<?> clazz = propValue != null ? propValue.getClass() : null;
					System.out.println("\t\t" + propName + "=" + propValue + "(" + (clazz != null ? clazz.getName() : "null") + ")");
				}
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
			List<IMachine> machines = this.management.getMachines();
			for (IMachine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				boolean succeed = this.management.removeMachine(machine.getId());

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

				IMachine newMachine = this.management.addMachine(machineName, ipAddress, description);
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
			List<IMachine> machines = this.management.getMachines();
			for (IMachine machine : machines) {
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

	@Ignore
	public void test005_postMachineProperties() {
		System.out.println("--- --- --- test005_postMachineProperties() --- --- ---");
		try {
			List<IMachine> machines = this.management.getMachines();
			for (IMachine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();

				if ("Machine1".equals(machineName) || "Machine2".equals(machineName) || "Machine3".equals(machineName)) {
					Map<String, Object> newProperties = new LinkedHashMap<String, Object>();
					newProperties.put("date1", new Date());
					newProperties.put("date2", new Date());
					newProperties.put("float1", new Float(5.01));
					newProperties.put("float2", new Float(5.02));
					newProperties.put("long1", new Long(5001));
					newProperties.put("long2", new Long(5002));
					newProperties.put("string1", "true");
					newProperties.put("string2", "65.43");
					newProperties.put("boolean1", true);
					newProperties.put("boolean2", false);

					boolean succeed = machine.setProperties(newProperties);
					if (succeed) {
						System.out.println("\tMachine '" + machineName + "' (" + machineId + ") properties are updated.");
					} else {
						System.out.println("\tMachine '" + machineName + "' (" + machineId + ") properties failed to be updated.");
					}
				}
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test006_removeMachineProperties() {
		System.out.println("--- --- --- test006_removeMachineProperties() --- --- ---");
		try {
			List<IMachine> machines = management.getMachines();
			for (IMachine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();

				if ("Machine1".equals(machineName) || "Machine2".equals(machineName) || "Machine3".equals(machineName)) {
					List<String> propertyNames = new ArrayList<String>();
					propertyNames.add("machine_date1");
					propertyNames.add("machine_date2");
					propertyNames.add("machine_float1");
					propertyNames.add("machine_float2");
					propertyNames.add("machine_long1");
					propertyNames.add("machine_long2");
					propertyNames.add("machine_string1");
					propertyNames.add("machine_string2");
					propertyNames.add("machine_string3");

					boolean succeed = machine.removeProperties(propertyNames);
					if (succeed) {
						System.out.println("\tMachine '" + machineName + "' (" + machineId + ") properties are removed.");
					} else {
						System.out.println("\tMachine '" + machineName + "' (" + machineId + ") properties failed to be removed.");
					}
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
