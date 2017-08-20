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
import org.origin.common.util.CLIHelper;
import org.origin.common.util.DateUtil;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class DomainManagementCLICommand implements Annotated {

	// Service type constants
	public static final String USER_REGISTRY = "userregistry";
	public static final String OAUTH2 = "oauth2";
	public static final String CONFIGR_EGISTRY = "configregistry";
	public static final String APP_STORE = "appstore";
	public static final String DOMAIN = "domain";
	public static final String TRANSFER_AGENT = "transferagent";

	// Column names constants
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "index_item_id", "domain_mgmt.host.url", "domain_mgmt.context_root", "domain_mgmt.name", "last_heartbeat_time", "heartbeat_expired" };
	protected static String[] MACHINE_CONFIG_TITLES = new String[] { "ID", "Name", "IP Address" };
	protected static String[] TRANSFER_AGENT_CONFIG_TITLES = new String[] { "Machine ID", "ID", "Name", "hostURL", "contextRoot" };
	protected static String[] NODE_CONFIG_TITLES = new String[] { "Machine ID", "Transfer Agent ID", "ID", "Name", "hostURL", "contextRoot" };

	protected BundleContext bundleContext;
	protected String scheme = "orbit";

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public DomainManagementCLICommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	protected String getScheme() {
		return this.scheme;
	}

	public void start() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".start()");
		}

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// show available services
						"lservices", //

						// machine configurations
						"list_machines", "list_machine", "add_machine", "update_machine", "remove_machine", //

						// transfer agent configurations
						"list_tas", "list_ta", "add_ta", "update_ta", "remove_ta", //

						// node configurations
						"list_nodes", "list_node", "add_node", "update_node", "remove_node", //
		});

		OSGiServiceUtil.register(this.bundleContext, DomainManagementCLICommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}

		OSGiServiceUtil.unregister(DomainManagementCLICommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void domainMgmtConnectorSet() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".domainMgmtConnectorSet() Dependency on DomainMgmtConnector is set.");
		}
	}

	@DependencyUnfullfilled
	public void domainMgmtConnectorUnset() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".domainMgmtConnectorSet() Dependency on DomainMgmtConnector is unset.");
		}
	}

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

	protected DomainManagement getDomainManagement() throws ClientException {
		DomainManagement domainMgmt = this.domainMgmtConnector.getService();
		checkDomainManagement(domainMgmt);
		print(domainMgmt);
		return domainMgmt;
	}

	protected void checkConnector() throws ClientException {
		if (this.domainMgmtConnector == null) {
			System.out.println("DomainMgmtConnector is not available.");
			throw new ClientException(500, "DomainMgmtConnector is not available.");
		}
	}

	protected void checkDomainManagement(DomainManagement domainMgmt) throws ClientException {
		if (domainMgmt == null) {
			System.err.println(getClass().getSimpleName() + ".checkDomainManagement() domainMgmt is not available.");
			throw new ClientException(500, "domainMgmt is not available.");
		}
	}

	protected void print(DomainManagement domainMgmt) {
		if (domainMgmt == null) {
			System.out.println("DomainManagement service is null.");
			return;
		} else {
			System.out.println(domainMgmt.getName() + " (" + domainMgmt.getURL() + ")");
		}
	}

	// -----------------------------------------------------------------------------------------
	// Service
	// lservices
	// -----------------------------------------------------------------------------------------
	@Descriptor("List services")
	public void lservices(@Descriptor("The service to list") @Parameter(names = { "-s", "--service" }, absentValue = "null") String service) throws ClientException {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "lservices", new String[] { "-service", service });
		}

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

	protected void listDomainServices() throws ClientException {
		List<LoadBalanceResource<DomainManagement>> resources = getServiceResources();

		String[][] rows = new String[resources.size()][DOMAIN_SERVICES_TITLES.length];
		int rowIndex = 0;
		for (LoadBalanceResource<DomainManagement> resource : resources) {
			Integer indexItemId = ResourcePropertyHelper.INSTANCE.getIndexItemId(resource);
			String hostUrl = ResourcePropertyHelper.INSTANCE.getHostUrl(resource);
			String contextRoot = ResourcePropertyHelper.INSTANCE.getContextRoot(resource);
			String name = ResourcePropertyHelper.INSTANCE.getName(resource);
			Date heartBeatTime = ResourcePropertyHelper.INSTANCE.getHeartbeatTime(resource);
			boolean expired = ResourcePropertyHelper.INSTANCE.isHeartBeatExpired(resource);

			rows[rowIndex++] = new String[] { indexItemId.toString(), hostUrl, contextRoot, name, DateUtil.toString(heartBeatTime, DateUtil.SIMPLE_DATE_FORMAT2), expired ? "yes" : "no" };
		}

		PrettyPrinter.prettyPrint(DOMAIN_SERVICES_TITLES, rows, resources.size());
	}

	// -----------------------------------------------------------------------------------------
	// Machine configs
	// list_machines
	// list_machine
	// add_machine
	// update_machine
	// remove_machine
	// -----------------------------------------------------------------------------------------
	@Descriptor("List machine configs")
	public void list_machines() {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_machines", new String[] { "n/a", null });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.GET_MACHINE_CONFIGS);
			Responses responses = domainMgmt.sendRequest(request);

			MachineConfig[] machineConfigs = domainMgmt.getResponseConverter().convertToMachineConfigs(responses);
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

	@Descriptor("List machine config")
	public void list_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_machine", new String[] { "id", id });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.GET_MACHINE_CONFIG);
			request.setParameter("machineId", id);

			Responses responses = domainMgmt.sendRequest(request);
			MachineConfig resultMachineConfig = domainMgmt.getResponseConverter().convertToMachineConfig(responses);

			MachineConfig[] machineConfigs = (resultMachineConfig != null) ? new MachineConfig[] { resultMachineConfig } : new MachineConfig[] {};
			String[][] rows = new String[machineConfigs.length][MACHINE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (MachineConfig machineConfig : machineConfigs) {
				String currMachineId = machineConfig.getId();
				String name = machineConfig.getName();
				String ip = machineConfig.getIpAddress();

				rows[rowIndex++] = new String[] { currMachineId, name, ip };
			}
			PrettyPrinter.prettyPrint(MACHINE_CONFIG_TITLES, rows, machineConfigs.length);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "add_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.ADD_MACHINE_CONFIG);
			request.setParameter("machineId", id);
			request.setParameter("name", name);
			request.setParameter("ipAddress", ip);

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

	@Descriptor("Update machine config")
	public void update_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ip //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "update_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.UPDATE_MACHINE_CONFIG);
			request.setParameter("machineId", id);

			List<String> fieldsToUpdate = new ArrayList<String>();

			if (!Parameter.UNSPECIFIED.equals(name)) {
				if ("null".equals(name)) {
					name = null;
				}
				request.setParameter("name", name);
				fieldsToUpdate.add("name");
			}

			if (!Parameter.UNSPECIFIED.equals(ip)) {
				if ("null".equals(ip)) {
					ip = null;
				}
				request.setParameter("ipAddress", ip);
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

	@Descriptor("Remove machine config")
	public void remove_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "remove_machine", new String[] { "id", id });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.REMOVE_MACHINE_CONFIG);
			request.setParameter("machineId", id);

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
	// TA configs
	// list_tas
	// list_ta
	// add_ta
	// update_ta
	// remove_ta
	// ----------------------------------------------------------------
	@Descriptor("List TA configs")
	public void list_tas( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_tas", new String[] { "machineId", machineId });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

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
			String[][] rows = new String[taConfigs.length][TRANSFER_AGENT_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (TransferAgentConfig taConfig : taConfigs) {
				String currMachineId = taConfig.getMachineId();
				String id = taConfig.getId();
				String name = taConfig.getName();
				String hostURL = taConfig.getHostURL();
				String contextRoot = taConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, id, name, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(TRANSFER_AGENT_CONFIG_TITLES, rows, taConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List TA config")
	public void list_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_ta", new String[] { "machineId", machineId }, new String[] { "id", id });
		}

		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagement domainMgmt = getDomainManagement();

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

	@Descriptor("Add TA configuration")
	public void add_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Transfer Agent Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Transfer Agent host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Transfer Agent context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "add_ta", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request request = new Request(OrbitConstants.Requests.ADD_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);
			request.setParameter("name", name);
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

	@Descriptor("Update TA configuration")
	public void update_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Transfer Agent Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Transfer Agent host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Transfer Agent contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "update_ta", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

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

	@Descriptor("Remove TA configuration")
	public void remove_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "remove_ta", new String[] { "machineId", machineId }, new String[] { "id", id });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

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
	// Node configs
	// list_nodes
	// list_node
	// add_node
	// update_node
	// remove_node
	// ----------------------------------------------------------------
	@Descriptor("List node configs")
	public void list_nodes( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });
		}

		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(transferAgentId)) {
				System.out.println("Please specify -transferAgentId parameter");
				return;
			}

			DomainManagement domainMgmt = getDomainManagement();

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

	@Descriptor("Get node config")
	public void list_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id });
		}

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

			DomainManagement domainMgmt = getDomainManagement();

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
				String hostURL = nodeConfig.getHostURL();
				String contextRoot = nodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currTransferAgentId, currId, name, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Add node config")
	public void add_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node context root") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "add_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

			Request addNodeConfigRequest = new Request(OrbitConstants.Requests.ADD_NODE_CONFIG);
			addNodeConfigRequest.setParameter("machineId", machineId);
			addNodeConfigRequest.setParameter("transferAgentId", transferAgentId);
			addNodeConfigRequest.setParameter("id", id);
			addNodeConfigRequest.setParameter("name", name);
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

	@Descriptor("Update node config")
	public void update_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Node Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Node host URL") @Parameter(names = { "-hostURL", "--hostURL" }, absentValue = Parameter.UNSPECIFIED) String hostURL, //
			@Descriptor("Node contextRoot") @Parameter(names = { "-contextRoot", "--contextRoot" }, absentValue = Parameter.UNSPECIFIED) String contextRoot //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "update_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

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

	@Descriptor("Remove node config")
	public void remove_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "remove_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id });
		}

		try {
			DomainManagement domainMgmt = getDomainManagement();

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

}
