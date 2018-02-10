package org.orbit.platform.runtime;

public class OSConstants {

	public static final String OS__NAME = "GAIA";
	public static final String OS__VERSION = "1.0.0";

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
	public static String GAIA_HOME = "gaia.home";

	// ----------------------------------------------------------------------------------------
	// OS CommandService
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String COMMAND_SERVICE_INDEXER_ID = "os.command_service.indexer"; // index provider id for OS
	public static String COMMAND_SERVICE_TYPE = "CommandService"; // type of index item for OS

	// index item properties
	public static String COMMAND_SERVICE_NAME = "os.command_service.name";
	public static String COMMAND_SERVICE_CONTEXT_ROOT = "os.command_service.context_root";

}

// config properties
// public static String COMPONENT_NODE_NAMESPACE = "os.namespace";
// public static String COMPONENT_NODE_NAME = "os.name";
// public static String COMPONENT_NODE_HOST_URL = "os.host.url";
// public static String COMPONENT_NODE_CONTEXT_ROOT = "os.context_root";
// public static String COMPONENT_NODE_JDBC_DRIVER = "os.jdbc.driver";
// public static String COMPONENT_NODE_JDBC_URL = "os.jdbc.url";
// public static String COMPONENT_NODE_JDBC_USERNAME = "os.jdbc.username";
// public static String COMPONENT_NODE_JDBC_PASSWORD = "os.jdbc.password";
