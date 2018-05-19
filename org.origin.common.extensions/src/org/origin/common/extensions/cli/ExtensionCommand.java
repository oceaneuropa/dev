package org.origin.common.extensions.cli;

import java.util.Hashtable;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.annotation.Annotated;
import org.origin.common.extensions.ExtensionActivator;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * 
 */
public class ExtensionCommand implements Annotated {

	protected static String[] EXTENSION_COLUMNS0 = new String[] { "Extension Type Id", "Extension Id", "Name", "Description" };
	protected static String[] EXTENSION_COLUMNS1 = new String[] { "Key", "Value" };

	protected static String[] INTERFACE_COLUMNS0 = new String[] { "Name", "Singleton", "Autostart", "Interface Class Name", "Interface Impl Class", "Parameters" };
	protected static String[] INTERFACE_COLUMNS1 = new String[] { "Key", "Value" };

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionCommand.class);

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();

	/**
	 * Activate the command.
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.bundleContext = bundleContext;

		this.properties = new Hashtable<Object, Object>();
		// PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_HOST_URL);
		// PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_INDEX_SERVICE_URL);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "origin");
		props.put("osgi.command.function",
				new String[] { //
						"lprops", //
						"lextensions", "linterfaces" //
		});
		OSGiServiceUtil.register(bundleContext, ExtensionCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Deactivate the command.
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(ExtensionCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		this.bundleContext = null;
	}

	protected IExtensionService getExtensionService() {
		return ExtensionActivator.getDefault().getExtensionService();
	}

	/**
	 * List properties.
	 * 
	 * <pre>
	 * e.g.
	 * lprops -fragment osgi,eclipse
	 * lprops -fragment platform,orbit,component
	 * lprops -prefix org.osgi.service,orbit.,platform.,component.
	 * </pre>
	 */
	public void lprops( //
			@Descriptor("Prefix") @Parameter(names = { "-prefix", "--prefix" }, absentValue = "") String prefix, //
			@Descriptor("Fragment") @Parameter(names = { "-fragment", "--fragment" }, absentValue = "") String fragment //
	) {
		LOG.debug("lprops()");

		String[] prefixes = null;
		if (prefix != null && !prefix.isEmpty()) {
			if (prefix.contains(",")) {
				prefixes = prefix.split(",");
			} else {
				prefixes = new String[] { prefix };
			}
		}

		String[] fragments = null;
		if (fragment != null && !fragment.isEmpty()) {
			if (fragment.contains(",")) {
				fragments = fragment.split(",");
			} else {
				fragments = new String[] { fragment };
			}
		}

		PropertyUtil.printSystemProperties(prefixes, fragments);
		PropertyUtil.printSystemEnvironmentVariables(prefixes, fragments);
	}

	/**
	 * List all extensions.
	 * 
	 */
	public void lextensions() {
		LOG.debug("lextensions()");
		IExtension[] extensions = getExtensionService().getExtensions();
		String[][] records = new String[extensions.length][EXTENSION_COLUMNS0.length];
		int index = 0;
		for (IExtension extension : extensions) {
			records[index++] = new String[] { extension.getTypeId(), extension.getId(), extension.getName(), extension.getDescription() };
		}
		PrettyPrinter.prettyPrint(EXTENSION_COLUMNS0, records);
	}

	/**
	 * List interfaces of an extension.
	 * 
	 * <pre>
	 * e.g.
	 * linterfaces -typeId platform.sdk.ServiceActivator
	 * linterfaces -typeId platform.sdk.ServiceActivator -id org.orbit.infra.runtime.IndexServiceActivator
	 * </pre>
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 */
	public void linterfaces( //
			// Parameters
			@Descriptor("Extension type id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = "") String extensionTypeId, //
			@Descriptor("Extension id") @Parameter(names = { "-id", "--id" }, absentValue = "") String extensionId //
	) {
		extensionTypeId = extensionTypeId.trim();
		extensionId = extensionId.trim();
		LOG.debug("linterfaces()");
		LOG.debug("typeId = '" + extensionTypeId + "'");
		LOG.debug("id = '" + extensionId + "'");

		boolean hasTypeId = !extensionTypeId.isEmpty() ? true : false;
		boolean hasId = !extensionId.isEmpty() ? true : false;

		if (!hasTypeId && !hasId) {
			IExtension[] extensions = getExtensionService().getExtensions();
			for (IExtension extension : extensions) {
				linterfaces(extension);
				System.out.println();
			}

		} else {
			if (!hasId) {
				IExtension[] extensions = getExtensionService().getExtensions(extensionTypeId);
				for (IExtension extension : extensions) {
					linterfaces(extension);
					System.out.println();
				}

			} else {
				IExtension extension = getExtensionService().getExtension(extensionTypeId, extensionId);
				if (extension == null) {
					System.out.println("Extension (typeId='" + extensionTypeId + "', id='" + extensionId + "') does not exist.");
					return;
				}
				linterfaces(extension);
			}
		}
	}

	/**
	 * 
	 * @param extension
	 */
	protected void linterfaces0(IExtension extension) {
		if (extension == null) {
			return;
		}
		Object[] interfaces = extension.getInterfaces();

		int numRecords = 0;
		for (Object interfaceObj : interfaces) {
			InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
			org.origin.common.extensions.Parameter[] parameters = desc.getParameters();

			if (parameters.length == 0) {
				numRecords += 1;
			} else {
				numRecords += parameters.length;
			}
		}

		String[][] records = new String[numRecords][INTERFACE_COLUMNS1.length];
		int index = 0;
		for (Object interfaceObj : interfaces) {
			InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
			org.origin.common.extensions.Parameter[] parameters = desc.getParameters();

			String name = desc.getName();
			String isSingleton = String.valueOf(desc.isSingleton());
			String isAutoStart = String.valueOf(desc.isAutoStart());
			String interfaceClassName = desc.getInterfaceImplClassName();
			Class<?> interfaceImplClass = desc.getInterfaceImplClass();
			String interfaceImplClassName = interfaceImplClass != null ? interfaceImplClass.getName() : null;
			// String interfaceObject = (desc.getInterfaceInstance() != null) ? desc.getInterfaceInstance().toString() : null;

			if (parameters.length == 0) {
				records[index++] = new String[] { name, isSingleton, isAutoStart, interfaceClassName, interfaceImplClassName, null };

			} else {
				for (int i = 0; i < parameters.length; i++) {
					org.origin.common.extensions.Parameter parameter = parameters[i];
					if (i == 0) {
						records[index++] = new String[] { name, isSingleton, isAutoStart, interfaceClassName, interfaceImplClassName, parameter.getLabel() };
					} else {
						records[index++] = new String[] { "", "", "", "", "", parameter.getLabel() };
					}
				}
			}
		}

		System.out.println("Extension:");
		System.out.println(extension);

		System.out.println("\r\nInterfaces:");
		PrettyPrinter.prettyPrint(INTERFACE_COLUMNS1, records);
	}

	protected void linterfaces(IExtension extension) {
		if (extension == null) {
			return;
		}
		Object[] interfaces = extension.getInterfaces();

		int numRecords = 0;
		for (Object interfaceObj : interfaces) {
			InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
			org.origin.common.extensions.Parameter[] parameters = desc.getParameters();

			if (parameters.length == 0) {
				numRecords += 6;
			} else {
				numRecords += 6 + parameters.length - 1;
			}
		}

		String[][] records = new String[numRecords][INTERFACE_COLUMNS1.length];
		int index = 0;
		for (Object interfaceObj : interfaces) {
			InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
			org.origin.common.extensions.Parameter[] parameters = desc.getParameters();

			String name = desc.getName();
			String isSingleton = String.valueOf(desc.isSingleton());
			String isAutoStart = String.valueOf(desc.isAutoStart());
			Class<?> interfaceClass = desc.getInterfaceClass();
			String interfaceImplClassName = desc.getInterfaceImplClassName();

			records[index++] = new String[] { "Name", name };
			records[index++] = new String[] { "Singleton", isSingleton };
			records[index++] = new String[] { "AutoStart", isAutoStart };
			records[index++] = new String[] { "Interface", interfaceClass != null ? interfaceClass.getName() : "(n/a)" };
			records[index++] = new String[] { "Interface Impl", interfaceImplClassName };

			if (parameters.length == 0) {
				records[index++] = new String[] { "Parameters", "(n/a)" };

			} else {

				for (int i = 0; i < parameters.length; i++) {
					org.origin.common.extensions.Parameter parameter = parameters[i];
					if (i == 0) {
						records[index++] = new String[] { "Parameters", parameter.getLabel() };
					} else {
						records[index++] = new String[] { "", parameter.getLabel() };
					}
				}
			}
		}

		System.out.println("Extension:");
		// System.out.println(extension);
		String[][] records0 = new String[4][EXTENSION_COLUMNS1.length];
		records0[0] = new String[] { "Type Id", extension.getTypeId() };
		records0[1] = new String[] { "Id", extension.getId() };
		records0[2] = new String[] { "Name", extension.getName() };
		records0[3] = new String[] { "Description", extension.getDescription() };
		PrettyPrinter.prettyPrint(EXTENSION_COLUMNS1, records0, 1);

		System.out.println("\r\nInterfaces:");
		PrettyPrinter.prettyPrint(INTERFACE_COLUMNS1, records, interfaces.length);
	}

}

// public static class LOG {
// public static void info(String message) {
// System.out.println(message);
// }
//
// public static void error(String message) {
// System.err.println(message);
// }
// }
