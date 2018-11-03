package org.orbit.component.webconsole.servlet.tier3.domain;

import java.io.IOException;
import java.util.ArrayList;
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
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class PlatformPropertyListServlet extends HttpServlet {

	private static final long serialVersionUID = 2333552536533608770L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String extensionRegistryUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		String domainServiceUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String platformId = ServletUtil.getParameter(request, "id", "");

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}
		if (machineId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (platformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		IndexItem platformIndexItem = null;
		List<ExtensionItem> extensionItems = null;
		Map<String, List<ExtensionItem>> extensionItemMap = null;
		List<IndexItem> indexItems = new ArrayList<IndexItem>();

		if (!machineId.isEmpty() && !platformId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				// Get machine configuration
				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);

				// Get platform configuration
				platformConfig = ComponentClientsUtil.DomainControl.getPlatformConfig(domainServiceUrl, accessToken, machineId, platformId);

				// Get index item of the platform
				platformIndexItem = InfraClientsHelper.INDEX_SERVICE.getIndexItemOfPlatform(indexServiceUrl, accessToken, platformId);

				// Get all extensions from the platform
				extensionItems = InfraClientsHelper.EXTENSION_REGISTRY.getExtensionItemsOfPlatform(extensionRegistryUrl, accessToken, platformId);

				// extensionItemMap = OrbitExtensionHelper.INSTANCE.getExtensionItemMapOfPlatform(extensionRegistryUrl, id);
				extensionItemMap = InfraClientsHelper.EXTENSION_REGISTRY.toExtensionItemMap(extensionItems);

				// Get indexer extensions from the platform
				List<ExtensionItem> indexerExtensionItems = InfraClientsHelper.EXTENSION_REGISTRY.getExtensionItemsOfPlatform(extensionRegistryUrl, platformId, ServiceIndexTimerFactory.EXTENSION_TYPE_ID);

				// Get index items from the platform
				for (ExtensionItem indexerExtensionItem : indexerExtensionItems) {
					String indexerId = indexerExtensionItem.getExtensionId();

					List<IndexItem> currIndexItems = InfraClientsHelper.INDEX_SERVICE.getIndexItemsOfPlatform(indexServiceUrl, accessToken, indexerId, platformId);
					if (currIndexItems != null && !currIndexItems.isEmpty()) {
						indexItems.addAll(currIndexItems);
					}
				}

			} catch (Exception e) {
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
		if (platformConfig != null) {
			request.setAttribute("platformConfig", platformConfig);
		}
		if (platformIndexItem != null) {
			request.setAttribute("platformIndexItem", platformIndexItem);
		}
		if (extensionItems != null) {
			request.setAttribute("extensionItems", extensionItems);
		}
		if (extensionItemMap != null) {
			request.setAttribute("extensionItemMap", extensionItemMap);
		}
		if (indexItems != null) {
			request.setAttribute("indexItems", indexItems);
		}

		request.getRequestDispatcher(contextRoot + "/views/platform_properties.jsp").forward(request, response);
	}

}

// Map<String, List<IndexItem>> indexerIdToIndexItemsMap = new LinkedHashMap<String, List<IndexItem>>();
// indexerIdToIndexItemsMap.put(indexerId, currIndexItems);
// if (indexerIdToIndexItemsMap != null) {
// request.setAttribute("indexerIdToIndexItemsMap", indexerIdToIndexItemsMap);
// }
