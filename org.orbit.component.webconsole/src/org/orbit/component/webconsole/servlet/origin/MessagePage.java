package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class MessagePage extends HttpServlet {

	private static final long serialVersionUID = -746012511132710966L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";
		String redirectURL = "";

		String param1 = ServletUtil.getParameter(request, "message", null);
		if (param1 != null) {
			message = param1;
		}
		String param2 = ServletUtil.getParameter(request, "redirectURL", null);
		if (param2 != null) {
			redirectURL = param2;
		}

		HttpSession session = request.getSession(false);
		if (session != null) {
			Object value1 = session.getAttribute("message");
			if (value1 instanceof String) {
				session.removeAttribute("message");
				message = MessageHelper.INSTANCE.add(message, (String) value1);
			}

			Object value2 = session.getAttribute("redirectURL");
			if (value2 instanceof String) {
				session.removeAttribute("redirectURL");
				redirectURL = (String) value2;
			}
		}

		request.setAttribute("message", message);
		request.setAttribute("redirectURL", redirectURL);

		request.getRequestDispatcher(originContextRoot + "/views/originMessage.jsp").forward(request, response);
	}

}
