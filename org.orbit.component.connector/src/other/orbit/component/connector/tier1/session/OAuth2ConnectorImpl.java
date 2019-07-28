package other.orbit.component.connector.tier1.session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.session.OAuth2Client;
import org.orbit.component.connector.tier1.session.OAuth2ClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.api.tier1.session.OAuth2ConnectorV1;
import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.component.connector.IndexBasedLoadBalancedServiceConnectorImpl;

public class OAuth2ConnectorImpl extends IndexBasedLoadBalancedServiceConnectorImpl<OAuth2Client> implements OAuth2ConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public OAuth2ConnectorImpl(IndexServiceClient indexService) {
		super(indexService, OAuth2ConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.OAUTH2_INDEXER_ID, ComponentConstantsV1.OAUTH2_TYPE);
	}

	@Override
	protected OAuth2Client createService(Map<String, Object> properties) {
		return new OAuth2ClientImpl(null, properties);
	}

	@Override
	protected void updateService(OAuth2Client oauth2, Map<String, Object> properties) {
		oauth2.update(properties);
	}

}
