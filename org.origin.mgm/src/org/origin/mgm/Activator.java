package org.origin.mgm;

import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl.DatabaseIndexService;
import org.origin.mgm.service.impl.DatabaseIndexServiceConfiguration;
import org.origin.mgm.ws.IndexServiceApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static DatabaseIndexService indexService;

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
		Activator.indexService = getDatabaseIndexService(bundleContext);
		Activator.indexService.start();

		// 2. Start IndexServiceApplication web service
		this.indexServiceApplication = new IndexServiceApplication(bundleContext, "/indexservice/v1");
		this.indexServiceApplication.start();
	}

	/**
	 * 
	 * @param bundleContext
	 * @return
	 */
	protected DatabaseIndexService getDatabaseIndexService(BundleContext bundleContext) {
		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		DatabaseIndexServiceConfiguration config = new DatabaseIndexServiceConfiguration(properties);
		return new DatabaseIndexService(bundleContext, config);
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
