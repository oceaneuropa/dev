package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class SignInPage extends HttpServlet {

	private static final long serialVersionUID = -3484560174896278200L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String message = "";

		HttpSession session = request.getSession(false);
		if (session != null) {
			String msg1 = (String) session.getAttribute("message");
			if (msg1 != null && !msg1.isEmpty()) {
				session.removeAttribute("message");
				message = MessageHelper.INSTANCE.add(message, msg1);
			}
		}

		String msg2 = ServletUtil.getParameter(request, "message", "");
		if (msg2 != null && !msg2.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, msg2);
		}

		if (message != null && !message.isEmpty()) {
			request.setAttribute("message", message);
		}
		request.getRequestDispatcher(contextRoot + "/views/sign_in.jsp").forward(request, response);
	}

}
