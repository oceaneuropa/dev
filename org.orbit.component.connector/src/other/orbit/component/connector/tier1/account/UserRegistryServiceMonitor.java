package other.orbit.component.connector.tier1.account;

import java.io.IOException;
import java.util.List;

import org.orbit.component.connector.ComponentConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

import other.orbit.infra.api.indexes.ServiceMonitor;

public class UserRegistryServiceMonitor extends ServiceMonitor {

	public UserRegistryServiceMonitor(IndexService indexService) {
		super(indexService);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstants.USER_REGISTRY_INDEXER_ID, ComponentConstants.USER_REGISTRY_TYPE);
	}

}
