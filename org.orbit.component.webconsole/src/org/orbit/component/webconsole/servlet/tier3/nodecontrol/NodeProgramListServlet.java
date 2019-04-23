package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

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
import org.orbit.component.webconsole.util.DefaultPlatformClientResolver;
import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.ProgramManifest;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
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
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);

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
		ProgramManifest[] programs = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = ComponentClientsUtil.DomainControl.getPlatformConfig(domainServiceUrl, accessToken, machineId, parentPlatformId);

				NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver();
				nodeInfo = ComponentClientsUtil.NodeControl.getNode(nodeControlClientResolver, accessToken, parentPlatformId, nodeId);

				PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
				programs = PlatformClientsUtil.INSTANCE.getPrograms(platformClientResolver, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

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

		request.getRequestDispatcher(contextRoot + "/views/node_programs.jsp").forward(request, response);
	}

}
