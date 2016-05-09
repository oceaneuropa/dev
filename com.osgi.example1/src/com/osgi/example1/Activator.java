package com.osgi.example1;

import java.io.File;
import java.util.Properties;

import org.origin.common.jdbc.DatabaseUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osgi.example1.fs.server.service.FileSystem;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystem;
import com.osgi.example1.fs.server.service.database.DatabaseFileSystemConfiguration;
import com.osgi.example1.fs.server.service.local.LocalFileSystem;
import com.osgi.example1.fs.server.service.local.LocalFileSystemConfiguration;
import com.osgi.example1.fs.server.ws.FileSystemApplication;

/**
 * -Dorg.osgi.service.http.port=9090
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	protected static FileSystem fsService;

	static BundleContext getContext() {
		return context;
	}

	public static FileSystem getFsService() {
		return Activator.fsService;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());
	// protected ServiceTracker<HttpService, HttpService> httpServiceTracker;
	protected FileSystemApplication fsApplication;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("Activator.start()");
		Activator.context = bundleContext;

		// this.httpServiceTracker = new ServiceTracker<HttpService, HttpService>(bundleContext, HttpService.class, new
		// ServiceTrackerCustomizer<HttpService, HttpService>() {
		// @Override
		// public HttpService addingService(ServiceReference<HttpService> reference) {
		// Printer.pl("Activator.httpServiceTracker.addingService()");
		// HttpService httpService = getContext().getService(reference);
		// Printer.pl("\t httpService = " + httpService);
		// return httpService;
		// }
		//
		// @Override
		// public void modifiedService(ServiceReference<HttpService> reference, HttpService httpService) {
		// Printer.pl("Activator.httpServiceTracker.modifiedService()");
		// Printer.pl("\t httpService = " + httpService);
		// }
		//
		// @Override
		// public void removedService(ServiceReference<HttpService> reference, HttpService httpService) {
		// Printer.pl("Activator.httpServiceTracker.removedService()");
		// Printer.pl("\t httpService = " + httpService);
		// }
		// });
		// this.httpServiceTracker.open();

		this.logger.debug("FileSystem Activator.start()");

		Activator.context = bundleContext;

		// 1. Start FileSystem service
		Activator.fsService = getDatabaseFileSystem(bundleContext);
		Activator.fsService.adapt(BundleContext.class, bundleContext);
		Activator.fsService.start();

		// 2. Start FileSystemApplication web service
		this.fsApplication = new FileSystemApplication(bundleContext, "/fs/v1");
		this.fsApplication.start();
	}

	protected FileSystem getDatabaseFileSystem(BundleContext bundleContext) {
		// Properties properties = DatabaseUtil.getProperties("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/origin", "root", "admin");
		Properties properties = DatabaseUtil.getProperties("org.postgresql.Driver", "jdbc:postgresql://127.0.0.1:5432/origin", "postgres", "admin");
		DatabaseFileSystemConfiguration config = new DatabaseFileSystemConfiguration(properties);
		return new DatabaseFileSystem(config);
	}

	protected FileSystem getLocalFileSystem(BundleContext bundleContext) {
		// File homeDirector = new File("/Users/yayang/Downloads/ear"); // For Mac
		File homeDirector = new File("C:/downloads/test_source"); // For Win
		LocalFileSystemConfiguration config = new LocalFileSystemConfiguration(homeDirector);
		return new LocalFileSystem(config);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("Activator.stop()");

		// this.httpServiceTracker.close();

		// 1. Stop FileSystemApplication web service
		if (this.fsApplication != null) {
			this.fsApplication.stop();
			this.fsApplication = null;
		}

		// 2. Stop FileSystem service
		if (Activator.fsService != null) {
			Activator.fsService.stop();
			Activator.fsService = null;
		}

		Activator.context = null;
	}

}
