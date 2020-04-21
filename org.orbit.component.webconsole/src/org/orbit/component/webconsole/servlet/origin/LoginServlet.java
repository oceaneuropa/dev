package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.IdentityServiceUtil;
import org.orbit.component.api.util.UserAccountUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -1526198364210932098L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);

		String message = "";
		String username = ServletUtil.getParameter(request, "username", "");
		String password = ServletUtil.getParameter(request, "password", "");

		if (username.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "username is empty.");
		} else if (password.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "password is empty.");
		}

		boolean loginSucceed = false;
		if (!username.isEmpty() && !password.isEmpty()) {
			try {
				// Get identity service's access token
				String serviceAccessToken = null;

				boolean usernameExists = UserAccountUtil.usernameExists(userRegistryUrl, serviceAccessToken, username);
				// boolean emailExists = UserAccountUtil.emailExists(userRegistryUrl, identityServiceAccessToken, username);

				if (usernameExists) {
					LoginResponse loginResponse = IdentityServiceUtil.login(identityServiceUrl, username, null, password);
					if (loginResponse != null) {
						loginSucceed = loginResponse.isSucceed();
						if (loginSucceed) {
							// Login succeed
							try {
								OrbitTokenUtil.INSTANCE.updateSession(request, PlatformConstants.TOKEN_PROVIDER__ORBIT, loginResponse.getTokenType(), loginResponse.getAccessToken(), loginResponse.getRefreshToken());

							} catch (Exception e) {
								// Cannot get token from login response. Consider as sign in failed.
								loginSucceed = true;
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

			if (!loginSucceed) {
				message = MessageHelper.INSTANCE.add(message, "Incorrect username or password.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		if (loginSucceed) {
			// to main landing page
			response.sendRedirect(mainContextRoot + "/home");
		} else {
			// back to main page
			response.sendRedirect(mainContextRoot + "/");
		}
	}

}
