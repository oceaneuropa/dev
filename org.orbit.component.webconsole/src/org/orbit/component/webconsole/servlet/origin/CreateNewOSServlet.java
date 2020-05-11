package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.datatube.api.datacast.ChannelMetadata;
import org.orbit.datatube.api.datacast.ChannelStatus;
import org.orbit.datatube.api.datacast.DataCastClient;
import org.orbit.datatube.api.datacast.DataCastClientResolver;
import org.orbit.datatube.api.datatube.DataTubeClientResolver;
import org.orbit.datatube.api.datatube.RuntimeChannel;
import org.orbit.datatube.api.util.DataTubeClientsUtil;
import org.orbit.datatube.io.util.DataTubeIndexItemHelper;
import org.orbit.datatube.io.util.DefaultDataCastClientResolver;
import org.orbit.datatube.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.spirit.api.earth.EarthClientResolver;
import org.orbit.spirit.api.earth.EntityInstance;
import org.orbit.spirit.api.gaia.EntityMetadata;
import org.orbit.spirit.api.gaia.GaiaClientResolver;
import org.orbit.spirit.api.util.SpiritClientsUtil;
import org.orbit.spirit.io.util.DefaultEarthClientResolver;
import org.orbit.spirit.io.util.DefaultGaiaClientResolver;
import org.orbit.spirit.io.util.SpiritIndexItemHelper;
import org.origin.common.model.AccountConfig;
import org.origin.common.model.StatusImpl;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class CreateNewOSServlet extends HttpServlet {

	private static final long serialVersionUID = -1737645312623318772L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Get parameters and prepare utils
		HttpSession session = request.getSession(true);
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String gaiaId = ServletUtil.getParameter(request, "gaiaId", "gaia1");
		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "cast1");
		String name = ServletUtil.getParameter(request, "name", "");
		String accessCode = ServletUtil.getParameter(request, "access_code", "");
		String accessType = ServletUtil.getParameter(request, "access_type", accessCode.isEmpty() ? "public" : "private");
		String message = "";

		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "Name cannot be empty.");
			session.setAttribute("message", message);
			session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_CREATE_NEW_OS_PAGE_PATH);
			response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);
			return;
		}

		String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

		GaiaClientResolver gaiaClientResolver = new DefaultGaiaClientResolver();
		EarthClientResolver earthClientResolver = new DefaultEarthClientResolver();
		DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver();
		DataTubeClientResolver dataTubeClientResolver = new DefaultDataTubeClientResolver();

		boolean isGAIAOnline = false;
		boolean isEarthOnline = false;
		boolean isDataCastOnline = false;
		boolean isDataTubeOnline = false;

		String gaiaServiceUrl = null;
		String earthServiceUrl = null;
		String dataCastServiceUrl = null;
		String dataTubeServiceUrl = null;

		ChannelMetadata newChannel = null;
		RuntimeChannel channelRuntime = null;
		EntityMetadata newEntity = null;
		EntityInstance entityInstance = null;
		boolean isChannelRuntimeStarted = false;
		boolean isEntityInstanceStarted = false;

		try {
			// 2. Get services
			// (1) Get GAIA
			IndexItem gaiaIndexItem = SpiritIndexItemHelper.getGaiaIndexItem(accessToken, gaiaId);
			if (gaiaIndexItem != null) {
				isGAIAOnline = IndexItemHelper.INSTANCE.isOnline(gaiaIndexItem);
				if (!isGAIAOnline) {
					message = MessageHelper.INSTANCE.add(message, "GAIA '" + gaiaId + "' is offline.");
				} else {
					gaiaServiceUrl = (String) gaiaIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
				}
			}

			// (2) Get DataCast
			IndexItem dataCastIndexItem = DataTubeIndexItemHelper.getDataCastIndexItem(accessToken, dataCastId);
			if (dataCastIndexItem != null) {
				isDataCastOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
				if (!isDataCastOnline) {
					message = MessageHelper.INSTANCE.add(message, "Hermes '" + dataCastId + "' is offline.");
				} else {
					dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
				}
			}

			// 3. Check Entity with same name
			boolean entityExist = false;
			if (isGAIAOnline) {
				EntityMetadata existingEntity = SpiritClientsUtil.GAIA.getEntityMetadataByName(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, name);
				if (existingEntity != null) {
					entityExist = true;
					message = MessageHelper.INSTANCE.add(message, "Entity '" + name + "' already exists.");
				}
			}

			// see EntityMetadataAddServlet for creating Entity metadata and create/start Entity instance.
			// see ChannelMetadataAddServlet for creating Channel metadata.
			// see ChannelMetadataActionServlet for starting Channel runtime.
			if (isGAIAOnline && isDataCastOnline && !entityExist) {
				// 3. Create a channel metadata and start the channel instance
				// (1) Create channel metadata
				String channelId = null;
				String dataTubeId = null;
				List<AccountConfig> accountConfigs = null;
				Map<String, Object> channelProperties = null;
				String ownerAccountId = null;

				// Note: Allocating DataTube is internal to Hermes (DataCast)
				newChannel = DataTubeClientsUtil.DATA_CAST.createChannelMetadata(dataCastClientResolver, dataCastServiceUrl, accessToken, null, name, ChannelStatus.STARTED, accessType, accessCode, ownerAccountId, accountConfigs, channelProperties);
				if (newChannel == null) {
					message = MessageHelper.INSTANCE.add(message, "DataTube cannot be allocated.");
				} else {
					channelId = newChannel.getChannelId();
					dataTubeId = newChannel.getDataTubeId();
				}

				// (2) Create/start channel runtime
				if (newChannel != null && dataTubeId != null) {
					IndexItem dataTubeIndexItem = DataTubeIndexItemHelper.getDataTubeIndexItem(accessToken, dataCastId, dataTubeId);
					if (dataTubeIndexItem != null) {
						isDataTubeOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);
						if (!isDataTubeOnline) {
							message = MessageHelper.INSTANCE.add(message, "DataTube '" + dataTubeId + "' is not online.");
						} else {
							dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

							// DataTubeClientsUtil.DATA_TUBE.getRuntimeChannelId(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId, createIfNotExist);
							isChannelRuntimeStarted = DataTubeClientsUtil.DATA_TUBE.startRuntimeChannelById(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId);
							if (!isChannelRuntimeStarted) {
								message = MessageHelper.INSTANCE.add(message, "Channel runtime '" + channelId + "' is not started.");
							}
							channelRuntime = DataTubeClientsUtil.DATA_TUBE.getRuntimeChannelId(dataTubeClientResolver, dataTubeServiceUrl, accessToken, channelId, false);
						}
					}
				}

				// 4. Create Entity metadata
				if (newChannel != null && dataTubeId != null && isChannelRuntimeStarted) {
					Map<String, Object> entityProperties = new HashMap<String, Object>();
					entityProperties.put("gaia.id", gaiaId);
					entityProperties.put("data_cast.id", dataCastId);
					if (dataTubeId != null) {
						entityProperties.put("data_tube.id", dataTubeId);
					}
					entityProperties.put("channel.id", channelId);

					// Note: Allocating Earth is internal to GAIA. Client doesn't need to involve knowing/allocating Earth.
					String earthId = null;
					newEntity = SpiritClientsUtil.GAIA.createEntityMetadata(gaiaClientResolver, gaiaServiceUrl, accessToken, earthId, WebConstants.TYPE__GLASS_CUBE, name, StatusImpl.STARTED, accessType, accessCode, (List<AccountConfig>) null, entityProperties);
				}
			}

			// 5. Create and start Entity instance
			if (newEntity != null && newChannel != null && channelRuntime != null) {
				String earthId = newEntity.getEarthId();
				String entityId = newEntity.getEntityId();

				IndexItem earthIndexItem = SpiritIndexItemHelper.getEarthIndexItem(accessToken, gaiaId, earthId);
				if (earthIndexItem != null) {
					isEarthOnline = IndexItemHelper.INSTANCE.isOnline(earthIndexItem);

					if (!isEarthOnline) {
						message = MessageHelper.INSTANCE.add(message, "Earth '" + earthId + "' is offline.");
					} else {
						earthServiceUrl = (String) earthIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

						isEntityInstanceStarted = SpiritClientsUtil.EARTH.startInstance(earthClientResolver, earthServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
						if (!isEntityInstanceStarted) {
							message = MessageHelper.INSTANCE.add(message, "Entity instance '" + entityId + "' is not started.");
						}
						entityInstance = SpiritClientsUtil.EARTH.getInstanceById(earthClientResolver, earthServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
		}

		// 6. Redirect page
		if (entityInstance == null) {
			// Failed to create entity (with cube type) instance
			session.setAttribute("message", message);
			session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_CREATE_NEW_OS_PAGE_PATH);
			response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);

		} else {
			name = entityInstance.getName();
			response.sendRedirect(originContextRoot + "/OS/" + name);
		}
	}

	/**
	 * 
	 * @param accessToken
	 * @param dataCastId
	 * @return
	 */
	protected String allocateDataTube(String accessToken, String dataCastId) {
		String dataTubeId = null;

		try {
			IndexItem dataCastIndexItem = DataTubeIndexItemHelper.getDataCastIndexItem(accessToken, dataCastId);
			if (dataCastIndexItem != null) {
				boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
				if (isOnline) {
					String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
					if (dataCastServiceUrl != null) {
						DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver();
						DataCastClient dataCastClient = dataCastClientResolver.resolve(dataCastServiceUrl, accessToken);

						if (dataCastClient != null) {
							dataTubeId = dataCastClient.allocateDataTube();
						}
					}
				}
			}

		} catch (ClientException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataTubeId;
	}

}
