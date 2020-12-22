package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.CFGFactory;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigRegistryPropertyDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -620914827662558509L;

	private static String[] EMPTY_NAMES = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String[] names = ServletUtil.getParameterValues(request, "name", EMPTY_NAMES);

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (names == null || names.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Properties are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;

		if (!configRegistryId.isEmpty() && names.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFGFactory.getInstance().createCFG(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					IConfigRegistry configReg = cfg.getConfigRegistryById(configRegistryId);
					if (configReg == null) {
						message = MessageHelper.INSTANCE.add(message, "Config registry is not found.");

					} else {
						succeed = configReg.removeProperties(Arrays.asList(names));
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (names != null && names.length > 1) ? "Properties are deleted." : "Property is deleted.");
			} else {
				message = MessageHelper.INSTANCE.add(message, (names != null && names.length > 1) ? "Properties are not deleted." : "Property is not deleted.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/configregproperties?configRegistryId=" + configRegistryId);
	}

}
