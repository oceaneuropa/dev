package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.glasscube.api.GlassCubeManagerClientResolver;
import org.orbit.glasscube.api.util.CubeClientsUtil;
import org.orbit.glasscube.io.util.DefaultGlassCubeManagerClientResolver;
import org.orbit.glasscube.io.util.GlassCubeIndexItemHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class OSExitServlet extends HttpServlet {

	private static final long serialVersionUID = -3970512727450849563L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String cubeManagerId = ServletUtil.getParameter(request, "cubeManagerId", "");
		String cubeId = ServletUtil.getParameter(request, "cubeId", "");
		String message = "";
		String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

		// see CapsuleServlet
		try {
			GlassCubeManagerClientResolver clientResolver = new DefaultGlassCubeManagerClientResolver();

			IndexItem cubeManagerIndexItem = GlassCubeIndexItemHelper.INSTANCE.getCubeManagerIndexItem(accessToken, cubeManagerId);
			if (cubeManagerIndexItem != null) {
				boolean isCubeManagerOnline = IndexItemHelper.INSTANCE.isOnline(cubeManagerIndexItem);

				if (isCubeManagerOnline) {
					String cubeManagerWebServiceUrl = (String) cubeManagerIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
					boolean succeed = CubeClientsUtil.CUBE_MANAGER.leave(clientResolver, cubeManagerWebServiceUrl, accessToken, cubeId);
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

		// window.top.location.href = "http://www.example.com";
		response.sendRedirect(originContextRoot + WebConstants.ORIGIN_HOME_PAGE_PATH);
	}

}
