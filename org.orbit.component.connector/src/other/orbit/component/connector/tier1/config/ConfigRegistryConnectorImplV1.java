package other.orbit.component.connector.tier1.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.connector.ComponentConstants;
import org.orbit.component.connector.tier1.configregistry.ConfigRegistryClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

import other.orbit.component.api.tier1.config.ConfigRegistryConnectorV1;
import other.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class ConfigRegistryConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<ConfigRegistryClient> implements ConfigRegistryConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public ConfigRegistryConnectorImplV1(IndexService indexService) {
		super(indexService, ConfigRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstants.CONFIG_REGISTRY_INDEXER_ID, ComponentConstants.CONFIG_REGISTRY_TYPE);
	}

	@Override
	protected ConfigRegistryClient createService(Map<String, Object> properties) {
		return new ConfigRegistryClientImpl(null, properties);
	}

	@Override
	protected void updateService(ConfigRegistryClient configRegistry, Map<String, Object> properties) {
		configRegistry.update(properties);
	}

}
