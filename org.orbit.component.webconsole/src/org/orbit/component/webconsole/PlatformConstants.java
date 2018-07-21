package org.orbit.component.webconsole;

public class PlatformConstants {

	// ----------------------------------------------------------------------------------------
	// Connector property names
	// ----------------------------------------------------------------------------------------
	public static String REALM = "realm";
	public static String USERNAME = "username";
	public static String URL = "url";

	// ----------------------------------------------------------------------------------------
	// orbit service config properties
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_PLATFORM_URL = "orbit.platform.url";

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
	public static String PLATFORM_TYPE__SERVER = "server";

}
