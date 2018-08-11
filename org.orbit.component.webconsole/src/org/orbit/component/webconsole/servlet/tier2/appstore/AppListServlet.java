package org.orbit.component.webconsole.servlet.tier2.appstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

/**
 * https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
 *
 */
public class AppListServlet extends HttpServlet {

	private static final long serialVersionUID = -5332337437550713232L;

	private static final AppManifest[] EMPTY_APPS = new AppManifest[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		AppManifest[] appManifests = null;
		try {
			appManifests = OrbitComponentHelper.AppStore.getApps(appStoreUrl);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		if (appManifests == null) {
			appManifests = EMPTY_APPS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("appManifests", appManifests);
		request.getRequestDispatcher(contextRoot + "/views/appstore_apps_list_v1.jsp").forward(request, response);
	}

}
