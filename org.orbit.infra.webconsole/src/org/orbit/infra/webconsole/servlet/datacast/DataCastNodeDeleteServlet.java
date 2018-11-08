package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DataCastNodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataCastNodeDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 2318338748025863262L;
	private static String[] EMPTY_ELEMENT_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String[] elementIds = ServletUtil.getParameterValues(request, "elementId", EMPTY_ELEMENT_IDS);

		String message = "";
		if (elementIds.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'elementIds' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Data cast nodes are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (elementIds.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = DataCastNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg == null) {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + DataCastNodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");

				} else {
					for (String elementId : elementIds) {
						boolean currIsDeleted = cfgReg.deleteConfigElement(elementId);
						if (currIsDeleted) {
							hasSucceed = true;
						} else {
							hasFailed = true;
						}
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Data cast nodes are deleted successfully." : "Data cast node is deleted successfully.");
		} else {
			message = MessageHelper.INSTANCE.add(message, (elementIds.length > 1) ? "Data cast nodes are not deleted." : "Data cast node is not deleted.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/datacastlist");
	}

}
