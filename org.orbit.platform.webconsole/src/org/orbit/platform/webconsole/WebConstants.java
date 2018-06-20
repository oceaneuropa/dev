package org.orbit.platform.webconsole;

public class WebConstants {

	public static String ORBIT_INDEX_SERVICE_URL = "orbit.index_service.url";

	public static final String PLATFORM_WEB_CONSOLE_AUTOSTART = "platform.web_console.autostart";
	public static final String PLATFORM_WEB_CONSOLE_CONTEXT_ROOT = "platform.web_console.context_root";
	public static final String COMPONENT_WEB_CONSOLE_CONTEXT_ROOT = "component.web_console.context_root";

	public static String INDEX_SERVICE_INDEXER_ID = "component.index_service.indexer"; // index provider id for IndexService
	public static String CHANNEL_INDEXER_ID = "component.channel.indexer"; // index provider id for ChannelService

	public static String PLATFORM_INDEXER_ID = "platform.indexer"; // index provider id for Platform

	public static String USER_REGISTRY_INDEXER_ID = "component.user_registry.indexer"; // index provider id for UserRegistry
	public static String OAUTH2_INDEXER_ID = "component.oauth2.indexer"; // index provider id for OAuth2 service
	public static String AUTH_INDEXER_ID = "component.auth.indexer"; // index provider id for auth service
	public static String CONFIG_REGISTRY_INDEXER_ID = "component.config_registry.indexer"; // index provider id for ConfigRegistry
	public static String APP_STORE_INDEXER_ID = "component.app_store.indexer"; // index provider id for AppStore
	public static String DOMAIN_SERVICE_INDEXER_ID = "component.domain_management.indexer"; // index provider id for DomainService
	public static String NODE_CONTROL_INDEXER_ID = "component.node_control.indexer"; // index provider id for NodeControl
	public static String MISSION_CONTROL_INDEXER_ID = "component.mission_control.indexer"; // index provider id for MissionControl service

	public static String[] INDEXER_IDS;

	static {
		INDEXER_IDS = new String[] { //
				INDEX_SERVICE_INDEXER_ID, //
				CHANNEL_INDEXER_ID, //

				PLATFORM_INDEXER_ID, //

				USER_REGISTRY_INDEXER_ID, //
				OAUTH2_INDEXER_ID, //
				AUTH_INDEXER_ID, //
				CONFIG_REGISTRY_INDEXER_ID, //
				APP_STORE_INDEXER_ID, //
				DOMAIN_SERVICE_INDEXER_ID, //
				NODE_CONTROL_INDEXER_ID, //
				MISSION_CONTROL_INDEXER_ID, //
		};
	}

}
