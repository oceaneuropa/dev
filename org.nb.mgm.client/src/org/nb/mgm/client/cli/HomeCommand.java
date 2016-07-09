package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IHome;
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

public class HomeCommand implements Annotated {

	protected static String[] MACHINE_HOME_TITLES = new String[] { "Machine", "ID", "Name", "Description" };

	protected BundleContext bundleContext;

	@Dependency
	protected Management management;

	/**
	 * 
	 * @param bundleContext
	 */
	public HomeCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("HomeCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lhomes", "addhome", "updatehome", "removehome" });
		OSGiServiceUtil.register(this.bundleContext, HomeCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("HomeCommand.stop()");

		OSGiServiceUtil.unregister(HomeCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void managementSet() {
	}

	@DependencyUnfullfilled
	public void managementUnset() {
	}

	/**
	 * List Homes.
	 * 
	 * Command: lhomes
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	@Descriptor("List Homes")
	public void lhomes( //
			// parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId // optional
	) throws ClientException {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		List<IHome> homes = new ArrayList<IHome>();

		List<IMachine> machines = this.management.getMachines();
		for (IMachine machine : machines) {
			String currMachineId = machine.getId();

			if ("".equals(machineId) || currMachineId.equals(machineId)) {
				List<IHome> currHomes = machine.getHomes();

				if ("".equals(homeId)) {
					if (!currHomes.isEmpty()) {
						homes.addAll(currHomes);
					}
				} else {
					for (IHome currHome : currHomes) {
						String currHomeId = currHome.getId();

						if (currHomeId.equals(homeId)) {
							homes.add(currHome);
						}
					}
				}
			}
		}

		String[][] rows = new String[homes.size()][MACHINE_HOME_TITLES.length];
		int rowIndex = 0;
		String prevMachineId = null;
		for (IHome home : homes) {
			IMachine machine = home.getMachine();
			String currMachineId = machine.getId();

			String machineText = machine.getName() + " (" + currMachineId + ")";
			if (prevMachineId != null && prevMachineId.equals(currMachineId)) {
				machineText = "";
			}
			rows[rowIndex++] = new String[] { machineText, home.getId(), home.getName(), home.getDescription() };

			prevMachineId = currMachineId;
		}

		PrettyPrinter.prettyPrint(MACHINE_HOME_TITLES, rows);
	}

	/**
	 * Add a Home.
	 * 
	 * Command: addhome -machineid <machineId> -name <homeName> -desc <homeDescription>
	 * 
	 * @param machineId
	 * @param name
	 * @param description
	 */
	@Descriptor("Create Home")
	public void addhome( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // required
			@Descriptor("Home Name") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, // required
			@Descriptor("Home Description") @Parameter(names = { "-desc", "--description" }, absentValue = "") String description // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// machineId is required for creating new Home
			if ("".equals(machineId)) {
				System.out.println("Please specify -machineid parameter.");
				return;
			}

			// name is required for creating new Home
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			IMachine machine = this.management.getMachine(machineId);
			if (machine == null) {
				System.out.println("Machine cannot be found.");
				return;
			}

			IHome newHome = machine.addHome(name, description);
			if (newHome != null) {
				System.out.println("New Home is added. ");
			} else {
				System.out.println("New Home is not added.");
			}

		} catch (ClientException e) {
			System.out.println("Cannot add new Home. " + e.getMessage());
		}
	}

	/**
	 * Update Home information.
	 * 
	 * Command: updatehome -machineid <machineId> -homeid <homeId> -name <newHomeName> -desc <newHomeDescription>
	 * 
	 * @param machineId
	 * @param homeId
	 * @param newHomeName
	 * @param newHomeDescription
	 */
	@Descriptor("Update Home")
	public void updatehome( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId, // required
			@Descriptor("New Home name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String newHomeName, // optional
			@Descriptor("New Home description") @Parameter(names = { "-desc", "--description" }, absentValue = "null") String newHomeDescription // optional
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		// homeId is required for updating a Home
		if ("".equals(homeId)) {
			System.out.println("Please specify -homeid parameter.");
			return;
		}

		// homeName cannot be updated to empty string
		if ("".equals(newHomeName)) {
			System.out.println("Home name cannot be updated to empty string.");
			return;
		}

		try {
			IHome home = null;
			if ("".equals(machineId)) {
				home = this.management.getHome(homeId);
			} else {
				home = this.management.getHome(machineId, homeId);
			}
			if (home == null) {
				System.out.println("Home is not found.");
				return;
			}

			boolean isUpdated = false;
			if (!"null".equals(newHomeName)) {
				home.setName(newHomeName);
				isUpdated = true;
			}
			if (!"null".equals(newHomeDescription)) {
				home.setDescription(newHomeDescription);
				isUpdated = true;
			}

			if (isUpdated) {
				boolean succeed = home.update();
				if (succeed) {
					System.out.println("Home is updated. ");
				} else {
					System.out.println("Failed to update Home.");
				}
			} else {
				System.out.println("Home is not updated.");
			}

		} catch (ClientException e) {
			System.out.println("ClientException: " + e.getMessage());
		}
	}

	/**
	 * Remove a Home.
	 * 
	 * Command: removehome -machineid <machineId> -homeid <homeId>
	 * 
	 * @param machineId
	 * @param homeId
	 */
	@Descriptor("Remove Home from Machine")
	public void removehome( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId // required
	) {
		if (this.management == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			// homeId is required for removing a Home
			if ("".equals(homeId)) {
				System.out.println("Please specify -homeid parameter.");
				return;
			}

			boolean succeed = false;
			if ("".equals(machineId)) {
				// machineId is not specified
				succeed = this.management.removeHome(homeId);
			} else {
				// machineId is specified
				succeed = this.management.removeHome(machineId, homeId);
			}

			if (succeed) {
				System.out.println("Home is removed. ");
			} else {
				System.out.println("Failed to remove Home.");
			}

		} catch (ClientException e) {
			System.out.println("Failed to remove Home. " + e.getMessage());
		}
	}

}
