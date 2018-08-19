package org.orbit.component.webconsole.servlet.tier1.identity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;

public class SignOutServlet extends HttpServlet {

	private static final long serialVersionUID = 5932032936955558051L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String publicContextRoot = getServletConfig().getInitParameter(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.sendRedirect(publicContextRoot + "/signin");
	}

}
