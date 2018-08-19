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
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.component.webconsole.util.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.util.ServletUtil;

public class NodeProgramActionServlet extends HttpServlet {

	private static final long serialVersionUID = -8360998387897347956L;

	public static String ACTION__INSTALL = "install";
	public static String ACTION__UNINSTALL = "uninstall";
	public static String ACTION__ACTIVATE = "activate";
	public static String ACTION__DEACTIVATE = "deactivate";
	public static String ACTION__START = "start";
	public static String ACTION__STOP = "stop";

	public static List<String> ACTIONS = new ArrayList<String>();

	static {
		ACTIONS.add(ACTION__INSTALL);
		ACTIONS.add(ACTION__UNINSTALL);
		ACTIONS.add(ACTION__ACTIVATE);
		ACTIONS.add(ACTION__DEACTIVATE);
		ACTIONS.add(ACTION__START);
		ACTIONS.add(ACTION__STOP);
	}

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String message = "";

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");
		String[] idVersions = ServletUtil.getParameterValues(request, "appId_appVersion", EMPTY_IDS);
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
		if (idVersions.length == 0) {
			message = MessageHelper.INSTANCE.add(message, "'appId_appVersion' parameter is not set.");
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
		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && ACTIONS.contains(action)) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				// Get platform client of the node
				PlatformClient nodePlatformClient = null;
				IndexItem nodeIndexItem = InfraClientsUtil.IndexItems.getIndexItem(indexServiceUrl, accessToken, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (nodeIndexItem != null) {
					nodePlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(nodeIndexItem);
				}

				// Instruct the node to self install the program
				if (nodePlatformClient != null) {
					for (int i = 0; i < idVersions.length; i++) {
						String currIdVersion = idVersions[i];
						int index = currIdVersion.lastIndexOf("|");
						String currAppId = currIdVersion.substring(0, index);
						String currAppVersion = currIdVersion.substring(index + 1);

						boolean currSucceed = false;

						if (ACTION__INSTALL.equals(action)) {
							currSucceed = nodePlatformClient.installProgram(currAppId, currAppVersion, true);

						} else if (ACTION__UNINSTALL.equals(action)) {
							currSucceed = nodePlatformClient.uninstallProgram(currAppId, currAppVersion, true);

						} else if (ACTION__ACTIVATE.equals(action)) {
							currSucceed = nodePlatformClient.activateProgram(currAppId, currAppVersion, true);

						} else if (ACTION__DEACTIVATE.equals(action)) {
							currSucceed = nodePlatformClient.deactivateProgram(currAppId, currAppVersion, true);

						} else if (ACTION__START.equals(action)) {
							currSucceed = nodePlatformClient.startProgram(currAppId, currAppVersion, true);

						} else if (ACTION__STOP.equals(action)) {
							currSucceed = nodePlatformClient.stopProgram(currAppId, currAppVersion, true);
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
		if (ACTION__INSTALL.equals(action)) {
			actionMessage = "installed";

		} else if (ACTION__UNINSTALL.equals(action)) {
			actionMessage = "uninstalled";

		} else if (ACTION__ACTIVATE.equals(action)) {
			actionMessage = "activated";

		} else if (ACTION__DEACTIVATE.equals(action)) {
			actionMessage = "deactivated";

		} else if (ACTION__START.equals(action)) {
			actionMessage = "started";

		} else if (ACTION__STOP.equals(action)) {
			actionMessage = "stopped";
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are being " + actionMessage + "." : "Program is being " + actionMessage + ".");
		} else {
			message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are not " + actionMessage + "." : "Program is not " + actionMessage + ".");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId);
	}

}
