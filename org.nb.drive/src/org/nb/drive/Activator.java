package org.nb.drive;

import org.nb.drive.rest.server.DriveApplication;
import org.nb.drive.rest.server.DriveService;
import org.nb.drive.rest.server.impl.DriveServiceImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	private static BundleContext context;
	protected static DriveServiceImpl driveService;

	public static BundleContext getContext() {
		return context;
	}

	public static DriveService getDriveService() {
		return Activator.driveService;
	}

	protected DriveApplication driveApplication;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.logger.debug("REST Drive service Activator.start()");

		Activator.context = bundleContext;

		// 1. Start REST Drive service
		Activator.driveService = new DriveServiceImpl(bundleContext);
		Activator.driveService.start();

		// 2. Start DriveApplication web service
		this.driveApplication = new DriveApplication(bundleContext, "/drive/v1");
		this.driveApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		this.logger.debug("REST Drive service Activator.stop()");

		// 1. Stop DriveApplication service
		if (this.driveApplication != null) {
			this.driveApplication.stop();
			this.driveApplication = null;
		}

		// 2. Stop REST Drive service
		if (Activator.driveService != null) {
			Activator.driveService.stop();
			Activator.driveService = null;
		}

		Activator.context = null;
	}

}
