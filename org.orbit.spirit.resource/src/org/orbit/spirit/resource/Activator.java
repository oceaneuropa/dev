package org.orbit.spirit.resource;

import org.orbit.spirit.resource.userprograms.UserProgramsResourceFactory;
import org.origin.common.osgi.AbstractBundleActivator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends AbstractBundleActivator implements BundleActivator {

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);

	protected static Activator instance;

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");
		super.start(bundleContext);
		Activator.instance = this;

		UserProgramsResourceFactory.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("stop()");

		UserProgramsResourceFactory.unregister();

		Activator.instance = null;
		super.stop(bundleContext);
	}

}