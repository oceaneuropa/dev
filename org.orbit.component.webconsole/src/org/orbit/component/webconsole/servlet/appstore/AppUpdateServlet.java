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

public class AppUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 8855949531603304588L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);
		String message = "";

		String idStr = ServletUtil.getParameter(request, "id", "");
		String appId = ServletUtil.getParameter(request, "appId", "");
		String appVersion = ServletUtil.getParameter(request, "appVersion", "");
		String type = ServletUtil.getParameter(request, "type", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String desc = ServletUtil.getParameter(request, "desc", "");
		String fileName = ServletUtil.getParameter(request, "fileName", "");

		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (id == -1) {
			message = "'id' parameter is valid.";
		}

		boolean succeed = false;

		if (!appId.isEmpty()) {
			try {
				succeed = OrbitComponentHelper.INSTANCE.updateApp(appStoreUrl, id, appId, appVersion, type, name, desc, fileName);

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = "App is updated successfully.";
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/appstore/apps");
	}

}
