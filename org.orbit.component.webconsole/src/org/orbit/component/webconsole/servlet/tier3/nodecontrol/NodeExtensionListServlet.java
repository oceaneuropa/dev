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
import org.orbit.component.api.util.DomainUtil;
import org.orbit.component.api.util.NodeUtil;
import org.orbit.component.io.util.DefaultNodeControlClientResolver;
import org.orbit.component.io.util.DefaultPlatformClientResolver;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.api.ExtensionInfo;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeExtensionListServlet extends HttpServlet {

	private static final long serialVersionUID = -7911590298114595244L;

	private static final ExtensionInfo[] EMPTY_EXTENSIONS = new ExtensionInfo[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
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
		ExtensionInfo[] extensions = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = DomainUtil.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = DomainUtil.getPlatformConfig(domainServiceUrl, accessToken, machineId, parentPlatformId);

				NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver();
				nodeInfo = NodeUtil.getNode(nodeControlClientResolver, accessToken, parentPlatformId, nodeId);

				PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
				extensions = PlatformClientsUtil.INSTANCE.getExtensions(platformClientResolver, parentPlatformId, nodeId);

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (nodeInfo == null) {
			message = MessageHelper.INSTANCE.add(message, "Node with id '" + nodeId + "' is not found.");
		}
		if (extensions == null) {
			extensions = EMPTY_EXTENSIONS;
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
		request.setAttribute("extensions", extensions);

		request.getRequestDispatcher(contextRoot + "/views/node_extensions.jsp").forward(request, response);
	}

}

// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
