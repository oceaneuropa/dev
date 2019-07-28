package other.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.account.UserAccountClient;
import org.orbit.component.connector.tier1.account.UserAccountClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.api.tier1.account.UserRegistryConnectorV1;
import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.component.connector.IndexBasedLoadBalancedServiceConnectorImpl;

public class UserRegistryConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<UserAccountClient> implements UserRegistryConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public UserRegistryConnectorImplV1(IndexServiceClient indexService) {
		super(indexService, UserRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.USER_REGISTRY_INDEXER_ID, ComponentConstantsV1.USER_REGISTRY_TYPE);
	}

	@Override
	protected UserAccountClient createService(Map<String, Object> properties) {
		return new UserAccountClientImpl(null, properties);
	}

	@Override
	protected void updateService(UserAccountClient userRegistry, Map<String, Object> properties) {
		userRegistry.update(properties);
	}

}
