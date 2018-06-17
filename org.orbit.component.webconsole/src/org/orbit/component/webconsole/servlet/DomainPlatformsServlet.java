package org.orbit.component.webconsole.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class DomainPlatformsServlet extends HttpServlet {

	private static final long serialVersionUID = -7293566426088740431L;

	private static final PlatformConfig[] EMPTY_PLATFORM_CONFIGS = new PlatformConfig[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = request.getParameter("machineId");

		MachineConfig machineConfig = null;
		PlatformConfig[] platformConfigs = null;
		DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
		if (domainMgmt != null && machineId != null) {
			try {
				machineConfig = domainMgmt.getMachineConfig(machineId);
				platformConfigs = domainMgmt.getPlatformConfigs(machineId);
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		if (platformConfigs == null) {
			platformConfigs = EMPTY_PLATFORM_CONFIGS;
		}

		if (machineConfig != null) {
			request.setAttribute("machineConfig", machineConfig);
		}
		request.setAttribute("platformConfigs", platformConfigs);
		request.getRequestDispatcher(contextRoot + "/views/domain_platforms_v1.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
