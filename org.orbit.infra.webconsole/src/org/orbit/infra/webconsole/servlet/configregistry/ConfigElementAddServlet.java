package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigElementAddServlet extends HttpServlet {

	private static final long serialVersionUID = 2685745070610190479L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String parentElementId = ServletUtil.getParameter(request, "parentElementId", "");
		String name = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (name.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement configElement = null;
		if (!configRegistryId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFG.getDefault(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					IConfigRegistry configReg = cfg.getConfigRegistryById(configRegistryId);
					if (configReg == null) {
						message = MessageHelper.INSTANCE.add(message, "Config registry is not found.");

					} else {
						Map<String, Object> attributes = new HashMap<String, Object>();

						if (parentElementId.isEmpty()) {
							// root element
							configElement = configReg.createRootConfigElement(name, attributes, true);

						} else {
							// non-root element
							configElement = configReg.createConfigElement(parentElementId, name, attributes, true);
						}
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}

			if (configElement == null) {
				message = MessageHelper.INSTANCE.add(message, "Config element is not created.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Config element is created.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		String url = contextRoot + "/admin/configelements?configRegistryId=" + configRegistryId;
		if (!parentElementId.isEmpty()) {
			url += "&parentElementId=" + parentElementId;
		}
		response.sendRedirect(url);
	}

}
