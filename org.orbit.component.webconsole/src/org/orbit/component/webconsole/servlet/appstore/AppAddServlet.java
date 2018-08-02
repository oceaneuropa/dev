package org.orbit.component.webconsole.servlet.appstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class AppAddServlet extends HttpServlet {

	private static final long serialVersionUID = 3279968941691498949L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);
		String message = "";

		String id = ServletUtil.getParameter(request, "id", "");
		String version = ServletUtil.getParameter(request, "version", "");
		String type = ServletUtil.getParameter(request, "type", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String desc = ServletUtil.getParameter(request, "desc", "");
		String fileName = ServletUtil.getParameter(request, "fileName", "");

		if (id.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}
		if (version.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'version' parameter is not set.");
		}

		boolean succeed = false;
		if (!id.isEmpty()) {
			try {
				succeed = OrbitComponentHelper.INSTANCE.addApp(appStoreUrl, id, version, type, name, desc, fileName);

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = "App is added successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/appstore/apps");
	}

}
