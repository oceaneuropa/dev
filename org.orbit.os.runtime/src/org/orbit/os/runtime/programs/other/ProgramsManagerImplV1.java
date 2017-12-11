package org.orbit.os.runtime.programs.other;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.orbit.os.api.Constants;
import org.orbit.os.api.apps.ProgramManifest;
import org.orbit.os.api.util.AppManifestUtil;
import org.orbit.os.api.util.ProgramUtil;
import org.orbit.os.runtime.programs.Installer;
import org.orbit.os.runtime.programs.InstallerProvider;
import org.orbit.os.runtime.programs.ProgramException;
import org.orbit.os.runtime.programs.ProgramHandler;
import org.orbit.os.runtime.programs.ProgramsAndFeatures;
import org.orbit.os.runtime.util.SetupUtil;
import org.origin.common.runtime.Problem;
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
public class ProgramsManagerImplV1 implements ProgramsAndFeatures {

	protected boolean debug = false;
	protected BundleContext bundleContext;
	protected List<ProgramManifest> installedApps = new ArrayList<ProgramManifest>();
	protected Map<ProgramManifest, ProgramHandler> appHandlersMap = new HashMap<ProgramManifest, ProgramHandler>();
	protected AtomicBoolean isStarted = new AtomicBoolean(false);
	protected List<Problem> problems = new ArrayList<Problem>();

	/**
	 * 
	 * @param bundleContext
	 */
	public ProgramsManagerImplV1(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Override
	public synchronized void start() throws ProgramException {
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

		DefaultAppInstallerV1.getInstance().activate();

		loadInstalledApps();
		initAppHandlers();
		activateAppHandlers();
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
					Path manifestPath = appPath.resolve(Constants.APP_MANIFEST_FULLPATH);
					if (Files.exists(manifestPath)) {
						ProgramManifest appManifest = AppManifestUtil.loadManifes(manifestPath);
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
		for (ProgramManifest installedApp : this.installedApps) {
			ProgramHandler appHandler = new ProgramHandlerImplV1(installedApp, this.bundleContext);
			this.appHandlersMap.put(installedApp, appHandler);
		}
	}

	protected void activateAppHandlers() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activateAppHandlers()");
		}
		for (Iterator<ProgramManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			ProgramManifest appManifest = appManifestItor.next();
			ProgramHandler appHandler = this.appHandlersMap.get(appManifest);
			activateAppHandler(appHandler);
		}
	}

	protected void activateAppHandler(ProgramHandler appHandler) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".activateAppHandler() appHandler = " + appHandler);
		}
		if (appHandler != null) {
			appHandler.activate();
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
	public synchronized void stop() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".stop()");
		}
		if (!this.isStarted.compareAndSet(true, false)) {
			return;
		}

		deactivateAppHandlers();
		this.appHandlersMap.clear();
		this.installedApps.clear();

		DefaultAppInstallerV1.getInstance().deactivate();
	}

	protected void deactivateAppHandlers() throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".deactivateAppHandlers()");
		}
		for (Iterator<ProgramManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			ProgramManifest appManifest = appManifestItor.next();
			ProgramHandler appHandler = this.appHandlersMap.get(appManifest);
			deactivateAppHandler(appHandler);
		}
	}

	protected void deactivateAppHandler(ProgramHandler appHandler) throws ProgramException {
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
	public synchronized ProgramManifest[] getInstalledApps() {
		return this.installedApps.toArray(new ProgramManifest[this.installedApps.size()]);
	}

	@Override
	public synchronized boolean isInstalled(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".isAppInstalled() appId = " + appId + ", appVersion = " + appVersion);
		}

		for (Iterator<ProgramManifest> appItor = this.installedApps.iterator(); appItor.hasNext();) {
			ProgramManifest currInstalledApp = appItor.next();
			String currAppId = currInstalledApp.getId();
			String currAppVersion = currInstalledApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public synchronized ProgramManifest getInstalledPrograms(String appId, String appVersion) {
		ProgramManifest installedApp = null;
		for (Iterator<ProgramManifest> appItor = this.installedApps.iterator(); appItor.hasNext();) {
			ProgramManifest currInstalledApp = appItor.next();
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
	protected synchronized boolean registerApp(ProgramManifest appManifest) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".registerApp() appManifest = " + appManifest);
		}
		// stopping processing apps once the app manager is stopped.
		checkStarted();

		if (isInstalled(appManifest.getId(), appManifest.getVersion())) {
			return false;
		}

		// 1. Store AppManifest in the {NODE_HOME}/apps/<appId>_<appVersion>/manifest.json file internally in app node.
		Path nodeHome = SetupUtil.getNodeHome(this.bundleContext);
		Path appsPath = SetupUtil.getNodeAppsPath(nodeHome, true);
		String appFolderName = ProgramUtil.deriveAppFolderName(appManifest.getId(), appManifest.getVersion());
		Path appFolderPath = appsPath.resolve(appFolderName);

		Path manifestPath = appFolderPath.resolve(Constants.APP_MANIFEST_FULLPATH);
		try {
			AppManifestUtil.saveManifest(appManifest, manifestPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ProgramException(e.getMessage(), e);
		}

		// 2. Add AppManifest to the list of installed apps.
		boolean isRegistered = this.installedApps.add(appManifest);
		if (isRegistered) {
			// 3. Create AppHandler for the AppManifest and open the AppHandler.
			ProgramHandler appHandler = new ProgramHandlerImplV1(appManifest, this.bundleContext);
			this.appHandlersMap.put(appManifest, appHandler);
			activateAppHandler(appHandler);
		}
		return isRegistered;
	}

	/**
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	protected synchronized boolean unregisterApp(String appId, String appVersion) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".unregisterApp() appId = " + appId + ", appVersion = " + appVersion);
		}
		// stopping processing apps once the app manager is stopped.
		checkStarted();

		ProgramManifest appManifest = null;
		for (Iterator<ProgramManifest> appItor = this.installedApps.iterator(); appItor.hasNext();) {
			ProgramManifest currInstalledApp = appItor.next();
			String currAppId = currInstalledApp.getId();
			String currAppVersion = currInstalledApp.getVersion();
			if (currAppId.equals(appId) && currAppVersion.equals(appVersion)) {
				appManifest = currInstalledApp;
				break;
			}
		}

		if (appManifest != null) {
			boolean isUnregistered = this.installedApps.remove(appManifest);
			if (isUnregistered) {
				// Close the app handler
				ProgramHandler appHandler = this.appHandlersMap.remove(appManifest);
				deactivateAppHandler(appHandler);

				// Delete the {NODE_HOME}/apps/<appId>_<appVersion> internal folder for the app (including the manifest.json file in it).
				Path nodeHome = SetupUtil.getNodeHome(this.bundleContext);
				Path appsPath = SetupUtil.getNodeAppsPath(nodeHome, true);
				String appFolderName = ProgramUtil.deriveAppFolderName(appId, appVersion);
				Path appFolderPath = appsPath.resolve(appFolderName);
				try {
					// this does not work if folder contains files
					// Files.deleteIfExists(appFolderPath);

					// https://stackoverflow.com/questions/35988192/java-nio-most-concise-recursive-directory-delete
					// Note:
					// Files.walk - return all files/directories below rootPath including
					// .sorted - sort the list in reverse order, so the directory itself comes after the including subdirectories and files
					// .map - path the Path to File
					// .peek - is there only to show which entry is processed
					// .forEach - calls an every File object the .delete() method
					Files.walk(appFolderPath, FileVisitOption.FOLLOW_LINKS) //
							.sorted(Comparator.reverseOrder()) //
							.map(Path::toFile) //
							// .peek(System.out::println) //
							.forEach(java.io.File::delete);

				} catch (IOException e) {
					e.printStackTrace();
					throw new ProgramException(e.getMessage(), e);
				}
			}
			return isUnregistered;
		}
		return false;
	}

	/**
	 * 
	 * @param appId
	 * @param appVersion
	 * @param expectedToBeInstalled
	 * @throws ProgramException
	 */
	protected void checkAppInstalled(String appId, String appVersion, boolean expectedToBeInstalled) throws ProgramException {
		boolean isAppInstalled = isInstalled(appId, appVersion);
		if (isAppInstalled) {
			// app is installed
			if (!expectedToBeInstalled) {
				// app is expected to be not installed.
				throw new ProgramException("App " + appId + "(" + appVersion + ") is already installed.");
			}

		} else {
			// app is not installed
			if (expectedToBeInstalled) {
				// app is expected to be installed.
				throw new ProgramException("App " + appId + "(" + appVersion + ") is not installed.");
			}
		}
	}

	@Override
	public ProgramManifest install(Path appArchivePath) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".install() appArchivePath = " + appArchivePath.toString());
		}
		checkStarted();

		ProgramManifest appManifestFromArchive = ProgramUtil.extractProgramManifest(appArchivePath.toFile());
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

		Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		ProgramManifest appManifest = installer.install(this.bundleContext, appArchivePath);
		if (appManifest == null) {
			if (debug) {
				System.out.println("AppManifest is not returned by installer.");
			}
			return null;
		}

		boolean registered = registerApp(appManifest);
		if (registered) {
			return appManifest;
		}
		return null;
	}

	// @Override
	// public AppManifest install(String appId, String appVersion) throws AppException {
	// if (debug) {
	// System.out.println(getClass().getSimpleName() + ".install() appId = " + appId + ", appVersion = " + appVersion);
	// }
	// checkStarted();
	//
	// // app is expected to be not installed
	// checkAppInstalled(appId, appVersion, false);
	//
	// Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
	// AppManifest appManifest = installer.install(this.bundleContext, appId, appVersion);
	// if (appManifest == null) {
	// if (debug) {
	// System.out.println("AppManifest is not returned by installer.");
	// }
	// return null;
	// }
	//
	// boolean registered = registerApp(appManifest);
	// if (registered) {
	// return appManifest;
	// }
	// return null;
	// }

	@Override
	public ProgramManifest uninstall(String appId, String appVersion) throws ProgramException {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".uninstall() appId = " + appId + ", appVersion = " + appVersion);
		}
		checkStarted();

		// app is expected to be installed
		checkAppInstalled(appId, appVersion, true);

		ProgramManifest appManifest = getInstalledPrograms(appId, appVersion);
		if (appManifest == null) {
			if (debug) {
				System.out.println("App manifest cannot be found in installed apps.");
			}
			return null;
		}

		Installer installer = InstallerProvider.getInstance().getInstaller(this.bundleContext, appId, appVersion);
		boolean uninstalled = installer.uninstall(this.bundleContext, appManifest);
		if (uninstalled) {
			boolean unregistered = unregisterApp(appId, appVersion);
			if (unregistered) {
				return appManifest;
			}
		}
		return null;
	}

	@Override
	public synchronized ProgramHandler[] getProgramHandlers() {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".getAppHandlers()");
		}

		List<ProgramHandler> appHandlers = new ArrayList<ProgramHandler>();
		for (Iterator<ProgramManifest> appManifestItor = this.appHandlersMap.keySet().iterator(); appManifestItor.hasNext();) {
			ProgramManifest currAppManifest = appManifestItor.next();
			ProgramHandler currAppHandler = this.appHandlersMap.get(currAppManifest);
			appHandlers.add(currAppHandler);
		}
		return appHandlers.toArray(new ProgramHandler[appHandlers.size()]);
	}

	@Override
	public ProgramHandler getProgramHandler(String appId, String appVersion) {
		if (debug) {
			System.out.println(getClass().getSimpleName() + ".getAppHandler() appId = " + appId + ", appVersion = " + appVersion);
		}

		ProgramHandler appHandler = null;
		for (Iterator<ProgramManifest> appItor = this.appHandlersMap.keySet().iterator(); appItor.hasNext();) {
			ProgramManifest currAppManifest = appItor.next();
			ProgramHandler currAppHandler = this.appHandlersMap.get(currAppManifest);

			String currAppId = currAppHandler.getManifest().getId();
			String currAppVersion = currAppHandler.getManifest().getVersion();
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

// if (!Files.exists(appFolderPath)) {
// try {
// appFolderPath = Files.createDirectory(appFolderPath);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }

// Path metaInfPath = appFolderPath.resolve(Constants.META_INF);
// if (!Files.exists(metaInfPath)) {
// try {
// Files.createDirectories(metaInfPath);
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
