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
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.model.tier3.domain.request.UpdatePlatformConfigRequest;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class DomainPlatformUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -251759560621225982L;

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
					PlatformConfig platformConfig = domainMgmt.getPlatformConfig(machineId, id);
					if (platformConfig == null) {
						message = "Platform configuration is not found.";

					} else {
						UpdatePlatformConfigRequest updatePlatformRequest = new UpdatePlatformConfigRequest();
						updatePlatformRequest.setPlatformId(id);
						updatePlatformRequest.setName(name);
						updatePlatformRequest.setHostURL(hostUrl);
						updatePlatformRequest.setContextRoot(theContextRoot);
						succeed = domainMgmt.updatPlatformConfig(machineId, updatePlatformRequest);
					}

				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (succeed) {
			message = "Platform is changed successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/platform?machineId=" + machineId);
	}

}
