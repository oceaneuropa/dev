package org.nb.mgm.cli.felix;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.cli.util.PrintUtil;
import org.nb.mgm.client.Home;
import org.nb.mgm.client.Machine;
// import org.apache.felix.gogo.commands.Argument;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MetaSector;
import org.nb.mgm.client.MetaSpace;
import org.nb.mgm.client.MgmFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import osgi.mgm.common.util.ClientException;

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
		props.put("osgi.command.function", new String[] { "login", "list", "create" });
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
	 * mgm:list -machine
	 * 
	 * mgm:list -metasector
	 * 
	 * @param showMachines
	 * @param showHomes
	 * @param machineName
	 * @param showMetaSectors
	 * @param showMetaSpaces
	 * @param sectorName
	 */
	@Descriptor("List entities in the cluster")
	public void list(
			// options
			@Descriptor("Show Machines in the cluster.") @Parameter(names = { "-machine", "--machine" }, absentValue = "false", presentValue = "true") boolean showMachines, //
			@Descriptor("Show Homes in a Machine.") @Parameter(names = { "-home", "--home" }, absentValue = "false", presentValue = "true") boolean showHomes, //
			@Descriptor("Show MetaSectors in the cluster.") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "false", presentValue = "true") boolean showMetaSectors, //
			@Descriptor("Show MetaSpaces in a MetaSector.") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "false", presentValue = "true") boolean showMetaSpaces, //

			// parameters

			// for -home option
			@Descriptor("Machine id. Must specify either -machineid or -machinename when using -home.") @Parameter(names = { "-machineid", "--machineid" }, absentValue = "") String machineId, //
			@Descriptor("Machine name. Must specify either -machineid or -machinename when using -home.") @Parameter(names = { "-machinename", "--machinename" }, absentValue = "") String machineName, //

			// for -metaspace option
			@Descriptor("MetaSector id. Must specify either -metasectorid or -metasectorname when using -space.") @Parameter(names = { "-metasectorid", "--metasectorid" }, absentValue = "") String metaSectorId, //
			@Descriptor("MetaSector name. Must specify either -metasectorid or -metasectorname when using -space.") @Parameter(names = { "-metasectorname", "--metasectorname" }, absentValue = "") String metaSectorName //

	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
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
			System.out.println("ClientException: " + e.getMessage());
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
				System.out.println("Please specify (-machine | -home | -metasector | metaspace) option.");
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

}
