package org.orbit.component.webconsole.servlet.tier2.appstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class AppDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -1743125235880795958L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String appStoreUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_APP_STORE_URL);

		String[] appIdVersions = ServletUtil.getParameterValues(request, "appId_appVersion", EMPTY_IDS);

		String message = "";
		if (appIdVersions.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'appIdVersions' parameter is not set.");
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
			for (int i = 0; i < appIdVersions.length; i++) {
				String currIdVersion = appIdVersions[i];
				int index = currIdVersion.lastIndexOf("|");
				String currId = currIdVersion.substring(0, index);
				String currVersion = currIdVersion.substring(index + 1);

				boolean currSucceed = ComponentClientsUtil.AppStore.deleteApp(accessToken, currId, currVersion);
				if (currSucceed) {
					hasSucceed = true;
				} else {
					hasFailed = true;
				}
			}
		} catch (ClientException e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (appIdVersions.length > 1) ? "Apps are deleted successfully." : "App is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (appIdVersions.length > 1) ? "Apps are not deleted." : "App is not deleted.");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/appstore/apps");
	}

}

// if (id == null || id.isEmpty()) {
// String id = request.getParameter("id");
