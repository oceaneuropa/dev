package org.orbit.component.cli.tier3.other;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentConnector;
import org.orbit.component.connector.tier3.transferagent.TransferAgentHelper;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.util.CLIHelper;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferAgentCommand implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentCommand.class);

	protected static String[] NODESPACE_TITLES = new String[] { "Name" };
	protected static String[] NODE_TITLES = new String[] { "Name" };

	protected BundleContext bundleContext;
	protected String scheme = "ta";

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;
	@Dependency
	protected TransferAgentConnector transferAgentConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public TransferAgentCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	protected String getScheme() {
		return this.scheme;
	}

	public void start() {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", getScheme());
		props.put("osgi.command.function",
				new String[] { //
						// service
						"ping",

						// Nodespaces
						"list_nodespaces", "list_nodespace", "nodespace_exist", "create_nodespace", "delete_nodespace", //

						// Nodes
						"list_nodes", "node_exist", "create_node", "delete_node", "is_node_started", "start_node", "stop_node",//
		});

		OSGiServiceUtil.register(this.bundleContext, TransferAgentCommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
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
		TransferAgent transferAgent = TransferAgentHelper.getInstance().getTransferAgent(this.domainMgmtConnector, this.transferAgentConnector, machineId, transferAgentId);
		if (transferAgent == null) {
			LOG.error("TransferAgent is not available.");
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
	@Descriptor("Ping TA")
	public void ping( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		LOG.info("ta_ping()");
		CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			boolean ping = transferAgent.ping();
			System.out.println("ping = " + ping);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------------
	// Nodespaces
	// list_nodespaces
	// list_nodespace
	// nodespace_exist
	// create_nodespace
	// delete_nodespace
	// -----------------------------------------------------------------------------------------
	@Descriptor("List nodespaces")
	public void list_nodespaces( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId //
	) {
		LOG.info("list_nodespaces()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodespaces", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.GET_NODESPACES);

			// Responses responses = transferAgent.sendRequest(request);
			// NodespaceInfo[] nodespaceInfoResponses = transferAgent.getResponseConverter().convertToNodespaceInfos(responses);
			// String[][] rows = new String[nodespaceInfoResponses.length][NODESPACE_TITLES.length];
			// int rowIndex = 0;
			// for (NodespaceInfo nodespaceInfo : nodespaceInfoResponses) {
			// String name = nodespaceInfo.getName();
			//
			// rows[rowIndex++] = new String[] { name };
			// }
			// PrettyPrinter.prettyPrint(NODESPACE_TITLES, rows, nodespaceInfoResponses.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List nodespace")
	public void list_nodespace( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		LOG.info("list_nodespace()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.GET_NODESPACE);
			request.setParameter("nodespace", nodespace);

			// Responses responses = transferAgent.sendRequest(request);
			// NodespaceInfo nodespaceInfoResponse = transferAgent.getResponseConverter().convertToNodespaceInfo(responses);
			//
			// NodespaceInfo[] nodespaceInfos = (nodespaceInfoResponse != null) ? new NodespaceInfo[] { nodespaceInfoResponse } : new NodespaceInfo[] {};
			// String[][] rows = new String[nodespaceInfos.length][NODESPACE_TITLES.length];
			// int rowIndex = 0;
			// for (NodespaceInfo nodespaceInfo : nodespaceInfos) {
			// String name = nodespaceInfo.getName();
			//
			// rows[rowIndex++] = new String[] { name };
			// }
			// PrettyPrinter.prettyPrint(NODESPACE_TITLES, rows, nodespaceInfos.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Check whether nodespace exists")
	public void nodespace_exist( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		LOG.info("list_nodespace()");
		CLIHelper.getInstance().printCommand(getScheme(), "nodespace_exist", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.NODESPACE_EXIST);
			request.setParameter("nodespace", nodespace);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create a nodespace")
	public void create_nodespace( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		LOG.info("create_nodespace()");
		CLIHelper.getInstance().printCommand(getScheme(), "create_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.CREATE_NODESPACE);
			request.setParameter("nodespace", nodespace);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete a nodespace")
	public void delete_nodespace( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		LOG.info("delete_nodespace()");
		CLIHelper.getInstance().printCommand(getScheme(), "delete_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.DELETE_NODESPACE);
			request.setParameter("nodespace", nodespace);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// -----------------------------------------------------------------------------------------
	// Nodes
	// list_nodes
	// node_exist
	// create_node
	// delete_node
	// is_node_started
	// start_node
	// stop_node
	// -----------------------------------------------------------------------------------------
	@Descriptor("List nodes")
	public void list_nodes( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		LOG.info("list_nodes()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.LIST_NODES);
			if (!Parameter.UNSPECIFIED.equals(nodespace)) {
				request.setParameter("nodespace", nodespace);
			}
			// Response response = transferAgent.sendRequest(request);
			//
			// NodeInfo[] nodeInfos = transferAgent.getResponseConverter().convertToNodeInfos(responses);
			//
			// String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			// int rowIndex = 0;
			// for (NodeInfo nodeInfo : nodeInfos) {
			// String name = nodeInfo.getName();
			// rows[rowIndex++] = new String[] { name };
			// }
			// PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List node")
	public void list_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("list_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "list_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.GET_NODE);
			request.setParameter("nodespace", nodespace);
			request.setParameter("node", nodeId);

			// Responses responses = transferAgent.sendRequest(request);
			// NodeInfo nodeInfo = transferAgent.getResponseConverter().convertToNodeInfo(responses);
			//
			// NodeInfo[] nodeInfos = (nodeInfo != null) ? new NodeInfo[] { nodeInfo } : new NodeInfo[] {};
			// String[][] rows = new String[nodeInfos.length][NODE_TITLES.length];
			// int rowIndex = 0;
			// for (NodeInfo currNodeInfo : nodeInfos) {
			// String name = currNodeInfo.getName();
			//
			// rows[rowIndex++] = new String[] { name };
			// }
			// PrettyPrinter.prettyPrint(NODE_TITLES, rows, nodeInfos.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Check whether node exists")
	public void node_exist( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("node_exist()");
		CLIHelper.getInstance().printCommand(getScheme(), "node_exist", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.NODE_EXIST);
			request.setParameter("nodespace", nodespace);
			request.setParameter("node", nodeId);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create a node")
	public void create_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("create_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "create_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.CREATE_NODE);
			request.setParameter("nodespace", nodespace);
			request.setParameter("node", nodeId);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete a node")
	public void delete_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("delete_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "delete_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(OrbitConstants.Requests.DELETE_NODE);
			request.setParameter("nodespace", nodespace);
			request.setParameter("node", nodeId);

			// Responses responses = transferAgent.sendRequest(request);
			// Response response = responses.getResponse(Response.class);
			// if (response != null) {
			// System.out.println(response.getSimpleLabel());
			//
			// if (response.getException() != null) {
			// response.getException().printStackTrace();
			// }
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Check whether a node is started")
	public void is_node_started( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("is_node_started()");
		CLIHelper.getInstance().printCommand(getScheme(), "is_node_started", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Start a node")
	public void start_node( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace, //
			@Descriptor("Node ID") @Parameter(names = { "-nodeId", "--nodeId" }, absentValue = Parameter.UNSPECIFIED) String nodeId //
	) {
		LOG.info("start_node()");
		CLIHelper.getInstance().printCommand(getScheme(), "start_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

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
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

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
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId,
// //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "is_nodespace_opened", new String[] { "machineId", machineId }, new String[] { "transferAgentId",
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
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String
// transferAgentId, //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "open_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId",
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
// @Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String
// transferAgentId, //
// @Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
// ) {
// if (debug) {
// CLIHelper.getInstance().printCommand(getScheme(), "close_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId",
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
