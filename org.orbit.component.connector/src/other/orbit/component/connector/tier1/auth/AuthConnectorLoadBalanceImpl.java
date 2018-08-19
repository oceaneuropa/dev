package other.orbit.component.connector.tier1.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.connector.ComponentConstants;
import org.orbit.component.connector.tier1.auth.AuthClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

import other.orbit.component.api.tier1.auth.AuthConnectorV0;
import other.orbit.component.api.tier1.auth.AuthConnectorV1;
import other.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class AuthConnectorLoadBalanceImpl extends IndexBasedLoadBalancedServiceConnectorImpl<AuthClient> implements AuthConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public AuthConnectorLoadBalanceImpl(IndexService indexService) {
		super(indexService, AuthConnectorV0.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstants.AUTH_INDEXER_ID, ComponentConstants.AUTH_TYPE);
	}

	@Override
	protected AuthClient createService(Map<String, Object> properties) {
		return new AuthClientImpl(null, properties);
	}

	@Override
	protected void updateService(AuthClient service, Map<String, Object> properties) {
		service.update(properties);
	}

}
