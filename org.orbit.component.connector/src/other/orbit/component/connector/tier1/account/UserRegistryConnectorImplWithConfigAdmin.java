package other.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccounts;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.origin.common.annotation.Dependency;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import other.orbit.component.api.tier1.account.UserRegistryConnectorV1;
import other.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class UserRegistryConnectorImplWithConfigAdmin extends IndexBasedLoadBalancedServiceConnectorImpl<UserAccounts> implements UserRegistryConnectorV1 {

	@Dependency
	protected ConfigurationAdmin configAdmin;

	/**
	 * 
	 * @param indexService
	 */
	public UserRegistryConnectorImplWithConfigAdmin(IndexService indexService) {
		super(indexService, UserRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE);
	}

	@Override
	protected UserAccounts createService(Map<String, Object> properties) {
		try {
			// Unique key to identify a service and Configuration
			String indexItemId = properties.get(org.orbit.infra.api.InfraConstants.INDEX_ITEM_ID).toString(); // "index_item_id"

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
	protected void updateService(UserAccounts userRegistry, Map<String, Object> properties) {
		userRegistry.update(properties);
	}

}
