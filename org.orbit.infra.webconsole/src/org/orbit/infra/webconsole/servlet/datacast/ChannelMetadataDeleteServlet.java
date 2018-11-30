package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.io.util.InfraIndexItemHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelMetadataDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = -8421469580508595284L;

	private static String[] EMPTY_CHANNEL_IDS = new String[] {};

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);

		String groupBy = ServletUtil.getParameter(request, "groupBy", "");

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");
		String[] channelIds = ServletUtil.getParameterValues(request, "channelId", EMPTY_CHANNEL_IDS);

		String message = "";
		if (channelIds.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'channelId' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Channels are not selected.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;
		if (channelIds.length > 0) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				List<String> dataTubeIdsWithoutIndexItem = new ArrayList<String>();
				List<String> dataTubeIdsWithoutOnlineService = new ArrayList<String>();

				IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
				if (dataCastIndexItem != null) {
					boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);

					if (isDataCastOnline) {
						String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

						DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
						DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver(indexServiceUrl);

						Map<String, IndexItem> dataTubeIndexItemMap = InfraIndexItemHelper.getDataTubeIndexItemsMap(indexServiceUrl, accessToken, dataCastId);

						for (String channelId : channelIds) {
							ChannelMetadata channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelId(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId);
							if (channelMetadata == null) {
								message = MessageHelper.INSTANCE.add(message, "Channel metadata (channelId='" + channelId + "') is not found.");
								continue;
							}

							String dataTubeId = channelMetadata.getDataTubeId();

							boolean currIsDeleted = InfraClientsHelper.DATA_CAST.deleteChannelMetadataById(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId);
							if (currIsDeleted) {
								hasSucceed = true;

								IndexItem dataTubeIndexItem = dataTubeIndexItemMap.get(dataTubeId);
								if (dataTubeIndexItem != null) {
									boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);

									if (isDataTubeOnline) {
										// delete runtime channel from data tube service
										String dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
										boolean isRuntimeChannelDeleted = InfraClientsHelper.DATA_TUBE.deleteRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										if (isRuntimeChannelDeleted) {
											message = MessageHelper.INSTANCE.add(message, "Runtime channel (channelId='" + channelId + "'; dataTubeId='" + dataTubeId + "') is deleted.");
										} else {
											message = MessageHelper.INSTANCE.add(message, "Runtime channel (channelId='" + channelId + "'; dataTubeId='" + dataTubeId + "') is not deleted.");
										}
									} else {
										if (!dataTubeIdsWithoutOnlineService.contains(dataTubeId)) {
											dataTubeIdsWithoutOnlineService.add(dataTubeId);
										}
									}
								} else {
									if (!dataTubeIdsWithoutIndexItem.contains(dataTubeId)) {
										dataTubeIdsWithoutIndexItem.add(dataTubeId);
									}
								}
							} else {
								hasFailed = true;
							}
						}
					} else {
						message = MessageHelper.INSTANCE.add(message, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataCast service (dataCastId='" + dataCastId + "') is not found.");
				}

				for (String currDataTubeId : dataTubeIdsWithoutIndexItem) {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataTubeId='" + currDataTubeId + "') is not found.");
				}
				for (String currDataTubeId : dataTubeIdsWithoutOnlineService) {
					message = MessageHelper.INSTANCE.add(message, "DataTube service (dataTubeId='" + currDataTubeId + "') is not online.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}

			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (succeed) {
				message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Channel metadatas are deleted successfully." : "Channel metadata is deleted successfully.");
			} else {
				message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Channel metadatas are not deleted." : "Channel metadata is not deleted.");
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
