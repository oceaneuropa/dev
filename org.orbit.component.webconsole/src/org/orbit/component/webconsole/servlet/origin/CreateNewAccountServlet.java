package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.identity.CreateNewAccountResponse;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.IdentityServiceUtil;
import org.orbit.component.api.util.UserAccountUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class CreateNewAccountServlet extends HttpServlet {

	private static final long serialVersionUID = 3715555154356311555L;

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
		String email = ServletUtil.getParameter(request, "email", "");
		String password = ServletUtil.getParameter(request, "password", "");

		if (username.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "username is empty.");
		} else if (email.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "email is empty.");
		} else if (password.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "password is empty.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean createNewAccountSucceed = false;
		boolean loginSucceed = false;

		if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
			try {
				// Get identity service's access token
				String identityServiceAccessToken = null;

				boolean usernameExists = UserAccountUtil.usernameExists(userRegistryUrl, identityServiceAccessToken, username);
				// boolean emailExists = UserAccountUtil.emailExists(userRegistryUrl, identityServiceAccessToken, email);

				if (usernameExists) {
					message = MessageHelper.INSTANCE.add(message, "username '" + username + "' is already taken.");
				}
				// if (emailExists) {
				// message = MessageHelper.INSTANCE.add(message, "email '" + email + "' is already taken.");
				// }

				if (!usernameExists) {
					CreateNewAccountResponse createNewAccountResponse = IdentityServiceUtil.createNewAccount(identityServiceUrl, username, email, password);
					if (createNewAccountResponse != null) {
						createNewAccountSucceed = createNewAccountResponse.isSucceed();
						String responseMessage = createNewAccountResponse.getMessage();

						message = MessageHelper.INSTANCE.add(message, responseMessage);
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (createNewAccountSucceed) {
				try {
					LoginResponse loginResponse = IdentityServiceUtil.login(identityServiceUrl, username, email, password);
					if (loginResponse != null && loginResponse.isSucceed()) {
						loginSucceed = true;
						try {
							OrbitTokenUtil.INSTANCE.updateSession(request, PlatformConstants.TOKEN_PROVIDER__ORBIT, loginResponse.getTokenType(), loginResponse.getAccessToken(), loginResponse.getRefreshToken());

						} catch (Exception e) {
							loginSucceed = false;
							message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
							e.printStackTrace();
						}
					} else {
						// failed
						String responseMsg = loginResponse.getMessage();
						message = MessageHelper.INSTANCE.add(message, responseMsg);
					}
				} catch (ClientException e) {
					message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
					e.printStackTrace();
				}

				if (!loginSucceed) {
					message = MessageHelper.INSTANCE.add(message, "Incorrect username or password.");
				}
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		if (createNewAccountSucceed && loginSucceed) {
			response.sendRedirect(mainContextRoot + "/home");
		} else {
			response.sendRedirect(mainContextRoot + "/createNewAccountPage");
		}
	}

}
