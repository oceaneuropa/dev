package org.orbit.component.webconsole.servlet.tier1.useraccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.util.UserAccountUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class UserAccountUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -7886418026610926872L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		String message = "";

		String accountId = ServletUtil.getParameter(request, "id", "");
		String username = ServletUtil.getParameter(request, "username", "");
		String email = ServletUtil.getParameter(request, "email", "");
		String password = ServletUtil.getParameter(request, "password", "");
		String firstName = ServletUtil.getParameter(request, "firstName", "");
		String lastName = ServletUtil.getParameter(request, "lastName", "");
		String phone = ServletUtil.getParameter(request, "phone", "");

		if (accountId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is empty.");
		}
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
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
				succeed = UserAccountUtil.updateUserAccount(userRegistryUrl, accessToken, accountId, username, email, password, firstName, lastName, phone);

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "User account is changed.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "User account is not changed.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/useraccounts");
	}

}

// boolean exists = ComponentClientsUtil.UserAccounts.usernameExists(userRegistryUrl, accessToken, username);
// if (!exists) {
// message = MessageHelper.INSTANCE.add(message, "User '" + username + "' is not found.");
// } else {
// succeed = ComponentClientsUtil.UserAccounts.updateUserAccount(userRegistryUrl, accessToken, accountId, username, email, password, firstName,
// lastName, phone);
// }
