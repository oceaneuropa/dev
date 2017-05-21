package org.orbit.component.server;

public class OrbitConstants {

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
	// UserRegistry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_USER_REGISTRY_NAME = "component.userregistry.name";
	public static String COMPONENT_USER_REGISTRY_HOST_URL = "component.userregistry.host.url";
	public static String COMPONENT_USER_REGISTRY_CONTEXT_ROOT = "component.userregistry.context_root";
	public static String COMPONENT_USER_REGISTRY_JDBC_DRIVER = "component.userregistry.jdbc.driver";
	public static String COMPONENT_USER_REGISTRY_JDBC_URL = "component.userregistry.jdbc.url";
	public static String COMPONENT_USER_REGISTRY_JDBC_USERNAME = "component.userregistry.jdbc.username";
	public static String COMPONENT_USER_REGISTRY_JDBC_PASSWORD = "component.userregistry.jdbc.password";

	// index item values
	public static String USER_REGISTRY_INDEXER_ID = "component.userregistry.indexer"; // index provider id for UserRegistry
	public static String USER_REGISTRY_TYPE = "UserRegistry"; // type of index item for UserRegistry

	// index item properties
	public static String USER_REGISTRY_HOST_URL = "userregistry.host.url";
	public static String USER_REGISTRY_CONTEXT_ROOT = "userregistry.context_root";
	public static String USER_REGISTRY_NAME = "userregistry.name";

	// ----------------------------------------------------------------------------------------
	// OAuth2
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_OAUTH2_NAME = "component.oauth2.name";
	public static String COMPONENT_OAUTH2_HOST_URL = "component.oauth2.host.url";
	public static String COMPONENT_OAUTH2_CONTEXT_ROOT = "component.oauth2.context_root";
	public static String COMPONENT_OAUTH2_JDBC_DRIVER = "component.oauth2.jdbc.driver";
	public static String COMPONENT_OAUTH2_JDBC_URL = "component.oauth2.jdbc.url";
	public static String COMPONENT_OAUTH2_JDBC_USERNAME = "component.oauth2.jdbc.username";
	public static String COMPONENT_OAUTH2_JDBC_PASSWORD = "component.oauth2.jdbc.password";

	// index item values
	public static String OAUTH2_INDEXER_ID = "component.oauth2.indexer"; // index provider id for OAuth2 service
	public static String OAUTH2_TYPE = "OAuth2"; // type of index item for OAuth2 service

	// index item properties
	public static String OAUTH2_HOST_URL = "oauth2.host.url";
	public static String OAUTH2_CONTEXT_ROOT = "oauth2.context_root";
	public static String OAUTH2_NAME = "oauth2.name";

	// ----------------------------------------------------------------------------------------
	// ConfigRegistry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_CONFIG_REGISTRY_NAME = "component.configregistry.name";
	public static String COMPONENT_CONFIG_REGISTRY_HOST_URL = "component.configregistry.host.url";
	public static String COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT = "component.configregistry.context_root";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER = "component.configregistry.jdbc.driver";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_URL = "component.configregistry.jdbc.url";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME = "component.configregistry.jdbc.username";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD = "component.configregistry.jdbc.password";

	// index item values
	public static String CONFIG_REGISTRY_INDEXER_ID = "component.configregistry.indexer"; // index provider id for ConfigRegistry
	public static String CONFIG_REGISTRY_TYPE = "ConfigRegistry"; // type of index item for ConfigRegistry

	// index item properties
	public static String CONFIG_REGISTRY_HOST_URL = "configregistry.host.url";
	public static String CONFIG_REGISTRY_CONTEXT_ROOT = "configregistry.context_root";
	public static String CONFIG_REGISTRY_NAME = "configregistry.name";

	// ----------------------------------------------------------------------------------------
	// AppStore
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_APP_STORE_NAME = "component.appstore.name";
	public static String COMPONENT_APP_STORE_HOST_URL = "component.appstore.host.url";
	public static String COMPONENT_APP_STORE_CONTEXT_ROOT = "component.appstore.context_root";
	public static String COMPONENT_APP_STORE_JDBC_DRIVER = "component.appstore.jdbc.driver";
	public static String COMPONENT_APP_STORE_JDBC_URL = "component.appstore.jdbc.url";
	public static String COMPONENT_APP_STORE_JDBC_USERNAME = "component.appstore.jdbc.username";
	public static String COMPONENT_APP_STORE_JDBC_PASSWORD = "component.appstore.jdbc.password";

	// index item values
	public static String APP_STORE_INDEXER_ID = "component.appstore.indexer"; // index provider id for AppStore
	public static String APP_STORE_TYPE = "AppStore"; // type of index item for AppStore

	// index item properties
	public static String APPSTORE_HOST_URL = "appstore.host.url";
	public static String APPSTORE_CONTEXT_ROOT = "appstore.context_root";
	public static String APPSTORE_NAME = "appstore.name";

	// ----------------------------------------------------------------------------------------
	// DomainManagement
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_DOMAIN_MANAGEMENT_NAME = "component.domain_mgmt.name";
	public static String COMPONENT_DOMAIN_MANAGEMENT_HOST_URL = "component.domain_mgmt.host.url";
	public static String COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT = "component.domain_mgmt.context_root";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER = "component.domain_mgmt.jdbc.driver";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL = "component.domain_mgmt.jdbc.url";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME = "component.domain_mgmt.jdbc.username";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD = "component.domain_mgmt.jdbc.password";

	// index item values
	public static String DOMAIN_MANAGEMENT_INDEXER_ID = "component.domain_mgmt.indexer"; // index provider id for DomainManagement service
	public static String DOMAIN_MANAGEMENT_TYPE = "DomainManagement"; // type of index item for DomainManagement service

	// index item properties
	public static String DOMAIN_MANAGEMENT_HOST_URL = "domain_mgmt.host.url";
	public static String DOMAIN_MANAGEMENT_CONTEXT_ROOT = "domain_mgmt.context_root";
	public static String DOMAIN_MANAGEMENT_NAME = "domain_mgmt.name";

}

// property names
// public static String OSGI_HTTP_PORT = "org.osgi.service.http.port"; // OSGi
// public static String ORBIT_HTTP_HOST = "orbit.service.http.host";
// public static String ORBIT_HTTP_PORT = "orbit.service.http.port";
// public static String ORBIT_HTTP_CONTEXTROOT = "orbit.service.http.contextroot";