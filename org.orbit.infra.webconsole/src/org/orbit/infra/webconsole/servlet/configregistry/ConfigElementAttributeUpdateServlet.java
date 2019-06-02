package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;

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

public class ConfigElementAttributeUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = -8183969582031118014L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String parentElementId = ServletUtil.getParameter(request, "parentElementId", "");
		String elementId = ServletUtil.getParameter(request, "elementId", "");

		String oldName = ServletUtil.getParameter(request, "oldName", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String value = ServletUtil.getParameter(request, "value", "");

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (elementId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'elementId' parameter is not set.");
		}
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!configRegistryId.isEmpty() && !elementId.isEmpty() && !name.isEmpty()) {
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
						IConfigElement configElement = configReg.getConfigElement(elementId);

						if (configElement == null) {
							message = MessageHelper.INSTANCE.add(message, "Config element is not found.");

						} else {
							succeed = configElement.setAttribute(oldName, name, value);
						}
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, "Attribute is updated.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Attribute is not updated.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/configelementattributes?configRegistryId=" + configRegistryId + "&parentElementId=" + parentElementId + "&elementId=" + elementId);
	}

}
