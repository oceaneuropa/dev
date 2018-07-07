package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.ServletHelper;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.origin.common.util.ServletUtil;

public class NodeAttributeListServlet extends HttpServlet {

	private static final long serialVersionUID = 2333552536533608770L;

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
		String id = ServletUtil.getParameter(request, "id", "");

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}
		if (machineId.isEmpty()) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "'machineId' parameter is not set.";
		}
		if (platformId.isEmpty()) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "'platformId' parameter is not set.";
		}
		if (id.isEmpty()) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "'id' parameter is not set.";
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		NodeInfo nodeInfo = null;
		IndexItem nodeIndexItem = null;
		if (!machineId.isEmpty() && !platformId.isEmpty() && !id.isEmpty()) {
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainClient != null && machineId != null && platformId != null) {
				try {
					machineConfig = domainClient.getMachineConfig(machineId);
					platformConfig = domainClient.getPlatformConfig(machineId, platformId);

					if (platformConfig != null) {
						NodeControlClient nodeControlClient = ServletHelper.INSTANCE.getNodeControlClient(platformConfig);

						if (nodeControlClient != null) {
							nodeInfo = nodeControlClient.getNode(id);
						}
					}

				} catch (Exception e) {
					message = ServletHelper.INSTANCE.checkMessage(message);
					message += "Exception occurs: '" + e.getMessage() + "'.";
					e.printStackTrace();
				}
			}

			if (nodeInfo != null) {
				IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
				if (indexService != null) {
					String nodeId = nodeInfo.getId();
					nodeIndexItem = NodeHelper.INSTANCE.getNodeIndexItem(indexService, platformId, nodeId);
				}
			}
		}

		if (nodeInfo == null) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "Node with id '" + id + "' is not found.";
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
		if (nodeInfo != null) {
			request.setAttribute("nodeInfo", nodeInfo);
		}
		if (nodeIndexItem != null) {
			request.setAttribute("nodeIndexItem", nodeIndexItem);
		}

		request.getRequestDispatcher(contextRoot + "/views/domain_node_attributes_v1.jsp").forward(request, response);
	}

}
