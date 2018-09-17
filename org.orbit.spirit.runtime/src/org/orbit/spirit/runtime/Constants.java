package org.orbit.spirit.runtime;

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
	// index item properties
	public static String GAIA__NAME = "gaia.name";
	public static String GAIA__HOST_URL = "gaia.host.url";
	public static String GAIA__CONTEXT_ROOT = "gaia.context_root";
	public static String GAIA__JDBC_DRIVER = "gaia.jdbc.driver";
	public static String GAIA__JDBC_URL = "gaia.jdbc.url";
	public static String GAIA__JDBC_USERNAME = "gaia.jdbc.username";
	public static String GAIA__JDBC_PASSWORD = "gaia.jdbc.password";

	// index item values
	public static String IDX__GAIA__INDEXER_ID = "gaia.indexer"; // index provider id for OS
	public static String IDX__GAIA__TYPE = "GAIA"; // type of index item for OS

	// ---------------------------------------------------------------
	// Service command request constants
	// ---------------------------------------------------------------
	public static String START_SERVICE = "start_service";
	public static String STOP_SERVICE = "stop_service";

}
