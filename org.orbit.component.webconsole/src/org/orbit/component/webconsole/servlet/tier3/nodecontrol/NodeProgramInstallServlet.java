package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

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
import org.orbit.infra.api.util.OrbitIndexHelper;
import org.orbit.platform.api.PlatformClient;
import org.origin.common.util.ServletUtil;

public class NodeProgramInstallServlet extends HttpServlet {

	private static final long serialVersionUID = 2040020865403867310L;

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

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				// Get platform client of the node
				PlatformClient nodePlatformClient = null;
				IndexItem nodeIndexItem = OrbitIndexHelper.INSTANCE.getIndexItem(indexServiceUrl, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
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

						boolean currSucceed = nodePlatformClient.installProgram(currAppId, currAppVersion, true);
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

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are being installed." : "Program is being installed.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are not installed." : "Program is not installed.");
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId);
	}

}
