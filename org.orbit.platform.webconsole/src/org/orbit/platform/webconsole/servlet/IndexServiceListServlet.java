package org.orbit.platform.webconsole.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.infra.api.InfraClients;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.platform.webconsole.WebConstants;

/**
 * Servlet implementation class IndexServiceServlet
 * 
 * @see https://www.programmergate.com/pass-data-servlet-jsp/
 * 
 * @see https://www.w3schools.com/Css/css_table.asp
 * @see https://www.cssfontstack.com/Lucida-Grande
 * @see https://www.w3schools.com/cssref/pr_font_font.asp
 * @see https://www.w3schools.com/css/css_align.asp
 * @see https://www.w3schools.com/Css/css_padding.asp
 * 
 * @see https://jqueryui.com/tabs/
 * 
 */
public class IndexServiceListServlet extends ServiceAwareServlet {

	private static final long serialVersionUID = 5912224656258717859L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);

		// Map<String, String> indexerIdToName = new TreeMap<String, String>();
		// IExtension[] extensions = Activator.getInstance().getExtensionRegistry().getExtensions(InfraConstants.INDEX_PROVIDER_EXTENSION_TYPE_ID);
		// for (IExtension extension : extensions) {
		// String currIndexerId = extension.getId();
		// String currIndexerName = extension.getName();
		// indexerIdToName.put(currIndexerId, currIndexerName);
		// }

		Map<String, List<IndexItem>> indexerIdToIndexItems = new LinkedHashMap<String, List<IndexItem>>();
		IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
		if (indexService != null) {
			for (String indexerId : WebConstants.INDEXER_IDS) {
				List<IndexItem> indexItems = indexService.getIndexItems(indexerId);
				indexerIdToIndexItems.put(indexerId, indexItems);
			}
			// for (Iterator<String> idItor = indexerIdToName.keySet().iterator(); idItor.hasNext();) {
			// String indexerId = idItor.next();
			// List<IndexItem> indexItems = indexService.getIndexItems(indexerId);
			// indexerIdToIndexItems.put(indexerId, indexItems);
			// }
		}

		// request.setAttribute("indexerIdToName", indexerIdToName);
		request.setAttribute("indexerIdToIndexItems", indexerIdToIndexItems);
		request.getRequestDispatcher(contextRoot + "/views/index_service_v1.jsp").forward(request, response);
	}

}

// import javax.servlet.annotation.WebInitParam;
// import javax.servlet.annotation.WebServlet;

// @WebServlet(name = "IndexServiceServlet", urlPatterns = { "/indexservice" }, initParams = { @WebInitParam(name = "user", value = "John1", description = "some
// name") })

// System.out.println("indexService = " + indexService);
// request.setAttribute("name", "Tom Cat");

// String email = getServletContext().getInitParameter("email");
// String user = getServletConfig().getInitParameter("user");
// IndexService indexService = getServiceContext().get(IndexService.class);

// response.getWriter().append("Served at: ").append(request.getContextPath());

// e.g. "/orbit/webconsole/platform"
// System.out.println("contextRoot = " + contextRoot);

// String contextPath = request.getContextPath();
// System.out.println("contextPath = " + contextPath);

// response.sendRedirect(request.getContextPath() + "/index_service.jsp");
// response.sendRedirect("./index_service.jsp");
// response.sendRedirect("/orbit/webconsole/platform/views/helloWorld.jsp");

// ServletConfig servletConfig = getServletConfig();
// ServletContext servletContext = servletConfig.getServletContext();
// RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("./index.jsp");
// RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher("/orbit/webconsole/platform/views/helloWorld.jsp");
// RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(contextRoot + "/views/helloWorld.jsp");
// requestDispatcher.forward(request, response);

// int num = indexItems.size();
// System.out.println("Indexer: " + indexerId + " (num: " + num + ")");
// for (IndexItem indexItem : indexItems) {
// int id = indexItem.getIndexItemId();
// String name = indexItem.getName();
// String type = indexItem.getType();
// Map<String, Object> properties = indexItem.getProperties();
// String text = id + ", " + name + ", " + type;
// System.out.println("\t" + text);
// }

// getServletConfig().getServletContext().getRequestDispatcher(contextRoot + "/views/helloWorld.jsp").forward(request, response);

// @Override
// protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// doGet(request, response);
// }
