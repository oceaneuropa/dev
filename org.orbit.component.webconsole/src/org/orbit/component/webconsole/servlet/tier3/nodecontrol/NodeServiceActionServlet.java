package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.DefaultPlatformClientResolver;
import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeServiceActionServlet extends HttpServlet {

	private static final long serialVersionUID = 2167417472005847191L;

	public static String ACTION__START = "start";
	public static String ACTION__STOP = "stop";

	public static List<String> ACTIONS = new ArrayList<String>();

	static {
		ACTIONS.add(ACTION__START);
		ACTIONS.add(ACTION__STOP);
	}

	private static String[] EMPTY_SIDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String message = "";

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");
		String[] sidArray = ServletUtil.getParameterValues(request, "sid", EMPTY_SIDS);
		String action = ServletUtil.getParameter(request, "action", "");

		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (parentPlatformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}
		if (sidArray.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "Services are not selected.");
		}
		if (action.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'action' parameter is not set.");
		}
		if (!ACTIONS.contains(action)) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is not supported. Supported actions: " + Arrays.toString(ACTIONS.toArray(new String[ACTIONS.size()])) + ".");
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && ACTIONS.contains(action) && sidArray.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
				PlatformClient nodePlatformClient = platformClientResolver.resolve(parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

				if (nodePlatformClient != null) {
					for (int i = 0; i < sidArray.length; i++) {
						String currSidStr = sidArray[i];

						int sid = -1;
						try {
							sid = Integer.parseInt(currSidStr);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (sid == -1) {
							continue;
						}

						boolean currSucceed = false;

						if (ACTION__START.equals(action)) {
							currSucceed = nodePlatformClient.startService(sid);

						} else if (ACTION__STOP.equals(action)) {
							currSucceed = nodePlatformClient.stopService(sid);
						}

						if (currSucceed) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
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
		if (ACTION__START.equals(action)) {
			actionMessage = "started";

		} else if (ACTION__STOP.equals(action)) {
			actionMessage = "stopped";
		}

		if (sidArray.length > 0) {
			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (sidArray.length > 1) ? "Services are " + actionMessage + "." : "Service is " + actionMessage + ".");
			} else {
				message = MessageHelper.INSTANCE.add(message, (sidArray.length > 1) ? "Services are not " + actionMessage + "." : "Service is not " + actionMessage + ".");
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeservices?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId);
	}

}
