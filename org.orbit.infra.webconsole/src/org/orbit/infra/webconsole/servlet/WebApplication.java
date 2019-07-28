package org.orbit.infra.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.webconsole.WebConstants;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementAddServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementAttributeAddServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementAttributeDeleteServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementAttributeListServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementAttributeUpdateServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementDeleteServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementListServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigElementUpdateServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryAddServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryDeleteServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryListServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryPropertyAddServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryPropertyDeleteServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryPropertyListServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryPropertyUpdateServlet;
import org.orbit.infra.webconsole.servlet.configregistry.ConfigRegistryUpdateServlet;
import org.orbit.platform.sdk.http.PlatformWebApplication;
import org.orbit.service.servlet.impl.JspMetadataImpl;
import org.orbit.service.servlet.impl.ResourceMetadataImpl;
import org.orbit.service.servlet.impl.ServletMetadataImpl;
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
				WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		// e.g. "/orbit/webconsole/infra"
		String contextRoot = (String) this.getProperties().get(WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	protected void addResources(BundleContext bundleContext) {
		Hashtable<Object, Object> dicts = new Hashtable<Object, Object>();
		if (!this.properties.isEmpty()) {
			dicts.putAll(this.properties);
		}

		dicts.put("contextPath", getContextRoot());

		// put bundle name in the path, so that the bundle name can be retrieved from it and be used to get http context
		String bundlePrefix = "/org.orbit.infra.webconsole";

		// Web resources
		addResource(new ResourceMetadataImpl("/views/css", bundlePrefix + "/WEB-INF/views/css"));
		addResource(new ResourceMetadataImpl("/views/icons", bundlePrefix + "/WEB-INF/views/icons"));
		addResource(new ResourceMetadataImpl("/views/js", bundlePrefix + "/WEB-INF/views/js"));

		// Admin
		// Config Registry
		addServlet(new ServletMetadataImpl("/admin/configregs", new ConfigRegistryListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregadd", new ConfigRegistryAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregupdate", new ConfigRegistryUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregdelete", new ConfigRegistryDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/configregproperties", new ConfigRegistryPropertyListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregpropertyadd", new ConfigRegistryPropertyAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregpropertyupdate", new ConfigRegistryPropertyUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configregpropertydelete", new ConfigRegistryPropertyDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/configelements", new ConfigElementListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementadd", new ConfigElementAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementupdate", new ConfigElementUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementdelete", new ConfigElementDeleteServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/configelementattributes", new ConfigElementAttributeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementattributeadd", new ConfigElementAttributeAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementattributeupdate", new ConfigElementAttributeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/configelementattributedelete", new ConfigElementAttributeDeleteServlet(), dicts));

		// Add JSPs
		addJSP(new JspMetadataImpl(bundleContext.getBundle(), "/views", "/WEB-INF", dicts));
	}

}

// Files
// addServlet(new ServletMetadataImpl("/files", new FileListServlet(), dicts));
// addServlet(new ServletMetadataImpl("/mkdir", new CreateDirectoryServlet(), dicts));
// addServlet(new ServletMetadataImpl("/fileupload", new FileUploadServlet(), dicts));
// addServlet(new ServletMetadataImpl("/filedownload", new FileDownloadServlet(), dicts));
// addServlet(new ServletMetadataImpl("/filedelete", new FileDeleteServlet(), dicts));
