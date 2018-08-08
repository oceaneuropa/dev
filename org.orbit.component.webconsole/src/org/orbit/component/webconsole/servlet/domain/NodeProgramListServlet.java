package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.util.OrbitIndexHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.ProgramManifest;
import org.origin.common.util.ServletUtil;

public class NodeProgramListServlet extends HttpServlet {

	private static final long serialVersionUID = 2284649451336905223L;

	private static final ProgramManifest[] EMPTY_PROGRAMS = new ProgramManifest[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");

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
		if (parentPlatformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		NodeInfo nodeInfo = null;
		IndexItem nodeIndexItem = null;
		ProgramManifest[] programs = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				machineConfig = OrbitComponentHelper.INSTANCE.getMachineConfig(domainServiceUrl, machineId);
				platformConfig = OrbitComponentHelper.INSTANCE.getPlatformConfig(domainServiceUrl, machineId, parentPlatformId);

				NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(indexServiceUrl, parentPlatformId);
				nodeInfo = OrbitComponentHelper.INSTANCE.getNode(nodeControlClient, nodeId);

				nodeIndexItem = OrbitIndexHelper.INSTANCE.getIndexItem(indexServiceUrl, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (nodeIndexItem != null) {
					PlatformClient nodePlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(nodeIndexItem);
					if (nodePlatformClient != null) {
						programs = nodePlatformClient.getPrograms();
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (nodeInfo == null) {
			message = MessageHelper.INSTANCE.add(message, "Node with id '" + nodeId + "' is not found.");
		}
		if (programs == null) {
			programs = EMPTY_PROGRAMS;
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
		request.setAttribute("programs", programs);

		request.getRequestDispatcher(contextRoot + "/views/domain_node_programs_v1.jsp").forward(request, response);
	}

}
