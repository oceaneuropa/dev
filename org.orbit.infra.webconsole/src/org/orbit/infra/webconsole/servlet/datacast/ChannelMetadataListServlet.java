package org.orbit.infra.webconsole.servlet.datacast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.ChannelMetadataComparator;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.api.util.RuntimeChannelComparator;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DataCastIndexItemHelper;
import org.orbit.infra.io.util.DataCastNodeConfigHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class ChannelMetadataListServlet extends HttpServlet {

	private static final long serialVersionUID = -4798595665452356829L;

	protected static ChannelMetadata[] EMPTY_CHANNEL_METADATAS = new ChannelMetadata[0];

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
		IConfigElement dataCastConfigElement = null;
		ChannelMetadata[] channelMetadatas = null;

		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = DataCastNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					dataCastConfigElement = DataCastNodeConfigHelper.INSTANCE.getDataCastConfigElement(cfgReg, dataCastId);
					if (dataCastConfigElement == null) {
						message = MessageHelper.INSTANCE.add(message, "Config element for data cast node (dataCastId: '" + dataCastId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + DataCastNodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be found or created.");
				}

				// Get DataCast service index item and check whether the service is online
				DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver(indexServiceUrl);
				DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver(indexServiceUrl);

				Map<String, IndexItem> dataCastIndexItemMap = DataCastIndexItemHelper.getDataCastIndexItemsMap(indexServiceUrl, accessToken);
				IndexItem dataCastIndexItem = dataCastIndexItemMap.get(dataCastId);

				if (dataCastIndexItem != null) {
					String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
					boolean isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);

					if (isDataCastOnline) {
						channelMetadatas = InfraClientsHelper.DATA_CAST.getChannelMetadatas(dataCastClientResolver, dataCastServiceUrl, accessToken, ChannelMetadataComparator.ASC);

						if (channelMetadatas != null) {
							// Get service metadata of DataTubes which belong to the DataCast
							Map<String, DataTubeServiceMetadata> dataTubeServiceMetadataMap = new HashMap<String, DataTubeServiceMetadata>();
							Map<String, Map<String, RuntimeChannel>> dataTubeRuntimeChannelsMap = new HashMap<String, Map<String, RuntimeChannel>>();

							Map<String, IndexItem> dataTubeIndexItemMap = DataCastIndexItemHelper.getDataTubeIndexItemsMap(indexServiceUrl, accessToken, dataCastId);
							for (Iterator<String> dataTubeItor = dataTubeIndexItemMap.keySet().iterator(); dataTubeItor.hasNext();) {
								String dataTubeId = dataTubeItor.next();

								// Get DataTube service index item and check whether the service is online
								IndexItem dataTubeIndexItem = dataTubeIndexItemMap.get(dataTubeId);

								if (dataTubeIndexItem != null) {
									String dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__BASE_URL);
									boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);

									// Get DataTube service metadata
									if (isDataTubeOnline) {
										try {
											DataTubeServiceMetadata metadata = InfraClientsHelper.DATA_TUBE.getServiceMetadata(dataTubeClientResolver, dataTubeServiceUrl, accessToken);
											if (metadata != null) {
												dataTubeServiceMetadataMap.put(dataTubeId, metadata);

												Map<String, RuntimeChannel> runtimeChannelsMap = InfraClientsHelper.DATA_TUBE.getRuntimeChannelsMap(dataTubeClientResolver, dataTubeServiceUrl, accessToken, RuntimeChannelComparator.ASC);
												dataTubeRuntimeChannelsMap.put(dataTubeId, runtimeChannelsMap);
											}
										} catch (Exception e) {
											message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dataTubeId: '" + dataTubeId + "'");
											e.printStackTrace();
										}
									}
								}
							}

							// Set DataTube service metadata and RuntimeChannel to ChannelMetadatas
							for (ChannelMetadata channelMetadata : channelMetadatas) {
								String channelId = channelMetadata.getChannelId();
								String dataTubeId = channelMetadata.getDataTubeId();

								DataTubeServiceMetadata metadata = dataTubeServiceMetadataMap.get(dataTubeId);
								if (metadata != null) {
									channelMetadata.adapt(DataTubeServiceMetadata.class, metadata);
								}

								Map<String, RuntimeChannel> runtimeChannelsMap = dataTubeRuntimeChannelsMap.get(dataTubeId);
								if (runtimeChannelsMap != null) {
									RuntimeChannel runtimeChannel = runtimeChannelsMap.get(channelId);
									if (runtimeChannel != null) {
										channelMetadata.adapt(RuntimeChannel.class, runtimeChannel);
									}
								}
							}
						}

					} else {
						message = MessageHelper.INSTANCE.add(message, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataCast service (dataCastId='" + dataCastId + "') cannot be found.");
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (channelMetadatas == null) {
			channelMetadatas = EMPTY_CHANNEL_METADATAS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dataCastId", dataCastId);
		if (dataCastConfigElement != null) {
			request.setAttribute("dataCastConfigElement", dataCastConfigElement);
		}
		request.setAttribute("channelMetadatas", channelMetadatas);

		request.getRequestDispatcher(contextRoot + "/views/admin_channel_metadata_list.jsp").forward(request, response);
	}

}
