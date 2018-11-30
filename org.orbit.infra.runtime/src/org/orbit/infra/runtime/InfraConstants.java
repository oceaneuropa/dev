package org.orbit.infra.runtime;

public class InfraConstants {

	// ----------------------------------------------------------------------------------------
	// Global
	// ----------------------------------------------------------------------------------------
	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";
	public static String ORBIT_EXTENSION_REGISTRY_URL = "orbit.extension_registry.url";
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

	public static String DATACAST__RELAY_AUTOSTART = "infra.data_cast.relay.autostart";
	public static String DATACAST__RELAY_NAME = "infra.data_cast.relay.name";
	public static String DATACAST__RELAY_CONTEXT_ROOT = "infra.data_cast.relay.context_root";
	public static String DATACAST__RELAY_HOSTS = "infra.data_cast.relay.hosts";
	public static String DATACAST__RELAY_URLS = "infra.data_cast.relay.urls";

	public static String DATATUBE__RELAY_AUTOSTART = "infra.data_tube.relay.autostart";
	public static String DATATUBE__RELAY_NAME = "infra.data_tube.relay.name";
	public static String DATATUBE__RELAY_CONTEXT_ROOT = "infra.data_tube.relay.context_root";
	public static String DATATUBE__RELAY_HOSTS = "infra.data_tube.relay.hosts";
	public static String DATATUBE__RELAY_URLS = "infra.data_tube.relay.urls";

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
	public static String IDX__CONFIG_REGISTRY__INDEXER_ID = "infra.config_registry.indexer";
	public static String IDX__CONFIG_REGISTRY__TYPE = "ConfigRegistry";

	// EditPolicy values
	public static String CONFIG_REGISTRY__EDITPOLICY_ID = "infra.config_registry.editpolicy";
	public static String CONFIG_REGISTRY__SERVICE_NAME = "infra.config_registry.service";

	// ----------------------------------------------------------------------------------------
	// Data Cast
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String DATACAST__AUTOSTART = "infra.data_cast.autostart";
	public static String DATACAST__ID = "infra.data_cast.id";
	public static String DATACAST__NAME = "infra.data_cast.name";
	public static String DATACAST__HOST_URL = "infra.data_cast.host.url";
	public static String DATACAST__CONTEXT_ROOT = "infra.data_cast.context_root";
	public static String DATACAST__JDBC_DRIVER = "infra.data_cast.jdbc.driver";
	public static String DATACAST__JDBC_URL = "infra.data_cast.jdbc.url";
	public static String DATACAST__JDBC_USERNAME = "infra.data_cast.jdbc.username";
	public static String DATACAST__JDBC_PASSWORD = "infra.data_cast.jdbc.password";

	// index item values
	public static String IDX__DATACAST__INDEXER_ID = "infra.data_cast.indexer";
	public static String IDX__DATACAST__TYPE = "DataCast";

	// index item properties
	public static String IDX_PROP__DATACAST__ID = "data_cast.id";

	// EditPolicy values
	public static String DATACAST__EDITPOLICY_ID = "infra.data_cast.editpolicy";
	public static String DATACAST__SERVICE_NAME = "infra.data_cast.service";

	// ----------------------------------------------------------------------------------------
	// Data Tube
	// ----------------------------------------------------------------------------------------
	// config properties
	public static String DATATUBE__AUTOSTART = "infra.data_tube.autostart";
	public static String DATATUBE__DATACAST_ID = "infra.data_tube.datacast_id";
	public static String DATATUBE__ID = "infra.data_tube.id";
	public static String DATATUBE__NAME = "infra.data_tube.name";
	public static String DATATUBE__HOST_URL = "infra.data_tube.host.url";
	public static String DATATUBE__CONTEXT_ROOT = "infra.data_tube.context_root";
	public static String DATATUBE__HTTP_PORT = "infra.data_tube.http_port";

	// index item values
	public static String IDX__DATATUBE__INDEXER_ID = "infra.data_tube.indexer";
	public static String IDX__DATATUBE__TYPE = "DataTube";

	// index item properties
	public static String IDX_PROP__DATATUBE__DATACAST_ID = "data_tube.data_cast_id";
	public static String IDX_PROP__DATATUBE__ID = "data_tube.id";
	public static String IDX_PROP__DATATUBE__WEB_SOCKET_HTTP_PORT = "data_tube.web_socket_http_port";

	// EditPolicy values
	public static String DATATUBE__EDITPOLICY_ID = "infra.data_tube.editpolicy";
	public static String DATATUBE__SERVICE_NAME = "infra.data_tube.service";

	// ----------------------------------------------------------------------------------------
	// Ranking targets
	// ----------------------------------------------------------------------------------------
	public static String RANKING_PROVIDER__DATA_TUBE_NODES__NEW_CHANNEL = "data_tube_nodes_ranking__new_channel";

}
