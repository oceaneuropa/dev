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
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.infra.api.util.RuntimeChannelComparator;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.io.util.InfraIndexItemHelper;
import org.orbit.infra.io.util.NodeConfigHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class RuntimeChannelListServlet extends HttpServlet {

	private static final long serialVersionUID = -5671452954042682351L;

	protected static RuntimeChannel[] EMPTY_RUNTIME_CHANNELS = new RuntimeChannel[0];

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ---------------------------------------------------------------
		// Get parameters
		// ---------------------------------------------------------------
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
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
		String dataTubeId = ServletUtil.getParameter(request, "dataTubeId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		DataCastServiceMetadata dataCastServiceMetadata = null;
		DataTubeServiceMetadata dataTubeServiceMetadata = null;
		IConfigElement dataCastConfigElement = null;
		IConfigElement dataTubeConfigElement = null;
		RuntimeChannel[] runtimeChannels = null;

		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = NodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					dataCastConfigElement = NodeConfigHelper.INSTANCE.getDataCastConfigElement(cfgReg, dataCastId);
					dataTubeConfigElement = NodeConfigHelper.INSTANCE.getDataTubeConfigElement(cfgReg, dataCastId, dataTubeId);

					if (dataCastConfigElement == null) {
						message = MessageHelper.INSTANCE.add(message, "Config element for data cast node (dataCastId: '" + dataCastId + "') cannot be found.");
					}
					if (dataTubeConfigElement == null) {
						message = MessageHelper.INSTANCE.add(message, "Config element for data tube node (dataCastId: '" + dataCastId + "'; dataTubeId: '" + dataTubeId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + NodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be found or created.");
				}

				DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver();
				DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver();

				// Get DataCast service index item and check whether the service is online
				boolean isDataCastOnline = false;
				String dataCastServiceUrl = null;
				IndexItem dataCastIndexItem = InfraIndexItemHelper.getDataCastIndexItem(accessToken, dataCastId);
				if (dataCastIndexItem != null) {
					dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
					isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
					if (isDataCastOnline) {
						dataCastServiceMetadata = InfraClientsUtil.DATA_CAST.getServiceMetadata(dataCastClientResolver, dataCastServiceUrl, accessToken);
					} else {
						message = MessageHelper.INSTANCE.add(message, "DataCast service (dataCastId='" + dataCastId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataCast service (dataCastId='" + dataCastId + "') cannot be found.");
				}

				// Get DataTube service index item and check whether the service is online
				IndexItem dataTubeIndexItem = InfraIndexItemHelper.getDataTubeIndexItem(accessToken, dataCastId, dataTubeId);
				if (dataTubeIndexItem != null) {
					String dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
					boolean isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);

					// Get runtime channels from DataTube service
					if (isDataTubeOnline) {
						try {
							dataTubeServiceMetadata = InfraClientsUtil.DATA_TUBE.getServiceMetadata(dataTubeClientResolver, dataTubeServiceUrl, accessToken);
							runtimeChannels = InfraClientsUtil.DATA_TUBE.getRuntimeChannels(dataTubeClientResolver, dataTubeServiceUrl, accessToken, RuntimeChannelComparator.ASC);

							if (isDataCastOnline) {
								for (RuntimeChannel runtimeChannel : runtimeChannels) {
									String channelId = runtimeChannel.getChannelId();

									// Get channel metadata of the runtime channel from DataCast service
									try {
										ChannelMetadata channelMetadata = InfraClientsUtil.DATA_CAST.getChannelMetadataByChannelId(dataCastClientResolver, dataCastServiceUrl, accessToken, channelId);
										if (channelMetadata != null) {
											runtimeChannel.adapt(ChannelMetadata.class, channelMetadata);
										}
									} catch (Exception e) {
										message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dataCastId: '" + dataCastId + "'");
										e.printStackTrace();
									}
								}
							}
						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dataTubeId: '" + dataTubeId + "'");
							e.printStackTrace();
						}
					} else {
						message = MessageHelper.INSTANCE.add(message, "DataTube service (dataCastId='" + dataCastId + "'; dataTubeId='" + dataTubeId + "') is not online.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "IndexItem for DataTube service (dataCastId='" + dataCastId + "'; dataTubeId='" + dataTubeId + "') cannot be found.");
				}
			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, e.getMessage());
				e.printStackTrace();
			}
		}

		if (runtimeChannels == null) {
			runtimeChannels = EMPTY_RUNTIME_CHANNELS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dataCastId", dataCastId);
		request.setAttribute("dataTubeId", dataTubeId);
		if (dataCastServiceMetadata != null) {
			request.setAttribute("dataCastServiceMetadata", dataCastServiceMetadata);
		}
		if (dataTubeServiceMetadata != null) {
			request.setAttribute("dataTubeServiceMetadata", dataTubeServiceMetadata);
		}
		if (dataCastConfigElement != null) {
			request.setAttribute("dataCastConfigElement", dataCastConfigElement);
		}
		if (dataTubeConfigElement != null) {
			request.setAttribute("dataTubeConfigElement", dataTubeConfigElement);
		}
		request.setAttribute("runtimeChannels", runtimeChannels);

		request.getRequestDispatcher(contextRoot + "/views/admin_runtime_channel_list.jsp").forward(request, response);
	}

}
