package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.tier1.identity.RegisterResponse;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = -2584771402151923746L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String publicContextRoot = getServletConfig().getInitParameter(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);
		String componentContextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
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
		boolean signUpSucceed = false;
		if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
			try {
				// Get identity service's access token
				String identityServiceAccessToken = null;

				boolean usernameExists = ComponentClientsUtil.UserAccounts.usernameExists(userRegistryUrl, identityServiceAccessToken, username);
				boolean emailExists = ComponentClientsUtil.UserAccounts.emailExists(userRegistryUrl, identityServiceAccessToken, email);

				if (usernameExists) {
					message = MessageHelper.INSTANCE.add(message, "username '" + username + "' is already taken.");
				}
				if (emailExists) {
					message = MessageHelper.INSTANCE.add(message, "email '" + email + "' is already taken.");
				}

				if (!usernameExists && !emailExists) {
					RegisterResponse registerResponse = ComponentClientsUtil.Identity.register(identityServiceUrl, username, email, password);
					if (registerResponse != null) {
						signUpSucceed = registerResponse.isSucceed();
						String responseMessage = registerResponse.getMessage();

						message = MessageHelper.INSTANCE.add(message, responseMessage);
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		boolean signInSucceed = false;
		if (signUpSucceed) {
			try {
				LoginResponse loginResponse = ComponentClientsUtil.Identity.login(identityServiceUrl, username, email, password);
				if (loginResponse != null) {
					signInSucceed = loginResponse.isSucceed();
					if (signInSucceed) {
						// Sign in succeed
						try {
							OrbitTokenUtil.INSTANCE.updateSession(request, PlatformConstants.TOKEN_PROVIDER__ORBIT, loginResponse.getTokenType(), loginResponse.getTokenValue());

						} catch (Exception e) {
							// Cannot get token from sign in response. Consider as sign in failed.
							signInSucceed = true;
							message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
							e.printStackTrace();
						}
					} else {
						// Sign in failed
						String responseMsg = loginResponse.getMessage();
						message = MessageHelper.INSTANCE.add(message, responseMsg);
					}
				}
			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (!signInSucceed) {
			message = MessageHelper.INSTANCE.add(message, "Incorrect username or password.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		if (signUpSucceed && signInSucceed) {
			response.sendRedirect(componentContextRoot + "/user_main");
		} else {
			response.sendRedirect(publicContextRoot + "/signup");
		}
	}

}
