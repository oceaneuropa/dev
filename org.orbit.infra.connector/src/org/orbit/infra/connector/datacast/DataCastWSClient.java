package org.orbit.infra.connector.datacast;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.infra.connector.util.ClientModelConverter;
import org.orbit.infra.model.RequestConstants;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.util.ResponseUtil;

/*
 * DataCast web service client.
 * 
 * {contextRoot} example:
 * /orbit/v1/datacast
 * 
 */
public class DataCastWSClient extends WSClient {

	public DataCastWSClient(WSClientConfiguration config) {
		super(config);
	}

	/**
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/allocate?type={type}
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String allocateDataTubeId() throws ClientException {
		String dataTubeId = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("allocate");
			target = target.queryParam("type", RequestConstants.ALLOCATE_TYPE__DATA_TUBE_ID);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			dataTubeId = ClientModelConverter.DATA_CAST.getDataTubeId(response);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return dataTubeId;
	}

}
