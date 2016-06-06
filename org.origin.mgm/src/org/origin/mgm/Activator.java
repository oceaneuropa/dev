package org.origin.mgm;

import java.nio.file.Path;
import java.util.Properties;

import org.origin.common.env.HomeUtil;
import org.origin.common.util.Printer;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.impl.DatabaseIndexServiceConfiguration;
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
		logger.debug("Origin Activator.start()");

		Activator.context = bundleContext;

		// Get ORIGIN_HOME system property and load {ORIGIN_HOME}/config.ini file into Properties.
		Path originHome = HomeUtil.getOriginHome(bundleContext);
		Path configIniPath = originHome.resolve("config.ini");
		Properties configProperties = HomeUtil.getProperties(originHome, "config.ini");
		System.out.println("originHome = " + originHome);
		System.out.println(configIniPath + " properties:");
		Printer.pl(configProperties);

		// 1. Start IndexService

		// String driver = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.driver", String.class, "org.postgresql.Driver");
		// String url = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.url", String.class,
		// "jdbc:postgresql://127.0.0.1:5432/origin");
		// String username = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.username", String.class, "postgres");
		// String password = PropertyUtil.getProperty(configIniProperties, "index.service.jdbc.password", String.class, "admin");
		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		// Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres",
		// "admin");
		// Properties properties = DatabaseUtil.getProperties(driver, url, username, password);
		// PropertyUtil.loadProperty(bundleContext, properties, IndexServiceConstants.CONFIG_PROP_HEARTBEAT_EXPIRE_TIME, Integer.class);
		// PropertyUtil.loadProperty(bundleContext, properties, IndexServiceConstants.CONFIG_PROP_NODE_NAME, String.class);
		// properties.putAll(configIniProperties);
		// System.out.println("DatabaseIndexServiceConfiguration properties:");
		// Printer.pl(properties);

		DatabaseIndexServiceConfiguration config = new DatabaseIndexServiceConfiguration(configProperties);
		Activator.indexService = new IndexServiceImpl(bundleContext, config);
		Activator.indexService.start();

		// 2. Start IndexServiceApplication web service
		this.indexServiceApplication = new IndexServiceApplication(bundleContext, "/indexservice/v1");
		this.indexServiceApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		logger.debug("Origin Activator.stop()");

		// 1. Stop IndexServiceApplication web service
		if (this.indexServiceApplication != null) {
			this.indexServiceApplication.stop();
			this.indexServiceApplication = null;
		}

		// 2. Stop IndexService
		if (Activator.indexService != null) {
			Activator.indexService.stop();
			Activator.indexService = null;
		}

		Activator.context = null;
	}

}
