package org.orbit.component.webconsole.servlet.tier3.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.util.DomainUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class MachineUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -6229931528096900487L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		String message = "";

		String id = ServletUtil.getParameter(request, "id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String ip = ServletUtil.getParameter(request, "ip", "");

		if (id.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!id.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				MachineConfig machineConfig = DomainUtil.getMachineConfig(domainServiceUrl, accessToken, id);
				if (machineConfig == null) {
					message = MessageHelper.INSTANCE.add(message, "Machine configuration is not found.");

				} else {
					succeed = DomainUtil.updateMachineConfig(domainServiceUrl, accessToken, id, name, ip);
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Machine is changed.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Machine is not changed.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/machines");
	}

}
