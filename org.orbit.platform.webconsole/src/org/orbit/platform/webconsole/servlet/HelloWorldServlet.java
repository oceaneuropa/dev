package org.orbit.platform.webconsole.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.orbit.platform.webconsole.WebConstants;

/**
 * 
 * Servlet implementation class HelloWorldServlet
 * 
 * e.g. http://localhost:8001/orbit/webconsole/platform/hello
 * 
 * @see https://stackoverflow.com/questions/14665037/getting-the-init-parameters-in-a-servlet?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=
 *      google_rich_qa
 * @see http://www.hubberspot.com/2013/09/how-to-use-webinitparam-annotation-for.html
 * 
 */
@WebServlet(urlPatterns = { "/helloworld" }, initParams = { @WebInitParam(name = "user", value = "John", description = "some name") })
public class HelloWorldServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String indexServiceUrl = getServletConfig().getInitParameter(WebConstants.ORBIT_INDEX_SERVICE_URL);
		String contextRoot = getServletConfig().getInitParameter(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		// IndexService indexService = InfraClients.getInstance().getIndexService(indexServiceUrl);
		// System.out.println("indexService = " + indexService);

		String message = request.getParameter("message");
		if (message == null) {
			message = "n/a";
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try {
			out.println("<html>");
			out.println("<body>");
			out.println("<h2>Hello " + message + "</h2>");
			out.println("<p>indexServiceUrl = " + indexServiceUrl + "</p>");
			out.println("<p>contextRoot = " + contextRoot + "</p>");
			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

// response.getWriter().append("Served at: ").append(request.getContextPath());

// getServletContext().getInitParameterNames()
// getServletConfig().getInitParameterNames()
// orbit.index_service.url
// platform.web_console.context_root
// System.out.println("getServletContext().getInitParameterNames()");
// for (Enumeration<String> itor1 = getServletContext().getInitParameterNames(); itor1.hasMoreElements();) {
// String name = itor1.nextElement();
// System.out.println(" " + name);
// }
// System.out.println("getServletConfig().getInitParameterNames()");
// for (Enumeration<String> itor2 = getServletConfig().getInitParameterNames(); itor2.hasMoreElements();) {
// String name = itor2.nextElement();
// System.out.println(" " + name);
// }

// String email = getServletContext().getInitParameter("email");
// String user = getServletConfig().getInitParameter("user");
// System.out.println("user = " + user);
// System.out.println("email = " + email);
