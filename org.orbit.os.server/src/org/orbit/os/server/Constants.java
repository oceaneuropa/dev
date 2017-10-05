package org.orbit.os.server;

public class Constants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_INDEX_SERVICE_URL = "component.indexservice.url";
	// global config properties
	public static String ORBIT_HOST_URL = "orbit.host.url";
	// global index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";

	// ----------------------------------------------------------------------------------------
	// Node
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_NODE_NAMESPACE = "component.node.namespace";
	public static String COMPONENT_NODE_NAME = "component.node.name";
	public static String COMPONENT_NODE_HOST_URL = "component.node.host.url";
	public static String COMPONENT_NODE_CONTEXT_ROOT = "component.node.context_root";
	public static String COMPONENT_NODE_JDBC_DRIVER = "component.node.jdbc.driver";
	public static String COMPONENT_NODE_JDBC_URL = "component.node.jdbc.url";
	public static String COMPONENT_NODE_JDBC_USERNAME = "component.node.jdbc.username";
	public static String COMPONENT_NODE_JDBC_PASSWORD = "component.node.jdbc.password";

	// index item values
	public static String NODE_INDEXER_ID = "component.node.indexer"; // index provider id for Node
	public static String NODE_TYPE = "Node"; // type of index item for Node

	// index item properties
	public static String NODE_OS_NAME = "node.os.name";
	public static String NODE_OS_VERSION = "node.os.version";
	public static String NODE_NAME = "node.name";
	public static String NODE_HOST_URL = "node.host.url";
	public static String NODE_CONTEXT_ROOT = "node.context_root";
	public static String NODE_HOME = "node.home";

}
