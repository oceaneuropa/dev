package org.orbit.component.webconsole.servlet.domain;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier3.domainmanagement.MachineConfig;
import org.orbit.component.api.tier3.domainmanagement.PlatformConfig;
import org.orbit.component.api.util.OrbitComponentHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.MessageHelper;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.util.OrbitInfraHelper;
import org.origin.common.util.ServletUtil;

public class PlatformPropertyListServlet extends HttpServlet {

	private static final long serialVersionUID = 2333552536533608770L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String extensionRegistryUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_EXTENSION_REGISTRY_URL);
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
			message = MessageHelper.INSTANCE.add(message, "'machineId' parameter is not set.");
		}
		if (id.isEmpty()) {
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

		if (!machineId.isEmpty() && !id.isEmpty()) {
			try {
				machineConfig = OrbitComponentHelper.INSTANCE.getMachineConfig(domainServiceUrl, machineId);

				platformConfig = OrbitComponentHelper.INSTANCE.getPlatformConfig(domainServiceUrl, machineId, id);

				platformIndexItem = OrbitInfraHelper.INSTANCE.getPlatformIndexItem(indexServiceUrl, id);

				extensionItems = OrbitInfraHelper.INSTANCE.getExtensionItems(extensionRegistryUrl, id);

				// extensionItemMap = OrbitInfraHelper.INSTANCE.getExtensionItemMap(extensionRegistryUrl, id);
				extensionItemMap = OrbitInfraHelper.INSTANCE.getExtensionItemMap(extensionItems);

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
		request.getRequestDispatcher(contextRoot + "/views/domain_platform_properties_v1.jsp").forward(request, response);
	}

}
