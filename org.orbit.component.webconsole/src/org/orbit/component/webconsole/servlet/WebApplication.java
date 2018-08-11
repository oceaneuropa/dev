package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.OrbitConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.tier1.identity.SignInPage;
import org.orbit.component.webconsole.servlet.tier1.identity.SignInServlet;
import org.orbit.component.webconsole.servlet.tier1.identity.SignOutServlet;
import org.orbit.component.webconsole.servlet.tier1.identity.SignUpPage;
import org.orbit.component.webconsole.servlet.tier1.identity.SignUpServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountAddServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountDeleteServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountListServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountUpdateServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppAddServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppDeleteServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppDownloadServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppListServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppUpdateServlet;
import org.orbit.component.webconsole.servlet.tier2.appstore.AppUploadServlet;
import org.orbit.component.webconsole.servlet.tier2.other.FileUploadServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.MachineAddServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.MachineDeleteServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.MachineListServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.MachineUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.PlatformAddServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.PlatformDeleteServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.PlatformListServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.PlatformPropertyListServlet;
import org.orbit.component.webconsole.servlet.tier3.domain.PlatformUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeCreateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeDeleteServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramActionServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramInstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramUninstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyAddServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyDeleteServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeStartServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeStopServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.ProgramsProviderServlet;
import org.orbit.infra.api.InfraConstants;
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
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
				OrbitConstants.ORBIT_IDENTITY_SERVICE_URL, //
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

		// Web resources
		addResource(new ResourceMetadataImpl("/views/css", "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", "/WEB-INF/views/js"));

		// Identity
		addServlet(new ServletMetadataImpl("/signup", new SignUpPage(), dicts));
		addServlet(new ServletMetadataImpl("/signin", new SignInPage(), dicts));

		addServlet(new ServletMetadataImpl("/signup_req", new SignUpServlet(), dicts));
		addServlet(new ServletMetadataImpl("/signin_req", new SignInServlet(), dicts));
		addServlet(new ServletMetadataImpl("/signout_req", new SignOutServlet(), dicts));

		// User accounts
		addServlet(new ServletMetadataImpl("/useraccounts", new UserAccountListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountadd", new UserAccountAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountupdate", new UserAccountUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountdelete", new UserAccountDeleteServlet(), dicts));

		// Machines
		addServlet(new ServletMetadataImpl("/domain/machines", new MachineListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineadd", new MachineAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machineupdate", new MachineUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/machinedelete", new MachineDeleteServlet(), dicts));

		// Platforms
		addServlet(new ServletMetadataImpl("/domain/platforms", new PlatformListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformadd", new PlatformAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformupdate", new PlatformUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/platformdelete", new PlatformDeleteServlet(), dicts));

		// Platform properties
		addServlet(new ServletMetadataImpl("/domain/platformproperties", new PlatformPropertyListServlet(), dicts));

		// Nodes
		addServlet(new ServletMetadataImpl("/domain/nodes", new NodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodecreate", new NodeCreateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeupdate", new NodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodedelete", new NodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestart", new NodeStartServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestop", new NodeStopServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodestatus", new NodeListServlet(), dicts));

		// Node properties
		addServlet(new ServletMetadataImpl("/domain/nodeproperties", new NodePropertyListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributeadd", new NodePropertyAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributeupdate", new NodePropertyUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeattributedelete", new NodePropertyDeleteServlet(), dicts));

		// Node programs
		addServlet(new ServletMetadataImpl("/domain/nodeprograms", new NodeProgramListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeprograminstall", new NodeProgramInstallServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeprogramuninstall", new NodeProgramUninstallServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeprogramaction", new NodeProgramActionServlet(), dicts));

		// AppStore
		addServlet(new ServletMetadataImpl("/appstore/apps", new AppListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/appstore/appadd", new AppAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/appstore/appupdate", new AppUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/appstore/appupload", new AppUploadServlet(), dicts));
		addServlet(new ServletMetadataImpl("/appstore/appdownload", new AppDownloadServlet(), dicts));
		addServlet(new ServletMetadataImpl("/appstore/appdelete", new AppDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/appstore/programsprovider", new ProgramsProviderServlet(), dicts));

		// Other
		addServlet(new ServletMetadataImpl("/upload", new FileUploadServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}
