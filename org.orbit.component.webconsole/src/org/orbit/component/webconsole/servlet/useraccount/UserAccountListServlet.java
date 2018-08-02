package org.orbit.component.webconsole.servlet.useraccount;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class UserAccountListServlet extends HttpServlet {

	private static final long serialVersionUID = 5360399058993136274L;

	private static final UserAccount[] EMPTY_USER_ACCOUNTS = new UserAccount[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String userRegistryUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_USER_ACCOUNTS_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		UserAccount[] userAccounts = null;
		UserAccountClient userRegistry = OrbitClients.getInstance().getUserAccounts(userRegistryUrl);
		if (userRegistry != null) {
			try {
				userAccounts = userRegistry.getUserAccounts();
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		if (userAccounts == null) {
			userAccounts = EMPTY_USER_ACCOUNTS;
		}

		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("userAccounts", userAccounts);
		request.getRequestDispatcher(contextRoot + "/views/user_accounts_v1.jsp").forward(request, response);
	}

}

// <script src="<%=contextRoot + "/views/js/userregistry.js"%>"></script>
