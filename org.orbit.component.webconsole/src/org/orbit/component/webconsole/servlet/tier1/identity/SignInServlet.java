package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
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
		String publicContextRoot = getServletConfig().getInitParameter(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		String message = "";

		String username_or_email_value = ServletUtil.getParameter(request, "username_email", "");
		String password = ServletUtil.getParameter(request, "password", "");

		if (username_or_email_value.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "username or email is empty.");
		}
		if (password.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'password' parameter is empty.");
		}

		boolean signInSucceed = false;
		if (!username_or_email_value.isEmpty() && !password.isEmpty()) {
			try {
				// Get identity service's access token
				String identityServiceAccessToken = null;

				boolean usernameExists = ComponentClientsUtil.UserAccounts.usernameExists(userRegistryUrl, identityServiceAccessToken, username_or_email_value);
				boolean emailExists = ComponentClientsUtil.UserAccounts.emailExists(userRegistryUrl, identityServiceAccessToken, username_or_email_value);

				if (usernameExists || emailExists) {
					String username = usernameExists ? username_or_email_value : null;
					String email = emailExists ? username_or_email_value : null;

					LoginResponse loginResponse = ComponentClientsUtil.Identity.login(identityServiceUrl, username, email, password);
					if (loginResponse != null) {
						signInSucceed = loginResponse.isSucceed();
						if (signInSucceed) {
							// Login succeed
							try {
								OrbitTokenUtil.INSTANCE.updateSession(request, PlatformConstants.TOKEN_PROVIDER__ORBIT, loginResponse.getTokenType(), loginResponse.getTokenValue());

							} catch (Exception e) {
								// Cannot get token from sign in response. Consider as sign in failed.
								signInSucceed = true;
								message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
								e.printStackTrace();
							}
						} else {
							// Login failed
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

		if (!signInSucceed) {
			message = MessageHelper.INSTANCE.add(message, "Incorrect username or password.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		if (signInSucceed) {
			// Note:
			// Redirect to desktop servlet. Show main page for now.
			response.sendRedirect(contextRoot + "/user_main");
		} else {
			response.sendRedirect(publicContextRoot + "/signin");
		}
	}

}
