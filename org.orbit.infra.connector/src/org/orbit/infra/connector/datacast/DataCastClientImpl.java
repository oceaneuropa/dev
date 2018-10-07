package org.orbit.infra.connector.datacast;

import java.util.Map;

import org.orbit.infra.api.datacast.DataCastClient;
import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.ServiceMetadataDTO;

public class DataCastClientImpl extends ServiceClientImpl<DataCastClient, DataCastWSClient> implements DataCastClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DataCastClientImpl(ServiceConnector<DataCastClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DataCastWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new DataCastWSClient(config);
	}

	@Override
	public DataCastServiceMetadata getMetadata() throws ClientException {
		DataCastServiceMetadataImpl metadata = new DataCastServiceMetadataImpl();
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
