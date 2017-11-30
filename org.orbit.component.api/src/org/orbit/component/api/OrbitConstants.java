package org.orbit.component.api;

public class OrbitConstants {

	public static String COMPONENT_INDEX_SERVICE_URL = "component.indexservice.url";

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

	public static class Requests {
		// ---------------------------------------------------------------
		// Management request constants
		// ---------------------------------------------------------------
		// Constants for machine configuration requests
		public static String GET_MACHINE_CONFIGS = "get_machine_configs";
		public static String GET_MACHINE_CONFIG = "get_machine_config";
		public static String ADD_MACHINE_CONFIG = "add_machine_config";
		public static String UPDATE_MACHINE_CONFIG = "update_machine_config";
		public static String REMOVE_MACHINE_CONFIG = "remove_machine_config";

		// Constants for transfer agent requests
		public static String GET_TA_CONFIGS = "get_ta_configs";
		public static String GET_TA_CONFIG = "get_ta_config";
		public static String ADD_TA_CONFIG = "add_ta_config";
		public static String UPDATE_TA_CONFIG = "update_ta_config";
		public static String REMOVE_TA_CONFIG = "remove_ta_config";

		// Constants for node requests
		public static String GET_NODE_CONFIGS = "get_node_configs";
		public static String GET_NODE_CONFIG = "get_node_config";
		public static String ADD_NODE_CONFIG = "add_node_config";
		public static String UPDATE_NODE_CONFIG = "update_node_config";
		public static String REMOVE_NODE_CONFIG = "remove_node_config";

		// ---------------------------------------------------------------
		// TA request constants
		// ---------------------------------------------------------------
		// Constants for nodespaces
		public static String GET_NODESPACES = "get_nodespaces";
		public static String GET_NODESPACE = "get_nodespace";
		public static String NODESPACE_EXIST = "nodespace_exist";
		public static String CREATE_NODESPACE = "create_nodespace";
		public static String DELETE_NODESPACE = "delete_nodespace";

		// Constants for nodes
		public static String GET_NODES = "get_nodes";
		public static String GET_NODE = "get_node";
		public static String NODE_EXIST = "node_exist";
		public static String CREATE_NODE = "create_node";
		public static String DELETE_NODE = "delete_node";
		public static String START_NODE = "start_node";
		public static String STOP_NODE = "stop_node";
	}

}
