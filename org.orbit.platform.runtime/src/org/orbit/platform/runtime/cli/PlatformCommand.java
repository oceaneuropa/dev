package org.orbit.platform.runtime.cli;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.platform.runtime.Activator;
import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.core.Platform;
//import org.orbit.platform.runtime.gaia.service.GAIA;
//import org.orbit.platform.runtime.gaia.service.impl.GAIAImpl;
//import org.orbit.platform.runtime.platform.Platform;
import org.orbit.platform.runtime.programs.Program;
import org.orbit.platform.runtime.programs.ProgramBundle;
import org.orbit.platform.runtime.programs.ProgramHandler;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.runtime.programs.manifest.ProgramManifest;
import org.orbit.platform.sdk.WSRelayControl;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.IProgramExtensionService;
import org.origin.common.annotation.Annotated;
//import org.origin.common.annotation.DependencyFullfilled;
//import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.BundleHelper;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.util.PropertyUtil;
import org.origin.common.util.URIUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * 
 */
public class PlatformCommand implements Annotated {

	// protected static Logger LOG = LoggerFactory.getLogger(AppCommand.class);
	public static class LOG {
		public static void info(String message) {
			System.out.println(message);
		}
	}

	protected BundleContext bundleContext;
	protected Map<Object, Object> properties;
	// protected GAIAImpl gaiaImpl;
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();

	// @org.origin.common.annotation.Dependency
	// protected GAIA gaia;

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
		props.put("osgi.command.scope", "gaia");
		props.put("osgi.command.function",
				new String[] { //
						"listapps", "installapp", "uninstallapp", //
						"activateapp", "deactivateapp", //
						"startapp", "stopapp" //
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

	// @DependencyFullfilled
	// public void gaiaSet() {
	// LOG.info("GAIA service is set.");
	// LOG.info("GAIA is " + this.gaia);
	// }
	//
	// @DependencyUnfullfilled
	// public void gaiaUnset() {
	// LOG.info("GAIA service is unset.");
	// LOG.info("GAIA is " + this.gaia);
	// }

	public void startCommandService() throws ClientException {

	}

	public void stopCommandService() throws ClientException {

	}

	protected ProgramsAndFeatures getProgramsAndFeatures() {
		Platform platform = Activator.getInstance().getPlatform();
		return platform.getProgramsAndFeatures();
	}

	protected IProgramExtensionService getProgramExtensionService() {
		Platform platform = Activator.getInstance().getPlatform();
		return platform.getProgramExtensionService();
	}

	@Descriptor("List installed programs")
	public void list_programs() throws ClientException {
		LOG.info("listapps()");

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

		IProgramExtensionService service = getProgramExtensionService();

		// Start ws relay
		IProgramExtension relayExtension = service.getExtension(WSRelayControl.EXTENSION_TYPE_ID, extensionId);
		if (relayExtension == null) {
			LOG.info("Program extension is not available.");
			return;
		}
		WSRelayControl relayControl = relayExtension.getAdapter(WSRelayControl.class);
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

		IProgramExtensionService service = getProgramExtensionService();
		IProgramExtension extension = service.getExtension(WSRelayControl.EXTENSION_TYPE_ID, extensionId);
		if (extension == null) {
			LOG.info("Program extension is not available.");
			return;
		}

		WSRelayControl relayControl = extension.getAdapter(WSRelayControl.class);
		if (relayControl == null) {
			LOG.info("WSRelayControl is not available.");
			return;
		}

		String hostURL = getHostURL();
		relayControl.stop(bundleContext, hostURL, contextRoot);
	}

}

// @Descriptor("Start GAIA")
// public void startGAIA() throws ClientException {
// LOG.info("startGAIA()");
//
// if (this.gaiaImpl == null) {
// // Properties configIniProps = SetupUtil.getNodeHomeConfigIniProperties(this.bundleContext);
// this.gaiaImpl = new GAIAImpl(this.bundleContext, null);
// }
// this.gaiaImpl.start();
// }
//
// @Descriptor("Stop GAIA")
// public void stopGAIA() throws ClientException {
// LOG.info("stopGAIA()");
//
// if (this.gaiaImpl != null) {
// this.gaiaImpl.stop();
// this.gaiaImpl = null;
// }
// }
// protected void checkGAIA() throws ClientException {
// if (this.gaia == null) {
// LOG.info("NodeOS is not available.");
// throw new ClientException(500, "GAIA is not available.");
// }
// }
