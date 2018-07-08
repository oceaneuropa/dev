package org.orbit.platform.runtime;

import java.io.File;

public class PlatformConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String ORBIT_REALM = "orbit.realm";
	public static String ORBIT_HOST_URL = "orbit.host.url";
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";

	// ----------------------------------------------------------------------------------------
	// Platform
	// ----------------------------------------------------------------------------------------
	public static String PLATFORM_INDEXER_ID = "platform.indexer";
	public static String PLATFORM_INDEXER_TYPE = "Platform";

	// index item property names
	public static String PLATFORM_ID = "platform.id";
	public static String PLATFORM_NAME = "platform.name";
	public static String PLATFORM_VERSION = "platform.version";
	public static String PLATFORM_PARENT_ID = "platform.parent.id";
	public static String PLATFORM_TYPE = "platform.type";
	public static String PLATFORM_HOST_URL = "platform.host.url";
	public static String PLATFORM_CONTEXT_ROOT = "platform.context_root";
	public static String PLATFORM_HOME = "platform.home";
	public static String PLATFORM_RUNTIME_STATE = "platform.runtime_state";

	// index item property values
	public static String PLATFORM_TYPE__NODE = "node";

	// ----------------------------------------------------------------------------------------
	// Platform CommandService
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String COMMAND_SERVICE_INDEXER_ID = "command_service.indexer"; // index provider id for OS
	public static String COMMAND_SERVICE_TYPE = "CommandService"; // type of index item for OS

	// index item properties
	public static String COMMAND_SERVICE_NAME = "command_service.name";
	public static String COMMAND_SERVICE_CONTEXT_ROOT = "command_service.context_root";

	// ----------------------------------------------------------------------------------------
	// Program constants
	// ----------------------------------------------------------------------------------------
	public final static String META_INF = "META-INF"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FILENAME = "manifest.json"; //$NON-NLS-1$
	public final static String APP_MANIFEST_FULLPATH = META_INF + File.separator + APP_MANIFEST_FILENAME; // $NON-NLS-1$

	// ---------------------------------------------------------------
	// Service command request constants
	// ---------------------------------------------------------------
	public static String START_SERVICE = "start_service";
	public static String STOP_SERVICE = "stop_service";
	public static String SHUTDOWN_PLATFORM = "shutdown_platform";

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

// config properties
// public static String COMPONENT_NODE_NAMESPACE = "os.namespace";
// public static String COMPONENT_NODE_NAME = "os.name";
// public static String COMPONENT_NODE_HOST_URL = "os.host.url";
// public static String COMPONENT_NODE_CONTEXT_ROOT = "os.context_root";
// public static String COMPONENT_NODE_JDBC_DRIVER = "os.jdbc.driver";
// public static String COMPONENT_NODE_JDBC_URL = "os.jdbc.url";
// public static String COMPONENT_NODE_JDBC_USERNAME = "os.jdbc.username";
// public static String COMPONENT_NODE_JDBC_PASSWORD = "os.jdbc.password";

//// ----------------------------------------------------------------------------------------
//// GAIA
//// ----------------------------------------------------------------------------------------
//// index item values
// public static String GAIA_INDEXER_ID = "gaia.indexer"; // index provider id for OS
// public static String GAIA_TYPE = "GAIA"; // type of index item for OS
//
//// index item properties
// public static String GAIA_NAME = "gaia.name";
// public static String GAIA_HOST_URL = "gaia.host.url";
// public static String GAIA_CONTEXT_ROOT = "gaia.context_root";
