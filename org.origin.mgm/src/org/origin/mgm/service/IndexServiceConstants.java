package org.origin.mgm.service;

public class IndexServiceConstants {

	public static final String INTERNAL_ERROR = "500";

	public static final String ERROR_CODE_INDEX_ITEM_EXIST = "1001";
	public static final String ERROR_CODE_INDEX_ITEM_NOT_FOUND = "2002";
	public static final String ERROR_CODE_INDEX_ITEM_ILLEGAL_ARGUMENTS = "2003";

	// constants for the values related to the index service.
	public static final String EDITING_DOMAIN = "indexservice"; // editing domain name for editing index services
	public static final String INDEX_PROVIDER_ID = "indexservice.index.provider"; // index provider id for index services
	public static final String NAMESPACE = "index.service.node"; // namespace of index item for index services
	public static final String NAME_PREFIX = "node"; // default name of index item for index services
	public static final int DEFAULT_HEARTBEAT_EXPIRE_TIME = 30; // 30 seconds (if no heartbeat for the last 30 seconds, the node is considered as not active)

	// constants for configuration property names
	public static final String CONFIG_PROP_NODE_NAME = "index.service.node.name";
	public static final String CONFIG_PROP_NODE_URL = "index.service.node.url";
	public static final String CONFIG_PROP_NODE_CONTEXT_ROOT = "index.service.node.contextRoot";
	public static final String CONFIG_PROP_NODE_USERNAME = "index.service.node.username";
	public static final String CONFIG_PROP_NODE_PASSWORD = "index.service.node.password";
	public static final String CONFIG_PROP_HEARTBEAT_EXPIRE_TIME = "index.service.heartbeatExpireTime";

	// constants for command names
	public static final String CMD_CREATE_INDEX_ITEM = "create_index_item";
	public static final String CMD_DELETE_INDEX_ITEM = "delete_index_item";
	public static final String CMD_UPDATE_INDEX_ITEM = "update_index_item";

	// constants for index item attribute names
	public static final String IDX_INDEX_ITEM_ID = "indexItemId";
	public static final String IDX_INDEX_PROVIDER_ID = "indexProviderId";
	public static final String IDX_NAMESPACE = "namespace";
	public static final String IDX_NAME = "name";
	public static final String IDX_PROPERTIES = "properties";
	public static final String IDX_CREATE_TIME = "createTime";
	public static final String IDX_LAST_UPDATE_TIME = "lastUpdateTime";

	// constants for index item property names
	public static final String IDX_PROP_URL = "url";
	public static final String IDX_PROP_CONTEXT_ROOT = "contextRoot";
	public static final String IDX_PROP_USERNAME = "username";
	public static final String IDX_PROP_PASSWORD = "password";
	public static final String IDX_PROP_LAST_HEARTBEAT_TIME = "lastHeartbeatTime";

}
