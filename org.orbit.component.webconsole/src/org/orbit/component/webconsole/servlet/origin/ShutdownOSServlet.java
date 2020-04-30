package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

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
import org.orbit.spirit.api.gaia.EntityMetadata;
import org.orbit.spirit.api.gaia.GaiaClientResolver;
import org.orbit.spirit.api.util.SpiritClientsUtil;
import org.orbit.spirit.io.util.DefaultEarthClientResolver;
import org.orbit.spirit.io.util.DefaultGaiaClientResolver;
import org.orbit.spirit.io.util.SpiritIndexItemHelper;
import org.origin.common.model.StatusImpl;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ShutdownOSServlet extends HttpServlet {

	private static final long serialVersionUID = -5693425709887720168L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String gaiaId = ServletUtil.getParameter(request, "gaiaId", "gaia1");
		String name = ServletUtil.getParameter(request, "name", "");

		String message = "";
		if (name.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, "OS name cannot be empty.");
			session.setAttribute("message", message);
			session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
			response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);
			return;
		}

		String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
		GaiaClientResolver gaiaClientResolver = new DefaultGaiaClientResolver();
		EarthClientResolver earthClientResolver = new DefaultEarthClientResolver();

		// see EntityMetadataActionServlet and EntityMetadataDeleteServlet
		try {
			IndexItem gaiaIndexItem = SpiritIndexItemHelper.getGaiaIndexItem(accessToken, gaiaId);
			if (gaiaIndexItem != null) {
				boolean isGAIAOnline = IndexItemHelper.INSTANCE.isOnline(gaiaIndexItem);

				if (isGAIAOnline) {
					String gaiaServiceUrl = (String) gaiaIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

					EntityMetadata entityMetadata = SpiritClientsUtil.GAIA.getEntityMetadataByName(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, name);
					if (entityMetadata == null) {
						message = MessageHelper.INSTANCE.add(message, "OS is not found.");
						session.setAttribute("message", message);
						session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
						response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);
						return;

					} else {
						String earthId = entityMetadata.getEarthId();
						String entityId = entityMetadata.getEntityId();

						// Stop metadata
						SpiritClientsUtil.GAIA.setEntityMetadataStatusById(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId, StatusImpl.STOPPED, false);

						// Stop instance
						IndexItem earthIndexItem = SpiritIndexItemHelper.getEarthIndexItem(accessToken, gaiaId, earthId);
						if (earthIndexItem != null) {
							boolean isEarthOnline = IndexItemHelper.INSTANCE.isOnline(earthIndexItem);
							if (isEarthOnline) {
								String earthServiceUrl = (String) earthIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
								SpiritClientsUtil.EARTH.stopInstance(earthClientResolver, earthServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
							}
						}

						// Delete metadata
						SpiritClientsUtil.GAIA.deleteEntityMetadataById(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, entityId);
					}

				} else {
					message = MessageHelper.INSTANCE.add(message, "GAIA is offline.");
					session.setAttribute("message", message);
					session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
					response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);
					return;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			session.setAttribute("message", message);
			session.setAttribute("redirectURL", originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
			response.sendRedirect(originContextRoot + WebConstants.ORIGIN_MESSAGE_PAGE_PATH);
			return;
		}

		response.sendRedirect(originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
	}

}
