package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.DefaultNodeControlClientResolver;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.component.webconsole.util.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.api.PlatformMetadata;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.util.ServletUtil;

public class NodeListServlet extends HttpServlet {

	private static final long serialVersionUID = 2782056562245686399L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		NodeInfo[] nodeInfos = null;
		Map<String, IndexItem> nodeIdToIndexItemMap = null;

		if (!machineId.isEmpty() && !platformId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = ComponentClientsUtil.DomainControl.getPlatformConfig(domainServiceUrl, accessToken, machineId, platformId);

				NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver(indexServiceUrl);
				nodeInfos = ComponentClientsUtil.NodeControl.getNodes(nodeControlClientResolver, accessToken, platformId);

				// Get index items for platforms with type "node" and parent platform id equals the platformId
				if (nodeInfos != null) {
					nodeIdToIndexItemMap = InfraClientsUtil.Indexes.getPlatformIdToIndexItem(indexServiceUrl, accessToken, platformId, PlatformConstants.PLATFORM_TYPE__NODE, PlatformConstants.PLATFORM_TYPE__SERVER);

					for (NodeInfo nodeInfo : nodeInfos) {
						String nodeId = nodeInfo.getId();
						boolean isOnline = false;
						String runtimeState = "";

						IndexItem indexItem = nodeIdToIndexItemMap.get(nodeId);

						if (indexItem != null) {
							isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);
							runtimeState = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_RUNTIME_STATE);

							if (isOnline) {
								try {
									PlatformClient nodePlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, indexItem);
									if (nodePlatformClient != null) {
										PlatformMetadata platformMetadata = nodePlatformClient.getMetadata();
										if (platformMetadata != null) {
											String jvmName = platformMetadata.getJvmName();
											String pid = platformMetadata.getPid();

											nodeInfo.getRuntimeProperties().put("jvm_name", jvmName);
											nodeInfo.getRuntimeProperties().put("pid", pid);
										}
									}
								} catch (Exception e) {
									// message = MessageHelper.INSTANCE.add(message, e.getMessage());
								}
							}
						}
						nodeInfo.getRuntimeStatus().setOnline(isOnline);
						nodeInfo.getRuntimeStatus().setRuntimeState(runtimeState);
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		if (machineConfig != null) {
			request.setAttribute("machineConfig", machineConfig);
		}
		if (platformConfig != null) {
			request.setAttribute("platformConfig", platformConfig);
		}
		if (nodeInfos != null) {
			request.setAttribute("nodeInfos", nodeInfos);
		}
		if (nodeIdToIndexItemMap != null) {
			request.setAttribute("nodeIdToIndexItemMap", nodeIdToIndexItemMap);
		}

		// String PID = ManagementFactory.getRuntimeMXBean().getName();
		// request.setAttribute("PID", PID);

		request.getRequestDispatcher(contextRoot + "/views/nodes_list.jsp").forward(request, response);
	}

}

// private static final NodeConfig[] EMPTY_NODE_CONFIGS = new NodeConfig[0];
// NodeConfig[] nodeConfigs = null;
// nodeConfigs = domainClient.getNodeConfigs(machineId, platformId);
// if (nodeConfigs == null) {
// nodeConfigs = EMPTY_NODE_CONFIGS;
// }
// request.setAttribute("nodeConfigs", nodeConfigs);

// NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(indexServiceUrl, accessToken, platformId);
