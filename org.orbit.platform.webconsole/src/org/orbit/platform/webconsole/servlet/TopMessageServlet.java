package org.orbit.platform.webconsole.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.platform.webconsole.WebConstants;

public class TopMessageServlet extends HttpServlet {

	private static final long serialVersionUID = -565136186468471770L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);

		// String message = null;
		// HttpSession session = request.getSession(false);
		// if (session != null) {
		// message = (String) session.getAttribute("message");
		// if (message != null) {
		// session.removeAttribute("message");
		// }
		// }
		// if (message != null) {
		// request.setAttribute("message", message);
		// }

		request.getRequestDispatcher(contextRoot + "/views/top_message.jsp").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
