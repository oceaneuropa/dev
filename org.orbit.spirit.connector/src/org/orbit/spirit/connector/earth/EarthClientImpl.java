package org.orbit.spirit.connector.earth;

import java.util.Map;

import org.orbit.spirit.api.earth.EarthClient;
import org.orbit.spirit.api.earth.EarthServiceMetadata;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.ServiceMetadataDTO;

public class EarthClientImpl extends ServiceClientImpl<EarthClient, EarthWSClient> implements EarthClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public EarthClientImpl(ServiceConnector<EarthClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected EarthWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new EarthWSClient(config);
	}

	@Override
	public EarthServiceMetadata getMetadata() throws ClientException {
		EarthServiceMetadataImpl metadata = new EarthServiceMetadataImpl();
		ServiceMetadataDTO metadataDTO = getWSClient().getMetadata();
		if (metadataDTO != null) {
			Map<String, Object> properties = metadataDTO.getProperties();
			if (properties != null && !properties.isEmpty()) {
				metadata.getProperties().putAll(properties);
			}
		}
		return metadata;
	}

}
