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
import org.orbit.infra.io.configregistry.CFGFactory;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ConfigRegistryPropertyListServlet extends HttpServlet {

	private static final long serialVersionUID = 1636771773886184262L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String configRegistryId = ServletUtil.getParameter(request, "configRegistryId", "");

		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigRegistry configReg = null;
		Map<String, Object> properties = null;

		if (!configRegistryId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFGFactory.INSTANCE.createCFG(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					configReg = cfg.getConfigRegistryById(configRegistryId);
					if (configReg == null) {
						message = MessageHelper.INSTANCE.add(message, "Config registry is not found.");

					} else {
						properties = configReg.getProperties();
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		if (configReg != null) {
			request.setAttribute("configReg", configReg);
		}
		request.setAttribute("properties", properties);

		request.getRequestDispatcher(contextRoot + "/views/config_reg_properties.jsp").forward(request, response);
	}

}
