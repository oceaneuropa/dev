package org.orbit.infra.model;

import java.util.ArrayList;
import java.util.List;

public class RequestConstants {

	public static String CONFIG_REGISTRY__LIST_CONFIG_REGISTRIES = "list_config_registries";
	public static String CONFIG_REGISTRY__GET_CONFIG_REGISTRY = "get_config_registry";
	public static String CONFIG_REGISTRY__CONFIG_REGISTRY_EXISTS = "config_registry_exists";
	public static String CONFIG_REGISTRY__CREATE_CONFIG_REGISTRY = "create_config_registry";
	public static String CONFIG_REGISTRY__UPDATE_CONFIG_REGISTRY = "update_config_registry";
	public static String CONFIG_REGISTRY__SET_CONFIG_REGISTRY_PROPERTIES = "set_config_registry_properties";
	public static String CONFIG_REGISTRY__REMOVE_CONFIG_REGISTRY_PROPERTIES = "remove_config_registry_properties";
	public static String CONFIG_REGISTRY__DELETE_CONFIG_REGISTRY = "delete_config_registry";

	public static String CONFIG_ELEMENT__LIST_CONFIG_ELEMENTS = "list_config_elements";
	public static String CONFIG_ELEMENT__GET_CONFIG_ELEMENT = "get_config_element";
	public static String CONFIG_ELEMENT__GET_CONFIG_ELEMENT_PATH = "get_config_element_path";
	public static String CONFIG_ELEMENT__CONFIG_ELEMENT_EXISTS = "config_element_exists";
	public static String CONFIG_ELEMENT__CREATE_CONFIG_ELEMENT = "create_config_element";
	public static String CONFIG_ELEMENT__UPDATE_CONFIG_ELEMENT = "update_config_element";
	public static String CONFIG_ELEMENT__SET_CONFIG_ELEMENT_ATTRIBUTE = "set_config_element_attribute";
	public static String CONFIG_ELEMENT__SET_CONFIG_ELEMENT_ATTRIBUTES = "set_config_element_attributes";
	public static String CONFIG_ELEMENT__REMOVE_CONFIG_ELEMENT_ATTRIBUTE = "remove_config_element_attribute";
	public static String CONFIG_ELEMENT__REMOVE_CONFIG_ELEMENT_ATTRIBUTES = "remove_config_element_attributes";
	public static String CONFIG_ELEMENT__DELETE_CONFIG_ELEMENT = "delete_config_element";

	public static String DATACAST__LIST_DATATUBE_CONFIGS = "list_datatube_configs";
	public static String DATACAST__GET_DATATUBE_CONFIG = "get_datatube_config";
	public static String DATACAST__CREATE_DATATUBE_CONFIG = "create_datatube_config";
	public static String DATACAST__UPDATE_DATATUBE_CONFIG = "update_datatube_config";
	public static String DATACAST__SET_DATATUBE_CONFIG_PROPERTIES = "set_datatube_config_properties";
	public static String DATACAST__REMOVE_DATATUBE_CONFIG_PROPERTIES = "remove_datatube_config_properties";
	public static String DATACAST__DELETE_DATATUBE_CONFIG = "delete_datatube_config";

	public static String DATACAST__LIST_CHANNEL_METADATAS = "list_channel_metadatas";
	public static String DATACAST__GET_CHANNEL_METADATA = "get_channel_metadata";
	public static String DATACAST__CHANNEL_METADATA_EXISTS = "channel_metadata_exists";
	public static String DATACAST__CREATE_CHANNEL_METADATA = "create_channel_metadata";
	public static String DATACAST__ALLOCATE_DATA_TUBE_ID = "allocate_data_tube_id";
	public static String DATACAST__UPDATE_CHANNEL_METADATA = "update_channel_metadata";
	public static String DATACAST__SET_CHANNEL_METADATA_STATUS = "set_channel_metadata_status";
	public static String DATACAST__CLEAR_CHANNEL_METADATA_STATUS = "clear_channel_metadata_status";
	public static String DATACAST__SET_CHANNEL_METADATA_ACCOUNT_CONFIGS = "set_channel_metadata_account_configs";
	public static String DATACAST__REMOVE_CHANNEL_METADATA_ACCOUNT_CONFIGS = "remove_channel_metadata_account_configs";
	public static String DATACAST__SET_CHANNEL_METADATA_PROPERTIES = "set_channel_metadata_properties";
	public static String DATACAST__REMOVE_CHANNEL_METADATA_PROPERTIES = "remove_channel_metadata_properties";
	public static String DATACAST__DELETE_CHANNEL_METADATA = "delete_channel_metadata";

	public static String DATATUBE__LIST_RUNTIME_CHANNELS = "list_runtime_channels";
	public static String DATATUBE__GET_RUNTIME_CHANNEL = "get_runtime_channel";
	public static String DATATUBE__RUNTIME_CHANNEL_EXISTS = "runtime_channel_exists";
	public static String DATATUBE__CREATE_RUNTIME_CHANNEL = "create_runtime_channel";
	public static String DATATUBE__DELETE_RUNTIME_CHANNEL = "delete_runtime_channel";
	public static String DATATUBE__RUNTIME_CHANNEL_ON_ACTION = "runtime_channel_on_action";

	public static List<String> RUNTIME_CHANNEL_ACTIONS = new ArrayList<String>();
	public static String RUNTIME_CHANNEL_ACTION__START = "start";
	public static String RUNTIME_CHANNEL_ACTION__SUSPEND = "suspend";
	public static String RUNTIME_CHANNEL_ACTION__STOP = "stop";
	public static String RUNTIME_CHANNEL_ACTION__SYNC = "sync";

	static {
		RUNTIME_CHANNEL_ACTIONS.add(RUNTIME_CHANNEL_ACTION__START);
		RUNTIME_CHANNEL_ACTIONS.add(RUNTIME_CHANNEL_ACTION__SUSPEND);
		RUNTIME_CHANNEL_ACTIONS.add(RUNTIME_CHANNEL_ACTION__STOP);
		RUNTIME_CHANNEL_ACTIONS.add(RUNTIME_CHANNEL_ACTION__SYNC);
	}

	public static String ALLOCATE_TYPE__DATA_TUBE_ID = "data_tube_id";

}

// public static String DATACAST__ADD_CHANNEL_METADATA_ACCOUNT_IDS = "set_channel_metadata_account_ids";
// public static String DATACAST__REMOVE_CHANNEL_METADATA_ACCOUNT_IDS = "remove_channel_metadata_account_ids";
