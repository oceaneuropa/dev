package org.orbit.infra.model;

import org.orbit.infra.model.repo.resource.RepoResourceFactory;
import org.orbit.infra.model.repo.resource.ReposResourceFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	protected static BundleContext context;
	protected static Activator instance;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getInstance() {
		return instance;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.instance = this;

		// Register WorkingCopy factories
		ReposResourceFactory.register();
		RepoResourceFactory.register();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.instance = null;
		Activator.context = null;

		// Unregister WorkingCopy factories
		ReposResourceFactory.unregister();
		RepoResourceFactory.unregister();
	}

}
