package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitComponentHelper;
import org.orbit.component.webconsole.servlet.OrbitInfraHelper;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.api.PlatformConstants;
import org.origin.common.util.ServletUtil;

public class NodeListServlet extends HttpServlet {

	private static final long serialVersionUID = 2782056562245686399L;

	private static final NodeInfo[] EMPTY_NODE_INFOS = new NodeInfo[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

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

		if (!machineId.isEmpty() && !platformId.isEmpty()) {
			try {
				machineConfig = OrbitComponentHelper.INSTANCE.getMachineConfig(domainServiceUrl, machineId);

				platformConfig = OrbitComponentHelper.INSTANCE.getPlatformConfig(domainServiceUrl, machineId, platformId);

				nodeInfos = OrbitComponentHelper.INSTANCE.getNodes(domainServiceUrl, machineId, platformId);

				// Get index items for platforms with type "node" and parent platform id equals the platformId
				if (nodeInfos != null) {
					Map<String, IndexItem> nodeIdToIndexItem = OrbitInfraHelper.INSTANCE.getPlatformIdToIndexItem(indexServiceUrl, platformId, PlatformConstants.PLATFORM_TYPE__NODE);
					for (NodeInfo nodeInfo : nodeInfos) {
						String nodeId = nodeInfo.getId();
						boolean isOnline = false;
						String runtimeState = "";
						IndexItem indexItem = nodeIdToIndexItem.get(nodeId);
						if (indexItem != null) {
							isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);
							runtimeState = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_RUNTIME_STATE);
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

		if (nodeInfos == null) {
			nodeInfos = EMPTY_NODE_INFOS;
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
		request.setAttribute("nodeInfos", nodeInfos);

		request.getRequestDispatcher(contextRoot + "/views/domain_nodes_v1.jsp").forward(request, response);
	}

}

// private static final NodeConfig[] EMPTY_NODE_CONFIGS = new NodeConfig[0];
// NodeConfig[] nodeConfigs = null;
// nodeConfigs = domainClient.getNodeConfigs(machineId, platformId);
// if (nodeConfigs == null) {
// nodeConfigs = EMPTY_NODE_CONFIGS;
// }
// request.setAttribute("nodeConfigs", nodeConfigs);
