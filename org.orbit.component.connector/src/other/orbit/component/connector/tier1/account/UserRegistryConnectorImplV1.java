package other.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccounts;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier1.account.UserRegistryImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

import other.orbit.component.api.tier1.account.UserRegistryConnectorV1;
import other.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class UserRegistryConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<UserAccounts> implements UserRegistryConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public UserRegistryConnectorImplV1(IndexService indexService) {
		super(indexService, UserRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.USER_REGISTRY_INDEXER_ID, OrbitConstants.USER_REGISTRY_TYPE);
	}

	@Override
	protected UserAccounts createService(Map<String, Object> properties) {
		return new UserRegistryImpl(null, properties);
	}

	@Override
	protected void updateService(UserAccounts userRegistry, Map<String, Object> properties) {
		userRegistry.update(properties);
	}

}
