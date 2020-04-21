package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.origin.OSPage;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

public class OSWebApplication extends PlatformWebApplication {

	/**
	 * 
	 * @param initProperties
	 */
	public OSWebApplication(Map<Object, Object> initProperties) {
		super(initProperties);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.CUBE__WEB_CONSOLE_CONTEXT_ROOT, //
				ComponentConstants.ORBIT_IDENTITY_SERVICE_URL, //
				ComponentConstants.ORBIT_USER_ACCOUNTS_URL, //
				ComponentConstants.ORBIT_AUTH_URL, //
				ComponentConstants.ORBIT_DOMAIN_SERVICE_URL, //
				ComponentConstants.ORBIT_NODE_CONTROL_URL, //
				ComponentConstants.ORBIT_MISSION_CONTROL_URL, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		return "world";
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		dicts.put("contextPath", "world");

		// String bundlePrefix = "/org.orbit.component.webconsole";

		// Web resources
		// addResource(new ResourceMetadataImpl("/views/images", bundlePrefix + "/WEB-INF/views/images"));
		// addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		// addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		// addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Servlets
		addServlet(new ServletMetadataImpl("/cube/*", new OSPage(), dicts));

		// Add JSPs
		// addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
