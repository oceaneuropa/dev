package org.origin.common;

import java.util.Hashtable;
import java.util.Map;

import org.origin.common.annotation.DependencyConfigurator;
import org.origin.common.command.IEditingDomain;
import org.origin.common.deploy.WebServiceDeployer;
import org.origin.common.service.CLIAwareRegistry;
import org.origin.common.service.WebServiceAwareRegistry;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.origin.common";
	public static String ORBIT_REALM = "orbit.realm";

	protected static Logger LOG = LoggerFactory.getLogger(Activator.class);
	protected static Activator plugin;
	protected static BundleContext context;

	public static BundleContext getContext() {
		return context;
	}

	public static Activator getDefault() {
		return plugin;
	}

	protected Map<Object, Object> properties;
	protected IEditingDomain editingDomain;
	protected DependencyConfigurator dependencyConfigurator;
	protected WebServiceDeployer webServiceDeployer;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		Activator.context = bundleContext;
		Activator.plugin = this;

		Map<Object, Object> properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, ORBIT_REALM);
		this.properties = properties;

		WebServiceAwareRegistry.getInstance().start(bundleContext);
		CLIAwareRegistry.getInstance().start(bundleContext);

		this.editingDomain = IEditingDomain.getEditingDomain(PLUGIN_ID);

		this.dependencyConfigurator = new DependencyConfigurator(bundleContext);
		this.dependencyConfigurator.start();

		this.webServiceDeployer = new WebServiceDeployer(bundleContext);
		this.webServiceDeployer.start();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		LOG.info("start()");

		if (this.dependencyConfigurator != null) {
			this.dependencyConfigurator.stop();
			this.dependencyConfigurator = null;
		}

		if (this.webServiceDeployer != null) {
			this.webServiceDeployer.stop();
			this.webServiceDeployer = null;
		}

		IEditingDomain.disposeEditingDomain(PLUGIN_ID);

		WebServiceAwareRegistry.getInstance().stop(bundleContext);
		CLIAwareRegistry.getInstance().stop(bundleContext);

		this.editingDomain = null;
		Activator.plugin = null;
		Activator.context = null;
	}

	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	public String getRealm() {
		String realm = (String) this.properties.get(ORBIT_REALM);
		return realm;
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

// protected PropertiesConfigServiceFactory propertiesConfigFactory;
// protected PropertiesConfigCommand propertiesConfigCommand;

// this.propertiesConfigFactory = new PropertiesConfigServiceFactory(bundleContext);
// this.propertiesConfigCommand = new PropertiesConfigCommand(bundleContext);
// OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigFactory);
// OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), propertiesConfigCommand);

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
