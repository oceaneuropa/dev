package org.orbit.infra.api;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";
	public static String ORBIT_EXTENSION_REGISTRY_URL = "orbit.extension_registry.url";
	// public static String ORBIT_CHANNEL_SERVICE_URL = "orbit.channel_service.url";
	public static String ORBIT_DATACAST_URL = "orbit.data_cast.url";
	public static String ORBIT_DATATUBE_URL = "orbit.data_tube.url";

	// global index item properties
	public static String INDEX_ITEM_ID = "index_item_id";
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";
	public static String LAST_PING_TIME = "last_ping_time";
	public static String LAST_PING_SUCCEED = "last_ping_succeed";

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
	// index item properties
	// public static String INDEX_SERVICE_NAME = "index_service.name";
	public static String INDEX_SERVICE_HOST_URL = "index_service.host.url";
	public static String INDEX_SERVICE_CONTEXT_ROOT = "index_service.context_root";

	// ----------------------------------------------------------------------------------------
	// ExtensionRegistry
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String EXTENSION_REGISTRY_INDEXER_ID = "component.extension_registry.indexer";
	public static String EXTENSION_REGISTRY_TYPE = "ExtensionRegistry";

	// ----------------------------------------------------------------------------------------
	// Data Cast
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String DATACAST__AUTOSTART = "infra.data_cast.autostart";
	public static String DATACAST__ID = "infra.data_cast.id";
	public static String DATACAST__NAME = "infra.data_cast.name";
	public static String DATACAST__HOST_URL = "infra.data_cast.host.url";
	public static String DATACAST__CONTEXT_ROOT = "infra.data_cast.context_root";
	public static String DATACAST__JDBC_DRIVER = "infra.data_cast.jdbc.driver";
	public static String DATACAST__JDBC_URL = "infra.dfs.data_cast.url";
	public static String DATACAST__JDBC_USERNAME = "infra.data_cast.jdbc.username";
	public static String DATACAST__JDBC_PASSWORD = "infra.data_cast.jdbc.password";

	// index item values
	public static String IDX__DATACAST__INDEXER_ID = "infra.data_cast.indexer";
	public static String IDX__DATACAST__TYPE = "DataCast";

	// index item properties
	public static String IDX_PROP__DATACAST__ID = "data_cast.id";
	public static String IDX_PROP__DATACAST__NAME = "data_cast.name";
	public static String IDX_PROP__DATACAST__HOST_URL = "data_cast.host.url";
	public static String IDX_PROP__DATACAST__CONTEXT_ROOT = "data_cast.context_root";

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
	public static String DATATUBE__NAME = "infra.data_tube.name";
	public static String DATATUBE__HOST_URL = "infra.data_tube.host.url";
	public static String DATATUBE__CONTEXT_ROOT = "infra.data_tube.context_root";
	public static String DATATUBE__HTTP_PORT = "infra.data_tube.http_port";

	// index item values
	public static String IDX__DATATUBE__INDEXER_ID = "infra.data_tube.indexer";
	public static String IDX__DATATUBE__TYPE = "DataTube";

	// index item properties
	public static String IDX_PROP__DATATUBE__DATACAST_ID = "data_tube.data_cast_id";
	public static String IDX_PROP__DATATUBE__ID = "data_tube.id";
	public static String IDX_PROP__DATATUBE__NAME = "data_tube.name";
	public static String IDX_PROP__DATATUBE__HOST_URL = "data_tube.host.url";
	public static String IDX_PROP__DATATUBE__CONTEXT_ROOT = "data_tube.context_root";
	public static String IDX_PROP__DATATUBE__HTTP_PORT = "data_tube.http_port";

	// EditPolicy values
	public static String DATATUBE__EDITPOLICY_ID = "infra.data_tube.editpolicy";
	public static String DATATUBE__SERVICE_NAME = "infra.data_tube.service";

}

// ----------------------------------------------------------------------------------------
// Channel
// ----------------------------------------------------------------------------------------
// index item values
// public static String DATATUBE__INDEXER_ID = "component.data_tube.indexer";
// public static String DATATUBE__TYPE = "DataTube";

// index item properties
// public static String IDX_PROP__DATATUBE_NAME = "data_tube.name";
// public static String IDX_PROP__DATATUBE_HOST_URL = "data_tube.host.url";
// public static String IDX_PROP__DATATUBE_CONTEXT_ROOT = "data_tube.context_root";
