package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.DefaultPlatformClientResolver;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.ServiceInfo;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeServicePropertyRemoveServlet extends HttpServlet {

	private static final long serialVersionUID = -2764270591353443995L;

	// private static String[] EMPTY_NAMES = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");
		String sidStr = ServletUtil.getParameter(request, "sid", "");
		int sid = -1;
		try {
			sid = Integer.parseInt(sidStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// String[] names = ServletUtil.getParameterValues(request, "name", EMPTY_NAMES);
		List<String> names = ServletUtil.getParameterValues(request, "name");

		// Validate parameters
		String message = "";
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (parentPlatformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}
		if (sidStr.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'sid' parameter is not set.");
		}
		if (sid == -1) {
			message = MessageHelper.INSTANCE.add(message, "sid is invalid.");
		}
		if (names.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "Properties are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		// boolean hasSucceed = false;
		// boolean hasFailed = false;

		ServiceInfo serviceInfo = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && !names.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
				serviceInfo = PlatformClientsUtil.INSTANCE.getService(platformClientResolver, parentPlatformId, nodeId, sid);

				if (serviceInfo != null) {
					// for (String currName : names) {
					// boolean currSucceed = PlatformClientsUtil.INSTANCE.removeServiceProperty(platformClientResolver, parentPlatformId, nodeId, sid,
					// currName);
					// if (currSucceed) {
					// hasSucceed = true;
					// } else {
					// hasFailed = true;
					// }
					// }
					succeed = PlatformClientsUtil.INSTANCE.removeServiceProperties(platformClientResolver, parentPlatformId, nodeId, sid, names);
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (serviceInfo == null) {
			message = MessageHelper.INSTANCE.add(message, "Service with sid '" + sid + "' is not found.");
		}

		// if (hasSucceed && !hasFailed) {
		// succeed = true;
		// }

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (names != null && names.size() > 1) ? "Service properties are removed successfully." : "Service removed is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (names != null && names.size() > 1) ? "Service properties are not removed." : "Service removed is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeserviceproperties?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId + "&sid=" + sid);
	}

}
