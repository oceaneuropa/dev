package org.orbit.infra.webconsole.servlet;

import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.webconsole.WebConstants;
import org.orbit.infra.webconsole.servlet.datacast.ChannelMetadataActionServlet;
import org.orbit.infra.webconsole.servlet.datacast.ChannelMetadataAddServlet;
import org.orbit.infra.webconsole.servlet.datacast.ChannelMetadataDeleteServlet;
import org.orbit.infra.webconsole.servlet.datacast.ChannelMetadataListServlet;
import org.orbit.infra.webconsole.servlet.datacast.ChannelMetadataUpdateServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataCastNodeActionServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataCastNodeAddServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataCastNodeDeleteServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataCastNodeListServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataCastNodeUpdateServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataTubeNodeActionServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataTubeNodeAddServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataTubeNodeDeleteServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataTubeNodeListServlet;
import org.orbit.infra.webconsole.servlet.datacast.DataTubeNodeUpdateServlet;
import org.orbit.infra.webconsole.servlet.datatube.RuntimeChannelListServlet;
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
				InfraConstants.ORBIT_INDEX_SERVICE_URL, //
				InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, //
				WebConstants.PLATFORM_WEB_CONSOLE_CONTEXT_ROOT, //
				WebConstants.INFRA__WEB_CONSOLE_CONTEXT_ROOT, //
				InfraConstants.ORBIT_DATACAST_URL, //
		};
		return propNames;
	}

	@Override
	public String getContextRoot() {
		// e.g. "/orbit/webconsole/substance"
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
		// DataCast
		addServlet(new ServletMetadataImpl("/admin/datacastlist", new DataCastNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datacastadd", new DataCastNodeAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datacastupdate", new DataCastNodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datacastdelete", new DataCastNodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datacastaction", new DataCastNodeActionServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/datatubelist", new DataTubeNodeListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datatubeadd", new DataTubeNodeAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datatubeupdate", new DataTubeNodeUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datatubedelete", new DataTubeNodeDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/datatubeaction", new DataTubeNodeActionServlet(), dicts));

		addServlet(new ServletMetadataImpl("/admin/channelmetadatalist", new ChannelMetadataListServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/channelmetadataadd", new ChannelMetadataAddServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/channelmetadataupdate", new ChannelMetadataUpdateServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/channelmetadatadelete", new ChannelMetadataDeleteServlet(), dicts));
		addServlet(new ServletMetadataImpl("/admin/channelmetadataaction", new ChannelMetadataActionServlet(), dicts));

		// DataTube
		addServlet(new ServletMetadataImpl("/admin/runtimechannellist", new RuntimeChannelListServlet(), dicts));

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
