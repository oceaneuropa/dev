package org.nb.mgm;

import org.nb.mgm.service.ManagementService;
import org.nb.mgm.service.impl.ManagementServiceImpl;
import org.nb.mgm.ws.ManagementApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static ManagementServiceImpl mgmService;

	public static BundleContext getContext() {
		return context;
	}

	public static ManagementService getMgmService() {
		return Activator.mgmService;
	}

	protected ManagementApplication mgmApplication;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.logger.debug("Management Activator.start()");

		Activator.context = bundleContext;

		// 1. Start the management service
		ManagementServiceImpl mgmService = new ManagementServiceImpl(bundleContext);
		mgmService.setAutoSave(true);
		mgmService.start();
		Activator.mgmService = mgmService;

		// 2. Start ManagementApplication
		this.mgmApplication = new ManagementApplication(bundleContext, "/mgm/v1");
		this.mgmApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.logger.debug("Management Activator.stop()");

		// 1. Stop ManagementApplication
		if (this.mgmApplication != null) {
			this.mgmApplication.stop();
			this.mgmApplication = null;
		}

		// 2. Stop the management service
		if (Activator.mgmService != null) {
			Activator.mgmService.stop();
			Activator.mgmService = null;
		}

		Activator.context = null;
	}

}
