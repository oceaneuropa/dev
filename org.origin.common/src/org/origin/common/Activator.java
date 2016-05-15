package org.origin.common;

import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyConfigurator;
import org.origin.common.deploy.WSDeployer;
import org.origin.common.osgi.PropertiesConfigCommand;
import org.origin.common.osgi.PropertiesConfigServiceFactory;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected WSDeployer wsDeployer;
	// protected NodeApplication nodeApplication;
	// protected PropertiesConfigAdmin propertiesConfigAdmin;

	protected DependencyConfigurator dependencyConfigurator;

	protected PropertiesConfigServiceFactory propertiesConfigFactory;
	protected ServiceRegistration<?> propertiesConfigFactoryReg;

	protected PropertiesConfigCommand propertiesConfigCommand;
	protected ServiceRegistration<?> propertiesConfigCommandReg;

	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.start()");

		Activator.context = bundleContext;

		// Start NodeApplication web service
		// this.nodeApplication = new NodeApplication(bundleContext, "/node");
		// this.nodeApplication.start();

		// this.propertiesConfigAdmin = new PropertiesConfigAdmin(bundleContext);
		// this.propertiesConfigAdmin.start();

		// Start WebService deployer
		this.wsDeployer = new WSDeployer(bundleContext);
		this.wsDeployer.start();

		this.dependencyConfigurator = new DependencyConfigurator(bundleContext);
		this.dependencyConfigurator.start();

		this.propertiesConfigFactory = new PropertiesConfigServiceFactory(bundleContext);
		this.propertiesConfigFactoryReg = bundleContext.registerService(Annotated.class.getName(), propertiesConfigFactory, null);

		this.propertiesConfigCommand = new PropertiesConfigCommand(bundleContext);
		this.propertiesConfigCommandReg = bundleContext.registerService(Annotated.class.getName(), propertiesConfigCommand, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.stop()");

		// Stop NodeApplication web service
		// if (this.nodeApplication != null) {
		// this.nodeApplication.stop();
		// this.nodeApplication = null;
		// }

		// if (this.propertiesConfigAdmin != null) {
		// this.propertiesConfigAdmin.stop();
		// this.propertiesConfigAdmin = null;
		// }

		// Stop WebService deployer
		if (this.wsDeployer != null) {
			this.wsDeployer.stop();
			this.wsDeployer = null;
		}

		if (this.dependencyConfigurator != null) {
			this.dependencyConfigurator.stop();
			this.dependencyConfigurator = null;
		}

		if (this.propertiesConfigFactoryReg != null) {
			this.propertiesConfigFactoryReg.unregister();
			this.propertiesConfigFactoryReg = null;
		}

		if (this.propertiesConfigCommandReg != null) {
			this.propertiesConfigCommandReg.unregister();
			this.propertiesConfigCommandReg = null;
		}

		Activator.context = null;
	}

}
