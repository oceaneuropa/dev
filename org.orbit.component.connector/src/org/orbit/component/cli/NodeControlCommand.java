package org.orbit.component.cli;

import java.util.Hashtable;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.Requests;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.connector.util.ModelConverter;
import org.orbit.platform.sdk.command.CommandActivator;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;
import org.origin.common.rest.client.ServiceClientCommand;
import org.origin.common.rest.model.Request;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeControlCommand extends ServiceClientCommand implements CommandActivator {

	public static final String ID = "org.orbit.component.cli.NodeControlCommand";

	protected static Logger LOG = LoggerFactory.getLogger(NodeControlCommand.class);

	protected static String[] NODESPACE_TITLES = new String[] { "Name" };
	protected static String[] NODE_TITLES = new String[] { "Name" };

	protected String scheme = "ta";
	protected Map<Object, Object> properties;

	@Override
	public String getScheme() {
		return this.scheme;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(final BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						"ping", "echo", //
						"platform_ping", "list_nodes", "list_nodes2", "get_node", "node_exist", "create_node", "delete_node", "start_node", "stop_node", "node_status"//
				});

		OSGiServiceUtil.register(bundleContext, NodeControlCommand.class.getName(), this, props);

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		this.properties = properties;
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(final BundleContext bundleContext) {
		OSGiServiceUtil.unregister(NodeControlCommand.class.getName(), this);
	}

	@Override
	public ServiceClient getServiceClient() {
		return getDomainService();
	}

	protected DomainManagementClient getDomainService() {
		DomainManagementClient domainService = OrbitClients.getInstance().getDomainService(this.properties);
		if (domainService == null) {
			throw new IllegalStateException("DomainService is null.");
		}
		return domainService;
	}

	protected NodeControlClient getNodeControl(String machineId, String platformId) throws ClientException {
		DomainManagementClient domainService = getDomainService();
		PlatformConfig platformConfig = domainService.getPlatformConfig(machineId, platformId);
		if (platformConfig != null) {
			String url = platformConfig.getHostURL() + platformConfig.getContextRoot();
			NodeControlClient nodeControlClient = OrbitClients.getInstance().getNodeControl(url);
			if (nodeControlClient == null) {
				throw new IllegalStateException("NodeControl is null.");
			}
			return nodeControlClient;
		}
		return null;
	}

	// -----------------------------------------------------------------------------------------
	// Platforms
	//
	// platform_ping
	// -----------------------------------------------------------------------------------------
	/**
	 * @param machineId
	 * @param platformId
	 */
	@Descriptor("platform_ping")
	public void platform_ping( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "machineId", machineId }, new String[] { "platformId", platformId });
		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			boolean ping = nodeControl.ping();
			System.out.println("ping result = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------------
	// Nodes
	//
	// list_nodes
	// node_exist
	// create_node
	// delete_node
	// start_node
	// stop_node
	// node_status
	// -----------------------------------------------------------------------------------------
	@Descriptor("List nodes")
	public void list_nodes( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "platformId", platformId });
		if (Parameter.UNSPECIFIED.equals(machineId)) {
			LOG.error("'-machineId' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(platformId)) {
			LOG.error("'-platformId' parameter is not set.");
			return;
		}

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.GET_NODES);
			Response response = nodeControl.sendRequest(request);

			NodeInfo[] nodeInfos = ModelConverter.NodeControl.getNodes(response);
			String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			int rowIndex = 0;
			for (NodeInfo nodeInfo : nodeInfos) {
				String name = nodeInfo.getName();
				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	@Descriptor("Get node")
	public void get_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("get_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_node", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.GET_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = nodeControl.sendRequest(request);

			NodeInfo nodeInfo = ModelConverter.NodeControl.getNode(response);
			NodeInfo[] nodeInfos = (nodeInfo != null) ? new NodeInfo[] { nodeInfo } : new NodeInfo[] {};
			String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			int rowIndex = 0;
			for (NodeInfo currNodeInfo : nodeInfos) {
				String name = currNodeInfo.getName();

				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Check whether node exists")
	public void node_exist( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("node_exist()");
		CLIHelper.getInstance().printCommand(getScheme(), "node_exist", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.NODE_EXIST);
			request.setParameter("nodeId", nodeId);

			Response response = nodeControl.sendRequest(request);
			boolean exists = ModelConverter.NodeControl.exists(response);
			LOG.info("exists: " + exists);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create a node")
	public void create_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("create_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "create_node", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.CREATE_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = nodeControl.sendRequest(request);

			boolean succeed = ModelConverter.NodeControl.isCreated(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete a node")
	public void delete_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("delete_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "delete_node", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.DELETE_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = nodeControl.sendRequest(request);

			boolean succeed = ModelConverter.NodeControl.isDeleted(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Get the status of a node")
	public void node_status( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("node_status()");
		CLIHelper.getInstance().printCommand(getScheme(), "node_status", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

			Request request = new Request(Requests.NODE_STATUS);
			request.setParameter("nodeId", nodeId);

			Response response = nodeControl.sendRequest(request);

			String status = ModelConverter.NodeControl.getStatus(response);
			LOG.info("status: " + status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Start a node")
	public void start_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("start_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "start_node", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Stop a node")
	public void stop_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Platform ID") @Parameter(names = { "-platformId", "--platformId" }, absentValue = Parameter.UNSPECIFIED) String platformId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("stop_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "stop_node", new String[] { "machineId", machineId }, new String[] { "platformId", platformId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			NodeControlClient nodeControl = getNodeControl(machineId, platformId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// public class TransferAgentCommand implements Annotated {

// @Dependency
// protected DomainServiceConnector domainConnector;

// @Dependency
// protected AuthConnector authConnector;

// @Dependency
// protected TransferAgentConnector transferAgentConnector;
// protected ServiceConnectorAdapterV1<TransferAgent> transferAgentConnector;
// protected ServiceConnectorAdapterV2 transferAgentConnector;

// OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
// OSGiServiceUtil.unregister(Annotated.class.getName(), this);

// DomainService domainService = this.domainConnector.getService();

// @DependencyFullfilled
// public void connectorsSet() {
// LOG.info("connectorsSet()");
// }
//
// @DependencyUnfullfilled
// public void connectorsUnset() {
// LOG.info("connectorsUnset()");
// }

// this.transferAgentConnector = new ServiceConnectorAdapterV1<TransferAgent>(TransferAgent.class.getName());
// this.transferAgentConnector.start(bundleContext);
// this.transferAgentConnector = new ServiceConnectorAdapterV2(TransferAgent.class.getName());
// this.transferAgentConnector.start(bundleContext);

// protected Auth getAuth() {
// if (this.authConnector == null) {
// LOG.error("AuthConnector is not available.");
// return null;
// }
// Auth auth = this.authConnector.getService();
// if (auth == null) {
// LOG.error("Auth is not available.");
// }
// return auth;
// }

// protected void checkDomainManagement(DomainManagement domainMgmt) throws ClientException {
// if (domainMgmt == null) {
// System.err.println(getClass().getSimpleName() + ".checkDomainManagement() domainMgmt is not available.");
// throw new ClientException(500, "domainMgmt is not available.");
// }
// }
// protected void checkTransferAgent(TransferAgent transferAgent) throws ClientException {
// if (transferAgent == null) {
// System.err.println(getClass().getSimpleName() + ".checkTransferAgent() transferAgent is not available.");
// throw new ClientException(500, "transferAgent is not available.");
// }
// }

// is_nodespace_opened
// open_nodespace
// close_nodespace

// @Descriptor("Check whether nodespace is opened")
// public void is_nodespace_opened( //
// @Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED)
// String transferAgentId,
// //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "is_nodespace_opened", new String[] { "machineId", machineId }, new String[] {
// "transferAgentId",
// transferAgentId }, new String[] { "nodespace", nodespace });
// }
//
// try {
// TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// @Descriptor("Open a nodespace")
// public void open_nodespace( //
// @Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED)
// String
// transferAgentId, //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "open_nodespace", new String[] { "machineId", machineId }, new String[] {
// "transferAgentId",
// transferAgentId }, new String[] { "nodespace", nodespace });
// }
//
// try {
// TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
// @Descriptor("Close a nodespace")
// public void close_nodespace( //
// @Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED)
// String
// transferAgentId, //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "close_nodespace", new String[] { "machineId", machineId }, new String[] {
// "transferAgentId",
// transferAgentId }, new String[] { "nodespace", nodespace });
// }
//
// try {
// TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// protected ServiceConnectorAdapter<Auth> authConnector;

// protected Auth getAuth(String realm, String username, String url) throws ClientException {
// Map<String, Object> properties = new HashMap<String, Object>();
// properties.put(OrbitConstants.REALM, realm);
// properties.put(OrbitConstants.USERNAME, username);
// properties.put(OrbitConstants.URL, url);
//
// Auth auth = this.authConnector.getService(properties);
// if (auth == null) {
// LOG.error("Auth is not available.");
// }
// return auth;
// }

// @Descriptor("login")
// public void login( //
// @Descriptor("Auth server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
// @Descriptor("username") @Parameter(names = { "-username", "--username" }, absentValue = Parameter.UNSPECIFIED) String username, //
// @Descriptor("password") @Parameter(names = { "-password", "--password" }, absentValue = Parameter.UNSPECIFIED) String password //
// ) {
// CLIHelper.getInstance().printCommand(getScheme(), "login", new String[] { "url", url }, new String[] { "username", username }, new
// String[] { "password",
// password });
// if (Parameter.UNSPECIFIED.equals(url)) {
// LOG.error("'-url' parameter is not set.");
// return;
// }
// if (Parameter.UNSPECIFIED.equals(username)) {
// LOG.error("'-username' parameter is not set.");
// return;
// }
// if (Parameter.UNSPECIFIED.equals(password)) {
// LOG.error("'-password' parameter is not set.");
// return;
// }
//
// try {
// Auth auth = getAuth(this.default_realm, this.default_username, url);
// if (auth == null) {
// return;
// }
//
// TokenRequest tokenRequest = new TokenRequest();
// tokenRequest.setGrantType(GrantTypes.USER_CREDENTIALS);
// tokenRequest.setUsername(username);
// tokenRequest.setPassword(password);
//
// TokenResponse tokenResponse = auth.getToken(tokenRequest);
// if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
// // keep the username, if authorized
// this.default_username = username;
// }
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
