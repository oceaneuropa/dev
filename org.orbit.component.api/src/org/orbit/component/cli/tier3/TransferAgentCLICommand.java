package org.orbit.component.cli.tier3;

import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.DomainManagement;
import org.orbit.component.api.tier3.domain.DomainManagementConnector;
import org.orbit.component.api.tier3.transferagent.NodespaceInfo;
import org.orbit.component.api.tier3.transferagent.TransferAgent;
import org.orbit.component.api.tier3.transferagent.TransferAgentHelper;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Response;
import org.origin.common.rest.model.Responses;
import org.origin.common.util.CLIHelper;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

public class TransferAgentCLICommand implements Annotated {

	protected static String[] NODESPACE_INFO_TITLES = new String[] { "Name" };

	protected BundleContext bundleContext;
	protected String scheme = "ta";

	@Dependency
	protected DomainManagementConnector domainMgmtConnector;

	protected boolean debug = true;

	/**
	 * 
	 * @param bundleContext
	 */
	public TransferAgentCLICommand(BundleContext bundleContext) {
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
						// service
						"ping",

						// Nodespaces
						"list_nodespaces", "list_nodespace", "nodespace_exist", "create_nodespace", "delete_nodespace", //

						// Nodes
						"list_nodes", "node_exist", "create_node", "delete_node", "is_node_started", "start_node", "stop_node",//
		});

		OSGiServiceUtil.register(this.bundleContext, TransferAgentCLICommand.class.getName(), this, props);
		OSGiServiceUtil.register(this.bundleContext, Annotated.class.getName(), this);
	}

	public void stop() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}

		OSGiServiceUtil.unregister(TransferAgentCLICommand.class.getName(), this);
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

	protected TransferAgent getTransferAgent(String machineId, String transferAgentId) throws ClientException {
		DomainManagement domainMgmt = this.domainMgmtConnector.getService();
		checkDomainManagement(domainMgmt);
		print(domainMgmt);

		TransferAgent transferAgent = getTransferAgent(domainMgmt, machineId, transferAgentId);
		checkTransferAgent(transferAgent);
		print(transferAgent);

		return transferAgent;
	}

	protected TransferAgent getTransferAgent(DomainManagement domainMgmt, String machineId, String transferAgentId) throws ClientException {
		return TransferAgentHelper.getInstance().getTransferAgent(domainMgmt, machineId, transferAgentId);
	}

	protected void checkConnector() throws ClientException {
		if (this.domainMgmtConnector == null) {
			System.err.println(getClass().getSimpleName() + ".checkConnector() DomainMgmtConnector is not available.");
			throw new ClientException(500, "DomainMgmtConnector is not available.");
		}
	}

	protected void checkDomainManagement(DomainManagement domainMgmt) throws ClientException {
		if (domainMgmt == null) {
			System.err.println(getClass().getSimpleName() + ".checkDomainManagement() domainMgmt is not available.");
			throw new ClientException(500, "domainMgmt is not available.");
		}
	}

	protected void checkTransferAgent(TransferAgent transferAgent) throws ClientException {
		if (transferAgent == null) {
			System.err.println(getClass().getSimpleName() + ".checkTransferAgent() transferAgent is not available.");
			throw new ClientException(500, "transferAgent is not available.");
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

	protected void print(TransferAgent transferAgent) {
		if (transferAgent == null) {
			System.out.println("transferAgent service is null.");
			return;
		} else {
			System.out.println(transferAgent.getName() + " (" + transferAgent.getURL() + ")");
		}
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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "ping", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_nodespaces", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(OrbitConstants.Requests.GET_NODESPACES);

			Responses responses = transferAgent.sendRequest(request);
			NodespaceInfo[] nodespaceInfoResponses = transferAgent.getResponseConverter().convertToNodespaceInfos(responses);
			String[][] rows = new String[nodespaceInfoResponses.length][NODESPACE_INFO_TITLES.length];
			int rowIndex = 0;
			for (NodespaceInfo nodespaceInfo : nodespaceInfoResponses) {
				String name = nodespaceInfo.getName();

				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(NODESPACE_INFO_TITLES, rows, nodespaceInfoResponses.length);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(OrbitConstants.Requests.GET_NODESPACE);
			request.setParameter("nodespace", nodespace);

			Responses responses = transferAgent.sendRequest(request);
			NodespaceInfo nodespaceInfoResponse = transferAgent.getResponseConverter().convertToNodespaceInfo(responses);

			NodespaceInfo[] nodespaceInfos = (nodespaceInfoResponse != null) ? new NodespaceInfo[] { nodespaceInfoResponse } : new NodespaceInfo[] {};
			String[][] rows = new String[nodespaceInfos.length][NODESPACE_INFO_TITLES.length];
			int rowIndex = 0;
			for (NodespaceInfo nodespaceInfo : nodespaceInfos) {
				String name = nodespaceInfo.getName();

				rows[rowIndex++] = new String[] { name };
			}
			PrettyPrinter.prettyPrint(NODESPACE_INFO_TITLES, rows, nodespaceInfos.length);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "nodespace_exist", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(OrbitConstants.Requests.NODESPACE_EXIST);
			request.setParameter("nodespace", nodespace);

			Responses responses = transferAgent.sendRequest(request);
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

	@Descriptor("Create a nodespace")
	public void create_nodespace( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "create_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(OrbitConstants.Requests.CREATE_NODESPACE);
			request.setParameter("nodespace", nodespace);

			Responses responses = transferAgent.sendRequest(request);
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

	@Descriptor("Delete a nodespace")
	public void delete_nodespace( //
			@Descriptor("Machine ID") @Parameter(names = { "-machineId", "--machineId" }, absentValue = Parameter.UNSPECIFIED) String machineId, //
			@Descriptor("Transfer Agent ID") @Parameter(names = { "-transferAgentId", "--transferAgentId" }, absentValue = Parameter.UNSPECIFIED) String transferAgentId, //
			@Descriptor("Nodespace") @Parameter(names = { "-nodespace", "--nodespace" }, absentValue = Parameter.UNSPECIFIED) String nodespace //
	) {
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "delete_nodespace", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

			Request request = new Request(OrbitConstants.Requests.DELETE_NODESPACE);
			request.setParameter("nodespace", nodespace);

			Responses responses = transferAgent.sendRequest(request);
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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "list_nodes", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "node_exist", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "create_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "delete_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "is_node_started", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "start_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

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
		if (debug) {
			CLIHelper.getInstance().printCommand(getScheme(), "stop_node", new String[] { "machineId", machineId }, new String[] { "transferAgentId", transferAgentId }, new String[] { "nodespace", nodespace }, new String[] { "nodeId", nodeId });
		}

		try {
			TransferAgent transferAgent = getTransferAgent(machineId, transferAgentId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

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
