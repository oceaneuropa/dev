package org.origin.common;

import org.origin.common.annotation.DependencyConfigurator;
import org.origin.common.command.IEditingDomain;
import org.origin.common.deploy.WebServiceDeployer;
import org.origin.common.util.Printer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.origin.common"; //$NON-NLS-1$
	// The shared instance
	protected static Activator plugin;

	private static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	protected IEditingDomain editingDomain;
	protected DependencyConfigurator dependencyConfigurator;
	protected WebServiceDeployer webServiceDeployer;
	// protected PropertiesConfigServiceFactory propertiesConfigFactory;
	// protected PropertiesConfigCommand propertiesConfigCommand;

	protected boolean debug = true;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.start()");

		this.editingDomain = IEditingDomain.getEditingDomain(PLUGIN_ID);

		this.dependencyConfigurator = new DependencyConfigurator(bundleContext);
		this.dependencyConfigurator.start();

		this.webServiceDeployer = new WebServiceDeployer(bundleContext);
		this.webServiceDeployer.start();

		// this.propertiesConfigFactory = new PropertiesConfigServiceFactory(bundleContext);
		// this.propertiesConfigCommand = new PropertiesConfigCommand(bundleContext);
		// OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigFactory);
		// OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigCommand);

		Activator.context = bundleContext;
		Activator.plugin = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Printer.pl("org.origin.common.Activator.stop()");
		Activator.plugin = null;
		Activator.context = null;

		if (this.dependencyConfigurator != null) {
			this.dependencyConfigurator.stop();
			this.dependencyConfigurator = null;
		}

		if (this.webServiceDeployer != null) {
			this.webServiceDeployer.stop();
			this.webServiceDeployer = null;
		}

		// OSGiServiceUtil.unregister(this.propertiesConfigFactory);
		// OSGiServiceUtil.unregister(this.propertiesConfigCommand);
		//
		// if (this.propertiesConfigCommand != null) {
		// this.propertiesConfigCommand.stop();
		// this.propertiesConfigCommand = null;
		// }
		// if (this.propertiesConfigFactory != null) {
		// this.propertiesConfigFactory.stop();
		// this.propertiesConfigFactory = null;
		// }

		this.editingDomain = null;
		IEditingDomain.disposeEditingDomain(PLUGIN_ID);
	}

	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
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
