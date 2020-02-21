package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class ConfigElementAttributeListServlet extends HttpServlet {

	private static final long serialVersionUID = -3578005943609891754L;

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
		String parentElementId = ServletUtil.getParameter(request, "parentElementId", "");
		String elementId = ServletUtil.getParameter(request, "elementId", "");

		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}
		if (elementId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'elementId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigRegistry configReg = null;
		List<IConfigElement> parentConfigElements = new ArrayList<IConfigElement>();
		IConfigElement configElement = null;
		Map<String, Object> attributes = null;

		if (!configRegistryId.isEmpty() && !elementId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				CFG cfg = CFG.getDefault(accessToken);
				if (!cfg.isOnline()) {
					message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

				} else {
					configReg = cfg.getConfigRegistryById(configRegistryId);
					if (configReg == null) {
						message = MessageHelper.INSTANCE.add(message, "Config registry is not found.");

					} else {
						configElement = configReg.getElement(elementId);

						if (configElement == null) {
							message = MessageHelper.INSTANCE.add(message, "Config element is not found.");

						} else {
							attributes = configElement.getAttributes();

							IConfigElement parentElement = configReg.getElement(parentElementId);
							while (parentElement != null) {
								parentConfigElements.add(0, parentElement);
								parentElement = configReg.getElement(parentElement.getParentElementId());
							}
						}
					}
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (attributes == null) {
			attributes = new HashMap<String, Object>();
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
		request.setAttribute("parentConfigElements", parentConfigElements);
		if (configElement != null) {
			request.setAttribute("configElement", configElement);
		}

		request.setAttribute("attributes", attributes);

		request.getRequestDispatcher(contextRoot + "/views/config_element_attributes.jsp").forward(request, response);
	}

}
