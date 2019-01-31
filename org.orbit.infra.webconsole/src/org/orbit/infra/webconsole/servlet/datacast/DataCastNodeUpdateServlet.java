package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;
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

public class DataCastNodeUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 64890005468755567L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String elementId = ServletUtil.getParameter(request, "elementId", "");
		String dataCastId = ServletUtil.getParameter(request, "data_cast_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String enabledStr = ServletUtil.getParameter(request, "enabled", "");
		boolean enabled = ("true".equals(enabledStr)) ? true : false;

		String message = "";
		if (elementId.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'elementId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean hasNameChange = false;
		boolean isNameUpdated = false;

		boolean hasAttributesChange = false;
		boolean isAttributesUpdated = false;

		if (!elementId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = NodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					IConfigElement configElement = cfgReg.getConfigElement(elementId);
					if (configElement != null) {
						// Update name
						String oldName = configElement.getName();
						if (!name.equals(oldName)) {
							hasNameChange = true;
							isNameUpdated = configElement.rename(name);
							if (isNameUpdated) {
								message = MessageHelper.INSTANCE.add(message, "Config element name is updated.");
							}
						}

						// Update attributes
						Map<String, Object> attributes = configElement.getAttributes();
						String oldDataCastId = configElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);
						if (!oldDataCastId.equals(dataCastId)) {
							hasAttributesChange = true;
							attributes.put(InfraConstants.IDX_PROP__DATACAST__ID, dataCastId);
						}
						boolean oldEnabled = configElement.getAttribute("enabled", Boolean.class);
						if (enabled != oldEnabled) {
							hasAttributesChange = true;
							attributes.put("enabled", enabled);
						}
						if (!oldDataCastId.equals(dataCastId) || enabled != oldEnabled) {
							isAttributesUpdated = configElement.setAttributes(attributes);
							if (isAttributesUpdated) {
								message = MessageHelper.INSTANCE.add(message, "Config element attributes are updated.");
							}
						}

					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element with elementId '" + elementId + "' cannot be found.");
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + NodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if ((hasNameChange && isNameUpdated) || (hasAttributesChange && isAttributesUpdated)) {
			hasSucceed = true;
		}
		if ((hasNameChange && !isNameUpdated) || (hasAttributesChange && !isAttributesUpdated)) {
			hasFailed = true;
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		if (succeed) {
			// message = MessageHelper.INSTANCE.add(message, "Data cast node is updated.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Data cast node is not updated.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/datacastlist");
	}

}
