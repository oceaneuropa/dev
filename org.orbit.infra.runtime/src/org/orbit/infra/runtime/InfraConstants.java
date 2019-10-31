package org.orbit.infra.runtime;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_HOST_URL = "orbit.host.url";

	// global index item properties
	public static String TOKEN_PROVIDER__ORBIT = "orbit";

	// ----------------------------------------------------------------------------------------
	// IndexService
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_INDEX_SERVICE_AUTOSTART = "component.index_service.autostart";
	public static String COMPONENT_INDEX_SERVICE_HOST_URL = "component.index_service.host.url";
	public static String COMPONENT_INDEX_SERVICE_NAME = "component.index_service.name";
	public static String COMPONENT_INDEX_SERVICE_CONTEXT_ROOT = "component.index_service.context_root";
	public static String COMPONENT_INDEX_SERVICE_JDBC_DRIVER = "component.index_service.jdbc.driver";
	public static String COMPONENT_INDEX_SERVICE_JDBC_URL = "component.index_service.jdbc.url";
	public static String COMPONENT_INDEX_SERVICE_JDBC_USERNAME = "component.index_service.jdbc.username";
	public static String COMPONENT_INDEX_SERVICE_JDBC_PASSWORD = "component.index_service.jdbc.password";

	// index item values
	public static String INDEX_SERVICE_INDEXER_ID = "component.index_service.indexer"; // index provider id for IndexService
	public static String INDEX_SERVICE_TYPE = "IndexService"; // type of index item for IndexService

	// ----------------------------------------------------------------------------------------
	// Extension Registry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String COMPONENT_EXTENSION_REGISTRY_AUTOSTART = "component.extension_registry.autostart";
	public static String COMPONENT_EXTENSION_REGISTRY_HOST_URL = "component.extension_registry.host.url";
	public static String COMPONENT_EXTENSION_REGISTRY_NAME = "component.extension_registry.name";
	public static String COMPONENT_EXTENSION_REGISTRY_CONTEXT_ROOT = "component.extension_registry.context_root";
	public static String COMPONENT_EXTENSION_REGISTRY_JDBC_DRIVER = "component.extension_registry.jdbc.driver";
	public static String COMPONENT_EXTENSION_REGISTRY_JDBC_URL = "component.extension_registry.jdbc.url";
	public static String COMPONENT_EXTENSION_REGISTRY_JDBC_USERNAME = "component.extension_registry.jdbc.username";
	public static String COMPONENT_EXTENSION_REGISTRY_JDBC_PASSWORD = "component.extension_registry.jdbc.password";

	// index item values
	public static String EXTENSION_REGISTRY_INDEXER_ID = "component.extension_registry.indexer";
	public static String EXTENSION_REGISTRY_TYPE = "ExtensionRegistry";

	// ----------------------------------------------------------------------------------------
	// Relay
	// ----------------------------------------------------------------------------------------
	public static String COMPONENT_INDEX_SERVICE_RELAY_AUTOSTART = "component.index_service.relay.autostart";
	public static String COMPONENT_INDEX_SERVICE_RELAY_NAME = "component.index_service.relay.name";
	public static String COMPONENT_INDEX_SERVICE_RELAY_CONTEXT_ROOT = "component.index_service.relay.context_root";
	public static String COMPONENT_INDEX_SERVICE_RELAY_HOSTS = "component.index_service.relay.hosts";
	public static String COMPONENT_INDEX_SERVICE_RELAY_URLS = "component.index_service.relay.urls";

	public static String COMPONENT_EXTENSION_REGISTRY_RELAY_AUTOSTART = "component.extension_registry.relay.autostart";
	public static String COMPONENT_EXTENSION_REGISTRY_RELAY_NAME = "component.extension_registry.relay.name";
	public static String COMPONENT_EXTENSION_REGISTRY_RELAY_CONTEXT_ROOT = "component.extension_registry.relay.context_root";
	public static String COMPONENT_EXTENSION_REGISTRY_RELAY_HOSTS = "component.extension_registry.relay.hosts";
	public static String COMPONENT_EXTENSION_REGISTRY_RELAY_URLS = "component.extension_registry.relay.urls";

	public static String CONFIG_REGISTRY__RELAY_AUTOSTART = "infra.config_registry.relay.autostart";
	public static String CONFIG_REGISTRY__RELAY_NAME = "infra.config_registry.relay.name";
	public static String CONFIG_REGISTRY__RELAY_CONTEXT_ROOT = "infra.config_registry.relay.context_root";
	public static String CONFIG_REGISTRY__RELAY_HOSTS = "infra.config_registry.relay.hosts";
	public static String CONFIG_REGISTRY__RELAY_URLS = "infra.config_registry.relay.urls";

	// ----------------------------------------------------------------------------------------
	// Config Registry
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String CONFIG_REGISTRY__AUTOSTART = "infra.config_registry.autostart";
	// public static String CONFIG_REGISTRY__ID = "infra.config_registry.id";
	public static String CONFIG_REGISTRY__NAME = "infra.config_registry.name";
	public static String CONFIG_REGISTRY__HOST_URL = "infra.config_registry.host.url";
	public static String CONFIG_REGISTRY__CONTEXT_ROOT = "infra.config_registry.context_root";
	public static String CONFIG_REGISTRY__JDBC_DRIVER = "infra.config_registry.jdbc.driver";
	public static String CONFIG_REGISTRY__JDBC_URL = "infra.config_registry.jdbc.url";
	public static String CONFIG_REGISTRY__JDBC_USERNAME = "infra.config_registry.jdbc.username";
	public static String CONFIG_REGISTRY__JDBC_PASSWORD = "infra.config_registry.jdbc.password";

	// index item values
	public static String CONFIG_REGISTRY__INDEXER_ID = "infra.config_registry.indexer";
	public static String CONFIG_REGISTRY__TYPE = "ConfigRegistry";

	// EditPolicy values
	public static String CONFIG_REGISTRY__EDITPOLICY_ID = "infra.config_registry.editpolicy";
	public static String CONFIG_REGISTRY__SERVICE_NAME = "infra.config_registry.service";

}
