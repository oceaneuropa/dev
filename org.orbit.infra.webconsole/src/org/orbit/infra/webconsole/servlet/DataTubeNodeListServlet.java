package org.orbit.infra.webconsole.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.datatube.DataTubeClientResolver;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexItemHelper;
import org.orbit.infra.api.util.InfraClientsHelper;
import org.orbit.infra.io.IConfigElement;
import org.orbit.infra.io.IConfigRegistry;
import org.orbit.infra.io.util.DataCastNodeConfigHelper;
import org.orbit.infra.io.util.DataCastIndexItemHelper;
import org.orbit.infra.io.util.DefaultDataTubeClientResolver;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.platform.sdk.util.OrbitTokenUtil;
import org.origin.common.servlet.MessageHelper;
import org.origin.common.util.ServletUtil;

public class DataTubeNodeListServlet extends HttpServlet {

	private static final long serialVersionUID = -7506275257981482773L;

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

		String dataCastId = ServletUtil.getParameter(request, "dataCastId", "");

		// ---------------------------------------------------------------
		// Handle data
		// ---------------------------------------------------------------
		IConfigElement dataCastConfigElement = null;
		IConfigElement[] configElements = null;

		if (!dataCastId.isEmpty()) {
			try {
				String accessToken = OrbitTokenUtil.INSTANCE.getAccessToken(request);

				IConfigRegistry cfgReg = DataCastNodeConfigHelper.INSTANCE.getDataCastNodesConfigRegistry(accessToken, true);
				if (cfgReg != null) {
					dataCastConfigElement = DataCastNodeConfigHelper.INSTANCE.getDataCastConfigElement(cfgReg, dataCastId);
					if (dataCastConfigElement != null) {
						configElements = dataCastConfigElement.memberConfigElements();
					} else {
						message = MessageHelper.INSTANCE.add(message, "Config element for data cast node (dataCastId: '" + dataCastId + "') cannot be found.");
					}
				} else {
					message = MessageHelper.INSTANCE.add(message, "Config registry for '" + DataCastNodeConfigHelper.INSTANCE.getConfigRegistryName__DataCastNodes() + "' cannot be found or created.");
				}

				if (configElements != null) {
					Map<String, IndexItem> dataTubeIndexItemMap = DataCastIndexItemHelper.getDataTubeIndexItemsMap(indexServiceUrl, accessToken, dataCastId);

					DataTubeClientResolver clientResolver = new DefaultDataTubeClientResolver(indexServiceUrl);

					for (IConfigElement configElement : configElements) {
						String dataTubeId = configElement.getAttribute(InfraConstants.IDX_PROP__DATATUBE__ID, String.class);

						IndexItem dataTubeIndexItem = dataTubeIndexItemMap.get(dataTubeId);
						if (dataTubeIndexItem != null) {
							configElement.adapt(IndexItem.class, dataTubeIndexItem);

							boolean isOnline = IndexItemHelper.INSTANCE.isOnline(dataTubeIndexItem);
							if (isOnline) {
								try {
									String dataTubeServiceUrl = (String) dataTubeIndexItem.getProperties().get(InfraConstants.IDX_PROP__DATATUBE__BASE_URL);
									DataTubeServiceMetadata metadata = InfraClientsHelper.DATA_TUBE.getServiceMetadata(clientResolver, dataTubeServiceUrl, accessToken);
									if (metadata != null) {
										configElement.adapt(DataTubeServiceMetadata.class, metadata);
									}
								} catch (Exception e) {
									message = MessageHelper.INSTANCE.add(message, e.getMessage() + " dataTubeId: '" + dataTubeId + "'");
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
		request.setAttribute("dataCastId", dataCastId);
		if (dataCastConfigElement != null) {
			request.setAttribute("dataCastConfigElement", dataCastConfigElement);
		}
		request.setAttribute("configElements", configElements);

		request.getRequestDispatcher(contextRoot + "/views/admin_datatube_list.jsp").forward(request, response);
	}

}
