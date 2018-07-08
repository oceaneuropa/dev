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
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.DomainIndexItemHelper;
import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.api.PlatformConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class PlatformListServlet extends HttpServlet {

	private static final long serialVersionUID = -8366157200926964290L;
	private static final PlatformConfig[] EMPTY_PLATFORM_CONFIGS = new PlatformConfig[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String machineId = ServletUtil.getParameter(request, "machineId", "");

		MachineConfig machineConfig = null;
		PlatformConfig[] platformConfigs = null;
		if (!machineId.isEmpty()) {
			DomainManagementClient domainMgmt = OrbitClients.getInstance().getDomainService(domainServiceUrl);
			if (domainMgmt != null && machineId != null) {
				try {
					machineConfig = domainMgmt.getMachineConfig(machineId);
					platformConfigs = domainMgmt.getPlatformConfigs(machineId);

				} catch (ClientException e) {
					e.printStackTrace();
				}
			}

			// Get index items for platforms with type "server"
			IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
			if (indexService != null) {
				Map<String, IndexItem> platformIdToIndexItem = DomainIndexItemHelper.INSTANCE.getPlatformIdToIndexItem(indexService, null, PlatformConstants.PLATFORM_TYPE__SERVER);

				if (platformConfigs != null) {
					for (PlatformConfig platformConfig : platformConfigs) {
						String platformId = platformConfig.getId();
						boolean isActivate = false;
						String runtimeState = "";
						IndexItem indexItem = platformIdToIndexItem.get(platformId);
						if (indexItem != null) {
							isActivate = IndexItemHelper.INSTANCE.isUpdatedWithinSeconds(indexItem, 20);
							runtimeState = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_RUNTIME_STATE);
						}
						platformConfig.getRuntimeStatus().setActivate(isActivate);
						platformConfig.getRuntimeStatus().setRuntimeState(runtimeState);
					}
				}
			}
		}

		if (platformConfigs == null) {
			platformConfigs = EMPTY_PLATFORM_CONFIGS;
		}

		if (message != null) {
			request.setAttribute("message", message);
		}
		if (machineConfig != null) {
			request.setAttribute("machineConfig", machineConfig);
		}
		request.setAttribute("platformConfigs", platformConfigs);
		request.getRequestDispatcher(contextRoot + "/views/domain_platforms_v1.jsp").forward(request, response);
	}

}

// @Override
// protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// doGet(request, response);
// }
