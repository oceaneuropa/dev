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

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MainPage extends HttpServlet {

	private static final long serialVersionUID = -8798612634123701761L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
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
		request.getRequestDispatcher(mainContextRoot + "/views/originMain.jsp").forward(request, response);
	}

}
