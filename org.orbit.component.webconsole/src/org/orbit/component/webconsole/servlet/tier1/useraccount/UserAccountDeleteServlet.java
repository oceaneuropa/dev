package org.orbit.component.webconsole.servlet.tier1.useraccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class UserAccountDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -7886418026610926872L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String userRegistryUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		String message = "";

		String[] accountIds = ServletUtil.getParameterValues(request, "id", EMPTY_IDS);

		if (accountIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (accountIds != null) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
				for (String currAccountId : accountIds) {
					boolean currSucceed = ComponentClientsUtil.UserAccounts.deleteUserAccount(userRegistryUrl, accessToken, currAccountId);
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
			message = MessageHelper.INSTANCE.add(message, (accountIds != null && accountIds.length > 1) ? "Users are deleted." : "User is deleted.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (accountIds != null && accountIds.length > 1) ? "Users are not deleted." : "User is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/useraccounts");
	}

}
