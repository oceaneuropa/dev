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

public class NodePropertyDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -294271532237816404L;

	private static String[] EMPTY_NAMES = new String[] {};

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

		String[] names = ServletUtil.getParameterValues(request, "name", EMPTY_NAMES);

		String message = "";
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (id.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}
		if (names == null || names.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (!machineId.isEmpty() && !platformId.isEmpty() && !id.isEmpty() && names.length > 0) {
			try {
				for (String currName : names) {
					boolean currSucceed = OrbitComponentHelper.INSTANCE.deleteNodeAttribute(domainServiceUrl, machineId, platformId, id, currName);
					if (currSucceed) {
						hasSucceed = true;
					} else {
						hasFailed = true;
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (names != null && names.length > 1) ? "Attributes are deleted successfully." : "Attribute is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (names != null && names.length > 1) ? "Attributes are not deleted." : "Attribute is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeproperties?machineId=" + machineId + "&platformId=" + platformId + "&id=" + id);
	}

}
