package other.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.List;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.component.connector.ServiceMonitor;

public class UserRegistryServiceMonitor extends ServiceMonitor {

	public UserRegistryServiceMonitor(IndexServiceClient indexService) {
		super(indexService);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.USER_REGISTRY_INDEXER_ID, ComponentConstantsV1.USER_REGISTRY_TYPE);
	}

}
