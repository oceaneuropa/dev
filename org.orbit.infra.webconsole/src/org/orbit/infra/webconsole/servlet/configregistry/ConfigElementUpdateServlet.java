package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.CFGFactory;
import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigElementUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 6077045955124315741L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String parentElementId = ServletUtil.getParameter(request, "parentElementId", "");
		String elementId = ServletUtil.getParameter(request, "elementId", "");
		String name = ServletUtil.getParameter(request, "name", "");

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
		boolean hasNameChange = false;
		boolean isNameUpdated = false;
		if (!configRegistryId.isEmpty() && !elementId.isEmpty() && !name.isEmpty()) {
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
						IConfigElement configElement = configReg.getElement(elementId);
						if (configElement == null) {
							message = MessageHelper.INSTANCE.add(message, "Config element is not found.");

						} else {
							// Update name
							String oldName = configElement.getName();
							if (!name.equals(oldName)) {
								hasNameChange = true;
								isNameUpdated = configElement.rename(name);
								// if (isNameUpdated) {
								// message = MessageHelper.INSTANCE.add(message, "Config element name is updated.");
								// }
							}
						}
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}

			boolean succeed = false;
			boolean hasSucceed = false;
			boolean hasFailed = false;
			if ((hasNameChange && isNameUpdated)) {
				hasSucceed = true;
			}
			if ((hasNameChange && !isNameUpdated)) {
				hasFailed = true;
			}
			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, "Config element is updated.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Config element is not updated.");
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
