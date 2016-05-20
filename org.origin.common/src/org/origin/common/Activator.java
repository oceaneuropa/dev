package org.origin.common;

import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyConfigurator;
import org.origin.common.deploy.WSDeployer;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.osgi.PropertiesConfigCommand;
import org.origin.common.osgi.PropertiesConfigServiceFactory;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	protected DependencyConfigurator dependencyConfigurator;
	protected WSDeployer wsDeployer;
	protected PropertiesConfigServiceFactory propertiesConfigFactory;
	protected PropertiesConfigCommand propertiesConfigCommand;

	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.start()");

		Activator.context = bundleContext;

		this.dependencyConfigurator = new DependencyConfigurator(bundleContext);
		this.dependencyConfigurator.start();

		this.wsDeployer = new WSDeployer(bundleContext);
		this.wsDeployer.start();

		this.propertiesConfigFactory = new PropertiesConfigServiceFactory(bundleContext);
		this.propertiesConfigCommand = new PropertiesConfigCommand(bundleContext);

		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigFactory);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigCommand);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.stop()");

		if (this.dependencyConfigurator != null) {
			this.dependencyConfigurator.stop();
			this.dependencyConfigurator = null;
		}

		if (this.wsDeployer != null) {
			this.wsDeployer.stop();
			this.wsDeployer = null;
		}

		OSGiServiceUtil.unregister(this.propertiesConfigFactory);
		OSGiServiceUtil.unregister(this.propertiesConfigCommand);

		if (this.propertiesConfigCommand != null) {
			this.propertiesConfigCommand.stop();
			this.propertiesConfigCommand = null;
		}
		if (this.propertiesConfigFactory != null) {
			this.propertiesConfigFactory.stop();
			this.propertiesConfigFactory = null;
		}

		Activator.context = null;
	}

}

// protected NodeApplication nodeApplication;
// protected PropertiesConfigAdmin propertiesConfigAdmin;
// protected ServiceRegistration<?> propertiesConfigFactoryReg;
// protected ServiceRegistration<?> propertiesConfigCommandReg;

// Start NodeApplication web service
// this.nodeApplication = new NodeApplication(bundleContext, "/node");
// this.nodeApplication.start();
// this.propertiesConfigAdmin = new PropertiesConfigAdmin(bundleContext);
// this.propertiesConfigAdmin.start();

// Stop NodeApplication web service
// if (this.nodeApplication != null) {
// this.nodeApplication.stop();
// this.nodeApplication = null;
// }
// if (this.propertiesConfigAdmin != null) {
// this.propertiesConfigAdmin.stop();
// this.propertiesConfigAdmin = null;
// }
// if (this.propertiesConfigFactoryReg != null) {
// this.propertiesConfigFactoryReg.unregister();
// this.propertiesConfigFactoryReg = null;
// }
// if (this.propertiesConfigCommandReg != null) {
// this.propertiesConfigCommandReg.unregister();
// this.propertiesConfigCommandReg = null;
// }
