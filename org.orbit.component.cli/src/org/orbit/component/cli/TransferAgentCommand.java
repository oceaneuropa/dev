package org.orbit.component.cli;

import java.util.Hashtable;

import javax.ws.rs.core.Response;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.Requests;
import org.orbit.component.api.tier3.domain.DomainService;
import org.orbit.component.api.tier3.domain.DomainServiceConnector;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
import org.orbit.component.model.tier3.transferagent.dto.NodeInfo;
import org.orbit.component.model.tier3.transferagent.dto.TransferAgentResponseConverter;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferAgentCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentCommand.class);

	protected static String[] NODESPACE_TITLES = new String[] { "Name" };
	protected static String[] NODE_TITLES = new String[] { "Name" };

	protected String scheme = "ta";

	@Dependency
	protected DomainServiceConnector domainConnector;

	protected String getScheme() {
		return this.scheme;
	}

	public void start(final BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function", new String[] { //
				"ping", //
				"echo", "level", //
				"list_nodes", "list_nodes2", "get_node", "node_exist", "create_node", "delete_node", "start_node", "stop_node", "node_status"//
		});

		OSGiServiceUtil.register(bundleContext, TransferAgentCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	public void stop(final BundleContext bundleContext) {
		OSGiServiceUtil.unregister(TransferAgentCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);
	}

	@DependencyFullfilled
	public void connectorsSet() {
		LOG.info("connectorsSet()");
	}

	@DependencyUnfullfilled
	public void connectorsUnset() {
		LOG.info("connectorsUnset()");
	}

	protected TransferAgent getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		TransferAgent transferAgent = null;
		DomainService domainService = this.domainConnector.getService();
		if (domainService != null) {
			TransferAgentConfig taConfig = domainService.getTransferAgentConfig(machineId, transferAgentId);
			if (taConfig != null) {
				String url = taConfig.getHostURL() + taConfig.getContextRoot();
				transferAgent = OrbitClients.getInstance().getTransferAgent(url);
			}
		}
		return transferAgent;
	}

	// -----------------------------------------------------------------------------------------
	// Service
	// ping
	// -----------------------------------------------------------------------------------------
	/**
	 * 
	 * @param machineId
	 * @param transferAgentId
	 */
	@Descriptor("ping")
	public void ping( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });
		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			boolean ping = transferAgent.ping();
			System.out.println("ping result = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 * @param message
	 */
	@Descriptor("echo")
	public void echo( //
			@Descriptor("TA server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Message") @Parameter(names = { "-m", "--m" }, absentValue = Parameter.UNSPECIFIED) String message //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "echo", new String[] { "url", url }, new String[] { "m", message });
		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'--url' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(message)) {
			LOG.error("'--m' parameter is not set.");
			return;
		}

		try {
			TransferAgent transferAgent = OrbitClients.getInstance().getTransferAgent(url);
			String echoMessage = transferAgent.echo(message);
			System.out.println("echo result = " + echoMessage);

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Descriptor("level")
	public void level( //
			@Descriptor("TA server URL") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url, //
			@Descriptor("Level 1") @Parameter(names = { "-l1", "-l1" }, absentValue = Parameter.UNSPECIFIED) String level1, //
			@Descriptor("Level 2") @Parameter(names = { "-l2", "-l2" }, absentValue = Parameter.UNSPECIFIED) String level2, //
			@Descriptor("Message 1") @Parameter(names = { "-m1", "--m1" }, absentValue = Parameter.UNSPECIFIED) String message1, //
			@Descriptor("Message 2") @Parameter(names = { "-m2", "--m2" }, absentValue = Parameter.UNSPECIFIED) String message2 //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "level", new String[] { "url", url }, new String[] { "l1", level1 }, new String[] { "l2", level2 }, new String[] { "m1", message1 }, new String[] { "m2", message2 });

		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'--url' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(level1)) {
			LOG.error("'--l1' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(level2)) {
			LOG.error("'--l2' parameter is not set.");
			return;
		}

		try {
			TransferAgent transferAgent = OrbitClients.getInstance().getTransferAgent(url);
			String levelResult = transferAgent.level(level1, level2, message1, message2);
			System.out.println("level result = " + levelResult);

		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	// -----------------------------------------------------------------------------------------
	// Nodes
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
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });
		if (Parameter.UNSPECIFIED.equals(machineId)) {
			LOG.error("'-machineId' parameter is not set.");
			return;
		}
		if (Parameter.UNSPECIFIED.equals(transferAgentId)) {
			LOG.error("'-transferAgentId' parameter is not set.");
			return;
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.LIST_NODES);
			Response response = transferAgent.sendRequest(request);

			NodeInfo[] nodeInfos = TransferAgentResponseConverter.INSTANCE.convertToNodeInfos(response);
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

	/**
	 * 
	 * @param url
	 *            e.g. http://127.0.0.1:12001/orbit/v1/ta
	 * 
	 *            e.g. http://127.0.0.1:13001/orbit/v1/ta
	 */
	@Descriptor("List nodes 2")
	public void list_nodes2(//
			@Descriptor("url") @Parameter(names = { "-url", "--url" }, absentValue = Parameter.UNSPECIFIED) String url //
	) {
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes2", new String[] { "url", url });

		if (Parameter.UNSPECIFIED.equals(url)) {
			LOG.error("'-url' parameter is not set.");
			return;
		}

		try {
			TransferAgent transferAgent = OrbitClients.getInstance().getTransferAgent(url);
			Request request = new Request(Requests.LIST_NODES);
			Response response = transferAgent.sendRequest(request);

			NodeInfo[] nodeInfos = TransferAgentResponseConverter.INSTANCE.convertToNodeInfos(response);
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
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("get_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(Requests.GET_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = transferAgent.sendRequest(request);

			NodeInfo nodeInfo = TransferAgentResponseConverter.INSTANCE.convertToNodeInfo(response);
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
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("node_exist()");
		CLIHelper.getInstance().printCommand(getScheme(), "node_exist", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(Requests.NODE_EXIST);
			request.setParameter("nodeId", nodeId);

			Response response = transferAgent.sendRequest(request);
			boolean exists = TransferAgentResponseConverter.INSTANCE.nodeExists(response);
			LOG.info("exists: " + exists);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create a node")
	public void create_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("create_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "create_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(Requests.CREATE_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = transferAgent.sendRequest(request);

			boolean succeed = TransferAgentResponseConverter.INSTANCE.createNodeSucceed(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete a node")
	public void delete_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("delete_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "delete_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(Requests.DELETE_NODE);
			request.setParameter("nodeId", nodeId);

			Response response = transferAgent.sendRequest(request);

			boolean succeed = TransferAgentResponseConverter.INSTANCE.deleteNodeSucceed(response);
			LOG.info("succeed: " + succeed);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Start a node")
	public void start_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("start_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "start_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Stop a node")
	public void stop_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("stop_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "stop_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Get the status of a node")
	public void node_status( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("node_status()");
		CLIHelper.getInstance().printCommand(getScheme(), "node_status", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(Requests.NODE_STATUS);
			request.setParameter("nodeId", nodeId);

			Response response = transferAgent.sendRequest(request);

			String status = TransferAgentResponseConverter.INSTANCE.getNodeStatus(response);
			LOG.info("status: " + status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// @Dependency
// protected AuthConnector authConnector;

// @Dependency
// protected TransferAgentConnector transferAgentConnector;
// protected ServiceConnectorAdapterV1<TransferAgent> transferAgentConnector;
// protected ServiceConnectorAdapterV2 transferAgentConnector;

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
