package org.orbit.infra.webconsole.servlet.datacast;

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
import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.io.util.DefaultDataCastClientResolver;
import org.orbit.infra.io.util.InfraIndexItemHelper;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;

public class DataCastNodeListServletV0 extends HttpServlet {

	private static final long serialVersionUID = -4942763685721798471L;

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

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		List<IndexItem> dataCastIndexItems = null;
		Map<String, DataCastServiceMetadata> dataCastIdToServiceMetadata = new HashMap<String, DataCastServiceMetadata>();

		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			dataCastIndexItems = InfraIndexItemHelper.getDataCastIndexItemsList(accessToken);

			DataCastClientResolver dataCastClientResolver = new DefaultDataCastClientResolver();
			for (IndexItem dataCastIndexItem : dataCastIndexItems) {
				String dataCastId = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__ID);
				String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.SERVICE__BASE_URL);

				DataCastServiceMetadata dataCastServiceMetadata = null;

				boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
				if (isOnline) {
					try {
						DataCastClient dataCastClient = dataCastClientResolver.resolve(dataCastServiceUrl, accessToken);
						if (dataCastClient != null) {
							dataCastServiceMetadata = dataCastClient.getMetadata();
						}
					} catch (Exception e) {
						message = MessageHelper.INSTANCE.add(message, e.getMessage());
					}
				}

				if (dataCastServiceMetadata != null) {
					dataCastIdToServiceMetadata.put(dataCastId, dataCastServiceMetadata);
				}
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}
		if (dataCastIndexItems == null) {
			dataCastIndexItems = new ArrayList<IndexItem>();
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("dataCastIndexItems", dataCastIndexItems);
		request.setAttribute("dataCastIdToServiceMetadata", dataCastIdToServiceMetadata);

		request.getRequestDispatcher(contextRoot + "/views/admin_datacast_list.jsp").forward(request, response);
	}

}

// String currHostUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__HOST_URL);
// String currContextRoot = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__CONTEXT_ROOT);
// String dataCastServiceUrl = WebServiceAwareHelper.INSTANCE.getURL(currHostUrl, currContextRoot);

// Map<String, PlatformServiceMetadata> dataCastIdToPlatformMetadata = new HashMap<String, PlatformServiceMetadata>();
// PlatformServiceMetadata platformMetadata = null;
// try {
// String platformId = (String) dataCastIndexItem.getProperties().get(PlatformConstants.PLATFORM_ID);
// if (platformId != null) {
// IndexItem platformIndexItem = OrbitClientHelper.INSTANCE.getPlatformIndexItem(indexServiceUrl, accessToken, platformId);
// if (platformIndexItem != null) {
// PlatformClient dataCastPlatformClient = OrbitClientHelper.INSTANCE.getPlatformClient(accessToken, platformIndexItem);
// if (dataCastPlatformClient != null) {
// platformMetadata = dataCastPlatformClient.getMetadata();
// }
// }
// }
// } catch (Exception e) {
// message = MessageHelper.INSTANCE.add(message, e.getMessage());
// }
// if (platformMetadata != null) {
// dataCastIdToPlatformMetadata.put(dataCastId, platformMetadata);
// }
// request.setAttribute("dataCastIdToPlatformMetadata", dataCastIdToPlatformMetadata);
