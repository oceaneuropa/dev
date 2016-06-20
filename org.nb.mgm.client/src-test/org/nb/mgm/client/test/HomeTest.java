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
import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MgmFactory;
import org.origin.common.rest.client.ClientException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomeTest {

	protected Management mgm;

	public HomeTest() {
		this.mgm = getManagement();
	}

	protected void setUp() {
		this.mgm = getManagement();
	}

	protected Management getManagement() {
		return MgmFactory.createManagement("http://127.0.0.1:9090", "admin", "123");
	}

	@Test
	public void test001_getHomes() {
		System.out.println("--- --- --- test001_getHomes() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				List<Home> homes = machine.getHomes();

				if (!machineName.startsWith("Machine")) {
					continue;
				}

				System.out.println("Machine '" + machineName + "' (" + machineId + ") homes (size=" + homes.size() + "):");
				for (Home home : homes) {
					System.out.println("\t" + home.toString());

					Map<String, Object> properties = home.getProperties();
					System.out.println("\t\tproperties (size=" + properties.size() + "):");
					for (Iterator<String> propNameItor = properties.keySet().iterator(); propNameItor.hasNext();) {
						String propName = propNameItor.next();
						Object propValue = properties.get(propName);
						Class<?> clazz = propValue != null ? propValue.getClass() : null;
						System.out.println("\t\t\t" + propName + "=" + propValue + "(" + (clazz != null ? clazz.getName() : "null") + ")");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test002_deleteHomes() {
		System.out.println("--- --- --- test002_deleteHomes() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				List<Home> homes = machine.getHomes();

				System.out.println("Machine '" + machineName + "' (" + machineId + ") homes (size=" + homes.size() + "):");
				for (Home home : homes) {
					String homeId = home.getId();
					String homeName = home.getName();

					boolean succeed = mgm.deleteHome(machineId, homeId);
					if (succeed) {
						System.out.println("\tHome '" + homeName + "' (" + homeId + ") is deleted.");
					} else {
						System.out.println("\tHome '" + homeName + "' (" + homeId + ") failed to be deleted.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test003_addHomes() {
		System.out.println("--- --- --- test003_addHomes() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();

				if ("Machine1".equals(machineName)) {
					Home home101 = mgm.addHome(machineId, "Home101", "/Users/yayang/tibco_dev/Home101", "Description of Home101");
					Home home102 = mgm.addHome(machineId, "Home102", "/Users/yayang/tibco_dev/Home102", "Description of Home102");

					if (home101 != null) {
						System.out.println("Home101 is created: " + home101.toString());
					} else {
						System.out.println("Home101 failed to be created.");
					}
					if (home102 != null) {
						System.out.println("Home102 is created: " + home102.toString());
					} else {
						System.out.println("Home102 failed to be created.");
					}

				} else if ("Machine2".equals(machineName)) {
					Home home201 = mgm.addHome(machineId, "Home201", "/Users/yayang/tibco_dev/Home201", "Description of Home201");
					Home home202 = mgm.addHome(machineId, "Home202", "/Users/yayang/tibco_dev/Home202", "Description of Home202");

					if (home201 != null) {
						System.out.println("Home201 is created: " + home201.toString());
					} else {
						System.out.println("Home201 failed to be created.");
					}
					if (home202 != null) {
						System.out.println("Home202 is created: " + home202.toString());
					} else {
						System.out.println("Home202 failed to be created.");
					}

				} else if ("Machine3".equals(machineName)) {
					Home home301 = mgm.addHome(machineId, "Home301", "/Users/yayang/tibco_dev/Home301", "Description of Home301");
					Home home302 = mgm.addHome(machineId, "Home302", "/Users/yayang/tibco_dev/Home302", "Description of Home302");

					if (home301 != null) {
						System.out.println("Home301 is created: " + home301.toString());
					} else {
						System.out.println("Home301 failed to be created.");
					}
					if (home302 != null) {
						System.out.println("Home302 is created: " + home302.toString());
					} else {
						System.out.println("Home302 failed to be created.");
					}
				}
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test004_postHomeProperties() {
		System.out.println("--- --- --- test004_postHomeProperties() --- --- ---");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				List<Home> homes = machine.getHomes();

				if ("Machine1".equals(machineName)) {
					System.out.println("Machine '" + machineName + "' (" + machineId + ") homes (size=" + homes.size() + "):");
					for (Home home : homes) {
						String homeId = home.getId();
						String homeName = home.getName();
						if ("Home101".equals(homeName) || "Home102".equals(homeName)) {
							Map<String, Object> newProperties = new LinkedHashMap<String, Object>();
							newProperties.put("date1", new Date());
							newProperties.put("float1", new Float(1.2));
							newProperties.put("long1", new Long(888));
							newProperties.put("date2", new Date());
							newProperties.put("float2", new Float(2.2));
							newProperties.put("long2", new Long(999));
							newProperties.put("string1", "true");
							newProperties.put("string2", "777");
							newProperties.put("string3", "12.34");

							boolean succeed = home.setProperties(newProperties);
							if (succeed) {
								System.out.println("\tHome '" + homeName + "' (" + homeId + ") properties are updated.");
							} else {
								System.out.println("\tHome '" + homeName + "' (" + homeId + ") properties failed to be updated.");
							}
						}
					}
				}
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Ignore
	public void test005_removeHomeProperties() {
		System.out.println("--- --- --- test005_removeHomeProperties() --- --- ---");
		// properties.put("startTime", new Date());
		// properties.put("name", "testName");
		// properties.put("number", new Integer(10));
		// properties.put("numberStr", "10");
		try {
			List<Machine> machines = mgm.getMachines();
			for (Machine machine : machines) {
				String machineId = machine.getId();
				String machineName = machine.getName();
				List<Home> homes = machine.getHomes();

				if ("Machine1".equals(machineName)) {
					System.out.println("Machine '" + machineName + "' (" + machineId + ") homes (size=" + homes.size() + "):");
					for (Home home : homes) {
						String homeId = home.getId();
						String homeName = home.getName();
						if ("Home101".equals(homeName) || "Home102".equals(homeName)) {
							List<String> propertyNames = new ArrayList<String>();
							propertyNames.add("startTime");
							propertyNames.add("name");
							propertyNames.add("number");
							propertyNames.add("numberStr");

							boolean succeed = home.removeProperties(propertyNames);
							if (succeed) {
								System.out.println("\tHome '" + homeName + "' (" + homeId + ") properties are removed.");
							} else {
								System.out.println("\tHome '" + homeName + "' (" + homeId + ") properties failed to be removed.");
							}
						}
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
		Result result = JUnitCore.runClasses(HomeTest.class);
		System.out.println("--- --- --- HomeTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
