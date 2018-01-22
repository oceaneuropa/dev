package org.orbit.os.api;

import java.io.File;

public class OSConstants {

	// ----------------------------------------------------------------------------------------
	// Program constants
	// ----------------------------------------------------------------------------------------
	public final static String META_INF = "META-INF"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FILENAME = "manifest.json"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FULLPATH = META_INF + File.separator + APP_MANIFEST_FILENAME; // $NON-NLS-1$

	// ----------------------------------------------------------------------------------------
	// Connector property names
	// ----------------------------------------------------------------------------------------
	public static String REALM = "realm";
	public static String USERNAME = "username";
	public static String URL = "url";

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_GAIA_URL = "orbit.gaia.url";

}
