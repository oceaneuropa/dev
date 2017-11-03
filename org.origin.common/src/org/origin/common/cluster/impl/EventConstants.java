package org.origin.common.cluster.impl;

public interface EventConstants {

	// --------------------------------------------------------
	// Event namespaces
	// --------------------------------------------------------
	public static final String CLUSTER_EVENT_GROUP = "cluster.eventgroup";
	public static final String CLUSTER_EVENT_GROUP_PROPERTIES = "cluster.eventgroup.properties";

	// --------------------------------------------------------
	// Event local names
	// --------------------------------------------------------
	// Properties
	public static final String SET_PROPERTIES = "SetProperties";
	public static final String SET_PROPERTY = "SetProperty";
	public static final String REMOVE_PROPERTY = "RemoveProperty";
	public static final String CLEAR_PROPERTIES = "ClearProperties";

	// Property with Map
	public static final String SET_PROPERTY_MAP_PUTALL = "SetProperty_Map_PutAll";
	public static final String SET_PROPERTY_MAP_PUT = "SetProperty_Map_Put";
	public static final String SET_PROPERTY_MAP_REMOVE = "SetProperty_Map_Remove";
	public static final String SET_PROPERTY_MAP_CLEAR = "SetProperty_Map_Clear";

	// Property with List
	public static final String SET_PROPERTY_LIST_ADDALL = "SetProperty_List_AddAll";
	public static final String SET_PROPERTY_LIST_ADD = "SetProperty_List_Add";
	public static final String SET_PROPERTY_LIST_REMOVEALL = "SetProperty_List_RemoveAll";
	public static final String SET_PROPERTY_LIST_REMOVE = "SetProperty_List_Remove";
	public static final String SET_PROPERTY_LIST_CLEAR = "SetProperty_List_Clear";

	// --------------------------------------------------------
	// Args when using Map object
	// --------------------------------------------------------
	// The property names in the map
	public static final String PROP_NAME = "propName";
	public static final String MAP = "map";
	public static final String MAP_KEY = "mapKey";
	public static final String MAP_VALUE = "mapValue";
	public static final String LIST = "list";
	public static final String LIST_ITEM = "listItem";

}
