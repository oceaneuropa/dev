package org.orbit.infra.connector;

public class OrbitConstants {

	public static String REALM = "realm";
	public static String USERNAME = "username";

	// ----------------------------------------------------------------------------------------
	// IndexItem
	// ----------------------------------------------------------------------------------------
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	// property names
	public static String COMPONENT_INDEX_SERVICE_URL_PROP = "component.indexservice.url";

	// ----------------------------------------------------------------------------------------
	// Channel
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer"; // index provider id for Channel
	public static String CHANNEL_TYPE = "Channel"; // type of index item for Channel

	// index item properties
	public static String CHANNEL_NAMESPACE = "channel.namespace";
	public static String CHANNEL_NAME = "channel.name";
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String INDEX_SERVICE_INDEXER_ID = "component.indexservice.indexer"; // index provider id for IndexService
	public static String INDEX_SERVICE_TYPE = "IndexService"; // type of index item for IndexService

	// index item properties
	public static String INDEX_SERVICE_NAME = "indexservice.name";
	public static String INDEX_SERVICE_HOST_URL = "indexservice.host.url";
	public static String INDEX_SERVICE_CONTEXT_ROOT = "indexservice.context_root";

}
