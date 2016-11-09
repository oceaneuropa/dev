package org.origin.mgm;

public class OriginConstants {

	// ----------------------------------------------------------------------------------------
	// Common
	// ----------------------------------------------------------------------------------------
	// property names
	public static String OSGI_HTTP_PORT_PROP = "org.osgi.service.http.port"; // OSGi
	public static String ORBIT_HTTP_HOST_PROP = "orbit.service.http.host";
	public static String ORBIT_HTTP_PORT_PROP = "orbit.service.http.port";
	public static String ORBIT_HTTP_CONTEXT_ROOT_PROP = "orbit.service.http.contextroot";

	// default values
	public static String ORBIT_DEFAULT_CONTEXT_ROOT = "/orbit/v1";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// error codes
	public static String ERROR_CODE_INDEX_ITEM_EXIST = "1001";
	public static String ERROR_CODE_INDEX_ITEM_NOT_FOUND = "2002";
	public static String ERROR_CODE_INDEX_ITEM_ILLEGAL_ARGUMENTS = "2003";

	// constants for command names
	public static String CMD_CREATE_INDEX_ITEM = "create_index_item";
	public static String CMD_DELETE_INDEX_ITEM = "delete_index_item";
	public static String CMD_UPDATE_INDEX_ITEM = "update_index_item";

	// constants for the values related to the index service.
	public static String INDEX_SERVICE_EDITING_DOMAIN = "indexservice"; // editing domain name for editing index services
	public static String INDEX_SERVICE_INDEXER_ID = "component.indexservice.indexer"; // index provider id for IndexService
	public static String INDEX_SERVICE_TYPE = "IndexService"; // type of index item for IndexService

	public static int DEFAULT_HEARTBEAT_EXPIRE_TIME = 30; // if no heartbeat for the last 30 seconds, the node is considered as not active.

	// property names
	public static String COMPONENT_INDEX_SERVICE_NAME_PROP = "component.indexservice.name";
	public static String COMPONENT_INDEX_SERVICE_URL_PROP = "component.indexservice.url";
	public static String COMPONENT_INDEX_SERVICE_CONTEXT_ROOT_PROP = "component.indexservice.contextroot";
	public static String COMPONENT_INDEX_SERVICE_JDBC_DRIVER_PROP = "component.indexservice.jdbc.driver";
	public static String COMPONENT_INDEX_SERVICE_JDBC_URL_PROP = "component.indexservice.jdbc.url";
	public static String COMPONENT_INDEX_SERVICE_JDBC_USERNAME_PROP = "component.indexservice.jdbc.username";
	public static String COMPONENT_INDEX_SERVICE_JDBC_PASSWORD_PROP = "component.indexservice.jdbc.password";

	// constants for configuration property names
	public static String CONFIG_SERVICE_HEARTBEAT_EXPIRE_TIME = "index.service.heartbeatExpireTime";

	// ----------------------------------------------------------------------------------------
	// IndexItem
	// ----------------------------------------------------------------------------------------
	// constants for index item attribute names
	public static String INDEX_ITEM_ID_ATTR = "indexItemId";
	public static String INDEX_ITEM_PROVIDER_ID_ATTR = "indexProviderId";
	public static String INDEX_ITEM_TYPE_ATTR = "type";
	public static String INDEX_ITEM_NAME_ATTR = "name";
	public static String INDEX_ITEM_PROPERTIES_ATTR = "properties";
	public static String INDEX_ITEM_CREATE_TIME_ATTR = "createTime";
	public static String INDEX_ITEM_LAST_UPDATE_TIME_ATTR = "lastUpdateTime";

	// constants for index item property names
	public static String INDEX_ITEM_URL_PROP = "url";
	public static String INDEX_ITEM_CONTEXT_ROOT_PROP = "context_root";
	public static String INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP = "last_heartbeat_time";

}
