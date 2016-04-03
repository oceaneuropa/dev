package org.nb.mgm.client.cli.felix;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MetaSector;
import org.nb.mgm.client.api.MetaSpace;
import org.nb.mgm.client.api.MgmFactory;
import org.nb.mgm.client.util.ClientException;
import org.nb.mgm.client.util.PrintUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class ManagementCommand {

	protected static String[] MACHINE_TITLES = new String[] { "ID", "Name", "IP", "Description" };
	protected static String[] HOME_TITLES = new String[] { "ID", "Name", "URL", "Description" };
	protected static String[] META_SECTOR_TITLES = new String[] { "ID", "Name", "Description" };
	protected static String[] META_SPACE_TITLES = new String[] { "ID", "Name", "Description" };

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;
	protected Management mgm;

	/**
	 * 
	 * @param bundleContext
	 */
	public ManagementCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("ManagementCommand.start()");
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "mgm");
		props.put("osgi.command.function", new String[] { "login", "list", "create", "delete" });
		this.registration = bundleContext.registerService(ManagementCommand.class.getName(), this, props);
	}

	public void stop() {
		System.out.println("ManagementCommand.stop()");
		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}
	}

	/**
	 * mgm:login -url http://127.0.0.1:9090 -u admin -p 123
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	@Descriptor("Login to the cloud")
	public void login(@Descriptor("url") @Parameter(names = { "-url", "--url" }, absentValue = "") String url, //
			@Descriptor("username") @Parameter(names = { "-u", "--username" }, absentValue = "") String username, //
			@Descriptor("password") @Parameter(names = { "-p", "--password" }, absentValue = "") String password //
	) {
		if (url.isEmpty()) {
			System.out.println("--url parameter is required.");
			return;
		}
		if (username.isEmpty()) {
			System.out.println("--username parameter is required.");
			return;
		}
		if (password.isEmpty()) {
			System.out.println("-password parameter is required.");
			return;
		}
		this.mgm = MgmFactory.createManagement(url, username, password);
	}

	// ------------------------------------------------------------------------------------------
	// list
	// ------------------------------------------------------------------------------------------
	/**
	 * List entities in the cluster.
	 * 
	 * @param showMachines
	 * @param showHomes
	 * @param showMetaSectors
	 * @param showMetaSpaces
	 * @param machineId
	 * @param machineName
	 * @param metaSectorId
	 * @param metaSectorName
	 */
	@Descriptor("List entities in the cluster.")
	public void list(
			// 1. Options
			@Descriptor("Show Machines in the cluster.") @Parameter(names = { "-machine", "--machine" }, absentValue = "false", presentValue = "true") boolean showMachines, //
			@Descriptor("Show Homes in a Machine. -machineid or -machinename is required.") @Parameter(names = { "-home", "--home" }, absentValue = "false", presentValue = "true") boolean showHomes, //
			@Descriptor("Show MetaSectors in the cluster.") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "false", presentValue = "true") boolean showMetaSectors, //
			@Descriptor("Show MetaSpaces in a MetaSector. -metasectorid or -metasectorname is required.") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "false", presentValue = "true") boolean showMetaSpaces, //

			// 2. Parameters
			// Required when using -home
			@Descriptor("Machine id.") @Parameter(names = { "-machineid", "--machineid" }, absentValue = "") String machineId, //
			@Descriptor("Machine name. ") @Parameter(names = { "-machinename", "--machinename" }, absentValue = "") String machineName, //

			// Required when using -metaspace
			@Descriptor("MetaSector id.") @Parameter(names = { "-metasectorid", "--metasectorid" }, absentValue = "") String metaSectorId, //
			@Descriptor("MetaSector name. ") @Parameter(names = { "-metasectorname", "--metasectorname" }, absentValue = "") String metaSectorName //
	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			if (!showMachines && !showHomes && !showMetaSectors && !showMetaSpaces) {
				System.out.println("Please specify (-machine | -home | -metasector | -metaspace) option.");
				return;
			}

			if (showMachines) {
				listMachines(this.mgm);

			} else if (showHomes) {
				if ("".equals(machineId) && "".equals(machineName)) {
					System.out.println("Please specify -machineid or -machinename parameter.");
					return;
				}

				if (!"".equals(machineId)) {
					listHomes(this.mgm, machineId);

				} else if (!"".equals(machineName)) {
					Properties properties = new Properties();
					properties.setProperty("name", machineName);
					List<Machine> machines = this.mgm.getMachines(properties);

					if (machines.isEmpty()) {
						System.out.println(MessageFormat.format("Machine with name ''{0}'' is not found.", new Object[] { machineName }));
						return;

					} else if (machines.size() > 1) {
						String[] machineIds = new String[machines.size()];
						for (int i = 0; i < machines.size(); i++) {
							machineIds[i] = machines.get(i).getId();
						}
						System.out.println(MessageFormat.format("{0} Machines ({1}) are found with name ''{2}''. Please specifiy -machineid parameter.", new Object[] { machines.size(), Arrays.toString(machineIds), machineName }));
						return;
					}

					String _machineId = machines.get(0).getId();
					listHomes(this.mgm, _machineId);
				}

			} else if (showMetaSectors) {
				listMetaSectors(this.mgm);

			} else if (showMetaSpaces) {
				if ("".equals(metaSectorId) && "".equals(metaSectorName)) {
					System.out.println("Please specify -metasectorid or -metasectorname parameter.");
					return;
				}

				if (!"".equals(metaSectorId)) {
					listMetaSpaces(this.mgm, metaSectorId);

				} else if (!"".equals(metaSectorName)) {
					Properties properties = new Properties();
					properties.setProperty("name", metaSectorName);
					List<MetaSector> metaSectors = this.mgm.getMetaSectors(properties);

					if (metaSectors.isEmpty()) {
						System.out.println(MessageFormat.format("MetaSector with name ''{0}'' is not found.", new Object[] { metaSectorName }));
						return;

					} else if (metaSectors.size() > 1) {
						String[] metaSectorIds = new String[metaSectors.size()];
						for (int i = 0; i < metaSectors.size(); i++) {
							metaSectorIds[i] = metaSectors.get(i).getId();
						}
						System.out.println(MessageFormat.format("{0} MetaSectors ({1}) are found with name ''{2}''. Please specifiy -metasectorid parameter.", new Object[] { metaSectors.size(), Arrays.toString(metaSectorIds), metaSectorName }));
						return;
					}

					String _metaSectorId = metaSectors.get(0).getId();
					listMetaSpaces(this.mgm, _metaSectorId);
				}
			}

		} catch (ClientException e) {
			System.out.println(e.getMessage() + "(" + e.getCode() + ")");
		}
	}

	/**
	 * Print out all Machines.
	 * 
	 * @param mgm
	 * @throws ClientException
	 */
	protected void listMachines(Management mgm) throws ClientException {
		List<Machine> machines = mgm.getMachines();
		String[][] rows = new String[machines.size()][MACHINE_TITLES.length];
		int rowIndex = 0;
		for (Machine machine : machines) {
			rows[rowIndex++] = new String[] { machine.getId(), machine.getName(), machine.getIpAddress(), machine.getDescription() };
		}
		PrintUtil.prettyPrint(MACHINE_TITLES, rows);
	}

	/**
	 * Print out Homes in a Machine.
	 * 
	 * @param mgm
	 * @param machineId
	 * @throws ClientException
	 */
	protected void listHomes(Management mgm, String machineId) throws ClientException {
		Machine machine = mgm.getMachine(machineId);
		if (machine == null) {
			System.out.println(MessageFormat.format("Machine with id=''{0}'' does not exist.", new Object[] { machineId }));
			return;
		}

		List<Home> homes = mgm.getHomes(machineId);
		String[][] rows = new String[homes.size()][HOME_TITLES.length];
		int rowIndex = 0;
		for (Home home : homes) {
			rows[rowIndex++] = new String[] { home.getId(), home.getName(), home.getUrl(), home.getDescription() };
		}
		PrintUtil.prettyPrint(HOME_TITLES, rows);
	}

	/**
	 * Print out all MetaSectors.
	 * 
	 * @param mgm
	 * @throws ClientException
	 */
	protected void listMetaSectors(Management mgm) throws ClientException {
		List<MetaSector> metaSectors = this.mgm.getMetaSectors();
		String[][] rows = new String[metaSectors.size()][META_SECTOR_TITLES.length];
		int rowIndex = 0;
		for (MetaSector metaSector : metaSectors) {
			rows[rowIndex++] = new String[] { metaSector.getId(), metaSector.getName(), metaSector.getDescription() };
		}
		PrintUtil.prettyPrint(META_SECTOR_TITLES, rows);
	}

	/**
	 * Print out MetaSpaces in a MetaSector.
	 * 
	 * @param mgm
	 * @param metaSectorId
	 * @throws ClientException
	 */
	protected void listMetaSpaces(Management mgm, String metaSectorId) throws ClientException {
		MetaSector metaSector = mgm.getMetaSector(metaSectorId);
		if (metaSector == null) {
			System.out.println(MessageFormat.format("MetaSector with id=''{0}'' does not exist.", new Object[] { metaSectorId }));
			return;
		}

		List<MetaSpace> metaSpaces = mgm.getMetaSpaces(metaSectorId);
		String[][] rows = new String[metaSpaces.size()][META_SPACE_TITLES.length];
		int rowIndex = 0;
		for (MetaSpace metaSpace : metaSpaces) {
			rows[rowIndex++] = new String[] { metaSpace.getId(), metaSpace.getName(), metaSpace.getDescription() };
		}
		PrintUtil.prettyPrint(META_SPACE_TITLES, rows);
	}

	// ------------------------------------------------------------------------------------------
	// create
	// ------------------------------------------------------------------------------------------
	/**
	 * mgm:create -machine
	 * 
	 * @param createMachine
	 * @param createHome
	 * @param createMetaSector
	 * @param createMetaSpace
	 * @param name
	 * @param description
	 * @param ipaddress
	 * @param machineid
	 * @param url
	 * @param metasectorid
	 */
	@Descriptor("Create entities in the cluster")
	public void create( //
			// 1. Options
			@Descriptor("Create a Machine in the cluster.") @Parameter(names = { "-machine", "--machine" }, absentValue = "false", presentValue = "true") boolean createMachine, //
			@Descriptor("Create a Home in a Machine.") @Parameter(names = { "-home", "--home" }, absentValue = "false", presentValue = "true") boolean createHome, //
			@Descriptor("Create a MetaSector in the cluster.") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "false", presentValue = "true") boolean createMetaSector, //
			@Descriptor("Create a MetaSpace in a MetaSector.") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "false", presentValue = "true") boolean createMetaSpace, //

			// 2. Parameters
			// required for Machine, Home, MetaSector, MetaSpace.
			@Descriptor("Name of (Machine | Home | MetaSector | MetaSpace).") @Parameter(names = { "-name", "--name" }, absentValue = "") String name, //
			// optional for Machine, Home, MetaSector, MetaSpace.
			@Descriptor("Description of (Machine | Home | MetaSector | MetaSpace).") @Parameter(names = { "-description", "--description" }, absentValue = "") String description, //

			// required for Machine
			@Descriptor("IP address of Machine. Required when using -machine.") @Parameter(names = { "-ipaddress", "--ipaddress" }, absentValue = "") String ipaddress, //

			// required for Home
			@Descriptor("Machine Id for creating a new Home.") @Parameter(names = { "-machineid", "--machineid" }, absentValue = "") String machineid, //
			@Descriptor("URL of a Home.") @Parameter(names = { "-url", "--url" }, absentValue = "") String url, //

			// required for MetaSector

			// required for MetaSpace
			@Descriptor("MetaSector Id for creating a new MetaSpace.") @Parameter(names = { "-metasectorid", "--metasectorid" }, absentValue = "") String metasectorid //

	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			if (!createMachine && !createHome && !createMetaSector && !createMetaSpace) {
				System.out.println("Please specify (-machine | -home | -metasector | -metaspace) option.");
				return;
			}

			// name is required for creating all entities
			if ("".equals(name)) {
				System.out.println("Please specify -name parameter.");
				return;
			}

			if (createMachine) {
				// name (required)
				// ipaddress (required)
				// description (optional)
				if ("".equals(ipaddress)) {
					System.out.println("Please specify -ipaddress parameter.");
					return;
				}
				createMachine(this.mgm, name, ipaddress, description);

			} else if (createHome) {
				// machineid (required)
				// name (required)
				// url (required)
				// description (optional)
				if ("".equals(machineid)) {
					System.out.println("Please specify -machineid parameter.");
					return;
				}
				if ("".equals(url)) {
					System.out.println("Please specify -url parameter.");
					return;
				}
				createHome(this.mgm, machineid, name, url, description);

			} else if (createMetaSector) {
				// name (required)
				// description (optional)
				createMetaSector(this.mgm, name, description);

			} else if (createMetaSpace) {
				// metasectorid (required)
				// name (required)
				// description (optional)
				if ("".equals(metasectorid)) {
					System.out.println("Please specify -metasectorid parameter.");
					return;
				}
				createMetaSpace(this.mgm, metasectorid, name, description);
			}
		} catch (ClientException e) {
			System.out.println("Cannot create new entity. " + e.getMessage());
		}
	}

	/**
	 * Create a new Machine in the cluster.
	 * 
	 * @param mgm
	 * @param name
	 * @param ipaddress
	 * @param description
	 * @throws ClientException
	 */
	protected void createMachine(Management mgm, String name, String ipaddress, String description) throws ClientException {
		Machine newMachine = mgm.addMachine(name, ipaddress, description);
		if (newMachine != null) {
			System.out.println("New Machine is created. ");
		} else {
			System.out.println("New Machine is not created.");
		}
	}

	/**
	 * Create a new Home in a Machine.
	 * 
	 * @param mgm
	 * @param machineid
	 * @param name
	 * @param url
	 * @param description
	 * @throws ClientException
	 */
	protected void createHome(Management mgm, String machineid, String name, String url, String description) throws ClientException {
		Home newHome = mgm.addHome(machineid, name, url, description);
		if (newHome != null) {
			System.out.println("New Home is created. ");
		} else {
			System.out.println("New Home is not created.");
		}
	}

	/**
	 * Create a new MetaSector in the cluster.
	 * 
	 * @param mgm
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	protected void createMetaSector(Management mgm, String name, String description) throws ClientException {
		MetaSector newMetaSector = mgm.addMetaSector(name, description);
		if (newMetaSector != null) {
			System.out.println("New MetaSector is created. ");
		} else {
			System.out.println("New MetaSector is not created.");
		}
	}

	/**
	 * Create a new MetaSpace in a MetaSector.
	 * 
	 * @param mgm
	 * @param metasectorid
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	protected void createMetaSpace(Management mgm, String metasectorid, String name, String description) throws ClientException {
		MetaSpace newMetaSpace = mgm.addMetaSpace(metasectorid, name, description);
		if (newMetaSpace != null) {
			System.out.println("New MetaSpace is created. ");
		} else {
			System.out.println("New MetaSpace is not created.");
		}
	}

	// ------------------------------------------------------------------------------------------
	// delete
	// ------------------------------------------------------------------------------------------
	/**
	 * Delete entities from the cluster.
	 * 
	 * @param machineIdsToDelete
	 * @param homeIdsToDelete
	 * @param detaSectorIdsToDelete
	 * @param metaSpaceIdsToDelete
	 */
	@Descriptor("Delete entities from the cluster.")
	public void delete(
			// Parameter
			@Descriptor("Delete Machine(s) with Machine id or ids separated with ','.") @Parameter(names = { "-machine", "--machine" }, absentValue = "") String machineIdsToDelete, //
			@Descriptor("Delete Home(s) with Home id or ids separated with ','.") @Parameter(names = { "-home", "--home" }, absentValue = "") String homeIdsToDelete, //
			@Descriptor("Delete MetaSector(s) with MetaSector id or ids separated with ','.") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "") String detaSectorIdsToDelete, //
			@Descriptor("Delete MetaSpace(s) with MetaSpace id or ids separated with ','.") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "") String metaSpaceIdsToDelete //
	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}
		try {
			if ("".equals(machineIdsToDelete) && "".equals(homeIdsToDelete) && "".equals(detaSectorIdsToDelete) && "".equals(metaSpaceIdsToDelete)) {
				System.out.println("Please specify (-machine | -home | -metasector | -metaspace) parameter.");
				return;
			}

			if (!"".equals(machineIdsToDelete)) {
				// delete Machines
				String[] machineIds = null;
				if (machineIdsToDelete.contains(",")) {
					machineIds = machineIdsToDelete.split(",");
				} else {
					machineIds = new String[] { machineIdsToDelete };
				}
				deleteMachines(this.mgm, machineIds);

			} else if (!"".equals(homeIdsToDelete)) {
				// delete Homes from Machines
				String[] homeIds = null;
				if (homeIdsToDelete.contains(",")) {
					homeIds = homeIdsToDelete.split(",");
				} else {
					homeIds = new String[] { homeIdsToDelete };
				}
				deleteHomes(this.mgm, homeIds);

			} else if (!"".equals(detaSectorIdsToDelete)) {
				// delete MetaSectors
				String[] metaSectorIds = null;
				if (detaSectorIdsToDelete.contains(",")) {
					metaSectorIds = detaSectorIdsToDelete.split(",");
				} else {
					metaSectorIds = new String[] { detaSectorIdsToDelete };
				}
				deleteMetaSectors(this.mgm, metaSectorIds);

			} else if (!"".equals(metaSpaceIdsToDelete)) {
				// delete MetaSpaces
				String[] metaSpaceIds = null;
				if (metaSpaceIdsToDelete.contains(",")) {
					metaSpaceIds = metaSpaceIdsToDelete.split(",");
				} else {
					metaSpaceIds = new String[] { metaSpaceIdsToDelete };
				}
				deleteMetaSpaces(this.mgm, metaSpaceIds);
			}

		} catch (ClientException e) {
			System.out.println(e.getMessage() + "(" + e.getCode() + ")");
		}
	}

	/**
	 * Delete Machines from the cluster.
	 * 
	 * @param mgm
	 * @param machineIds
	 * @throws ClientException
	 */
	protected void deleteMachines(Management mgm, String[] machineIds) throws ClientException {
		for (String machineId : machineIds) {
			boolean succeed = mgm.deleteMachine(machineId);
			if (succeed) {
				System.out.println(MessageFormat.format("Machine with id=''{0}'' is deleted.", new Object[] { machineId }));
			} else {
				System.out.println(MessageFormat.format("Failed to delete Machine with id=''{0}''.", new Object[] { machineId }));
			}
		}
	}

	/**
	 * Delete Homes from Machines.
	 * 
	 * @param mgm
	 * @param homeIds
	 * @throws ClientException
	 */
	protected void deleteHomes(Management mgm, String[] homeIds) throws ClientException {
		String[] machineIds = new String[homeIds.length];

		List<Machine> machines = mgm.getMachines();
		for (Machine machine : machines) {
			String machineId = machine.getId();
			List<Home> homes = machine.getHomes();

			for (int i = 0; i < homeIds.length; i++) {
				String homeId = homeIds[i];
				if (machineIds[i] != null) {
					continue;
				}
				for (Home home : homes) {
					if (homeId.equals(home.getId())) {
						machineIds[i] = machineId;
						break;
					}
				}
			}
		}

		for (int i = 0; i < homeIds.length; i++) {
			String homeId = homeIds[i];

			String machineId = machineIds[i];
			if (machineId == null) {
				System.out.println(MessageFormat.format("Cannot find the Machine that the Home with id=''{0}'' belongs to.", new Object[] { homeId }));
				continue;
			}

			boolean succeed = mgm.deleteHome(machineId, homeId);
			if (succeed) {
				System.out.println(MessageFormat.format("Home with id=''{0}'' is deleted.", new Object[] { homeId }));
			} else {
				System.out.println(MessageFormat.format("Failed to delete Home with id=''{0}''.", new Object[] { homeId }));
			}
		}
	}

	/**
	 * Delete MetaSectors from the cluster.
	 * 
	 * @param mgm
	 * @param metaSectorIds
	 * @throws ClientException
	 */
	protected void deleteMetaSectors(Management mgm, String[] metaSectorIds) throws ClientException {
		for (String metaSectorId : metaSectorIds) {
			boolean succeed = mgm.deleteMetaSector(metaSectorId);
			if (succeed) {
				System.out.println(MessageFormat.format("MetaSector with id=''{0}'' is deleted.", new Object[] { metaSectorId }));
			} else {
				System.out.println(MessageFormat.format("Failed to delete MetaSector with id=''{0}''.", new Object[] { metaSectorId }));
			}
		}
	}

	/**
	 * Delete MetaSpaces from MetaSectors.
	 * 
	 * @param mgm
	 * @param metaSpaceIds
	 * @throws ClientException
	 */
	protected void deleteMetaSpaces(Management mgm, String[] metaSpaceIds) throws ClientException {
		String[] metaSectorIds = new String[metaSpaceIds.length];

		List<MetaSector> metaSectors = mgm.getMetaSectors();
		for (MetaSector metaSector : metaSectors) {
			String metaSectorId = metaSector.getId();
			List<MetaSpace> metaSpaces = metaSector.getMetaSpaces();

			for (int i = 0; i < metaSpaceIds.length; i++) {
				String metaSpaceId = metaSpaceIds[i];
				if (metaSectorIds[i] != null) {
					continue;
				}
				for (MetaSpace metaSpace : metaSpaces) {
					if (metaSpaceId.equals(metaSpace.getId())) {
						metaSectorIds[i] = metaSectorId;
						break;
					}
				}
			}
		}

		for (int i = 0; i < metaSpaceIds.length; i++) {
			String metaSpaceId = metaSpaceIds[i];

			String metaSectorId = metaSectorIds[i];
			if (metaSectorId == null) {
				System.out.println(MessageFormat.format("Cannot find the MetaSector that the MetaSpace with id=''{0}'' belongs to.", new Object[] { metaSpaceId }));
				continue;
			}

			boolean succeed = mgm.deleteMetaSpace(metaSectorId, metaSpaceId);
			if (succeed) {
				System.out.println(MessageFormat.format("MetaSpace with id=''{0}'' is deleted.", new Object[] { metaSpaceId }));
			} else {
				System.out.println(MessageFormat.format("Failed to delete MetaSpace with id=''{0}''.", new Object[] { metaSpaceId }));
			}
		}
	}

}
