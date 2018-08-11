package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.platform.sdk.token.JWTTokenHandler;
import org.orbit.platform.sdk.util.JWTTokenHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class SignInServlet extends HttpServlet {

	private static final long serialVersionUID = 3614261826355225722L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_IDENTITY_SERVICE_URL);
		String message = "";

		String username_or_email_value = ServletUtil.getParameter(request, "username_email", "");
		String password = ServletUtil.getParameter(request, "password", "");

		if (username_or_email_value.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "username or email is empty.");
		}
		if (password.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'password' parameter is empty.");
		}

		boolean succeed = false;
		if (!username_or_email_value.isEmpty() && !password.isEmpty()) {
			try {
				boolean usernameExists = OrbitComponentHelper.Identity.usernameExists(identityServiceUrl, username_or_email_value);
				boolean emailExists = OrbitComponentHelper.Identity.emailExists(identityServiceUrl, username_or_email_value);

				if (usernameExists || emailExists) {
					String username = usernameExists ? username_or_email_value : null;
					String email = emailExists ? username_or_email_value : null;

					LoginResponse loginResponse = OrbitComponentHelper.Identity.login(identityServiceUrl, username, email, password);
					if (loginResponse != null) {
						succeed = loginResponse.isSucceed();

						if (succeed) {
							String tokenType = loginResponse.getTokenType();
							String tokenValue = loginResponse.getTokenValue();

							try {
								Map<String, String> payload = JWTTokenHelper.INSTANCE.getTokenPayload("orbit", tokenValue);
								if (payload != null) {
									String firstName = payload.get(JWTTokenHandler.PAYLOAD__FIRST_NAME);
									String lastName = payload.get(JWTTokenHandler.PAYLOAD__LAST_NAME);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							String responseMsg = loginResponse.getMessage();
							message = MessageHelper.INSTANCE.add(message, responseMsg);
						}
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			// Note:
			// - redirect to desktop servlet in the future
			// - show a jsp page for now
			request.getRequestDispatcher(contextRoot + "/views/user_main.jsp").forward(request, response);

		} else {
			message = MessageHelper.INSTANCE.add(message, "Incorrect username or password.");

			HttpSession session = request.getSession(true);
			session.setAttribute("message", message);

			response.sendRedirect(contextRoot + "/signin");
		}
	}

}
