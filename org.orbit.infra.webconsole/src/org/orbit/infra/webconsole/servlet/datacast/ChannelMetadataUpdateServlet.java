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
import org.orbit.infra.io.util.DataCastNodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelMetadataUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 6717634139408260228L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");
		String channelId = ServletUtil.getParameter(request, "channelId", "");
		String name = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (channelId.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'channelId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean hasNameChange = false;
		boolean isNameUpdated = false;

		boolean hasAttributesChange = false;
		boolean isAttributesUpdated = false;

		if (!channelId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = DataCastNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					IConfigElement configElement = cfgReg.getConfigElement(channelId);
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

					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element with elementId '" + channelId + "' cannot be found.");
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + DataCastNodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");
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

		response.sendRedirect(contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId);
	}

}
