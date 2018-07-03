package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.appstore.AppStoreServlet;
import org.orbit.component.webconsole.servlet.domain.DomainMachineAddServlet;
import org.orbit.component.webconsole.servlet.domain.DomainMachineDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.DomainMachineListServlet;
import org.orbit.component.webconsole.servlet.domain.DomainMachineUpdateServlet;
import org.orbit.component.webconsole.servlet.domain.DomainNodeCreateServlet;
import org.orbit.component.webconsole.servlet.domain.DomainNodeDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.DomainNodeListServlet;
import org.orbit.component.webconsole.servlet.domain.DomainNodeUpdateServlet;
import org.orbit.component.webconsole.servlet.domain.DomainPlatformAddServlet;
import org.orbit.component.webconsole.servlet.domain.DomainPlatformDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.DomainPlatformListServlet;
import org.orbit.component.webconsole.servlet.domain.DomainPlatformUpdateServlet;
import org.orbit.component.webconsole.servlet.useraccount.UserAccountAddServlet;
import org.orbit.component.webconsole.servlet.useraccount.UserAccountDeleteServlet;
import org.orbit.component.webconsole.servlet.useraccount.UserAccountListServlet;
import org.orbit.component.webconsole.servlet.useraccount.UserAccountUpdateServlet;
import org.orbit.service.servlet.WebApplicationImpl;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.osgi.framework.BundleContext;

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
				OrbitConstants.ORBIT_USER_ACCOUNTS_URL, //
				OrbitConstants.ORBIT_AUTH_URL, //
				OrbitConstants.ORBIT_REGISTRY_URL, //
				OrbitConstants.ORBIT_APP_STORE_URL, //
				OrbitConstants.ORBIT_DOMAIN_SERVICE_URL, //
				OrbitConstants.ORBIT_NODE_CONTROL_URL, //
				OrbitConstants.ORBIT_MISSION_CONTROL_URL, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = (String) this.getProperties().get(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		String contextRoot = (String) this.properties.get(WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT);
		dicts.put("contextPath", contextRoot);

		// Add resources
		addResource(new ResourceMetadataImpl("/views/css", "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/js", "/WEB-INF/views/js"));

		// Add servlets
		addServlet(new ServletMetadataImpl("/useraccounts", new UserAccountListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountadd", new UserAccountAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountupdate", new UserAccountUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountdelete", new UserAccountDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/machines", new DomainMachineListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineadd", new DomainMachineAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineupdate", new DomainMachineUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machinedelete", new DomainMachineDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/platforms", new DomainPlatformListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformadd", new DomainPlatformAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformupdate", new DomainPlatformUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformdelete", new DomainPlatformDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/nodes", new DomainNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodecreate", new DomainNodeCreateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeupdate", new DomainNodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodedelete", new DomainNodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestart", new DomainNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestop", new DomainNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestatus", new DomainNodeListServlet(), dicts));

		addServlet(new ServletMetadataImpl("/appstore", new AppStoreServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
