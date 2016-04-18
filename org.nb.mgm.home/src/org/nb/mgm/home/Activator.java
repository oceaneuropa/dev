package org.nb.mgm.home;

import org.nb.mgm.home.service.HomeService;
import org.nb.mgm.home.service.impl.HomeServiceImpl;
import org.nb.mgm.home.ws.HomeApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static HomeServiceImpl homeService;

	static BundleContext getContext() {
		return context;
	}

	protected HomeApplication homeApplication;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static HomeService getHomeService() {
		return homeService;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.logger.debug("Home Management Activator.start()");

		Activator.context = bundleContext;

		// SystemPropertyUtil.printSystemProperties();
		// SystemPropertyUtil.printSystemEnvironmentVariables();

		// 1. Start Home management service
		Activator.homeService = new HomeServiceImpl(bundleContext);
		Activator.homeService.start();

		// 2. Start HomeApplication web service
		this.homeApplication = new HomeApplication(bundleContext, "/home/v1");
		this.homeApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.logger.debug("Home Management Activator.stop()");

		// 1. Stop HomeApplication service
		if (this.homeApplication != null) {
			this.homeApplication.stop();
			this.homeApplication = null;
		}

		// 2. Stop Home management service
		if (Activator.homeService != null) {
			Activator.homeService.stop();
			Activator.homeService = null;
		}

		Activator.context = null;
	}

}
