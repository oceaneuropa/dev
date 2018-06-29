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
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.webconsole.WebConstants;

public class DomainMachineGetServlet extends HttpServlet {

	private static final long serialVersionUID = -7293566426088740431L;

	private static final MachineConfig[] EMPTY_MACHINE_CONFIGS = new MachineConfig[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

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
		DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
		if (domainMgmt != null) {
			try {
				machineConfigs = domainMgmt.getMachineConfigs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (machineConfigs == null) {
			machineConfigs = EMPTY_MACHINE_CONFIGS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("machineConfigs", machineConfigs);
		request.getRequestDispatcher(contextRoot + "/views/domain_machines_v1.jsp").forward(request, response);
	}

}

// @Override
// protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// doGet(request, response);
// }
