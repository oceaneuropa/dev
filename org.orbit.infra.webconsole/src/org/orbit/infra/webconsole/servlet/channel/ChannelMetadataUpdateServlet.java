package org.orbit.infra.webconsole.servlet.channel;

import java.io.IOException;

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

public class ChannelMetadataUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 6717634139408260228L;

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
		String channelId = ServletUtil.getParameter(request, "channel_id", "");
		String name = ServletUtil.getParameter(request, "name", "");
		String accessType = ServletUtil.getParameter(request, "access_type", "");
		String accessCode = ServletUtil.getParameter(request, "access_code", "");
		String ownerAccountId = ServletUtil.getParameter(request, "owner_account_id", "");

		String message = "";
		if (channelId.isEmpty()) {
			MessageHelper.INSTANCE.add(message, "'channelId' parameter is not set.");
		}

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		boolean isChannelMetadataUpdated = false;
		boolean isRuntimeChannelUpdated = false;

		if (!channelId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
				if (dataCastIndexItem != null) {
					boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);

					if (isDataCastOnline) {
						String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

						DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
						DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver(indexServiceUrl);

						ChannelMetadata channelMetadata = InfraClientsHelper.DATA_CAST.getChannelMetadataByChannelId(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId);

						if (channelMetadata != null) {
							boolean isDataTubeChanged = false;
							String oldDataTubeId = channelMetadata.getDataTubeId();
							if (!dataTubeId.isEmpty() && !dataTubeId.equals(oldDataTubeId)) {
								isDataTubeChanged = true;
							}

							boolean updateName = false;
							String oldName = channelMetadata.getName();
							if (!name.isEmpty() && !name.equals(oldName)) {
								updateName = true;
							}

							boolean updateAccessType = false;
							String oldAccessType = channelMetadata.getAccessType();
							if ("public".equals(accessType) || "private".equals(accessType)) {
								if (!accessType.equals(oldAccessType)) {
									updateAccessType = true;
								}
							}

							boolean updateAccessCode = false;
							String oldAccessCode = channelMetadata.getAccessCode();
							if (!accessCode.equals(oldAccessCode)) {
								updateAccessCode = true;
							}

							boolean updateOwnerAccountId = false;
							String oldOwnerAccountId = channelMetadata.getOwnerAccountId();
							if (!ownerAccountId.equals(oldOwnerAccountId)) {
								updateOwnerAccountId = true;
							}

							isChannelMetadataUpdated = InfraClientsHelper.DATA_CAST.updateChannelMetadata(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId, isDataTubeChanged, dataTubeId, updateName, name, updateAccessType, accessType, updateAccessCode, accessCode, updateOwnerAccountId, ownerAccountId);

							if (isChannelMetadataUpdated) {
								IndexItem oldDataTubeIndexItem = InfraIndexItemHelper.getDataTubeIndexItem(indexServiceUrl, accessToken, dataCastId, oldDataTubeId);
								IndexItem newDataTubeIndexItem = InfraIndexItemHelper.getDataTubeIndexItem(indexServiceUrl, accessToken, dataCastId, dataTubeId);

								if (isDataTubeChanged) {
									boolean isOldRuntimeChannelRemoved = false;
									boolean isNewRuntimeChannelCreated = false;

									if (oldDataTubeIndexItem != null) {
										boolean isOldDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(oldDataTubeIndexItem);

										if (isOldDataTubeOnline) {
											String dataTubeServiceUrl = (String) oldDataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
											isOldRuntimeChannelRemoved = InfraClientsHelper.DATA_TUBE.deleteRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										} else {
											message = MessageHelper.INSTANCE.add(message, "DataTube service (dataTubeId='" + oldDataTubeId + "') is not online.");
										}
									} else {
										message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataTubeId='" + oldDataTubeId + "') is not found.");
									}

									if (newDataTubeIndexItem != null) {
										boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(newDataTubeIndexItem);

										if (isDataTubeOnline) {
											String dataTubeServiceUrl = (String) newDataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
											isNewRuntimeChannelCreated = InfraClientsHelper.DATA_TUBE.syncRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										} else {
											message = MessageHelper.INSTANCE.add(message, "DataTube service (dataTubeId='" + dataTubeId + "') is not online.");
										}
									} else {
										message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataTubeId='" + dataTubeId + "') is not found.");
									}

									if (isOldRuntimeChannelRemoved && isNewRuntimeChannelCreated) {
										isRuntimeChannelUpdated = true;
									}

								} else {
									if (newDataTubeIndexItem != null) {
										boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(newDataTubeIndexItem);

										if (isDataTubeOnline) {
											String dataTubeServiceUrl = (String) newDataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
											isRuntimeChannelUpdated = InfraClientsHelper.DATA_TUBE.syncRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);

										} else {
											message = MessageHelper.INSTANCE.add(message, "DataTube service (dataTubeId='" + dataTubeId + "') is not online.");
										}
									} else {
										message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataTubeId='" + dataTubeId + "') is not found.");
									}
								}
							}

						} else {
							message = MessageHelper.INSTANCE.add(message, "Channel metadata (channelId='" + channelId + "') is not found.");
						}
					} else {
						message = MessageHelper.INSTANCE.add(message, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataCast service (dataCastId='" + dataCastId + "') is not found.");
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			}
		}

		if (isChannelMetadataUpdated) {
			message = MessageHelper.INSTANCE.add(message, "Channe metadata is updated.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "hanne metadata is not updated.");
		}

		if (isRuntimeChannelUpdated) {
			message = MessageHelper.INSTANCE.add(message, "Runtime channe is updated.");
		} else {
			message = MessageHelper.INSTANCE.add(message, "Runtime Channe is not updated.");
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		HttpSession session = request.getSession(true);
		session.setAttribute("message", message);

		response.sendRedirect(contextRoot + "/admin/channelmetadatalist?dataCastId=" + dataCastId + "&groupBy=" + groupBy);
	}

}
