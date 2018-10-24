package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

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
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.DefaultNodeControlClientResolver;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodePropertyListServlet extends HttpServlet {

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
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");

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
		if (parentPlatformId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'platformId' parameter is not set.");
		}
		if (nodeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		NodeInfo nodeInfo = null;
		IndexItem nodeIndexItem = null;
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		List<ExtensionItem> extensionItems = null;
		Map<String, List<ExtensionItem>> extensionItemMap = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = ComponentClientsUtil.DomainControl.getPlatformConfig(domainServiceUrl, accessToken, machineId, parentPlatformId);

				NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver(indexServiceUrl);
				nodeInfo = ComponentClientsUtil.NodeControl.getNode(nodeControlClientResolver, accessToken, parentPlatformId, nodeId);

				nodeIndexItem = InfraClientsUtil.INDEX_SERVICE.getIndexItem(indexServiceUrl, accessToken, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (nodeIndexItem != null) {
					String nodePlatformId = (String) nodeIndexItem.getProperties().get(PlatformConstants.PLATFORM_ID);

					if (nodePlatformId != null) {
						List<ExtensionItem> indexerExtensionItems = InfraClientsUtil.EXTENSION_REGISTRY.getExtensionItemsOfPlatform(extensionRegistryUrl, nodePlatformId, ServiceIndexTimerFactory.EXTENSION_TYPE_ID);
						for (ExtensionItem indexerExtensionItem : indexerExtensionItems) {
							String indexerId = indexerExtensionItem.getExtensionId();

							List<IndexItem> currIndexItems = InfraClientsUtil.INDEX_SERVICE.getIndexItemsOfPlatform(indexServiceUrl, accessToken, indexerId, nodePlatformId);
							if (currIndexItems != null && !currIndexItems.isEmpty()) {
								indexItems.addAll(currIndexItems);
							}
						}

						// Get all extensions from the platform (of the Node)
						extensionItems = InfraClientsUtil.EXTENSION_REGISTRY.getExtensionItemsOfPlatform(extensionRegistryUrl, accessToken, nodePlatformId);
						extensionItemMap = InfraClientsUtil.EXTENSION_REGISTRY.toExtensionItemMap(extensionItems);
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (nodeInfo == null) {
			message = MessageHelper.INSTANCE.add(message, "Node with id '" + nodeId + "' is not found.");
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
		if (nodeInfo != null) {
			request.setAttribute("nodeInfo", nodeInfo);
		}
		if (nodeIndexItem != null) {
			request.setAttribute("nodeIndexItem", nodeIndexItem);
		}
		if (indexItems != null) {
			request.setAttribute("indexItems", indexItems);
		}
		if (extensionItems != null) {
			request.setAttribute("extensionItems", extensionItems);
		}
		if (extensionItemMap != null) {
			request.setAttribute("extensionItemMap", extensionItemMap);
		}

		request.getRequestDispatcher(contextRoot + "/views/node_properties.jsp").forward(request, response);
	}

}
