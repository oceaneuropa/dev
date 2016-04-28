package org.origin.common;

import org.origin.common.deploy.WSDeployer;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected WSDeployer wsDeployer;
	// protected NodeApplication nodeApplication;
	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Container.start()");
		}
		Activator.context = bundleContext;

		// 1. Start WebService deployer
		this.wsDeployer = new WSDeployer(bundleContext);
		this.wsDeployer.start();

		// 2. Start NodeApplication web service
		// this.nodeApplication = new NodeApplication(bundleContext, "/node");
		// this.nodeApplication.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if (debug) {
			Printer.pl("Container.stop()");
		}

		// 1. Stop NodeApplication web service
		// if (this.nodeApplication != null) {
		// this.nodeApplication.stop();
		// this.nodeApplication = null;
		// }

		// 2. Stop WebService deployer
		if (this.wsDeployer != null) {
			this.wsDeployer.stop();
			this.wsDeployer = null;
		}

		Activator.context = null;
	}

}
