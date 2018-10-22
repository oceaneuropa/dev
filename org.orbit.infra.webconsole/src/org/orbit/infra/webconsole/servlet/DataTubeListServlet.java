package org.orbit.infra.webconsole.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.util.DataCastUtil;
import org.orbit.infra.io.util.DataTubeClientResolverImpl;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataTubeListServlet extends HttpServlet {

	private static final long serialVersionUID = -7506275257981482773L;

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
		IndexItem dataCastIndexItem = null;
		List<IndexItem> dataTubeIndexItems = null;
		Map<String, DataTubeServiceMetadata> dataTubeIdToServiceMetadata = new HashMap<String, DataTubeServiceMetadata>();

		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				dataCastIndexItem = DataCastUtil.getDataCastIndexItem(indexServiceUrl, accessToken, dataCastId);
				dataTubeIndexItems = DataCastUtil.getDataTubeIndexItems(indexServiceUrl, accessToken, dataCastId);

				DataTubeClientResolver dataTubeClientResolver = new DataTubeClientResolverImpl(indexServiceUrl);

				for (IndexItem dataTubeIndexItem : dataTubeIndexItems) {
					String dataTubeId = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__ID);

					DataTubeServiceMetadata dataTubeServiceMetadata = null;

					boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);
					if (isOnline) {
						try {
							DataTubeClient dataTubeClient = dataTubeClientResolver.resolve(dataCastId, dataTubeId, accessToken);
							if (dataTubeClient != null) {
								dataTubeServiceMetadata = dataTubeClient.getMetadata();
							}
						} catch (Exception e) {
							message = MessageHelper.INSTANCE.add(message, e.getMessage());
						}
					}

					if (dataTubeServiceMetadata != null) {
						dataTubeIdToServiceMetadata.put(dataTubeId, dataTubeServiceMetadata);
					}
				}

			} catch (Exception e) {
				message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
				e.printStackTrace();
			}
		}

		if (dataTubeIndexItems == null) {
			dataTubeIndexItems = new ArrayList<IndexItem>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dataCastId", dataCastId);
		if (dataCastIndexItem != null) {
			request.setAttribute("dataCastIndexItem", dataCastIndexItem);
		}
		request.setAttribute("dataTubeIndexItems", dataTubeIndexItems);
		request.setAttribute("dataTubeIdToServiceMetadata", dataTubeIdToServiceMetadata);

		request.getRequestDispatcher(contextRoot + "/views/admin_datatube_list.jsp").forward(request, response);
	}

}

// Map<String, PlatformServiceMetadata> dataTubeIdToPlatformMetadata = new HashMap<String, PlatformServiceMetadata>();
// PlatformServiceMetadata platformMetadata = null;
// try {
// String platformId = (String) dataTubeIndexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
// if (platformId != null) {
// IndexItem platformIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(indexServiceUrl, accessToken, platformId);
// if (platformIndexItem != null) {
// PlatformClient platformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, platformIndexItem);
// if (platformClient != null) {
// platformMetadata = platformClient.getMetadata();
// }
// }
// }
// } catch (Exception e) {
// message = MessageHelper.INSTANCE.add(message, e.getMessage());
// }
// if (platformMetadata != null) {
// dataTubeIdToPlatformMetadata.put(dataTubeId, platformMetadata);
// }
// request.setAttribute("dataTubeIdToPlatformMetadata", dataTubeIdToPlatformMetadata);
