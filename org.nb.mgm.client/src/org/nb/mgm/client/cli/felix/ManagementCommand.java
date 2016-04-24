package org.nb.mgm.client.cli.felix;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.common.util.PrintUtil;
import org.nb.common.util.StringUtil;
import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MetaSector;
import org.nb.mgm.client.api.MetaSpace;
import org.nb.mgm.client.api.MgmFactory;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManagementCommand {

	protected static String[] MACHINE_TITLES = new String[] { "ID", "Name", "IP", "Description" };
	protected static String[] HOME_TITLES = new String[] { "ID", "Name", "URL", "Description" };
	protected static String[] META_SECTOR_TITLES = new String[] { "ID", "Name", "Description" };
	protected static String[] META_SPACE_TITLES = new String[] { "ID", "Name", "Description" };

	protected static String NULL = "null";

	protected static Logger logger = LoggerFactory.getLogger(ManagementCommand.class);
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
		logger.debug("ManagementCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "mgm");
		props.put("osgi.command.function", new String[] { "login", "list", "create", "update", "delete" });
		this.registration = bundleContext.registerService(ManagementCommand.class.getName(), this, props);
	}

	public void stop() {
		logger.debug("ManagementCommand.stop()");

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
	// List
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
	// Create
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

			// required for Machine (name, ip, description)
			@Descriptor("IP address of Machine. Required when using -machine.") @Parameter(names = { "-ip", "--ipaddress" }, absentValue = "") String ipaddress, //

			// required for Home (machineid, name, url, description)
			@Descriptor("Machine Id for creating a new Home.") @Parameter(names = { "-machineid", "--machineid" }, absentValue = "") String machineid, //
			@Descriptor("URL of a Home.") @Parameter(names = { "-url", "--url" }, absentValue = "") String url, //

			// required for MetaSector (name, description)

			// required for MetaSpace (metasectorid, name, description)
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
				System.out.println("Please specify -name parameter for creating new (Machine | Home | MetaSector | MetaSpace).");
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
	// Update
	// ------------------------------------------------------------------------------------------
	/**
	 * mgm:update -machine {machineId} -name {name} -ip {ipaddress} -description {description}
	 * 
	 * mgm:update -home {homeId} -name {name} -url {url} -description {description}
	 * 
	 * mgm:update -metasector {metasectorId} -name {name} -description {description}
	 * 
	 * mgm:update -metaspace {metaspaceId} -name {name} -description {description}
	 * 
	 * @param machineIdToUpdate
	 * @param homeIdToUpdate
	 * @param metaSectorIdToUpdate
	 * @param metaSpaceIdToUpdate
	 * @param name
	 * @param description
	 * @param ipaddress
	 * @param url
	 */
	@Descriptor("Delete entity in the cluster.")
	public void update( //
			// Parameter
			@Descriptor("Update Machine.") @Parameter(names = { "-machine", "--machine" }, absentValue = "") String machineIdToUpdate, //
			@Descriptor("Update Home.") @Parameter(names = { "-home", "--home" }, absentValue = "") String homeIdToUpdate, //
			@Descriptor("Update MetaSector.") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "") String metaSectorIdToUpdate, //
			@Descriptor("Delete MetaSpace.") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "") String metaSpaceIdToUpdate, //

			// name of Machine, Home, MetaSector, MetaSpace.
			@Descriptor("Name of (Machine | Home | MetaSector | MetaSpace).") @Parameter(names = { "-name", "--name" }, absentValue = "null") String name, //
			// description for Machine, Home, MetaSector, MetaSpace.
			@Descriptor("Description of (Machine | Home | MetaSector | MetaSpace).") @Parameter(names = { "-description", "--description" }, absentValue = "null") String description, //

			// Machine ip address
			@Descriptor("IP address of Machine. Required when using -machine.") @Parameter(names = { "-ip", "--ipaddress" }, absentValue = "null") String ipaddress, //

			// Home url
			@Descriptor("URL of a Home.") @Parameter(names = { "-url", "--url" }, absentValue = "null") String url //
	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}

		try {
			if ("".equals(machineIdToUpdate) && "".equals(homeIdToUpdate) && "".equals(metaSectorIdToUpdate) && "".equals(metaSpaceIdToUpdate)) {
				System.out.println("Please specify (-machine | -home | -metasector | -metaspace) parameter.");
				return;
			}

			if (!"".equals(machineIdToUpdate)) {
				if (NULL.equals(name) && NULL.equals(ipaddress) && NULL.equals(description)) {
					System.out.println("Please specify -name or -ipaddress or -description parameter.");
					return;
				}
				updateMachine(mgm, machineIdToUpdate, name, ipaddress, description);

				listMachines(mgm);

			} else if (!"".equals(homeIdToUpdate)) {
				if (NULL.equals(name) && NULL.equals(url) && NULL.equals(description)) {
					System.out.println("Please specify -name or -url or -description parameter.");
					return;
				}
				updateHome(mgm, homeIdToUpdate, name, url, description);

				Home home = mgm.getHome(homeIdToUpdate);
				if (home != null && home.getMachine() != null) {
					listHomes(mgm, home.getMachine().getId());
				}

			} else if (!"".equals(metaSectorIdToUpdate)) {
				if (NULL.equals(name) && NULL.equals(description)) {
					System.out.println("Please specify -name or -description parameter.");
					return;
				}
				updateMetaSector(mgm, metaSectorIdToUpdate, name, description);

				listMetaSectors(mgm);

			} else if (!"".equals(metaSpaceIdToUpdate)) {
				if (NULL.equals(name) && NULL.equals(description)) {
					System.out.println("Please specify -name or -description parameter.");
					return;
				}
				updateMetaSpace(mgm, metaSpaceIdToUpdate, name, description);

				MetaSpace metaSpace = mgm.getMetaSpace(metaSpaceIdToUpdate);
				if (metaSpace != null && metaSpace.getMetaSector() != null) {
					listMetaSpaces(mgm, metaSpace.getMetaSector().getId());
				}
			}

		} catch (ClientException e) {
			System.out.println(e.getMessage() + "(" + e.getCode() + ")");
		}
	}

	/**
	 * Update Machine.
	 * 
	 * @param mgm
	 * @param machineId
	 * @param name
	 * @param ipaddress
	 * @param description
	 * @throws ClientException
	 */
	protected void updateMachine(Management mgm, String machineId, String name, String ipaddress, String description) throws ClientException {
		Machine machine = mgm.getMachine(machineId);
		if (machine == null) {
			System.out.println(MessageFormat.format("Machine with id=''{0}'' is not found.", new Object[] { machineId }));
			return;
		}

		String oldName = machine.getName();
		String oldIpAddress = machine.getIpAddress();
		String oldDescription = machine.getDescription();

		boolean isChanged = false;
		if (!NULL.equals(name) && !StringUtil.equals(oldName, name)) {
			machine.setName(name);
			isChanged = true;
		}
		if (!NULL.equals(ipaddress) && !StringUtil.equals(oldIpAddress, ipaddress)) {
			machine.setIpAddress(ipaddress);
			isChanged = true;
		}
		if (!NULL.equals(description) && !StringUtil.equals(oldDescription, description)) {
			machine.setDescription(description);
			isChanged = true;
		}

		if (isChanged) {
			System.out.println(MessageFormat.format("Machine with id=''{0}'' is updated.", new Object[] { machineId }));
			machine.update();
		} else {
			System.out.println(MessageFormat.format("Machine with id=''{0}'' is not updated.", new Object[] { machineId }));
		}
	}

	/**
	 * Update Home.
	 * 
	 * @param mgm
	 * @param homeId
	 * @param name
	 * @param url
	 * @param description
	 * @throws ClientException
	 */
	protected void updateHome(Management mgm, String homeId, String name, String url, String description) throws ClientException {
		Home home = mgm.getHome(homeId);
		if (home == null) {
			System.out.println(MessageFormat.format("Home with id=''{0}'' is not found.", new Object[] { homeId }));
			return;
		}

		String oldName = home.getName();
		String oldUrl = home.getUrl();
		String oldDescription = home.getDescription();

		boolean isChanged = false;
		if (!NULL.equals(name) && !StringUtil.equals(oldName, name)) {
			home.setName(name);
			isChanged = true;
		}
		if (!NULL.equals(url) && !StringUtil.equals(oldUrl, url)) {
			home.setUrl(url);
			isChanged = true;
		}
		if (!NULL.equals(description) && !StringUtil.equals(oldDescription, description)) {
			home.setDescription(description);
			isChanged = true;
		}

		if (isChanged) {
			System.out.println(MessageFormat.format("Home with id=''{0}'' is updated.", new Object[] { homeId }));
			home.update();
		} else {
			System.out.println(MessageFormat.format("Home with id=''{0}'' is not updated.", new Object[] { homeId }));
		}
	}

	/**
	 * Update MetaSector.
	 * 
	 * @param mgm
	 * @param metaSectorId
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	protected void updateMetaSector(Management mgm, String metaSectorId, String name, String description) throws ClientException {
		MetaSector metaSector = mgm.getMetaSector(metaSectorId);
		if (metaSector == null) {
			System.out.println(MessageFormat.format("MetaSector with id=''{0}'' is not found.", new Object[] { metaSectorId }));
			return;
		}

		String oldName = metaSector.getName();
		String oldDescription = metaSector.getDescription();

		boolean isChanged = false;
		if (!NULL.equals(name) && !StringUtil.equals(oldName, name)) {
			metaSector.setName(name);
			isChanged = true;
		}
		if (!NULL.equals(description) && !StringUtil.equals(oldDescription, description)) {
			metaSector.setDescription(description);
			isChanged = true;
		}

		if (isChanged) {
			System.out.println(MessageFormat.format("MetaSector with id=''{0}'' is updated.", new Object[] { metaSectorId }));
			metaSector.update();
		} else {
			System.out.println(MessageFormat.format("MetaSector with id=''{0}'' is not updated.", new Object[] { metaSectorId }));
		}
	}

	/**
	 * Update MetaSpace.
	 * 
	 * @param mgm
	 * @param metaSpaceId
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	protected void updateMetaSpace(Management mgm, String metaSpaceId, String name, String description) throws ClientException {
		MetaSpace metaSpace = mgm.getMetaSpace(metaSpaceId);
		if (metaSpace == null) {
			System.out.println(MessageFormat.format("MetaSpace with id=''{0}'' is not found.", new Object[] { metaSpaceId }));
			return;
		}

		String oldName = metaSpace.getName();
		String oldDescription = metaSpace.getDescription();

		boolean isChanged = false;
		if (!NULL.equals(name) && !StringUtil.equals(oldName, name)) {
			metaSpace.setName(name);
			isChanged = true;
		}
		if (!NULL.equals(description) && !StringUtil.equals(oldDescription, description)) {
			metaSpace.setDescription(description);
			isChanged = true;
		}

		if (isChanged) {
			System.out.println(MessageFormat.format("MetaSpace with id=''{0}'' is updated.", new Object[] { metaSpaceId }));
			metaSpace.update();
		} else {
			System.out.println(MessageFormat.format("MetaSpace with id=''{0}'' is not updated.", new Object[] { metaSpaceId }));
		}
	}

	// ------------------------------------------------------------------------------------------
	// Delete
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
		for (int i = 0; i < homeIds.length; i++) {
			String homeId = homeIds[i];
			boolean succeed = mgm.deleteHome(homeId);
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
		for (int i = 0; i < metaSpaceIds.length; i++) {
			String metaSpaceId = metaSpaceIds[i];
			boolean succeed = mgm.deleteMetaSpace(metaSpaceId);
			if (succeed) {
				System.out.println(MessageFormat.format("MetaSpace with id=''{0}'' is deleted.", new Object[] { metaSpaceId }));
			} else {
				System.out.println(MessageFormat.format("Failed to delete MetaSpace with id=''{0}''.", new Object[] { metaSpaceId }));
			}
		}
	}

}
