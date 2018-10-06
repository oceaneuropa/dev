package org.orbit.infra.api;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";
	public static String ORBIT_EXTENSION_REGISTRY_URL = "orbit.extension_registry.url";
	public static String ORBIT_CHANNEL_SERVICE_URL = "orbit.channel_service.url";

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
	// Channel
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer";
	public static String CHANNEL_TYPE = "Channel";

	// index item properties
	public static String CHANNEL_NAME = "channel.name";
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";

}
