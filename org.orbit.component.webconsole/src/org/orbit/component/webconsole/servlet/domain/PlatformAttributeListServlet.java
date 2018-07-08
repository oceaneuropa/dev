package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.DomainManagementClient;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.DomainIndexItemHelper;
import org.orbit.component.webconsole.servlet.ServletHelper;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.origin.common.util.ServletUtil;

public class PlatformAttributeListServlet extends HttpServlet {

	private static final long serialVersionUID = 2333552536533608770L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String id = ServletUtil.getParameter(request, "id", "");

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}
		if (machineId.isEmpty()) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "'machineId' parameter is not set.";
		}
		if (id.isEmpty()) {
			message = ServletHelper.INSTANCE.checkMessage(message);
			message += "'id' parameter is not set.";
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		IndexItem platformIndexItem = null;

		if (!machineId.isEmpty() && !id.isEmpty()) {
			DomainManagementClient domainClient = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainClient != null) {
				try {
					machineConfig = domainClient.getMachineConfig(machineId);
					platformConfig = domainClient.getPlatformConfig(machineId, id);

				} catch (Exception e) {
					message = ServletHelper.INSTANCE.checkMessage(message);
					message += "Exception occurs: '" + e.getMessage() + "'.";
					e.printStackTrace();
				}
			}

			if (platformConfig != null) {
				IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
				if (indexService != null) {
					platformIndexItem = DomainIndexItemHelper.INSTANCE.getPlatformIndexItem(indexService, id);
				}
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		if (machineConfig != null) {
			request.setAttribute("machineConfig", machineConfig);
		}
		if (platformConfig != null) {
			request.setAttribute("platformConfig", platformConfig);
		}
		if (platformIndexItem != null) {
			request.setAttribute("platformIndexItem", platformIndexItem);
		}

		request.getRequestDispatcher(contextRoot + "/views/domain_platform_attributes_v1.jsp").forward(request, response);
	}

}
