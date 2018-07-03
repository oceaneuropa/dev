package org.orbit.component.webconsole.servlet.useraccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.account.UserAccounts;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class UserAccountDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -7886418026610926872L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String userRegistryUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_USER_ACCOUNTS_URL);
		String message = "";

		// String id = request.getParameter("id");
		String[] ids = request.getParameterValues("id");

		// if (id == null || id.isEmpty()) {
		if (ids == null || ids.length == 0) {
			message = "'id' parameter is not set.";
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (ids != null) {
			UserAccounts userRegistry = OrbitClients.getInstance().getUserAccounts(userRegistryUrl);
			if (userRegistry != null) {
				try {
					for (String currId : ids) {
						boolean currSucceed = userRegistry.delete(currId);
						if (currSucceed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}
		if (succeed) {
			if (ids != null && ids.length > 1) {
				message = "Users are deleted successfully.";
			} else {
				message = "User is deleted successfully.";
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/useraccounts");
	}

}
