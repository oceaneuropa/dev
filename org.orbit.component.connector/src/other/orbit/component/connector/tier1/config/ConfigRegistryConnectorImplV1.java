package other.orbit.component.connector.tier1.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.registry.Registry;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.component.connector.tier1.config.ConfigRegistryImpl;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;

import other.orbit.component.api.tier1.config.ConfigRegistryConnectorV1;

import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class ConfigRegistryConnectorImplV1 extends IndexBasedLoadBalancedServiceConnectorImpl<Registry> implements ConfigRegistryConnectorV1 {

	/**
	 * 
	 * @param indexService
	 */
	public ConfigRegistryConnectorImplV1(IndexService indexService) {
		super(indexService, ConfigRegistryConnectorV1.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE);
	}

	@Override
	protected Registry createService(Map<String, Object> properties) {
		return new ConfigRegistryImpl(null, properties);
	}

	@Override
	protected void updateService(Registry configRegistry, Map<String, Object> properties) {
		configRegistry.update(properties);
	}

}
