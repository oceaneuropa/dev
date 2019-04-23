package org.orbit.infra.api;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";
	public static String ORBIT_EXTENSION_REGISTRY_URL = "orbit.extension_registry.url";
	public static String ORBIT_CONFIG_REGISTRY_URL = "orbit.config_registry.url";
	public static String ORBIT_DATACAST_URL = "orbit.data_cast.url";

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

	// ----------------------------------------------------------------------------------------
	// Data Cast
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String DATACAST__AUTOSTART = "infra.data_cast.autostart";
	public static String DATACAST__ID = "infra.data_cast.id";
	public static String DATACAST__JDBC_DRIVER = "infra.data_cast.jdbc.driver";
	public static String DATACAST__JDBC_URL = "infra.dfs.data_cast.url";
	public static String DATACAST__JDBC_USERNAME = "infra.data_cast.jdbc.username";
	public static String DATACAST__JDBC_PASSWORD = "infra.data_cast.jdbc.password";

	// index item values
	public static String IDX__DATACAST__INDEXER_ID = "infra.data_cast.indexer";
	public static String IDX__DATACAST__TYPE = "DataCast";

	// index item properties
	public static String IDX_PROP__DATACAST__ID = "data_cast.id";

	// EditPolicy values
	public static String DATACAST__EDITPOLICY_ID = "infra.data_cast.editpolicy";
	public static String DATACAST__SERVICE_NAME = "infra.data_cast.service";

	// ----------------------------------------------------------------------------------------
	// Data Tube
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String DATATUBE__AUTOSTART = "infra.data_tube.autostart";
	public static String DATATUBE__DATACAST_ID = "infra.data_tube.datacast_id";
	public static String DATATUBE__ID = "infra.data_tube.id";
	public static String DATATUBE__HTTP_PORT = "infra.data_tube.http_port";

	// index item values
	public static String IDX__DATATUBE__INDEXER_ID = "infra.data_tube.indexer";
	public static String IDX__DATATUBE__TYPE = "DataTube";

	// index item properties
	public static String IDX_PROP__DATATUBE__DATACAST_ID = "data_tube.data_cast_id";
	public static String IDX_PROP__DATATUBE__ID = "data_tube.id";
	public static String IDX_PROP__DATATUBE__WEB_SOCKET_PORT = "data_tube.web_socket.port";
	public static String IDX_PROP__DATATUBE__CHANNEL_WEB_SOCKET_URL = "data_tube.channel_web_socket.url";

	// EditPolicy values
	public static String DATATUBE__EDITPOLICY_ID = "infra.data_tube.editpolicy";
	public static String DATATUBE__SERVICE_NAME = "infra.data_tube.service";

}
