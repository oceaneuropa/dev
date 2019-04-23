package org.orbit.component.webconsole.servlet.tier3.nodecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.component.api.tier2.appstore.AppManifest;
import org.orbit.component.api.util.ComponentClientsUtil;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.util.DefaultPlatformClientResolver;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformClientResolver;
import org.orbit.platform.api.ProgramManifest;
import org.orbit.platform.api.util.PlatformClientsUtil;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.ServletUtil;

public class NodesProgramBatchUninstallProviderServlet extends HttpServlet {

	private static final long serialVersionUID = -8226300280836492147L;

	private static String[] EMPTY_IDS = new String[] {};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// String indexServiceUrl = getServletConfig().getInitParameter(InfraConstants.ORBIT_INDEX_SERVICE_URL);
		// String appStoreUrl = getServletConfig().getInitParameter(ComponentConstants.ORBIT_APP_STORE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		String platformId = ServletUtil.getParameter(request, "platformId", "");
		String[] nodeIds = ServletUtil.getParameterValues(request, "id", EMPTY_IDS);

		List<AppManifest> appManifests = new ArrayList<AppManifest>();
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);
			PlatformClientResolver platformClientResolver = new DefaultPlatformClientResolver(accessToken);

			List<String> programIdAndVersionList = new ArrayList<String>();
			for (String nodeId : nodeIds) {
				IndexItem nodeIndexItem = InfraClientsUtil.INDEX_SERVICE.getIndexItem(accessToken, platformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (nodeIndexItem == null) {
					continue;
				}
				boolean isNodeOnline = IndexItemHelper.INSTANCE.isOnline(nodeIndexItem);
				if (!isNodeOnline) {
					continue;
				}

				ProgramManifest[] currPrograms = PlatformClientsUtil.INSTANCE.getPrograms(platformClientResolver, platformId, nodeId, InfraConstants.PLATFORM_TYPE__NODE);
				if (currPrograms != null) {
					for (ProgramManifest currProgram : currPrograms) {
						String programId = currProgram.getId();
						String programVersion = currProgram.getVersion();

						String programIdAndVersion = programId + "|" + programVersion;
						if (!programIdAndVersionList.contains(programIdAndVersion)) {
							programIdAndVersionList.add(programIdAndVersion);
						}
					}
				}
			}

			for (String programIdAndVersion : programIdAndVersionList) {
				int index = programIdAndVersion.indexOf("|");
				String programId = programIdAndVersion.substring(0, index);
				String programVersion = programIdAndVersion.substring(index + 1);

				AppManifest appManifest = ComponentClientsUtil.AppStore.getApp(accessToken, programId, programVersion);
				if (appManifest != null) {
					appManifests.add(appManifest);
				}
			}

		} catch (ClientException e) {
			e.printStackTrace();
		}

		request.setAttribute("appManifests", appManifests.toArray(new AppManifest[appManifests.size()]));
		request.getRequestDispatcher(contextRoot + "/views/programs_provider.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
