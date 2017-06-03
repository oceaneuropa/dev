package org.orbit.os.server.util;

import java.io.File;

import org.orbit.app.installer.AppInstaller;
import org.orbit.app.installer.AppInstallerRegistry;

/**
 * Default installer for installing app to node OS.
 */
public class NodeAppInstaller implements AppInstaller {

	public static final String ID = "NodeAppInstaller";

	public static NodeAppInstaller INSTANCE = null;

	public static NodeAppInstaller getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new NodeAppInstaller();
		}
		return INSTANCE;
	}

	public void start() {
		AppInstallerRegistry.getInstance().registerSystemInstaller(this);
	}

	public void stop() {
		AppInstallerRegistry.getInstance().unregisterSystemInstaller(this);
	}

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public boolean isSupported(String appId, String appVersion) {
		return true;
	}

	@Override
	public boolean install(File appZipFile) {
		return false;
	}

}
