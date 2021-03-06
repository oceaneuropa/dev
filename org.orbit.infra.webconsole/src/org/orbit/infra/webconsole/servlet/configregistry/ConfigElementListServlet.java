package org.orbit.infra.webconsole.servlet.configregistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class ConfigElementListServlet extends HttpServlet {

	private static final long serialVersionUID = 5441827406099072948L;

	protected static IConfigElement[] EMPTY_CONFIG_ELEMENTS = new IConfigElement[] {};

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

		if (configRegistryId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'configRegistryId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigRegistry configReg = null;
		List<IConfigElement> parentConfigElements = new ArrayList<IConfigElement>();
		IConfigElement[] configElements = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			CFG cfg = CFGFactory.getInstance().createCFG(accessToken);
			if (!cfg.isOnline()) {
				message = MessageHelper.INSTANCE.add(message, "Config registry service is not online.");

			} else {
				configReg = cfg.getConfigRegistryById(configRegistryId);
				if (configReg == null) {
					message = MessageHelper.INSTANCE.add(message, "Config registry is not found.");

				} else {
					if (parentElementId.isEmpty()) {
						configElements = configReg.listRootElements();

					} else {
						configElements = configReg.listElements(parentElementId);

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
		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
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

		request.setAttribute("configElements", configElements);

		request.getRequestDispatcher(contextRoot + "/views/config_elements.jsp").forward(request, response);
	}

}

// if (configElements != null) {
// for (IConfigElement configElement : configElements) {
// IConfigElement[] childrenConfigElements = configElements = configReg.listConfigElements(configElement.getElementId());
// int childrenNum = (childrenConfigElements != null) ? childrenConfigElements.length : 0;
// configElement.setTransientProperty("childrenNum", childrenNum);
// }
// }
