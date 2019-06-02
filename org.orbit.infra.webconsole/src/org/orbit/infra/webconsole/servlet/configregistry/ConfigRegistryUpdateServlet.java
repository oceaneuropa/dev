package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;

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

public class ConfigRegistryUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 6077045955124315741L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String type = ServletUtil.getParameter(request, "type", "");
		String name = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (type.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'type' parameter is not set.");
		}
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'name' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		if (!configRegistryId.isEmpty() && !type.isEmpty() && !name.isEmpty()) {
			boolean hasTypeChange = false;
			boolean isTypeUpdated = false;
			boolean hasNameChange = false;
			boolean isNameUpdated = false;
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
						String oldType = configReg.getType();
						if (!type.equals(oldType)) {
							hasTypeChange = true;
							isTypeUpdated = configReg.updatedType(type);
						}

						String oldName = configReg.getName();
						if (!name.equals(oldName)) {
							hasNameChange = true;
							isNameUpdated = configReg.rename(name);
						}
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}

			boolean succeed = false;
			boolean hasSucceed = false;
			boolean hasFailed = false;

			if (hasTypeChange) {
				if (isTypeUpdated) {
					hasSucceed = true;
				} else {
					hasFailed = true;
				}
			}
			if (hasNameChange) {
				if (isNameUpdated) {
					hasSucceed = true;
				} else {
					hasFailed = true;
				}
			}

			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, "Config registry is updated.");
			} else {
				message = MessageHelper.INSTANCE.add(message, "Config registry is not updated.");
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
