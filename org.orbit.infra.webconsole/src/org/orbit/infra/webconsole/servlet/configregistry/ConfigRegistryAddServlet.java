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
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigRegistryAddServlet extends HttpServlet {

	private static final long serialVersionUID = -7406926914882947717L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);
		String type = ServletUtil.getParameter(request, "type", "");
		String name = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (type.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'type' parameter is not set.");
		}
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		if (!type.isEmpty() && !name.isEmpty()) {
			IConfigRegistry configReg = null;
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFG.getDefault(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					Map<String, Object> properties = new HashMap<String, Object>();
					configReg = cfg.createConfigRegistry(type, name, properties, true);
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}

			if (configReg == null) {
				message = MessageHelper.INSTANCE.add(message, "Config registry is not created.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Config registry is created.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/configregs");
	}

}
