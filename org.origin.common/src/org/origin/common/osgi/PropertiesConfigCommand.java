package org.origin.common.osgi;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.xml.namespace.QName;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.Dependency;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.util.DateUtil;
import org.origin.common.util.StringUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class PropertiesConfigCommand implements Annotated {

	/** managed service factory ID --- "properties.config.service.factory" */
	public static final String FACTORY_PID = PropertiesConfigServiceFactory.SERVICE_PID;
	/** property name for QName */
	public static final String QNAME = "qname";

	protected BundleContext bundleContext;
	protected ServiceRegistration<?> registration;

	@Dependency
	protected ConfigurationAdmin configAdmin;

	// @Dependency
	// protected EventAdmin eventAdmin;

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
		props.put("osgi.command.function", new String[] { "lprops", "setprop", "rmprop", "rmprops" });
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

	protected String normalizeFullName(String fullname) {
		if (fullname != null && !fullname.isEmpty()) {
			// remove starting "."
			fullname = StringUtil.removeStartingCharacters(fullname, ".");
			// remove ending "."
			fullname = StringUtil.removeEndingCharacters(fullname, ".");
		}
		return fullname;
	}

	protected String getQNameString(String fullname, String tns, String localName) {
		String qNameString = null;
		if (!fullname.isEmpty()) {
			int index = fullname.lastIndexOf(".");
			if (index > 0) {
				// has "."
				String _tns = fullname.substring(0, index);
				String _localName = fullname.substring(index + 1);
				qNameString = new QName(_tns, _localName).toString();
			} else {
				// no "."
				qNameString = new QName(fullname).toString();
			}
		} else if (!localName.isEmpty()) {
			qNameString = new QName(tns, localName).toString();
		}
		return qNameString;
	}

	protected String getFilterString() {
		return "(service.factoryPid=" + FACTORY_PID + ")";
	}

	protected String getFilterString(String qNameString) {
		return "(&(service.factoryPid=" + FACTORY_PID + ")(" + QNAME + "=" + qNameString + "))";
	}

	/**
	 * List properties of all QNames.
	 * 
	 * @throws Exception
	 */
	@Descriptor("List properties of all QNames")
	public void lprops( //
			@Descriptor("fully qualified name") @Parameter(absentValue = "", names = { "-fn", "--fullname" }) String fullname, //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName //
	) throws Exception {
		// System.out.println("PropertiesConfigCommand.lprops()");

		fullname = normalizeFullName(fullname);

		String filter = null;
		String qNameString = getQNameString(fullname, tns, localName);
		if (qNameString != null) {
			filter = getFilterString(qNameString);
		} else {
			filter = getFilterString();
		}

		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		if (configs == null || configs.length == 0) {
			System.out.println("Configurations is empty.");
		}
		if (configs != null) {
			for (Configuration config : configs) {
				System.out.println(config);
				Dictionary<String, Object> properties = config.getProperties();
				if (properties != null) {
					System.out.println("Properties: " + properties.toString());
				}
			}
		}

		System.out.println();
	}

	/**
	 * Set the property of a QName.
	 * 
	 * @param fullname
	 * @param tns
	 * @param localName
	 * @param propName
	 * @param propValue
	 * @param propType
	 * @throws Exception
	 */
	@Descriptor("Set the property of a QNameS")
	public void setprop( //
			@Descriptor("fully qualified name") @Parameter(absentValue = "", names = { "-fn", "--fullname" }) String fullname, //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName, //
			@Descriptor("propName") @Parameter(absentValue = "", names = { "-pn", "--propertyname" }) String propName, //
			@Descriptor("propValue") @Parameter(absentValue = "", names = { "-pv", "--propertyvalue" }) String propValue, //
			@Descriptor("propType") @Parameter(absentValue = "string", names = { "-pt", "--propertytype" }) String propType //
	) throws Exception {
		// System.out.println("PropertiesConfigCommand.setproperty()");

		fullname = normalizeFullName(fullname);

		if (fullname.isEmpty() && (tns.isEmpty() && localName.isEmpty())) {
			System.err.println("Please specify either full name (-fn or --fullname) or a combination of target namespace (-t or --tns) and local name (-n or --name).");
			return;
		}

		String qNameString = getQNameString(fullname, tns, localName);
		String filter = getFilterString(qNameString);

		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		Configuration config = null;
		if (configs != null && configs.length > 0) {
			config = configs[0];
		}
		boolean createNewConfig = false;
		if (config == null) {
			config = configAdmin.createFactoryConfiguration(PropertiesConfigServiceFactory.SERVICE_PID);
			createNewConfig = true;
			System.out.println("Configuration for '" + qNameString + "' is not found. New Configuration is created.");
		} else {
			System.out.println("Configuration for '" + qNameString + "' is found.");
		}

		Dictionary<String, Object> configProps = config.getProperties();
		if (configProps == null) { // this check is necessary
			configProps = new Hashtable<String, Object>();
		}
		if (createNewConfig) {
			configProps.put(QNAME, qNameString);
		}

		Object propertyObject = null;
		if ("string".equalsIgnoreCase(propType)) {
			propertyObject = propValue;

		} else if ("integer".equalsIgnoreCase(propType)) {
			try {
				propertyObject = Integer.parseInt(propValue);
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to integer. " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}

		} else if ("boolean".equalsIgnoreCase(propType)) {
			try {
				propertyObject = Boolean.parseBoolean(propValue);
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to boolean. " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}

		} else if ("long".equalsIgnoreCase(propType)) {
			try {
				propertyObject = Long.parseLong(propValue);
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to long. " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}

		} else if ("double".equalsIgnoreCase(propType)) {
			try {
				propertyObject = Double.parseDouble(propValue);
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to double. " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}

		} else if ("float".equalsIgnoreCase(propType)) {
			try {
				propertyObject = Float.parseFloat(propValue);
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to float. " + e.getClass().getSimpleName() + ":" + e.getMessage());
			}

		} else if ("date".equalsIgnoreCase(propType)) {
			try {
				propertyObject = DateUtil.toDate(propValue, DateUtil.getCommonDateFormats());
			} catch (Exception e) {
				System.err.println("Cannot convert property value '" + propValue + "' to date. " + e.getClass().getSimpleName() + ":" + e.getMessage());
				System.err.println("Supported date formats are: " + Arrays.toString(DateUtil.getCommonDateFormats()));
			}

		} else {
			// treated as string type
			System.err.println("Unsupported property type: '" + propType + "'.");
			propertyObject = propValue;
		}

		// Property value string cannot be converted to corresponding type of value. Set property value using the property value string
		if (propertyObject == null) {
			System.err.println("Property value '" + propValue + "' cannot be converted to typed value. Set the property using the string value.");
			propertyObject = propValue;
		}

		// Set property value to the Configuration
		configProps.put(propName, propValue);

		// Update the properties of the Configuration, which will notify corresponding managed service factory.
		config.update(configProps);
	}

	/**
	 * Remove property of a QName.
	 * 
	 * @param fullname
	 * @param tns
	 * @param localName
	 * @param propName
	 * @throws Exception
	 */
	@Descriptor("Remove property of a QName")
	public void rmprop( //
			@Descriptor("fully qualified name") @Parameter(absentValue = "", names = { "-fn", "--fullname" }) String fullname, //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName, //
			@Descriptor("propName") @Parameter(absentValue = "", names = { "-pn", "--propertyname" }) String propName //
	) throws Exception {

		fullname = normalizeFullName(fullname);

		if (fullname.isEmpty() && (tns.isEmpty() && localName.isEmpty())) {
			System.err.println("Please specify either full name (-fn or --fullname) or a combination of target namespace (-t or --tns) and local name (-n or --name).");
			return;
		}

		String qNameString = getQNameString(fullname, tns, localName);
		String filter = getFilterString(qNameString);

		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		Configuration config = null;
		if (configs != null && configs.length > 0) {
			config = configs[0];
		}
		if (config == null) {
			System.out.println("Configuration for '" + qNameString + "' is not found. ");
			return;
		}

		System.out.println("Configuration for '" + qNameString + "' is found.");

		Dictionary<String, Object> configProps = config.getProperties();
		if (configProps != null) {
			// Remove property from the properties map
			configProps.remove(propName);

			// Update the properties of the Configuration
			config.update(configProps);
		}
	}

	/**
	 * Remove all properties of a QName.
	 * 
	 * @param fullname
	 * @param tns
	 * @param localName
	 * @throws Exception
	 */
	@Descriptor("Remove all properties of a QName")
	public void rmprops( //
			@Descriptor("fully qualified name") @Parameter(absentValue = "", names = { "-fn", "--fullname" }) String fullname, //
			@Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
			@Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName //
	) throws Exception {

		fullname = normalizeFullName(fullname);

		if (fullname.isEmpty() && (tns.isEmpty() && localName.isEmpty())) {
			System.err.println("Please specify either full name (-fn or --fullname) or a combination of target namespace (-t or --tns) and local name (-n or --name).");
			return;
		}

		String qNameString = getQNameString(fullname, tns, localName);
		String filter = getFilterString(qNameString);

		Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
		Configuration config = null;
		if (configs != null && configs.length > 0) {
			config = configs[0];
		}
		if (config == null) {
			System.out.println("Configuration for '" + qNameString + "' is not found. ");
			return;
		}

		System.out.println("Configuration for '" + qNameString + "' is found.");
		config.delete();
	}

}

// @Descriptor("Add QName config command")
// public void addq( //
// @Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
// @Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName, //
// @Descriptor("propName") @Parameter(absentValue = "", names = { "-pname", "--propertyname" }) String propName, //
// @Descriptor("propValue") @Parameter(absentValue = "", names = { "-pvalue", "--propertyvalue" }) String propValue //
// ) throws Exception {
// System.out.println("QNameAdminCommand.addq()");
//
// String qname = new QName(tns, localName).toString();
//
// // String filter = "(&(service.factoryPid=qname.service.admin)(tns=" + tns + ")(localName=" + localName + "))";
// String filter = "(&(service.factoryPid=qname.service.admin)(qname=" + qname + "))";
// Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
// System.out.println("filter=" + filter);
//
// Configuration config = null;
// if (configs != null && configs.length > 0) {
// config = configs[0];
// }
//
// if (config == null) {
// config = configAdmin.createFactoryConfiguration(PropertiesConfigServiceFactory.SERVICE_PID);
// System.out.println("Configuration is not found. New Configuration is created.");
// } else {
// System.out.println("Configuration is found.");
// }
//
// Dictionary<String, Object> configProps = config.getProperties();
// if (configProps == null) {
// configProps = new Hashtable<String, Object>();
// }
// configProps.put("qname", new QName(tns, localName).toString());
// configProps.put(propName, propValue);
//
// config.update(configProps);
// }

// @Descriptor("Remove QName config command")
// public void rmq( //
// @Descriptor("target namespace") @Parameter(absentValue = "", names = { "-t", "--tns" }) String tns, //
// @Descriptor("local name") @Parameter(absentValue = "", names = { "-n", "--name" }) String localName //
// ) throws Exception {
// System.out.println("QNameAdminCommand.rmq()");
// String qname = new QName(tns, localName).toString();
//
// // String filter = "(&(service.factoryPid=qname.service.admin)(tns=" + tns + ")(localName=" + localName + "))";
// String filter = "(&(service.factoryPid=qname.service.admin)(qname=" + qname + "))";
// Configuration[] configs = ConfigurationUtil.getFactoryConfiguration(configAdmin, filter);
// System.out.println("filter=" + filter);
//
// Configuration config = null;
// if (configs != null && configs.length > 0) {
// config = configs[0];
// }
//
// if (config == null) {
// System.out.println("Configuration is not found.");
//
// } else {
// for (Configuration currConfig : configs) {
// System.out.println("Configuration is found.");
// currConfig.delete();
// }
// }
// }
// @Descriptor("List configurations")
// public void lconfigs_old() throws Exception {
// System.out.println("PropertiesConfigCommand.lconfigs()");
// ServiceReference<ConfigurationAdmin> configAdminRef = this.bundleContext.getServiceReference(ConfigurationAdmin.class);
// if (configAdminRef == null) {
// System.err.println("ConfigurationAdmin is not available.");
// return;
// }
// try {
// ConfigurationAdmin configAdmin = this.bundleContext.getService(configAdminRef);
// ConfigurationUtil.listConfigurations(configAdmin, null);
// } finally {
// if (configAdminRef != null) {
// this.bundleContext.ungetService(configAdminRef);
// }
// }
// }

// @Descriptor("List configurations")
// public void lconfigs() throws Exception {
// System.out.println("PropertiesConfigCommand.lconfigs()");
// ConfigurationUtil.listConfigurations(this.configAdmin, null);
//
// System.out.println();
// }
