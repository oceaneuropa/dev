package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.appstore.AppStoreServlet;
import org.orbit.component.webconsole.servlet.domain.MachineAddServlet;
import org.orbit.component.webconsole.servlet.domain.MachineDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.MachineListServlet;
import org.orbit.component.webconsole.servlet.domain.MachineUpdateServlet;
import org.orbit.component.webconsole.servlet.domain.NodeCreateServlet;
import org.orbit.component.webconsole.servlet.domain.NodeDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.NodeListServlet;
import org.orbit.component.webconsole.servlet.domain.NodePropertyAddServlet;
import org.orbit.component.webconsole.servlet.domain.NodePropertyDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.NodePropertyListServlet;
import org.orbit.component.webconsole.servlet.domain.NodePropertyUpdateServlet;
import org.orbit.component.webconsole.servlet.domain.NodeStartServlet;
import org.orbit.component.webconsole.servlet.domain.NodeStopServlet;
import org.orbit.component.webconsole.servlet.domain.NodeUpdateServlet;
import org.orbit.component.webconsole.servlet.domain.PlatformAddServlet;
import org.orbit.component.webconsole.servlet.domain.PlatformDeleteServlet;
import org.orbit.component.webconsole.servlet.domain.PlatformListServlet;
import org.orbit.component.webconsole.servlet.domain.PlatformPropertyListServlet;
import org.orbit.component.webconsole.servlet.domain.PlatformUpdateServlet;
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
				WebConstants.ORBIT_EXTENSION_REGISTRY_URL, //
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
		addResource(new ResourceMetadataImpl("/views/icons", "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", "/WEB-INF/views/js"));

		// Add servlets
		addServlet(new ServletMetadataImpl("/useraccounts", new UserAccountListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountadd", new UserAccountAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountupdate", new UserAccountUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountdelete", new UserAccountDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/machines", new MachineListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineadd", new MachineAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineupdate", new MachineUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machinedelete", new MachineDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/platforms", new PlatformListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformadd", new PlatformAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformupdate", new PlatformUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformdelete", new PlatformDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/platformproperties", new PlatformPropertyListServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/nodes", new NodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodecreate", new NodeCreateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeupdate", new NodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodedelete", new NodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestart", new NodeStartServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestop", new NodeStopServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestatus", new NodeListServlet(), dicts));

		addServlet(new ServletMetadataImpl("/domain/nodeproperties", new NodePropertyListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributeadd", new NodePropertyAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributeupdate", new NodePropertyUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributedelete", new NodePropertyDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/appstore", new AppStoreServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
