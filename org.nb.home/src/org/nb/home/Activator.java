package org.nb.home;

import org.nb.home.model.resource.ProjectResourceFactory;
import org.nb.home.model.resource.WorkspaceResourceFactory;
import org.nb.home.service.HomeAgentService;
import org.nb.home.service.impl.HomeAgentServiceImpl;
import org.nb.home.util.HomeNodeNature;
import org.nb.home.util.HomeProjectNature;
import org.nb.home.util.HomeWorkspaceNature;
import org.nb.home.ws.HomeApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static HomeAgentServiceImpl homeService;

	static BundleContext getContext() {
		return context;
	}

	protected HomeApplication homeApplication;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static HomeAgentService getHomeService() {
		return homeService;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		this.logger.debug("Home Management Activator.start()");
		// SystemPropertyUtil.printSystemProperties();
		// SystemPropertyUtil.printSystemEnvironmentVariables();

		// 1. Register nature providers
		HomeWorkspaceNature.register();
		HomeProjectNature.register();
		HomeNodeNature.register();

		// 2. Register cache factory
		WorkspaceResourceFactory.register();
		ProjectResourceFactory.register();

		// 3. Start Home management service
		Activator.homeService = new HomeAgentServiceImpl(bundleContext);
		Activator.homeService.start();

		// 4. Start HomeApplication
		this.homeApplication = new HomeApplication(bundleContext, "/home/v1");
		this.homeApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.logger.debug("Home Management Activator.stop()");

		// 1. Stop HomeApplication
		if (this.homeApplication != null) {
			this.homeApplication.stop();
			this.homeApplication = null;
		}

		// 2. Stop HomeApplication service
		if (Activator.homeService != null) {
			Activator.homeService.stop();
			Activator.homeService = null;
		}

		// 3. Unregister cache factory
		WorkspaceResourceFactory.unregister();
		ProjectResourceFactory.unregister();

		// 4. Unregister nature providers
		HomeWorkspaceNature.unregister();
		HomeProjectNature.unregister();
		HomeNodeNature.unregister();

		Activator.context = null;
	}

}
