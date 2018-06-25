package org.orbit.component.webconsole.servlet.userregistry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.account.UpdateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserAccount;
import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class UserRegistryUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -7886418026610926872L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String userRegistryUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_USER_REGISTRY_URL);
		String message = "";

		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		if (id == null || id.isEmpty()) {
			message = "'id' parameter is not set.";
		}

		boolean succeed = false;
		if (id != null) {
			UserRegistry userRegistry = OrbitClients.getInstance().getUserRegistry(userRegistryUrl);
			if (userRegistry != null) {
				try {
					UserAccount userAccount = userRegistry.getUserAccount(id);
					if (userAccount == null) {
						message = "User is not found.";

					} else {
						UpdateUserAccountRequest updateUserAccountRequest = new UpdateUserAccountRequest();
						updateUserAccountRequest.setUserId(id);
						updateUserAccountRequest.setPassword(password);
						updateUserAccountRequest.setEmail(email);
						updateUserAccountRequest.setFirstName(firstName);
						updateUserAccountRequest.setLastName(lastName);
						updateUserAccountRequest.setPhone(phone);

						succeed = userRegistry.update(updateUserAccountRequest);
					}
				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (succeed) {
			message = "User account is changed successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/userregistry");
	}

}

// userAccount.getPassword();
// userAccount.getFirstName();
// userAccount.getLastName();
// userAccount.getEmail();
// userAccount.getPhone();
