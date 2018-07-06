package org.orbit.platform.runtime.cli;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.core.Platform;
import org.orbit.platform.runtime.processes.ProcessException;
import org.orbit.platform.runtime.processes.ProcessHandler;
import org.orbit.platform.runtime.processes.ProcessManager;
import org.orbit.platform.runtime.programs.Program;
import org.orbit.platform.runtime.programs.ProgramBundle;
import org.orbit.platform.runtime.programs.ProgramHandler;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.runtime.programs.manifest.ProgramManifest;
import org.orbit.platform.runtime.util.CommonModelHelper;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.command.CommandActivator;
import org.orbit.platform.sdk.relaycontrol.WSRelayControl;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.extensions.ExtensionActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.core.IExtensionService;
import org.origin.common.launch.LaunchConfig;
import org.origin.common.launch.LaunchService;
import org.origin.common.launch.LaunchType;
import org.origin.common.osgi.BundleHelper;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.service.WebServiceAware;
import org.origin.common.service.WebServiceAwareHelper;
import org.origin.common.service.WebServiceAwareRegistry;
import org.origin.common.util.PrettyPrinter;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * 
 */
public class PlatformCommand implements Annotated, CommandActivator {

	public static final String ID = "org.orbit.platform.runtime.cli.PlatformCommand";

	protected static String[] EXTENSION_COLUMNS = new String[] { "Extension Type Id", "Extension Id", "Name", "Description" };
	protected static String[] INTERFACE_COLUMNS = new String[] { "Name", "Singleton", "Autostart", "Interface Class Name", "Interface Impl Class", "Parameters" };
	protected static String[] PROCESS_COLUMNS = new String[] { "Process Id", "Name", "Runtime State" };
	protected static String[] WEB_SERVICE_COLUMNS = new String[] { "Name", "Base URL" };
	protected static String[] LAUNCH_TYPE_COLUMNS = new String[] { "Id", "Name" };
	protected static String[] LAUNCH_CONFIG_COLUMNS = new String[] { "Name", "Type Id", "Location", "Attributes" };

	protected static Logger LOG = LoggerFactory.getLogger(PlatformCommand.class);

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();

	@org.origin.common.annotation.Dependency
	protected Platform platform;

	/**
	 * Activate the command.
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.bundleContext = bundleContext;

		this.properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_HOST_URL);
		PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_INDEX_SERVICE_URL);

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "platform");
		props.put("osgi.command.function",
				new String[] { //
						"lhost", "lendpoints", //

						// "lextensions", "linterfaces", //

						"lprocs", "create_proc", "start_proc", "stop_proc", "exit_proc", //
						"start_all_procs", "stop_all_procs", "exit_all_procs", //

						"lprograms", "installapp", "uninstallapp", //
						"activateapp", "deactivateapp", //
						"startapp", "stopapp", //

				// "llaunchtypes", "llaunchconfigs", "createlaunchconfig", "deletelaunchconfig" //
				});
		OSGiServiceUtil.register(bundleContext, PlatformCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Deactivate the command.
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(PlatformCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		this.bundleContext = null;
	}

	@DependencyFullfilled
	public void platformSet() {
		LOG.info("platformSet()");
		LOG.info("Platform is " + this.platform);
	}

	@DependencyUnfullfilled
	public void platformUnset() {
		LOG.info("Platform service is unset.");
	}

	protected void checkPlatform() {
		// Platform platform = Activator.getInstance().getPlatform();
		if (this.platform == null) {
			throw new IllegalStateException("Platform is null");
		}
	}

	protected ProgramsAndFeatures getProgramsAndFeatures() {
		checkPlatform();
		return this.platform.getProgramsAndFeatures();
	}

	protected IExtensionService getExtensionService() {
		return ExtensionActivator.getDefault().getExtensionService();
	}

	protected ProcessManager getProcessManager() {
		checkPlatform();
		return this.platform.getProcessManager();
	}

	// protected String getRealm() {
	// checkPlatform();
	// return this.platform.getRealm();
	// }

	public void lhost() {
		String hostURL = this.platform.getHostURL();
		System.out.println(hostURL);
	}

	public void lendpoints() {
		WebServiceAware[] webServiceAwares = WebServiceAwareRegistry.getInstance().getServices();
		String[][] records = new String[webServiceAwares.length][WEB_SERVICE_COLUMNS.length];
		int index = 0;
		for (WebServiceAware webServiceAware : webServiceAwares) {
			records[index++] = new String[] { webServiceAware.getName(), WebServiceAwareHelper.INSTANCE.getURL(webServiceAware) };
		}
		PrettyPrinter.prettyPrint(WEB_SERVICE_COLUMNS, records);
	}

	/**
	 * List all processes.
	 * 
	 */
	public void lprocs() {
		IProcess[] processes = getProcessManager().getProcesses();
		String[][] records = new String[processes.length][PROCESS_COLUMNS.length];
		int index = 0;
		for (IProcess process : processes) {
			int pid = process.getPID();
			ProcessHandler.RUNTIME_STATE runtimeState = getProcessManager().getProcessHandler(pid).getRuntimeState();
			records[index++] = new String[] { String.valueOf(process.getPID()), process.getName(), runtimeState.getValue() };
		}
		PrettyPrinter.prettyPrint(PROCESS_COLUMNS, records);
	}

	/**
	 * 
	 * @param extensionTypeId
	 * @param extensionId
	 * @param params
	 * @throws ClientException
	 */
	public void create_process( //
			// Parameters
			@Descriptor("Extension type id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = "") String extensionTypeId, //
			@Descriptor("Extension id") @Parameter(names = { "-id", "--id" }, absentValue = "") String extensionId, //
			@Descriptor("Parameters") @Parameter(names = { "-params", "--params" }, absentValue = "") String params //
	) throws ClientException {
		try {
			IExtension extension = getExtensionService().getExtension(extensionTypeId, extensionId);
			if (extension == null) {
				System.out.println("Extension (typeId='" + extensionTypeId + "', id='" + extensionId + "') does not exist.");
				return;
			}

			Map<Object, Object> properties = CommonModelHelper.INSTANCE.toMap(params);
			int pid = getProcessManager().createProcess(extension, properties);
			System.out.println("pid: " + pid);

		} catch (ProcessException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param pid
	 * @param async
	 * @throws ClientException
	 */
	public void start_proc( //
			// Parameters
			@Descriptor("Process id") @Parameter(names = { "-pid", "--pid" }, absentValue = "") int pid, //
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		try {
			boolean succeed = getProcessManager().startProcess(pid, async);
			if (!async) {
				System.out.println("started: " + succeed);
			}

		} catch (ProcessException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param async
	 * @throws ClientException
	 */
	public void start_all_procs(
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		IProcess[] processes = getProcessManager().getProcesses();
		int numProcesses = processes.length;
		int numSucceeded = 0;
		for (IProcess process : processes) {
			try {
				boolean succeed = getProcessManager().startProcess(process.getPID(), async);
				if (succeed) {
					numSucceeded++;
				}
			} catch (ProcessException e) {
				// e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		if (!async) {
			System.out.println("Total started: " + numSucceeded + "/" + numProcesses);
		}
	}

	/**
	 * 
	 * @param pid
	 * @param async
	 * @throws ClientException
	 */
	public void stop_proc(
			// Parameters
			@Descriptor("Process id") @Parameter(names = { "-pid", "--pid" }, absentValue = "") int pid, //
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		try {
			boolean succeed = getProcessManager().stopProcess(pid, async);
			if (!async) {
				System.out.println("stopped: " + succeed);
			}

		} catch (ProcessException e) {
			// e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	/**
	 * 
	 * @param async
	 * @throws ClientException
	 */
	public void stop_all_procs(
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		IProcess[] processes = getProcessManager().getProcesses();
		int numProcesses = processes.length;
		int numSucceeded = 0;
		for (IProcess process : processes) {
			try {

				boolean succeed = getProcessManager().stopProcess(process.getPID(), async);
				if (succeed) {
					numSucceeded++;
				}
			} catch (ProcessException e) {
				// e.printStackTrace();
				System.err.println(e.getMessage());
			}
		}
		if (!async) {
			System.out.println("Total stopped: " + numSucceeded + "/" + numProcesses);
		}
	}

	/**
	 * 
	 * @param pid
	 * @param async
	 * @throws ClientException
	 */
	public void exit_proc(
			// Parameters
			@Descriptor("Process id") @Parameter(names = { "-pid", "--pid" }, absentValue = "") int pid, //
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		try {
			boolean succeed = getProcessManager().exitProcess(pid, async);
			if (!async) {
				System.out.println("exited: " + succeed);
			}
		} catch (ProcessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param async
	 * @throws ClientException
	 */
	public void exit_all_procs(
			// Options
			@Descriptor("Async") @Parameter(names = { "-async", "--async" }, absentValue = "false", presentValue = "true") boolean async //
	) throws ClientException {
		IProcess[] processes = getProcessManager().getProcesses();
		int numProcesses = processes.length;
		int numSucceeded = 0;
		for (IProcess process : processes) {
			try {
				boolean succeed = getProcessManager().exitProcess(process.getPID(), async);
				if (succeed) {
					numSucceeded++;
				}
			} catch (ProcessException e) {
				e.printStackTrace();
			}
		}
		if (!async) {
			System.out.println("Total exited: " + numSucceeded + "/" + numProcesses);
		}
	}

	@Descriptor("List programs")
	public void lprograms() throws ClientException {
		LOG.info("lprograms()");

		ProgramHandler[] programHandlers = getProgramsAndFeatures().getProgramHandlers();
		int num = (programHandlers != null) ? programHandlers.length : 0;

		LOG.info("Number of apps: " + num);
		if (programHandlers != null) {
			for (ProgramHandler programHandler : programHandlers) {
				LOG.info("------------------------------------------------------------");
				ProgramManifest appManifest = programHandler.getManifest();
				String appId = appManifest.getId();
				String appVersion = appManifest.getVersion();
				Program app = programHandler.getProgram();

				String appText = appId + "_" + appVersion + " (Activation State: " + programHandler.getRuntimeState().toString() + ")";
				if (app != null) {
					// DependencySet.STATE dependencySetState = runtimeApp.getDependencySet().getState();
					Program.RUNTIME_STATE appRuntimeState = app.getRuntimeState();
					// appText += " (DependencySet State: " + dependencySetState.toString() + ")";
					appText += " (Runtime State: " + appRuntimeState.toString() + ")";
				}
				LOG.info(appText);

				if (app != null) {
					// String appBundleText = " " + app.getSimpleName();
					// appBundleText += " (Runtime State: " + app.getRuntimeState().toString() + ")";
					// System.out.println(appBundleText);

					Bundle[] osgiBundles = bundleContext.getBundles();

					LOG.info("    Bundles:");
					for (ProgramBundle appBundle : app.getProgramBundles()) {
						String bundleName = appBundle.getBundleName();
						String bundleVersion = appBundle.getBundleVersion();
						// Dependency.STATE dependencyState = appBundle.getDependency().getState();
						ProgramBundle.RUNTIME_STATE appBundleRuntimeState = appBundle.getRuntimeState();

						Bundle osgiBundle = BundleHelper.INSTANCE.findBundle(osgiBundles, bundleName, bundleVersion);

						// long bundleId = -1;
						// if (osgiBundle != null) {
						// bundleId = osgiBundle.getBundleId();
						// }

						// String appModuleText = " (" + bundleId + ") " + (bundleName + " (" + bundleVersion + ")");
						String appModuleText = "        " + osgiBundle.toString();
						// appBundleText += " (Dependency State: " + dependencyState.toString() + ")";
						appModuleText += " (Runtime State: " + appBundleRuntimeState.toString() + ")";
						LOG.info(appModuleText);
					}
				}
			}
		}

		LOG.info("");
	}

	/**
	 * 
	 * @param location
	 * @throws ClientException
	 */
	@Descriptor("Install a program")
	public void installapp(
			// Parameters
			@Descriptor("app file location") @Parameter(absentValue = "", names = { "-location", "--location" }) String location //
	) throws ClientException {
		LOG.info("installapp()");
		LOG.info("appFileLocation = " + location);

		if (location != null && !location.isEmpty()) {
			// install app archive file from file system
			Path appArchivePath = Paths.get(location);
			try {
				ProgramManifest appManifest = getProgramsAndFeatures().install(appArchivePath);
				if (appManifest != null) {
					LOG.info("installapp() " + appManifest.getSimpleName() + " is installed.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// install app from app store
		}

		LOG.info("");
	}

	@Descriptor("Uninstall a program")
	public void uninstallapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("uninstallapp()");
		LOG.info("appId = " + appId);
		LOG.info("appVersion = " + appVersion);

		try {
			ProgramManifest appManifest = getProgramsAndFeatures().uninstall(appId, appVersion);
			if (appManifest != null) {
				LOG.info("uninstallapp() " + appManifest.getSimpleName() + " is uninstalled.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Activate a program")
	public void activateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("activateapp()");

		try {
			ProgramsAndFeatures programsAndFeatures = getProgramsAndFeatures();
			if (!programsAndFeatures.isInstalled(appId, appVersion)) {
				LOG.info("Program is not installed.");
				return;
			}

			ProgramHandler programHandler = getProgramsAndFeatures().getProgramHandler(appId, appVersion);
			if (programHandler == null) {
				LOG.info("ProgramHandler is not found.");
				return;
			}

			programHandler.activate();
			LOG.info(programHandler.getManifest().getSimpleName() + " is activated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Deactivate a program")
	public void deactivateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("deactivateapp()");

		try {
			ProgramsAndFeatures programsAndFeatures = getProgramsAndFeatures();
			if (!programsAndFeatures.isInstalled(appId, appVersion)) {
				LOG.info("Program is not installed.");
				return;
			}

			ProgramHandler programHandler = getProgramsAndFeatures().getProgramHandler(appId, appVersion);
			if (programHandler == null) {
				LOG.info("ProgramHandler is not found.");
				return;
			}

			programHandler.deactivate();
			LOG.info(programHandler.getManifest().getSimpleName() + " is deactivated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Start a program")
	public void startapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("startapp()");

		try {
			ProgramsAndFeatures programsAndFeatures = getProgramsAndFeatures();
			if (!programsAndFeatures.isInstalled(appId, appVersion)) {
				LOG.info("Program is not installed.");
				return;
			}

			ProgramHandler programHandler = programsAndFeatures.getProgramHandler(appId, appVersion);
			if (programHandler == null) {
				LOG.info("ProgramHandler is not found.");
				return;
			}

			// Activate the ProgramHandler if needed
			if (!programHandler.getRuntimeState().isActivated()) {
				programHandler.activate();
			}

			programHandler.start();
			LOG.info(programHandler.getManifest().getSimpleName() + " is started.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Stop a program")
	public void stopapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("stopapp()");

		try {
			ProgramsAndFeatures appsManager = getProgramsAndFeatures();
			if (!appsManager.isInstalled(appId, appVersion)) {
				LOG.info("App is not installed.");
				return;
			}

			ProgramHandler appHandler = appsManager.getProgramHandler(appId, appVersion);
			if (appHandler == null) {
				LOG.info("AppHandler is not found.");
				return;
			}
			appHandler.stop();
			LOG.info(appHandler.getManifest().getSimpleName() + " is stopped.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	public String getHostURL() {
		String globalHostURL = (String) this.properties.get(PlatformConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	/**
	 * 
	 * @param extensionId
	 * @param contextRoot
	 * @param targetHostURLsString
	 */
	public void startrelay(String extensionId, String contextRoot, String targetHostURLsString) {
		LOG.info("startrelay('" + extensionId + "')");

		IExtensionService service = getExtensionService();

		// Start ws relay
		IExtension relayExtension = service.getExtension(WSRelayControl.EXTENSION_TYPE_ID, extensionId);
		if (relayExtension == null) {
			LOG.info("Program extension is not available.");
			return;
		}
		WSRelayControl relayControl = relayExtension.getInterface(WSRelayControl.class);
		if (relayControl == null) {
			LOG.info("WSRelayControl is not available.");
			return;
		}

		String hostURL = getHostURL();
		List<URI> targetURIs = URIUtil.toList(targetHostURLsString, contextRoot);
		relayControl.start(bundleContext, wsClientFactory, hostURL, contextRoot, targetURIs);
	}

	/**
	 * 
	 * @param extensionId
	 * @param contextRoot
	 */
	public void stoprelay(String extensionId, String contextRoot) {
		LOG.info("stoprelay('" + extensionId + "')");

		IExtensionService service = getExtensionService();
		IExtension extension = service.getExtension(WSRelayControl.EXTENSION_TYPE_ID, extensionId);
		if (extension == null) {
			LOG.info("Program extension is not available.");
			return;
		}

		WSRelayControl relayControl = extension.getInterface(WSRelayControl.class);
		if (relayControl == null) {
			LOG.info("WSRelayControl is not available.");
			return;
		}

		String hostURL = getHostURL();
		relayControl.stop(bundleContext, hostURL, contextRoot);
	}

	@Descriptor("List launch types")
	public void llaunchtypes() {
		LaunchService launchService = org.origin.common.launch.LaunchActivator.getDefault().getLaunchService();
		if (launchService == null) {
			LOG.error("ILaunchService is null.");
			return;
		}

		LaunchType[] launchTypes = launchService.getLaunchTypes();
		String[][] records = new String[launchTypes.length][LAUNCH_TYPE_COLUMNS.length];
		int index = 0;
		for (LaunchType launchType : launchTypes) {
			records[index++] = new String[] { launchType.getId(), launchType.getName() };
		}
		PrettyPrinter.prettyPrint(LAUNCH_TYPE_COLUMNS, records);
	}

	@Descriptor("List launch configurations")
	public void llaunchconfigs() {
		LaunchService launchService = org.origin.common.launch.LaunchActivator.getDefault().getLaunchService();
		if (launchService == null) {
			LOG.error("ILaunchService is null.");
			return;
		}

		try {
			LaunchConfig[] launchConfigs = launchService.getLaunchConfigurations();
			String[][] records = new String[launchConfigs.length][LAUNCH_CONFIG_COLUMNS.length];
			int index = 0;
			for (LaunchConfig launchConfig : launchConfigs) {
				String name = launchConfig.getName();
				String typeId = launchConfig.getTypeId();
				File file = launchConfig.getFile();
				Map<String, Object> attributes = launchConfig.getAttributes();

				String attrString = "";
				if (attributes != null) {
					int index1 = 0;
					for (Iterator<String> itor = attributes.keySet().iterator(); itor.hasNext();) {
						String attrName = itor.next();
						Object attrValue = attributes.get(attrName);
						if (index1 > 0) {
							attrString += ",";
						}
						attrString += ((attrValue != null) ? "[" + attrValue.getClass().getSimpleName() + "] " + attrValue.toString() : "null");
						index1++;
					}
				}
				records[index++] = new String[] { name, typeId, file.getAbsolutePath(), attrString };
			}
			PrettyPrinter.prettyPrint(LAUNCH_CONFIG_COLUMNS, records);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Create launch configuration")
	public void createlaunchconfig( //
			// Parameters
			@Descriptor("launch type id") @Parameter(absentValue = "", names = { "-typeId", "--typeId" }) String typeId, //
			@Descriptor("launch config name") @Parameter(absentValue = "", names = { "-name", "--name" }) String name //
	) {
		LaunchService launchService = org.origin.common.launch.LaunchActivator.getDefault().getLaunchService();
		if (launchService == null) {
			LOG.error("ILaunchService is null.");
			return;
		}
		try {
			LaunchConfig launchConfig = launchService.createLaunchConfiguration(typeId, name);
			if (launchConfig != null) {
				LOG.info("Launch configuration is created.");
			} else {
				LOG.info("Launch configuration is not created.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Descriptor("Delete launch configuration")
	public void deletelaunchconfig( //
			// Parameters
			@Descriptor("launch config name") @Parameter(absentValue = "", names = { "-name", "--name" }) String name //
	) {
		LaunchService launchService = org.origin.common.launch.LaunchActivator.getDefault().getLaunchService();
		if (launchService == null) {
			LOG.error("ILaunchService is null.");
			return;
		}
	}

}

/// **
// * List all extensions.
// *
// */
// public void lextensions() {
// IExtension[] extensions = getExtensionService().getExtensions();
// String[][] records = new String[extensions.length][EXTENSION_COLUMNS.length];
// int index = 0;
// for (IExtension extension : extensions) {
// records[index++] = new String[] { extension.getTypeId(), extension.getId(), extension.getName(), extension.getDescription() };
// }
// PrettyPrinter.prettyPrint(EXTENSION_COLUMNS, records);
// }
//
/// **
// * List interfaces of an extension.
// *
// * @param extensionTypeId
// * @param extensionId
// */
// public void lextensioninterfaces_v1( //
// // Parameters
// @Descriptor("Extension type id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = "") String extensionTypeId, //
// @Descriptor("Extension id") @Parameter(names = { "-id", "--id" }, absentValue = "") String extensionId //
// ) {
//
// IExtension extension = getExtensionService().getExtension(extensionTypeId, extensionId);
// if (extension == null) {
// System.out.println("Extension (typeId='" + extensionTypeId + "', id='" + extensionId + "') does not exist.");
// return;
// }
//
// Object[] interfaces = extension.getInterfaces();
// String[][] records = new String[interfaces.length][INTERFACE_COLUMNS.length];
// int index = 0;
// for (Object interfaceObj : interfaces) {
// InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
// String parameters = ProgramExtensionHelper.INSTANCE.toParametersString(desc.getParameters());
// records[index++] = new String[] { desc.getName(), String.valueOf(desc.isSingleton()), String.valueOf(desc.isAutoStart()), parameters };
// }
// PrettyPrinter.prettyPrint(INTERFACE_COLUMNS, records);
// }
//
/// **
// * List interfaces of an extension.
// *
// * @param extensionTypeId
// * @param extensionId
// */
// public void linterfaces( //
// // Parameters
// @Descriptor("Extension type id") @Parameter(names = { "-typeId", "--typeId" }, absentValue = "") String extensionTypeId, //
// @Descriptor("Extension id") @Parameter(names = { "-id", "--id" }, absentValue = "") String extensionId //
// ) {
// if ("".equals(extensionTypeId) || "".equals(extensionId)) {
// IExtension[] extensions = getExtensionService().getExtensions();
// for (IExtension extension : extensions) {
// linterfaces(extension);
// System.out.println();
// }
//
// } else if (!"".equals(extensionTypeId) && "".equals(extensionId)) {
// IExtension[] extensions = getExtensionService().getExtensions(extensionTypeId);
// for (IExtension extension : extensions) {
// linterfaces(extension);
// System.out.println();
// }
//
// } else if (!"".equals(extensionTypeId) && !"".equals(extensionId)) {
// IExtension extension = getExtensionService().getExtension(extensionTypeId, extensionId);
// if (extension == null) {
// System.out.println("Extension (typeId='" + extensionTypeId + "', id='" + extensionId + "') does not exist.");
// return;
// }
// linterfaces(extension);
// }
// }
//
/// **
// *
// * @param extension
// */
// protected void linterfaces(IExtension extension) {
// if (extension == null) {
// return;
// }
// Object[] interfaces = extension.getInterfaces();
//
// int numRecords = 0;
// for (Object interfaceObj : interfaces) {
// InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
// org.origin.common.extensions.Parameter[] parameters = desc.getParameters();
//
// if (parameters.length == 0) {
// numRecords += 1;
// } else {
// numRecords += parameters.length;
// }
// }
//
// String[][] records = new String[numRecords][INTERFACE_COLUMNS.length];
// int index = 0;
// for (Object interfaceObj : interfaces) {
// InterfaceDescription desc = extension.getInterfaceDescription(interfaceObj);
// org.origin.common.extensions.Parameter[] parameters = desc.getParameters();
//
// String name = desc.getName();
// String isSingleton = String.valueOf(desc.isSingleton());
// String isAutoStart = String.valueOf(desc.isAutoStart());
// String interfaceClassName = desc.getInterfaceClassName();
// Class<?> interfaceImplClass = desc.getInterfaceImplClass();
// String interfaceImplClassName = interfaceImplClass != null ? interfaceImplClass.getName() : null;
// // String interfaceObject = (desc.getInterfaceInstance() != null) ? desc.getInterfaceInstance().toString() : null;
//
// if (parameters.length == 0) {
// records[index++] = new String[] { name, isSingleton, isAutoStart, interfaceClassName, interfaceImplClassName, null };
//
// } else {
// for (int i = 0; i < parameters.length; i++) {
// org.origin.common.extensions.Parameter parameter = parameters[i];
// if (i == 0) {
// records[index++] = new String[] { name, isSingleton, isAutoStart, interfaceClassName, interfaceImplClassName, parameter.getLabel() };
// } else {
// records[index++] = new String[] { "", "", "", "", "", parameter.getLabel() };
// }
// }
// }
// }
//
// System.out.println("Extension:");
// System.out.println(extension);
//
// System.out.println("\r\nInterfaces:");
// PrettyPrinter.prettyPrint(INTERFACE_COLUMNS, records);
// }
