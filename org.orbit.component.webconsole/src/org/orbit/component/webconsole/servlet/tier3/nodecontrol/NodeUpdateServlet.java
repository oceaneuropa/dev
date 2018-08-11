package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.tier3.nodecontrol.NodeControlClient;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.origin.common.util.ServletUtil;

public class NodeUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -5634345204413047819L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");

		String id = ServletUtil.getParameter(request, "id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String typeId = ServletUtil.getParameter(request, "typeId", "");

		String message = "";
		if (machineId.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (id.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!machineId.isEmpty() && !platformId.isEmpty() && !id.isEmpty() && !id.isEmpty()) {
			try {
				NodeControlClient nodeControlClient = OrbitClientHelper.INSTANCE.getNodeControlClient(indexServiceUrl, platformId);
				succeed = OrbitComponentHelper.NodeControl.updateNode(nodeControlClient, id, name, typeId);

			} catch (Exception e) {
				MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Node is created.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Node is not created.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}
