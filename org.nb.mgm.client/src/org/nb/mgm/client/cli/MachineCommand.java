package org.nb.mgm.client.cli;

import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.Management;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class MachineCommand implements Annotated {

	protected static String[] MACHINE_TITLES = new String[] { "ID", "Name", "IP Address", "Description" };

	protected BundleContext bundleContext;

	@Dependency
	protected Management management;

	/**
	 * 
	 * @param bundleContext
	 */
	public MachineCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("MachineCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lmachines", "addmachine", "updatemachine", "removemachine" });
		OSGiServiceUtil.register(this.bundleContext, MachineCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("MachineCommand.stop()");

		OSGiServiceUtil.unregister(MachineCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void managementSet() {
	}

	@DependencyUnfullfilled
	public void managementUnset() {
	}

	/**
	 * List Machines.
	 * 
	 * Command: lmachines
	 * 
	 * @param mgm
	 * @throws ClientException
	 */
	@Descriptor("List Machines")
	public void lmachines() throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		List<IMachine> machines = this.management.getMachines(null);
		String[][] rows = new String[machines.size()][MACHINE_TITLES.length];
		int rowIndex = 0;
		for (IMachine machine : machines) {
			rows[rowIndex++] = new String[] { machine.getId(), machine.getName(), machine.getIpAddress(), machine.getDescription() };
		}
		PrettyPrinter.prettyPrint(MACHINE_TITLES, rows);
	}

	/**
	 * Add a Machine.
	 * 
	 * Command: addmachine -name <machineName> -ip <ipAddress> -desc <machineDescription>
	 * 
	 * @param name
	 * @param ipAddress
	 * @param description
	 */
	@Descriptor("Add Machine")
	public void addmachine( //
			// Parameters
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ipAddress" }, absentValue = "") String ipAddress, // required
			@Descriptor("Machine Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// name is required for creating new Machine
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			// machineId is required for adding new Machine.
			if ("".equals(ipAddress)) {
				System.out.println("Please specify -ip parameter.");
				return;
			}

			IMachine newMachine = this.management.addMachine(name, ipAddress, description);
			if (newMachine != null) {
				System.out.println("New Machine is added. ");
			} else {
				System.out.println("New Machine is not added.");
			}

		} catch (ClientException e) {
			System.out.println("Cannot added new Machine. " + e.getMessage());
		}
	}

	/**
	 * Update Machine information.
	 * 
	 * Command: updatemachine -machineid <machineId> -name <newMachineName> -ip <newMachineIpAddress> -desc <newMachineDescription>
	 * 
	 * @param machineId
	 */
	@Descriptor("Update Machine")
	public void updatemachine( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // required
			@Descriptor("New Machine name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newMachineName, // optional
			@Descriptor("New Machine name") @Parameter(names = { "-ip", "--ipAddress" }, absentValue = "null") String newIpAddress, // optional
			@Descriptor("New Machine description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newMachineDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// machineId is required for updating a Machine
		if ("".equals(machineId)) {
			System.out.println("Please specify -id parameter.");
			return;
		}

		try {
			IMachine machine = this.management.getMachine(machineId);
			if (machine == null) {
				System.out.println("Machine is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newMachineName)) {
				machine.setName(newMachineName);
				isUpdated = true;
			}
			if (!"null".equals(newIpAddress)) {
				machine.setIpAddress(newIpAddress);
				isUpdated = true;
			}
			if (!"null".equals(newMachineDescription)) {
				machine.setDescription(newMachineDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = machine.update();
				if (succeed) {
					System.out.println("Machine is updated. ");
				} else {
					System.out.println("Failed to update Machine.");
				}
			} else {
				System.out.println("Machine is not updated.");
			}

		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Remove a Machine.
	 * 
	 * Command: removemachine -machineid <machineId>
	 * 
	 * @param machineId
	 */
	@Descriptor("Remove a Machine")
	public void removemachine( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// machineId is required for deleting a Machine
			if ("".equals(machineId)) {
				System.out.println("Please specify -machineid parameter.");
				return;
			}

			boolean succeed = this.management.removeMachine(machineId);
			if (succeed) {
				System.out.println("Machine is removed. ");
			} else {
				System.out.println("Failed to remove Machine.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to remove Machine. " + e.getMessage());
		}
	}

}
