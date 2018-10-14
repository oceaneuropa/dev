package org.orbit.infra.connector.datatube;

import java.util.Map;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.orbit.infra.model.datatube.ChannelMessageDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClientImpl;
import org.origin.common.rest.client.ServiceConnector;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.model.ServiceMetadataDTO;

public class DataTubeClientImpl extends ServiceClientImpl<DataTubeClient, DataTubeWSClient> implements DataTubeClient {

	/**
	 * 
	 * @param connector
	 * @param properties
	 */
	public DataTubeClientImpl(ServiceConnector<DataTubeClient> connector, Map<String, Object> properties) {
		super(connector, properties);
	}

	@Override
	protected DataTubeWSClient createWSClient(Map<String, Object> properties) {
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		return new DataTubeWSClient(config);
	}

	@Override
	public DataTubeServiceMetadata getMetadata() throws ClientException {
		DataTubeServiceMetadataImpl metadata = new DataTubeServiceMetadataImpl();
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
	public boolean send(String channelId, String senderId, String message) throws ClientException {
		try {
			ChannelMessageDTO messageDTO = new ChannelMessageDTO();
			messageDTO.setChannelId(channelId);
			messageDTO.setSenderId(senderId);
			messageDTO.setMessage(message);

			int result = this.client.sendMessage(messageDTO);
			if (result > 0) {
				return true;
			}

		} catch (ClientException e) {
			throw e;
		}
		return false;
	}

}

// Map<String, Object> getProperties();
// void update(Map<String, Object> properties);
// boolean ping();
// boolean close() throws ClientException;
// protected Map<String, Object> properties;
// protected DataTubeWSClient client;
// protected AdaptorSupport adaptorSupport = new AdaptorSupport();
// /**
// *
// * @param connector
// * @param properties
// */
// public DataTubeClientImpl(ServiceConnector<DataTubeClient> connector, Map<String, Object> properties) {
// if (connector != null) {
// adapt(ServiceConnector.class, connector);
// }
// this.properties = checkProperties(properties);
// initClient();
// }
// protected Map<String, Object> checkProperties(Map<String, Object> properties) {
// if (properties == null) {
// properties = new HashMap<String, Object>();
// }
// return properties;
// }
// @Override
// public boolean close() throws ClientException {
// @SuppressWarnings("unchecked")
// ServiceConnector<DataTubeClient> connector = getAdapter(ServiceConnector.class);
// if (connector != null) {
// return connector.close(this);
// }
// return false;
// }
//
// @Override
// public Map<String, Object> getProperties() {
// return this.properties;
// }
//
// @Override
// public void update(Map<String, Object> properties) {
// this.properties = checkProperties(properties);
// initClient();
// }
//
// protected void initClient() {
// WSClientConfiguration config = WSClientConfiguration.create(this.properties);
// this.client = new DataTubeWSClient(config);
// }
//
// @Override
// public boolean ping() {
// return this.client.doPing();
// }
//
// @Override
// public <T> void adapt(Class<T> clazz, T object) {
// this.adaptorSupport.adapt(clazz, object);
// }
//
// @Override
// public <T> void adapt(Class<T>[] classes, T object) {
// this.adaptorSupport.adapt(classes, object);
// }
//
// @Override
// public <T> T getAdapter(Class<T> adapter) {
// return this.adaptorSupport.getAdapter(adapter);
// }
