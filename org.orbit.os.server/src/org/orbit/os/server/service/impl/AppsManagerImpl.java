package org.orbit.os.server.service.impl;

import java.io.File;
import java.util.List;

import org.orbit.app.Manifest;
import org.orbit.os.server.service.AppsManager;

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

	public AppsManagerImpl() {
	}

	@Override
	public List<Manifest> getAppManifests() {
		return null;
	}

	@Override
	public Manifest installApp(File appZipFile) {
		return null;
	}

	@Override
	public boolean uninstallApp(String appId, String appVersion) {
		return false;
	}

}
