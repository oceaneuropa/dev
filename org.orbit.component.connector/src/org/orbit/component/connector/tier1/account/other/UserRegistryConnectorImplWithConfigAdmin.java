package org.orbit.component.connector.tier1.account.other;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserRegistry;
import org.orbit.component.api.tier1.account.UserRegistryConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier1.account.UserRegistryManager;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;
import org.origin.common.annotation.Dependency;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class UserRegistryConnectorImplWithConfigAdmin extends IndexBasedLoadBalancedServiceConnectorImpl<UserRegistry> implements UserRegistryConnector {

	@Dependency
	protected ConfigurationAdmin configAdmin;

	/**
	 * 
	 * @param indexService
	 */
	public UserRegistryConnectorImplWithConfigAdmin(IndexService indexService) {
		super(indexService, UserRegistryConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE);
	}

	@Override
	protected UserRegistry createService(Map<String, Object> properties) {
		try {
			// Unique key to identify a service and Configuration
			String indexItemId = properties.get(org.orbit.infra.api.OrbitConstants.INDEX_ITEM_ID).toString(); // "index_item_id"

			Configuration configuration = null;
			// Find existing Configuration with same "index_item_id" property.
			try {
				String filter = "(index_item_id=" + indexItemId + ")";
				Configuration[] configurations = this.configAdmin.listConfigurations(filter);
				if (configurations != null && configurations.length > 0) {
					configuration = configurations[0];
				}
			} catch (InvalidSyntaxException e) {
				e.printStackTrace();
			}
			// If existing Configuration is not found, create a new Configuration for the managed service factory "component.userregistry.manager".
			if (configuration == null) {
				configuration = this.configAdmin.createFactoryConfiguration(UserRegistryManager.MANAGED_SERVICE_FACTORY_PID, null);
			}

			// Use new properties to update the Configuration.
			Hashtable<String, Object> newProperties = new Hashtable<String, Object>();
			newProperties.putAll(properties);
			configuration.update(newProperties);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// return new UserRegistryWSImpl(properties);
		return null;
	}

	@Override
	protected void updateService(UserRegistry userRegistry, Map<String, Object> properties) {
		userRegistry.update(properties);
	}

}
