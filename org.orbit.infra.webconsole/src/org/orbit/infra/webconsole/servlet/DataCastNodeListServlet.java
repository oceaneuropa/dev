package org.orbit.infra.webconsole.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datacast.DataCastClientResolver;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DataCastClientResolverImpl;
import org.orbit.infra.io.util.DataCastHelper;
import org.orbit.infra.io.util.DataCastUtil;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;

public class DataCastNodeListServlet extends HttpServlet {

	private static final long serialVersionUID = -4942763685721798471L;

	protected static IConfigElement[] EMPTY_CONFIG_ELEMENTS = new IConfigElement[] {};

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

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement[] configElements = null;
		try {
			String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

			// 1. Get or create config registry "DataCastNodes"
			// - get root IConfigElements from it
			IConfigRegistry cfgReg = DataCastHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
			if (cfgReg == null) {
				message = MessageHelper.INSTANCE.add(message, "Config registry with name '" + DataCastHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be retrieved or created.");
			} else {
				configElements = cfgReg.listRootConfigElements();
			}

			// 2. Each IConfigElement represents one DataCast node
			// - find IndexItem and DataCastServiceMetadata and attach them (if available) to IConfigElement.
			if (configElements != null) {
				Map<String, IndexItem> dataCastIndexItemMap = DataCastUtil.getDataCastIndexItemsMap(indexServiceUrl, accessToken);

				DataCastClientResolver clientResolver = new DataCastClientResolverImpl(indexServiceUrl);

				for (IConfigElement configElement : configElements) {
					String dataCastId = configElement.getAttribute(InfraConstants.IDX_PROP__DATACAST__ID, String.class);

					IndexItem dataCastIndexItem = dataCastIndexItemMap.get(dataCastId);
					if (dataCastIndexItem != null) {
						configElement.adapt(IndexItem.class, dataCastIndexItem);

						boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataCastIndexItem);
						if (isOnline) {
							try {
								String dataCastServiceUrl = (String) dataCastIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATACAST__BASE_URL);
								DataCastServiceMetadata dataCastServiceMetadata = InfraClientsUtil.DATA_CAST.getServiceMetadata(clientResolver, dataCastServiceUrl, accessToken);
								if (dataCastServiceMetadata != null) {
									configElement.adapt(DataCastServiceMetadata.class, dataCastServiceMetadata);
								}
							} catch (Exception e) {
								message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dataCastId: '" + dataCastId + "'");
								e.printStackTrace();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			message = MessageHelper.INSTANCE.add(message, "Exception occurs: '" + e.getMessage() + "'.");
			e.printStackTrace();
		}

		if (configElements == null) {
			configElements = EMPTY_CONFIG_ELEMENTS;
		}

		// ---------------------------------------------------------------
		// Render data
		// ---------------------------------------------------------------
		if (message != null) {
			request.setAttribute("message", message);
		}
		request.setAttribute("configElements", configElements);

		request.getRequestDispatcher(contextRoot + "/views/admin_datacast_list.jsp").forward(request, response);
	}

}

// protected static List<IndexItem> EMPTY_INDEX_ITEMS = new ArrayList<IndexItem>();

// if (dataCastIndexItems == null) {
// dataCastIndexItems = EMPTY_INDEX_ITEMS;
// }

// List<IndexItem> dataCastIndexItems = null;
// Map<String, DataCastServiceMetadata> dataCastIdToServiceMetadata = new HashMap<String, DataCastServiceMetadata>();
// dataCastIndexItems = DataCastUtil.getDataCastIndexItemsList(indexServiceUrl, accessToken);

// dataCastIdToServiceMetadata.put(dataCastId, dataCastServiceMetadata);

// request.setAttribute("dataCastIndexItems", dataCastIndexItems);
// request.setAttribute("dataCastIdToServiceMetadata", dataCastIdToServiceMetadata);

// ServiceMetadata metadata = cfg.getServiceServiceMetadata();
// System.out.println("metadata = \r\n" + metadata);
