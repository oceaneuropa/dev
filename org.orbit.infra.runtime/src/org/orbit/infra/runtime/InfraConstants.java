package org.orbit.infra.runtime;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_HOST_URL = "orbit.host.url";
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";

	// global index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";
	public static String PINGABLE = "pingable";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_INDEX_SERVICE_AUTOSTART = "component.index_service.autostart";
	public static String COMPONENT_INDEX_SERVICE_HOST_URL = "component.index_service.host.url";
	public static String COMPONENT_INDEX_SERVICE_NAME = "component.index_service.name";
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

	// ----------------------------------------------------------------------------------------
	// Channel
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_CHANNEL_AUTOSTART = "component.channel.autostart";
	public static String COMPONENT_CHANNEL_HOST_URL = "component.channel.host.url";
	public static String COMPONENT_CHANNEL_NAME = "component.channel.name";
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

	// ----------------------------------------------------------------------------------------
	// Relay
	// ----------------------------------------------------------------------------------------
	public static String COMPONENT_INDEX_SERVICE_RELAY_AUTOSTART = "component.index_service.relay.autostart";
	public static String COMPONENT_INDEX_SERVICE_RELAY_NAME = "component.index_service.relay.name";
	public static String COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT = "component.index_service.relay.context_root";
	public static String COMPONENT_INDEX_SERVICE_RELAY_HOSTS = "component.index_service.relay.hosts";
	public static String COMPONENT_INDEX_SERVICE_RELAY_URLS = "component.index_service.relay.urls";

	public static String COMPONENT_CHANNEL_RELAY_AUTOSTART = "component.channel.relay.autostart";
	public static String COMPONENT_CHANNEL_RELAY_NAME = "component.channel.relay.name";
	public static String COMPONENT_CHANNEL_RELAY_CONTEXT_ROOT = "component.channel.relay.context_root";
	public static String COMPONENT_CHANNEL_RELAY_HOSTS = "component.channel.relay.hosts";
	public static String COMPONENT_CHANNEL_RELAY_URLS = "component.channel.relay.urls";

}
