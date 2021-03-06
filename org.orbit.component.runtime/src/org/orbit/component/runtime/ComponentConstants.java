package org.orbit.component.runtime;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public interface ComponentConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String OSGI_SERVICE_HTTP_PORT = "org.osgi.service.http.port";
	public static String ORBIT_HOST_URL = "orbit.host.url";

	// global index item properties
	public static String TOKEN_PROVIDER__ORBIT = "orbit";

	// ----------------------------------------------------------------------------------------
	// Platform
	// ----------------------------------------------------------------------------------------
	public static String PLATFORM_HOME = "platform.home";
	public static String NODESPACE_LOCATION = "nodespace.location";

	// ----------------------------------------------------------------------------------------
	// Identity Service
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_IDENTITY_NAME = "component.identity.name";
	public static String COMPONENT_IDENTITY_HOST_URL = "component.identity.host.url";
	public static String COMPONENT_IDENTITY_CONTEXT_ROOT = "component.identity.context_root";
	public static String COMPONENT_IDENTITY_JDBC_DRIVER = "component.identity.jdbc.driver";
	public static String COMPONENT_IDENTITY_JDBC_URL = "component.identity.jdbc.url";
	public static String COMPONENT_IDENTITY_JDBC_USERNAME = "component.identity.jdbc.username";
	public static String COMPONENT_IDENTITY_JDBC_PASSWORD = "component.identity.jdbc.password";
	public static String COMPONENT_IDENTITY_AUTOSTART = "component.identity.autostart";
	public static String COMPONENT_IDENTITY_JWT_TOKEN_SECRET = "component.identity.jwt_token_secret";

	// index item values
	public static String IDENTITY_INDEXER_ID = "component.identity.indexer"; // index provider id for Identity service
	public static String IDENTITY_TYPE = "Identity"; // type of index item for Identity service

	// ----------------------------------------------------------------------------------------
	// User Registry Service
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_USER_REGISTRY_NAME = "component.user_registry.name";
	public static String COMPONENT_USER_REGISTRY_HOST_URL = "component.user_registry.host.url";
	public static String COMPONENT_USER_REGISTRY_CONTEXT_ROOT = "component.user_registry.context_root";
	public static String COMPONENT_USER_REGISTRY_JDBC_DRIVER = "component.user_registry.jdbc.driver";
	public static String COMPONENT_USER_REGISTRY_JDBC_URL = "component.user_registry.jdbc.url";
	public static String COMPONENT_USER_REGISTRY_JDBC_USERNAME = "component.user_registry.jdbc.username";
	public static String COMPONENT_USER_REGISTRY_JDBC_PASSWORD = "component.user_registry.jdbc.password";
	public static String COMPONENT_USER_REGISTRY_AUTOSTART = "component.user_registry.autostart";

	// index item values
	public static String USER_REGISTRY_INDEXER_ID = "component.user_registry.indexer"; // index provider id for UserRegistry
	public static String USER_REGISTRY_TYPE = "UserRegistry"; // type of index item for UserRegistry

	// ----------------------------------------------------------------------------------------
	// OAuth2 Service
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_OAUTH2_NAME = "component.oauth2.name";
	public static String COMPONENT_OAUTH2_HOST_URL = "component.oauth2.host.url";
	public static String COMPONENT_OAUTH2_CONTEXT_ROOT = "component.oauth2.context_root";
	public static String COMPONENT_OAUTH2_JDBC_DRIVER = "component.oauth2.jdbc.driver";
	public static String COMPONENT_OAUTH2_JDBC_URL = "component.oauth2.jdbc.url";
	public static String COMPONENT_OAUTH2_JDBC_USERNAME = "component.oauth2.jdbc.username";
	public static String COMPONENT_OAUTH2_JDBC_PASSWORD = "component.oauth2.jdbc.password";
	public static String COMPONENT_OAUTH2_AUTOSTART = "component.oauth2.autostart";

	// index item values
	public static String OAUTH2_INDEXER_ID = "component.oauth2.indexer"; // index provider id for OAuth2 service
	public static String OAUTH2_TYPE = "OAuth2"; // type of index item for OAuth2 service

	// ----------------------------------------------------------------------------------------
	// Auth Service
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_AUTH_NAME = "component.auth.name";
	public static String COMPONENT_AUTH_HOST_URL = "component.auth.host.url";
	public static String COMPONENT_AUTH_CONTEXT_ROOT = "component.auth.context_root";
	public static String COMPONENT_AUTH_TOKEN_SECRET = "component.auth.token_secret";
	public static String COMPONENT_AUTH_AUTOSTART = "component.auth.autostart";

	// index item values
	public static String AUTH_INDEXER_ID = "component.auth.indexer"; // index provider id for auth service
	public static String AUTH_TYPE = "Auth"; // type of index item for auth service

	// ----------------------------------------------------------------------------------------
	// Config Registry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_CONFIG_REGISTRY_NAME = "component.config_registry.name";
	public static String COMPONENT_CONFIG_REGISTRY_HOST_URL = "component.config_registry.host.url";
	public static String COMPONENT_CONFIG_REGISTRY_CONTEXT_ROOT = "component.config_registry.context_root";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_DRIVER = "component.config_registry.jdbc.driver";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_URL = "component.config_registry.jdbc.url";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_USERNAME = "component.config_registry.jdbc.username";
	public static String COMPONENT_CONFIG_REGISTRY_JDBC_PASSWORD = "component.config_registry.jdbc.password";
	public static String COMPONENT_CONFIG_REGISTRY_AUTOSTART = "component.config_registry.autostart";

	// index item values
	public static String CONFIG_REGISTRY_INDEXER_ID = "component.config_registry.indexer"; // index provider id for ConfigRegistry
	public static String CONFIG_REGISTRY_TYPE = "ConfigRegistry"; // type of index item for ConfigRegistry

	// ----------------------------------------------------------------------------------------
	// App Store
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_APP_STORE_NAME = "component.app_store.name";
	public static String COMPONENT_APP_STORE_HOST_URL = "component.app_store.host.url";
	public static String COMPONENT_APP_STORE_CONTEXT_ROOT = "component.app_store.context_root";
	public static String COMPONENT_APP_STORE_JDBC_DRIVER = "component.app_store.jdbc.driver";
	public static String COMPONENT_APP_STORE_JDBC_URL = "component.app_store.jdbc.url";
	public static String COMPONENT_APP_STORE_JDBC_USERNAME = "component.app_store.jdbc.username";
	public static String COMPONENT_APP_STORE_JDBC_PASSWORD = "component.app_store.jdbc.password";
	public static String COMPONENT_APP_STORE_AUTOSTART = "component.app_store.autostart";

	// index item values
	public static String APP_STORE_INDEXER_ID = "component.app_store.indexer"; // index provider id for AppStore
	public static String APP_STORE_TYPE = "AppStore"; // type of index item for AppStore

	// ----------------------------------------------------------------------------------------
	// Domain Management Service
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_DOMAIN_MANAGEMENT_NAME = "component.domain_management.name";
	public static String COMPONENT_DOMAIN_MANAGEMENT_HOST_URL = "component.domain_management.host.url";
	public static String COMPONENT_DOMAIN_MANAGEMENT_CONTEXT_ROOT = "component.domain_management.context_root";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_DRIVER = "component.domain_management.jdbc.driver";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_URL = "component.domain_management.jdbc.url";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_USERNAME = "component.domain_management.jdbc.username";
	public static String COMPONENT_DOMAIN_MANAGEMENT_JDBC_PASSWORD = "component.domain_management.jdbc.password";
	public static String COMPONENT_DOMAIN_MANAGEMENT_AUTOSTART = "component.domain_management.autostart";

	// index item values
	public static String DOMAIN_SERVICE_INDEXER_ID = "component.domain_management.indexer"; // index provider id for DomainService
	public static String DOMAIN_SERVICE_TYPE = "DomainManagement"; // type of index item for DomainService

	// ----------------------------------------------------------------------------------------
	// Node Control
	// ----------------------------------------------------------------------------------------
	public static String COMPONENT_NODE_CONTROL_NAME = "component.node_control.name";
	public static String COMPONENT_NODE_CONTROL_HOST_URL = "component.node_control.host.url";
	public static String COMPONENT_NODE_CONTROL_CONTEXT_ROOT = "component.node_control.context_root";
	public static String COMPONENT_NODE_CONTROL_JDBC_DRIVER = "component.node_control.jdbc.driver";
	public static String COMPONENT_NODE_CONTROL_JDBC_URL = "component.node_control.jdbc.url";
	public static String COMPONENT_NODE_CONTROL_JDBC_USERNAME = "component.node_control.jdbc.username";
	public static String COMPONENT_NODE_CONTROL_JDBC_PASSWORD = "component.node_control.jdbc.password";
	public static String COMPONENT_NODE_CONTROL_AUTOSTART = "component.node_control.autostart";

	// index item values
	public static String NODE_CONTROL_INDEXER_ID = "component.node_control.indexer"; // index provider id for NodeControl
	public static String NODE_CONTROL_TYPE = "NodeControl"; // type of index item for NodeControl

	// index item properties
	public static String NODE_CONTROL_HOME = "node_control.home";

	// EditPolicy values
	public static String NODE_CONTROL__EDITPOLICY_ID = "component.node_control.editpolicy";
	public static String NODE_CONTROL__SERVICE_NAME = "component.node_control.service";

	// ----------------------------------------------------------------------------------------
	// Mission Control
	// ----------------------------------------------------------------------------------------
	public static String COMPONENT_MISSION_CONTROL_NAME = "component.mission_control.name";
	public static String COMPONENT_MISSION_CONTROL_HOST_URL = "component.mission_control.host.url";
	public static String COMPONENT_MISSION_CONTROL_CONTEXT_ROOT = "component.mission_control.context_root";
	public static String COMPONENT_MISSION_CONTROL_JDBC_DRIVER = "component.mission_control.jdbc.driver";
	public static String COMPONENT_MISSION_CONTROL_JDBC_URL = "component.mission_control.jdbc.url";
	public static String COMPONENT_MISSION_CONTROL_JDBC_USERNAME = "component.mission_control.jdbc.username";
	public static String COMPONENT_MISSION_CONTROL_JDBC_PASSWORD = "component.mission_control.jdbc.password";
	public static String COMPONENT_MISSION_CONTROL_AUTOSTART = "component.mission_control.autostart";

	// index item values
	public static String MISSION_CONTROL_INDEXER_ID = "component.mission_control.indexer"; // index provider id for MissionControl service
	public static String MISSION_CONTROL_TYPE = "MissionControl"; // type of index item for MissionControl service

	// index item properties
	public static String MISSION_CONTROL_HOME = "mission_control.home";

}

// property names
// public static String OSGI_HTTP_PORT = "org.osgi.service.http.port"; // OSGi
// public static String ORBIT_HTTP_HOST = "orbit.service.http.host";
// public static String ORBIT_HTTP_PORT = "orbit.service.http.port";
// public static String ORBIT_HTTP_CONTEXTROOT = "orbit.service.http.contextroot";
