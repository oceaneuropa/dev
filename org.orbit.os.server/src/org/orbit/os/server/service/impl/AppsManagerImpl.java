package org.orbit.os.server.service.impl;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.app.AppManifest;
import org.orbit.app.Constants;
import org.orbit.app.util.AppManifestUtil;
import org.orbit.app.util.AppUtil;
import org.orbit.os.server.service.AppHandler;
import org.orbit.os.server.service.AppsManager;
import org.orbit.os.server.util.SetupUtil;
import org.osgi.framework.BundleContext;

/*
 * Functions for apps:
 * (1) Get installed apps manifests.
 * (2) Install app from a app zip file.
 * (3) Uninstall an app by id and version.
 * 
 * FrameworkFactory example code:
 * http://www.programcreek.com/java-api-examples/index.php?source_dir=arkadiko-master/src/osgi/com/liferay/arkadiko/osgi/OSGiFrameworkFactory.java
 * http://www.javased.com/index.php?api=org.osgi.framework.launch.FrameworkFactory
 * http://www.javased.com/index.php?source_dir=jbosgi-framework/itest/src/test/java/org/jboss/test/osgi/framework/launch/PersistentBundlesTestCase.java
 * 
 */
public class AppsManagerImpl implements AppsManager {

	protected boolean debug = true;
	protected BundleContext bundleContext;
	protected List<AppManifest> installedApps = new ArrayList<AppManifest>();
	protected Map<AppManifest, AppHandler> appHandlersMap = new HashMap<AppManifest, AppHandler>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);

	/**
	 * 
	 * @param bundleContext
	 */
	public AppsManagerImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public synchronized void start() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".start()");
		}

		if (isStarted()) {
			if (debug) {
				System.out.println("Already started.");
			}
			return;
		}
		this.isStarted.set(true);

		loadInstalledApps();
		initAppHandlers();
		openAppHandlers();
	}

	protected synchronized void loadInstalledApps() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".loadInstalledApps()");
		}

		this.installedApps.clear();
		Path nodeHome = SetupUtil.getNodeHome(this.bundleContext);
		Path appsPath = SetupUtil.getNodeAppsPath(nodeHome, true);
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(appsPath)) {
			for (Path appPath : stream) {
				if (Files.isDirectory(appPath)) {
					Path manifestPath = appPath.resolve(Constants.APP_MANIFEST_FILENAME);
					if (Files.exists(manifestPath)) {
						AppManifest appManifest = AppManifestUtil.loadManifes(manifestPath);
						if (appManifest != null) {
							this.installedApps.add(appManifest);
						}
					}
				}
			}
		} catch (IOException | DirectoryIteratorException e) {
			e.printStackTrace();
		}
	}

	protected void initAppHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".initAppHandlers()");
		}

		this.appHandlersMap.clear();
		for (AppManifest installedApp : this.installedApps) {
			AppHandler appHandler = new AppHandler(installedApp);
			this.appHandlersMap.put(installedApp, appHandler);
		}
	}

	protected void openAppHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".openAppHandlers()");
		}
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest appManifest = appManifestItor.next();
			AppHandler appHandler = this.appHandlersMap.get(appManifest);
			openAppHandler(appHandler);
		}
	}

	protected void openAppHandler(AppHandler appHandler) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".openAppHandler() appHandler = " + appHandler);
		}
		if (appHandler != null) {
			appHandler.open(this.bundleContext);
		}
	}

	protected synchronized boolean isStarted() {
		return this.isStarted.get() ? true : false;
	}

	protected void checkStarted() {
		if (!isStarted()) {
			throw new IllegalStateException(getClass().getSimpleName() + " is not started.");
		}
	}

	@Override
	public synchronized void stop() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		closeAppHandlers();
		this.appHandlersMap.clear();
		this.installedApps.clear();
	}

	protected void closeAppHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".closeAppHandlers()");
		}
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest appManifest = appManifestItor.next();
			AppHandler appHandler = this.appHandlersMap.get(appManifest);
			closeAppHandler(appHandler);
		}
	}

	protected void closeAppHandler(AppHandler appHandler) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".closeAppHandler() appHandler = " + appHandler);
		}
		if (appHandler != null) {
			// What is going to happen when AppHandler is closed.
			// (1) Stop tracking Bundles of the App.
			// (2) Dispose runtime App.
			appHandler.close(this.bundleContext);
		}
	}

	@Override
	public synchronized AppManifest[] getInstalledApps() {
		return this.installedApps.toArray(new AppManifest[this.installedApps.size()]);
	}

	@Override
	public synchronized boolean addApp(AppManifest appManifest) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".addApp() appManifest = " + appManifest);
		}
		// stopping processing apps once the app manager is stopped.
		checkStarted();

		if (appExists(appManifest.getId(), appManifest.getVersion())) {
			return false;
		}

		// 1. Store AppManifest in the {NODE_HOME}/apps/<appId>_<appVersion>/manifest.json file internally in app node.
		Path nodeHome = SetupUtil.getNodeHome(this.bundleContext);
		Path appsPath = SetupUtil.getNodeAppsPath(nodeHome, true);
		String appFolderName = AppUtil.getAppFolderName(appManifest.getId(), appManifest.getVersion());
		Path appFolderPath = appsPath.resolve(appFolderName);
		if (!Files.exists(appFolderPath)) {
			try {
				appFolderPath = Files.createDirectory(appFolderPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Path manifestPath = appFolderPath.resolve(Constants.APP_MANIFEST_FILENAME);
		try {
			Files.deleteIfExists(manifestPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AppManifestUtil.saveManifest(appManifest, manifestPath);

		// 2. Add AppManifest to the list of installed apps.
		boolean succeed = this.installedApps.add(appManifest);
		if (succeed) {
			// 3. Create AppHandler for the AppManifest and open the AppHandler.
			AppHandler appHandler = new AppHandler(appManifest);
			this.appHandlersMap.put(appManifest, appHandler);
			openAppHandler(appHandler);
		}
		return succeed;
	}

	@Override
	public synchronized boolean removeApp(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".removeApp() appId = " + appId + ", appVersion = " + appVersion);
		}
		// stopping processing apps once the app manager is stopped.
		checkStarted();

		AppManifest appManifest = null;
		for (AppManifest installedApp : this.installedApps) {
			String currAppId = installedApp.getId();
			String currAppVersion = installedApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				appManifest = installedApp;
				break;
			}
		}

		if (appManifest != null) {
			boolean succeed = this.installedApps.remove(appManifest);
			if (succeed) {
				// Close the app handler
				AppHandler appHandler = this.appHandlersMap.remove(appManifest);
				closeAppHandler(appHandler);

				// Delete the {NODE_HOME}/apps/<appId>_<appVersion> internal folder for the app (including the manifest.json file in it).
				Path nodeHome = SetupUtil.getNodeHome(this.bundleContext);
				Path appsPath = SetupUtil.getNodeAppsPath(nodeHome, true);
				String appFolderName = AppUtil.getAppFolderName(appId, appVersion);
				Path appFolderPath = appsPath.resolve(appFolderName);
				try {
					Files.deleteIfExists(appFolderPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return succeed;
		}
		return false;
	}

	@Override
	public synchronized boolean appExists(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".appExists() appId = " + appId + ", appVersion = " + appVersion);
		}

		for (AppManifest installedApp : this.installedApps) {
			String currAppId = installedApp.getId();
			String currAppVersion = installedApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized AppHandler[] getAppHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".getAppHandlers()");
		}

		List<AppHandler> appHandlers = new ArrayList<AppHandler>();
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest currAppManifest = appManifestItor.next();
			AppHandler currAppHandler = this.appHandlersMap.get(currAppManifest);
			appHandlers.add(currAppHandler);
		}
		return appHandlers.toArray(new AppHandler[appHandlers.size()]);
	}

	@Override
	public AppHandler getAppHandler(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".getAppHandler() appId = " + appId + ", appVersion = " + appVersion);
		}

		AppHandler appHandler = null;
		for (Iterator<AppManifest> appItor = this.appHandlersMap.keySet().iterator(); appItor.hasNext();) {
			AppManifest app = appItor.next();
			AppHandler currAppHandler = this.appHandlersMap.get(app);

			String currAppId = currAppHandler.getAppManifest().getId();
			String currAppVersion = currAppHandler.getAppManifest().getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				appHandler = currAppHandler;
				break;
			}
		}
		return appHandler;
	}

}
