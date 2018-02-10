package org.orbit.platform.runtime.programs;

import java.nio.file.Path;

import org.orbit.os.api.apps.ProgramManifest;
import org.orbit.platform.runtime.programs.ProgramException;

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
	 * @param appArchivePath
	 * @return
	 * @throws ProgramException
	 */
	ProgramManifest install(Object context, Path appArchivePath) throws ProgramException;

	/**
	 * Uninstall an app.
	 * 
	 * @param context
	 * @param appManifest
	 * @return
	 * @throws ProgramException
	 */
	boolean uninstall(Object context, ProgramManifest appManifest) throws ProgramException;

}

/// **
// * Install an app.
// *
// * @param context
// * @param appId
// * @param appVersion
// * @return
// * @throws AppException
// */
// AppManifest install(Object context, String appId, String appVersion) throws AppException;
