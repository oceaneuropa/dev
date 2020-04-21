package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.orbit.spirit.api.gaia.EntityMetadata;
import org.orbit.spirit.api.gaia.GaiaClientResolver;
import org.orbit.spirit.api.util.EntityMetadataComparator;
import org.orbit.spirit.api.util.SpiritClientsUtil;
import org.orbit.spirit.io.util.DefaultGaiaClientResolver;
import org.orbit.spirit.io.util.SpiritIndexItemHelper;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * @see CapsulexServlet
 */
public class OSPage extends HttpServlet {

	private static final long serialVersionUID = -545898425468066261L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mainContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
		String cubeContextRoot = getServletConfig().getInitParameter(WebConstants.CUBE__WEB_CONSOLE_CONTEXT_ROOT);

		String pathInfo = request.getPathInfo();

		String gaiaId = ServletUtil.getParameter(request, "gaiaId", "gaia1");
		String cubeName = ServletUtil.getParameter(request, "cubeName", "");
		if (pathInfo != null) {
			if (pathInfo.startsWith("/")) {
				pathInfo = pathInfo.substring(1);
			}
			cubeName = pathInfo;
		}

		Integer screenWidth = null;
		Integer screenHeight = null;
		String screenWidthStr = ServletUtil.getParameter(request, "screenWidth", "1440");
		String screenHeightStr = ServletUtil.getParameter(request, "screenHeight", "900");
		try {
			Integer value = Integer.valueOf(screenWidthStr);
			if (value != null) {
				screenWidth = value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Integer value = Integer.valueOf(screenHeightStr);
			if (value != null) {
				screenHeight = value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String cubeManagerId = null;
		String cubeId = null;

		String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
		GaiaClientResolver gaiaClientResolver = new DefaultGaiaClientResolver();
		IndexItem gaiaIndexItem = SpiritIndexItemHelper.getGaiaIndexItem(accessToken, gaiaId);
		if (gaiaIndexItem != null) {
			String gaiaServiceUrl = (String) gaiaIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
			boolean isGaiaOnline = IndexItemHelper.INSTANCE.isOnline(gaiaIndexItem);
			if (isGaiaOnline) {
				try {
					EntityMetadata[] entityMetadatas = SpiritClientsUtil.GAIA.getEntityMetadatas(gaiaClientResolver, gaiaServiceUrl, accessToken, WebConstants.TYPE__GLASS_CUBE, EntityMetadataComparator.ASC);
					for (EntityMetadata entityMetadata : entityMetadatas) {
						String currEarthId = entityMetadata.getEarthId();
						String currCubeId = entityMetadata.getEntityId();
						String currCubeName = entityMetadata.getName();

						if (cubeName.equals(currCubeName)) {
							cubeManagerId = currEarthId;
							cubeId = currCubeId;
							break;
						}
					}
				} catch (ClientException e) {
					e.printStackTrace();
				}
			}
		}

		if (cubeManagerId == null || cubeId == null) {
			// back to main landing page
			response.sendRedirect(mainContextRoot + "/home");

		} else {
			// to capsule page
			String iframeURL = cubeContextRoot + "/capsule";
			String queryParams = "";
			queryParams += "&cubeManagerId=" + cubeManagerId;
			queryParams += "&cubeId=" + cubeId;
			queryParams += "&action=join";

			if (screenWidth != null) {
				if (!queryParams.isEmpty()) {
					queryParams += "&";
				}
				queryParams += "screenWidth=" + screenWidth;
			}
			if (screenHeight != null) {
				if (!queryParams.isEmpty()) {
					queryParams += "&";
				}
				queryParams += "screenHeight=" + screenHeight;
			}
			if (!queryParams.isEmpty()) {
				iframeURL += "?" + queryParams;
			}

			response.addHeader("s", iframeURL);
			String content = "";
			content += "<!DOCTYPE HTML>";
			content += "<html lang=\"en\">";
			content += "<head>";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\">";
			content += "<title>Capsulex</title>";
			content += "<script type=\"text/javascript\" src=\"" + cubeContextRoot + "/js/capsulex.js\" defer></script>";
			content += "</head>";
			content += "<body>";
			content += "</body>";
			content += "</html>";
			response.getWriter().write(content);
			// response.getWriter().flush();
			// response.getWriter().close();
			// return;
		}
	}

}
