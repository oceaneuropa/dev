package org.orbit.fs.server;

import org.orbit.fs.server.service.FileSystemService;
import org.orbit.fs.server.ws.FileSystemApplication;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static FileSystemService fsService;

	static BundleContext getContext() {
		return context;
	}

	public static FileSystemService getFileSystemService() {
		return Activator.fsService;
	}

	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected FileSystemApplication fsApplication;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
