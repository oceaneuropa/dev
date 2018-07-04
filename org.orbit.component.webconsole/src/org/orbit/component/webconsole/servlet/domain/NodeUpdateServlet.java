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
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.ServletHelper;
import org.origin.common.util.ServletUtil;

public class NodeUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -5634345204413047819L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");

		String id = ServletUtil.getParameter(request, "id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String typeId = ServletUtil.getParameter(request, "typeId", "");

		String message = "";
		if (machineId == null || machineId.isEmpty()) {
			message = "'machineId' parameter is not set.";
		}
		if (platformId == null || platformId.isEmpty()) {
			message = "'platformId' parameter is not set.";
		}
		if (id == null || id.isEmpty()) {
			message = "'id' parameter is not set.";
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (machineId != null && platformId != null && id != null && !id.isEmpty()) {
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainClient != null && machineId != null && platformId != null) {
				try {
					PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);
					if (platformConfig != null) {
						NodeControlClient nodeControlClient = ServletHelper.INSTANCE.getNodeControlClient(platformConfig);
						if (nodeControlClient != null) {
							succeed = nodeControlClient.updateNode(id, name, typeId);
						}
					}
				} catch (Exception e) {
					message = ServletHelper.INSTANCE.checkMessage(message);
					message += "Exception occurs: '" + e.getMessage() + "'.";
					e.printStackTrace();
				}
			}
		}
		if (succeed) {
			message = "Node is created successfully.";
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}
