package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.NodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataCastNodeActionServlet extends HttpServlet {

	private static final long serialVersionUID = 1702575975605988511L;

	public static String ACTION__ENABLE = "enable";
	public static String ACTION__DISABLE = "disable";

	public static List<String> ACTIONS = new ArrayList<String>();

	static {
		ACTIONS.add(ACTION__ENABLE);
		ACTIONS.add(ACTION__DISABLE);
	}

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String[] elementIds = ServletUtil.getParameterValues(request, "elementId", EMPTY_IDS);
		String action = ServletUtil.getParameter(request, "action", "");

		String message = "";
		if (elementIds.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Data cast nodes are not selected.");
		}
		if (action.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'action' parameter is not set.");
		}
		if (!ACTIONS.contains(action)) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is not supported. Supported actions: " + Arrays.toString(ACTIONS.toArray(new String[ACTIONS.size()])) + ".");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (elementIds.length > 0 && ACTIONS.contains(action) && elementIds.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = NodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					for (int i = 0; i < elementIds.length; i++) {
						String elementId = elementIds[i];

						IConfigElement configElement = cfgReg.getConfigElement(elementId);
						if (configElement == null) {
							message = MessageHelper.INSTANCE.add(message, "Config element with elementId '" + elementId + "' cannot be found.");
							continue;
						}

						Map<String, Object> attributes = configElement.getAttributes();
						if (ACTION__ENABLE.equals(action)) {
							attributes.put("enabled", true);

						} else if (ACTION__DISABLE.equals(action)) {
							attributes.put("enabled", false);
						}

						boolean currSucceed = configElement.setAttributes(attributes);
						if (currSucceed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + NodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		String actionMessage = "";
		if (ACTION__ENABLE.equals(action)) {
			actionMessage = "enabled";
		} else if (ACTION__DISABLE.equals(action)) {
			actionMessage = "disabled";
		}

		if (elementIds.length > 0) {
			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Data cast nodes are " + actionMessage + "." : "Data cast node is " + actionMessage + ".");
			} else {
				message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Data cast nodes are not " + actionMessage + "." : "Data cast node is not " + actionMessage + ".");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/datacastlist");
	}

}
