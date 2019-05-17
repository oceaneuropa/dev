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

public class ConfigRegistryListServlet extends HttpServlet {

	private static final long serialVersionUID = -9065868048939095644L;

	protected static IConfigElement[] EMPTY_CONFIG_ELEMENTS = new IConfigElement[] {};
	protected static IConfigRegistry[] EMPTY_CONFIG_REGS = new IConfigRegistry[] {};

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

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigRegistry[] configRegs = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			CFG cfg = CFG.getDefault(accessToken);
			if (!cfg.isOnline()) {
				message = MessageHelper.INSTANCE.add(message, "Config registry is not online.");
			} else {
				configRegs = cfg.getConfigRegistries();
			}
		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (configRegs == null) {
			configRegs = EMPTY_CONFIG_REGS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("configRegs", configRegs);

		request.getRequestDispatcher(contextRoot + "/views/config_regs.jsp").forward(request, response);
	}

}
