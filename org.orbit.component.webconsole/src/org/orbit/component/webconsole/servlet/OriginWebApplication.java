package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.origin.CreateNewAccountPage;
import org.orbit.component.webconsole.servlet.origin.CreateNewAccountServlet;
import org.orbit.component.webconsole.servlet.origin.CreateNewOSInstancePage;
import org.orbit.component.webconsole.servlet.origin.CreateNewOSInstanceServlet;
import org.orbit.component.webconsole.servlet.origin.HomePage;
import org.orbit.component.webconsole.servlet.origin.LoginServlet;
import org.orbit.component.webconsole.servlet.origin.LogoutServlet;
import org.orbit.component.webconsole.servlet.origin.MainPage;
import org.orbit.component.webconsole.servlet.origin.MessagePage;
import org.orbit.component.webconsole.servlet.origin.OSPage;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

public class OriginWebApplication extends PlatformWebApplication {

	/**
	 * 
	 * @param initProperties
	 */
	public OriginWebApplication(Map<Object, Object> initProperties) {
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
		String contextRoot = (String) this.getProperties().get(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.ORIGIN__WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		String bundlePrefix = "/org.orbit.component.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/images", bundlePrefix + "/WEB-INF/views/images"));
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Origin
		addServlet(new ServletMetadataImpl("/", new MainPage(), dicts));
		addServlet(new ServletMetadataImpl("/message", new MessagePage(), dicts));
		addServlet(new ServletMetadataImpl("/loginHandler", new LoginServlet(), dicts));
		addServlet(new ServletMetadataImpl("/logoutHandler", new LogoutServlet(), dicts));
		addServlet(new ServletMetadataImpl(WebConstants.ORIGIN_CREATE_NEW_ACCOUNT_PAGE_PATH, new CreateNewAccountPage(), dicts));
		addServlet(new ServletMetadataImpl(WebConstants.ORIGIN_CREATE_NEW_ACCOUNT_HANDLER_PATH, new CreateNewAccountServlet(), dicts));

		addServlet(new ServletMetadataImpl(WebConstants.ORIGIN_HOME_PAGE_PATH, new HomePage(), dicts));
		addServlet(new ServletMetadataImpl(WebConstants.ORIGIN_CREATE_NEW_OS_PAGE_PATH, new CreateNewOSInstancePage(), dicts));
		addServlet(new ServletMetadataImpl(WebConstants.ORIGIN_CREATE_NEW_OS_HANDLER_PATH, new CreateNewOSInstanceServlet(), dicts));

		// https://stackoverflow.com/questions/8715474/servlet-and-path-parameters-like-xyz-value-test-how-to-map-in-web-xml
		addServlet(new ServletMetadataImpl("/OS/*", new OSPage(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
