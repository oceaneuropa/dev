package org.orbit.spirit.connector.gaia;

import java.util.Map;

import org.orbit.spirit.api.gaia.GaiaClient;
import org.orbit.spirit.api.gaia.GaiaServiceMetadata;
import org.orbit.spirit.api.gaia.WorldMetadata;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.ServiceMetadataDTO;

public class GaiaClientImpl extends ServiceClientImpl<GaiaClient, GaiaWSClient> implements GaiaClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public GaiaClientImpl(ServiceConnector<GaiaClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected GaiaWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new GaiaWSClient(config);
	}

	@Override
	public GaiaServiceMetadata getMetadata() throws ClientException {
		GaiaServiceMetadataImpl metadata = new GaiaServiceMetadataImpl();
		ServiceMetadataDTO metadataDTO = getWSClient().getMetadata();
		if (metadataDTO != null) {
			Map<String, Object> properties = metadataDTO.getProperties();
			if (properties != null && !properties.isEmpty()) {
				metadata.getProperties().putAll(properties);
			}
		}
		return metadata;
	}

	@Override
	public WorldMetadata[] getWorlds() throws ClientException {
		return null;
	}

	@Override
	public boolean worldExists(String worldId) throws ClientException {
		return false;
	}

	@Override
	public WorldMetadata getWorld(String worldId) throws ClientException {
		return null;
	}

	@Override
	public WorldMetadata createWorld(String worldId) throws ClientException {
		return null;
	}

	@Override
	public boolean deleteWorld(String worldId) throws ClientException {
		return false;
	}

}
