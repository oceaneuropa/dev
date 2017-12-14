package org.orbit.component.connector.tier1.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.orbit.component.api.tier1.config.ConfigRegistry;
import org.orbit.component.api.tier1.config.ConfigRegistryConnector;
import org.orbit.component.connector.OrbitConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexService;
import org.orbit.infra.api.indexes.IndexBasedLoadBalancedServiceConnectorImpl;

public class ConfigRegistryConnectorImpl extends IndexBasedLoadBalancedServiceConnectorImpl<ConfigRegistry> implements ConfigRegistryConnector {

	/**
	 * 
	 * @param indexService
	 */
	public ConfigRegistryConnectorImpl(IndexService indexService) {
		super(indexService, ConfigRegistryConnector.class);
	}

	@Override
	protected List<IndexItem> getIndexItems(IndexService indexService) throws IOException {
		return indexService.getIndexItems(OrbitConstants.CONFIG_REGISTRY_INDEXER_ID, OrbitConstants.CONFIG_REGISTRY_TYPE);
	}

	@Override
	protected ConfigRegistry createService(Map<String, Object> properties) {
		return new ConfigRegistryWSImpl(properties);
	}

	@Override
	protected void updateService(ConfigRegistry configRegistry, Map<String, Object> properties) {
		configRegistry.update(properties);
	}

}
