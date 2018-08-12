package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.platform.sdk.util.ExtensionHelper;
import org.origin.common.util.ServletUtil;

public class UserMainPage extends HttpServlet {

	private static final long serialVersionUID = 7108682550260146633L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String message = "";

		String reqMessage = ServletUtil.getParameter(request, "message", "");
		if (reqMessage != null && !reqMessage.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, reqMessage);
		}

		boolean isTokenValid = false;
		HttpSession session = request.getSession(false);
		if (session != null) {
			// String username = (String) session.getAttribute(WebConstants.SESSION__USERNAME);
			// String fullName = (String) session.getAttribute(WebConstants.SESSION__FULLNAME);
			// String tokenType = (String) session.getAttribute(WebConstants.SESSION__TOKEN_TYPE);
			String accessToken = (String) session.getAttribute(WebConstants.SESSION__ACCESS_TOKEN);

			if (accessToken != null) {
				isTokenValid = ExtensionHelper.JWT.isTokenValid(WebConstants.TOKEN_PROVIDER__ORBIT, accessToken);
			}
		}

		if (isTokenValid) {
			if (message != null && !message.isEmpty()) {
				request.setAttribute("message", message);
			}
			request.setAttribute("isTokenValid", isTokenValid);
			request.getRequestDispatcher(contextRoot + "/views/user_main.jsp").forward(request, response);

		} else {
			if (session == null) {
				session = request.getSession(true);
			}
			session.setAttribute("message", "Session expired.");

			response.sendRedirect(contextRoot + "/signin");
		}
	}

}
