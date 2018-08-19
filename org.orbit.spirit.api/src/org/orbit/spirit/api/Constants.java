package org.orbit.spirit.api;

import java.io.File;

public class Constants {

	// ----------------------------------------------------------------------------------------
	// Program constants
	// ----------------------------------------------------------------------------------------
	public final static String META_INF = "META-INF"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FILENAME = "manifest.json"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FULLPATH = META_INF + File.separator + APP_MANIFEST_FILENAME; // $NON-NLS-1$

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_GAIA_URL = "orbit.gaia.url";

	// ---------------------------------------------------------------
	// Service command request constants
	// ---------------------------------------------------------------
	public static String START_SERVICE = "start_service";
	public static String STOP_SERVICE = "stop_service";

	// ---------------------------------------------------------------
	// World request constants
	// ---------------------------------------------------------------
	public static String LIST_WORLDS = "list_worlds";
	public static String GET_WORLD = "get_world";
	public static String WORLD_EXIST = "world_exist";
	public static String CREATE_WORLD = "create_world";
	public static String DELETE_WORLD = "delete_world";
	public static String WORLD_STATUS = "world_status";

}
