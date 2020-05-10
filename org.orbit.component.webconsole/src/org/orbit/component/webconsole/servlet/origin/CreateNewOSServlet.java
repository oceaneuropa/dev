package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
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
		HttpSession session = request.getSession(true);
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String gaiaId = ServletUtil.getParameter(request, "gaiaId", "gaia1");
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

		// see EntityMetadataAddServlet
		EntityMetadata entityMetadata = null;
		try {
			IndexItem gaiaIndexItem = SpiritIndexItemHelper.getGaiaIndexItem(accessToken, gaiaId);
			if (gaiaIndexItem != null) {
				boolean isGAIAOnline = IndexItemHelper.INSTANCE.isOnline(gaiaIndexItem);

				if (!isGAIAOnline) {
					message = MessageHelper.INSTANCE.add(message, "GAIA '" + gaiaId + "' is offline.");

				} else {
					String gaiaServiceUrl = (String) gaiaIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

					EntityMetadata existingEntity = SpiritClientsUtil.GAIA.getEntityMetadataByName(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, name);
					if (existingEntity != null) {
						message = MessageHelper.INSTANCE.add(message, "Entity '" + name + "' already exists.");

					} else {
						List<AccountConfig> accountConfigs = null;
						Map<String, Object> properties = null;
						entityMetadata = SpiritClientsUtil.GAIA.createEntityMetadata(gaiaClientResolver, gaiaServiceUrl, accessToken, null, WebConstants.TYPE__GLASS_CUBE, name, StatusImpl.STARTED, accessType, accessCode, accountConfigs, properties);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
		}

		EntityInstance entityInstance = null;
		try {
			if (entityMetadata != null) {
				String earthId = entityMetadata.getEarthId();
				String entityId = entityMetadata.getEntityId();

				IndexItem earthIndexItem = SpiritIndexItemHelper.getEarthIndexItem(accessToken, gaiaId, earthId);
				if (earthIndexItem != null) {
					boolean isEarthOnline = IndexItemHelper.INSTANCE.isOnline(earthIndexItem);

					if (isEarthOnline) {
						String earthServiceUrl = (String) earthIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

						SpiritClientsUtil.EARTH.startInstance(earthClientResolver, earthServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
						entityInstance = SpiritClientsUtil.EARTH.getInstanceById(earthClientResolver, earthServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
					} else {
						message = MessageHelper.INSTANCE.add(message, "Earth is offline.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
		}

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

}
