package org.origin.common.launch.cli;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.origin.common.annotation.Annotated;
import org.origin.common.launch.LaunchActivator;
import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchConstants;
import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;
import org.origin.common.launch.Launcher;
import org.origin.common.launch.ProcessInstance;
import org.origin.common.launch.util.LaunchExtensionHelper;
import org.origin.common.launch.util.LaunchExtensionHelper.LauncherExtension;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.util.PrettyPrinter;
import org.osgi.framework.BundleContext;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * @see PlatformCommand
 */
public class LaunchCommand implements Annotated {

	protected static String[] LAUNCH_TYPE_COLUMNS = new String[] { "Id", "Name" };
	protected static String[] LAUNCHER_COLUMNS = new String[] { "Id", "TypeId", "Class" };
	protected static String[] LAUNCH_CONFIG_COLUMNS = new String[] { "Name", "Type Id", "Location", "Attributes" };
	protected static String[] RUNTIME_LAUNCH_COLUMNS = new String[] { "Runtime Id", "Configuration Name", "Working Directory", "Configuration Location", "Processes" };

	public static LaunchCommand INSTANCE = new LaunchCommand();

	// protected static Logger LOG = LoggerFactory.getLogger(AppCommand.class);
	public static class LOG {
		public static void info(String message) {
			System.out.println(message);
		}

		public static void error(String message) {
			System.err.println(message);
		}
	}

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
		props.put("osgi.command.scope", "platform");
		props.put("osgi.command.function",
				new String[] { //
						"llaunchtypes", "llaunchers", //
						"llaunchconfigs", "createlaunchconfig", "deletelaunchconfig", //
						"launch", "terminate", "llaunches", //
				});
		OSGiServiceUtil.register(bundleContext, LaunchCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Deactivate the command.
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(LaunchCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		this.bundleContext = null;
	}

	@Descriptor("List launch types")
	public void llaunchtypes() {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LaunchType[] launchTypes = launchService.getLaunchTypes();
			String[][] records = new String[launchTypes.length][LAUNCH_TYPE_COLUMNS.length];
			int index = 0;
			for (LaunchType launchType : launchTypes) {
				records[index++] = new String[] { launchType.getId(), launchType.getName() };
			}
			PrettyPrinter.prettyPrint(LAUNCH_TYPE_COLUMNS, records);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List launchers")
	public void llaunchers() {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LauncherExtension[] launcherExtensions = LaunchExtensionHelper.INSTANCE.getLauncherExtensions();
			String[][] records = new String[launcherExtensions.length][LAUNCHER_COLUMNS.length];
			int index = 0;
			for (LauncherExtension launcherExtension : launcherExtensions) {
				Launcher launcher = launcherExtension.getLauncher();
				String launcherClassName = (launcher != null) ? launcher.getClass().getName() : "(n/a)";
				records[index++] = new String[] { launcherExtension.getId(), launcherExtension.getTypeId(), launcherClassName };
			}
			PrettyPrinter.prettyPrint(LAUNCHER_COLUMNS, records);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List launch configurations")
	public void llaunchconfigs() {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LaunchConfig[] launchConfigs = launchService.getLaunchConfigurations();

			int totalLength = 0;
			for (LaunchConfig launchConfig : launchConfigs) {
				Map<String, Object> attributes = launchConfig.getAttributes();
				if (attributes.isEmpty()) {
					totalLength += 1;
				} else {
					totalLength += attributes.size();
				}
			}

			String[][] records = new String[totalLength][LAUNCH_CONFIG_COLUMNS.length];
			int index = 0;
			for (LaunchConfig launchConfig : launchConfigs) {
				String name = launchConfig.getName();
				String typeId = launchConfig.getTypeId();
				File file = launchConfig.getFile();
				Map<String, Object> attributes = launchConfig.getAttributes();

				if (attributes.isEmpty()) {
					records[index++] = new String[] { name, typeId, file.getAbsolutePath(), "" };

				} else {
					int i = 0;
					for (Iterator<String> itor = attributes.keySet().iterator(); itor.hasNext();) {
						String attrName = itor.next();
						Object attrValue = attributes.get(attrName);
						String attrString = attrName + "=" + ((attrValue != null) ? attrValue.toString() : "null");

						if (i == 0) {
							records[index++] = new String[] { name, typeId, file.getAbsolutePath(), attrString };
						} else {
							records[index++] = new String[] { "", "", "", attrString };
						}
						i++;
					}
				}
			}
			PrettyPrinter.prettyPrint(LAUNCH_CONFIG_COLUMNS, records);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create launch configuration")
	public void createlaunchconfig( //
			// Parameters
			@Descriptor("launch type id") @Parameter(absentValue = "", names = { "-typeId", "--typeId" }) String typeId, //
			@Descriptor("launch config name") @Parameter(absentValue = "", names = { "-name", "--name" }) String name //
	) {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LaunchConfig launchConfig = launchService.createLaunchConfiguration(typeId, name);
			if (launchConfig != null) {
				LOG.info("Launch configuration is created.");
			} else {
				LOG.info("Launch configuration is not created.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete launch configuration")
	public void deletelaunchconfig( //
			// Parameters
			@Descriptor("launch config name") @Parameter(absentValue = "", names = { "-name", "--name" }) String name //
	) {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Descriptor("List runtime launches")
	public void llaunches() {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LaunchInstance[] launchHandlers = launchService.getLaunchInstances();

			int totalLength = 0;
			for (LaunchInstance currLaunchHandler : launchHandlers) {
				ProcessInstance[] runtimeProcesses = currLaunchHandler.getProcessInstances();
				if (runtimeProcesses.length == 0) {
					totalLength += 1;
				} else {
					totalLength += runtimeProcesses.length;
				}
			}

			String[][] records = new String[totalLength][RUNTIME_LAUNCH_COLUMNS.length];
			int index = 0;
			for (LaunchInstance currLaunchHandler : launchHandlers) {
				String launchConfigName = currLaunchHandler.getLaunchConfiguration().getName();
				String runtimeLaunchId = currLaunchHandler.getAttribute(LaunchConstants.LAUNCH_INSTANCE_ID);
				String workingDirLocation = currLaunchHandler.getAttribute(LaunchConstants.WORKING_DIRECTORY);

				if (workingDirLocation == null) {
					workingDirLocation = "(n/a)";
				}
				String configLocation = currLaunchHandler.getAttribute(LaunchConstants.CONFIG_LOCATION);
				if (configLocation == null) {
					configLocation = "(n/a)";
				}

				ProcessInstance[] processHandlers = currLaunchHandler.getProcessInstances();
				if (processHandlers.length == 0) {
					records[index++] = new String[] { runtimeLaunchId, launchConfigName, workingDirLocation, configLocation, "" };

				} else {
					int index1 = 0;
					for (ProcessInstance currRuntimeProcesse : processHandlers) {
						Process currSysProcess = currRuntimeProcesse.getSystemProcess();
						boolean isAlive = currSysProcess.isAlive();
						String processStr = currSysProcess.toString() + " (Alive: " + isAlive + ")";
						if (index1 == 0) {
							records[index++] = new String[] { runtimeLaunchId, launchConfigName, workingDirLocation, configLocation, processStr };
						} else {
							records[index++] = new String[] { "", "", "", processStr };
						}
						index1++;
					}
				}
			}
			PrettyPrinter.prettyPrint(RUNTIME_LAUNCH_COLUMNS, records);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * e.g. launch -typeId node -name Eclipse
	 * 
	 * @param typeId
	 * @param name
	 */
	@Descriptor("Launch")
	public void launch( //
			// Parameters
			@Descriptor("launch type id") @Parameter(absentValue = "", names = { "-typeId", "--typeId" }) String typeId, //
			@Descriptor("Launch configuration name") @Parameter(absentValue = "", names = { "-name", "--name" }) String name //
	) {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			LaunchConfig launchConfig = launchService.getLaunchConfiguration(typeId, name);
			if (launchConfig == null) {
				LOG.error("LaunchConfiguration is not found.");
				return;
			}

			LaunchInstance launchHandler = launchConfig.launch();
			if (launchHandler != null) {
				System.out.println("Launch is created.");
			} else {
				System.out.println("Launch is not created.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * terminate -id Eclipse_1
	 * 
	 * @param id
	 */
	@Descriptor("Terminate")
	public void terminate(
			// Options
			@Descriptor("Terminate all") @Parameter(names = { "-all", "--all" }, absentValue = "false", presentValue = "true") boolean all, //
			// Parameters
			@Descriptor("Runtime launch id") @Parameter(names = { "-id", "--id" }, absentValue = "") String id //
	) {
		try {
			LaunchService launchService = LaunchActivator.getDefault().getLaunchService();
			if (launchService == null) {
				LOG.error("LaunchService is null.");
				return;
			}

			if (all) {
				for (LaunchInstance launchHandler : launchService.getLaunchInstances()) {
					String currId = launchHandler.getId();
					try {
						launchHandler.terminate();
						System.out.println("Launch '" + currId + "' is terminated.");

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Launch '" + currId + "' is not terminated.");
					}
				}

			} else {
				LaunchInstance launchHandler = launchService.getLaunchInstance(id);
				if (launchHandler == null) {
					LOG.error("Launch handler is not found.");
					return;
				}
				String currId = launchHandler.getId();
				try {
					launchHandler.terminate();
					System.out.println("Launch '" + currId + "' is terminated.");

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Launch '" + currId + "' is not terminated.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

// boolean init = false;
// if (init) {
// config.setAttribute(LaunchConstants.WORKING_DIRECTORY, "/Users/yayang/origin/ta1/nodespaces/node0");
// config.setAttribute(LaunchConstants.CONFIG_LOCATION, "/Users/yayang/origin/ta1/nodespaces/node0/configuration");
//
// Map<String, String> systemArguments = new LinkedHashMap<String, String>();
// systemArguments.put("-jar", "/Users/yayang/origin/ta1/plugins/org.eclipse.osgi_3.10.100.v20150529-1857.jar");
// systemArguments.put("-configuration", "file:/Users/yayang/origin/ta1/nodespaces/node0/configuration");
// config.setAttribute(LaunchConstants.SYSTEM_ARGUMENTS_MAP, systemArguments);
//
// Map<String, String> vmArguments = new LinkedHashMap<String, String>();
// // vmArguments.put("-Declipse.ignoreApp", "true");
// // vmArguments.put("-Dosgi.noShutdown", "true");
// config.setAttribute(LaunchConstants.VM_ARGUMENTS_MAP, vmArguments);
//
// Map<String, String> configProperties = new LinkedHashMap<String, String>();
// configProperties.put("eclipse.ignoreApp", "true");
// configProperties.put("osgi.noShutdown", "true");
// configProperties.put("osgi.bundles.defaultStartLevel", "4");
// configProperties.put("osgi.bundles", "reference:file:eclipse.equinox/org.eclipse.equinox.simpleconfigurator_1.1.0.v20131217-1203.jar@1:start");
// configProperties.put("org.eclipse.equinox.simpleconfigurator.configUrl",
// "file:/Users/yayang/origin/ta1/configuration/org.eclipse.equinox.simpleconfigurator/bundles.info");
// config.setAttribute(LaunchConstants.OSGI_CONFIG_PROPERTIES_MAP, configProperties);
//
// Map<String, String> configVMArguments = new LinkedHashMap<String, String>();
// configVMArguments.put("com.sun.management.jmxremote.port", "9999");
// configVMArguments.put("com.sun.management.jmxremote.authenticate", "false");
// configVMArguments.put("com.sun.management.jmxremote.ssl", "false");
// configVMArguments.put("org.osgi.service.http.port", "12001");
// configVMArguments.put("platform.home", "/Users/yayang/origin/ta1");
// config.setAttribute(LaunchConstants.OSGI_CONFIG_VM_ARGUMENTS_MAP, configVMArguments);
//
// List<String> programArguments = new ArrayList<String>();
// programArguments.add("-consoleLog");
// programArguments.add("-console");
// config.setAttribute(LaunchConstants.PROGRAM_ARGUMENTS_LIST, programArguments);
//
// config.save();
// }
