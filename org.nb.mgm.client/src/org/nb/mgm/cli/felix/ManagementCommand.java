package org.nb.mgm.cli.felix;

import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.nb.mgm.cli.util.PrintUtil;
import org.nb.mgm.client.Machine;
// import org.apache.felix.gogo.commands.Argument;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MetaSector;
import org.nb.mgm.client.MgmFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import osgi.mgm.common.util.ClientException;

public class ManagementCommand {

	protected static String[] MACHINE_TITLES = new String[] { "ID", "Name", "IP", "Description" };

	protected static String[] META_SECTOR_TITLES = new String[] { "ID", "Name", "Description" };

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
		props.put("osgi.command.function", new String[] { "login", "list" });
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

	/**
	 * mgm:list -machine
	 * 
	 * mgm:list -metasector
	 * 
	 * @param showMachines
	 * @param showHomes
	 * @param machineName
	 * @param showSectors
	 * @param showSpaces
	 * @param sectorName
	 */
	@Descriptor("List entities of the cloud")
	public void list(
			// parameters
			@Descriptor("Show machines in the cloud") @Parameter(names = { "-machine", "--machine" }, absentValue = "false", presentValue = "true") boolean showMachines, //
			@Descriptor("Show homes in a machine") @Parameter(names = { "-home", "--home" }, absentValue = "false", presentValue = "true") boolean showHomes, //
			@Descriptor("Show sectors in the cloud") @Parameter(names = { "-metasector", "--metasector" }, absentValue = "false", presentValue = "true") boolean showSectors, //
			@Descriptor("Show spaces in a sector") @Parameter(names = { "-metaspace", "--metaspace" }, absentValue = "false", presentValue = "true") boolean showSpaces, //
			// options
			@Descriptor("machine name. required when using --home") @Parameter(names = { "-machinename", "--machinename" }, absentValue = "") String machineName, //
			@Descriptor("sector name. required when using --space") @Parameter(names = { "-metasectorname", "--metasectorname" }, absentValue = "") String sectorName //
	) {
		if (this.mgm == null) {
			System.out.println("Please login first.");
			return;
		}
		try {
			if (showMachines) {
				List<Machine> machines = this.mgm.getMachines();
				String[][] rows = new String[machines.size()][4];
				int rowIndex = 0;
				for (Machine machine : machines) {
					rows[rowIndex++] = new String[] { machine.getId(), machine.getName(), machine.getIpAddress(), machine.getDescription() };
				}
				PrintUtil.prettyPrint(MACHINE_TITLES, rows);

			} else if (showHomes) {

			} else if (showSectors) {
				List<MetaSector> metaSectors = this.mgm.getMetaSectors();
				String[][] rows = new String[metaSectors.size()][4];
				int rowIndex = 0;
				for (MetaSector metaSector : metaSectors) {
					rows[rowIndex++] = new String[] { metaSector.getId(), metaSector.getName(), metaSector.getDescription() };
				}
				PrintUtil.prettyPrint(META_SECTOR_TITLES, rows);

			} else if (showSpaces) {

			}

		} catch (ClientException e) {
			e.printStackTrace();
		}
	}

}
