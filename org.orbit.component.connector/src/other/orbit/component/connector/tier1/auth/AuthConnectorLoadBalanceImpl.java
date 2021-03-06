package other.orbit.component.connector.tier1.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.auth.AuthClient;
import org.orbit.component.connector.tier1.auth.AuthClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.api.tier1.auth.AuthConnectorV0;
import other.orbit.component.api.tier1.auth.AuthConnectorV1;
import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.component.connector.IndexBasedLoadBalancedServiceConnectorImpl;

public class AuthConnectorLoadBalanceImpl extends IndexBasedLoadBalancedServiceConnectorImpl<AuthClient> implements AuthConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public AuthConnectorLoadBalanceImpl(IndexServiceClient indexService) {
		super(indexService, AuthConnectorV0.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.AUTH_INDEXER_ID, ComponentConstantsV1.AUTH_TYPE);
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
