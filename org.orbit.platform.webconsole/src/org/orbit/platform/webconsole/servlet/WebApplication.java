package org.orbit.platform.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.webconsole.WebConstants;
import org.orbit.service.servlet.WebApplicationImpl;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

/**
 * <pre>
 * Example URLs:
 * http://localhost:8001/orbit/webconsole/platform/hello
 * http://localhost:8001/orbit/webconsole/platform/indexservice
 * </pre>
 * 
 * See org.eclipse.packagedrone.web.dispatcher.JspServletInitializer for usage of org.eclipse.equinox.jsp.jasper.JspServlet
 */
public class WebApplication extends WebApplicationImpl {

	/**
	 * 
	 * @param initProperties
	 */
	public WebApplication(Map<Object, Object> initProperties) {
		super(initProperties);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				WebConstants.ORBIT_INDEX_SERVICE_URL, //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.getProperties().get(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		// Add resources
		addResource(new ResourceMetadataImpl("/views/css", "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/js", "/WEB-INF/views/js"));
		// addResource(new ResourceMetadataImpl("/views/menu", "/WEB-INF/views/menu"));

		// Add servlets
		addServlet(new ServletMetadataImpl("/hello", new HelloWorldServlet(), dicts));
		addServlet(new ServletMetadataImpl("/top_menu", new TopMenuServlet(), dicts));
		addServlet(new ServletMetadataImpl("/top_message", new TopMessageServlet(), dicts));
		addServlet(new ServletMetadataImpl("/indexservice", new IndexServiceListServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views/menu", "/WEB-INF", dicts));
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views/dialog", "/WEB-INF", dicts));
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views/jquery-ui-dialog", "/WEB-INF", dicts));
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views/html5", "/WEB-INF", dicts));
	}

}

// Add resources
// addResource(new ResourceMetadataImpl("/", "/webapp"));
// addResource(new ResourceMetadataImpl("/index_service.jsp", "/webapp/index_service.jsp"));
// addResource(new ResourceMetadataImpl("/weblog", "/webapp/index.html"));

// Add JSPs
// JspServlet jspServlet = new JspServlet(this.bundle, "/WEB-INF/views", String.format("/bundle/%d/WEB-INF/views", this.bundle.getBundleId()));
// this.webContainer.registerServlet(jspServlet, new String[] { "*.jsp" }, null, this.httpContext);
// Bundle bundle = bundleContext.getBundle();
// long bundleId = bundle.getBundleId();
// String alias = "/bundle/" + bundleId + "/WEB-INF/views";
// addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/WEB-INF/", null, new String[] { "*.jsp" }));
// addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/orbit/webconsole/platform/views", "/WEB-INF", null, null));
