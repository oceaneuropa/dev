package org.orbit.component.webconsole.servlet.tier3.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;

public class MachineListServlet extends HttpServlet {

	private static final long serialVersionUID = -7293566426088740431L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig[] machineConfigs = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			machineConfigs = ComponentClientsUtil.DomainControl.getMachineConfigs(domainServiceUrl, accessToken);

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		if (machineConfigs != null) {
			request.setAttribute("machineConfigs", machineConfigs);
		}

		request.getRequestDispatcher(contextRoot + "/views/domain_machines_list_v1.jsp").forward(request, response);
	}

}
