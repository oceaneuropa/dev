package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;

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
import org.orbit.component.io.util.DefaultNodeControlClientResolver;
import org.orbit.component.io.util.DefaultPlatformClientResolver;
import org.orbit.component.io.util.OrbitClientHelper;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.Problem;
import org.orbit.platform.api.ProgramInfo;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeProgramProblemsListServlet extends HttpServlet {

	private static final long serialVersionUID = 176485682101167387L;

	private static final Problem[] EMPTY_PROBLEMS = new Problem[0];

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
		String programId = ServletUtil.getParameter(request, "programId", "");
		String programVersion = ServletUtil.getParameter(request, "programVersion", "");

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
		if (programId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'programId' parameter is not set.");
		}
		if (programVersion.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'programVersion' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		MachineConfig machineConfig = null;
		PlatformConfig platformConfig = null;
		NodeInfo nodeInfo = null;
		ProgramInfo program = null;
		Problem[] problems = null;
		boolean isNodeOnline = false;

		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && !programId.isEmpty() && !programVersion.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				machineConfig = ComponentClientsUtil.DomainControl.getMachineConfig(domainServiceUrl, accessToken, machineId);
				platformConfig = ComponentClientsUtil.DomainControl.getPlatformConfig(domainServiceUrl, accessToken, machineId, parentPlatformId);

				IndexItem nodeIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(accessToken, nodeId);
				if (nodeIndexItem != null) {
					isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				}

				if (isNodeOnline) {
					NodeControlClientResolver nodeControlClientResolver = new DefaultNodeControlClientResolver();
					nodeInfo = ComponentClientsUtil.NodeControl.getNode(nodeControlClientResolver, accessToken, parentPlatformId, nodeId);

					PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
					PlatformClient nodePlatformClient = platformClientResolver.resolve(parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

					program = nodePlatformClient.getProgram(programId, programVersion);
					problems = nodePlatformClient.getProgramProblems(programId, programVersion);

					if (program == null) {
						message = MessageHelper.INSTANCE.add(message, "Program is not found.");
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "Node '" + nodeId + "' is offline.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}
		if (problems == null) {
			problems = EMPTY_PROBLEMS;
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
		if (program != null) {
			request.setAttribute("program", program);
		}
		request.setAttribute("programId", programId);
		request.setAttribute("programVersion", programVersion);
		request.setAttribute("problems", problems);

		request.getRequestDispatcher(contextRoot + "/views/node_program_problems.jsp").forward(request, response);
	}

}
