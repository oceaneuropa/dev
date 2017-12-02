package org.orbit.os.server.apps;

import java.nio.file.Path;

import org.orbit.app.AppManifest;

public interface Installer {

	/**
	 * Get the id of the installer. Each installer should have an unique id.
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Check whether an app is supported by this installer.
	 * 
	 * @param context
	 * @param appId
	 * @param appVersion
	 * @return
	 */
	boolean isSupported(Object context, String appId, String appVersion);

	/**
	 * Install an app.
	 * 
	 * @param context
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws AppException
	 */
	AppManifest install(Object context, String appId, String appVersion) throws AppException;

	/**
	 * Install an app.
	 * 
	 * @param context
	 * @param appArchivePath
	 * @return
	 * @throws AppException
	 */
	AppManifest install(Object context, Path appArchivePath) throws AppException;

	/**
	 * Uninstall an app.
	 * 
	 * @param context
	 * @param appManifest
	 * @return
	 * @throws AppException
	 */
	boolean uninstall(Object context, AppManifest appManifest) throws AppException;

}
