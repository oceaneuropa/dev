package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.identity.RegisterResponse;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = -2584771402151923746L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_IDENTITY_SERVICE_URL);
		String message = "";

		String username = ServletUtil.getParameter(request, "username", "");
		String email = ServletUtil.getParameter(request, "email", "");
		String password = ServletUtil.getParameter(request, "password", "");

		if (username.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'username' parameter is empty.");
		}
		if (email.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'email' parameter is empty.");
		}
		if (password.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'password' parameter is empty.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
			try {
				boolean usernameExists = OrbitComponentHelper.Identity.usernameExists(identityServiceUrl, username);
				boolean emailExists = OrbitComponentHelper.Identity.emailExists(identityServiceUrl, email);

				if (usernameExists) {
					message = MessageHelper.INSTANCE.add(message, "username '" + username + "' is already taken.");
				}
				if (emailExists) {
					message = MessageHelper.INSTANCE.add(message, "email '" + email + "' is already taken.");
				}

				if (!usernameExists && !emailExists) {
					RegisterResponse registerResponse = OrbitComponentHelper.Identity.register(identityServiceUrl, username, email, password);
					if (registerResponse != null) {
						succeed = registerResponse.isSucceed();
						String responseMessage = registerResponse.getMessage();

						message = MessageHelper.INSTANCE.add(message, responseMessage);
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (succeed) {
			response.sendRedirect(contextRoot + "/signin");

		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute("message", message);

			response.sendRedirect(contextRoot + "/signup");
		}
	}

}
