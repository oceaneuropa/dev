package org.orbit.infra.webconsole.servlet.channel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.ChannelStatus;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.io.util.InfraIndexItemHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.model.AccountConfig;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelMetadataAddServlet extends HttpServlet {

	private static final long serialVersionUID = -4595501836933011392L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String groupBy = ServletUtil.getParameter(request, "groupBy", "");

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");
		String dataTubeId = ServletUtil.getParameter(request, "data_tube_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String accessType = ServletUtil.getParameter(request, "access_type", "");
		String accessCode = ServletUtil.getParameter(request, "access_code", "");
		String ownerAccountId = ServletUtil.getParameter(request, "owner_account_id", "");
		String start = ServletUtil.getParameter(request, "-start", "");

		// normalize value
		if (!"public".equals(accessType) && !"private".equals(accessType)) {
			accessType = "public";
		}

		ChannelStatus channelStatus = null;
		if ("true".equalsIgnoreCase(start)) {
			channelStatus = ChannelStatus.STARTED;
		} else {
			channelStatus = ChannelStatus.STOPPED;
		}

		String message = "";
		if (dataCastId.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'data_cast_id' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		ChannelMetadata channelMetadata = null;
		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				if (ownerAccountId.isEmpty()) {
					ownerAccountId = OrbitTokenUtil.INSTANCE.getAccountIdFromSession(request);
				}

				DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
				DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver(indexServiceUrl);

				IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
				if (dataCastIndexItem != null) {
					boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);

					if (isDataCastOnline) {
						String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

						List<AccountConfig> accountConfigs = null;
						Map<String, Object> properties = null;

						channelMetadata = InfraClientsHelper.DATA_CAST.createChannelMetadata(dataCastClientResolver, dataCastServiceUrl, accessToken, dataTubeId, name, channelStatus, accessType, accessCode, ownerAccountId, accountConfigs, properties);

						if (channelMetadata != null) {
							message = MessageHelper.INSTANCE.add(message, "Channel metadata is created.");
							String channelId = channelMetadata.getChannelId();

							IndexItem dataTubeIndexItem = InfraIndexItemHelper.getDataTubeIndexItem(indexServiceUrl, accessToken, dataCastId, dataTubeId);
							if (dataTubeIndexItem != null) {
								boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);

								if (isDataTubeOnline) {
									String dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
									RuntimeChannel runtimeChannel = InfraClientsHelper.DATA_TUBE.createRuntimeChannelId(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId, true);

									if (runtimeChannel != null) {
										message = MessageHelper.INSTANCE.add(message, "Runtime channel is created.");
									} else {
										message = MessageHelper.INSTANCE.add(message, "Runtime channel is not created.");
									}
								} else {
									message = MessageHelper.INSTANCE.add(message, "DataTube service (dataTubeId='" + dataTubeId + "') is not online.");
								}
							} else {
								message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataTubeId='" + dataTubeId + "') is not found.");
							}
						} else {
							message = MessageHelper.INSTANCE.add(message, "Channel metadata is not created.");
						}
					} else {
						message = MessageHelper.INSTANCE.add(message, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataCast service (dataCastId='" + dataCastId + "') cannot be found.");
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId + "&groupBy=" + groupBy);
	}

}
