package org.orbit.os.server.app.impl.v2;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.app.AppManifest;
import org.orbit.app.util.AppManifestUtil;
import org.orbit.app.util.AppUtil;
import org.orbit.os.server.app.AppException;
import org.orbit.os.server.app.AppHandler;
import org.orbit.os.server.app.AppInstaller;
import org.orbit.os.server.app.AppInstallerProvider;
import org.orbit.os.server.app.AppsManager;
import org.origin.common.runtime.Problem;
import org.osgi.framework.Bundle;
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
public class AppsManagerImplV2 implements AppsManager {

	public static final String APPLICATION_HEADER = "Application";

	protected boolean debug = false;
	protected BundleContext bundleContext;
	protected List<AppManifest> installedApps = new ArrayList<AppManifest>();
	protected Map<AppManifest, AppHandler> appHandlersMap = new HashMap<AppManifest, AppHandler>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param bundleContext
	 */
	public AppsManagerImplV2(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
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
	public synchronized void start() throws AppException {
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

		DefaultAppInstallerV2.getInstance().activate();

		loadInstalledApps();
		initAppHandlers();
		activateAppHandlers();
		autoStartAppHandlers();
	}

	protected synchronized void loadInstalledApps() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".loadInstalledApps()");
		}

		this.installedApps.clear();

		Bundle[] osgiBundles = this.bundleContext.getBundles();
		for (Bundle osgiBundle : osgiBundles) {
			AppManifest appManifest = AppManifestUtil.getAppManifest(osgiBundle);
			if (appManifest != null) {
				this.installedApps.add(appManifest);
			}
		}
	}

	protected void initAppHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".initAppHandlers()");
		}

		this.appHandlersMap.clear();
		for (AppManifest installedApp : this.installedApps) {
			AppHandler appHandler = new AppHandlerImplV2(installedApp, this.bundleContext);
			this.appHandlersMap.put(installedApp, appHandler);
		}
	}

	protected void activateAppHandlers() throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activateAppHandlers()");
		}
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest appManifest = appManifestItor.next();
			AppHandler appHandler = this.appHandlersMap.get(appManifest);
			activateAppHandler(appHandler);
		}
	}

	protected void activateAppHandler(AppHandler appHandler) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activateAppHandler() appHandler = " + appHandler);
		}
		if (appHandler != null) {
			appHandler.activate();
		}
	}

	/**
	 * 
	 * @throws AppException
	 */
	protected void autoStartAppHandlers() throws AppException {
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest currAppManifest = appManifestItor.next();
			AppHandler currAppHandler = this.appHandlersMap.get(currAppManifest);
			currAppHandler.startApp();
		}
	}

	@Override
	public synchronized void stop() throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		deactivateAppHandlers();
		this.appHandlersMap.clear();
		this.installedApps.clear();

		DefaultAppInstallerV2.getInstance().deactivate();
	}

	protected void deactivateAppHandlers() throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivateAppHandlers()");
		}
		for (Iterator<AppManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			AppManifest appManifest = appManifestItor.next();
			AppHandler appHandler = this.appHandlersMap.get(appManifest);
			deactivateAppHandler(appHandler);
		}
	}

	protected void deactivateAppHandler(AppHandler appHandler) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivateAppHandler() appHandler = " + appHandler);
		}
		if (appHandler != null) {
			// What is going to happen when AppHandler is closed.
			// (1) Stop tracking Bundles of the App.
			// (2) Dispose runtime App.
			appHandler.deactivate();
		}
	}

	@Override
	public synchronized AppManifest[] getInstalledApps() {
		return this.installedApps.toArray(new AppManifest[this.installedApps.size()]);
	}

	@Override
	public synchronized boolean isAppInstalled(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".isAppInstalled() appId = " + appId + ", appVersion = " + appVersion);
		}

		for (Iterator<AppManifest> appItor = this.installedApps.iterator(); appItor.hasNext();) {
			AppManifest currInstalledApp = appItor.next();
			String currAppId = currInstalledApp.getId();
			String currAppVersion = currInstalledApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized AppManifest getInstalledApp(String appId, String appVersion) {
		AppManifest installedApp = null;
		for (Iterator<AppManifest> appItor = this.installedApps.iterator(); appItor.hasNext();) {
			AppManifest currInstalledApp = appItor.next();
			String currAppId = currInstalledApp.getId();
			String currAppVersion = currInstalledApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				installedApp = currInstalledApp;
				break;
			}
		}
		return installedApp;
	}

	/**
	 * 
	 * @param appManifest
	 * @return
	 */
	protected synchronized AppHandler registerApp(AppManifest appManifest) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".registerApp() appManifest = " + appManifest);
		}
		// Stopping processing apps once the app manager is stopped.
		checkStarted();

		if (isAppInstalled(appManifest.getId(), appManifest.getVersion())) {
			return getAppHandler(appManifest.getId(), appManifest.getVersion());
		}

		// 1. Create AppHandler for the AppManifest and open the AppHandler.
		AppHandler appHandler = new AppHandlerImplV2(appManifest, this.bundleContext);
		activateAppHandler(appHandler);

		// 2. Add AppManifest to the list of installed apps.
		this.appHandlersMap.put(appManifest, appHandler);
		this.installedApps.add(appManifest);

		return appHandler;
	}

	/**
	 * 
	 * @param appManifest
	 * @return
	 * @throws AppException
	 */
	protected synchronized boolean unregisterApp(AppManifest appManifest) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".unregisterApp() appManifest = " + appManifest.getSimpleName());
		}

		// stopping processing apps once the app manager is stopped.
		checkStarted();

		boolean removed = this.installedApps.remove(appManifest);
		AppHandler appHandler = this.appHandlersMap.remove(appManifest);
		if (removed && appHandler != null) {
			deactivateAppHandler(appHandler);
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param appId
	 * @param appVersion
	 * @param expectedToBeInstalled
	 * @throws AppException
	 */
	protected void checkAppInstalled(String appId, String appVersion, boolean expectedToBeInstalled) throws AppException {
		boolean isAppInstalled = isAppInstalled(appId, appVersion);
		if (isAppInstalled) {
			// app is installed
			if (!expectedToBeInstalled) {
				// app is expected to be not installed.
				throw new AppException("App " + appId + "(" + appVersion + ") is already installed.");
			}

		} else {
			// app is not installed
			if (expectedToBeInstalled) {
				// app is expected to be installed.
				throw new AppException("App " + appId + "(" + appVersion + ") is not installed.");
			}
		}
	}

	@Override
	public AppManifest installApp(Path appArchivePath) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".install() appArchivePath = " + appArchivePath.toString());
		}
		checkStarted();

		AppManifest appManifestFromArchive = AppUtil.extractAppManifest(appArchivePath.toFile());
		if (appManifestFromArchive == null) {
			if (debug) {
				System.out.println("App manifest cannot be found in the app archive file '" + appArchivePath.toString() + "'.");
			}
			return null;
		}
		String appId = appManifestFromArchive.getId();
		String appVersion = appManifestFromArchive.getVersion();

		// app is expected to be not installed
		checkAppInstalled(appId, appVersion, false);

		AppInstaller installer = AppInstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		AppManifest appManifest = installer.install(this.bundleContext, appArchivePath);
		if (appManifest == null) {
			if (debug) {
				System.out.println("AppManifest is not returned by installer.");
			}
			return null;
		}

		AppHandler newAppHandler = registerApp(appManifest);
		if (newAppHandler != null) {
			return appManifest;
		}
		return null;
	}

	@Override
	public AppManifest installApp(String appId, String appVersion) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".install() appId = " + appId + ", appVersion = " + appVersion);
		}
		checkStarted();

		// app is expected to be not installed
		checkAppInstalled(appId, appVersion, false);

		AppInstaller installer = AppInstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		AppManifest appManifest = installer.install(this.bundleContext, appId, appVersion);
		if (appManifest == null) {
			if (debug) {
				System.out.println("AppManifest is not returned by installer.");
			}
			return null;
		}

		AppHandler newAppHandler = registerApp(appManifest);
		if (newAppHandler != null) {
			return appManifest;
		}
		return null;
	}

	@Override
	public AppManifest uninstallApp(String appId, String appVersion) throws AppException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".uninstall() appId = " + appId + ", appVersion = " + appVersion);
		}
		checkStarted();

		// app is expected to be installed
		checkAppInstalled(appId, appVersion, true);

		AppManifest appManifest = getInstalledApp(appId, appVersion);
		if (appManifest == null) {
			if (debug) {
				System.out.println("App manifest cannot be found in installed apps.");
			}
			return null;
		}

		AppInstaller installer = AppInstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		boolean uninstalled = installer.uninstall(this.bundleContext, appManifest);
		if (uninstalled) {
			boolean unregistered = unregisterApp(appManifest);
			if (unregistered) {
				return appManifest;
			}
		}
		return null;
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
			AppManifest currAppManifest = appItor.next();
			AppHandler currAppHandler = this.appHandlersMap.get(currAppManifest);

			String currAppId = currAppHandler.getAppManifest().getId();
			String currAppVersion = currAppHandler.getAppManifest().getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				appHandler = currAppHandler;
				break;
			}
		}
		return appHandler;
	}

	@Override
	public List<Problem> getProblems() {
		return this.problems;
	}

}

// protected BundleTracker<AppHandler> applicationBundleTracker;

// Start tracking OSGi bundles of the App.
// int bundleState = Bundle.RESOLVED | Bundle.STARTING | Bundle.STOPPING | Bundle.ACTIVE;
// this.applicationBundleTracker = new BundleTracker<AppHandler>(this.bundleContext, bundleState, new BundleTrackerCustomizer<AppHandler>() {
// @Override
// public AppHandler addingBundle(Bundle bundle, BundleEvent event) {
// try {
// AppHandler appHandler = handleAddingApplication(bundle, event);
// if (appHandler != null) {
// return appHandler;
// }
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// return null;
// }
//
// @Override
// public void modifiedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) {
// try {
// System.out.println("applicationBundleTracker.modifiedBundle() " + appHandler);
// handleModifiedApplication(bundle, event, appHandler);
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// }
//
// @Override
// public void removedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) {
// try {
// System.out.println("applicationBundleTracker.removedBundle() " + appHandler);
// handleRemovedApplication(bundle, event, appHandler);
//
// } catch (AppException e) {
// e.printStackTrace();
// getProblems().add(new Problem(e));
// }
// }
// });
// this.applicationBundleTracker.open();

// Stop tracking Bundles of applications
// Closing bundle tracker will trigger BundleTracker<AppHandler>.removedBundle(Bundle bundle, BundleEvent event, AppHandler appHandler) method.
// if (this.applicationBundleTracker != null) {
// this.applicationBundleTracker.close();
// this.applicationBundleTracker = null;
// }

// /**
// *
// * @param bundle
// * @param event
// * @return
// * @throws AppException
// */
// protected AppHandler handleAddingApplication(Bundle bundle, BundleEvent event) throws AppException {
// if (debug) {
// // System.out.print(getClass().getSimpleName() + ".handleAddingApplication() ");
// }
//
// // Load META-INF/manifest.json from OSGi bundle.
// // If AppManifest can be retrieved, it means the OSGi Bundle is for the application itself.
// AppManifest appManifestFromBundle = AppManifestUtil.getAppManifest(bundle);
// if (appManifestFromBundle != null) {
// // if (debug) {
// // System.out.print(getClass().getSimpleName() + ".handleAddingApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// // System.out.println("appManifest = " + appManifestFromBundle);
// // }
//
// // String appId = appManifestFromBundle.getId();
// // String appVersion = appManifestFromBundle.getVersion();
// // boolean isAppInstalled = isAppInstalled(appId, appVersion);
// // if (isAppInstalled) {
// // AppHandler existingAppHandler = getAppHandler(appId, appVersion);
// // existingAppHandler.getApp().setBundle(bundle);
// // return existingAppHandler;
// //
// // } else {
// // AppHandler newAppHandler = registerApp(appManifestFromBundle);
// // if (newAppHandler != null) {
// // newAppHandler.getApp().setBundle(bundle);
// // }
// // return newAppHandler;
// // }
// }
// return null;
// }
//
// /**
// *
// * @param bundle
// * @param event
// * @param appHandler
// * @throws AppException
// */
// protected void handleModifiedApplication(Bundle bundle, BundleEvent event, AppHandler appHandler) throws AppException {
// if (debug) {
// System.out.print(getClass().getSimpleName() + ".handleModifiedApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// }
//
// String bundleName = BundleHelper.INSTANCE.getSimpleName(bundle);
//
// if (BundleUtil.isBundleInstalledEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is installed.");
// }
//
// } else if (BundleUtil.isBundleResolvedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is resolved.");
// }
//
// } else if (BundleUtil.isBundleStartingEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is starting.");
// }
//
// } else if (BundleUtil.isBundleStartedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is started.");
// }
// // appHandler.startApp();
//
// } else if (BundleUtil.isBundleStoppingEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is stopping.");
// }
//
// } else if (BundleUtil.isBundleStoppedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is stopped.");
// }
// // appHandler.stopApp();
//
// } else if (BundleUtil.isBundleUnresolvedEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is unresolved.");
// }
//
// } else if (BundleUtil.isBundleUninstalledEvent(event)) {
// if (debug) {
// System.out.println(bundleName + " is uninstalled.");
// }
// }
// }
//
// /**
// *
// * @param bundle
// * @param event
// * @param appHandler
// * @throws AppException
// */
// protected void handleRemovedApplication(Bundle bundle, BundleEvent event, AppHandler appHandler) throws AppException {
// if (debug) {
// System.out.print(getClass().getSimpleName() + ".handleRemovedApplication() " + BundleHelper.INSTANCE.getSimpleName(event));
// }
//
// // Question:
// // Should the application be uninstalled when the OSGi bundle for the application itself is uninstalled?
// //
// // Answer:
// // Probably no. So that OSGi bundle (which can have configurations) for the application can be re-installed/updated.
// // That also means uninstallApp() should actively unregisterApp() after all the OSGi bundles of the application have been uninstalled.
//
// // AppManifest appManifest = appHandler.getAppManifest();
// // String appId = appManifest.getId();
// // String appVersion = appManifest.getVersion();
// // unregisterApp(appId, appVersion);
// }
