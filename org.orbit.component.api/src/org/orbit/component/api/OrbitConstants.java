package org.orbit.component.api;

public class OrbitConstants {

	// ----------------------------------------------------------------------------------------
	// Connector property names
	// ----------------------------------------------------------------------------------------
	public static String REALM = "realm";
	public static String USERNAME = "username";
	public static String URL = "url";

	public static String COMPONENT_INDEX_SERVICE_URL = "component.indexservice.url";

	// global index item properties
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";

	// ----------------------------------------------------------------------------------------
	// Channel
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer"; // index provider id for Channel
	public static String CHANNEL_TYPE = "Channel"; // type of index item for Channel

	// index item properties
	public static String CHANNEL_NAMESPACE = "channel.namespace";
	public static String CHANNEL_NAME = "channel.name";
	public static String CHANNEL_HOST_URL = "channel.host.url";
	public static String CHANNEL_CONTEXT_ROOT = "channel.context_root";

	// ----------------------------------------------------------------------------------------
	// UserRegistry
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String USER_REGISTRY_INDEXER_ID = "component.userregistry.indexer"; // index provider id for UserRegistry
	public static String USER_REGISTRY_TYPE = "UserRegistry"; // type of index item for UserRegistry

	// index item properties
	public static String USER_REGISTRY_NAMESPACE = "userregistry.namespace";
	public static String USER_REGISTRY_NAME = "userregistry.name";
	public static String USER_REGISTRY_HOST_URL = "userregistry.host.url";
	public static String USER_REGISTRY_CONTEXT_ROOT = "userregistry.context_root";

	// ----------------------------------------------------------------------------------------
	// Auth
	// ----------------------------------------------------------------------------------------
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
	// index item values
	public static String CONFIG_REGISTRY_INDEXER_ID = "component.configregistry.indexer"; // index provider id for ConfigRegistry
	public static String CONFIG_REGISTRY_TYPE = "ConfigRegistry"; // type of index item for ConfigRegistry

	// index item properties
	public static String CONFIG_REGISTRY_NAMESPACE = "configregistry.namespace";
	public static String CONFIG_REGISTRY_NAME = "configregistry.name";
	public static String CONFIG_REGISTRY_HOST_URL = "configregistry.host.url";
	public static String CONFIG_REGISTRY_CONTEXT_ROOT = "configregistry.context_root";

	// ----------------------------------------------------------------------------------------
	// AppStore
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String APP_STORE_INDEXER_ID = "component.appstore.indexer"; // index provider id for AppStore
	public static String APP_STORE_TYPE = "AppStore"; // type of index item for AppStore

	// index item properties
	public static String APPSTORE_NAMESPACE = "appstore.namespace";
	public static String APPSTORE_NAME = "appstore.name";
	public static String APPSTORE_HOST_URL = "appstore.host.url";
	public static String APPSTORE_CONTEXT_ROOT = "appstore.context_root";

	// ----------------------------------------------------------------------------------------
	// DomainService
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String DOMAIN_SERVICE_INDEXER_ID = "component.domain_service.indexer"; // index provider id for DomainService
	public static String DOMAIN_SERVICE_TYPE = "DomainService"; // type of index item for DomainService

	// index item properties
	public static String DOMAIN_SERVICE_NAMESPACE = "domain_service.namespace";
	public static String DOMAIN_SERVICE_NAME = "domain_service.name";
	public static String DOMAIN_MANAGEMENT_HOST_URL = "domain_service.host.url";
	public static String DOMAIN_MANAGEMENT_CONTEXT_ROOT = "domain_service.context_root";

	// ----------------------------------------------------------------------------------------
	// TransferAgent
	// ----------------------------------------------------------------------------------------
	// index item values
	public static String TRANSFER_AGENT_INDEXER_ID = "component.transfer_agent.indexer"; // index provider id for TransferAgent service
	public static String TRANSFER_AGENT_TYPE = "TransferAgent"; // type of index item for TransferAgent service

	// index item properties
	public static String TRANSFER_AGENT_MACHINE_ID = "transfer_agent.machine_id";
	public static String TRANSFER_AGENT_TRANSFER_AGENT_ID = "transfer_agent.id";
	public static String TRANSFER_AGENT_NAMESPACE = "transfer_agent.namespace";
	public static String TRANSFER_AGENT_NAME = "transfer_agent.name";
	public static String TRANSFER_AGENT_HOST_URL = "transfer_agent.host.url";
	public static String TRANSFER_AGENT_CONTEXT_ROOT = "transfer_agent.context_root";
	public static String TRANSFER_AGENT_HOME = "transfer_agent.home";

}
