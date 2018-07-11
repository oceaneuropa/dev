package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class MachineUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -6229931528096900487L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String id = ServletUtil.getParameter(request, "id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String ip = ServletUtil.getParameter(request, "ip", "");

		String message = "";
		if (id.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		boolean succeed = false;

		if (!id.isEmpty()) {
			try {
				MachineConfig machineConfig = OrbitHelper.INSTANCE.getMachineConfig(domainServiceUrl, id);
				if (machineConfig == null) {
					message = MessageHelper.INSTANCE.add(message, "Machine configuration is not found.");

				} else {
					succeed = OrbitHelper.INSTANCE.updateMachineConfig(domainServiceUrl, id, name, ip);
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Machine is changed successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Machine is not changed.");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/machines");
	}

}
