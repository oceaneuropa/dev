package org.orbit.component.webconsole.servlet.tier1.useraccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.identity.LoginResponse;
import org.orbit.component.api.util.IdentityServiceUtil;
import org.orbit.component.api.util.UserAccountUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.spirit.model.userprograms.UserPrograms;
import org.orbit.spirit.resource.util.UserProgramsHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class UserProgramsListServlet extends HttpServlet {

	private static final long serialVersionUID = 5227696907842754331L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String identityServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);

		// String dfsServiceUrl = getServletConfig().getInitParameter(SubstanceConstants.ORBIT_DFS_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String accountId = ServletUtil.getParameter(request, "accountId", "");
		if (accountId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'accountId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		String username = null;
		UserPrograms userPrograms = null;
		try {
			// Note:
			// Use admin user's access token to get the account information of other user with given accountId.
			// This is for testing only. In real world, admin user should never be allowed to access other people's data(programs, files, etc).
			String adminUserAccessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
			UserAccount userAccount = UserAccountUtil.getUserAccount(userRegistryUrl, adminUserAccessToken, accountId);

			if (userAccount != null) {
				username = userAccount.getUsername();
				String email = userAccount.getEmail();
				String password = userAccount.getPassword();
				LoginResponse loginResponse = IdentityServiceUtil.login(identityServiceUrl, username, email, password);

				if (loginResponse != null) {
					// @see org.orbit.component.webconsole.servlet.tier1.identity.SignInServlet
					boolean signInSucceed = loginResponse.isSucceed();
					if (signInSucceed) {
						String accessToken = loginResponse.getAccessToken();
						try {
							userPrograms = UserProgramsHelper.INSTANCE.getUserPrograms(accessToken);

						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, "Cannot getUserPrograms: '" + e.getMessage() + "'.");
						}
						if (userPrograms == null) {
							userPrograms = UserProgramsHelper.INSTANCE.createUserPrograms(accessToken);
						}
						if (userPrograms == null) {
							message = MessageHelper.INSTANCE.add(message, "UserPrograms is not found.");
						}
					} else {
						message = MessageHelper.INSTANCE.add(message, "User sign failed. Username: " + username + ", Email: " + email);
					}
				}
			} else {
				message = MessageHelper.INSTANCE.add(message, "UserAccount is not found.");
			}

		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();

		} catch (IOException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("accountId", accountId);
		if (username != null) {
			request.setAttribute("username", username);
		}
		if (userPrograms != null) {
			request.setAttribute("userPrograms", userPrograms);
		}

		request.getRequestDispatcher(contextRoot + "/views/user_programs.jsp").forward(request, response);
	}

}
