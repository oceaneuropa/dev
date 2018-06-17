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
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class DomainMachinesServlet extends HttpServlet {

	private static final long serialVersionUID = -7293566426088740431L;

	private static final MachineConfig[] EMPTY_MACHINE_CONFIGS = new MachineConfig[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		MachineConfig[] machineConfigs = null;
		DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
		if (domainMgmt != null) {
			try {
				machineConfigs = domainMgmt.getMachineConfigs();
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		if (machineConfigs == null) {
			machineConfigs = EMPTY_MACHINE_CONFIGS;
		}

		request.setAttribute("machineConfigs", machineConfigs);
		request.getRequestDispatcher(contextRoot + "/views/domain_v1.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
