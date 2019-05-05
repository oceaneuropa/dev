package org.orbit.component.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.webconsole.WebConstants;
import org.orbit.component.webconsole.servlet.tier1.identity.SignOutServlet;
import org.orbit.component.webconsole.servlet.tier1.identity.UserMainPage;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountAddServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountDeleteServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountListServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserAccountUpdateServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserProgramActionServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserProgramAddServlet;
import org.orbit.component.webconsole.servlet.tier1.useraccount.UserProgramsListServlet;
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
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeExtensionListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramActionServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramInstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramProblemsActionServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramProblemsListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeProgramUninstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyAddServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyDeleteServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodePropertyUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServiceActionServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServiceListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServicePropertyAddServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServicePropertyListServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServicePropertyRemoveServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeServicePropertyUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeStartServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeStopServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodeUpdateServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodesProgramBatchInstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodesProgramBatchUninstallProviderServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.NodesProgramBatchUninstallServlet;
import org.orbit.component.webconsole.servlet.tier3.nodecontrol.ProgramsProviderServlet;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
import org.orbit.substance.api.SubstanceConstants;
import org.osgi.framework.BundleContext;

public class WebApplication extends PlatformWebApplication {

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
				// InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				// InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.COMPONENT_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.PUBLIC_WEB_CONSOLE_CONTEXT_ROOT, //
				ComponentConstants.ORBIT_IDENTITY_SERVICE_URL, //
				ComponentConstants.ORBIT_USER_ACCOUNTS_URL, //
				ComponentConstants.ORBIT_AUTH_URL, //
				// ComponentConstants.ORBIT_REGISTRY_URL, //
				// ComponentConstants.ORBIT_APP_STORE_URL, //
				ComponentConstants.ORBIT_DOMAIN_SERVICE_URL, //
				ComponentConstants.ORBIT_NODE_CONTROL_URL, //
				ComponentConstants.ORBIT_MISSION_CONTROL_URL, //
				SubstanceConstants.ORBIT_DFS_URL, //
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

		String bundlePrefix = "/org.orbit.component.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Identity
		// addServlet(new ServletMetadataImpl("/signup", new SignUpPage(), dicts)); // public
		// addServlet(new ServletMetadataImpl("/signin", new SignInPage(), dicts)); // public
		// addServlet(new ServletMetadataImpl("/signup_req", new SignUpServlet(), dicts)); // public
		// addServlet(new ServletMetadataImpl("/signin_req", new SignInServlet(), dicts)); // public

		addServlet(new ServletMetadataImpl("/signout", new SignOutServlet(), dicts));
		addServlet(new ServletMetadataImpl("/user_main", new UserMainPage(), dicts));

		// User accounts
		addServlet(new ServletMetadataImpl("/useraccounts", new UserAccountListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountadd", new UserAccountAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountupdate", new UserAccountUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/useraccountdelete", new UserAccountDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/userprograms", new UserProgramsListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/userprogramadd", new UserProgramAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/userprogramaction", new UserProgramActionServlet(), dicts));

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
		addServlet(new ServletMetadataImpl("/domain/nodesprograminstall", new NodesProgramBatchInstallServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodesprogramuninstall", new NodesProgramBatchUninstallServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodesprogramuninstallprovider", new NodesProgramBatchUninstallProviderServlet(), dicts));

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
		addServlet(new ServletMetadataImpl("/domain/nodeprogramproblems", new NodeProgramProblemsListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeprogramproblemsaction", new NodeProgramProblemsActionServlet(), dicts));

		// Node extensions
		addServlet(new ServletMetadataImpl("/domain/nodeextensions", new NodeExtensionListServlet(), dicts));

		// Node services
		addServlet(new ServletMetadataImpl("/domain/nodeservices", new NodeServiceListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeserviceaction", new NodeServiceActionServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeserviceproperties", new NodeServicePropertyListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeservicepropertyadd", new NodeServicePropertyAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeservicepropertyupdate", new NodeServicePropertyUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/domain/nodeservicepropertyremove", new NodeServicePropertyRemoveServlet(), dicts));

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
