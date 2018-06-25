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
import org.orbit.component.model.tier3.domain.request.AddPlatformConfigRequest;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class DomainPlatformAddServlet extends HttpServlet {

	private static final long serialVersionUID = 8803928159322859632L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);
		String message = "";

		String machineId = request.getParameter("machineId");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String hostUrl = request.getParameter("hostUrl");
		String theContextRoot = request.getParameter("contextRoot");

		if (machineId == null || machineId.isEmpty()) {
			message = "'machineId' parameter is not set.";
		}
		if (id == null || id.isEmpty()) {
			message = "'id' parameter is not set.";
		}

		boolean succeed = false;
		if (id != null) {
			DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainMgmt != null) {
				try {
					AddPlatformConfigRequest addPlatformRequest = new AddPlatformConfigRequest();
					addPlatformRequest.setPlatformId(id);
					addPlatformRequest.setName(name);
					addPlatformRequest.setHostURL(hostUrl);
					addPlatformRequest.setContextRoot(theContextRoot);

					succeed = domainMgmt.addPlatformConfig(machineId, addPlatformRequest);

				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (succeed) {
			message = "Platform is added successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/platform?machineId=" + machineId);
	}

}
