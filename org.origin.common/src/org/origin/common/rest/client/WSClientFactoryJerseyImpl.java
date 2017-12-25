package org.origin.common.rest.client;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 * 
 * @see ClientConfiguration#createClient()
 */
public class WSClientFactoryJerseyImpl implements WSClientFactory {

	protected ClientConfig clientConfig;

	@Override
	public Client createClient(Map<String, Object> properties) {
		ClientConfig clientConfig = getJerseyClientConfig();
		Client client = createWSClient(clientConfig);
		return client;
	}

	protected synchronized ClientConfig getJerseyClientConfig() {
		if (this.clientConfig == null) {
			this.clientConfig = createClientConfig();
		}
		return this.clientConfig;
	}

	protected ClientConfig createClientConfig() {
		ClientConfig clientConfig = new ClientConfig();

		clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, new BasicHttpClientConnectionManager());
		clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 10 * 1000);
		clientConfig.property(ClientProperties.READ_TIMEOUT, 10 * 1000);

		// Note:
		// - Set ApacheConnectorProvider to the ClientConfig causes Jersey Inflector post to fail
		// - Wasted 2 hours. Shit!
		// clientConfig.connectorProvider(new ApacheConnectorProvider());

		// Note:
		// - These two features seem to be optional
		// - Jersey Inflector post still works without them (for now).
		// clientConfig.register(JacksonFeature.class);
		// clientConfig.register(MultiPartFeature.class);

		return clientConfig;
	}

	protected Client createWSClient(ClientConfig clientConfig) {
		// Client client = JerseyClientBuilder.createClient(clientConfig);
		// if (hasJacksonFeature) {
		// client.register(JacksonFeature.class);
		// }
		// if (hasMultiPartFeature) {
		// client.register(MultiPartFeature.class);
		// }
		// return client;
		return ClientBuilder.newBuilder().register(MultiPartFeature.class).withConfig(clientConfig).build();
	}

}
