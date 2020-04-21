package org.orbit.component.webconsole.servlet.tier1;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.PlatformConstants;
import org.orbit.platform.sdk.util.ExtensionHelper;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MainLandingPage extends HttpServlet {

	private static final long serialVersionUID = -8798612634123701761L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mainContextRoot = getServletConfig().getInitParameter(WebConstants.MAIN_WEB_CONSOLE_CONTEXT_ROOT);
		String message = "";

		String reqMessage = ServletUtil.getParameter(request, "message", "");
		if (reqMessage != null && !reqMessage.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, reqMessage);
		}

		boolean isTokenValid = false;
		HttpSession session = request.getSession(false);
		if (session != null) {
			String username = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_USERNAME);
			String accessToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_ACCESS_TOKEN);
			String refreshToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_REFRESH_TOKEN);

			isTokenValid = ExtensionHelper.JWT.isTokenValid(PlatformConstants.TOKEN_PROVIDER__ORBIT, accessToken);
		}

		if (message != null && !message.isEmpty()) {
			request.setAttribute("message", message);
		}
		request.setAttribute("isTokenValid", isTokenValid);

		request.getRequestDispatcher(mainContextRoot + "/views/mainLanding.jsp").forward(request, response);
	}

}
