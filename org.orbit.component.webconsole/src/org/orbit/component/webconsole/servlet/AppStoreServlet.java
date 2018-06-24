package org.orbit.component.webconsole.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier2.appstore.AppQuery;
import org.orbit.component.api.tier2.appstore.AppStore;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class AppStoreServlet extends HttpServlet {

	private static final long serialVersionUID = 6485270417490539175L;

	private static final AppManifest[] EMPTY_APPS = new AppManifest[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);

		AppManifest[] appManifests = null;
		AppStore appStore = OrbitClients.getInstance().getAppStore(appStoreUrl);
		if (appStore != null) {
			try {
				AppQuery query = new AppQuery();
				appManifests = appStore.getApps(query);
			} catch (ClientException e) {
				e.printStackTrace();
			}
		}
		if (appManifests == null) {
			appManifests = EMPTY_APPS;
		}
		request.setAttribute("appManifests", appManifests);
		request.getRequestDispatcher(contextRoot + "/views/appstore_v1.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
