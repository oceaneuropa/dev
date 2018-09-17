package org.orbit.spirit.runtime;

public class SpiritConstants {

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
	// Configuration property names
	public static String GAIA__AUTOSTART = "spirit.gaia.autostart";
	public static String GAIA__ID = "spirit.gaia.id";
	public static String GAIA__NAME = "spirit.gaia.name";
	public static String GAIA__HOST_URL = "spirit.gaia.host.url";
	public static String GAIA__CONTEXT_ROOT = "spirit.gaia.context_root";
	public static String GAIA__JDBC_DRIVER = "spirit.gaia.jdbc.driver";
	public static String GAIA__JDBC_URL = "spirit.gaia.jdbc.url";
	public static String GAIA__JDBC_USERNAME = "spirit.gaia.jdbc.username";
	public static String GAIA__JDBC_PASSWORD = "spirit.gaia.jdbc.password";

	// EditPolicy values
	public static String GAIA__EDITPOLICY_ID = "spirit.gaia.editpolicy";
	public static String GAIA__SERVICE_NAME = "spirit.gaia.service";

	// Index item properties
	public static String IDX_PROP__GAIA__ID = "gaia.id";
	public static String IDX_PROP__GAIA__NAME = "gaia.name";
	public static String IDX_PROP__GAIA__HOST_URL = "gaia.host.url";
	public static String IDX_PROP__GAIA__CONTEXT_ROOT = "gaia.context_root";

	// index item values
	public static String IDX__GAIA__INDEXER_ID = "gaia.indexer"; // index provider id for OS
	public static String IDX__GAIA__TYPE = "GAIA"; // type of index item for OS

	// ----------------------------------------------------------------------------------------
	// Earth
	// ----------------------------------------------------------------------------------------
	// Configuration property names
	public static String EARTH__AUTOSTART = "spirit.earth.autostart";
	public static String EARTH__GAIA_ID = "spirit.earth.gaia_id";
	public static String EARTH__ID = "spirit.earth.id";
	public static String EARTH__NAME = "spirit.earth.name";
	public static String EARTH__HOST_URL = "spirit.earth.host.url";
	public static String EARTH__CONTEXT_ROOT = "spirit.earth.context_root";
	public static String EARTH__JDBC_DRIVER = "spirit.earth.jdbc.driver";
	public static String EARTH__JDBC_URL = "spirit.earth.jdbc.url";
	public static String EARTH__JDBC_USERNAME = "spirit.earth.jdbc.username";
	public static String EARTH__JDBC_PASSWORD = "spirit.earth.jdbc.password";

	// EditPolicy values
	public static String EARTH__EDITPOLICY_ID = "spirit.earth.editpolicy";
	public static String EARTH__SERVICE_NAME = "spirit.earth.service";

	// Index item properties
	public static String IDX_PROP__EARTH__GAIA_ID = "earth.gaia_id";
	public static String IDX_PROP__EARTH__ID = "earth.id";
	public static String IDX_PROP__EARTH__NAME = "earth.name";
	public static String IDX_PROP__EARTH__HOST_URL = "earth.host.url";
	public static String IDX_PROP__EARTH__CONTEXT_ROOT = "earth.context_root";

	// index item values
	public static String IDX__EARTH__INDEXER_ID = "earth.indexer"; // index provider id for OS
	public static String IDX__EARTH__TYPE = "Earth"; // type of index item for OS

}

// ---------------------------------------------------------------
// Service command request constants
// ---------------------------------------------------------------
// public static String START_SERVICE = "start_service";
// public static String STOP_SERVICE = "stop_service";
