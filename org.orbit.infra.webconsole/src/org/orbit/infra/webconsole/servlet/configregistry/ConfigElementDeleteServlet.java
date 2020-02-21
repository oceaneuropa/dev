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

public class ConfigElementDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 6085363423588871612L;

	private static String[] EMPTY_ELEMENT_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");
		String parentElementId = ServletUtil.getParameter(request, "parentElementId", "");
		String[] elementIds = ServletUtil.getParameterValues(request, "elementId", EMPTY_ELEMENT_IDS);

		String message = "";
		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (elementIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Config elements are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (!configRegistryId.isEmpty() && elementIds.length > 0) {
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
						for (String currElementId : elementIds) {
							try {
								boolean currSucceed = configReg.deleteElement(currElementId);
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
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}

			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Config elements are deleted." : "Config element is deleted.");
			} else {
				message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Config elements are not deleted." : "Config element is not deleted.");
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
