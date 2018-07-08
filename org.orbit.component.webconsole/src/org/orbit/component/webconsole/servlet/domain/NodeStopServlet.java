package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.DomainIndexItemHelper;
import org.orbit.component.webconsole.servlet.ServletHelper;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.api.Clients;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformConstants;
import org.origin.common.util.ServletUtil;

public class NodeStopServlet extends HttpServlet {

	private static final long serialVersionUID = 2116303378035252009L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String[] ids = ServletUtil.getParameterValues(request, "id", EMPTY_IDS);

		String message = "";
		if (machineId.isEmpty()) {
			message = "'machineId' parameter is not set.";
		}
		if (platformId.isEmpty()) {
			message = "'platformId' parameter is not set.";
		}
		if (ids.length == 0) {
			message = "'id' parameter is not set.";
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (!machineId.isEmpty() && !platformId.isEmpty() && ids.length > 0) {
			IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);

			if (domainClient != null && indexService != null && machineId != null && platformId != null) {
				try {
					PlatformConfig platformConfig = domainClient.getPlatformConfig(machineId, platformId);

					if (platformConfig != null) {
						Map<String, IndexItem> nodeIdToIndexItem = DomainIndexItemHelper.INSTANCE.getPlatformIdToIndexItem(indexService, platformId, PlatformConstants.PLATFORM_TYPE__NODE);

						for (String currNodeId : ids) {
							PlatformClient platformClient = null;

							IndexItem indexItem = nodeIdToIndexItem.get(currNodeId);
							if (indexItem != null) {
								String platformHostURL = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_HOST_URL);
								String platformContextRoot = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_CONTEXT_ROOT);
								if (platformHostURL != null && platformContextRoot != null) {
									String url = platformHostURL;
									if (!url.endsWith("/") && !platformContextRoot.startsWith("/")) {
										url += "/";
									}
									url += platformContextRoot;
									platformClient = Clients.getInstance().getPlatformClient(url);
								}
							}

							if (platformClient != null) {
								platformClient.shutdown(10 * 1000, false);
								hasSucceed = true;

							} else {
								hasFailed = true;
							}
						}
					}

				} catch (Exception e) {
					message = ServletHelper.INSTANCE.checkMessage(message);
					message += "Exception occurs: '" + e.getMessage() + "'.";
					e.printStackTrace();
				}
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}
		if (succeed) {
			if (ids != null && ids.length > 1) {
				message = "Nodes are stopped.";
			} else {
				message = "Node is stopped.";
			}
		} else {
			if (ids != null && ids.length > 1) {
				message = "Nodes are not stopped.";
			} else {
				message = "Node is not stopped.";
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodes?machineId=" + machineId + "&platformId=" + platformId);
	}

}

// NodeControlClient nodeControlClient = ServletHelper.INSTANCE.getNodeControlClient(platformConfig);
// if (nodeControlClient != null) {
// for (String currNodeId : ids) {
// boolean currSucceed = nodeControlClient.stopNode(currNodeId);
// if (currSucceed) {
// hasSucceed = true;
// } else {
// hasFailed = true;
// }
// }
// }
