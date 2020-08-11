package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.tier1.SignInPage;
import org.orbit.component.webconsole.servlet.tier1.SignInServlet;
import org.orbit.component.webconsole.servlet.tier1.SignUpPage;
import org.orbit.component.webconsole.servlet.tier1.SignUpServlet;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

public class PublicWebApplication extends PlatformWebApplication {

	/**
	 * 
	 * @param initProperties
	 */
	public PublicWebApplication(Map<Object, Object> initProperties) {
		super(initProperties);
	}

	@Override
	protected String[] getPropertyNames() {
		String[] propNames = new String[] { //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.PUBLIC_WEB_CONSOLE_APPS_FOLDER, //
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
		String contextRoot = (String) this.getProperties().get(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		String bundlePrefix = "/org.orbit.component.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/images/apps", bundlePrefix + "/WEB-INF/images/apps"));
		addResource(new ResourceMetadataImpl("/apps", bundlePrefix + "/WEB-INF/apps"));

		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Identity
		addServlet(new ServletMetadataImpl("/signup", new SignUpPage(), dicts)); // public
		addServlet(new ServletMetadataImpl("/signin", new SignInPage(), dicts)); // public
		addServlet(new ServletMetadataImpl("/signup_req", new SignUpServlet(), dicts)); // public
		addServlet(new ServletMetadataImpl("/signin_req", new SignInServlet(), dicts)); // public

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}

// InfraConstants.ORBIT_INDEX_SERVICE_URL, //
// InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
// ComponentConstants.ORBIT_REGISTRY_URL, //
// ComponentConstants.ORBIT_APP_STORE_URL, //