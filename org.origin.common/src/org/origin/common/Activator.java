package org.origin.common;

import org.origin.common.deploy.WSDeployer;
import org.origin.common.osgi.PropertiesConfigCommand;
import org.origin.common.osgi.PropertiesConfigAdmin;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static PropertiesConfigAdmin qnameServiceAdminSupport;

	static BundleContext getContext() {
		return context;
	}

	protected WSDeployer wsDeployer;
	// protected NodeApplication nodeApplication;
	protected PropertiesConfigCommand qnameCommand;

	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.start()");

		Activator.context = bundleContext;

		// 1. Start WebService deployer
		this.wsDeployer = new WSDeployer(bundleContext);
		this.wsDeployer.start();

		// 2. Start NodeApplication web service
		// this.nodeApplication = new NodeApplication(bundleContext, "/node");
		// this.nodeApplication.start();

		// 3. Start the QNameServiceAdminSupport service
		Activator.qnameServiceAdminSupport = new PropertiesConfigAdmin(bundleContext);
		Activator.qnameServiceAdminSupport.start();

		this.qnameCommand = new PropertiesConfigCommand(bundleContext);
		this.qnameCommand.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.stop()");

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

		// 3. Stop the QNameServiceAdminSupport service
		if (Activator.qnameServiceAdminSupport != null) {
			Activator.qnameServiceAdminSupport.stop();
			Activator.qnameServiceAdminSupport = null;
		}

		if (this.qnameCommand != null) {
			this.qnameCommand.stop();
			this.qnameCommand = null;
		}

		Activator.context = null;
	}

}
