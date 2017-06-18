package org.orbit.os.server.cli;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.orbit.app.AppManifest;
import org.orbit.os.server.Activator;
import org.orbit.os.server.app.App;
import org.orbit.os.server.app.AppBundle;
import org.orbit.os.server.app.AppHandler;
import org.orbit.os.server.app.AppsManager;
import org.orbit.os.server.service.NodeOS;
import org.orbit.os.server.service.impl.NodeOSImpl;
import org.origin.common.annotation.Annotated;
import org.origin.common.annotation.DependencyFullfilled;
import org.origin.common.annotation.DependencyUnfullfilled;
import org.origin.common.osgi.OSGiServiceUtil;
import org.origin.common.rest.client.ClientException;
import org.osgi.framework.BundleContext;

/**
 * 
 * @see FileSystemCommand
 * @see AppStoreCommand
 * 
 */
public class NodeOSCommand implements Annotated {

	protected boolean debug = false;
	protected BundleContext bundleContext;
	protected NodeOSImpl nodeOSImpl;

	@org.origin.common.annotation.Dependency
	protected NodeOS nodeOS;

	/**
	 * Activate the command.
	 * 
	 * @param bundleContext
	 */
	public void activate(BundleContext bundleContext) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activate()");
		}

		this.bundleContext = bundleContext;

		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "node");
		props.put("osgi.command.function",
				new String[] { //
						"startnodeos", "stopnodeos", //
						"lapps", "listapps", "installapp", "uninstallapp", //
						"activateapp", "deactivateapp", //
						"startapp", "stopapp" //
		});
		OSGiServiceUtil.register(bundleContext, NodeOSCommand.class.getName(), this, props);
		OSGiServiceUtil.register(bundleContext, Annotated.class.getName(), this);
	}

	/**
	 * Deactivate the command.
	 * 
	 * @param bundleContext
	 */
	public void deactivate(BundleContext bundleContext) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivate()");
		}

		OSGiServiceUtil.unregister(NodeOSCommand.class.getName(), this);
		OSGiServiceUtil.unregister(Annotated.class.getName(), this);

		this.bundleContext = null;
	}

	@DependencyFullfilled
	public void nodeOSSet() {
		if (debug) {
			System.out.println("NodeOS service is set.");
			System.out.println("NodeOS is " + this.nodeOS);
		}
	}

	@DependencyUnfullfilled
	public void nodeOSUnset() {
		if (debug) {
			System.out.println("NodeOS service is unset.");
			System.out.println("NodeOS is " + this.nodeOS);
		}
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
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".startnodeos()");
		}

		if (this.nodeOSImpl == null) {
			Properties configIniProps = Activator.getNodeHomeConfigIniProperties();
			this.nodeOSImpl = new NodeOSImpl(this.bundleContext, configIniProps);
		}
		this.nodeOSImpl.start();
	}

	/**
	 * Command to stop node OS service
	 * 
	 */
	@Descriptor("Command to stop node OS service")
	public void stopnodeos() throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stopnodeos()");
		}

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
			if (debug) {
				System.out.println("NodeOS is not available.");
			}
			throw new ClientException(500, "NodeOS is not available.");
		}
	}

	@Descriptor("Command to list installed alls")
	public void lapps() throws ClientException {
		listapps();
	}

	@Descriptor("Command to list installed alls")
	public void listapps() throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".listapps()");
		}
		checkNodeOS();

		AppHandler[] appHandlers = this.nodeOS.getAppsManager().getAppHandlers();
		int num = (appHandlers != null) ? appHandlers.length : 0;

		System.out.println("Number of apps: " + num);

		if (appHandlers != null) {
			for (AppHandler appHandler : appHandlers) {
				System.out.println("------------------------------------------------------------");
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
				System.out.println(appText);

				if (app != null) {
					// String appBundleText = " " + app.getSimpleName();
					// appBundleText += " (Runtime State: " + app.getRuntimeState().toString() + ")";
					// System.out.println(appBundleText);

					for (AppBundle appBundle : app.getAppBundles()) {
						String bundleName = appBundle.getBundleName();
						String bundleVersion = appBundle.getBundleVersion();
						// Dependency.STATE dependencyState = appBundle.getDependency().getState();
						AppBundle.RUNTIME_STATE appBundleRuntimeState = appBundle.getRuntimeState();

						String appModuleText = "    bundle: " + (bundleName + "_" + bundleVersion);
						// appBundleText += " (Dependency State: " + dependencyState.toString() + ")";
						appModuleText += " (Runtime State: " + appBundleRuntimeState.toString() + ")";
						System.out.println(appModuleText);
					}
				}
			}
		}

		System.out.println();
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
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".installapp()");
			System.out.println("appFileLocation = " + location);
		}
		checkNodeOS();

		if (location != null && !location.isEmpty()) {
			// install app archive file from file system
			Path appArchivePath = Paths.get(location);
			try {
				AppManifest appManifest = this.nodeOS.getAppsManager().installApp(appArchivePath);
				if (appManifest != null) {
					System.out.println(getClass().getSimpleName() + ".installapp() " + appManifest.getSimpleName() + " is installed.");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// install app from app store
		}

		System.out.println();
	}

	@Descriptor("Command to uninstall an app")
	public void uninstallapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".uninstallapp()");
			System.out.println("appId = " + appId);
			System.out.println("appVersion = " + appVersion);
		}
		checkNodeOS();

		try {
			AppManifest appManifest = this.nodeOS.getAppsManager().uninstallApp(appId, appVersion);
			if (appManifest != null) {
				System.out.println(getClass().getSimpleName() + ".uninstallapp() " + appManifest.getSimpleName() + " is uninstalled.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Descriptor("Command to activate an app")
	public void activateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activateapp()");
		}
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isAppInstalled(appId, appVersion)) {
				System.out.println("App is not installed.");
				return;
			}

			AppHandler appHandler = this.nodeOS.getAppsManager().getAppHandler(appId, appVersion);
			if (appHandler == null) {
				System.out.println("AppHandler is not found.");
				return;
			}

			appHandler.activate();
			System.out.println(appHandler.getAppManifest().getSimpleName() + " is activated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Descriptor("Command to deactivate an app")
	public void deactivateapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivateapp()");
		}
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isAppInstalled(appId, appVersion)) {
				System.out.println("App is not installed.");
				return;
			}

			AppHandler appHandler = this.nodeOS.getAppsManager().getAppHandler(appId, appVersion);
			if (appHandler == null) {
				System.out.println("AppHandler is not found.");
				return;
			}

			appHandler.deactivate();
			System.out.println(appHandler.getAppManifest().getSimpleName() + " is deactivated.");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Descriptor("Command to start an app")
	public void startapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".startapp()");
		}
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isAppInstalled(appId, appVersion)) {
				System.out.println("App is not installed.");
				return;
			}

			AppHandler appHandler = appsManager.getAppHandler(appId, appVersion);
			if (appHandler == null) {
				System.out.println("AppHandler is not found.");
				return;
			}

			// Activate the AppHandler if needed
			if (!appHandler.getRuntimeState().isActivated()) {
				appHandler.activate();
			}

			appHandler.startApp();
			if (debug) {
				System.out.println(appHandler.getAppManifest().getSimpleName() + " is started.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Descriptor("Command to stop an app")
	public void stopapp(
			// Parameters
			@Descriptor("app id") @Parameter(absentValue = "", names = { "-id", "--id" }) String appId, //
			@Descriptor("app version") @Parameter(absentValue = "", names = { "-version", "--version" }) String appVersion) throws ClientException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stopapp()");
		}
		checkNodeOS();

		try {
			AppsManager appsManager = this.nodeOS.getAppsManager();
			if (!appsManager.isAppInstalled(appId, appVersion)) {
				System.out.println("App is not installed.");
				return;
			}

			AppHandler appHandler = appsManager.getAppHandler(appId, appVersion);
			if (appHandler == null) {
				System.out.println("AppHandler is not found.");
				return;
			}
			appHandler.stopApp();
			if (debug) {
				System.out.println(appHandler.getAppManifest().getSimpleName() + " is stopped.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
	}

}
