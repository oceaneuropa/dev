package org.orbit.component.webconsole.servlet.tier1.registry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.api.tier1.configregistry.EPath;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class RegistryGetServlet extends HttpServlet {

	private static final long serialVersionUID = -2730455408815112857L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String registryUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_REGISTRY_URL);

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
		String userId = null;
		ConfigRegistryClient registry = OrbitClients.getInstance().getConfigRegistry(registryUrl);
		if (registry != null) {
			try {
				registry.getProperties();
				registry.getProperties(userId, new EPath("/"));
				registry.getProperty(userId, new EPath("/"), "test");
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}

		request.getRequestDispatcher(contextRoot + "/views/user_accounts_v1.jsp").forward(request, response);
	}

}
