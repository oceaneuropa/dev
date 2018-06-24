package org.orbit.component.webconsole.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class DomainMachineDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 3763741024191509965L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		String message = "";

		// String id = request.getParameter("id");
		String[] ids = request.getParameterValues("id");

		// if (id == null || id.isEmpty()) {
		if (ids == null || ids.length == 0) {
			message = "'id' parameter is not set.";
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (ids != null) {
			DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainMgmt != null) {
				try {
					for (String currId : ids) {
						boolean currSucceed = domainMgmt.removeMachineConfig(currId);
						if (currSucceed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}
		if (succeed) {
			if (ids != null && ids.length > 1) {
				message = "Machines are deleted successfully.";
			} else {
				message = "Machine is deleted successfully.";
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain");
	}

}
