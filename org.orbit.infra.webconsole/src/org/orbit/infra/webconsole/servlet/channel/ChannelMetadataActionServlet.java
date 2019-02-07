package org.orbit.infra.webconsole.servlet.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.io.util.InfraIndexItemHelper;
import org.orbit.infra.model.RequestConstants;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelMetadataActionServlet extends HttpServlet {

	private static final long serialVersionUID = 8445029366151298542L;

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
		String action = ServletUtil.getParameter(request, "action", "");

		String message = "";
		if (channelIds.length == 0) {
			// message = MessageHelper.INSTANCE.add(message, "'channelId' parameter is not set.");
			message = MessageHelper.INSTANCE.add(message, "Channels are not selected.");
		}
		if (action.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "'action' parameter is not set.");
		}
		if (!RequestConstants.RUNTIME_CHANNEL_ACTIONS.contains(action)) {
			message = MessageHelper.INSTANCE.add(message, "Action '" + action + "' is not supported. Supported actions: " + Arrays.toString(RequestConstants.RUNTIME_CHANNEL_ACTIONS.toArray(new String[RequestConstants.RUNTIME_CHANNEL_ACTIONS.size()])) + ".");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		if (channelIds.length > 0 && RequestConstants.RUNTIME_CHANNEL_ACTIONS.contains(action)) {

			boolean isStartAction = false;
			boolean isSuspendAction = false;
			boolean isStopAction = false;
			boolean isSyncRuntimeChannelAction = false;

			String actionResultLabel = "";
			if (RequestConstants.RUNTIME_CHANNEL_ACTION__START.equals(action)) {
				isStartAction = true;
				actionResultLabel = "started";

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__SUSPEND.equals(action)) {
				isSuspendAction = true;
				actionResultLabel = "suspended";

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__STOP.equals(action)) {
				isStopAction = true;
				actionResultLabel = "stopped";

			} else if (RequestConstants.RUNTIME_CHANNEL_ACTION__SYNC.equals(action)) {
				isSyncRuntimeChannelAction = true;
				actionResultLabel = "synchronized";
			}

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

							// String channelName = channelMetadata.getName();
							String dataTubeId = channelMetadata.getDataTubeId();

							boolean isDataTubeOnline = false;
							String dataTubeServiceUrl = null;
							IndexItem dataTubeIndexItem = dataTubeIndexItemMap.get(dataTubeId);
							if (dataTubeIndexItem != null) {
								isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);
								if (isDataTubeOnline) {
									dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
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

							boolean isRuntimeChannelUpdated = false;
							if (isSyncRuntimeChannelAction) {
								isRuntimeChannelUpdated = InfraClientsHelper.DATA_TUBE.syncRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

								if (isRuntimeChannelUpdated) {
									hasSucceed = true;
								} else {
									hasFailed = true;
								}

							} else {
								ChannelStatus channelStatus = ChannelStatus.EMPTY;
								if (isStartAction) {
									channelStatus = ChannelStatus.STARTED;
								} else if (isSuspendAction) {
									channelStatus = ChannelStatus.SUSPENDED;
								} else if (isStopAction) {
									channelStatus = ChannelStatus.STOPPED;
								}

								boolean isChannelMetadataUpdated = InfraClientsHelper.DATA_CAST.setChannelMetadataStatusById(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId, channelStatus, false);
								if (isChannelMetadataUpdated) {
									if (isDataTubeOnline && dataTubeServiceUrl != null) {
										// delete runtime channel from data tube service
										isRuntimeChannelUpdated = false;
										if (isStartAction) {
											isRuntimeChannelUpdated = InfraClientsHelper.DATA_TUBE.startRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										} else if (isSuspendAction) {
											isRuntimeChannelUpdated = InfraClientsHelper.DATA_TUBE.suspendRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										} else if (isStopAction) {
											isRuntimeChannelUpdated = InfraClientsHelper.DATA_TUBE.stopRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);
										}
									}
								}

								if (isChannelMetadataUpdated && isRuntimeChannelUpdated) {
									hasSucceed = true;
								} else {
									hasFailed = true;
								}
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
				e.printStackTrace();
			}

			if (hasSucceed && !hasFailed) {
				succeed = true;
			}

			if (channelIds.length > 0) {
				if (succeed) {
					if (isSyncRuntimeChannelAction) {
						message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Runtime channels are synchronized." : "Runtime channel is synchronized.");
					} else {
						message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Channels are " + actionResultLabel + "." : "Channel is " + actionResultLabel + ".");
					}
				} else {
					if (isSyncRuntimeChannelAction) {
						message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Runtime channels are not synchronized." : "Runtime channel is not synchronized.");
					} else {
						message = MessageHelper.INSTANCE.add(message, (channelIds.length > 1) ? "Channels are not " + actionResultLabel + "." : "Channel is not " + actionResultLabel + ".");
					}
				}
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
