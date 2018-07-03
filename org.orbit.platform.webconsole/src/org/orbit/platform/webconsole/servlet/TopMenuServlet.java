package org.orbit.platform.webconsole.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.platform.webconsole.WebConstants;

/**
 * @see https://www.w3schools.com/howto/tryit.asp?filename=tryhow_css_dropdown_navbar
 * 
 * @see https://stackoverflow.com/questions/9110148/include-another-jsp-file
 * @see https://stackoverflow.com/questions/35455627/how-include-servlet-output-to-jsp-file
 * 
 */
public class TopMenuServlet extends HttpServlet {

	private static final long serialVersionUID = 6099729387833803371L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String platformContextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		String componentContextRoot = getServletConfig().getInitParameter(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);

		Map<String, String> servicesURLMap = new LinkedHashMap<String, String>();
		// hard code for now
		servicesURLMap.put("Index Service", platformContextRoot + "/indexservice");

		servicesURLMap.put("User Accounts", componentContextRoot + "/useraccounts");
		servicesURLMap.put("App Store", componentContextRoot + "/appstore");
		servicesURLMap.put("Domain", componentContextRoot + "/domain/machines");

		request.setAttribute("servicesURLMap", servicesURLMap);
		request.getRequestDispatcher(platformContextRoot + "/views/menu/top_menu_v2.jsp").include(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
