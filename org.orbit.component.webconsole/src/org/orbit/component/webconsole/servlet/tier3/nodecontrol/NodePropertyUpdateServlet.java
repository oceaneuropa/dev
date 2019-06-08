package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.OrbitClientHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodePropertyUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -3078734229049103631L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");

		String oldName = ServletUtil.getParameter(request, "oldName", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String value = ServletUtil.getParameter(request, "value", "");

		String message = "";
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;

		if (!machineId.isEmpty() && !platformId.isEmpty() && !nodeId.isEmpty() && !name.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(accessToken, platformId);
				succeed = ComponentClientsUtil.NodeControl.updateNodeAttribute(nodeControlClient, nodeId, oldName, name, value);

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Attribute is updated.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Attribute is not updated.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + nodeId);
	}

}
