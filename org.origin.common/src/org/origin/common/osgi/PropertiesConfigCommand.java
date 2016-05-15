package org.origin.common.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;

public class PropertiesConfigCommand implements Annotated {

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected ConfigurationAdmin configAdmin;

	@Dependency
	protected EventAdmin eventAdmin;

	/**
	 * 
	 * @param bundleContext
	 */
	public PropertiesConfigCommand(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@DependencyFullfilled
	public void start() {
		System.out.println("PropertiesConfigCommand.start()");

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "origin");
		props.put("osgi.command.function", new String[] { "lconfigs", "lprops", "setprop", "rmprop", "rmallprops" });
		this.registration = bundleContext.registerService(PropertiesConfigCommand.class.getName(), this, props);
	}

	@DependencyUnfullfilled
	public void stop() {
		System.out.println("PropertiesConfigCommand.stop()");

		if (this.registration != null) {
			this.registration.unregister();
			this.registration = null;
		}
	}

	@Descriptor("List configurations")
	public void lconfigs_old() throws Exception {
		System.out.println("PropertiesConfigCommand.lconfigs()");
		ServiceReference<ConfigurationAdmin> configAdminRef = this.bundleContext.getServiceReference(ConfigurationAdmin.class);
		if (configAdminRef == null) {
			System.err.println("ConfigurationAdmin is not available.");
			return;
		}
		try {
			ConfigurationAdmin configAdmin = this.bundleContext.getService(configAdminRef);
			ConfigurationUtil.listConfigurations(configAdmin, null);
		} finally {
			if (configAdminRef != null) {
				this.bundleContext.ungetService(configAdminRef);
			}
		}
	}

	@Descriptor("List configurations")
	public void lconfigs() throws Exception {
		System.out.println("PropertiesConfigCommand.lconfigs()");
		ConfigurationUtil.listConfigurations(this.configAdmin, null);
	}

	public void setproperty(@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName, //
			@Descriptor("propName") @Parameter(absentValue = "", names = { "-pname", "--propertyname" }) String propName, //
			@Descriptor("propValue") @Parameter(absentValue = "", names = { "-pvalue", "--propertyvalue" }) String propValue //
	) throws Exception {

	}

	@Descriptor("Add QName config command")
	public void addq( //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName, //
			@Descriptor("propName") @Parameter(absentValue = "", names = { "-pname", "--propertyname" }) String propName, //
			@Descriptor("propValue") @Parameter(absentValue = "", names = { "-pvalue", "--propertyvalue" }) String propValue //
	) throws Exception {
		System.out.println("QNameAdminCommand.addq()");

		String qname = new QName(tns, localName).toString();

		// String filter = "(&(service.factoryPid=qname.service.admin)(tns=" + tns + ")(localName=" + localName + "))";
		String filter = "(&(service.factoryPid=qname.service.admin)(qname=" + qname + "))";
		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		System.out.println("filter=" + filter);

		Configuration config = null;
		if (configs != null && configs.length > 0) {
			config = configs[0];
		}

		if (config == null) {
			config = configAdmin.createFactoryConfiguration(PropertiesConfigServiceFactory.SERVICE_PID);
			System.out.println("Configuration is not found. New Configuration is created.");
		} else {
			System.out.println("Configuration is found.");
		}

		Dictionary<String, Object> configProps = config.getProperties();
		if (configProps == null) {
			configProps = new Hashtable<String, Object>();
		}
		configProps.put("qname", new QName(tns, localName).toString());
		configProps.put(propName, propValue);

		config.update(configProps);
	}

	@Descriptor("Remove QName config command")
	public void rmq( //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName //
	) throws Exception {
		System.out.println("QNameAdminCommand.rmq()");
		String qname = new QName(tns, localName).toString();

		// String filter = "(&(service.factoryPid=qname.service.admin)(tns=" + tns + ")(localName=" + localName + "))";
		String filter = "(&(service.factoryPid=qname.service.admin)(qname=" + qname + "))";
		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		System.out.println("filter=" + filter);

		Configuration config = null;
		if (configs != null && configs.length > 0) {
			config = configs[0];
		}

		if (config == null) {
			System.out.println("Configuration is not found.");

		} else {
			for (Configuration currConfig : configs) {
				System.out.println("Configuration is found.");
				currConfig.delete();
			}
		}
	}

}
