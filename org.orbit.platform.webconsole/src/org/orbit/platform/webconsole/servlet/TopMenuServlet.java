package org.orbit.platform.webconsole.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.platform.webconsole.WebConstants;

/**
 * @see https://stackoverflow.com/questions/9110148/include-another-jsp-file
 * @see https://stackoverflow.com/questions/35455627/how-include-servlet-output-to-jsp-file
 * 
 */
public class TopMenuServlet extends ServiceAwareServlet {

	private static final long serialVersionUID = 6099729387833803371L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		String contextRoot2 = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		Map<String, String> servicesURLMap = new LinkedHashMap<String, String>();
		// hard code for now
		servicesURLMap.put("Index Service", contextRoot + "/indexservice");
		servicesURLMap.put("User Registry", contextRoot2 + "/userregistry");
		servicesURLMap.put("Domain", contextRoot2 + "/domain");

		request.setAttribute("servicesURLMap", servicesURLMap);
		request.getRequestDispatcher(contextRoot + "/views/menu/top_menu.jsp").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
