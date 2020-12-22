package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.CFGFactory;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigRegistryDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 2449128321244942084L;

	private static String[] EMPTY_CONFIG_REGISTRY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String[] configRegistryIds = ServletUtil.getParameterValues(request, "configRegistryId", EMPTY_CONFIG_REGISTRY_IDS);

		String message = "";
		if (configRegistryIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Config registries are not selected.");
		}

		boolean isMultipleSelection = (configRegistryIds.length > 1) ? true : false;

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		if (configRegistryIds.length > 0) {
			boolean succeed = false;
			boolean hasSucceed = false;
			boolean hasFailed = false;
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFGFactory.INSTANCE.createCFG(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					for (String configRegistryId : configRegistryIds) {
						try {
							boolean currSucceed = cfg.deleteConfigRegistryById(configRegistryId);
							if (currSucceed) {
								hasSucceed = true;
							} else {
								hasFailed = true;
							}
						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, e.getMessage());
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, isMultipleSelection ? "Config registries are deleted." : "Config registry is deleted.");
			} else {
				message = MessageHelper.INSTANCE.add(message, isMultipleSelection ? "Config registries are not deleted." : "Config registry is not deleted.");
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
