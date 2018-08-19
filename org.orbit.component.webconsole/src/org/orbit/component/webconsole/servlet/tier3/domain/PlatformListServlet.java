package org.orbit.component.webconsole.servlet.tier3.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.MessageHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class PlatformListServlet extends HttpServlet {

	private static final long serialVersionUID = -8366157200926964290L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String extensionRegistryUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String machineId = ServletUtil.getParameter(request, "machineId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig[] platformConfigs = null;
		Map<String, IndexItem> platformIdToIndexItemMap = null;
		Map<String, Map<String, List<IndexItem>>> platformIdToIndexerIdToIndexItemsMap = new HashMap<String, Map<String, List<IndexItem>>>();

		if (!machineId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);

				platformConfigs = ComponentClientsUtil.DomainControl.getPlatformConfigs(domainServiceUrl, accessToken, machineId);

				// Get index items for platforms
				platformIdToIndexItemMap = InfraClientsUtil.IndexItems.getPlatformIdToIndexItem(indexServiceUrl, accessToken, null, PlatformConstants.PLATFORM_TYPE__SERVER);

				if (platformConfigs != null) {
					for (PlatformConfig platformConfig : platformConfigs) {
						String platformId = platformConfig.getId();
						boolean isOnline = false;
						String runtimeState = "";
						IndexItem indexItem = platformIdToIndexItemMap.get(platformId);
						if (indexItem != null) {
							isOnline = IndexItemHelper.INSTANCE.isOnline(indexItem);
							runtimeState = (String) indexItem.getProperties().get(PlatformConstants.PLATFORM_RUNTIME_STATE);
						}
						platformConfig.getRuntimeStatus().setOnline(isOnline);
						platformConfig.getRuntimeStatus().setRuntimeState(runtimeState);

						Map<String, List<IndexItem>> indexerIdToIndexItemsMap = new LinkedHashMap<String, List<IndexItem>>();
						// Get extensions indexer ids from the platform
						List<String> indexerIds = InfraClientsUtil.ExtensionRegistry.getExtensionIdsOfPlatform(extensionRegistryUrl, accessToken, platformId, ServiceIndexTimerFactory.EXTENSION_TYPE_ID);
						for (String indexerId : indexerIds) {
							List<IndexItem> currIndexItems = InfraClientsUtil.IndexItems.getIndexItemsOfPlatform(indexServiceUrl, accessToken, indexerId, platformId);
							indexerIdToIndexItemsMap.put(indexerId, currIndexItems);
						}
						platformIdToIndexerIdToIndexItemsMap.put(platformId, indexerIdToIndexItemsMap);
					}
				}

			} catch (ClientException e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
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
		if (platformConfigs != null) {
			request.setAttribute("platformConfigs", platformConfigs);
		}
		if (platformIdToIndexItemMap != null) {
			request.setAttribute("platformIdToIndexItemMap", platformIdToIndexItemMap);
		}
		request.setAttribute("platformIdToIndexerIdToIndexItemsMap", platformIdToIndexerIdToIndexItemsMap);

		request.getRequestDispatcher(contextRoot + "/views/domain_platforms_list_v1.jsp").forward(request, response);
	}

}
