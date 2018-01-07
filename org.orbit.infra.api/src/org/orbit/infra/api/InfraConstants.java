package org.orbit.infra.api;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String REALM = "realm";
	public static String USERNAME = "username";
	public static String URL = "url";

	// global index item properties
	public static String INDEX_ITEM_ID = "index_item_id";

	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	public static String LAST_PING_TIME = "last_ping_time";
	public static String LAST_PING_SUCCEED = "last_ping_succeed";

	// ----------------------------------------------------------------------------------------
	// IndexService/IndexItem
	// ----------------------------------------------------------------------------------------
	// property names
	public static String COMPONENT_INDEX_SERVICE_URL = "component.indexs_ervice.url";

	// index item properties
	public static String INDEX_SERVICE_NAME = "indexservice.name";
	public static String INDEX_SERVICE_HOST_URL = "indexservice.host.url";
	public static String INDEX_SERVICE_CONTEXT_ROOT = "indexservice.context_root";

	// ----------------------------------------------------------------------------------------
	// Channel
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer"; // index provider id for Channel
	public static String CHANNEL_TYPE = "Channel"; // type of index item for Channel

	// index item properties
	public static String CHANNEL_NAME = "channel.name";
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";

}
