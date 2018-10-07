package org.orbit.infra.api.datacast;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ServiceClient;

public interface DataCastClient extends ServiceClient {

	@Override
	DataCastServiceMetadata getMetadata() throws ClientException;

}
