package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.io.util.DefaultPlatformClientResolver;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.InfraConstants;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class NodeProgramProblemsActionServlet extends HttpServlet {

	private static final long serialVersionUID = -637141448135512688L;

	public static String ACTION__CLEAR = "clear";

	public static List<String> ACTIONS = new ArrayList<String>();

	static {
		ACTIONS.add(ACTION__CLEAR);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		String machineId = ServletUtil.getParameter(request, "machineId", "");
		String parentPlatformId = ServletUtil.getParameter(request, "platformId", "");
		String nodeId = ServletUtil.getParameter(request, "id", "");
		String programId = ServletUtil.getParameter(request, "programId", "");
		String programVersion = ServletUtil.getParameter(request, "programVersion", "");
		String action = ServletUtil.getParameter(request, "action", "");

		String message = "";
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
		if (action.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'action' parameter is not set.");
		}
		if (!ACTIONS.contains(action)) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is not supported. Supported actions: " + Arrays.toString(ACTIONS.toArray(new String[ACTIONS.size()])) + ".");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		if (!machineId.isEmpty() && !parentPlatformId.isEmpty() && !nodeId.isEmpty() && !programId.isEmpty() && !programVersion.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);
				PlatformClient nodePlatformClient = platformClientResolver.resolve(parentPlatformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);

				if (ACTION__CLEAR.equals(action)) {
					succeed = nodePlatformClient.clearProgramProblems(programId, programVersion);
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is performed.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' failed.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/domain/nodeprogramproblems?machineId=" + machineId + "&platformId=" + parentPlatformId + "&id=" + nodeId + "&programId=" + programId + "&programVersion=" + programVersion);
	}

}
