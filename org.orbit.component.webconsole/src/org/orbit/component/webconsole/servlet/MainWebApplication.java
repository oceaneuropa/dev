package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.tier1.MainCreateNewAccountPage;
import org.orbit.component.webconsole.servlet.tier1.MainCreateNewAccountServlet;
import org.orbit.component.webconsole.servlet.tier1.MainLandingPage;
import org.orbit.component.webconsole.servlet.tier1.MainLoginServlet;
import org.orbit.component.webconsole.servlet.tier1.MainLogoutServlet;
import org.orbit.component.webconsole.servlet.tier1.MainPage;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

public class MainWebApplication extends PlatformWebApplication {

	/**
	 * 
	 * @param initProperties
	 */
	public MainWebApplication(Map<Object, Object> initProperties) {
		super(initProperties);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.MAIN_WEB_CONSOLE_CONTEXT_ROOT, //
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
		String contextRoot = (String) this.getProperties().get(WebConstants.MAIN_WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.MAIN_WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		String bundlePrefix = "/org.orbit.component.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/images", bundlePrefix + "/WEB-INF/views/images"));
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Main
		addServlet(new ServletMetadataImpl("/", new MainPage(), dicts));
		addServlet(new ServletMetadataImpl("/loginHandler", new MainLoginServlet(), dicts));
		addServlet(new ServletMetadataImpl("/logoutHandler", new MainLogoutServlet(), dicts));
		addServlet(new ServletMetadataImpl("/createNewAccountPage", new MainCreateNewAccountPage(), dicts));
		addServlet(new ServletMetadataImpl("/createNewAccountHandler", new MainCreateNewAccountServlet(), dicts));
		addServlet(new ServletMetadataImpl("/landingPage", new MainLandingPage(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
