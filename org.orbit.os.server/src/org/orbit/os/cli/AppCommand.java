package org.orbit.os.cli;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.app.AppManifest;
import org.orbit.os.server.apps.App;
import org.orbit.os.server.apps.AppBundle;
import org.orbit.os.server.apps.AppHandler;
import org.orbit.os.server.apps.AppsManager;
import org.orbit.os.server.service.GAIA;
import org.orbit.os.server.service.GAIAImpl;
import org.orbit.os.server.util.SetupUtil;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.BundleHelper;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * 
 */
public class AppCommand implements Annotated {

	// protected static Logger LOG = LoggerFactory.getLogger(AppCommand.class);
	public static class LOG {
		public static void info(String message) {
			System.out.println(message);
		}
	}

	protected BundleContext bundleContext;
	protected GAIAImpl nodeOSImpl;

	@org.origin.common.annotation.Dependency
	protected GAIA nodeOS;

	/**
	 * Activate the command.
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		LOG.info("start()");

		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "node");
		props.put("osgi.command.function",
				new String[] { //
						"startnodeos", "stopnodeos", //
						"listapps", "installapp", "uninstallapp", //
						"activateapp", "deactivateapp", //
						"startapp", "stopapp" //
		});
		OSGiServiceUtil.register(bundleContext, AppCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Deactivate the command.
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		LOG.info("stop()");

		OSGiServiceUtil.unregister(AppCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		this.bundleContext = null;
	}

	@DependencyFullfilled
	public void nodeOSSet() {
		LOG.info("NodeOS service is set.");
		LOG.info("NodeOS is " + this.nodeOS);
	}

	@DependencyUnfullfilled
	public void nodeOSUnset() {
		LOG.info("NodeOS service is unset.");
		LOG.info("NodeOS is " + this.nodeOS);
	}

	// ----------------------------------------------------------------------------------------
	// NodeOS life cycle commands
	// ----------------------------------------------------------------------------------------
	/**
	 * Command to start node OS service
	 * 
	 */
	@Descriptor("Command to start node OS service")
	public void startnodeos() throws ClientException {
		LOG.info("startnodeos()");

		if (this.nodeOSImpl == null) {
			Properties configIniProps = SetupUtil.getNodeHomeConfigIniProperties(this.bundleContext);
			this.nodeOSImpl = new GAIAImpl(this.bundleContext, configIniProps);
		}
		this.nodeOSImpl.start();
	}

	/**
	 * Command to stop node OS service
	 * 
	 */
	@Descriptor("Command to stop node OS service")
	public void stopnodeos() throws ClientException {
		LOG.info("stopnodeos()");

		if (this.nodeOSImpl != null) {
			this.nodeOSImpl.stop();
			this.nodeOSImpl = null;
		}
	}

	// ----------------------------------------------------------------------------------------
	// App life cycle commands
	// ----------------------------------------------------------------------------------------
	protected void checkNodeOS() throws ClientException {
		if (this.nodeOS == null) {
			LOG.info("NodeOS is not available.");
			throw new ClientException(500, "NodeOS is not available.");
		}
	}

	@Descriptor("Command to list installed alls")
	public void listapps() throws ClientException {
		LOG.info("listapps()");
		checkNodeOS();

		AppHandler[] appHandlers = this.nodeOS.getAppsManager().getAppHandlers();
		int num = (appHandlers != null) ? appHandlers.length : 0;

		LOG.info("Number of apps: " + num);
		if (appHandlers != null) {
			for (AppHandler appHandler : appHandlers) {
				LOG.info("------------------------------------------------------------");
				AppManifest appManifest = appHandler.getAppManifest();
				String appId = appManifest.getId();
				String appVersion = appManifest.getVersion();
				App app = appHandler.getApp();

				String appText = appId + "_" + appVersion + " (Activation State: " + appHandler.getRuntimeState().toString() + ")";
				if (app != null) {
					// DependencySet.STATE dependencySetState = runtimeApp.getDependencySet().getState();
					App.RUNTIME_STATE appRuntimeState = app.getRuntimeState();
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
					for (AppBundle appBundle : app.getAppBundles()) {
						String bundleName = appBundle.getBundleName();
						String bundleVersion = appBundle.getBundleVersion();
						// Dependency.STATE dependencyState = appBundle.getDependency().getState();
						AppBundle.RUNTIME_STATE appBundleRuntimeState = appBundle.getRuntimeState();

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
	@Descriptor("Command to install an app")
	public void installapp(
			// Parameters
			@Descriptor("app file location") @Parameter(absentValue = "", names = { "-location", "--location" }) String location //
	) throws ClientException {
		LOG.info("installapp()");
		LOG.info("appFileLocation = " + location);

		checkNodeOS();

		if (location != null && !location.isEmpty()) {
			// install app archive file from file system
			Path appArchivePath = Paths.get(location);
			try {
				AppManifest appManifest = this.nodeOS.getAppsManager().install(appArchivePath);
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

	@Descriptor("Command to uninstall an app")
	public void uninstallapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("uninstallapp()");
		LOG.info("appId = " + appId);
		LOG.info("appVersion = " + appVersion);

		checkNodeOS();

		try {
			AppManifest appManifest = this.nodeOS.getAppsManager().uninstall(appId, appVersion);
			if (appManifest != null) {
				LOG.info("uninstallapp() " + appManifest.getSimpleName() + " is uninstalled.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Command to activate an app")
	public void activateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("activateapp()");
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isInstalled(appId, appVersion)) {
				LOG.info("App is not installed.");
				return;
			}

			AppHandler appHandler = this.nodeOS.getAppsManager().getAppHandler(appId, appVersion);
			if (appHandler == null) {
				LOG.info("AppHandler is not found.");
				return;
			}

			appHandler.activate();
			LOG.info(appHandler.getAppManifest().getSimpleName() + " is activated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Command to deactivate an app")
	public void deactivateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("deactivateapp()");
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isInstalled(appId, appVersion)) {
				LOG.info("App is not installed.");
				return;
			}

			AppHandler appHandler = this.nodeOS.getAppsManager().getAppHandler(appId, appVersion);
			if (appHandler == null) {
				LOG.info("AppHandler is not found.");
				return;
			}

			appHandler.deactivate();
			LOG.info(appHandler.getAppManifest().getSimpleName() + " is deactivated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Command to start an app")
	public void startapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("startapp()");
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isInstalled(appId, appVersion)) {
				LOG.info("App is not installed.");
				return;
			}

			AppHandler appHandler = appsManager.getAppHandler(appId, appVersion);
			if (appHandler == null) {
				LOG.info("AppHandler is not found.");
				return;
			}

			// Activate the AppHandler if needed
			if (!appHandler.getRuntimeState().isActivated()) {
				appHandler.activate();
			}

			appHandler.startApp();
			LOG.info(appHandler.getAppManifest().getSimpleName() + " is started.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

	@Descriptor("Command to stop an app")
	public void stopapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		LOG.info("stopapp()");
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isInstalled(appId, appVersion)) {
				LOG.info("App is not installed.");
				return;
			}

			AppHandler appHandler = appsManager.getAppHandler(appId, appVersion);
			if (appHandler == null) {
				LOG.info("AppHandler is not found.");
				return;
			}
			appHandler.stopApp();
			LOG.info(appHandler.getAppManifest().getSimpleName() + " is stopped.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOG.info("");
	}

}
