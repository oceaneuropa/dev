package org.orbit.component.webconsole.servlet.tier3.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class PlatformUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -251759560621225982L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String id = ServletUtil.getParameter(request, "id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String hostUrl = ServletUtil.getParameter(request, "hostUrl", "");
		String theContextRoot = ServletUtil.getParameter(request, "contextRoot", "");

		String message = "";
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (id.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (machineId != null && id != null) {
			try {
				PlatformConfig platformConfig = OrbitComponentHelper.Domain.getPlatformConfig(domainServiceUrl, machineId, id);
				if (platformConfig == null) {
					message = MessageHelper.INSTANCE.add(message, "Platform configuration is not found.");

				} else {
					succeed = OrbitComponentHelper.Domain.updatePlatformConfig(domainServiceUrl, machineId, id, name, hostUrl, theContextRoot);
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Platform is changed successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Platform is not changed.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/platforms?machineId=" + machineId);
	}

}
