package org.orbit.component.cli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.NodeConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.util.DomainUtil;
import org.orbit.component.connector.util.ClientModelConverter;
import org.orbit.component.model.RequestConstants;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.annotation.Annotated;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.ResponseUtil;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainManagementClientCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.component.cli.DomainManagementClientCommand";

	protected static Logger LOG = LoggerFactory.getLogger(DomainManagementClientCommand.class);

	// Column names constants
	protected static String[] DOMAIN_SERVICES_TITLES = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] AUTH_SERVICES_TITLES = new String[] { "Index Item Id", "Namespace", "Name", "Host URL", "Context Root", "Heartbeat Time", "" };
	protected static String[] MACHINE_CONFIG_TITLES = new String[] { "ID", "Name", "IP Address" };
	protected static String[] PLATFORM_CONFIG_TITLES = new String[] { "Machine ID", "ID", "Name", "hostURL", "Context Root", "Home" };
	protected static String[] NODE_CONFIG_TITLES = new String[] { "Machine ID", "Transfer Agent ID", "ID", "Name", "Host URL", "Context Root" };

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	protected String scheme = "orbit";

	protected String getScheme() {
		return this.scheme;
	}

	public void start(BundleContext bundleContext) {
		LOG.info("start()");
		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put(COMMAND_SCOPE, getScheme());
		props.put(COMMAND_FUNCTION, new String[] { //
				// machine configurations
				"list_machines", "list_machine", "add_machine", "update_machine", "remove_machine", //

				// transfer agent configurations
				"list_tas", "list_ta", "add_ta", "update_ta", "remove_ta", //

				// node configurations
				"list_nodes", "list_node", "add_node", "update_node", "remove_node", //
		});

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		this.properties = properties;

		OSGiServiceUtil.register(this.bundleContext, DomainManagementClientCommand.class.getName(), this, props);
	}

	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(DomainManagementClientCommand.class.getName(), this);

		this.bundleContext = null;
	}

	protected DomainManagementClient getDomainManagementCLient() {
		String domainServiceUrl = (String) this.properties.get(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		DomainManagementClient domainService = DomainUtil.getClient(domainServiceUrl, null);
		if (domainService == null) {
			throw new IllegalStateException("DomainManagementClient is null.");
		}
		return domainService;
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
		CLIHelper.getInstance().printCommand(getScheme(), "list_machines");

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_MACHINE_CONFIGS);
			response = domainClient.sendRequest(request);

			MachineConfig[] machineConfigs = ClientModelConverter.Domain.convertToMachineConfigs(response);
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
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("List machine config")
	public void list_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_machine", new String[] { "id", id });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_MACHINE_CONFIG);
			request.setParameter("machineId", id);

			response = domainClient.sendRequest(request);
			MachineConfig resultMachineConfig = ClientModelConverter.Domain.convertToMachineConfig(response);

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
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Add machine config")
	public void add_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = "null") String name, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = "null") String ip //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "add_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.ADD_MACHINE_CONFIG);
			request.setParameter("machineId", id);
			request.setParameter("name", name);
			request.setParameter("ipAddress", ip);

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Update machine config")
	public void update_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id, //
			@Descriptor("Machine Name") @Parameter(names = { "-name", "--name" }, absentValue = Parameter.UNSPECIFIED) String name, //
			@Descriptor("Machine IP Address") @Parameter(names = { "-ip", "--ip" }, absentValue = Parameter.UNSPECIFIED) String ip //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "update_machine", new String[] { "id", id }, new String[] { "name", name }, new String[] { "ip", ip });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.UPDATE_MACHINE_CONFIG);
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

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Remove machine config")
	public void remove_machine( //
			@Descriptor("Machine ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_machine", new String[] { "id", id });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.REMOVE_MACHINE_CONFIG);
			request.setParameter("machineId", id);

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "list_tas", new String[] { "machineId", machineId });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_TA_CONFIGS);
			if (!Parameter.UNSPECIFIED.equals(machineId)) {
				if ("null".equals(machineId)) {
					machineId = null;
				}
				if (machineId != null) {
					request.setParameter("machineId", machineId);
				}
			}

			response = domainClient.sendRequest(request);

			PlatformConfig[] platformConfigs = ClientModelConverter.Domain.convertToPlatformConfigs(response);
			String[][] rows = new String[platformConfigs.length][PLATFORM_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (PlatformConfig platformConfig : platformConfigs) {
				String currMachineId = platformConfig.getMachineId();
				String id = platformConfig.getId();
				String name = platformConfig.getName();
				String hostURL = platformConfig.getHostURL();
				String contextRoot = platformConfig.getContextRoot();
				String home = platformConfig.getHome();

				rows[rowIndex++] = new String[] { currMachineId, id, name, hostURL, contextRoot, home };
			}
			PrettyPrinter.prettyPrint(PLATFORM_CONFIG_TITLES, rows, platformConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("List TA config")
	public void list_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_ta", new String[] { "machineId", machineId }, new String[] { "id", id });

		Response response = null;
		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(id)) {
				System.out.println("Please specify -id parameter");
				return;
			}

			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);

			response = domainClient.sendRequest(request);
			PlatformConfig resultTaConfig = ClientModelConverter.Domain.convertToPlatformConfig(response);

			PlatformConfig[] taConfigs = (resultTaConfig != null) ? new PlatformConfig[] { resultTaConfig } : new PlatformConfig[] {};
			String[][] rows = new String[taConfigs.length][PLATFORM_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (PlatformConfig taConfig : taConfigs) {
				String taId = taConfig.getId();
				String name = taConfig.getName();
				String hostURL = taConfig.getHostURL();
				String contextRoot = taConfig.getContextRoot();
				String home = taConfig.getHome();

				rows[rowIndex++] = new String[] { machineId, taId, name, hostURL, contextRoot, home };
			}
			PrettyPrinter.prettyPrint(PLATFORM_CONFIG_TITLES, rows, taConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "add_ta", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.ADD_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);
			request.setParameter("name", name);
			request.setParameter("hostURL", hostURL);
			request.setParameter("contextRoot", contextRoot);

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "update_ta", new String[] { "machineId", machineId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.UPDATE_TA_CONFIG);
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

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Remove TA configuration")
	public void remove_ta( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_ta", new String[] { "machineId", machineId }, new String[] { "id", id });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.REMOVE_TA_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("id", id);

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });

		Response response = null;
		try {
			if (Parameter.UNSPECIFIED.equals(machineId)) {
				System.out.println("Please specify -machineId parameter");
				return;
			}
			if (Parameter.UNSPECIFIED.equals(transferAgentId)) {
				System.out.println("Please specify -transferAgentId parameter");
				return;
			}

			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_NODE_CONFIGS);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);

			response = domainClient.sendRequest(request);

			NodeConfig[] nodeConfigs = ClientModelConverter.Domain.convertToNodeConfigs(response);
			String[][] rows = new String[nodeConfigs.length][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfig nodeConfig : nodeConfigs) {
				String currMachineId = nodeConfig.getMachineId();
				String currTransferAgentId = nodeConfig.getPlatformId();
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
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Get node config")
	public void list_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = "null") String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = "null") String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id });

		Response response = null;
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

			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.GET_NODE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);
			request.setParameter("id", id);

			response = domainClient.sendRequest(request);
			NodeConfig resultNodeConfig = ClientModelConverter.Domain.convertToNodeConfig(response);

			NodeConfig[] nodeConfigs = (resultNodeConfig != null) ? new NodeConfig[] { resultNodeConfig } : new NodeConfig[] {};
			String[][] rows = new String[nodeConfigs.length][NODE_CONFIG_TITLES.length];
			int rowIndex = 0;
			for (NodeConfig nodeConfig : nodeConfigs) {
				String currMachineId = nodeConfig.getMachineId();
				String currTransferAgentId = nodeConfig.getPlatformId();
				String currId = nodeConfig.getId();
				String name = nodeConfig.getName();
				String hostURL = nodeConfig.getHostURL();
				String contextRoot = nodeConfig.getContextRoot();

				rows[rowIndex++] = new String[] { currMachineId, currTransferAgentId, currId, name, hostURL, contextRoot };
			}
			PrettyPrinter.prettyPrint(NODE_CONFIG_TITLES, rows, nodeConfigs.length);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "add_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		Response response = null;
		try {
			DomainManagementClient domainService = getDomainManagementCLient();

			Request addNodeConfigRequest = new Request(RequestConstants.ADD_NODE_CONFIG);
			addNodeConfigRequest.setParameter("machineId", machineId);
			addNodeConfigRequest.setParameter("transferAgentId", transferAgentId);
			addNodeConfigRequest.setParameter("id", id);
			addNodeConfigRequest.setParameter("name", name);
			addNodeConfigRequest.setParameter("hostURL", hostURL);
			addNodeConfigRequest.setParameter("contextRoot", contextRoot);

			response = domainService.sendRequest(addNodeConfigRequest);
			if (response != null) {
				// System.out.println(response.getSimpleLabel());
				// if (response.getException() != null) {
				// response.getException().printStackTrace();
				// }

				// If node config is added successfully, create the node through ta.
				// if (Response.SUCCESS.equals(response.getStatus())) {
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
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
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
		CLIHelper.getInstance().printCommand(getScheme(), "update_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id }, new String[] { "name", name }, new String[] { "hostURL", hostURL }, new String[] { "contextRoot", contextRoot });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.UPDATE_NODE_CONFIG);
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

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

	@Descriptor("Remove node config")
	public void remove_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = "null") String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-id", "--id" }, absentValue = Parameter.UNSPECIFIED) String id //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "remove_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "id", id });

		Response response = null;
		try {
			DomainManagementClient domainClient = getDomainManagementCLient();

			Request request = new Request(RequestConstants.REMOVE_NODE_CONFIG);
			request.setParameter("machineId", machineId);
			request.setParameter("transferAgentId", transferAgentId);
			request.setParameter("id", id);

			response = domainClient.sendRequest(request);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
	}

}
