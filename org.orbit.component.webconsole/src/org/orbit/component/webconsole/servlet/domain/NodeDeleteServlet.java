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

public class NodeDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 2116303378035252009L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		String machineId = request.getParameter("machineId");
		String platformId = request.getParameter("platformId");

		String[] ids = request.getParameterValues("id");

		String message = "";
		if (machineId == null || machineId.isEmpty()) {
			message = "'machineId' parameter is not set.";
		}
		if (platformId == null || platformId.isEmpty()) {
			message = "'platformId' parameter is not set.";
		}
		if (ids == null || ids.length == 0) {
			message = "'id' parameter is not set.";
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (machineId != null && platformId != null && ids != null) {
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainClient != null && machineId != null && platformId != null) {
				try {
					PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);
					if (platformConfig != null) {
						NodeControlClient nodeControlClient = ServletHelper.INSTANCE.getNodeControlClient(platformConfig);
						if (nodeControlClient != null) {
							for (String currId : ids) {
								boolean currSucceed = nodeControlClient.deleteNode(currId);
								if (currSucceed) {
									hasSucceed = true;
								} else {
									hasFailed = true;
								}
							}
						}
					}

				} catch (Exception e) {
					message = ServletHelper.INSTANCE.checkMessage(message);
					message += "Exception occurs: '" + e.getMessage() + "'.";
					e.printStackTrace();
				}
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}
		if (succeed) {
			if (ids != null && ids.length > 1) {
				message = "Nodes are deleted successfully.";
			} else {
				message = "Node is deleted successfully.";
			}
		} else {
			if (ids != null && ids.length > 1) {
				message = "Nodes are not deleted.";
			} else {
				message = "Node is not deleted.";
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}

// String id = request.getParameter("id");
// if (id == null || id.isEmpty()) {
// message = "'id' parameter is not set.";
// }
