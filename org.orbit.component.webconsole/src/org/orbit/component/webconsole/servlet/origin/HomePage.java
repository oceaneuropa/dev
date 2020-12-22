package org.orbit.component.webconsole.servlet.origin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.webconsole.WebConstants;
import org.orbit.glasscube.api.GlassCube;
import org.orbit.glasscube.api.GlassCubeConstants;
import org.orbit.glasscube.api.GlassCubeManagerClientResolver;
import org.orbit.glasscube.api.util.CubeClientsUtil;
import org.orbit.glasscube.io.util.ConfigRegistryHelper;
import org.orbit.glasscube.io.util.DefaultGlassCubeManagerClientResolver;
import org.orbit.glasscube.io.util.GlassCubeIndexItemHelper;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * @see CubeManagerNodeListServlet and CapsuleListServlet
 */
public class HomePage extends HttpServlet {

	private static final long serialVersionUID = -8798612634123701761L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String originContextRoot = getServletConfig().getInitParameter(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);

		String message = "";
		String reqMessage = ServletUtil.getParameter(request, "message", "");
		if (reqMessage != null && !reqMessage.isEmpty()) {
			message = MessageHelper.INSTANCE.add(message, reqMessage);
		}

		List<GlassCube> allCubes = new ArrayList<GlassCube>();
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
			GlassCubeManagerClientResolver clientResolver = new DefaultGlassCubeManagerClientResolver();

			IConfigRegistry cfgReg = ConfigRegistryHelper.getGlassCubeManagerNodesConfigRegistry(accessToken);
			IConfigElement[] cubeManagers = (cfgReg != null) ? cfgReg.listRootElements() : null;
			if (cubeManagers != null) {
				Map<String, IndexItem> cubeManagerIndexItemMap = GlassCubeIndexItemHelper.INSTANCE.getCubeManagerIndexItemsMap(accessToken);

				for (IConfigElement cubeManager : cubeManagers) {
					String cubeManagerId = cubeManager.getAttribute(GlassCubeConstants.IDX_PROP__CUBE_MANAGER__ID, String.class);

					IndexItem cubeManagerIndexItem = cubeManagerIndexItemMap.get(cubeManagerId);
					if (cubeManagerIndexItem != null) {
						boolean isOnline = IndexItemHelper.INSTANCE.isOnline(cubeManagerIndexItem);
						if (isOnline) {
							String serviceUrl = (String) cubeManagerIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);
							try {
								GlassCube[] cubes = CubeClientsUtil.CUBE_MANAGER.getCubes(clientResolver, serviceUrl, accessToken, false);
								for (GlassCube cube : cubes) {
									// UserInfo[] users = CubeClientsUtil.CUBE_MANAGER.getUsers(clientResolver, serviceUrl, accessToken, cube.getId());
									// cube.adapt(UserInfo[].class, users);
									allCubes.add(cube);
								}
							} catch (ClientException e) {
								e.printStackTrace();
								message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
		}

		if (message != null && !message.isEmpty()) {
			request.setAttribute("message", message);
		}
		request.setAttribute("cubes", allCubes);

		request.getRequestDispatcher(originContextRoot + "/views/originHome.jsp").forward(request, response);
	}

}

// String cubeContextRoot = getServletConfig().getInitParameter(WebConstants.CUBE__WEB_CONSOLE_CONTEXT_ROOT);
// boolean isTokenValid = false;
// HttpSession session = request.getSession(false);
// if (session != null) {
// String username = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_USERNAME);
// String accessToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_ACCESS_TOKEN);
// String refreshToken = (String) session.getAttribute(PlatformConstants.SESSION__ORBIT_REFRESH_TOKEN);
// isTokenValid = ExtensionHelper.JWT.isTokenValid(PlatformConstants.TOKEN_PROVIDER__ORBIT, accessToken);
// }
// request.setAttribute("isTokenValid", isTokenValid);
