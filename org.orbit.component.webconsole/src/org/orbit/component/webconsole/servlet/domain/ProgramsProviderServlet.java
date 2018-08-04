package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.origin.common.rest.client.ClientException;

public class ProgramsProviderServlet extends HttpServlet {

	private static final long serialVersionUID = 1790757328274893358L;

	private static final AppManifest[] EMPTY_APPS = new AppManifest[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String appStoreUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_APP_STORE_URL);

		AppManifest[] appManifests = null;
		try {
			appManifests = OrbitComponentHelper.INSTANCE.getApps(appStoreUrl);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		if (appManifests == null) {
			appManifests = EMPTY_APPS;
		}

		request.setAttribute("appManifests", appManifests);
		request.getRequestDispatcher(contextRoot + "/views/programs_provider.jsp").forward(request, response);
	}

}
