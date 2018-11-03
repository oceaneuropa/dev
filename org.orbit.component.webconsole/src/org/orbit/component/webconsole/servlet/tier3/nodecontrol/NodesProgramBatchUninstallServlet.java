package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.OrbitClientHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodesProgramBatchUninstallServlet extends HttpServlet {

	private static final long serialVersionUID = -6405789062586870427L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String[] nodeIds = ServletUtil.getParameterValues(request, "nodeId", EMPTY_IDS);
		String[] idVersions = ServletUtil.getParameterValues(request, "appId_appVersion", EMPTY_IDS);

		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeIds.length <= 0) {
			// message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Nodes are not selected.");
		}
		if (idVersions.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'id_version' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Apps are not selected.");
		}

		if (!machineId.isEmpty() && !platformId.isEmpty() && nodeIds.length > 0 && idVersions.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				for (String nodeId : nodeIds) {
					IndexItem nodeIndexItem = InfraClientsHelper.INDEX_SERVICE.getIndexItem(indexServiceUrl, accessToken, platformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
					if (nodeIndexItem == null) {
						continue;
					}
					boolean isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
					if (!isNodeOnline) {
						continue;
					}

					PlatformClient nodePlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, nodeIndexItem);
					if (nodePlatformClient == null) {
						message = MessageHelper.INSTANCE.add(message, "Cannot find PlatformClient of node. nodeId='" + nodeId + "'.");
						continue;
					}

					boolean succeed = false;
					boolean hasSucceed = false;
					boolean hasFailed = false;

					// Instruct the node to self uninstall the program
					for (int i = 0; i < idVersions.length; i++) {
						String currIdVersion = idVersions[i];
						int index = currIdVersion.lastIndexOf("|");
						String currAppId = currIdVersion.substring(0, index);
						String currAppVersion = currIdVersion.substring(index + 1);

						if (nodePlatformClient.isProgramInstalled(currAppId, currAppVersion)) {
							boolean currSucceed = nodePlatformClient.uninstallProgram(currAppId, currAppVersion, true);
							if (currSucceed) {
								hasSucceed = true;
							} else {
								hasFailed = true;
							}
						}
					}
					if (hasSucceed && !hasFailed) {
						succeed = true;
					}

					if (succeed) {
						String msg = (idVersions.length > 1) ? "Programs are uninstalled from node." : "Program is installed from node.";
						message = MessageHelper.INSTANCE.add(message, msg + " nodeId='" + nodeId + "'.");
					} else {
						String msg = (idVersions.length > 1) ? "Programs are not uninstalled from node." : "Program is not uninstalled from node.";
						message = MessageHelper.INSTANCE.add(message, msg + " nodeId='" + nodeId + "'.");
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, e.getMessage());
				e.printStackTrace();
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}
