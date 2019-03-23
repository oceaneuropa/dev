package org.orbit.component.runtime.extension.nodecontrol;

import java.util.Map;

import org.orbit.component.api.ComponentConstants;
import org.orbit.component.api.util.ComponentServicesPropertiesHandler;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.util.InfraServicesPropertiesHandler;
import org.orbit.platform.sdk.common.PropertiesRegulator;
import org.origin.common.resources.node.INode;

public class NodeConfigIniPropertiesRegulator implements PropertiesRegulator {

	public static final String ID = "org.orbit.component.runtime.NodeConfigIniPropertiesRegulator";

	@Override
	public void update(Object source, Object target, Map<Object, Object> properties) {
		INode node = null;
		if (target instanceof INode) {
			node = (INode) target;
		}

		if (node != null) {
			// Update properties to be generated in node's config.ini file

			// Add "orbit.index_service.url" property if not exist
			if (!properties.containsKey(InfraConstants.ORBIT_INDEX_SERVICE_URL)) {
				String indexServiceUrl = InfraServicesPropertiesHandler.getInstance().getIndexServiceURL();
				if (indexServiceUrl != null && !indexServiceUrl.isEmpty()) {
					properties.put(InfraConstants.ORBIT_INDEX_SERVICE_URL, indexServiceUrl);
				}
			}

			// Add "orbit.extension_registry.url" property if not exist
			if (!properties.containsKey(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL)) {
				String extensionRegistryUrl = InfraServicesPropertiesHandler.getInstance().getExtensionRegistryURL();
				if (extensionRegistryUrl != null && !extensionRegistryUrl.isEmpty()) {
					properties.put(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL, extensionRegistryUrl);
				}
			}

			// Add "orbit.app_store.url" property if not exist
			if (!properties.containsKey(ComponentConstants.ORBIT_APP_STORE_URL)) {
				String appStoreUrl = ComponentServicesPropertiesHandler.getInstance().getAppStoreURL();
				if (appStoreUrl != null && !appStoreUrl.isEmpty()) {
					properties.put(ComponentConstants.ORBIT_APP_STORE_URL, appStoreUrl);
				}
			}
		}
	}

}
