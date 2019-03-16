package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodesProgramBatchInstallServlet extends HttpServlet {

	private static final long serialVersionUID = -4355755821088040889L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String message = "";

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String[] nodeIds = ServletUtil.getParameterValues(request, "nodeId", EMPTY_IDS);
		String[] idVersions = ServletUtil.getParameterValues(request, "appId_appVersion", EMPTY_IDS);

		String cleanOption = ServletUtil.getParameter(request, "-clean", "");

		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeIds.length <= 0) {
			// message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Nodes are not selected.");
		}
		if (idVersions.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'appId_appVersion' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Apps are not selected.");
		}

		boolean autoStartNode = true;

		if (!machineId.isEmpty() && !platformId.isEmpty() && nodeIds.length > 0 && idVersions.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				Map<String, Object> options = new HashMap<String, Object>();
				if ("true".equalsIgnoreCase(cleanOption)) {
					options.put("-clean", true);
				}

				NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(accessToken, platformId);

				for (String nodeId : nodeIds) {
					NodeInfo nodeInfo = nodeControlClient.getNode(nodeId);
					if (nodeInfo == null) {
						message = MessageHelper.INSTANCE.add(message, "Cannot find node. nodeId='" + nodeId + "'.");
						continue;
					}

					IndexItem nodeIndexItem = InfraClientsHelper.INDEX_SERVICE.getIndexItem(accessToken, platformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
					if (autoStartNode) {
						boolean doStartNode = false;
						if (nodeIndexItem == null) {
							doStartNode = true;
						} else {
							boolean isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
							if (!isNodeOnline) {
								doStartNode = true;
							}
						}
						if (doStartNode) {
							// start current node, before installing apps on it.
							boolean isStarted = ComponentClientsUtil.NodeControl.startNode(nodeControlClient, nodeId, options);
							if (!isStarted) {
								message = MessageHelper.INSTANCE.add(message, "Cannot start node. nodeId='" + nodeId + "'.");
								continue;
							}

							long totalWaitingTime = 0;
							while (nodeIndexItem == null) {
								nodeIndexItem = InfraClientsHelper.INDEX_SERVICE.getIndexItem(accessToken, platformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
								try {
									Thread.sleep(1000);
								} catch (Exception e) {
									e.printStackTrace();
								}
								totalWaitingTime += 1000;
								if (totalWaitingTime > 10 * 1000) {
									break;
								}
							}

							if (nodeIndexItem != null) {
								totalWaitingTime = 0;
								boolean isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
								if (!isNodeOnline) {
									try {
										Thread.sleep(1000);
									} catch (Exception e) {
										e.printStackTrace();
									}
									totalWaitingTime += 1000;
									if (totalWaitingTime > 10 * 1000) {
										break;
									}
								}
							}
						}
					}

					if (nodeIndexItem == null) {
						continue;
					} else {
						boolean isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
						if (!isNodeOnline) {
							continue;
						}
					}

					PlatformClient nodePlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, nodeIndexItem);
					if (nodePlatformClient == null) {
						message = MessageHelper.INSTANCE.add(message, "Cannot find PlatformClient of node. nodeId='" + nodeId + "'.");
						continue;
					}

					boolean succeed = false;
					boolean hasSucceed = false;
					boolean hasFailed = false;

					// Instruct the node to self install the program
					for (int i = 0; i < idVersions.length; i++) {
						String currIdVersion = idVersions[i];
						int index = currIdVersion.lastIndexOf("|");
						String currAppId = currIdVersion.substring(0, index);
						String currAppVersion = currIdVersion.substring(index + 1);

						boolean currSucceed = nodePlatformClient.installProgram(currAppId, currAppVersion, true);
						if (currSucceed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}

					if (hasSucceed && !hasFailed) {
						succeed = true;
					}

					if (succeed) {
						String msg = (idVersions.length > 1) ? "Programs are installed to node." : "Program is installed to node.";
						message = MessageHelper.INSTANCE.add(message, msg + " nodeId='" + nodeId + "'.");
					} else {
						String msg = (idVersions.length > 1) ? "Programs are not installed to node." : "Program is not installed to node.";
						message = MessageHelper.INSTANCE.add(message, msg + " nodeId='" + nodeId + "'.");
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, e.getMessage());
				e.printStackTrace();
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}

// boolean isNodeOnline = nodeControlClient.ping();
// if (!isNodeOnline) {
// if (autoStartNode) {
// // start current node, before installing apps on it.
// boolean isStarted = ComponentClientsUtil.NodeControl.startNode(nodeControlClient, nodeId);
// if (!isStarted) {
// message = MessageHelper.INSTANCE.add(message, "Cannot start node. nodeId='" + nodeId + "'.");
// continue;
// }
//
// } else {
// // ignore installing apps on stopped node.
// continue;
// }
// }
