package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DataCastIndexItemHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelListServlet extends HttpServlet {

	private static final long serialVersionUID = -1927854053516423728L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String message = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			message = (String) session.getAttribute("message");
			if (message != null) {
				session.removeAttribute("message");
			}
		}

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IndexItem dataCastIndexItem = null;

		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				dataCastIndexItem = DataCastIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
				if (dataCastIndexItem == null) {
					message = MessageHelper.INSTANCE.add(message, "DataCast index item doesn't exist. dataCastId: '" + dataCastId + "'.");
				}

				if (dataCastIndexItem != null) {
					boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
					if (!isOnline) {
						message = MessageHelper.INSTANCE.add(message, "DataCast service is not online. dataCastId: '" + dataCastId + "'.");
					}

					if (isOnline) {
						DataCastClient dataCastClient = null;
						String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
						if (dataCastServiceUrl != null) {
							try {
								DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
								dataCastClient = dataCastClientResolver.resolve(dataCastServiceUrl, accessToken);

								if (dataCastClient == null) {
									message = MessageHelper.INSTANCE.add(message, "DataCast client is not available. dataCastId: '" + dataCastId + "'.");
								}
								if (dataCastClient != null) {
									
								}
							} catch (Exception e) {
								message = MessageHelper.INSTANCE.add(message, e.getMessage());
							}
						}
					}
				}

				dataCastIndexItem = DataCastIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);

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
		request.setAttribute("dataCastId", dataCastId);
		if (dataCastIndexItem != null) {
			request.setAttribute("dataCastIndexItem", dataCastIndexItem);
		}

		request.getRequestDispatcher(contextRoot + "/views/admin_channel_list.jsp").forward(request, response);
	}

}
