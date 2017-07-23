package org.orbit.component.cli.tier3;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.TransferAgentConfig;
import org.orbit.component.api.tier3.domain.request.UpdateMachineConfigRequest;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentHelper;
import org.orbit.component.model.tier3.domain.request.AddMachineConfigRequest;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.loadbalance.LoadBalancer;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class DomainManagementCLICommand implements Annotated {

	// Column names constants
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.host.url", "domain_mgmt.context_root", "domain_mgmt.name", "last_heartbeat_time", "heartbeat_expired" };
	protected static String[] MACHINE_CONFIG_TITLES = new String[] { "ID", "Name", "IP Address" };
	protected static String[] TRANSFER_AGENT_CONFIG_TITLES = new String[] { "Machine ID", "ID", "Name", "Home", "hostURL", "contextRoot" };
	protected static String[] NODE_CONFIG_TITLES = new String[] { "Machine ID", "Transfer Agent ID", "ID", "Name", "Home", "hostURL", "contextRoot" };

	// Service types
	public static final String USER_REGISTRY = "userregistry";
	public static final String OAUTH2 = "oauth2";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	protected BundleContext bundleContext;

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public DomainManagementCLICommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		System.out.println("DomainManagementCLICommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "orbit");
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //

						// machine configurations
						"_lmachines", "_addmachine", "_updatemachine", "_removemachine", //
						"getmachines", "getmachine", "addmachine", "updatemachine", "removemachine", //

						// transfer agent configurations
						"gettransferagents", "gettransferagent", "addtransferagent", "updatetransferagent", "removetransferagent", //

						// node configurations
						"getnodes", "getnode", "addnode", "updatenode", "removenode", //

						// TA live commands
						"ta_ping", "ta_list_nodes", "ta_node_exist", "ta_create_node", "ta_delete_node", "ta_start_node", "ta_stop_node",//
		});

		OSGiServiceUtil.register(this.bundleContext, DomainManagementCLICommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		System.out.println("DomainManagementCLICommand.stop()");

		OSGiServiceUtil.unregister(DomainManagementCLICommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void domainMgmtConnectorSet() {
		System.out.println("DomainMgmtConnector is set.");
	}

	@DependencyUnfullfilled
	public void domainMgmtConnectorUnset() {
		System.out.println("DomainMgmtConnector is unset.");
	}

	protected void checkConnector() throws ClientException {
		if (this.domainMgmtConnector == null) {
			System.out.println("DomainMgmtConnector is not available.");
			throw new ClientException(500, "DomainMgmtConnector is not available.");
		}
	}

	/**
	 * 
	 * @param domainMgmt
	 */
	protected void print(DomainManagement domainMgmt) {
		if (domainMgmt == null) {
			System.out.println("DomainManagement service is null.");
			return;
		} else {
			System.out.println(domainMgmt.getName() + " (" + domainMgmt.getURL() + ")");
		}
	}

	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		// System.out.println("list services: " + service);
		if (USER_REGISTRY.equalsIgnoreCase(service)) {

		} else if (OAUTH2.equalsIgnoreCase(service)) {

		} else if (CONFIGR_EGISTRY.equalsIgnoreCase(service)) {

		} else if (APP_STORE.equalsIgnoreCase(service)) {

		} else if (DOMAIN.equalsIgnoreCase(service)) {
			listDomainServices();

		} else if (TRANSFER_AGENT.equalsIgnoreCase(service)) {

		} else {
			System.err.println("###### Unsupported service name: " + service);
		}
	}

	public void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagement>> resources = getServiceResources();

		// System.out.println("Number of services: " + resources.size());
		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagement> resource : resources) {
			// String id = resource.getId();
			// DomainManagement domainMgmt = resource.getService();
			// String name = domainMgmt.getName();

			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getHostUrl(resource);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getContextRoot(resource);
			String name = ResourcePropertyHelper.INSTANCE.getName(resource);
			Date heartBeatTime = ResourcePropertyHelper.INSTANCE.getHeartbeatTime(resource);
			boolean expired = ResourcePropertyHelper.INSTANCE.isHeartBeatExpired(resource);

			// String url = domainMgmt.getURL();
			// System.out.println(name + " (id = '" + id + "', url = '" + url + "')");
			// System.out.println(name + " (url = '" + url + "')");
			// System.out.println(name);
			// Map<?, ?> properties = resource.getProperties();
			// Printer.pl(properties);
			// System.out.println("ping: " + domainMgmt.ping());
			// System.out.println();

			// System.out.println(indexItemId + " - " + hostUrl + " - " + contextRoot + " - " + name + " - " + DateUtil.toString(heartBeatTime,
			// DateUtil.SIMPLE_DATE_FORMAT2));

			rows[rowIndex++] = new String[] { indexItemId.toString(), hostUrl, contextRoot, name, DateUtil.toString(heartBeatTime, DateUtil.SIMPLE_DATE_FORMAT2), expired ? "yes" : "no" };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_TITLES, rows, resources.size());
	}

	/**
	 * 
	 * @return
	 * @throws ClientException
	 */
	protected List<LoadBalanceResource<DomainManagement>> getServiceResources() throws ClientException {
		checkConnector();

		LoadBalancer<DomainManagement> lb = this.domainMgmtConnector.getLoadBalancer();
		if (lb == null) {
			System.out.println("DomainManagement LoadBalancer is not available.");
			return null;
		}

		List<LoadBalanceResource<DomainManagement>> resources = lb.getResources();
		if (resources == null) {
			System.out.println("DomainManagement LoadBalancer's resource is null.");
			return null;
		}
		return resources;
	}

	// ----------------------------------------------------------------
	// Machine configuration
	// ----------------------------------------------------------------
	@Descriptor("List machine configurations")
	public void _lmachines() {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			MachineConfig[] machineConfigs = domainMgmt.getMachineConfigs();
			// System.out.println("Number of machines: " + machineConfigs.length);
			String[][] rows = new String[machineConfigs.length][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfig machineConfig : machineConfigs) {
				String machineId = machineConfig.getId();
				String machineName = machineConfig.getName();
				String ipAddress = machineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { machineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Add machine configuration")
	public void _addmachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String machineName, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = "null") String ipAddress //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			AddMachineConfigRequest addMachineRequest = new AddMachineConfigRequest();
			addMachineRequest.setMachineId(machineId);
			addMachineRequest.setName(machineName);
			addMachineRequest.setIpAddress(ipAddress);

			boolean succeed = domainMgmt.addMachineConfig(addMachineRequest);
			if (succeed) {
				System.out.println("Machine is added.");
			} else {
				System.out.println("Machine is not added.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Update machine configuration")
	public void _updatemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String machineName, //
			@Descriptor("IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ipAddress //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			UpdateMachineConfigRequest updateMachineRequest = new UpdateMachineConfigRequest();
			updateMachineRequest.setMachineId(machineId);

			if (!Parameter.UNSPECIFIED.equals(machineName)) {
				if ("null".equals(machineName)) {
					machineName = null;
				}
				updateMachineRequest.setName(machineName);
			}

			if (!Parameter.UNSPECIFIED.equals(ipAddress)) {
				if ("null".equals(ipAddress)) {
					ipAddress = null;
				}
				updateMachineRequest.setIpAddress(ipAddress);
			}

			boolean succeed = domainMgmt.updateMachineConfig(updateMachineRequest);
			if (succeed) {
				System.out.println("Machine is updated.");
			} else {
				System.out.println("Machine is not updated.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 */
	@Descriptor("Remmove machine configuration")
	public void _removemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			boolean succeed = domainMgmt.removeMachineConfig(machineId);
			if (succeed) {
				System.out.println("Machine is removed.");
			} else {
				System.out.println("Machine is not removed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Machine configuration
	// ----------------------------------------------------------------
	@Descriptor("Get machine configurations")
	public void getmachines() {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_MACHINE_CONFIGS);
			Responses responses = domainMgmt.sendRequest(request);

			MachineConfig[] machineConfigs = domainMgmt.getResponseConverter().convertToMachineConfigs(responses);
			// System.out.println("Number of machines: " + machineConfigs.length);
			String[][] rows = new String[machineConfigs.length][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfig machineConfig : machineConfigs) {
				String machineId = machineConfig.getId();
				String machineName = machineConfig.getName();
				String ipAddress = machineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { machineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 */
	@Descriptor("Get machine configuration")
	public void getmachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String machineId //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_MACHINE_CONFIG);
			request.setParameter("machineId", machineId);

			Responses responses = domainMgmt.sendRequest(request);
			MachineConfig resultMachineConfig = domainMgmt.getResponseConverter().convertToMachineConfig(responses);

			MachineConfig[] machineConfigs = (resultMachineConfig != null) ? new MachineConfig[] { resultMachineConfig } : new MachineConfig[] {};
			String[][] rows = new String[machineConfigs.length][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfig machineConfig : machineConfigs) {
				String currMachineId = machineConfig.getId();
				String machineName = machineConfig.getName();
				String ipAddress = machineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { currMachineId, machineName, ipAddress };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Add machine configuration")
	public void addmachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String machineName, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = "null") String ipAddress //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.ADD_MACHINE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("name", machineName);
			request.setParameter("ipAddress", ipAddress);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param machineName
	 * @param ipAddress
	 */
	@Descriptor("Update machine configuration")
	public void updatemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String machineName, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ipAddress //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.UPDATE_MACHINE_CONFIG);
			request.setParameter("machineId", machineId);

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(machineName)) {
				if ("null".equals(machineName)) {
					machineName = null;
				}
				request.setParameter("name", machineName);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(ipAddress)) {
				if ("null".equals(ipAddress)) {
					ipAddress = null;
				}
				request.setParameter("ipAddress", ipAddress);
				fieldsToUpdate.add("ipAddress");
			}

			request.setParameter("fieldsToUpdate", fieldsToUpdate);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 */
	@Descriptor("Remove machine configuration")
	public void removemachine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.REMOVE_MACHINE_CONFIG);
			request.setParameter("machineId", machineId);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Transfer Agent configuration
	// ----------------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 */
	@Descriptor("Get transfer agent configurations")
	public void gettransferagents( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_TA_CONFIGS);
			if (!Parameter.UNSPECIFIED.equals(machineId)) {
				if ("null".equals(machineId)) {
					machineId = null;
				}
				if (machineId != null) {
					request.setParameter("machineId", machineId);
				}
			}

			Responses responses = domainMgmt.sendRequest(request);

			TransferAgentConfig[] taConfigs = domainMgmt.getResponseConverter().convertToTransferAgentConfigs(responses);
			// System.out.println("Number of transfer agents: " + taConfigs.length);
			String[][] rows = new String[taConfigs.length][TRANSFER_AGENT_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (TransferAgentConfig taConfig : taConfigs) {
				String currMachineId = taConfig.getMachineId();
				String id = taConfig.getId();
				String name = taConfig.getName();
				String home = taConfig.getHome();
				String hostURL = taConfig.getHostURL();
				String contextRoot = taConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, id, name, home, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(TRANSFER_AGENT_CONFIG_TITLES, rows, taConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param id
	 */
	@Descriptor("Get transfer agent configuration")
	public void gettransferagent( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);

			Responses responses = domainMgmt.sendRequest(request);
			TransferAgentConfig resultTaConfig = domainMgmt.getResponseConverter().convertToTransferAgentConfig(responses);

			TransferAgentConfig[] taConfigs = (resultTaConfig != null) ? new TransferAgentConfig[] { resultTaConfig } : new TransferAgentConfig[] {};
			String[][] rows = new String[taConfigs.length][TRANSFER_AGENT_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (TransferAgentConfig taConfig : taConfigs) {
				String taId = taConfig.getId();
				String name = taConfig.getName();
				String home = taConfig.getHome();
				String hostURL = taConfig.getHostURL();
				String contextRoot = taConfig.getContextRoot();

				rows[rowIndex++] = new String[] { machineId, taId, name, home, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(TRANSFER_AGENT_CONFIG_TITLES, rows, taConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 */
	@Descriptor("Add transfer agent configuration")
	public void addtransferagent( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Transfer Agent Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Transfer Agent Home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home, //
			@Descriptor("Transfer Agent host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Transfer Agent context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.ADD_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);
			request.setParameter("name", name);
			request.setParameter("home", home);
			request.setParameter("hostURL", hostURL);
			request.setParameter("contextRoot", contextRoot);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 */
	@Descriptor("Update transfer agent configuration")
	public void updatetransferagent( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Transfer Agent Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Transfer Agent Home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home, //
			@Descriptor("Transfer Agent host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Transfer Agent contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.UPDATE_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				request.setParameter("name", name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(home)) {
				if ("null".equals(home)) {
					home = null;
				}
				request.setParameter("home", home);
				fieldsToUpdate.add("home");
			}

			if (!Parameter.UNSPECIFIED.equals(hostURL)) {
				if ("null".equals(hostURL)) {
					hostURL = null;
				}
				request.setParameter("hostURL", hostURL);
				fieldsToUpdate.add("hostURL");
			}

			if (!Parameter.UNSPECIFIED.equals(contextRoot)) {
				if ("null".equals(contextRoot)) {
					hostURL = null;
				}
				request.setParameter("contextRoot", contextRoot);
				fieldsToUpdate.add("contextRoot");
			}

			request.setParameter("fieldsToUpdate", fieldsToUpdate);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param id
	 */
	@Descriptor("Remove transfer agent configuration")
	public void removetransferagent( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.REMOVE_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// Node configuration
	// ----------------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 */
	@Descriptor("Get node configurations")
	public void getnodes( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(transferAgentId)) {
				System.out.println("Please specify -transferAgentId parameter");
				return;
			}

			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_NODE_CONFIGS);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);

			Responses responses = domainMgmt.sendRequest(request);

			NodeConfig[] nodeConfigs = domainMgmt.getResponseConverter().convertToNodeConfigs(responses);
			String[][] rows = new String[nodeConfigs.length][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfig nodeConfig : nodeConfigs) {
				String currMachineId = nodeConfig.getMachineId();
				String currTransferAgentId = nodeConfig.getTransferAgentId();
				String currId = nodeConfig.getId();
				String name = nodeConfig.getName();
				String home = nodeConfig.getHome();
				String hostURL = nodeConfig.getHostURL();
				String contextRoot = nodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currTransferAgentId, currId, name, home, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 */
	@Descriptor("Get node configuration")
	public void getnode( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(transferAgentId)) {
				System.out.println("Please specify -transferAgentId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.GET_NODE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);
			request.setParameter("id", id);

			Responses responses = domainMgmt.sendRequest(request);
			NodeConfig resultNodeConfig = domainMgmt.getResponseConverter().convertToNodeConfig(responses);

			NodeConfig[] nodeConfigs = (resultNodeConfig != null) ? new NodeConfig[] { resultNodeConfig } : new NodeConfig[] {};
			String[][] rows = new String[nodeConfigs.length][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfig nodeConfig : nodeConfigs) {
				String currMachineId = nodeConfig.getMachineId();
				String currTransferAgentId = nodeConfig.getTransferAgentId();
				String currId = nodeConfig.getId();
				String name = nodeConfig.getName();
				String home = nodeConfig.getHome();
				String hostURL = nodeConfig.getHostURL();
				String contextRoot = nodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currTransferAgentId, currId, name, home, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 */
	@Descriptor("Add node configuration")
	public void addnode( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node Home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request addNodeConfigRequest = new Request(OrbitConstants.Requests.ADD_NODE_CONFIG);
			addNodeConfigRequest.setParameter("machineId", machineId);
			addNodeConfigRequest.setParameter("transferAgentId", transferAgentId);
			addNodeConfigRequest.setParameter("id", id);
			addNodeConfigRequest.setParameter("name", name);
			addNodeConfigRequest.setParameter("home", home);
			addNodeConfigRequest.setParameter("hostURL", hostURL);
			addNodeConfigRequest.setParameter("contextRoot", contextRoot);

			Responses responses = domainMgmt.sendRequest(addNodeConfigRequest);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());
				if (response.getException() != null) {
					response.getException().printStackTrace();
				}

				// If node config is added successfully, create the node through ta.
				if (Response.SUCCESS.equals(response.getStatus())) {
					// TransferAgentConfig taConfig = domainMgmt.getTransferAgentConfig(machineId, transferAgentId);
					// if (taConfig == null) {
					// System.err.println("Transfer Agent configuration is not found.");
					// return;
					// }
					//
					// TransferAgent transferAgent = null;
					// if (taConfig != null) {
					// transferAgent = Activator.getInstance().getTransferAgentAdapter().getTransferAgent(taConfig);
					// }
					// if (transferAgent == null) {
					// System.err.println("Transfer Agent is not available.");
					// }
					//
					// boolean ping = transferAgent.ping();
					// System.out.println("ping = " + ping);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 * @param name
	 * @param home
	 * @param hostURL
	 * @param contextRoot
	 */
	@Descriptor("Update node configuration")
	public void updatenode( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node Home") @Parameter(names = { "-home", "--home" }, absentValue = Parameter.UNSPECIFIED) String home, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.UPDATE_NODE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);
			request.setParameter("id", id);

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				request.setParameter("name", name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(home)) {
				if ("null".equals(home)) {
					home = null;
				}
				request.setParameter("home", home);
				fieldsToUpdate.add("home");
			}

			if (!Parameter.UNSPECIFIED.equals(hostURL)) {
				if ("null".equals(hostURL)) {
					hostURL = null;
				}
				request.setParameter("hostURL", hostURL);
				fieldsToUpdate.add("hostURL");
			}

			if (!Parameter.UNSPECIFIED.equals(contextRoot)) {
				if ("null".equals(contextRoot)) {
					hostURL = null;
				}
				request.setParameter("contextRoot", contextRoot);
				fieldsToUpdate.add("contextRoot");
			}

			request.setParameter("fieldsToUpdate", fieldsToUpdate);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param id
	 */
	@Descriptor("Remove node configuration")
	public void removenode( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			Request request = new Request(OrbitConstants.Requests.REMOVE_NODE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);
			request.setParameter("id", id);

			Responses responses = domainMgmt.sendRequest(request);
			Response response = responses.getResponse(Response.class);
			if (response != null) {
				System.out.println(response.getSimpleLabel());

				if (response.getException() != null) {
					response.getException().printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------
	// ta live commands
	// ----------------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 */
	@Descriptor("Ping TA")
	public void ta_ping( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		System.out.println("command: ta_ping");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			boolean ping = transferAgent.ping();
			System.out.println("ping = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * List nodes.
	 * 
	 * @param machineId
	 * @param transferAgentId
	 */
	@Descriptor("List nodes in TA file system")
	public void ta_list_nodes( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		System.out.println("command: ta_list_nodes");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check node exists
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 */
	@Descriptor("Check node exists in TA file system")
	public void ta_node_exist( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		System.out.println("command: ta_node_exist");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);
		System.out.println("\tnodeId = " + nodeId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 */
	@Descriptor("Create node in TA file system")
	public void ta_create_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		System.out.println("command: ta_create_node");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);
		System.out.println("\tnodeId = " + nodeId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 */
	@Descriptor("Delete node in TA file system")
	public void ta_delete_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		System.out.println("command: ta_delete_node");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);
		System.out.println("\tnodeId = " + nodeId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 */
	@Descriptor("Start node in TA file system")
	public void ta_start_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		System.out.println("command: ta_start_node");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);
		System.out.println("\tnodeId = " + nodeId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 * @param nodeId
	 */
	@Descriptor("Stop node in TA file system")
	public void ta_stop_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		System.out.println("command: ta_stop_node");
		System.out.println("parameters:");
		System.out.println("\tmachineId = " + machineId);
		System.out.println("\ttransferAgentId = " + transferAgentId);
		System.out.println("\tnodeId = " + nodeId);

		try {
			DomainManagement domainMgmt = this.domainMgmtConnector.getService();
			print(domainMgmt);

			TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
