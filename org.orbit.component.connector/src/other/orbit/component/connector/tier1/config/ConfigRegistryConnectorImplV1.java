package other.orbit.component.connector.tier1.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.configregistry.ConfigRegistryClient;
import org.orbit.component.connector.tier1.configregistry.ConfigRegistryClientImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexServiceClient;

import other.orbit.component.api.tier1.config.ConfigRegistryConnectorV1;
import other.orbit.component.connector.ComponentConstantsV1;
import other.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class ConfigRegistryConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<ConfigRegistryClient> implements ConfigRegistryConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public ConfigRegistryConnectorImplV1(IndexServiceClient indexService) {
		super(indexService, ConfigRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexServiceClient indexService) throws IOException {
		return indexService.getIndexItems(ComponentConstantsV1.CONFIG_REGISTRY_INDEXER_ID, ComponentConstantsV1.CONFIG_REGISTRY_TYPE);
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
