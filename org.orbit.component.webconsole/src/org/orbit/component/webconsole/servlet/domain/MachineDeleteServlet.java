package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.component.webconsole.servlet.OrbitComponentHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class MachineDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 3763741024191509965L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String[] ids = ServletUtil.getParameterValues(request, "id", EMPTY_IDS);

		String message = "";
		if (ids.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (ids != null) {
			try {
				for (String currId : ids) {
					boolean currSucceed = OrbitComponentHelper.INSTANCE.removeMachineConfig(domainServiceUrl, currId);
					if (currSucceed) {
						hasSucceed = true;
					} else {
						hasFailed = true;
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (ids.length > 1) ? "Machines are deleted successfully." : "Machine is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (ids.length > 1) ? "Machines are not deleted." : "Machine is not deleted.");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/machines");
	}

}

// if (id == null || id.isEmpty()) {
// String id = request.getParameter("id");
