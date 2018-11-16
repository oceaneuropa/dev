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
import org.orbit.infra.io.util.InfraNodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataCastNodeAddServlet extends HttpServlet {

	private static final long serialVersionUID = 9035877661499855559L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String dataCastId = ServletUtil.getParameter(request, "data_cast_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String enabledStr = ServletUtil.getParameter(request, "enabled", "");
		boolean enabled = ("true".equals(enabledStr)) ? true : false;

		String message = "";
		if (dataCastId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'data_cast_id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement configElement = null;
		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = InfraNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg == null) {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + InfraNodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");

				} else {
					Map<String, Object> attributes = new HashMap<String, Object>();
					attributes.put(InfraConstants.IDX_PROP__DATACAST__ID, dataCastId);
					attributes.put("enabled", enabled);
					configElement = cfgReg.createRootConfigElement(name, attributes, true);
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

		response.sendRedirect(contextRoot + "/admin/datacastlist");
	}

}
