package org.orbit.os.server.app;

import java.util.List;

public class AppInstallerProvider {

	private static AppInstallerProvider INSTANCE;

	public static AppInstallerProvider getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AppInstallerProvider();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param context
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	public AppInstaller getInstaller(Object context, String appId, String appVersion) {
		// 1. Get extension/specialized installers for an app first.
		List<AppInstaller> extensionInstallers = AppInstallerRegistry.getInstance().getExtensionInstallers();
		if (extensionInstallers != null) {
			for (AppInstaller installer : extensionInstallers) {
				if (installer.isSupported(context, appId, appVersion)) {
					return installer;
				}
			}
		}

		// 2. Get system installer for the app later.
		List<AppInstaller> systemInstallers = AppInstallerRegistry.getInstance().getSystemInstallers();
		if (systemInstallers != null) {
			for (AppInstaller installer : systemInstallers) {
				if (installer.isSupported(context, appId, appVersion)) {
					return installer;
				}
			}
		}
		return null;
	}

}
