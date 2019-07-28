package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.io.util.DefaultPlatformClientResolver;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeProgramUninstallServlet extends HttpServlet {

	private static final long serialVersionUID = -4493226816635603686L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String message = "";

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");
		String[] idVersions = ServletUtil.getParameterValues(request, "id_version", EMPTY_IDS);

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
			// message = MessageHelper.INSTANCE.add(message, "'id_version' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Programs are not selected.");
		}

		boolean isNodeOnline = false;
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && idVersions.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IndexItem nodeIndexItem = InfraClientsUtil.INDEX_SERVICE.getIndexItem(accessToken, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (nodeIndexItem != null) {
					isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				}

				if (isNodeOnline) {
					PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
					PlatformClient nodePlatformClient = platformClientResolver.resolve(parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

					// Instruct the node to self uninstall the program
					if (nodePlatformClient != null) {
						for (int i = 0; i < idVersions.length; i++) {
							String currIdVersion = idVersions[i];
							int index = currIdVersion.lastIndexOf("_");
							String currAppId = currIdVersion.substring(0, index);
							String currAppVersion = currIdVersion.substring(index + 1);

							boolean currSucceed = nodePlatformClient.uninstallProgram(currAppId, currAppVersion, true);
							if (currSucceed) {
								hasSucceed = true;
							} else {
								hasFailed = true;
							}
						}
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Node '" + nodeId + "' is offline.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (idVersions.length > 0 && isNodeOnline) {
			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are uninstalled." : "Program is uninstalled.");
			} else {
				message = MessageHelper.INSTANCE.add(message, (idVersions.length > 1) ? "Programs are not uninstalled." : "Program is not uninstalled.");
			}
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeprograms?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId);
	}

}
