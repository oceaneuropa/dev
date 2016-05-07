package org.origin.mgm;

import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl.IndexServiceImpl;
import org.origin.mgm.ws.IndexServiceApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static IndexServiceImpl indexService;

	public static BundleContext getContext() {
		return context;
	}

	public static IndexService getIndexService() {
		return Activator.indexService;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected IndexServiceApplication indexServiceApplication;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		logger.debug("Management Activator.start()");

		Activator.context = bundleContext;

		// 1. Start IndexService
		Activator.indexService = new IndexServiceImpl(bundleContext);
		Activator.indexService.start();

		// 2. Start IndexServiceApplication web service
		this.indexServiceApplication = new IndexServiceApplication(bundleContext, "/indexservice/v1");
		this.indexServiceApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		logger.debug("Management Activator.stop()");

		// 1. Stop MgmApplication service
		if (this.indexServiceApplication != null) {
			this.indexServiceApplication.stop();
			this.indexServiceApplication = null;
		}

		// 2. Stop Mgm management service
		if (Activator.indexService != null) {
			Activator.indexService.stop();
			Activator.indexService = null;
		}

		Activator.context = null;
	}

}
