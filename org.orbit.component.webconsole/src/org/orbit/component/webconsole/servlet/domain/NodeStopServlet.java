package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitComponentHelper;
import org.origin.common.util.ServletUtil;

public class NodeStopServlet extends HttpServlet {

	private static final long serialVersionUID = 2116303378035252009L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String[] nodeIds = ServletUtil.getParameterValues(request, "id", EMPTY_IDS);

		String message = "";
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (!machineId.isEmpty() && !platformId.isEmpty() && nodeIds.length > 0) {
			for (String currNodeId : nodeIds) {
				try {
					boolean currSucceed = OrbitComponentHelper.INSTANCE.stopNode(domainServiceUrl, machineId, platformId, currNodeId);
					if (currSucceed) {
						hasSucceed = true;
					} else {
						hasFailed = true;
					}
				} catch (Exception e) {
					message = MessageHelper.INSTANCE.add(message, e.getMessage());
					e.printStackTrace();
				}
			}
		}

		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (nodeIds.length > 1) ? "Nodes are stopped." : "Node is stopped.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (nodeIds.length > 1) ? "Nodes are not stopped." : "Node is not stopped.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}

// String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);

// NodeControlClient nodeControlClient = ServletHelper.INSTANCE.getNodeControlClient(platformConfig);
// if (nodeControlClient != null) {
// for (String currNodeId : ids) {
// boolean currSucceed = nodeControlClient.stopNode(currNodeId);
// if (currSucceed) {
// hasSucceed = true;
// } else {
// hasFailed = true;
// }
// }
// }

// boolean launchInstanceShutdown = true;
// boolean directShutdown = false;
// try {
// if (directShutdown) {
// PlatformClient nodePlatformClient = ClientHelper.INSTANCE.getNodePlatformClient(nodeIdToIndexItem, currNodeId);
// if (nodePlatformClient != null) {
// nodePlatformClient.shutdown(10 * 1000, false);
// hasSucceed = true;
// } else {
// hasFailed = true;
// }
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
