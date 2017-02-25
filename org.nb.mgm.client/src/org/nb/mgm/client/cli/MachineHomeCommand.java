package org.nb.mgm.client.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.ManagementClient;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;

public class MachineHomeCommand implements Annotated {

	protected static String[] HOME_TITLES = new String[] { "ID", "Name", "Description" };
	protected static String[] HOME_TITLES_ALL = new String[] { "Machine", "ID", "Name", "Description", "Properties" };

	protected static String[] HOME_PROPERTY_TITLES = new String[] { "Home", "Properties" };

	protected BundleContext bundleContext;

	@Dependency
	protected ManagementClient mgmClient;

	/**
	 * 
	 * @param bundleContext
	 */
	public MachineHomeCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("HomeCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "nb");
		props.put("osgi.command.function", new String[] { "lhomes", "addhome", "updatehome", "removehome", "lhomeproperties", "sethomeproperty", "removehomeproperty" });
		OSGiServiceUtil.register(this.bundleContext, MachineHomeCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("HomeCommand.stop()");

		OSGiServiceUtil.unregister(MachineHomeCommand.class.getName(), this);
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
	 * @param homeId
	 * @throws ClientException
	 */
	@Descriptor("List Homes")
	public void lhomes( //
			// Options
			@Descriptor("List with detailed information of each Home") @Parameter(names = { "-all", "--all" }, absentValue = "false", presentValue = "true") boolean all, //
			// parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId // optional
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		all = true;

		List<IHome> homes = new ArrayList<IHome>();

		List<IMachine> machines = this.mgmClient.getMachines();
		for (IMachine machine : machines) {
			String currMachineId = machine.getId();
			if ("".equals(machineId) || currMachineId.equals(machineId)) {
				List<IHome> currHomes = machine.getHomes();
				for (IHome currHome : currHomes) {
					String currHomeId = currHome.getId();
					if ("".equals(homeId) || currHomeId.equals(homeId)) {
						homes.add(currHome);
					}
				}
			}
		}

		if (all) {
			List<String[]> items = new ArrayList<String[]>();

			// String prevMachineId = null;
			for (IHome home : homes) {
				IMachine machine = home.getMachine();
				// String currMachineId = machine.getId();
				String machineName = machine.getName();
				// if (prevMachineId != null && prevMachineId.equals(currMachineId)) {
				// machineName = "";
				// }

				String currHomeId = home.getId();
				String homeName = home.getName();
				String homeDesc = home.getDescription();
				// String homeText = "[" + machineName + "]/" + homeName;
				String homeText = homeName;

				Map<String, Object> properties = home.getProperties();
				if (properties.isEmpty()) {
					String[] item = new String[] { machineName, currHomeId, homeText, homeDesc, "" };
					items.add(item);

				} else {
					int i = 0;
					for (Iterator<String> propNameItor = properties.keySet().iterator(); propNameItor.hasNext();) {
						String propName = propNameItor.next();
						Object propValue = properties.get(propName);
						String propText = propName + "=" + propValue;
						if (propValue != null) {
							propText += " (" + propValue.getClass().getName() + ")";
						}
						String[] item = null;
						if (i == 0) {
							item = new String[] { machineName, currHomeId, homeText, homeDesc, propText };
						} else {
							item = new String[] { "", "", "", "", propText };
						}
						items.add(item);

						i++;
					}
				}
				// prevMachineId = currMachineId;
			}

			String[][] rows = new String[items.size()][HOME_TITLES_ALL.length];
			int rowIndex = 0;
			for (String[] item : items) {
				rows[rowIndex++] = item;
			}
			PrettyPrinter.prettyPrint(HOME_TITLES_ALL, rows, homes.size());

		} else {
			String[][] rows = new String[homes.size()][HOME_TITLES.length];
			int rowIndex = 0;
			// String prevMachineId = null;
			for (IHome home : homes) {
				IMachine machine = home.getMachine();
				// String currMachineId = machine.getId();
				String machineName = machine.getName();
				// if (prevMachineId != null && prevMachineId.equals(currMachineId)) {
				// machineName = "";
				// }

				String currHomeId = home.getId();
				String homeName = home.getName();
				String homeDesc = home.getDescription();

				String homeText = "[" + machineName + "]/" + homeName;

				rows[rowIndex++] = new String[] { currHomeId, homeText, homeDesc };
				// prevMachineId = currMachineId;
			}

			PrettyPrinter.prettyPrint(HOME_TITLES, rows, homes.size());
		}
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
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

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

		IMachine machine = this.mgmClient.getMachine(machineId);
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
	}

	/**
	 * Update Home information.
	 * 
	 * Command: updatehome [-machineid <machineId>] -homeid <homeId> -name <newHomeName> -desc <newHomeDescription>
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
	) throws ClientException {
		if (this.mgmClient == null) {
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

		IHome home = null;
		if ("".equals(machineId)) {
			home = this.mgmClient.getHome(homeId);
		} else {
			home = this.mgmClient.getHome(machineId, homeId);
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
	}

	/**
	 * Remove a Home.
	 * 
	 * Command: removehome [-machineid <machineId>] -homeid <homeId>
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
		if (this.mgmClient == null) {
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
				succeed = this.mgmClient.removeHome(homeId);
			} else {
				// machineId is specified
				succeed = this.mgmClient.removeHome(machineId, homeId);
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

	/**
	 * List Home properties.
	 * 
	 * Command: lhomeproperties
	 * 
	 * @param machineId
	 * @param homeId
	 */
	public void lhomeproperties( //
			// parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId // optional
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		List<IHome> homes = new ArrayList<IHome>();

		List<IMachine> machines = this.mgmClient.getMachines();
		for (IMachine machine : machines) {
			String currMachineId = machine.getId();
			if ("".equals(machineId) || currMachineId.equals(machineId)) {
				List<IHome> currHomes = machine.getHomes();
				for (IHome currHome : currHomes) {
					String currHomeId = currHome.getId();
					if ("".equals(homeId) || currHomeId.equals(homeId)) {
						homes.add(currHome);
					}
				}
			}
		}

		int rowSize = 0;
		for (IHome home : homes) {
			Map<String, Object> properties = home.getProperties();
			if (properties.isEmpty()) {
				rowSize += 1;
			} else {
				rowSize += properties.size();
			}
		}

		int numOfProperties = 0;

		String[][] rows = new String[rowSize][HOME_PROPERTY_TITLES.length];
		int rowIndex = 0;
		// String prevMachineId = null;
		for (IHome home : homes) {
			IMachine machine = home.getMachine();
			// String currMachineId = machine.getId();
			String currMachineName = machine.getName();
			// String machineText = machine.getName() + " (" + currMachineId + ")";

			// String currHomeId = home.getId();
			String currHomeName = home.getName();
			// String currHomeDesc = home.getDescription();
			String homeText = "[" + currMachineName + "]/" + currHomeName;
			// if (prevMachineId != null && prevMachineId.equals(currMachineId)) {
			// machineText = "";
			// }

			int propIndex = 0;
			Map<String, Object> properties = home.getProperties();
			numOfProperties += properties.size();

			if (properties.isEmpty()) {
				rows[rowIndex++] = new String[] { homeText, "(n/a)" };

			} else {
				for (Iterator<String> propNameItor = properties.keySet().iterator(); propNameItor.hasNext();) {
					String propName = propNameItor.next();
					Object propValue = properties.get(propName);
					String propText = propName + "=" + propValue;
					if (propValue != null) {
						propText += " (" + propValue.getClass().getName() + ")";
					}

					if (propIndex > 0) {
						// machineText = "";
						homeText = "";
						// currHomeId = "";
						// currHomeName = "";
						// currHomeDesc = "";
					}
					rows[rowIndex++] = new String[] { homeText, propText };
					propIndex++;
				}
			}
			// prevMachineId = currMachineId;
		}

		PrettyPrinter.prettyPrint(HOME_PROPERTY_TITLES, rows, numOfProperties);
	}

	/**
	 * Set Home property
	 * 
	 * Command: sethomeproperty [-machineid <machineId>] -homeid <homeId> -pname <propertyName> -pvalue <propertyValue> -ptype <propertyType>
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertyName
	 * @param propertyValue
	 * @param propertyType
	 *            supported type names are: string, boolean, int, long, float, double, date.
	 * @throws ClientException
	 */
	@Descriptor("Set Home Property")
	public void sethomeproperty( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId, // required
			@Descriptor("Property Name") @Parameter(names = { "-pname", "--propertyName" }, absentValue = "") String propertyName, // required
			@Descriptor("Property Value") @Parameter(names = { "-pvalue", "--propertyValue" }, absentValue = "") String propertyValue, // optional
			@Descriptor("Property Type") @Parameter(names = { "-ptype", "--propertyType" }, absentValue = "string") String propertyType // optional
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		// homeId is required for creating new Home
		if ("".equals(homeId)) {
			System.out.println("Please specify -homeid parameter.");
			return;
		}

		// propertyName is required=
		if ("".equals(propertyName)) {
			System.out.println("Please specify -pname parameter.");
			return;
		}

		IHome home = null;
		if ("".equals(machineId)) {
			home = this.mgmClient.getHome(homeId);
		} else {
			home = this.mgmClient.getHome(machineId, homeId);
		}
		if (home == null) {
			System.out.println("Home is not found.");
			return;
		}

		Object value = PropertyUtil.getPropertyValue(propertyType, propertyValue);
		boolean succeed = home.setProperty(propertyName, value);
		if (succeed) {
			System.out.println("Home property is set.");
		} else {
			System.out.println("Failed to set Home property.");
		}
	}

	/**
	 * Remove Home property
	 * 
	 * Command: removehomeproperty [-machineid <machineId>] -homeid <homeId> -pname <propertyName>
	 * 
	 * @param machineId
	 * @param homeId
	 * @param propertyName
	 * @throws ClientException
	 */
	@Descriptor("Remove Home Property")
	public void removehomeproperty( //
			// Parameters
			@Descriptor("Machine ID") @Parameter(names = { "-machineid", "--machineId" }, absentValue = "") String machineId, // optional
			@Descriptor("Home ID") @Parameter(names = { "-homeid", "--homeId" }, absentValue = "") String homeId, // required
			@Descriptor("Property Name") @Parameter(names = { "-pname", "--propertyName" }, absentValue = "") String propertyName // required
	) throws ClientException {
		if (this.mgmClient == null) {
			System.out.println("Please login first.");
			return;
		}

		// homeId is required for creating new Home
		if ("".equals(homeId)) {
			System.out.println("Please specify -homeid parameter.");
			return;
		}

		// propertyName is required=
		if ("".equals(propertyName)) {
			System.out.println("Please specify -pname parameter.");
			return;
		}

		IHome home = null;
		if ("".equals(machineId)) {
			home = this.mgmClient.getHome(homeId);
		} else {
			home = this.mgmClient.getHome(machineId, homeId);
		}
		if (home == null) {
			System.out.println("Home is not found.");
			return;
		}

		if (!home.getProperties().containsKey(propertyName)) {
			System.out.println("Home property does not exist.");
			return;
		}

		boolean succeed = home.removeProperty(propertyName);
		if (succeed) {
			System.out.println("Home property is removed.");
		} else {
			System.out.println("Failed to remove Home property.");
		}
	}

}
