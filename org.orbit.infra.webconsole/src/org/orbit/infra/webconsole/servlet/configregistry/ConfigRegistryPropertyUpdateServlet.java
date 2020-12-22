package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;

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

public class ConfigRegistryPropertyUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -8187972592535713420L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");

		String oldName = ServletUtil.getParameter(request, "oldName", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String value = ServletUtil.getParameter(request, "value", "");

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!configRegistryId.isEmpty() && !name.isEmpty()) {
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
						succeed = configReg.setProperty(oldName, name, value);
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, "Property is updated.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Property is not updated.");
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
