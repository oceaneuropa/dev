package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.OrbitExtensionHelper;
import org.orbit.infra.api.util.OrbitIndexHelper;
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
		String domainServiceUrl = getServletConfig().getInitParameter(OrbitConstants.ORBIT_DOMAIN_SERVICE_URL);

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
				// Get machine configuration
				machineConfig = OrbitComponentHelper.INSTANCE.getMachineConfig(domainServiceUrl, machineId);

				// Get platform configuration
				platformConfig = OrbitComponentHelper.INSTANCE.getPlatformConfig(domainServiceUrl, machineId, platformId);

				// Get index item of the platform
				platformIndexItem = OrbitIndexHelper.INSTANCE.getIndexItemOfPlatform(indexServiceUrl, platformId);

				// Get all extensions from the platform
				extensionItems = OrbitExtensionHelper.INSTANCE.getExtensionItemsOfPlatform(extensionRegistryUrl, platformId);

				// extensionItemMap = OrbitExtensionHelper.INSTANCE.getExtensionItemMapOfPlatform(extensionRegistryUrl, id);
				extensionItemMap = OrbitExtensionHelper.INSTANCE.toExtensionItemMap(extensionItems);

				// Get indexer extensions from the platform
				List<ExtensionItem> indexerExtensionItems = OrbitExtensionHelper.INSTANCE.getExtensionItemsOfPlatform(extensionRegistryUrl, platformId, ServiceIndexTimerFactory.EXTENSION_TYPE_ID);

				// Get index items from the platform
				for (ExtensionItem indexerExtensionItem : indexerExtensionItems) {
					String indexerId = indexerExtensionItem.getExtensionId();

					List<IndexItem> currIndexItems = OrbitIndexHelper.INSTANCE.getIndexItemsOfPlatform(indexServiceUrl, indexerId, platformId);
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

		request.getRequestDispatcher(contextRoot + "/views/domain_platform_properties_v1.jsp").forward(request, response);
	}

}

// Map<String, List<IndexItem>> indexerIdToIndexItemsMap = new LinkedHashMap<String, List<IndexItem>>();
// indexerIdToIndexItemsMap.put(indexerId, currIndexItems);
// if (indexerIdToIndexItemsMap != null) {
// request.setAttribute("indexerIdToIndexItemsMap", indexerIdToIndexItemsMap);
// }
