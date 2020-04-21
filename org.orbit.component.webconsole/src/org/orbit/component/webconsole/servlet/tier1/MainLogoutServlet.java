package org.orbit.component.webconsole.servlet.tier1;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MainLogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -6610510356684380453L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mainContextRoot = getServletConfig().getInitParameter(WebConstants.MAIN_WEB_CONSOLE_CONTEXT_ROOT);

		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		response.sendRedirect(mainContextRoot + "/");
	}

}
