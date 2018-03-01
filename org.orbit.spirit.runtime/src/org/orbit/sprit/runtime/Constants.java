package org.orbit.sprit.runtime;

public class Constants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String ORBIT_HOST_URL = "orbit.host.url";
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";

	// index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";

	// ----------------------------------------------------------------------------------------
	// GAIA
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String GAIA_INDEXER_ID = "gaia.indexer"; // index provider id for OS
	public static String GAIA_TYPE = "GAIA"; // type of index item for OS

	// index item properties
	public static String GAIA_NAME = "gaia.name";
	public static String GAIA_HOST_URL = "gaia.host.url";
	public static String GAIA_CONTEXT_ROOT = "gaia.context_root";

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
