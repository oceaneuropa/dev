package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.NodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataTubeNodeAddServlet extends HttpServlet {

	private static final long serialVersionUID = -2372849439735213448L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");
		String dataTubeId = ServletUtil.getParameter(request, "data_tube_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String enabledStr = ServletUtil.getParameter(request, "enabled", "");
		boolean enabled = ("true".equals(enabledStr)) ? true : false;

		String message = "";
		if (dataTubeId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'data_tube_id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement dataCastConfigElement = null;
		IConfigElement configElement = null;
		if (!dataTubeId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = NodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					dataCastConfigElement = NodeConfigHelper.INSTANCE.getDataCastConfigElement(cfgReg, dataCastId);
					if (dataCastConfigElement != null) {
						Map<String, Object> attributes = new HashMap<String, Object>();
						attributes.put(InfraConstants.IDX_PROP__DATATUBE__ID, dataTubeId);
						attributes.put("enabled", enabled);
						configElement = dataCastConfigElement.createMemberConfigElement(name, attributes, true);

					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element for data cast node (dataCastId: '" + dataCastId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + NodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		if (configElement == null) {
			message = MessageHelper.INSTANCE.add(message, "Config element is not created.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Config element is created successfully.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/datatubelist?dataCastId=" + dataCastId);
	}

}
