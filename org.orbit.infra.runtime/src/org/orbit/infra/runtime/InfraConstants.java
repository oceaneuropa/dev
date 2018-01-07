package org.orbit.infra.runtime;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_INDEX_SERVICE_URL = "component.index_service.url";

	// global config properties
	public static String ORBIT_HOST_URL = "orbit.host.url";

	// global index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	public static String PINGABLE = "pingable";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// runtime error codes
	public static String ERROR_CODE_INDEX_ITEM_EXIST = "1001";
	public static String ERROR_CODE_INDEX_ITEM_NOT_FOUND = "2002";
	public static String ERROR_CODE_INDEX_ITEM_ILLEGAL_ARGUMENTS = "2003";

	// runtime command names
	public static String CMD_CREATE_INDEX_ITEM = "create_index_item";
	public static String CMD_DELETE_INDEX_ITEM = "delete_index_item";
	public static String CMD_UPDATE_INDEX_ITEM = "update_index_item";

	// runtime constants
	public static String INDEX_SERVICE_EDITING_DOMAIN = "indexservice"; // editing domain name for editing index services
	public static String CONFIG_SERVICE_HEARTBEAT_EXPIRE_TIME = "index.service.heartbeatExpireTime";
	public static int DEFAULT_HEARTBEAT_EXPIRE_TIME = 30; // if no heartbeat for the last 30 seconds, the node is considered as not active.

	// config properties
	public static String COMPONENT_INDEX_SERVICE_AUTOSTART = "component.index_service.autostart";
	public static String COMPONENT_INDEX_SERVICE_NAME = "component.index_service.name";
	public static String COMPONENT_INDEX_SERVICE_HOST_URL = "component.index_service.host.url";
	public static String COMPONENT_INDEX_SERVICE_CONTEXT_ROOT = "component.index_service.context_root";
	public static String COMPONENT_INDEX_SERVICE_JDBC_DRIVER = "component.index_service.jdbc.driver";
	public static String COMPONENT_INDEX_SERVICE_JDBC_URL = "component.index_service.jdbc.url";
	public static String COMPONENT_INDEX_SERVICE_JDBC_USERNAME = "component.index_service.jdbc.username";
	public static String COMPONENT_INDEX_SERVICE_JDBC_PASSWORD = "component.index_service.jdbc.password";

	// index item properties
	public static String INDEX_SERVICE_NAME = "index_service.name";
	public static String INDEX_SERVICE_HOST_URL = "index_service.host.url";
	public static String INDEX_SERVICE_CONTEXT_ROOT = "index_service.context_root";

	// index item values
	public static String INDEX_SERVICE_INDEXER_ID = "component.index_service.indexer"; // index provider id for IndexService
	public static String INDEX_SERVICE_TYPE = "IndexService"; // type of index item for IndexService

	// index item attribute names
	public static String INDEX_ITEM_ID_ATTR = "indexItemId";
	public static String INDEX_ITEM_PROVIDER_ID_ATTR = "indexProviderId";
	public static String INDEX_ITEM_TYPE_ATTR = "type";
	public static String INDEX_ITEM_NAME_ATTR = "name";
	public static String INDEX_ITEM_PROPERTIES_ATTR = "properties";
	public static String INDEX_ITEM_CREATE_TIME_ATTR = "createTime";
	public static String INDEX_ITEM_LAST_UPDATE_TIME_ATTR = "lastUpdateTime";

	// ----------------------------------------------------------------------------------------
	// Channel
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_CHANNEL_AUTOSTART = "component.channel.autostart";
	public static String COMPONENT_CHANNEL_NAME = "component.channel.name";
	public static String COMPONENT_CHANNEL_HOST_URL = "component.channel.host.url";
	public static String COMPONENT_CHANNEL_CONTEXT_ROOT = "component.channel.context_root";
	public static String COMPONENT_CHANNEL_HTTP_PORT = "component.channel.http_port";

	// index item values
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer"; // index provider id for ChannelService
	public static String CHANNEL_TYPE = "Channel"; // type of index item for ChannelService

	// index item properties
	public static String CHANNEL_NAME = "channel.name";
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";
	public static String CHANNEL_HTTP_PORT = "channel.http_port";

}
