package org.origin.mgm;

public class OriginConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// global config properties
	public static String ORBIT_HOST_URL = "orbit.host.url";
	// global index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";

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
	public static String COMPONENT_INDEX_SERVICE_NAMESPACE = "component.indexservice.namespace";
	public static String COMPONENT_INDEX_SERVICE_NAME = "component.indexservice.name";
	public static String COMPONENT_INDEX_SERVICE_HOST_URL = "component.indexservice.host.url";
	public static String COMPONENT_INDEX_SERVICE_CONTEXT_ROOT = "component.indexservice.context_root";
	public static String COMPONENT_INDEX_SERVICE_JDBC_DRIVER = "component.indexservice.jdbc.driver";
	public static String COMPONENT_INDEX_SERVICE_JDBC_URL = "component.indexservice.jdbc.url";
	public static String COMPONENT_INDEX_SERVICE_JDBC_USERNAME = "component.indexservice.jdbc.username";
	public static String COMPONENT_INDEX_SERVICE_JDBC_PASSWORD = "component.indexservice.jdbc.password";

	// index item properties
	public static String INDEX_SERVICE_NAME = "indexservice.name";
	public static String INDEX_SERVICE_HOST_URL = "indexservice.host.url";
	public static String INDEX_SERVICE_CONTEXT_ROOT = "indexservice.context_root";

	// index item values
	public static String INDEX_SERVICE_INDEXER_ID = "component.indexservice.indexer"; // index provider id for IndexService
	public static String INDEX_SERVICE_TYPE = "IndexService"; // type of index item for IndexService

	// index item attribute names
	public static String INDEX_ITEM_ID_ATTR = "indexItemId";
	public static String INDEX_ITEM_PROVIDER_ID_ATTR = "indexProviderId";
	public static String INDEX_ITEM_TYPE_ATTR = "type";
	public static String INDEX_ITEM_NAME_ATTR = "name";
	public static String INDEX_ITEM_PROPERTIES_ATTR = "properties";
	public static String INDEX_ITEM_CREATE_TIME_ATTR = "createTime";
	public static String INDEX_ITEM_LAST_UPDATE_TIME_ATTR = "lastUpdateTime";

}

// property names
// public static String OSGI_HTTP_PORT_PROP = "org.osgi.service.http.port"; // OSGi
// public static String ORBIT_HTTP_HOST_PROP = "orbit.service.http.host";
// public static String ORBIT_HTTP_PORT_PROP = "orbit.service.http.port";
// public static String ORBIT_HTTP_CONTEXT_ROOT_PROP = "orbit.service.http.contextroot";
// default values
// public static String ORBIT_DEFAULT_CONTEXT_ROOT = "/orbit/v1";
