package org.orbit.component.runtime.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.model.tier3.domain.MachineConfigRTO;
import org.orbit.component.model.tier3.domain.NodeConfigRTO;
import org.orbit.component.model.tier3.domain.PlatformConfigRTO;
import org.orbit.component.runtime.tier3.domainmanagement.service.DomainManagementService;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainManagementCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.component.runtime.cli.DomainManagementCommand";

	protected static Logger LOG = LoggerFactory.getLogger(DomainManagementCommand.class);

	// Column names constants
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.namespace", "domain_mgmt.name", "domain_mgmt.host.url", "domain_mgmt.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] AUTH_SERVICES_TITLES = new String[] { "index_item_id", "auth.namespace", "auth.name", "auth.host.url", "auth.context_root", "last_heartbeat_time", "heartbeat_expire_time" };
	protected static String[] MACHINE_CONFIG_TITLES = new String[] { "ID", "Name", "IP Address" };
	protected static String[] PLATFORM_CONFIG_TITLES = new String[] { "Machine ID", "ID", "Name", "hostURL", "contextRoot", "Home" };
	protected static String[] NODE_CONFIG_TITLES = new String[] { "Machine ID", "Platform ID", "ID", "Name", "hostURL", "contextRoot" };

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	protected String scheme = "orbit";

	@Dependency
	protected DomainManagementService domainService;

	protected String getScheme() {
		return this.scheme;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// machine configurations
						"lmachines", "lmachine", "add_machine", "update_machine", "remove_machine", //

						// platform configurations
						"lplatforms", "lplatform", "add_platform", "update_platform", "remove_platform", //

						// node configurations
						"lnodeconfigs", "lnodeconfig", "add_nodeconfig", "update_nodeconfig", "remove_nodeconfig", //
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(this.bundleContext, DomainManagementCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
		OSGiServiceUtil.unregister(DomainManagementCommand.class.getName(), this);

		this.bundleContext = null;
	}

	protected DomainManagementService getDomainManagementService() {
		if (this.domainService == null) {
			throw new IllegalStateException("DomainManagementService is null.");
		}
		return this.domainService;
	}

	// -----------------------------------------------------------------------------------------
	// Machine configs
	// lmachines
	// lmachine
	// add_machine
	// update_machine
	// remove_machine
	// -----------------------------------------------------------------------------------------
	@Descriptor("List machine configs")
	public void lmachines() {
		CLIHelper.getInstance().printCommand(getScheme(), "lmachines");

		try {
			DomainManagementService domainService = getDomainManagementService();

			List<MachineConfigRTO> machineConfigs = domainService.getMachineConfigs();
			String[][] rows = new String[machineConfigs.size()][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfigRTO currMachineConfig : machineConfigs) {
				String machineId = currMachineConfig.getId();
				String machineName = currMachineConfig.getName();
				String ipAddress = currMachineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { machineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List machine config")
	public void lmachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "lmachine", new String[] { "id", id });

		try {
			DomainManagementService domainService = getDomainManagementService();

			MachineConfigRTO machineConfig = domainService.getMachineConfig(id);

			List<MachineConfigRTO> machineConfigs = new ArrayList<MachineConfigRTO>();
			if (machineConfig != null) {
				machineConfigs.add(machineConfig);
			}
			String[][] rows = new String[machineConfigs.size()][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfigRTO currMachineConfig : machineConfigs) {
				String machineId = currMachineConfig.getId();
				String machineName = currMachineConfig.getName();
				String ipAddress = currMachineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { machineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Add machine config")
	public void add_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String name, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = "null") String ip //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "add_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });

		try {
			DomainManagementService domainService = getDomainManagementService();

			MachineConfigRTO addMachineRequest = new MachineConfigRTO();
			addMachineRequest.setId(id);
			addMachineRequest.setName(name);
			addMachineRequest.setIpAddress(ip);

			boolean succeed = domainService.addMachineConfig(addMachineRequest);
			System.out.println("Machine config added: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Update machine config")
	public void update_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ip //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "update_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });

		try {
			DomainManagementService domainService = getDomainManagementService();

			MachineConfigRTO updateMachineRequest = new MachineConfigRTO();
			updateMachineRequest.setId(id);
			List<String> fieldsToUpdate = new ArrayList<String>();
			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				updateMachineRequest.setName(name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(ip)) {
				if ("null".equals(ip)) {
					ip = null;
				}
				updateMachineRequest.setIpAddress(ip);
				fieldsToUpdate.add("ipAddress");
			}

			boolean succeed = domainService.updateMachineConfig(updateMachineRequest, fieldsToUpdate);
			System.out.println("Machine config updated: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Remove machine config")
	public void remove_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_machine", new String[] { "id", id });

		try {
			DomainManagementService domainService = getDomainManagementService();

			boolean succeed = domainService.deleteMachineConfig(id);
			System.out.println("Machine config removed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Platform configs
	// lplatforms
	// lplatform
	// add_platform
	// update_platform
	// remove_platform
	// ----------------------------------------------------------------
	@Descriptor("List Platform configs")
	public void lplatforms( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "lplatforms", new String[] { "machineId", machineId });

		try {
			DomainManagementService domainService = getDomainManagementService();

			List<PlatformConfigRTO> platformConfigs = domainService.getPlatformConfigs(machineId);

			String[][] rows = new String[platformConfigs.size()][PLATFORM_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (PlatformConfigRTO platformConfig : platformConfigs) {
				// String currMachineId = platformConfig.getMachineId();
				String id = platformConfig.getId();
				String name = platformConfig.getName();
				String hostURL = platformConfig.getHostURL();
				String contextRoot = platformConfig.getContextRoot();
				String home = platformConfig.getHome();

				rows[rowIndex++] = new String[] { machineId, id, name, hostURL, contextRoot, home };
			}
			PrettyPrinter.prettyPrint(PLATFORM_CONFIG_TITLES, rows, platformConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List Platform config")
	public void lplatform( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "lplatform", new String[] { "machineId", machineId }, new String[] { "id", id });

		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagementService domainService = getDomainManagementService();

			PlatformConfigRTO platformConfig = domainService.getPlatformConfig(machineId, id);

			List<PlatformConfigRTO> platformConfigs = new ArrayList<PlatformConfigRTO>();
			if (platformConfig != null) {
				platformConfigs.add(platformConfig);
			}

			String[][] rows = new String[platformConfigs.size()][PLATFORM_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (PlatformConfigRTO currPlatformConfig : platformConfigs) {
				String platformId = currPlatformConfig.getId();
				String name = currPlatformConfig.getName();
				String hostURL = currPlatformConfig.getHostURL();
				String contextRoot = currPlatformConfig.getContextRoot();
				String home = currPlatformConfig.getHome();

				rows[rowIndex++] = new String[] { machineId, platformId, name, hostURL, contextRoot, home };
			}
			PrettyPrinter.prettyPrint(PLATFORM_CONFIG_TITLES, rows, platformConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Add Platform configuration")
	public void add_platform( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Platform Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Platform host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Platform context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "add_platform", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		try {
			DomainManagementService domainService = getDomainManagementService();

			PlatformConfigRTO addPlatformRequest = new PlatformConfigRTO();
			addPlatformRequest.setId(id);
			addPlatformRequest.setName(name);
			addPlatformRequest.setHostURL(hostURL);
			addPlatformRequest.setContextRoot(contextRoot);

			boolean succeed = domainService.addPlatformConfig(machineId, addPlatformRequest);
			System.out.println("Platform config added: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Update Platform configuration")
	public void update_platform( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Platform Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Platform host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Platform contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "update_platform", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		try {
			DomainManagementService domainService = getDomainManagementService();

			PlatformConfigRTO updatePlatformRequest = new PlatformConfigRTO();

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				updatePlatformRequest.setName(name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(hostURL)) {
				if ("null".equals(hostURL)) {
					hostURL = null;
				}
				updatePlatformRequest.setHostURL(hostURL);
				fieldsToUpdate.add("hostURL");
			}

			if (!Parameter.UNSPECIFIED.equals(contextRoot)) {
				if ("null".equals(contextRoot)) {
					hostURL = null;
				}
				updatePlatformRequest.setContextRoot(contextRoot);
				fieldsToUpdate.add("contextRoot");
			}

			boolean succeed = domainService.updatePlatformConfig(machineId, updatePlatformRequest, fieldsToUpdate);
			System.out.println("Platform config updated: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Remove Platform configuration")
	public void remove_platform( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_platform", new String[] { "machineId", machineId }, new String[] { "id", id });

		try {
			DomainManagementService domainService = getDomainManagementService();

			boolean succeed = domainService.deletePlatformConfig(machineId, id);
			System.out.println("Platform config removed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Node configs
	// lnodes
	// lnode
	// add_node
	// update_node
	// remove_node
	// ----------------------------------------------------------------
	@Descriptor("List node configs")
	public void lnodeconfigs( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "lnodeconfigs", new String[] { "machineId", machineId }, new String[] { "platformId", platformId });

		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(platformId)) {
				System.out.println("Please specify -platformId parameter");
				return;
			}

			DomainManagementService domainService = getDomainManagementService();

			List<NodeConfigRTO> nodeConfigs = domainService.getNodeConfigs(machineId, platformId);

			String[][] rows = new String[nodeConfigs.size()][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfigRTO nodeConfig : nodeConfigs) {
				String currMachineId = nodeConfig.getMachineId();
				String currPlatformId = nodeConfig.getPlatformId();
				String currId = nodeConfig.getId();
				String name = nodeConfig.getName();
				String home = nodeConfig.getHome();
				String hostURL = nodeConfig.getHostURL();
				String contextRoot = nodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currPlatformId, currId, name, home, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Get node config")
	public void lnodeconfig( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = "null") String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "lnodeconfig", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "id", id });

		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(platformId)) {
				System.out.println("Please specify -platformId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagementService domainService = getDomainManagementService();

			List<NodeConfigRTO> nodeConfigs = new ArrayList<NodeConfigRTO>();
			NodeConfigRTO nodeConfig = domainService.getNodeConfig(machineId, platformId, id);
			if (nodeConfig != null) {
				nodeConfigs.add(nodeConfig);
			}

			String[][] rows = new String[nodeConfigs.size()][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfigRTO currNodeConfig : nodeConfigs) {
				String currMachineId = currNodeConfig.getMachineId();
				String currPlatformId = currNodeConfig.getPlatformId();
				String currId = currNodeConfig.getId();
				String name = currNodeConfig.getName();
				String hostURL = currNodeConfig.getHostURL();
				String contextRoot = currNodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currPlatformId, currId, name, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Add node config")
	public void add_nodeconfig( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = "null") String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot, //
			@Descriptor("Node home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "add_nodeconfig", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot }, new String[] { "home", home });

		try {
			DomainManagementService domainService = getDomainManagementService();

			NodeConfigRTO addNodeRequest = new NodeConfigRTO();
			addNodeRequest.setMachineId(machineId);
			addNodeRequest.setPlatformId(platformId);
			addNodeRequest.setId(id);
			addNodeRequest.setName(name);
			addNodeRequest.setHostURL(hostURL);
			addNodeRequest.setContextRoot(contextRoot);
			addNodeRequest.setHome(home);

			boolean succeed = domainService.addNodeConfig(machineId, platformId, addNodeRequest);
			System.out.println("Node config added: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Update node config")
	public void update_nodeconfig( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = "null") String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot, //
			@Descriptor("Node home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "update_nodeconfig", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot }, new String[] { "home", home });

		try {
			DomainManagementService domainService = getDomainManagementService();

			NodeConfigRTO updateNodeRequest = new NodeConfigRTO();
			updateNodeRequest.setMachineId(machineId);
			updateNodeRequest.setPlatformId(platformId);
			updateNodeRequest.setId(id);

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				updateNodeRequest.setName(name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(hostURL)) {
				if ("null".equals(hostURL)) {
					hostURL = null;
				}
				updateNodeRequest.setHostURL(hostURL);
				fieldsToUpdate.add("hostURL");
			}

			if (!Parameter.UNSPECIFIED.equals(contextRoot)) {
				if ("null".equals(contextRoot)) {
					hostURL = null;
				}
				updateNodeRequest.setContextRoot(contextRoot);
				fieldsToUpdate.add("contextRoot");
			}

			if (!Parameter.UNSPECIFIED.equals(home)) {
				if ("null".equals(home)) {
					home = null;
				}
				updateNodeRequest.setHome(home);
				fieldsToUpdate.add("home");
			}

			boolean succeed = domainService.updateNodeConfig(machineId, platformId, updateNodeRequest, fieldsToUpdate);
			System.out.println("Node config updated: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Remove node config")
	public void remove_nodeconfig( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = "null") String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_nodeconfig", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "id", id });

		try {
			DomainManagementService domainService = getDomainManagementService();

			boolean succeed = domainService.deleteNodeConfig(machineId, platformId, id);
			System.out.println("Node config removed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
