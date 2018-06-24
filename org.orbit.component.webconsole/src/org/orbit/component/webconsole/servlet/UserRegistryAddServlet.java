package org.orbit.component.webconsole.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier1.account.CreateUserAccountRequest;
import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class UserRegistryAddServlet extends HttpServlet {

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
					CreateUserAccountRequest createUserAccountRequest = new CreateUserAccountRequest();
					createUserAccountRequest.setUserId(id);
					createUserAccountRequest.setPassword(password);
					createUserAccountRequest.setFirstName(firstName);
					createUserAccountRequest.setLastName(lastName);
					createUserAccountRequest.setEmail(email);
					createUserAccountRequest.setPhone(phone);
					succeed = userRegistry.register(createUserAccountRequest);

				} catch (ClientException e) {
					e.printStackTrace();
					message = e.getMessage();
				}
			}
		}
		if (succeed) {
			message = "User is added successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/userregistry");
	}

}

// request.setAttribute("message", message);
// request.getRequestDispatcher(contextRoot + "/userregistry").forward(request, response);
// response.sendRedirect(contextRoot + "/userregistry?message=" + message);
