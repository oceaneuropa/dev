package org.orbit.component.cli.other;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.IndexConstants;
import org.orbit.component.api.Requests;
import org.orbit.component.api.tier3.domain.DomainManagementClient;
import org.orbit.component.api.tier3.domain.other.DomainServiceConnector;
import org.orbit.component.api.tier3.nodecontrol.NodeManagementClient;
import org.orbit.component.api.tier3.nodecontrol.other.TransferAgentConnector;
import org.orbit.component.model.tier3.domain.dto.TransferAgentConfig;
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

public class TransferAgentCommandV1 implements Annotated {

	protected static Logger LOG = LoggerFactory.getLogger(TransferAgentCommandV1.class);

	protected static String[] NODESPACE_TITLES = new String[] { "Name" };
	protected static String[] NODE_TITLES = new String[] { "Name" };

	protected BundleContext bundleContext;
	protected String scheme = "ta";

	@Dependency
	protected DomainServiceConnector domainMgmtConnector;
	@Dependency
	protected TransferAgentConnector transferAgentConnector;

	/**
	 * 
	 * @param bundleContext
	 */
	public TransferAgentCommandV1(BundleContext bundleContext) {
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

		OSGiServiceUtil.register(this.bundleContext, TransferAgentCommandV1.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		OSGiServiceUtil.unregister(TransferAgentCommandV1.class.getName(), this);
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

	protected NodeManagementClient getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		NodeManagementClient transferAgent = getTransferAgent(this.domainMgmtConnector, this.transferAgentConnector, machineId, transferAgentId);
		if (transferAgent == null) {
			LOG.error("TransferAgent is not available.");
		}
		return transferAgent;
	}

	public NodeManagementClient getTransferAgent(DomainServiceConnector domainConnector, TransferAgentConnector taConnector, String machineId, String transferAgentId) throws ClientException {
		DomainManagementClient domain = domainConnector.getService();
		if (domain != null) {
			TransferAgentConfig taConfig = domain.getTransferAgentConfig(machineId, transferAgentId);
			if (taConfig != null) {
				Map<String, Object> properties = new HashMap<String, Object>();
				properties.put(IndexConstants.TRANSFER_AGENT_MACHINE_ID, taConfig.getMachineId());
				properties.put(IndexConstants.TRANSFER_AGENT_TRANSFER_AGENT_ID, taConfig.getId());
				properties.put(IndexConstants.TRANSFER_AGENT_NAME, taConfig.getName());
				properties.put(IndexConstants.TRANSFER_AGENT_HOST_URL, taConfig.getHostURL());
				properties.put(IndexConstants.TRANSFER_AGENT_CONTEXT_ROOT, taConfig.getContextRoot());
				properties.put(IndexConstants.TRANSFER_AGENT_HOME, taConfig.getHome());

				NodeManagementClient transferAgent = taConnector.getService(properties);
				return transferAgent;
			}
		}
		return null;
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.GET_NODESPACES);

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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.GET_NODESPACE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.NODESPACE_EXIST);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.CREATE_NODESPACE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.DELETE_NODESPACE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.GET_NODES);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.GET_NODE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.NODE_EXIST);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.CREATE_NODE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

			Request request = new Request(Requests.DELETE_NODE);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
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
			NodeManagementClient transferAgent = getTransferAgent(machineId, transferAgentId);
			if (transferAgent == null) {
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
