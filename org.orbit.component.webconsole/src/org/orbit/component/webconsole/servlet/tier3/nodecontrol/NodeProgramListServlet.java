package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.tier3.domain.MachineConfig;
import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeControlClientResolver;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.orbit.component.api.util.AppStoreUtil;
import org.orbit.component.api.util.DomainUtil;
import org.orbit.component.api.util.NodeUtil;
import org.orbit.component.io.util.DefaultNodeControlClientResolver;
import org.orbit.component.io.util.DefaultPlatformClientResolver;
import org.orbit.component.io.util.OrbitClientHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.orbit.infra.io.util.ConfigRegistryHelper;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.ProgramInfo;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.orbit.platform.connector.impl.ProgramInfoImpl;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeProgramListServlet extends HttpServlet {

	private static final long serialVersionUID = 2284649451336905223L;

	private static final ProgramInfo[] EMPTY_PROGRAMS = new ProgramInfo[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
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
		ProgramInfo[] programs = null;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = DomainUtil.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = DomainUtil.getPlatformConfig(domainServiceUrl, accessToken, machineId, parentPlatformId);

				NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver();
				nodeInfo = NodeUtil.getNode(nodeControlClientResolver, accessToken, parentPlatformId, nodeId);

				// Get node's programs (metadata) from config registry.
				List<AppManifest> nodeAppManifests = new ArrayList<AppManifest>();
				IConfigRegistry registry = ConfigRegistryHelper.getPlatformsRegistry(accessToken, true);
				if (registry != null) {
					// IConfigRegistry not being null means CFG service is online and available.
					// Get the children elements of the "/{platformId}/programs" config element.
					IConfigElement[] programElements = ConfigRegistryHelper.getProgramsElementChildren(registry, nodeId, true);

					for (IConfigElement programElement : programElements) {
						// String currName = programElement.getName();
						String currAppId = programElement.getAttribute("id", String.class);
						String currAppVersion = programElement.getAttribute("version", String.class);
						// String currType = programElement.getAttribute("type", String.class);
						// String currDesc = programElement.getAttribute("description", String.class);

						if (currAppId != null && !currAppId.isEmpty()) {
							try {
								// When app version is null or empty, AppStore should return app with latest version.
								AppManifest appManifest = AppStoreUtil.getApp(accessToken, currAppId, currAppVersion);
								if (appManifest != null) {
									nodeAppManifests.add(appManifest);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}

				boolean isNodeOnline = false;
				IndexItem nodeIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(accessToken, nodeId);
				if (nodeIndexItem != null) {
					isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				}
				if (isNodeOnline) {
					// Node is online
					// All programs shown are based on actual programs installed on the node (returned from the platform of the node).
					PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
					programs = PlatformClientsUtil.INSTANCE.getPrograms(platformClientResolver, parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

				} else {
					// Node is offline
					// All programs shown are based on config registry
					List<ProgramInfo> thePrograms = new ArrayList<ProgramInfo>();
					for (AppManifest nodeAppManifest : nodeAppManifests) {
						String currAppId = nodeAppManifest.getAppId();
						String currAppVersion = nodeAppManifest.getAppVersion();
						String currAppType = nodeAppManifest.getType();
						String currAppName = nodeAppManifest.getName();
						String currAppDesc = nodeAppManifest.getDescription();

						ProgramInfoImpl program = new ProgramInfoImpl();
						program.setId(currAppId);
						program.setVersion(currAppVersion);
						program.setType(currAppType);
						program.setName(currAppName);
						program.setDescription(currAppDesc);
						program.setActivationState(ProgramInfo.ACTIVATION_STATE.UNKNOWN);
						program.setRuntimeState(ProgramInfo.RUNTIME_STATE.UNKNOWN);

						thePrograms.add(program);
					}

					programs = thePrograms.toArray(new ProgramInfo[thePrograms.size()]);
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (nodeInfo == null) {
			message = MessageHelper.INSTANCE.add(message, "Node with id '" + nodeId + "' is not found.");
		}
		if (programs == null) {
			programs = EMPTY_PROGRAMS;
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
		request.setAttribute("id", nodeId);
		if (nodeInfo != null) {
			request.setAttribute("nodeInfo", nodeInfo);
		}
		request.setAttribute("programs", programs);

		request.getRequestDispatcher(contextRoot + "/views/node_programs.jsp").forward(request, response);
	}

}
