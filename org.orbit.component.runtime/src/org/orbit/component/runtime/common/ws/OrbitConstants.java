package org.orbit.component.runtime.common.ws;

public class OrbitConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String REALM = "realm";

	// config properties
	public static String COMPONENT_INDEX_SERVICE_URL = "component.indexservice.url";

	// global config properties
	public static String ORBIT_HOST_URL = "orbit.host.url";

	// global index item properties
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	// public static String PINGABLE = "pingable";

	// ----------------------------------------------------------------------------------------
	// UserRegistry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_USER_REGISTRY_AUTOSTART = "component.userregistry.autostart";
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
	public static String USER_REGISTRY_NAME = "userregistry.name";
	public static String USER_REGISTRY_HOST_URL = "userregistry.host.url";
	public static String USER_REGISTRY_CONTEXT_ROOT = "userregistry.context_root";

	// ----------------------------------------------------------------------------------------
	// OAuth2
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_OAUTH2_AUTOSTART = "component.oauth2.autostart";
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
	public static String OAUTH2_NAME = "oauth2.name";
	public static String OAUTH2_HOST_URL = "oauth2.host.url";
	public static String OAUTH2_CONTEXT_ROOT = "oauth2.context_root";

	// ----------------------------------------------------------------------------------------
	// Auth
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_AUTH_AUTOSTART = "component.auth.autostart";
	public static String COMPONENT_AUTH_NAME = "component.auth.name";
	public static String COMPONENT_AUTH_HOST_URL = "component.auth.host.url";
	public static String COMPONENT_AUTH_CONTEXT_ROOT = "component.auth.context_root";
	public static String COMPONENT_AUTH_TOKEN_SECRET = "component.auth.token_secret";
	// public static String COMPONENT_AUTH_JDBC_DRIVER = "component.auth.jdbc.driver";
	// public static String COMPONENT_AUTH_JDBC_URL = "component.auth.jdbc.url";
	// public static String COMPONENT_AUTH_JDBC_USERNAME = "component.auth.jdbc.username";
	// public static String COMPONENT_AUTH_JDBC_PASSWORD = "component.auth.jdbc.password";

	// index item values
	public static String AUTH_INDEXER_ID = "component.auth.indexer"; // index provider id for auth service
	public static String AUTH_TYPE = "Auth"; // type of index item for auth service

	// index item properties
	public static String AUTH_NAMESPACE = "auth.namespace";
	public static String AUTH_NAME = "auth.name";
	public static String AUTH_HOST_URL = "auth.host.url";
	public static String AUTH_CONTEXT_ROOT = "auth.context_root";

	// ----------------------------------------------------------------------------------------
	// ConfigRegistry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_CONFIG_REGISTRY_AUTOSTART = "component.configregistry.autostart";
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
	public static String CONFIG_REGISTRY_NAME = "configregistry.name";
	public static String CONFIG_REGISTRY_HOST_URL = "configregistry.host.url";
	public static String CONFIG_REGISTRY_CONTEXT_ROOT = "configregistry.context_root";

	// ----------------------------------------------------------------------------------------
	// AppStore
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_APP_STORE_AUTOSTART = "component.appstore.autostart";
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
	public static String APPSTORE_NAME = "appstore.name";
	public static String APPSTORE_HOST_URL = "appstore.host.url";
	public static String APPSTORE_CONTEXT_ROOT = "appstore.context_root";

	// ----------------------------------------------------------------------------------------
	// DomainService
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_DOMAIN_SERVICE_AUTOSTART = "component.domain_service.autostart";
	public static String COMPONENT_DOMAIN_SERVICE_NAME = "component.domain_service.name";
	public static String COMPONENT_DOMAIN_SERVICE_HOST_URL = "component.domain_service.host.url";
	public static String COMPONENT_DOMAIN_SERVICE_CONTEXT_ROOT = "component.domain_service.context_root";
	public static String COMPONENT_DOMAIN_SERVICE_JDBC_DRIVER = "component.domain_service.jdbc.driver";
	public static String COMPONENT_DOMAIN_SERVICE_JDBC_URL = "component.domain_service.jdbc.url";
	public static String COMPONENT_DOMAIN_SERVICE_JDBC_USERNAME = "component.domain_service.jdbc.username";
	public static String COMPONENT_DOMAIN_SERVICE_JDBC_PASSWORD = "component.domain_service.jdbc.password";

	// index item values
	public static String DOMAIN_SERVICE_INDEXER_ID = "component.domain_service.indexer"; // index provider id for DomainService
	public static String DOMAIN_SERVICE_TYPE = "DomainService"; // type of index item for DomainService

	// index item properties
	public static String DOMAIN_SERVICE_NAME = "domain_service.name";
	public static String DOMAIN_SERVICE_HOST_URL = "domain_service.host.url";
	public static String DOMAIN_SERVICE_CONTEXT_ROOT = "domain_service.context_root";

	// ----------------------------------------------------------------------------------------
	// TransferAgent
	// ----------------------------------------------------------------------------------------
	public static String COMPONENT_TRANSFER_AGENT_AUTOSTART = "component.transfer_agent.autostart";
	public static String COMPONENT_TRANSFER_AGENT_NAME = "component.transfer_agent.name";
	public static String COMPONENT_TRANSFER_AGENT_HOST_URL = "component.transfer_agent.host.url";
	public static String COMPONENT_TRANSFER_AGENT_CONTEXT_ROOT = "component.transfer_agent.context_root";
	public static String COMPONENT_TRANSFER_AGENT_HOME = "component.transfer_agent.home";
	public static String COMPONENT_TRANSFER_AGENT_JDBC_DRIVER = "component.transfer_agent.jdbc.driver";
	public static String COMPONENT_TRANSFER_AGENT_JDBC_URL = "component.transfer_agent.jdbc.url";
	public static String COMPONENT_TRANSFER_AGENT_JDBC_USERNAME = "component.transfer_agent.jdbc.username";
	public static String COMPONENT_TRANSFER_AGENT_JDBC_PASSWORD = "component.transfer_agent.jdbc.password";

	// index item values
	public static String TRANSFER_AGENT_INDEXER_ID = "component.transfer_agent.indexer"; // index provider id for TransferAgent service
	public static String TRANSFER_AGENT_TYPE = "TransferAgent"; // type of index item for TransferAgent service

	// index item properties
	public static String TRANSFER_AGENT_NAME = "transfer_agent.name";
	public static String TRANSFER_AGENT_HOST_URL = "transfer_agent.host.url";
	public static String TRANSFER_AGENT_CONTEXT_ROOT = "transfer_agent.context_root";
	public static String TRANSFER_AGENT_HOME = "transfer_agent.home";

}

// property names
// public static String OSGI_HTTP_PORT = "org.osgi.service.http.port"; // OSGi
// public static String ORBIT_HTTP_HOST = "orbit.service.http.host";
// public static String ORBIT_HTTP_PORT = "orbit.service.http.port";
// public static String ORBIT_HTTP_CONTEXTROOT = "orbit.service.http.contextroot";