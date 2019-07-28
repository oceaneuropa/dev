package org.orbit.infra.api;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";
	public static String ORBIT_EXTENSION_REGISTRY_URL = "orbit.extension_registry.url";
	public static String ORBIT_CONFIG_REGISTRY_URL = "orbit.config_registry.url";

	public static String INDEX_ITEM_ID = "index_item_id";

	// global index item properties
	public static String SERVICE__NAMESPACE = "service.namespace";
	public static String SERVICE__ID = "service.id";
	public static String SERVICE__NAME = "service.name";
	public static String SERVICE__HOST_URL = "service.host_url";
	public static String SERVICE__CONTEXT_ROOT = "service.context_root";
	public static String SERVICE__BASE_URL = "service.base_url";
	public static String SERVICE__LAST_HEARTBEAT_TIME = "service.last_heartbeat_time";

	// ----------------------------------------------------------------------------------------
	// Platform
	// ----------------------------------------------------------------------------------------
	public static String PLATFORM_INDEXER_ID = "platform.indexer";
	public static String PLATFORM_INDEXER_TYPE = "Platform";

	// index item property names
	public static String PLATFORM_ID = "platform.id";
	public static String PLATFORM_PARENT_ID = "platform.parent.id";
	public static String PLATFORM_TYPE = "platform.type";

	// index item property values
	public static String PLATFORM_TYPE__NODE = "node";
	public static String PLATFORM_TYPE__SERVER = "server";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------------
	// ExtensionRegistry
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String EXTENSION_REGISTRY_INDEXER_ID = "component.extension_registry.indexer";
	public static String EXTENSION_REGISTRY_TYPE = "ExtensionRegistry";

	// ----------------------------------------------------------------------------------------
	// Config Registry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String CONFIG_REGISTRY__AUTOSTART = "infra.config_registry.autostart";
	public static String CONFIG_REGISTRY__JDBC_DRIVER = "infra.config_registry.jdbc.driver";
	public static String CONFIG_REGISTRY__JDBC_URL = "infra.config_registry.jdbc.url";
	public static String CONFIG_REGISTRY__JDBC_USERNAME = "infra.config_registry.jdbc.username";
	public static String CONFIG_REGISTRY__JDBC_PASSWORD = "infra.config_registry.jdbc.password";

	// index item values
	public static String IDX__CONFIG_REGISTRY__INDEXER_ID = "infra.config_registry.indexer";
	public static String IDX__CONFIG_REGISTRY__TYPE = "ConfigRegistry";

	// EditPolicy values
	public static String CONFIG_REGISTRY__EDITPOLICY_ID = "infra.config_registry.editpolicy";
	public static String CONFIG_REGISTRY__SERVICE_NAME = "infra.config_registry.service";

}
