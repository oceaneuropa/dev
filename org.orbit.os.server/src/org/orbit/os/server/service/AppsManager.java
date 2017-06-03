package org.orbit.os.server.service;

import java.io.File;
import java.util.List;

import org.orbit.app.Manifest;

public interface AppsManager {

	public List<Manifest> getAppManifests();

	public Manifest installApp(File appZipFile);

	public boolean uninstallApp(String appId, String appVersion);

}
