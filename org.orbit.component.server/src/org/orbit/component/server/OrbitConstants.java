package org.orbit.component.server;

public class OrbitConstants {

	// ----------------------------------------------------------------------------------------
	// Common
	// ----------------------------------------------------------------------------------------
	// property names
	public static String OSGI_HTTP_PORT_PROP = "org.osgi.service.http.port"; // OSGi
	public static String ORBIT_HTTP_HOST_PROP = "orbit.service.http.host";
	public static String ORBIT_HTTP_PORT_PROP = "orbit.service.http.port";
	public static String ORBIT_HTTP_CONTEXTROOT_PROP = "orbit.service.http.contextroot";

	// default values
	public static String ORBIT_DEFAULT_CONTEXT_ROOT = "/orbit/v1";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// property names
	public static String COMPONENT_INDEX_SERVICE_URL_PROP = "component.indexservice.url";

	// ----------------------------------------------------------------------------------------
	// IndexItem
	// ----------------------------------------------------------------------------------------
	// constants for index item property names
	public static String INDEX_ITEM_URL_PROP = "url";
	public static String INDEX_ITEM_CONTEXT_ROOT_PROP = "context_root";
	public static String INDEX_ITEM_LAST_HEARTBEAT_TIME_PROP = "last_heartbeat_time";

	// ----------------------------------------------------------------------------------------
	// AppStore
	// ----------------------------------------------------------------------------------------
	// property names
	public static String COMPONENT_APP_STORE_NAME_PROP = "component.appstore.name";
	public static String COMPONENT_APP_STORE_CONTEXT_ROOT_PROP = "component.appstore.contextroot";
	public static String COMPONENT_APP_STORE_JDBC_DRIVER_PROP = "component.appstore.jdbc.driver";
	public static String COMPONENT_APP_STORE_JDBC_URL_PROP = "component.appstore.jdbc.url";
	public static String COMPONENT_APP_STORE_JDBC_USERNAME_PROP = "component.appstore.jdbc.username";
	public static String COMPONENT_APP_STORE_JDBC_PASSWORD_PROP = "component.appstore.jdbc.password";

	// values for indexing
	public static String APP_STORE_INDEXER_ID = "component.appstore.indexer"; // index provider id for AppStore
	public static String APP_STORE_TYPE = "AppStore"; // type of index item for AppStore

	// ----------------------------------------------------------------------------------------
	// ConfigRegistry
	// ----------------------------------------------------------------------------------------
	// property names
	public static String COMPONENT_CONFIG_REGISTRY_NAME_PROP = "component.configregistry.name";
	public static String COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT_PROP = "component.configregistry.contextroot";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER_PROP = "component.configregistry.jdbc.driver";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_URL_PROP = "component.configregistry.jdbc.url";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME_PROP = "component.configregistry.jdbc.username";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD_PROP = "component.configregistry.jdbc.password";

	// values for indexing
	public static String CONFIG_REGISTRY_INDEXER_ID = "component.configregistry.indexer"; // index provider id for ConfigRegistry
	public static String CONFIG_REGISTRY_TYPE = "ConfigRegistry"; // type of index item for ConfigRegistry

}
